package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEvent {
	
	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		loadData(data);
		return new NewInterCityRoadEvent(
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
