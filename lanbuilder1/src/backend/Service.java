package backend;

import static data.Constants.FRESHROUTER;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import backend.FileHandler;
import backend.SetParameter;
import backend.ConnectionHandler;
import backend.ListHandler;
import data.ConfigurationData;
import data.DictionaryMap;
import data.VlanInfoData;
import data.EthernetInfoData;
import data.ResultData;


public class Service {
	
	public static ResultData save(ConfigurationData userinput, String filename){
		ResultData feedback = new ResultData();
		SetParameter parameter = new SetParameter();

		String hostname = userinput.getHostName();
		String passwordoutput = userinput.getPassword();
				
		DictionaryMap<Integer, VlanInfoData> data = userinput.getVlanInfos();
		DictionaryMap<Integer, EthernetInfoData> interf = userinput.getEthernetInfos();
		
		String output = "";
	
		String login = parameter.login();
		String changeHostname = parameter.changeHostname(hostname);
		String saveConfig = parameter.saveConfig();
		String changePassword =	parameter.changePassword(passwordoutput);
		
		output += login;
		output += changeHostname;
		output += parameter.firstVLAN();
		if(FRESHROUTER){
			FileHandler fh = new FileHandler();
			String fileName = "./config/login.txt";
			
			String user = fh.loadLogin(fileName).getLoginData().getUser();
			String password = fh.loadLogin(fileName).getLoginData().getPassword();
			output += parameter.addMorePrivilege(user, password); //see Workaround in SetParameter.java
		}
		output += ListHandler.listVLansForOutput(data, parameter);
		output += ListHandler.listAllInterfacesForOutput(interf, parameter);
		output += saveConfig;
		output += changePassword;
		
		try {
			feedback.setSuccessfull(true);
			feedback.setErrorMessage(FileHandler.replaceOldOutputToFile(filename, output));
		} catch (Exception ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
			
		}
		
		return feedback;
	}
	
	public static ResultData run(ConfigurationData userinput){
		String ip = userinput.getIpSwitch().toString();
		String testfile = "./switch.cfg";//this relative path have to next to 
		ResultData feedback = new ResultData();
		
		if(!save(userinput, testfile).getSuccessfull()) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(save(userinput,testfile).getErrorMessage());
		}

		try {
			feedback.setSuccessfull(true);
			feedback.setErrorMessage( pushOutputBySshToSwitch(ip, testfile).getErrorMessage() );
		} catch (Exception ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		}

		return feedback;
	}
	
	public static ResultData saveConfigToFile(ConfigurationData userinput, String file) {
		Gson gson = new Gson();
        
		ResultData feedback = new ResultData();
		try {
			feedback.setSuccessfull(true);
			Object output = gson.toJson(userinput);
			
			FileHandler.replaceOldOutputToFile(file, output.toString());
			feedback.setErrorMessage("write to VALID JSON (RFC 4627) File");
		} catch (JsonIOException ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		}

		return feedback;
	}
		
	public static ResultData loadConfigFromFile(String file) {
		ResultData feedback = new ResultData();
		Gson gson = new Gson();
		ConfigurationData config;
		
		String ConfigFile = "";
		try {
			ConfigFile = FileHandler.readFile(file);
			feedback.setSuccessfull(true);
		} catch (IOException ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		}
		
		try {
			config = gson.fromJson(ConfigFile, ConfigurationData.class);
			feedback.setSuccessfull(true);
			feedback.setErrorMessage("success");
			feedback.setConfig(config);
		} catch (JsonSyntaxException ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		} catch (JsonIOException ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		}

		return feedback;		
	}

	private static ResultData pushOutputBySshToSwitch(String ip, String fileWithCiscoCommands) throws Exception {
		//get the SSH Login Credentials form external File
		ResultData feedback = new ResultData();
		FileHandler Commandfile = new FileHandler();
		
		String fileName = "./config/login.txt";

		String user = Commandfile.loadLogin(fileName).getLoginData().getUser();
		String password = Commandfile.loadLogin(fileName).getLoginData().getPassword();
		String secret = Commandfile.loadLogin(fileName).getLoginData().getSecret();

		try{
			String[] commands = Commandfile.readFileAsArray(fileWithCiscoCommands);
			ConnectionHandler sshconn = new ConnectionHandler(ip, user, password, secret, commands);
			feedback.setSuccessfull(true);
			feedback.setErrorMessage(sshconn.call());
		}catch(Exception ex){
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		}
		
		return feedback;	
	}

	

}