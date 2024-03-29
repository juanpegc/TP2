package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private List<Road> _roads;
	private String[] _colNames = { "Id", "Length", "Weather", "Max. Speed", "SpeedLimit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel(Controller ctrl) {
		_roads = new ArrayList<Road>();
		ctrl.addObserver(this);
	}

	public void setRoads(List<Road> roads) {
		this._roads = roads;
		update();
	}

	public void update() {
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return _roads == null ? 0 : _roads.size();
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
			s += _roads.get(rowIndex).getId();
			break;
		case 1:
			s += _roads.get(rowIndex).getLength();
			break;
		case 2:
			s += _roads.get(rowIndex).getWeather().toString();
			break;
		case 3:
			s += _roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s += _roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s += _roads.get(rowIndex).getContamination();
			break;
		case 6:
			s += _roads.get(rowIndex).getContLimit();
			break;
		default:
			s = null;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setRoads(map.getRoads());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setRoads(map.getRoads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roads = null;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
	}

}
