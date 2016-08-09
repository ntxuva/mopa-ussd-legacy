package scode.ntxuva.sms.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;



//Valter: altered below code to use different logging package
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import java.util.logging.*;

/**
 * Abstract servlet for receiving requests from Kannel
 *
 * @author garth
 */
public abstract class KannelServlet extends HttpServlet
{
	// Valter: altered below code to use different logging package
	// private static Logger logger = LoggerFactory.getLogger(KannelServlet.class);
	private static Logger logger = Logger.getLogger(KannelServlet.class.getName());
	private DataSource datasource;
	
	protected String kannelURL, kannelUser, kannelPassword, ntxuvaURL;
    protected Observable obs;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
	//load observers from params
    // Valter: removing handlers logic as have none yet to use
    /*
	obs = new Observable();
	String[] observers = config.getInitParameter("handlers").split(",");
	for (String s:observers) {
	    try {
		Observer o = (Observer)Class.forName(s).newInstance();
		logger.info("Loading handler "+s);
		obs.addObserver(o);
	    } catch (Exception e) {
		throw new ServletException(e);
	    }
	}
	logger.info("Loaded "+obs.countObservers()+" handlers");
	*/
	logger.finest("TODO: write 'handlers' code in KannelServlet init()");
	
	// Valter: load ntxuva address init parameters
	ServletContext context = getServletContext();
	kannelURL = context.getInitParameter("kannel-url");
	kannelUser = context.getInitParameter("kannel-user");
	kannelPassword = context.getInitParameter("kannel-password");
	ntxuvaURL = context.getInitParameter("ntxuva-url");
	
	/*
	// Valter: lookup JNDI datasource at init time
	try {
		InitialContext ctx = new InitialContext();
		datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/ntxuvaDB");
	} catch (NamingException e) {
		e.printStackTrace();
	} */
	
    }
    
    /*
    //Valter: get database connection
    protected Connection getConnection() throws SQLException {
    	return datasource.getConnection();
    }
    
    */

    // Valter: get headers from HTTP POST
    protected Map<String,String> getHeaderMap(HttpServletRequest request) 
    {
	Map<String,String> m = new HashMap<String,String>();
	for (Enumeration e = request.getHeaderNames(); e.hasMoreElements() ;) {
	    String s = (String)e.nextElement();
	    if (s.contains("X-Kannel")) m.put(s, request.getHeader(s));
	    logger.fine("Valter debug - 'getHeaderMap' method shows key-"
	    		 + s + " value-" + request.getHeader(s));
	}	
	return m;
    }

    // Valter: get parameters from HTTP GET
    protected Map<String,String> getParameterMap(HttpServletRequest request) 
    {
	Map<String,String> m = new HashMap<String,String>();
	for (Enumeration e = request.getParameterNames(); e.hasMoreElements() ;) {
	    String s = (String)e.nextElement();
	    if (true) m.put(s, request.getParameter(s));
	    logger.fine("Valter debug - 'getParameterMap' method shows key-"
	    		 + s + " value-" + request.getParameter(s));
	}	
	return m;
    }

    // Valter TODO: DRY method for creating MAP


    protected String getContent(ServletInputStream is) throws IOException
    {
	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	String line;
	StringBuffer content = new StringBuffer();
	while((line = rd.readLine()) != null) {
	    content.append(line);
	}
	rd.close();
	return content.toString();
    }

}
