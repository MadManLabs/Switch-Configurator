package data;

import backend.FileHandler;

public class IpSwitchInfoData {
	private int PartA;
	private int PartB;
	private int PartC;
	private int PartD;

	public IpSwitchInfoData(){
		
		FileHandler LoginFile = new FileHandler();
		
		String fileName = "./config/login.txt";
		String ip = LoginFile.loadLogin(fileName).getLoginData().getIp();
		String[] fn = ip.split("\\."); 
		
		PartA = Integer.parseInt(fn[0]);
		PartB = Integer.parseInt(fn[1]);
		PartC = Integer.parseInt(fn[2]);
		PartD = Integer.parseInt(fn[3]);
	}
	
	public IpSwitchInfoData(int partA, int partB, int partC, int partD){
		this.PartA = partA;
		this.PartB = partB;
		this.PartC = partC;
		this.PartD = partD;
	}
	
	public int getPartA() {
		return PartA;
	}
	
	public void setPartA(int partA) {
		PartA = partA;
	}
	
	public int getPartB() {
		return PartB;
	}
	
	public void setPartB(int partB) {
		PartB = partB;
	}
	
	public int getPartC() {
		return PartC;
	}
	
	public void setPartC(int partC) {
		PartC = partC;
	}
	
	public int getPartD() {
		return PartD;
	}
	
	public void setPartD(int partD) {
		PartD = partD;
	}
	
	public String toString(){
		return PartA + "." + PartB + "." + PartC + "." + PartD;
	}
}
