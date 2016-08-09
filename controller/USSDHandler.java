package scode.ntxuva.controller;

import java.util.logging.Logger;

import scode.ntxuva.dao.*;
import scode.ntxuva.model.Complaint;
import scode.ntxuva.model.USSDSession;
import scode.ntxuva.model.USSDTransaction;
import scode.ntxuva.sms.Questions;

public class USSDHandler {

	private static Logger logger = Logger.getLogger(USSDHandler.class.getName());
	
	private BairroDAO bairroDAO;
	private ComplaintTypeDAO complaintTypeDAO;
	private ContainerDAO containerDAO;
	private BlockDAO blockDAO;
	private USSDTransactionDAO ussdTransactionDAO;
	private USSDSessionDAO ussdSessionDAO;
	private ComplaintDAO complaintDAO;
	private ComplaintHandler complaintHandler;
	
	private Questions questions;
	private String newline;
	
	
	public USSDHandler() {
		bairroDAO = new BairroDAO();
		complaintTypeDAO = new ComplaintTypeDAO();
		containerDAO = new ContainerDAO();
		blockDAO = new BlockDAO();
		ussdTransactionDAO = new USSDTransactionDAO();
		ussdSessionDAO = new USSDSessionDAO();
		complaintDAO = new ComplaintDAO();
		complaintHandler = new ComplaintHandler();
		//questions = new Questions();
		newline = "\r\n";
	}
	
