package simulator.model;

import exceptions.RoadException;

public class CityRoad extends Road {

	protected CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws RoadException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);

	}

	@Override
	protected void reduceTotalContamination() {
		int x;
		Weather w = getWeather();
		if (w == Weather.WINDY || w == Weather.STORM) {
			x = 10;
		} else {
			x = 2;
		}
		reduceContamination(getContamination() -x);
	}

	@Override
	protected void updateSpeedLimit() {
		setSpeedLimit(getMaxSpeed());
	}

	@Override
	protected int calculateVehicleSpeed(Vehicle v) {
		return (int) (((11.0 - v.getContClass()) / 11.0) * getSpeedLimit());
	}

}
