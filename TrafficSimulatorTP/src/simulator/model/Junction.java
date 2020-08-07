package simulator.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

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

	protected Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor,
			int yCoor) {
		super(id);
		if (lsStrategy == null || dqStrategy == null || xCoor < 0 || yCoor < 0) {
			// throw exception
		}

		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	protected void addIncommingRoad(Road r) {
		if (r.getDestJunc()._id.equals(_id)) {
			roads.add(r);
			List<Vehicle> road = new LinkedList<>();
			eListRoads.add(road);
			roadList.put(r, road);
		}
	}

	protected void addOutGoingRoad(Road r) {

		if (r.getSrcJunc().equals(this) && map.get(r.getDestJunc()) == null) {
			map.put(r.getDestJunc(), r);
		} else {
			// TODO throw exception
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
	void advance(int time) {
		List<Vehicle> list = dqStrategy.dequeue(eListRoads.get(lightGreenIndex));
		for (int i = 0; i < list.size(); i++) {
			list.get(i).advance(time);
			list.remove(i);
		}
		int nextGreen = lsStrategy.chooseNextGreen(roads, eListRoads, lightGreenIndex, lastSwitchingTime, time);
		// TODO el último parámetro es time?
		if (nextGreen != lightGreenIndex) {
			lightGreenIndex = nextGreen;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("green", roads.get(lightGreenIndex));
		jo.put("queues", roads);
		return jo;
	}
}
