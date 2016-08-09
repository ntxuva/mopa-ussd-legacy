package scode.ntxuva.sms.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//Valter: altered below code to use different logging package
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.util.logging.*;

import scode.ntxuva.sms.Dlr;
import scode.ntxuva.sms.UrlTemplate;

/**
 * A sample servlet for receiving DLRs from Kannel.
 *
 * @author garth
 */
public class DlrServlet extends KannelServlet
{
	//Valter: altered below code to use different logging package
	// private static Logger logger = LoggerFactory.getLogger(KannelServlet.class);
	private static Logger logger = Logger.getLogger(DlrServlet.class.getName());
	    
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
	UrlTemplate u = new UrlTemplate(null).all();

	Dlr dlr = Dlr.buildFromTemplate(u, request.getParameterMap());

	PrintWriter out = response.getWriter();
	out.println("ok");

	obs.notifyObservers(dlr);
    }
}
