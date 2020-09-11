package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import exceptions.ControllerException;
import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.RoadMapException;
import exceptions.SetContClassException;
import exceptions.VehicleException;
import exceptions.WeatherException;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;
import simulator.model.Vehicle;

public class Controller {

	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws ControllerException {
		if (sim == null || eventsFactory == null) {
			throw new ControllerException("Invalid arguments");
		}
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}

	public void loadEvents(InputStream in) throws JSONException, SetContClassException, WeatherException {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray ja = new JSONArray();
		try {
			ja = jo.getJSONArray("events");
			for (Object e : ja) {
				Event t = eventsFactory.createInstance((JSONObject) e);
				sim.addEvent(t);
			}
		} catch (JSONException | IllegalArgumentException e) {
			throw new JSONException("Incorrect input");
		}
	}

	public void run(int n, OutputStream out) throws Exception {
		JSONArray ja = new JSONArray();
		while (n != 0) {
			sim.advance();
			ja.put(sim.report());
			n--;
		}
		JSONObject jo = new JSONObject();
		jo.put("states", ja);
		PrintStream output = new PrintStream(out);
		output.println(jo);
	}

	public void run(int n) throws Exception {
		while (n != 0) {
			sim.advance();
			n--;
		}
	}

	public void reset() {
		sim.reset();
	}

	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}

	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}

	public void addEvent(Event e) {
		sim.addEvent(e);
	}

	public List<Vehicle> getVehicles() {
		return sim.getVehicles();
	}

	public List<Road> getRoads() {
		return sim.getRoads();
	}
	
	public int getTime() {
		return sim.getTime();
	}

}
