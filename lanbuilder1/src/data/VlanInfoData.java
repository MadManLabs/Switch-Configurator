package data;

public class VlanInfoData {
	private int ID;
	private static int Counter;
	private int VlanNo;
	private String VlanName;
	
	public VlanInfoData(){
		init();
	}
	
	public VlanInfoData(int vlanNo, String vlanName){
		init();
		this.VlanNo = vlanNo;
		this.VlanName = vlanName;
	}
	
	public int getID() {
		return ID;
	}

	public int getVlanNo() {
		return VlanNo;
	}

	public void setVlanNo(int vlanNo) {
		VlanNo = vlanNo;
	}

	public String getVlanName() {
		return VlanName;
	}

	public void setVlanName(String vlanName) {
		VlanName = vlanName;
	}

	private void init(){
		if(Counter <= 0){
			Counter = 0;
		}
		
		// IDs starts by 1 
		ID = ++Counter;		
	}
	
	public String toString(){
		String output = getVlanNo() + " - " + getVlanName();
		
		return output;
	}
}
