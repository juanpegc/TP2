package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private List<Event> _events;
	private String[] _colNames = { "Time", "Desc." };

	public EventsTableModel(Controller ctrl) {
		_events = new ArrayList<Event>();
		ctrl.addObserver(this);
	}

	public void setEvents(List<Event> events) {
		this._events = events;
		update();
	}

	public void update() {
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return _events == null ? 0 : _events.size();
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
			s += _events.get(rowIndex).getTime();
			break;
		case 1:
			s += _events.get(rowIndex).toString();
			break;
		default:
			s = null;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setEvents(events);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._events = events;
        fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_events = null;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._events = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		
	}

}
