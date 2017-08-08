package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.io.IOUtils;

import backend.MainService;
import data.DialogTypeEnum;
import data.DictionaryMap;
import data.EthernetInfoData;
import data.VlanInfoData;

@SuppressWarnings("serial")
public class AddEditInterfaceDialog extends JDialog{
	private String dialogTitle = " Ethernet Interface";
	
	private DictionaryMap<Integer, VlanInfoData> AvailableVlans;
	private DictionaryMap<Integer, EthernetInfoData> Data;
	private EthernetInfoData DataToBeEdited;
	private DialogTypeEnum DialogType;
	
	private JTextField SlotTextField;
	private JTextField PortTextField;
	private JTextField SlotRangeTextField;
	private JTextField PortRangeStartTextField;
	private JTextField PortRangeEndTextField;
	private JTabbedPane Tabs;
	
	private JRadioButton FastEthernet;
	private JRadioButton GigabitEthernet;
	private ButtonGroup InterfaceButtonGroup;
	private JRadioButton TrunkMode;
	private JRadioButton AccessMode;
	private ButtonGroup ModeButtonGroup;
	private DictionaryMap<Integer, JCheckBox> CheckBoxList;
	private JButton SaveButton;
	private JButton CancelButton;
	private JPanel VlanPanel;
	
	private final int marginValue = 10;
	
	public AddEditInterfaceDialog(DialogTypeEnum dialogType){
		this(dialogType, new DictionaryMap<Integer, EthernetInfoData>());
	}
	
	public AddEditInterfaceDialog(DialogTypeEnum dialogType, DictionaryMap<Integer, EthernetInfoData> data)
	{
		this.AvailableVlans = MainService.getConfig().getVlanInfos();
		this.Data = data;
		this.DialogType = dialogType;
		
		if(Data.size() > 0 && DialogType == DialogTypeEnum.EDIT){
			DataToBeEdited = data.getValue(0); 
			// check if vlans are still available - otherwise remove from list
			for(int index = 0; index < DataToBeEdited.getVlanInfos().size(); index++){
				VlanInfoData entry = DataToBeEdited.getVlanInfos().getValue(index);
				if(!AvailableVlans.containsKey(entry.getID())){
					DataToBeEdited.getVlanInfos().remove(entry.getID());
				}
			}
		}else{
			DataToBeEdited = new EthernetInfoData();
		}
		
		init(DialogType);
	}
	
	private void init(DialogTypeEnum dialogType){
		setTitle(dialogType.description() + dialogTitle);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// Damit kann das vorherige Fenster erst wieder bedient werden, 
		// wenn der Dialog geschlossen wurde.
		setModal(true); 
		
		ImageIcon icon = createImageIcon("images/icon.png", "icon");
		setIconImage(icon.getImage());
		
		addComponentsToPane(getContentPane());
		
		pack();
		setVisible(true);	
	}
		
	private void addComponentsToPane(Container pane){
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		if(DialogType == DialogTypeEnum.ADD){
			Tabs = new JTabbedPane();
			Tabs.add("Add Single", getInterfaceNumber());
			Tabs.add("Add Range", getInterfaceRange());
			pane.add(Tabs);			
		}else{
			pane.add(getInterfaceNumber());
		}
		
		pane.add(Box.createRigidArea(new Dimension(0, 10)));
		pane.add(getRestOfTheDialog());
		pane.add(Box.createRigidArea(new Dimension(0, 10)));
		pane.add(getButtonPanel());
	}
	
	private JPanel getInterfaceRange(){
		JLabel label = new JLabel("Interface Range:");
		
		JLabel slot = new JLabel("Slot");
		JLabel port = new JLabel("Port");
		JLabel start = new JLabel("Start");
		JLabel end = new JLabel("End");
		
		SlotRangeTextField = new JTextField(2);
		SlotRangeTextField.setDocument(new UserInputLimit(2));
		SlotRangeTextField.setText("0");
	
		PortRangeStartTextField = new JTextField(2);
		PortRangeStartTextField.setDocument(new UserInputLimit(2));
		PortRangeStartTextField.setText("1");
		
		PortRangeEndTextField = new JTextField(2);
		PortRangeEndTextField.setDocument(new UserInputLimit(2));
		PortRangeEndTextField.setText("1");
		
		
		// Grafischer Aufbau
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
		JPanel slotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		slotPanel.add(slot);
		slotPanel.add(SlotRangeTextField);
		
		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		portPanel.add(port);
		portPanel.add(new JLabel("["));
		portPanel.add(start);
		portPanel.add(PortRangeStartTextField);
		portPanel.add(new JLabel(" - "));
		portPanel.add(end);
		portPanel.add(PortRangeEndTextField);
		portPanel.add(new JLabel("]"));
		
		infoPanel.add(Box.createHorizontalGlue());
		infoPanel.add(slotPanel);
		infoPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		infoPanel.add(new JLabel("/"));
		infoPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		infoPanel.add(portPanel);
		infoPanel.add(Box.createHorizontalGlue());		
		
		// Zusammenbau des Ergebnis Panels
		JPanel interfacePanel = new JPanel();
		interfacePanel.setLayout(new BoxLayout(interfacePanel, BoxLayout.LINE_AXIS));
		
		interfacePanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		interfacePanel.add(Box.createHorizontalGlue());
		
		interfacePanel.add(label);
		
		interfacePanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		interfacePanel.add(infoPanel);
		
		interfacePanel.add(Box.createHorizontalGlue());
		interfacePanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		return interfacePanel;
		
	}
	
