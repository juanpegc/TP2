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

	private List<Junction> _junctions;
	private String[] _colNames = { "Id", "Green", "Queues" };

	public JunctionsTableModel(Controller ctrl) {
		_junctions = new ArrayList<Junction>();
		ctrl.addObserver(this);
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
	public String getValueAt(int rowIndex, int columnIndex) {
		String s = "";
		switch (columnIndex) {
		case 0:
			s += _junctions.get(rowIndex).getId();
			break;
		case 1:
			int light = _junctions.get(rowIndex).getLightGreenIndex();
			if (light == -1)
				s += "NONE";
			else
				s += _junctions.get(rowIndex).getInRoads().get(light).getId().toString();
			break;
		case 2:
			if ((_junctions.get(rowIndex).getLightGreenIndex()) != -1) {
				s = "";
				for (Road r : _junctions.get(rowIndex).getInRoads()) {
					s += r.toString();
				}
			}
			break;
		default:
			s = null;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setJunctions(map.getJunctions());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setJunctions(map.getJunctions());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_junctions = null;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {

	}

}
