package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.JunctionException;
import exceptions.RoadMapException;

public class RoadMap {

	private List<Junction> listJunction;
	private List<Road> listRoad;
	private List<Vehicle> listVehicle;
	private Map<String, Junction> mapJunction;
	private Map<String, Road> mapRoad;
	private Map<String, Vehicle> mapVehicle;

	protected RoadMap() {
		listJunction = new ArrayList<>();
		listRoad = new ArrayList<>();
		listVehicle = new ArrayList<>();
		mapJunction = new HashMap<>();
		mapRoad = new HashMap<>();
		mapVehicle = new HashMap<>();
	}

	protected void addJunction(Junction j) {
		if (!listJunction.contains(j)) {
			listJunction.add(j);
			if (!mapJunction.containsKey(j.getId()))
				mapJunction.put(j.getId(), j);
		}

	}

	protected void addRoad(Road r) throws RoadMapException, JunctionException {
		if (mapRoad.get(r.getId()) != null || !mapJunction.containsKey(r.getSrcJunc().getId())
				|| !mapJunction.containsKey(r.getDestJunc().getId())) {
			throw new RoadMapException("Invalid road");
		}
		// Cruce origen - carretera
		mapJunction.get(r.getSrcJunc().getId()).addOutGoingRoad(r);
		mapJunction.get(r.getDestJunc().getId()).addIncommingRoad(r);
		listRoad.add(r);
		mapRoad.put(r.getId(), r);

	}

	protected void addVehicle(Vehicle v) throws RoadMapException {
		boolean valid = mapVehicle.get(v.getId()) == null;

		if (valid) {
			int i = 0;
			while (i < v.getItinerary().size() - 1 && valid) {
				Junction src = v.getItinerary().get(i);
				Junction dest = v.getItinerary().get(i + 1);
				for (Road r : listRoad) {
					valid = valid || (r.getSrcJunc() == src && r.getDestJunc() == dest); // TODO no es &&?
				}
				i++;
			}
		}
		if (valid) {
			listVehicle.add(v);
			mapVehicle.put(v.getId(), v);
		} else {
			throw new RoadMapException("Invalid vehicle");
		}
	}

	public Junction getJunction(String id) {
		return mapJunction.get(id);
	}

	public Road getRoad(String id) {
		return mapRoad.get(id);
	}

	public Vehicle getVehicle(String id) {
		return mapVehicle.get(id);
	}

	public List<Junction> getJunctions() {
		return listJunction;
	}

	public List<Road> getRoads() {
		return listRoad;
	}

	public List<Vehicle> getVehicles() {
		return listVehicle;
	}

	protected void reset() {
		listJunction.clear();
		listRoad.clear();
		listVehicle.clear();
		mapJunction.clear();
		mapRoad.clear();
		mapVehicle.clear();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (Junction j : listJunction) {
			ja.put(j.report());
		}
		jo.put("junctions", ja);

		ja = new JSONArray();
		for (Road r : listRoad) {
			ja.put(r.report());
		}
		jo.put("road", ja);

		ja = new JSONArray();
		for (Vehicle v : listVehicle) {
			ja.put(v.report());
		}
		jo.put("vehicles", ja);

		return jo;
	}

}
