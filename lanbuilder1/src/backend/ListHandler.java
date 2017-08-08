package backend;

import data.DictionaryMap;
import data.EthernetInfoData;
import data.VlanInfoData;

public class ListHandler {
	
	static final String LEERSTELLE = System.lineSeparator();
	
	public static String listVLansForOutput(DictionaryMap<Integer, VlanInfoData> data, SetParameter parameter) {
		String vlanliste = "";
		try {
				for(int i=0; i <= data.size(); i++) {
					vlanliste += parameter.addVlan(data.getEntry(i).getValue().getVlanNo(), 
							data.getEntry(i).getValue().getVlanName());
					vlanliste += parameter.setStateActive();
					vlanliste += parameter.setNoShutdown();
				}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return vlanliste;
	}
	
	public static String listAllInterfacesForOutput(DictionaryMap<Integer, EthernetInfoData> interf, SetParameter parameter) {
		String interfaceListItem = "";
		try {
			for(int i=0; i < interf.size(); i++) {
				interfaceListItem += parameter.moveLevleUp();
				EthernetInfoData currentinterface = interf.getValue(i);
				String interfacestring = currentinterface.getEthernetInterfaceString();
				interfaceListItem += parameter.editThisInterface(interfacestring);

				if(currentinterface.getIsTrunk()) {
					interfaceListItem += parameter.switchInterfaceModeToTrunk(interfacestring);
					
					DictionaryMap<Integer, VlanInfoData> currentInterfaceVlans = currentinterface.getVlanInfos();

					for(int j=0; j < currentInterfaceVlans.size(); j++) {
						int vlanNolistItem =  currentInterfaceVlans.getValue(j).getVlanNo();
						interfaceListItem += parameter.addVlanToInterface(vlanNolistItem);
					}
				}else {
					DictionaryMap<Integer, VlanInfoData> currentInterfaceVlans = currentinterface.getVlanInfos();
					int vlanNolistItem =  currentInterfaceVlans.getValue(0).getVlanNo();
					
					interfaceListItem += parameter.switchInterfaceModeToAccess(interfacestring);
					interfaceListItem += parameter.addVlanToInterfaceAccess(vlanNolistItem);
				}
				interfaceListItem += parameter.powerOnInterface(interfacestring);
				
				interfaceListItem += parameter.moveLevleHardDown();
				interfaceListItem += parameter.moveLevleHardDown();
				
			}
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return interfaceListItem;
	}
}