	public USSDTransaction execute(USSDTransaction request) {
		logger.info(request.toString());
		ussdTransactionDAO.persist(request);
			
		//////////////// BRAINS OF APPLICATION /////////////////////
		USSDTransaction response;
		USSDSession session;
		String answer;
		
		if (request.getIsContinuation().equals("false")) {
			// this is a new session, so create new session & show initial menu
			ussdSessionDAO.persist( 
					new USSDSession(request.getSessionID(), request.getSender()) );
			
			response = new USSDTransaction (request.getID(), 
					request.getSessionID(), request.getReceiver(), 
					request.getSender(), "request", 
					questions.MAIN_MENU.toString());
		} 
		
		else { // this is an existing session, so retrieve 
			// subscriber answer & current session
			answer = request.getText();
			session = ussdSessionDAO.findSession(
					new USSDSession(request.getSessionID(), request.getSender()) );
			
			if (session.getMainMenu() == 0) {
				// collect the subscriber's response from the main menu 
				// & send them next menu
				response = getMainMenuResults(answer, request, session);
				
			} else if (session.getMainMenu() == 2) {
				// the subscriber wants to know about their complaint status
				response = getComplaintStatus(answer, request, session);
		
			} else if (session.getMainMenu() == 1) { 
				// complaint menu 
				logger.info("inside complaint menu");
				
				if (session.getBairro() == 0) { 
					// bairro menu
					response = getBairro(answer, request, session);
					
				} else if (session.getComplaintType() == 0) {
					// complaint type menu
					response = getComplaintType(answer, request, session);
										
				} else if ( (session.getContainer() == 0) && (session.getComplaintType() <= 3) ) {
					// container menu 
					response = getContainer(answer, request, session);
										
				} else if ( (session.getBlock() == 0) && (session.getComplaintType() >= 4) ) {
					// block menu 
					response = getQuarteirao(answer, request, session);
										
				} else if ( (session.getComment() == null) || (!session.getComment().equals("2")) ) {
					// comment menu 
					response = getCommentMenu(answer, request, session);
										
				} else  {
					// collecting comment
					response = getComment(answer, request, session);
				}
							
			} else {
				// catch-all to send error message and close session
				response = invalidAnswer(answer, request, session);
			}
			
		}
						
		logger.info(response.toString());
		ussdTransactionDAO.persist(response);
		return response;
	}
	
	
	private USSDTransaction getMainMenuResults(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getMainMenuResult");
		
		if (answer.equals("1")) { // submit complaint
			logger.info("subscriber wants to submit complaint, "
					+ "send them to the bairro menu");
			sess.setMainMenu(1);
			ussdSessionDAO.persist(sess);
			
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					questions.BAIRRO_MENU.toString() + newline + 
					bairroDAO.printList(newline));	
		} 
		else if (answer.equals("2")) { // get complaint status
			logger.info("subscriber wants to get complaint status, "
					+ "send them to the complaint ID menu");
			sess.setMainMenu(2);
			ussdSessionDAO.persist(sess);
			
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					questions.COMPLAINT_ID.toString() );	
		}  
		else if (answer.equals("3")) { // send MOPA information
			logger.info("subscriber wants to get MOPA info");
			sess.setMainMenu(3);
			ussdSessionDAO.close(sess);
			
			// send SMS (using concatenation)
			complaintHandler.sendInfo(req.getSender(), 
					questions.MOPA_INFO.toString() );
			
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "end", 
					questions.END_MOPA_INFO.toString() );	
		} 
		else { // invalid answer
			return invalidAnswer("main menu = " + answer, req, sess);
		}
	}
	
	
	private USSDTransaction getBairro(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getBairro");
		
		if ( bairroDAO.isValid(answer) ) {
			// collect valid bairro answers & send to complaint type menu
			logger.info("valid bairro answer, send complaint type menu");
			sess.setBairro(Integer.parseInt(answer));
			ussdSessionDAO.persist(sess);
			/*
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					questions.COMPLAINT_MENU.toString() + newline + 
					complaintTypeDAO.printList(newline));
			*/

			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					complaintTypeDAO.printList(newline));
		} 
		else {
			// invalid bairro answers, inform subscriber
			return invalidAnswer("bairro = " + answer, req, sess);
		}
	}
	
	
	private USSDTransaction getComplaintType(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getComplaintType");
		
		if ( complaintTypeDAO.isValid(answer) ) {
			// collect valid complaint type answers 
			// & send to container/quarteirao menu
			sess.setComplaintType(Integer.parseInt(answer));
			ussdSessionDAO.persist(sess);
			
			if ( (answer.equals("1")) || (answer.equals("2")) || (answer.equals("3")) ) {
				logger.info("valid complaint type answer, send to container menu");
				return new USSDTransaction (req.getID(), req.getSessionID(), 
						req.getReceiver(), req.getSender(), "request", 
						questions.CONTAINER_ID.toString() + newline +
						containerDAO.printBairroList(sess.getBairro(), newline) );
			} else {
				logger.info("valid complaint type answer, send to block menu");
				return new USSDTransaction (req.getID(), req.getSessionID(), 
						req.getReceiver(), req.getSender(), "request", 
						questions.BLOCK_NUMBER.toString() );
			}	
		} 
		else {
			// invalid bairro answers, inform subscriber
			return invalidAnswer("complaint type = " + answer, req, sess);
		}
	}
	
	
	private USSDTransaction getContainer(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getContainer");
		
		if ( containerDAO.isValid(sess.getBairro(), answer) ) {
			// collect valid container answers & send to comment menu
			logger.info("valid container answer, send to comment menu");
			// String containerNbr = "" + sess.getBairro() + answer;
			sess.setContainer(Integer.parseInt(answer));
			ussdSessionDAO.persist(sess);
			
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					questions.COMMENT_MENU.toString() );
		} 
		else {
			// invalid bairro answers, inform subscriber
			return invalidAnswer("contentor = " + answer, req, sess);
		}
	}
	
	
	private USSDTransaction getQuarteirao(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getQuarteirao");
		
		if ( blockDAO.isValid(sess.getBairro(), answer) ) {
			// collect valid quarteirao answers & send to comment menu
			logger.info("valid quarteirao answer, send to comment menu");
			// int blockNbr = (1000 * sess.getBairro()) + Integer.parseInt(answer);
			sess.setBlock(Integer.parseInt(answer));
			ussdSessionDAO.persist(sess);
			
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					questions.COMMENT_MENU.toString() );
		}
		else {
			// invalid quarteirao answers, inform subscriber
			return invalidAnswer("quarteirao = " + answer, req, sess);
		}
	}
	
		
	private USSDTransaction getCommentMenu(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getCommentMenu");
		
		if (answer.equals("1")) { // submit complaint
			logger.info("submit complaint as-is");
			sess.setComment("");
			return finishComplaint(req, sess);
		} 
		else if (answer.equals("2")) { // get complaint comment
			logger.info("subscriber wants to include comment");
			sess.setComment("2");
			ussdSessionDAO.persist(sess);
			
			return new USSDTransaction (req.getID(), req.getSessionID(), 
					req.getReceiver(), req.getSender(), "request", 
					questions.COMMENT.toString() );	
		}  
		else { // invalid answer
			return invalidAnswer("comment menu = " + answer, req, sess);
		}
	}
	
	
	private USSDTransaction getComment(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("collect comment & send thanks to subscriber");
		sess.setComment(answer);
		return finishComplaint(req, sess);
	}
	
	
	private USSDTransaction finishComplaint(USSDTransaction req, USSDSession sess) {
		logger.info("submit complaint & send thanks to subscriber");
		ussdSessionDAO.close(sess);
		
		complaintHandler.sendComplaint( new Complaint(req.getSender(), sess) );
		
		return new USSDTransaction (req.getID(), req.getSessionID(), 
				req.getReceiver(), req.getSender(), "end", 
				questions.END_COMMENT.toString() );
	}
		
		
	private USSDTransaction getComplaintStatus(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering getComplaintID");
		sess.setComment(answer);
		ussdSessionDAO.close(sess);
		complaintHandler.sendComplaintStatus(req.getSender(), answer);
		
		return new USSDTransaction (req.getID(), req.getSessionID(), 
				req.getReceiver(), req.getSender(), "end", 
				questions.END_COMPLAINT_ID.toString() );
	}
	
	
	private USSDTransaction invalidAnswer(String answer, 
			USSDTransaction req, USSDSession sess) {
		logger.info("entering 'catch-all' invalidAnswer");
		sess.setComment("invalid answer: " + answer);
		ussdSessionDAO.close(sess);
		
		return new USSDTransaction (req.getID(), req.getSessionID(), 
				req.getReceiver(), req.getSender(), "end", 
				questions.INVALID.toString() );
	}
	
	
}