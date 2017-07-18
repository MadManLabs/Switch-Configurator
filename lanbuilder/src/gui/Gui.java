package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

import backend.MainService;
import backend.Service;
import data.ConfigurationData;
import data.ResultData;
import data.TableTypeEnum;

@SuppressWarnings("serial")
public class Gui extends JFrame{
	private UserTextInputArea TextInput;
	private DataTable VlanDataTable;
	private DataTable InterfaceDataTable;
	private JButton RunButton;
	private JButton SaveButton;
	private JButton LoadButton;
	
	private final JFileChooser fc = new JFileChooser();
	
	public Gui(){
		setTitle("Switch Configurator");
		
		setupFileChooser();
		addComponentsToPane(getContentPane());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		
		ImageIcon icon = createImageIcon("images/icon.png", "icon");
		setIconImage(icon.getImage());
		
		setResizable(false);
		setVisible(true);		
	}
	
	private void setupFileChooser(){
		fc.setMultiSelectionEnabled(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		FileFilter filter = new FileNameExtensionFilter("Configuration File", "cfg");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
	}
	
	private void addComponentsToPane(Container pane){
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		TextInput = new UserTextInputArea();
		VlanDataTable = new DataTable(TableTypeEnum.VLAN);
		InterfaceDataTable = new DataTable(TableTypeEnum.INTERFACE);
		
		pane.add(TextInput);
		pane.add(VlanDataTable);
		pane.add(Box.createRigidArea(new Dimension(0, 20)));
		pane.add(InterfaceDataTable);
		pane.add(Box.createRigidArea(new Dimension(0, 15)));
		pane.add(getButtonPanel());
		pane.add(Box.createRigidArea(new Dimension(0, 10)));
	}
	
	private JPanel getButtonPanel(){
		int marginValue = 10;
		
		RunButton = new JButton("Run");
		SaveButton = new JButton("Save");
		LoadButton = new JButton("Load");
		
		RunButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onRun();
			}
		});
		
		SaveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onSave();
			}
		});
		
		LoadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onLoad();
			}
		});
				
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
				
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(RunButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(SaveButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(LoadButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		return buttonPanel;
	}
	
	private void onLoad(){
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File selectedFile = fc.getSelectedFile();
			
			ResultData result = Service.loadConfigFromFile(selectedFile.getAbsolutePath());
			if(result.getSuccessfull() && result.getConfig() != null){
				ConfigurationData loadedConfig = result.getConfig();
				MainService.updateConfig(
						loadedConfig.getHostName(), 
						loadedConfig.getPassword(), 
						loadedConfig.getIpSwitch(), 
						loadedConfig.getEthernetInfos(), 
						loadedConfig.getVlanInfos());
				
				TextInput.updateTextInput();
				VlanDataTable.updateTable(false);
				InterfaceDataTable.updateTable(true);
				this.revalidate();
				this.repaint();
				JOptionPane.showMessageDialog(this, "Config Loaded.");
			}
			else{
				JOptionPane.showMessageDialog(this, "Loading failed", "Loading Failed", JOptionPane.ERROR_MESSAGE);
			}
		} else{
			 JOptionPane.showMessageDialog(this, "Loading canceled.");
		}		
	}
	
	private void onSave(){
		letComponentsUpdateCurrentConfig();
		
		int returnVal = fc.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File selectedFile = fc.getSelectedFile();
			
			//Service.save(MainService.getConfig(), selectedFile.getAbsolutePath());
			String filePath = selectedFile.getAbsolutePath();
			
			if(!filePath.endsWith(".cfg")){
				filePath += ".cfg";
			}
			
			Service.saveConfigToFile(MainService.getConfig(), filePath);
			
			JOptionPane.showMessageDialog(this, "Config Saved.");
		} else{
			 JOptionPane.showMessageDialog(this, "Saving canceled.");
		}		
	}
	
	private void onRun(){
		letComponentsUpdateCurrentConfig();
		
		ResultData result = Service.run(MainService.getConfig());
		int messageType = JOptionPane.ERROR_MESSAGE;
		String title = "Error";
		
		if(result.getSuccessfull())
		{
			messageType = JOptionPane.INFORMATION_MESSAGE;
			title = "Information";
		}
		
		JOptionPane.showMessageDialog(this, result.getErrorMessage(), title, messageType);
	}	
	
	private void letComponentsUpdateCurrentConfig(){
		TextInput.updateCurrentConfigWithData();		
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
}
