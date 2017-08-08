package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.io.IOUtils;

import data.DialogTypeEnum;
import data.VlanInfoData;

@SuppressWarnings("serial")
public class AddEditVlanDialog extends JDialog{
	private final String dialogTitle = " VLAN";
	
	private VlanInfoData Data;
	private DialogTypeEnum DialogType;
	
	private JTextField NumberTextField;
	private JTextField NameTextField;
	private JButton SaveButton;
	private JButton CancelButton;
	
 	public AddEditVlanDialog(DialogTypeEnum dialogType)
	{
 		this(dialogType, null);
	}
	
 	public AddEditVlanDialog(DialogTypeEnum dialogType, VlanInfoData data){
 		this.DialogType = dialogType;
 		this.Data = data;
 		
 		ImageIcon icon = createImageIcon("images/icon.png", "icon");
		setIconImage(icon.getImage());
 		
 		addComponentsToPane(getContentPane());
 		setSettings(dialogType);
 	}
 	
 	private void setSettings(DialogTypeEnum dialogType){
 		setTitle(dialogType.description() + dialogTitle);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// Damit kann das vorherige Fenster erst wieder bedient werden, 
		// wenn der Dialog geschlossen wurde.
		setModal(true); 
		pack();
		setVisible(true);
 	}
 	
	private void addComponentsToPane(Container pane){
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
				
		pane.add(getVlan());
		pane.add(Box.createRigidArea(new Dimension(0, 10)));
		pane.add(getButtonPanel());
	}
	
	private JPanel getVlan(){
		JLabel nameLabel = new JLabel("Name:");
		JLabel numberLabel = new JLabel("Number:");
		
		NameTextField = new JTextField(15);
		NameTextField.setDocument(new UserInputLimit(15)); 
		NumberTextField = new JTextField(4);
		NumberTextField.setDocument(new UserInputLimit(4)); 
		
		if(Data != null){
			NameTextField.setText(Data.getVlanName());
			NumberTextField.setText("" + Data.getVlanNo());
		}
		
		JPanel panel = new JPanel(new SpringLayout());
		
		panel.add(nameLabel);
		nameLabel.setLabelFor(NameTextField);
		panel.add(NameTextField);
		
		panel.add(numberLabel);
		numberLabel.setLabelFor(NumberTextField);
		panel.add(NumberTextField);
		
		SpringUtilities.makeCompactGrid(panel, 2, 2, 5, 5, 5, 5);
		
		return panel;
	}
	
	private JPanel getButtonPanel(){
		int marginValue = 10;
		
		SaveButton = new JButton("Save");
		CancelButton = new JButton("Cancel");
		
		SaveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onSave();
			}
		});
		
		CancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onCancel();
			}
		});
				
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
				
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(SaveButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(CancelButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		return buttonPanel;
	}
	
	private void onSave(){

		int number = 0;
		
		String name = NameTextField.getText();		
		
		try {
			number = Integer.parseInt(NumberTextField.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("No number entered.");
		}
					
		switch(DialogType){
			case ADD:			
				Data = new VlanInfoData(number, name);			
				break;
			case EDIT:
				Data.setVlanName(name);
				Data.setVlanNo(number);			
				break;
		}
		
		setVisible(false);		
	}
	
	private void onCancel(){
		setVisible(false);
	}
	
	public ImageIcon createImageIcon(String path,
            String description) {
        InputStream imgStream = getClass().getResourceAsStream(path);
        if (imgStream != null) {
        	try {
				return new ImageIcon(IOUtils.toByteArray(imgStream), description);
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else {
            System.err.println("Couldn't find file: " + path);            
            return null;
        }
		return null;
    }
	
	public VlanInfoData getResult(){
		return Data;
	}
}
