package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private List<Junction> _junctions;
	private String[] _colNames = { "Id", "Green", "Queues" };

	public JunctionsTableModel(Controller ctrl) {
		this._ctrl = ctrl;
		_junctions = new ArrayList<Junction>();
	}

	public void setJunctions(List<Junction> junctions) {
		this._junctions = junctions;
		update();
	}

	public void update() {
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return _junctions == null ? 0 : _junctions.size();
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return _colNames[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _junctions.get(rowIndex).getId();
			break;
		case 1:
			s = _junctions.get(rowIndex).getLightGreenIndex();
			break;
		case 2:
			s = _junctions.get(rowIndex).getInRoads().toString();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub

	}

}
