package gui;

import backend.MainService;
import data.DictionaryMap;
import data.EthernetInfoData;

@SuppressWarnings("serial")
public class EthernetInterfaceTableModel extends DataTableModel<EthernetInfoData>{
	
	public EthernetInterfaceTableModel(){
		super(getColumnNames(), new DictionaryMap<Integer, EthernetInfoData>());
	}
	
	public EthernetInterfaceTableModel(DictionaryMap<Integer, EthernetInfoData> data){
		super(getColumnNames(), data);
	}
	
	private static String[] getColumnNames(){
		String[] names = {"Ethernet Interface", "Mode", "VLAN No"};
		return names;
	}
	
	@Override
	public Object getValueAt(int row, int col){
		EthernetInfoData entry = data.getValue(row);
		
		switch(col){
		case 0:
			return entry.getEthernetInterfaceString();
		case 1:
			return entry.getModeString();
		case 2:
			return entry.getVlanString();
		}
		
		return null;
	}

	public void updateData(){
		data = MainService.getConfig().getEthernetInfos();
		update();
	}
}