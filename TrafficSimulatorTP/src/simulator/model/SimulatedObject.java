package simulator.model;

import org.json.JSONObject;

import exceptions.RoadException;
import exceptions.VehicleException;

public abstract class SimulatedObject {

	protected String _id;

	SimulatedObject(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	@Override
	public String toString() {
		return _id;
	}

	abstract void advance(int time) throws VehicleException, RoadException;

	abstract public JSONObject report();
}
