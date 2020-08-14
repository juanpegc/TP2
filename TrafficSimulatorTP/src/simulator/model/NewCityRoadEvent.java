package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	CityRoad r;

	public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) {
		super.execute(map);
	}

	@Override
	protected Road createRoad(RoadMap map) {
		return new CityRoad(super.getId(), map.getJunction(super.getSrcJunc()), map.getJunction(super.getDestJunc()),
				super.getMaxSpeed(), super.getCo2Limit(), super.getLength(), super.getWeather());

	}

}
