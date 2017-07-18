package backend;

import gui.Gui;

import java.io.IOException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import data.ConfigurationData;
import data.DictionaryMap;
import data.EthernetInfoData;
import data.IpSwitchInfoData;
import data.VlanInfoData;
import static data.Constants.DEV_MODE;
import static data.Constants.FRESHROUTER;


public class MainService {
	public static ConfigurationData Config;
	
	public static void main(String[] args) {
		Config = new ConfigurationData();
		Gui mainView = new Gui();
			if(args.length != 0) {
				try {
					if(args[0].toString().equals("debug") ) {
						DEV_MODE = true;
						FRESHROUTER = true;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
	}

	public static ConfigurationData getConfig(){
		return Config;
	}
	
	public static void updateConfig(String hostName, 
			String passwort, 
			IpSwitchInfoData ip, 
			DictionaryMap<Integer, EthernetInfoData> ethernetInfos, 
			DictionaryMap<Integer, VlanInfoData> vlanInfos){
		if(hostName != null && !hostName.equals("")){
			Config.setHostName(hostName);			
		}
		
		if(passwort != null && !passwort.equals("")){
			Config.setPassword(passwort);			
		}
		
		if(ip != null){
			Config.setIpSwitch(ip);
			
			String fileName = "./config/login.txt";
			try {
				Config.setIpSwitchToJson(ip, fileName);
			} catch (JsonSyntaxException | JsonIOException | IOException e) {
				e.printStackTrace();
			}
		}
		
		if(ethernetInfos != null){
			Config.setEthernetInfos(ethernetInfos);
		}
		
		if(vlanInfos != null){
			Config.setVlanInfos(vlanInfos);
		}		
	}
}
