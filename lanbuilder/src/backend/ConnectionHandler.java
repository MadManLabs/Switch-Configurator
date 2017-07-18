package backend;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static data.Constants.DEV_MODE;
//import backend.SetParameter;

public class ConnectionHandler {
	private String ip;
	private String user;
	private String password;
	private String secret;
	private String[] commands;

	public ConnectionHandler(String ip, String user, String password, String secret, String[] commands) {
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.secret = secret;
		this.commands = commands;
	}

	public String call(){
		String output = "";
		try {
			Channel channel = getChannel();
			InputStream input = channel.getInputStream();
			OutputStream out = channel.getOutputStream();
			PrintStream commandInput = new PrintStream(out, true);
			if(!DEV_MODE){
				SetParameter parameter = new SetParameter();
				commandInput.println(parameter.enable());
			}
			
			String response = getResponse(input, 50);
			
			commandInput.println(secret);
			response = getResponse(input, 100);
			if (response.contains("% Bad secrets")) {
				output = "auth failed";
				output += " Form IP: " + ip + " User: " + user + " Password: " + password + "Secret: " + secret;
				output += "Please Run the tool with the debug flag";
			}

			for (String command : commands) {
				commandInput.println(command);
				if(DEV_MODE){
					System.out.println( getResponse(input, 10) );
					
					//output += getResponse(input, 10); //for debug give all the Feedback form the Cisco Switch Consol back
				}
			}
			output += "success";
		} catch (Exception ex) {
			output = ex.getMessage();
			output += " Form IP: " + ip + " User: " + user + " Password: " + password + " Secret: " + secret;
		}

		return output;
	}

	private String getResponse(InputStream input, long maxTime) throws IOException {
		StringBuilder buffer = new StringBuilder();
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < maxTime || input.available() > 0) {
			if (input.available() > 0) {
				byte b = (byte) input.read();
				if (b != -1) {
					buffer.append((char) b);
				} else {
					break;
				}
			}
		}
		
		return buffer.toString();
	}

	private Channel getChannel() throws JSchException {
		JSch jsch = new JSch();

		Session session = jsch.getSession(user, ip, 22);
		session.setPassword(password);

		// insecure only for testing
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.connect();
		Channel channel = session.openChannel("shell");
		channel.connect();
		
		return channel;
	}

}