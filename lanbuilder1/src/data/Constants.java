package data;

public class Constants {

	public static boolean DEV_MODE = false;
	public static boolean FRESHROUTER = false;
	
	//public static final String LEERSTELLE = System.lineSeparator();
	public static final String LEERSTELLE = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	
	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final String TAB = "\t";
	public static final String SINGLE_QUOTE = "'";
	public static final String PERIOD = ".";
	public static final String DOUBLE_QUOTE = "\"";
	
	private Constants(){
		throw new AssertionError();
	}
}
