package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private List<Vehicle> _vehicles;
	private String[] _colNames = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2",
			"Distance" };
	
	public VehiclesTableModel(Controller ctrl) {
		_vehicles = new ArrayList<Vehicle>();
		ctrl.addObserver(this);
	}
	
	public void setVehicles(List<Vehicle> vehicles) {
		this._vehicles = vehicles;
		update();
	}

	public void update() {
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return _vehicles == null ? 0 : _vehicles.size();
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
			s = _vehicles.get(rowIndex).getId();
			break;
		case 1:
			VehicleStatus status = _vehicles.get(rowIndex).getStatus();
			if(status == VehicleStatus.PENDING) {
				s = "Pending";
			}
			else if(status == VehicleStatus.TRAVELING) {
				s = (_vehicles.get(rowIndex).getRoad().getId() + ":" + _vehicles.get(rowIndex).getLocation());
			}
			else if(status == VehicleStatus.WAITING) {
				s = ("Waiting:" + _vehicles.get(rowIndex).getCurrentJunction());
			}
			else {
				s = "Arrived";
			}
			break;
		case 2:
			s = _vehicles.get(rowIndex).getItinerary().toString();
			break;
		case 3:
			s = _vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			s = _vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = _vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			s = _vehicles.get(rowIndex).getContamination();
			break;
		case 7: 
			s = _vehicles.get(rowIndex).getDistance();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setVehicles(map.getVehicles());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setVehicles(map.getVehicles());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub

	}

}
