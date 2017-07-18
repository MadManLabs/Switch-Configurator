package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import backend.MainService;
import data.DialogTypeEnum;
import data.DictionaryMap;
import data.EthernetInfoData;
import data.TableTypeEnum;
import data.VlanInfoData;

@SuppressWarnings("serial")
public class DataTable extends JPanel {
	private JTable DataTable;
	private VlanInfoTableModel VlanTableModel;
	private EthernetInterfaceTableModel InterfaceTableModel;
	
	private TableTypeEnum TableType;
	private DictionaryMap<Integer, VlanInfoData> VlanInfos;
	private DictionaryMap<Integer, EthernetInfoData> EthernetInfos;
	
	private JButton AddButton;
	private JButton EditButton;
	private JButton RemoveButton;
		
	public DataTable(TableTypeEnum tableType){
		this(tableType, new DictionaryMap<Integer, VlanInfoData>(), new DictionaryMap<Integer, EthernetInfoData>());
	}
	
	public DataTable(TableTypeEnum tableType, DictionaryMap<Integer, VlanInfoData> vlanInfos){
		this(tableType, vlanInfos, new DictionaryMap<Integer, EthernetInfoData>());		
	}
	
	public DataTable(TableTypeEnum tableType, 
			DictionaryMap<Integer, VlanInfoData> vlanInfos, 
			DictionaryMap<Integer, EthernetInfoData> ethernetInfos){
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.VlanInfos = vlanInfos;
		this.EthernetInfos = ethernetInfos;
		
		init(tableType);
	}
	
	private void init(TableTypeEnum tableType){
		this.TableType = tableType;
		
		switch(tableType)
		{
			case VLAN:
				VlanTableModel = new VlanInfoTableModel(MainService.getConfig().getVlanInfos());
				DataTable = new JTable(VlanTableModel);
				
				break;
			case INTERFACE:
				InterfaceTableModel = new EthernetInterfaceTableModel();
				DataTable = new JTable(InterfaceTableModel);
				break;
			default:
				return;
		}
		
		generateTable(DataTable);
	}
		
	private void generateTable(JTable table){
		setupTableSettings(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		JPanel buttonPanel = addButtonPanel();
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));

		tablePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		tablePanel.add(scrollPane);
		tablePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		this.add(Box.createHorizontalGlue());
		this.add(buttonPanel);
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		this.add(tablePanel);
		this.add(Box.createHorizontalGlue());		
	}
	
	private void setupTableSettings(JTable table){
		// Set table settings
		
		// Set dimension
		table.setPreferredScrollableViewportSize(new Dimension(300,100));
		table.setFillsViewportHeight(true);
		
		// Der User soll nur einzelne Rows auswaehlen koennen
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);				
	}
	
	private JPanel addButtonPanel(){
		AddButton = new JButton("Add");
		EditButton = new JButton("Edit");
		RemoveButton = new JButton("Remove");
		
		AddButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onAdd();
			}
		});
		
		EditButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onEdit();
			}
		});
		
		RemoveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onRemove();
			}
		});
		
		int marginValue = 10;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
				
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(AddButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(EditButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		buttonPanel.add(RemoveButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(marginValue, 0)));
		
		return buttonPanel;
	}
	
	private void onAdd(){
		DictionaryMap<Integer, VlanInfoData> currentVlans = MainService.getConfig().getVlanInfos();
		
		switch(TableType){
			case VLAN:
				AddEditVlanDialog addVlanDialog = new AddEditVlanDialog(DialogTypeEnum.ADD); 
				VlanInfoData newVlan = addVlanDialog.getResult();
				
				if(newVlan != null){
					// add new vlan to list
					currentVlans.put(newVlan.getID(), newVlan);
					
					// update config with updated list
					MainService.updateConfig(null, null, null, null, currentVlans);
					VlanTableModel.updateData();
				}
				
				addVlanDialog.dispose();
				
				break;
			case INTERFACE:
				AddEditInterfaceDialog addInterfaceDialog = new AddEditInterfaceDialog(DialogTypeEnum.ADD); 
				DictionaryMap<Integer, EthernetInfoData> newInterfaces = addInterfaceDialog.getResult();
				
				if(newInterfaces != null && newInterfaces.size() > 0){
					DictionaryMap<Integer, EthernetInfoData> currentEthernetInfos = MainService.getConfig().getEthernetInfos();
					
					for(int i = 0; i < newInterfaces.size(); i++){
						EthernetInfoData newEntry = newInterfaces.getValue(i);
						currentEthernetInfos.put(newEntry.getID(), newEntry);						
					}					
					
					MainService.updateConfig(null, null, null, currentEthernetInfos, null);
					InterfaceTableModel.updateData();
				}
				
				addInterfaceDialog.dispose();
				break;
		}
	}
	
	private void onEdit(){
		int index = DataTable.getSelectedRow();
		if(index < 0){
			return;
		}
		
		DictionaryMap<Integer, VlanInfoData> currentVlans = MainService.getConfig().getVlanInfos();
		
		switch(TableType){
			case VLAN:
				VlanInfoData selectedVlan = currentVlans.getValue(index);
				
				if(selectedVlan == null){
					System.out.println("VlanInfo wurde nicht gefunden");
					return;
				}
				
				AddEditVlanDialog editVlanDialog = new AddEditVlanDialog(DialogTypeEnum.EDIT, selectedVlan);
				
				VlanInfoData editedVlan = editVlanDialog.getResult();
				currentVlans.remove(selectedVlan.getID());
				currentVlans.put(editedVlan.getID(), editedVlan);
				
				MainService.updateConfig(null, null, null, null, currentVlans);
				VlanTableModel.updateData();
				
				editVlanDialog.dispose();
				
				break;
			case INTERFACE:
				DictionaryMap<Integer, EthernetInfoData> currentEthernetInfos = MainService.getConfig().getEthernetInfos();
				EthernetInfoData selectedInterface = currentEthernetInfos.getValue(index);
				
				if(selectedInterface == null){
					System.out.println("Ethernet Interface Info nicht gefunden");
					return;
				}
				
				DictionaryMap<Integer, EthernetInfoData> toBeEdited = new DictionaryMap<Integer, EthernetInfoData>();
				toBeEdited.put(selectedInterface.getID(), selectedInterface);
				
				AddEditInterfaceDialog editInterfaceDialog = new AddEditInterfaceDialog(DialogTypeEnum.EDIT, toBeEdited);
				
				DictionaryMap<Integer, EthernetInfoData> editedInterfaceList = editInterfaceDialog.getResult();
				EthernetInfoData editedInterface = editedInterfaceList.getValue(0);
				
				currentEthernetInfos.remove(selectedInterface.getID());
				currentEthernetInfos.put(editedInterface.getID(), editedInterface);
				
				MainService.updateConfig(null, null, null, currentEthernetInfos, null);
				InterfaceTableModel.updateData();
				
				editInterfaceDialog.dispose();
				
				break;
		}
	}
	
	private void onRemove(){
		int index = DataTable.getSelectedRow();
		
		if(index < 0){
			return;
		}
		
		DictionaryMap<Integer, VlanInfoData> currentVlans = MainService.getConfig().getVlanInfos();
		
		switch(TableType){
			case VLAN:
				if(currentVlans.size() > 0){
					VlanInfoData selectedVlan = currentVlans.getValue(index);
					
					if(selectedVlan == null){
						System.out.println("VlanInfo wurde nicht gefunden");
						return;
					}
					
					currentVlans.remove(selectedVlan.getID());
					
					MainService.updateConfig(null, null, null, null, currentVlans);
					VlanTableModel.updateData();					
				}
				
				break;
			case INTERFACE:
				DictionaryMap<Integer, EthernetInfoData> currentEthernetInfos = MainService.getConfig().getEthernetInfos();
				
				if(currentEthernetInfos.size() > 0){
					EthernetInfoData selectedInterface = currentEthernetInfos.getValue(index);
					
					if(selectedInterface == null){
						System.out.println("Ethernet Interface Info wurde nicht gefunden");
						return;
					}
					
					currentEthernetInfos.remove(selectedInterface.getID());
					
					MainService.updateConfig(null, null, null, currentEthernetInfos, null);
					InterfaceTableModel.updateData();
				}				
				
				break;
		}
	}

	public void output(){
		DictionaryMap<Integer, VlanInfoData> currentVlans = MainService.getConfig().getVlanInfos();
		System.out.println("Size: " + currentVlans.size());
		
		for(int i = 0; i< currentVlans.size(); i++){
			VlanInfoData entry = currentVlans.getValue(i);
			System.out.println("Name: " + entry.getVlanName());
			System.out.println("No: " + entry.getVlanNo());
			System.out.println("ID: " + entry.getID());
			System.out.println("Index: " + i);
		}
	}
	
	public void updateTable(boolean isEthernetTable){
		if(isEthernetTable){
			InterfaceTableModel.updateData();
		}else{
			VlanTableModel.updateData();
		}
		
	}
	
	public DictionaryMap<Integer, VlanInfoData> getVlanData(){
		return VlanInfos;
	}
	
	public DictionaryMap<Integer, EthernetInfoData> getInterfaceData(){
		return EthernetInfos;
	}
}
