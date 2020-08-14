package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEvent{

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		loadData(data);
		return new NewCityRoadEvent(
				getTime(),
				getId(),
				getSrc(),
				getDest(),
				getLength(),
				getCo2Limit(),
				getMaxSpeed(),
				getWeather()
				);
	}

}
