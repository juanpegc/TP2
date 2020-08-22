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
		switch (getWeather()) {
		case WINDY: {
			x = 15;
			break;
		}
		case STORM: {
			x = 10;
			break;
		}
		default:
			x = 2;
			break;
		}
		reduceContamination(x);
	}

	@Override
	protected void updateSpeedLimit() {
		setSpeedLimit(getMaxSpeed());
	}

	@Override
	protected int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0-v.getContClass())/11.0)*getSpeedLimit());
	}

}
