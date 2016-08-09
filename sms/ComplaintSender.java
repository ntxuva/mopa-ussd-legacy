package scode.ntxuva.sms;
 
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import scode.ntxuva.dao.BairroDAO;
import scode.ntxuva.dao.BlockDAO;
import scode.ntxuva.dao.ComplaintDAO;
import scode.ntxuva.dao.ContainerDAO;
import scode.ntxuva.model.Block;
import scode.ntxuva.model.Complaint;
import scode.ntxuva.model.Container;

 
// import javax.net.ssl.HttpsURLConnection;
 
public class ComplaintSender {
	
	private static Logger logger = Logger.getLogger(ComplaintSender.class.getName());
	private final String USER_AGENT = "Mozilla/5.0";
	private String url;
	
	private BairroDAO bairroDAO;
	private ContainerDAO containerDAO;
	private BlockDAO blockDAO;
 
	/*
	public static void main(String[] args) throws Exception {
 
		ComplaintSender http = new ComplaintSender();
 
		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet();
 
		System.out.println("\nTesting 2 - Send Http POST request");
		http.sendPost();
 
	}
	*/
	
	public ComplaintSender(String url) {
		this.url = url;
		bairroDAO = new BairroDAO();
		containerDAO = new ContainerDAO();
		blockDAO = new BlockDAO();
	}
	
	public ComplaintSender() {
		url = "http://demo.ntxuva.org/georeport/v2/requests";
		bairroDAO = new BairroDAO();
		containerDAO = new ContainerDAO();
		blockDAO = new BlockDAO();
	}
 
	// HTTP GET request
	public String sendGet(String args) throws Exception {
		String URLStr = url + "/" + args + ".json";
		URL obj = new URL(URLStr);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
 
		int responseCode = con.getResponseCode();
		logger.info("Sending 'GET' request to URL : " + URLStr);
		logger.info("Response Code : " + responseCode);
		StringBuffer response = new StringBuffer(
				"Enviou numero de reclamacao invalido: " + args
				+ ". Por favor, tente novamente.");
 
		if (responseCode == 200) {
			// successful connection & complaint ID exists
			response.setLength(0);
			BufferedReader in = new BufferedReader(
				       new InputStreamReader(con.getInputStream()));
				String inputLine;
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				return "MOPA - A sua reclamacao " + args + " tem o estado de: "
					+ extractValue(response.toString(), "service_notice")
					+ ". Obrigado pelo seu pedido.";
		}
		return response.toString();
	}
	
	
	private String extractValue(String body, String key) {
		String value = "";
		int initial = body.indexOf(key);
		int start = body.indexOf(":", initial);
		int end = body.indexOf("\",", start);
		if (end > start)
			value = body.substring(start+2, end);
		return value;
	}
	
 
	// HTTP POST request
	public String sendPost(Complaint complaint) throws Exception {
		URL obj = new URL(url + ".json");
		//HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add request header
		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
//		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		StringBuffer params = new StringBuffer();
		params.append("phone=" + Long.toString(complaint.getMSISDN()) );
		if (complaint.getBlockID() == 0) {
			// get GPS coordinates for container
			Container container = containerDAO.getContainer(complaint.getContainerID());
			params.append("&lat=" + Double.toString(container.getLatitude()) );
			params.append("&long=" + Double.toString(container.getLongitude()) );
			params.append("&location_id=" + complaint.getContainerID());
		} else {
			// get GPS coordinates for block/quarteirao
			Block block = blockDAO.getBlock( complaint.getBlockID() );
			params.append("&lat=" + Double.toString(block.getLatitude()) );
			params.append("&long=" + Double.toString(block.getLongitude()) );
			params.append("&location_id=" + complaint.getBlockID());
		}
		params.append("&service_code=0" + complaint.getComplaintTypeID());
		params.append("&description=" + complaint.getComment());
		params.append("&address=" 
				+ bairroDAO.findItemName(complaint.getBairroID()));
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(params.toString());
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		logger.info("Sending 'POST' request to URL : " + obj.toString());
		logger.info("POST parameters : " + params.toString());
		logger.info("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		       new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return "MOPA - Obrigado por ter reportado o seu problema. "
				+ "O numero da sua reclamacao e': "
				+ extractValue(response.toString(), "service_request_id")
				+ ". Para saber o estado, marque *311#, opcao 2.";
 
	}
 
}