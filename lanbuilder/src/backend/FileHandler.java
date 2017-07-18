package backend;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import data.LoginData;
import data.ResultData;

import static java.lang.System.*;

public class FileHandler {
	 
	public FileHandler(){
	}

    public static void createFile(String fileName){
        try{
            File file = new File(fileName);
            if(!file.exists() && !file.isDirectory()) { 
            	file.createNewFile();
                file.isFile();
            }
        }catch (Exception ex) {
            out.println(ex.getMessage());
        }
    }
    
    public static void deleteFile(String fileName){
    	try{
            File file = new File(fileName);
            if(file.exists() && !file.isDirectory()) { 
            	file.delete();
            }
        }catch (Exception ex) {
            out.println(ex.getMessage());
        }
    }
    
    public ResultData loadLogin(String filename) {
		Gson gson = new Gson();
		ResultData feedback = new ResultData();
		
		LoginData loginData = null;
		try {
			loginData = gson.fromJson(FileHandler.readFile(filename), LoginData.class);
			feedback.setLoginData(loginData);
		} catch (JsonSyntaxException ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		} catch (IOException ex) {
			feedback.setSuccessfull(false);
			feedback.setErrorMessage(ex.getMessage());
		}

		return feedback;
}
    
	public static String delOldOutputFileAndwriteOutputToFile(String filename, String input) {
		String output = "";
		try{
			deleteFile(filename);
			writeFile(filename, input);
			output = "Successful write to file";
		}catch(Exception ex){
			output = ex.getMessage();
		}
		return output;
	}

    public static String writeFile(String fileName, String input) throws IOException {
    	createFile(fileName);
    	String output = "";
        FileWriter fileWriter = new FileWriter(fileName,true);
        try{
            fileWriter.write(input);
            fileWriter.write(System.lineSeparator());
            output = "File";
        }catch(Exception ex){
        	output = ex.getMessage();
        } finally {
            fileWriter.close();
        }
        
        return output;
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder output = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                output.append(line);
                output.append("\n");
                line = br.readLine();
            }

            return output.toString();
        }catch(Exception ex){
        	out.println(ex.getMessage());
        	return ex.getMessage();
        } finally {
        	
            br.close();
        }
    }
    
	public String[] readFileAsArray(String file) throws Exception {

		String[] arr= null;
	    List<String> itemsSchool = new ArrayList<String>();

	    try 
	    { 
	        FileInputStream fstream_school = new FileInputStream(file); 
	        DataInputStream data_input = new DataInputStream(fstream_school); 
	        @SuppressWarnings("resource")
			BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input)); 
	        String str_line; 

	        while ((str_line = buffer.readLine()) != null) 
	        { 
	            str_line = str_line.trim(); 
	            if ((str_line.length()!=0))  
	            { 
	                itemsSchool.add(str_line);
	            } 
	        }
       arr = (String[])itemsSchool.toArray(new String[itemsSchool.size()]);

	    }catch(Exception ex){
        	out.println(ex.getMessage());
        }
	    
	    return arr;
	}
 
}
