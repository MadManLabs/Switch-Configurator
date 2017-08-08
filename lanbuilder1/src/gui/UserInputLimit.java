package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class UserInputLimit extends PlainDocument { 
	private int limit; 
	// optional uppercase conversion 
	private boolean toUppercase = false; 
   
	UserInputLimit(int limit) { 
		super(); 
		this.limit = limit; 
	} 
    
	UserInputLimit(int limit, boolean upper) { 
		super(); 
		this.limit = limit; 
		toUppercase = upper; 
	}
  
	public void insertString
    	(int offset, String  str, AttributeSet attr)
    			throws BadLocationException {
		if (str == null) return;

		if ((getLength() + str.length()) <= limit) {
			if (toUppercase) str = str.toUpperCase();
			super.insertString(offset, str, attr);
		}
	}
}  