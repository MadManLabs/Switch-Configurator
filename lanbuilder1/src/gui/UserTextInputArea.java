package gui;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.apache.commons.io.IOUtils;

import backend.MainService;
import data.ConfigurationData;
import data.IpSwitchInfoData;

@SuppressWarnings("serial")
public class UserTextInputArea extends JPanel {
	private JTextField NameTextField;
	private JPasswordField PwTextField;
	private JTextField IpFieldA;
	private JTextField IpFieldB;
	private JTextField IpFieldC;
	private JTextField IpFieldD;
	
	public UserTextInputArea(){
		super(new FlowLayout(FlowLayout.LEFT));
		
		generatePanel(this);
	}
	
	private void generatePanel(JPanel panel){
		panel.add(generateInput());
		panel.add(generateIcon());
	}
	
	private JPanel generateIcon(){
		JPanel panel = new JPanel();
		JLabel iconLabel = new JLabel();
		
		iconLabel.setVerticalTextPosition(JLabel.BOTTOM);
		iconLabel.setHorizontalTextPosition(JLabel.CENTER);
		iconLabel.setHorizontalAlignment(JLabel.CENTER);
		iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		

		ImageIcon ci = createImageIcon("images/GUI.png", "test");
		iconLabel.setIcon(ci);
		panel.add(iconLabel);
		return panel;
	}
	
	private JPanel generateInput(){
		ConfigurationData currentConfig = MainService.getConfig();
		
		// Label Definition
		JLabel hostNameLabel = new JLabel("Host name:", JLabel.TRAILING);
		JLabel pwLabel = new JLabel("Password:", JLabel.TRAILING);
		JLabel ipLabel = new JLabel("Switch IP:", JLabel.TRAILING);
		
		// HostName Input Definition
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		NameTextField = new JTextField(15);		
		NameTextField.setDocument(new UserInputLimit(15));
		NameTextField.setText(currentConfig.getHostName());
		namePanel.add(NameTextField);
		
		// Password Input Definition
		JPanel pwPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		PwTextField = new JPasswordField(8);
		PwTextField.setDocument(new UserInputLimit(8));
		pwPanel.add(PwTextField);
		
		// IP Input Definition
		JPanel ipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		IpFieldA = new JTextField(3);
		IpFieldA.setDocument(new UserInputLimit(3));
		IpFieldB = new JTextField(3);
		IpFieldB.setDocument(new UserInputLimit(3));
		IpFieldC = new JTextField(3);
		IpFieldC.setDocument(new UserInputLimit(3));
		IpFieldD = new JTextField(3);
		IpFieldD.setDocument(new UserInputLimit(3));
		
		// Setzen von aktuellen Werten
		IpSwitchInfoData currentIp = currentConfig.getIpSwitch();
		IpFieldA.setText("" + currentIp.getPartA());
		IpFieldB.setText("" + currentIp.getPartB());
		IpFieldC.setText("" + currentIp.getPartC());
		IpFieldD.setText("" + currentIp.getPartD());

		ipPanel.add(IpFieldA);
		ipPanel.add(new JLabel("."));
		ipPanel.add(IpFieldB);
		ipPanel.add(new JLabel("."));
		ipPanel.add(IpFieldC);
		ipPanel.add(new JLabel("."));
		ipPanel.add(IpFieldD);
		
		// Panel zusammenbauen
		JPanel panel = new JPanel(new SpringLayout());
		
		panel.add(hostNameLabel);
		hostNameLabel.setLabelFor(namePanel);
		panel.add(namePanel);
		
		panel.add(pwLabel);
		pwLabel.setLabelFor(pwPanel);
		panel.add(pwPanel);
		
		panel.add(ipLabel);
		ipLabel.setLabelFor(ipPanel);
		panel.add(ipPanel);

		// Zu einem vernünftigen Grid formatieren
		SpringUtilities.makeCompactGrid(panel, 3, 2, 6,6,6,6);
		
		return panel;
	}
	
	public void updateCurrentConfigWithData(){
		String hostName = NameTextField.getText();
		String pw = new String(PwTextField.getPassword());
		IpSwitchInfoData ip = null;
		try {
			ip = new IpSwitchInfoData(
					Integer.parseInt(IpFieldA.getText()), 
					Integer.parseInt(IpFieldB.getText()), 
					Integer.parseInt(IpFieldC.getText()), 
					Integer.parseInt(IpFieldD.getText()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("No digits were entered for ip.");			
		}
		
		MainService.updateConfig(hostName, pw, ip, null, null);
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
	
	public void updateTextInput(){
		ConfigurationData currentConfig = MainService.getConfig();
		NameTextField.setText(currentConfig.getHostName());
		PwTextField.setText(currentConfig.getPassword());
		IpSwitchInfoData ip = currentConfig.getIpSwitch();
		IpFieldA.setText("" + ip.getPartA());
		IpFieldB.setText("" + ip.getPartB());
		IpFieldC.setText("" + ip.getPartC());
		IpFieldD.setText("" + ip.getPartD());
	}
}