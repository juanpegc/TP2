package simulator.model;

import java.util.List;

import org.json.JSONObject;

import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.RoadMapException;
import exceptions.SetContClassException;
import exceptions.VehicleException;
import exceptions.WeatherException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator {

	private RoadMap map;
	private List<Event> events;
	private int time;

	public TrafficSimulator() {
		time = 0;
		events = new SortedArrayList<>();
		map = new RoadMap();
	}

	public void addEvent(Event e) {
		events.add(e);
	}

	public void advance() throws VehicleException, SetContClassException, WeatherException, JunctionException,
			RoadException, RoadMapException {
		time++;
		int i = 0;
		while (events.size() != 0 && i < events.size()) {
			if (events.get(i).getTime() == time) {
				events.get(i).execute(map);
				events.remove(i);
			} else {
				i++;
			}
		}
		List<Junction> junctions = map.getJunctions();
		for (int j = 0; j < junctions.size(); j++) {
			junctions.get(j).advance(time);
		}

		List<Road> roads = map.getRoads();
		for (int j = 0; j < roads.size(); j++) {
			roads.get(j).advance(time);
		}

	}

	public void reset() {
		map.reset();
		events.clear();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", map.report());
		return jo;

	}

}
