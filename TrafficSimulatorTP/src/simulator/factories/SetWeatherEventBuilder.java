package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String, Weather>> l = new ArrayList<>();
		for (Object o : data.getJSONArray("info")) {
			String road = (String) ((JSONObject) o).get("road");
			Weather weather = Weather.valueOf((String) ((JSONObject) o).get("weather"));
			l.add(new Pair<>(road, weather));
		}
		return new SetWeatherEvent(data.getInt("time"), l);
	}

}