	private JPanel getInterfaceNumber(){
		JLabel label = new JLabel("Interface Number:");
		
		JLabel slot = new JLabel("Slot");
		JLabel port = new JLabel("Port");
		SlotTextField = new JTextField(2);
		SlotTextField.setDocument(new UserInputLimit(2)); 
		PortTextField = new JTextField(2);
		PortTextField.setDocument(new UserInputLimit(2)); 
		
		SlotTextField.setText("0");
		PortTextField.setText("1");
		
		if(Data != null){
			SlotTextField.setText("" + DataToBeEdited.getEthernetSlotNo());
			PortTextField.setText("" + DataToBeEdited.getEthernetPortNo());
		}
		
		// Grafischer Aufbau
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
		JPanel slotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		slotPanel.add(slot);
		slotPanel.add(SlotTextField);
		
		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		portPanel.add(port);
		portPanel.add(PortTextField);
		
		infoPanel.add(Box.createHorizontalGlue());
		infoPanel.add(slotPanel);
		infoPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		infoPanel.add(new JLabel("/"));
		infoPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		infoPanel.add(portPanel);
		infoPanel.add(Box.createHorizontalGlue());		
		
		// Zusammenbau des Ergebnis Panels
		JPanel interfacePanel = new JPanel();
		interfacePanel.setLayout(new BoxLayout(interfacePanel, BoxLayout.LINE_AXIS));
		
		interfacePanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		interfacePanel.add(Box.createHorizontalGlue());
		
		interfacePanel.add(label);
		
		interfacePanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		interfacePanel.add(infoPanel);
		
		interfacePanel.add(Box.createHorizontalGlue());
		interfacePanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		return interfacePanel;
	}
	
	private JPanel getRestOfTheDialog(){
		JLabel typeLabel = new JLabel("Interface Type:", JLabel.TRAILING);
		JLabel modeLabel = new JLabel("Mode:", JLabel.TRAILING);
		JLabel VlanLabel = new JLabel("VLAN No:", JLabel.TRAILING);
		
		JPanel typePanel = getInterfacePanel();
		JPanel modePanel = getModePanel();
		JPanel vlan = new JPanel();
		vlan.add(getVlanPanel());
		VlanPanel = vlan;
		
		
		JPanel panel = new JPanel(new SpringLayout());
		
		panel.add(typeLabel);
		typeLabel.setLabelFor(typePanel);
		panel.add(typePanel);
		
		panel.add(modeLabel);
		modeLabel.setLabelFor(modePanel);
		panel.add(modePanel);
		
		panel.add(VlanLabel);
		VlanLabel.setLabelFor(VlanPanel);
		panel.add(VlanPanel);
		
		SpringUtilities.makeCompactGrid(panel, 3, 2, 20, 5, 0, 0);
		
		return panel;
		
	}
	
	private JPanel getInterfacePanel(){
		FastEthernet = new JRadioButton("FastEthernet", true);
		GigabitEthernet = new JRadioButton("GigabitEthernet");
		
		if(Data != null){
			FastEthernet.setSelected(DataToBeEdited.getIsFastEthernet());
			GigabitEthernet.setSelected(!DataToBeEdited.getIsFastEthernet());
		}
		
		InterfaceButtonGroup = new ButtonGroup();
		InterfaceButtonGroup.add(FastEthernet);
		InterfaceButtonGroup.add(GigabitEthernet);
		
		JPanel radioBoxPanel = new JPanel();
		
		radioBoxPanel.setLayout(new BoxLayout(radioBoxPanel, BoxLayout.LINE_AXIS));
				
		radioBoxPanel.add(Box.createHorizontalGlue());
		radioBoxPanel.add(FastEthernet);
		radioBoxPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		radioBoxPanel.add(GigabitEthernet);
		radioBoxPanel.add(Box.createHorizontalGlue());
		
		JPanel interfacePanel = new JPanel();
		interfacePanel.setLayout(new BoxLayout(interfacePanel, BoxLayout.LINE_AXIS));
		
		interfacePanel.add(radioBoxPanel);
		
		return interfacePanel;
	}
	
