package data;

public class EthernetInfoData {
	private int ID;
	private static int Counter; 
	private boolean IsTrunk;
	private boolean IsFastEthernet;
	private int EthernetSlotNo;
	private int EthernetPortNo;
	private DictionaryMap<Integer, VlanInfoData> VlanInfos;
	
	public EthernetInfoData(){
		this(true, true, 0, 1, new DictionaryMap<Integer, VlanInfoData>());		
	}
	
	public EthernetInfoData(boolean isTrunk, boolean isFastEthernet, int slotNo, int portNo, DictionaryMap<Integer, VlanInfoData> vlanInfos){
		init();
		this.IsTrunk = isTrunk;
		this.IsFastEthernet = isFastEthernet;
		this.EthernetSlotNo = slotNo;
		this.EthernetPortNo = portNo;
		this.VlanInfos = vlanInfos;
	}
	
	private void init(){
		if(Counter <= 0){
			Counter = 0;
		}		
		
		// IDs starts by 1 
		ID = ++Counter;		
	}
	
	public int getID() {
		return ID;
	}

	public boolean getIsTrunk() {
		return IsTrunk;
	}
	
	public void setIsTrunk(boolean isTrunk) {
		IsTrunk = isTrunk;
	}
	
	public boolean getIsFastEthernet() {
		return IsFastEthernet;
	}
	
	public void setIsFastEthernet(boolean isFastEthernet) {
		IsFastEthernet = isFastEthernet;
	}
	
	public int getEthernetSlotNo() {
		return EthernetSlotNo;
	}
	
	public void setEthernetSlotNo(int ethernetSlotNo) {
		EthernetSlotNo = ethernetSlotNo;
	}
	
	public int getEthernetPortNo() {
		return EthernetPortNo;
	}
	
	public void setEthernetPortNo(int ethernetPortNo) {
		EthernetPortNo = ethernetPortNo;
	}
	
	public DictionaryMap<Integer, VlanInfoData> getVlanInfos() {
		return VlanInfos;
	}
	
	public void setVlanNo(DictionaryMap<Integer, VlanInfoData> vlanInfos) {
		VlanInfos = vlanInfos;
	}
	
	public boolean addVlanInfoToList(VlanInfoData vlanInfo){
		if(VlanInfos == null){
			return false;
		}
		
		VlanInfos.put(vlanInfo.getID(), vlanInfo);
		return true;
	}
	
	public boolean removeVlanInfoFromList(VlanInfoData vlanInfo){
		if(VlanInfos.containsKey(vlanInfo.getID())){
			VlanInfos.remove(vlanInfo.getID());
			return true;
		}
		
		return false;
	}	
	
	public String getModeString(){
		if(IsTrunk){
			return "Trunk";
		}
		
		return "Access";		
	}
	
	public String getEthernetInterfaceString(){
		String value = "";
		
		if(IsFastEthernet){
			value += "FastEthernet ";
		}
		else{
			value += "GigabitEthernet ";					
		}
		
		value += EthernetSlotNo + "/" + EthernetPortNo;
		
		return value;
	}
	
	public String getVlanString(){
		String[] vlans = new String[VlanInfos.size()];
		
		for(int index = 0; index < VlanInfos.size(); index++){
			vlans[index] = "" + VlanInfos.getValue(index).getVlanNo();			
		}
		
		return String.join(", ", vlans);
	}
	
	public String toString(){
		String newLine = System.lineSeparator();
		String result = "Interface: " + getEthernetInterfaceString() + newLine;
		result += "Mode: " + getModeString() + newLine;
		result += "Vlans: " + getVlanString();
		
		return result;
	}
}
