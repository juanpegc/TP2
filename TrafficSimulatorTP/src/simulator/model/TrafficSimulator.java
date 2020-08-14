package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class TrafficSimulator {

	private RoadMap map;
	private List<Event> events;
	private int time;

	public TrafficSimulator() {
		time = 0;
		events = new ArrayList<>();
	}

	public void addEvent(Event e) {
		events.add(e);
		// Recuerda que la lista de eventos tiene que estar ordenada como se describió
		// anteriormente
	}

	public void advance() {
		time++;
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getTime() == time) {
				events.get(i).execute(map); // TODO es así o al revés
				events.remove(i);
			}
		}
		List<Junction> junctions = map.getJunctions();
		for (int i = 0; i < junctions.size(); i++) {
			junctions.get(i).advance(i);
		}

		List<Road> roads = map.getRoads();
		for (int i = 0; i < roads.size(); i++) {
			roads.get(i).advance(i);
		}

	}

	public void reset() {
		map = new RoadMap();
		events = new ArrayList<>();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", map.report());
		return jo;

	}

}