	private JPanel getModePanel(){
		TrunkMode = new JRadioButton("Trunk", true);
		AccessMode = new JRadioButton("Access");
		
		AccessMode.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange() == ItemEvent.SELECTED) {
					VlanPanel.removeAll(); 
					VlanPanel.add(getVlanPanel());
					VlanPanel.revalidate();
					VlanPanel.repaint();
			    }
			    else if (e.getStateChange() == ItemEvent.DESELECTED) {
			    	VlanPanel.removeAll(); 
					VlanPanel.add(getVlanPanel());
					VlanPanel.revalidate();
					VlanPanel.repaint();
			    }				
			}
		});		
		
		ModeButtonGroup = new ButtonGroup();
		ModeButtonGroup.add(TrunkMode);
		ModeButtonGroup.add(AccessMode);
		
		JPanel radioBoxPanel = new JPanel();
		
		if(Data != null){
			TrunkMode.setSelected(DataToBeEdited.getIsTrunk());
			AccessMode.setSelected(!DataToBeEdited.getIsTrunk());
		}
		
		radioBoxPanel.setLayout(new BoxLayout(radioBoxPanel, BoxLayout.LINE_AXIS));
		
		radioBoxPanel.add(Box.createHorizontalGlue());
		radioBoxPanel.add(TrunkMode);
		radioBoxPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		radioBoxPanel.add(AccessMode);
		radioBoxPanel.add(Box.createHorizontalGlue());
		
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.LINE_AXIS));
				
		modePanel.add(radioBoxPanel);
		
		return modePanel;
	}
	
	private JPanel getVlanPanel(){
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));
		
		JPanel vlanPanel = new JPanel();
		vlanPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		CheckBoxList = new DictionaryMap<Integer, JCheckBox>();
				
		ButtonGroup checkboxGroup = new ButtonGroup();
				
		for(int index = 0; index < AvailableVlans.size(); index++){
			VlanInfoData entry = AvailableVlans.getValue(index);
			
			// Wenn ein VLAN schon in der Liste des InterfaceDatas vorhanden ist
			// sollte der Eintrag vorselektiert werden
			boolean isSelected = false;
			
			if(Data != null){
				isSelected= DataToBeEdited.getVlanInfos().containsKey(entry.getID());
			}
			
			JCheckBox checkBox = new JCheckBox(entry.toString(), isSelected);
			CheckBoxList.put(entry.getID(), checkBox);
			
			if(AccessMode.isSelected()){
				checkboxGroup.add(checkBox);
			}
			
			checkboxPanel.add(checkBox);			
		}
		
		vlanPanel.add(checkboxPanel);
		
		return vlanPanel;
	}
	
	private JPanel getButtonPanel(){
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
		boolean isTrunk = TrunkMode.isSelected();
		boolean isFastEthernet = FastEthernet.isSelected();
		int slot;
		int port;
		int rangeStart;
		int rangeEnd;
		
		DictionaryMap<Integer, VlanInfoData> vlans = new DictionaryMap<Integer, VlanInfoData>();
		for(int i = 0; i < CheckBoxList.size(); i++){
			Map.Entry<Integer, JCheckBox> currentEntry = CheckBoxList.getEntry(i);
			
			if(currentEntry.getValue().isSelected() && !vlans.containsKey(currentEntry.getKey())){
				VlanInfoData selectedVlan = AvailableVlans.get(currentEntry.getKey());
				if(selectedVlan != null){
					vlans.put(selectedVlan.getID(), selectedVlan);
				}
			}
		}
		
		if(Tabs == null || Tabs.getSelectedIndex() == 0){
			try {
				slot = Integer.parseInt(SlotTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				slot = 0;			
			}
			
			try {
				port = Integer.parseInt(PortTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				port = 1;			
			}
			
			
			DataToBeEdited.setIsTrunk(isTrunk);
			DataToBeEdited.setIsFastEthernet(isFastEthernet);
			DataToBeEdited.setEthernetSlotNo(slot);
			DataToBeEdited.setEthernetPortNo(port);
			DataToBeEdited.setVlanNo(vlans);
			
			Data.remove(DataToBeEdited.getID());
			Data.put(DataToBeEdited.getID(), DataToBeEdited);
			
		}else{
			try {
				slot = Integer.parseInt(SlotRangeTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				slot = 0;			
			}
			
			try {
				rangeStart = Integer.parseInt(PortRangeStartTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				rangeStart = 1;			
			}
			
			try {
				rangeEnd = Integer.parseInt(PortRangeEndTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				rangeEnd = 1;			
			}
			
			if(rangeStart < 1){
				rangeStart = 1;
			}
			if(rangeEnd < rangeStart){
				rangeEnd = rangeStart;
			}
			
			for(int i = rangeStart; i <= rangeEnd; i++){
				EthernetInfoData newEntry = new EthernetInfoData(isTrunk, isFastEthernet, slot, i, vlans);
				Data.put(newEntry.getID(), newEntry);
			}
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
	
	public DictionaryMap<Integer, EthernetInfoData> getResult(){
		return Data;
	}
}
