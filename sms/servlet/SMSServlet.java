package scode.ntxuva.sms.servlet;

import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






//Valter: altered below code to use different logging package
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.util.logging.*;

import scode.ntxuva.sms.ComplaintSender;
import scode.ntxuva.sms.HttpSmsSender;
import scode.ntxuva.sms.SendStatus;
import scode.ntxuva.sms.Sms;
import scode.ntxuva.sms.SmsSender;
import scode.ntxuva.sms.UrlTemplate;

// Valter: removed below code as could not find this/similar package
// import org.kannel.xml.*;

/**
 * A sample servlet for receiving SMSs from Kannel.
 *
 * @author garth
 */
public class SMSServlet extends KannelServlet
{
	// Valter: altered below code to use different logging package
	// private static Logger logger = LoggerFactory.getLogger(KannelServlet.class);
	private static Logger logger = Logger.getLogger(SMSServlet.class.getName());
	
	
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
	Sms sms = null;
	//Complaint complaint = null;
	Map headers = getHeaderMap(request);
	Map parameters = getParameterMap(request);
	
	/* Valter - logging for troubleshooting
	logger.info("Valter debugging - parameter names: " + request.getParameterNames());
	logger.info("Valter debugging - query string: " + request.getQueryString());
	logger.info("Valter debugging - content length: " + request.getContentLength());
	logger.info("Valter debugging - content type: " + request.getContentType());
	logger.info("Valter debugging - header map size: " + h.size());
	*/
	
	if (headers.size() > 1) {
	    //X-Kannel header request
	    sms = Sms.buildFromHeaders(headers);
	    sms.setCharset(Charset.forName(request.getCharacterEncoding()));
	    sms.setText(getContent(request.getInputStream()));
	    logger.info("Valter debugging - query string: " + request.getQueryString());
	    //XML post
	  	// Valter: removed below code as could not find this/similar package
	// } else if (request.getContentType().contains("xml")) {
	    // sms = smsFromXml(request.getInputStream());
		// logger.severe("Valter: did not know how to handle xml content type in " + this);
	} else {
	    //parameters
	    UrlTemplate u = new UrlTemplate(null).all();
	    // Valter: request.getParameterMap returns Map<String, String[]>
	    sms = Sms.buildFromTemplate(u, request.getParameterMap());
	}

	logger.info("Received SMS: " + sms.toString() );
	
	PrintWriter out = response.getWriter();
	// out.println("ok");
	
	// Valter TODO: finish this Publish-Subscriber code
	// obs.notifyObservers(sms);
	
	if (sms.getFrom().equals("mopa")) {
		logger.info("SMS is from MOPA so will forward to Kannel -> SMSC");
		try {
			/* SmsSender s = new HttpSmsSender(new URL(kannelURL));
		    sms.setUsername(kannelUser);
		    sms.setPassword(kannelPassword); */
			SmsSender s = new HttpSmsSender();
			SendStatus status = s.send(sms);
		    //System.out.println(status.toString());
		    logger.info("SMS status: " + status.toString());
		    out.println("Message successfully forwarded from MOPA to SMSC");
		} catch (Exception e) {
			out.println("Message received but not successfully forwarded to SMSC."
					+ " Please report error to: support@sourcecode.solutions .");
		    e.printStackTrace();
		}
	} else if (sms.getTo().contains("820707")) {
		logger.info("SMS is from subscriber so will forward to MOPA/ntxuva");
		try {
			/* SmsSender s = new HttpSmsSender(new URL(kannelURL));
		    sms.setUsername(kannelUser);
		    sms.setPassword(kannelPassword); */
			HttpSmsSender s = new HttpSmsSender();
			String responseStr = s.sendToMOPA(sms);
		    //System.out.println(status.toString());
		    logger.info("SMS status: " + responseStr);
		    out.println("Message successfully forwarded from subscriber to MOPA/ntxuva");
		} catch (Exception e) {
			out.println("Message received but not successfully handled."
					+ " Please report error to: support@sourcecode.solutions .");
		    e.printStackTrace();
		}
	} else {
		logger.info("Unexpected situation in SMS handler, so SMS will not be forwarded");
	}

    
    }


    /**
     * TODO: Convert XML MessageDocument to Sms object.
     */
    // Valter: removed below code as could not find this/similar package
    /*
    private Sms smsFromXml(InputStream is) throws IOException
    {
	try {
	    Submit s = MessageDocument.Factory.parse(is).getMessage().getSubmit();
	    Sms sms = new Sms();
	    //   <submit>
	    //       <da><number>destination number (to)</number></da>
	    sms.setTo(s.getDa().getNumber());
	    //       <oa><number>originating number (from)</number></oa>
	    sms.setFrom(s.getOa().getNumber());
	    //       <ud>user data (text)</ud>
	    sms.setText(s.getUd());
	    //       <udh>user data header (udh)</udh>
	    sms.setUdh(s.getUdh().getBytes()); //TODO: test this is correct
	    //     <meta-data>meta-data</meta-data>
	    //     <dcs>
	    //       <mclass>mclass</mclass>
	    sms.setMclass(s.getDcs().getMclass());
	    //       <coding>coding</coding>
	    sms.setCoding(s.getDcs().getCoding());
	    //       <mwi>mwi</mwi>
	    sms.setMwi(s.getDcs().getMwi());
	    //       <compress>compress</compress>
	    sms.setCompress(s.getDcs().getCompress());
	    //       <alt-dcs>alt-dcs</alt-dcs>
	    sms.setAltDcs(s.getDcs().getAltDcs());
	    //     </dcs>
	    //     <pid>pid</pid>
	    sms.setPid((new Integer(s.getPid()).byteValue()));
	    //     <rpi>rpi</rpi>
	    sms.setRpi(s.getRpi());
	    //     <vp>
	    //       <delay>validity time in minutes</delay>
	    sms.setValidity(s.getVp().getDelay());
	    //     </vp>
	    //     <timing>
	    //       <delay>deferred time in minutes</delay>
	    sms.setDeferred(s.getTiming().getDelay());
	    //     </timing>
	    //     <!-- request from application to Kannel -->
	    //     <statusrequest>
	    //       <dlr-mask>dlr-mask</dlr-mask>
	    //       <dlr-url>dlr-url</dlr-url>
	    //     </statusrequest>
	    //     <from>
	    //       <user>username</user>
	    //       <username>username</username> 
	    //       <pass>password</pass>
	    //       <password>password</password>
	    //       <account>account</account>
	    //     </from>
	    //     <to>smsc-id</to>
	    //     <!-- request from Kannel to application -->
	    //     <from>smsc-id</from>
	    sms.setSmsc(s.getFrom().toString()); //TODO: this may be incorrect
	    //     <to>service-name</to>
	    sms.setService(s.getTo());
	    //   </submit>
	    return sms;
	} catch (Exception e) {
	    throw new IOException(e);
	}
    }
    */

}
