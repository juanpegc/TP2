package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.VehicleException;

public class Junction extends SimulatedObject {

	private List<Road> roads; // Carreteras que entran al cruce
	private Map<Junction, Road> map; // Mapa de carreteras salientes (Cruce destino, carretera)
	private Map<Road, List<Vehicle>> roadList; // Mapa carretera-cola
	private List<List<Vehicle>> eListRoads; // Lista de colas para las carreteras entrantes
	private int lightGreenIndex; // índice de la carretera entrante que tiene el semáforo en verde
	private int lastSwitchingTime; // el paso en el cual el índice de semáforo en verde ha cambiado de valor
	private LightSwitchingStrategy lsStrategy;// Estrategia para cambiar semáforo
	private DequeuingStrategy dqStrategy;// Estrategia para eliminar vehículos de las colas
	private int xCoor, yCoor;// Coordenadas

	protected Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor)
			throws JunctionException {
		super(id);
		if (lsStrategy == null || dqStrategy == null || xCoor < 0 || yCoor < 0) {
			throw new JunctionException("Invalid arguments");
		}

		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		lightGreenIndex = -1;
		lastSwitchingTime = 0;
		roads = new ArrayList<>();
		map = new HashMap<>();
		roadList = new HashMap<>();
		eListRoads = new ArrayList<>();
	}

	protected void addIncommingRoad(Road r) throws JunctionException {
		if (r.getDestJunc().getId() == this.getId()) {
			roads.add(r);
			List<Vehicle> road = new LinkedList<>();
			eListRoads.add(road);
			roadList.put(r, road);
		} else {
			throw new JunctionException("Invalid road");
		}
	}

	protected void addOutGoingRoad(Road r) throws JunctionException {
		if (r.getSrcJunc().equals(this) && map.get(r.getDestJunc()) == null) {
			map.put(r.getDestJunc(), r);
		} else {
			throw new JunctionException("Invalid road");
		}
	}

	protected void enter(Vehicle v) {
		if (roadList.containsKey(v.getRoad())) {
			roadList.get(v.getRoad()).add(v);
		}
	}

	protected Road roadTo(Junction j) {
		return map.get(j);
	}

	@Override
	void advance(int time) throws VehicleException, RoadException {
		if (lightGreenIndex != -1) {
			List<Vehicle> list = dqStrategy.dequeue(eListRoads.get(lightGreenIndex));

			for (int i = 0; i < list.size(); i++) {
				list.get(i).moveToNextRoad();
				list.remove(i);
			}

		} else {
			int nextGreen = lsStrategy.chooseNextGreen(roads, eListRoads, lightGreenIndex, lastSwitchingTime, time);
			if (nextGreen != lightGreenIndex) {
				lightGreenIndex = nextGreen;
				lastSwitchingTime = time;
			}
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		if (lightGreenIndex == -1)
			jo.put("green", "none");
		else
			jo.put("green", roads.get(lightGreenIndex));

		JSONArray ja = new JSONArray();
		for (Road r : roads) {
			ja.put(r.report());
		}
		jo.put("queues", ja);

		return jo;
	}
}
