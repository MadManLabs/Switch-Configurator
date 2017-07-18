package gui;

import backend.MainService;
import data.DictionaryMap;
import data.VlanInfoData;

@SuppressWarnings("serial")
public class VlanInfoTableModel extends DataTableModel<VlanInfoData>{
	public VlanInfoTableModel(){
		this(new DictionaryMap<Integer, VlanInfoData>());
	}
	
	public VlanInfoTableModel(DictionaryMap<Integer, VlanInfoData> data){
		super(getColumnNames(), data);
	}
	
	private static String[] getColumnNames(){
		String[] names = {"VLAN No", "VLAN Name"};
		return names;
	}
	
	@Override
	public Object getValueAt(int row, int col){
		VlanInfoData entry = data.getValue(row);
		
		switch(col){
		case 0:
			return "" + entry.getVlanNo();
		case 1:
			return entry.getVlanName();			
		}
		
		return null;
	}
	
	public void updateData(){
		data = MainService.getConfig().getVlanInfos();
		update();
	}
}