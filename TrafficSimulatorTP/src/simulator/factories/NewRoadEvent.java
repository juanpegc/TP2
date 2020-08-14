package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEvent extends Builder<Event> {

	JSONObject data;

	public NewRoadEvent(String type) {
		super(type);
	}

	protected void loadData(JSONObject data) {
		this.data = data;
	}

	protected abstract Event createTheInstance(JSONObject data);

	protected int getTime() {
		return data.getInt("time");
	}

	protected String getId() {
		return data.getString("id");
	}

	protected String getSrc() {
		return data.getString("src");
	}

	protected String getDest() {
		return data.getString("dest");
	}

	protected int getLength() {
		return data.getInt("length");
	}

	protected int getCo2Limit() {
		return data.getInt("co2limit");
	}

	protected int getMaxSpeed() {
		return data.getInt("maxspeed");
	}

	protected Weather getWeather() {
		return Weather.valueOf(data.getString("weather"));
	}

}
