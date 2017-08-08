package backend;

import java.io.IOException;
import data.EthernetInfoData;
import static data.Constants.LEERSTELLE;


//this is only for the cisco commandline strings

public class SetParameter {

	public SetParameter(){
		
	}
	
	public String enable() {
		String output = "enable";
		output += LEERSTELLE;
			
		return output;
	}
	
	public String login(){
		String output = "configure terminal";
		output += LEERSTELLE;
			
		return output;
	}
	
	// Workaround by Paul Stewart - CCIE Security
	// set the privilege level in the local user database using the following method.
	// https://learningnetwork.cisco.com/message/79134#79134
	public String addMorePrivilege(String user, String password){
		String output = login();
		output += "username " + user + " password " + password;
		output += LEERSTELLE;
		output += "username " + user + " privilege 15";
		output += LEERSTELLE;
		output += moveLevleDown();
			
		return output;
	}

	public String setIpAdresseToVlan1(){
		String output = "interface vlan 1";
		output += LEERSTELLE;
		output += "ip address 192.168.1.1 255.255.255.0";
		output += LEERSTELLE;
				
		return output;
	}

	public String changeHostname(String host){
		String output = "hostname " + host;
		output += LEERSTELLE;
		output += moveLevleHardDown();
		
		return output;
	}
	
	public String firstVLAN(){
		String output = "vlan database";
		output += LEERSTELLE;
		output += "vtp transparent";
		output += LEERSTELLE;
		output += moveLevleHardDown();
		
		return output;
	}
	
	public String addVlan(int VLANNo, String VLANName){
		String output = moveLevleUp();
		output += "vlan " + VLANNo;
		output += LEERSTELLE;
		output += "name "+ VLANName;
		output += LEERSTELLE;
		output += moveLevleDown();

		return output;
	}
	
	public String removeVlan(int VLANNo){
		String output = "no vlan " + VLANNo;
		output += LEERSTELLE;
		
		return output;
	}
	

	public String editThisInterface(String Interfacename){
		String output = "interface " + Interfacename;
		output += LEERSTELLE;
		
		return output;
	}
	
	public String switchInterfaceModeToTrunk(String Interfacename){
		String output = "switchport trunk encapsulation dot1q";
		output += LEERSTELLE;
		output += "switchport mode trunk";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String switchInterfaceModeToAccess(String Interfacename){
		String output = "switchport access encapsulation dot1q";
		output += LEERSTELLE;
		output += "switchport mode access";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String changeInterfaceMode(String Interfacename){
		//switch form trunk to access and otherwise
		EthernetInfoData etherface = new EthernetInfoData();
		String mode = "";
			if(etherface.getIsTrunk()) {
				mode = "access";
			}else {
				mode = "trunk";
			}
		
		String output = LEERSTELLE;
		output += "switchport mode " + mode;
		output += LEERSTELLE;
		
		return output;
	}
	
	public String addVlanToInterface(int VLANNo) throws IOException{
		String output = "switchport trunk allowed vlan " + VLANNo;
		output += LEERSTELLE;
		
		return output;
	}
	public String addVlanToInterfaceAccess(int VLANNo) throws IOException{
		String output = "switchport access vlan " + VLANNo;
		output += LEERSTELLE;
		
		return output;
	}
	
	public String removeVlanFromInterface(int VLANNo){
		String output = "switchport trunk allowed vlan remove " + VLANNo;
		output += LEERSTELLE;
		
		return output;
	}

	public String powerOnInterface(String Interfacename){
		String output = "no shutdown";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String powerOffInterface(String Interfacename){
		String output = "shutdown";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String moveLevleDown() {
		String output = "end";
		output += LEERSTELLE;
		
		return output;
	}
	public String moveLevleHardDown() {
		String output = "exit";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String moveLevleUp() {
		String output = "configure terminal";
		output += LEERSTELLE;
		
		return output;
	}
	public String moveToVlanDatabase() {
		String output = "vlan database";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String saveConfig(){
		String output = "copy running-config startup-config";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String changePassword(String password){
		String output = "enable password " + password;
		output += LEERSTELLE;
		output += "enable secret " + password;
		output += LEERSTELLE;
		output += "service password-encryption";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String setStateActive(){
		String output = "state active";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String setNoShutdown(){
		String output = "no shutdown";
		output += LEERSTELLE;
		
		return output;
	}
	public String setShutdown(){
		String output = "shutdown";
		output += LEERSTELLE;
		
		return output;
	}
	
	public String setPortChannel(){
		String output = "interface port-channel 11";
		output += LEERSTELLE;
		
		return output;
	}
	
	
}