package simulator.model;

import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.RoadMapException;

public class NewInterCityRoadEvent extends NewRoadEvent {

	InterCityRoad r;
	
	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) throws RoadException, RoadMapException, JunctionException {
		super.execute(map);
	}

	@Override
	protected Road createRoad(RoadMap map) throws RoadException {
		return new InterCityRoad(getId(), map.getJunction(getSrcJunc()),
				map.getJunction(getDestJunc()), getMaxSpeed(), getCo2Limit(), getLength(),
				getWeather());
	}
	

}
