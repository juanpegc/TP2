package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if (sim == null || eventsFactory == null) {
			// throw exception
		}
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}

	public void loadEvents(InputStream in) throws JSONException{
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

	public void run(int n, OutputStream out) {
		while(n != 0) {
			sim.advance();
			
			n--;
		}
	}

	public void reset() {
		sim.reset();
	}

}
