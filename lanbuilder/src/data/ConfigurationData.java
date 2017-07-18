package data;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import backend.FileHandler;

public class ConfigurationData {
	private DictionaryMap<Integer, EthernetInfoData> EthernetInfos;
	
	private DictionaryMap<Integer, VlanInfoData> VlanInfos;
	
	private String HostName;
	private String Password;
	private IpSwitchInfoData IpSwitch;
	
	public ConfigurationData(){
		HostName = "Host";
		Password = "";
		
		IpSwitch = new IpSwitchInfoData();
		
		EthernetInfos = new DictionaryMap<Integer, EthernetInfoData>();
		
		// Als default VLAN wird VLAN1 zu beginn zum Dictionary hinzugefügt
		VlanInfos = new DictionaryMap<Integer, VlanInfoData>();
		VlanInfoData defaultVLan = new VlanInfoData(1, "VLAN1");
		VlanInfos.put(defaultVLan.getID(), defaultVLan);
	}
	
	public ConfigurationData(
			DictionaryMap<Integer, EthernetInfoData> ethernetInfos, 
			DictionaryMap<Integer, VlanInfoData> vlanInfos,
			String hostName, 
			String password,
			IpSwitchInfoData ipSwitch)
	{
		this.EthernetInfos = ethernetInfos;
		this.VlanInfos = vlanInfos;
		this.HostName = hostName;
		this.Password = password;
		this.IpSwitch = ipSwitch;
	}
	
	public DictionaryMap<Integer, EthernetInfoData> getEthernetInfos() {
		return EthernetInfos;
	}

	public void setEthernetInfos(
			DictionaryMap<Integer, EthernetInfoData> ethernetInfos) {
		EthernetInfos = ethernetInfos;
	}

	public DictionaryMap<Integer, VlanInfoData> getVlanInfos() {
		return VlanInfos;
	}

	public void setVlanInfos(DictionaryMap<Integer, VlanInfoData> vlanInfos) {
		VlanInfos = vlanInfos;
	}

	public String getHostName() {
		return HostName;
	}

	public void setHostName(String hostName) {
		HostName = hostName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public IpSwitchInfoData getIpSwitch() {
		return IpSwitch;
	}

	public void setIpSwitch(IpSwitchInfoData ipSwitch) {
		IpSwitch = ipSwitch;
	}
	
	public void setIpSwitchToJson(IpSwitchInfoData ipSwitch, String fileName) throws JsonSyntaxException, JsonIOException, IOException {
		Gson gson = new Gson();
		
		LoginData  loginData = gson.fromJson(FileHandler.readFile(fileName), LoginData.class);
		loginData.setIp(ipSwitch.toString());

		Object output = gson.toJson(loginData);
		FileHandler.delOldOutputFileAndwriteOutputToFile(fileName, output.toString());
	}

	public String toString(){
		String newLine = System.lineSeparator();
		
		String result = "Current Configuration" + newLine;
		result += "Host name: " + getHostName() + newLine;
		result += "password: " + getPassword() + newLine;
		result += "IP: " + getIpSwitch() + newLine + newLine;
		result += "VLANs: " + newLine;
		for(int i = 0; i < getVlanInfos().size(); i++){
			result += getVlanInfos().getValue(i) + newLine;
		}
		
		result += newLine;
		for(int i = 0; i < getEthernetInfos().size(); i++){
			result += getEthernetInfos().getValue(i) + newLine + newLine;
		}
		
		return result;
		
	}
}
