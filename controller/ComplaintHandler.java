package scode.ntxuva.controller;

import java.util.logging.Logger;

import scode.ntxuva.model.Complaint;
import scode.ntxuva.sms.ComplaintSender;
import scode.ntxuva.sms.HttpSmsSender;
import scode.ntxuva.sms.Questions;
import scode.ntxuva.sms.SendStatus;
import scode.ntxuva.sms.Sms;
import scode.ntxuva.sms.SmsSender;


public class ComplaintHandler {
	
	private static Logger logger = Logger.getLogger(ComplaintHandler.class.getName());
	private SmsSender smsSender;
	private ComplaintSender complaintSender;
	private Questions questions;
	
	public ComplaintHandler() {
		smsSender = new HttpSmsSender();
		complaintSender = new ComplaintSender();
	}
	
	public void sendComplaintStatus(long MSISDN, String complaintID) {
		try {
			String complaintStatusResp = complaintSender.sendGet(complaintID);
			sendSMS( MSISDN, complaintStatusResp );
		} catch (Exception ex) {
			ex.printStackTrace();
			sendSMS(MSISDN, questions.SYSTEM_ERROR.toString());
		}
	}
	
	public void sendComplaint(Complaint complaint) {
		try {
			String complaintIDResp = complaintSender.sendPost(complaint);
			sendSMS( complaint.getMSISDN(), complaintIDResp );
		} catch (Exception ex) {
			ex.printStackTrace();
			sendSMS(complaint.getMSISDN(), questions.SYSTEM_ERROR.toString());
		}
	}
	
	public void sendInfo(long MSISDN, String info) {
		try {
			sendSMS( MSISDN, info );
		} catch (Exception ex) {
			ex.printStackTrace();
			sendSMS(MSISDN, questions.SYSTEM_ERROR.toString());
		}
	}
	
	private void sendSMS(long MSISDN, String text) {
		Sms sms = new Sms();
		//sms.setUsername("foo");
		//sms.setPassword("bar");
		sms.setFrom("ntxuva");
		sms.setTo(Long.toString(MSISDN));
		sms.setText(text);
		try {
			SendStatus status = smsSender.send(sms);
		    logger.info("SMS status " + status.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}