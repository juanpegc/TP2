package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	
	
	
}
