package data;

public enum DialogTypeEnum {
	ADD ("Add"),
	EDIT ("Edit");
	
	private final String description; 
	
	
	DialogTypeEnum(String description){
		this.description = description;
	}
	
	public String description(){ 
		return description; 
	}
}
