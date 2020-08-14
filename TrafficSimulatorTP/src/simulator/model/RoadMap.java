package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

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
			if (!mapJunction.containsKey(j._id))
				mapJunction.put(j._id, j);
		}

	}

	protected void addRoad(Road r) {
		/*
		 * añade la carretera r al final de la lista de carreteras y modifica el mapa
		 * correspondiente. Debes comprobar que se cumplen lo siguiente: : (i) no existe
		 * ninguna otra carretera con el mismo identificador; y (ii) los cruces que
		 * conecta la carretera existen en el mapa de carreteras. En caso de que no se
		 * cumplan el método lanza una excepcion.
		 */
	}

	protected void addVehicle(Vehicle v) {
		/*
		 * añade el vehículo v al final de la lista de vehículos y modifica el mapa de
		 * vehículos en concordancia. Debes comprobar que los siguientes puntos se
		 * cumplen: (i) no existe ningún otro vehículo con el mismo identificador; y
		 * (ii) el itinerario es válido, es decir, existen carreteras que conecten los
		 * cruces consecutivos de su itinerario. En caso de que no se cumplan (i) y
		 * (ii), el método debe lanzar una excepción.
		 */
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
		listJunction = new ArrayList<>();
		listRoad = new ArrayList<>();
		listVehicle = new ArrayList<>();
		mapJunction = new HashMap<>();
		mapRoad = new HashMap<>();
		mapVehicle = new HashMap<>();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("junctions", listJunction);
		jo.put("road", listRoad);
		jo.put("vehicles", listVehicle);
		
		return jo;
	}

}
