package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.RoadMapException;
import exceptions.SetContClassException;
import exceptions.VehicleException;
import exceptions.WeatherException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap map;
	private List<Event> events;
	private int time;

	private List<TrafficSimObserver> observers;

	public TrafficSimulator() {
		time = 0;
		events = new SortedArrayList<>();
		map = new RoadMap();
		observers = new ArrayList<>();
	}

	public void addEvent(Event e) {
		events.add(e);
		for (TrafficSimObserver o : observers) {
			o.onEventAdded(map, events, e, time);
		}
	}

	public void advance() throws VehicleException, SetContClassException, WeatherException, JunctionException,
			RoadException, RoadMapException {
		try {
			time++;
			for (TrafficSimObserver o : observers) {
				o.onAdvanceStart(map, events, time);
			}

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

			for (TrafficSimObserver o : observers) {
				o.onAdvanceEnd(map, events, time);
			}
		} catch (Exception e) {
			// onError(e.getMessage());	//TODO
		}
	}

	public void reset() {
		map.reset();
		events.clear();
		time = 0;

		for (TrafficSimObserver o : observers) {
			o.onReset(map, events, time);
		}
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", map.report());
		return jo;

	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		o.onRegister(map, events, time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	}

	public List<Vehicle> getVehicles() {
		return map.getVehicles();
	}
	
	public List<Road> getRoads(){
		return map.getRoads();
	}

	public int getTime() {
		return time;
	}
	
}
