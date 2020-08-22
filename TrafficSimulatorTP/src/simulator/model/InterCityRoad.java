package simulator.model;

import exceptions.RoadException;
import exceptions.VehicleException;

public class InterCityRoad extends Road {

	protected InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws RoadException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	protected void reduceTotalContamination() {
		int x;
		switch (getWeather()) {
		case SUNNY: {
			x = 2;
			break;
		}
		case CLOUDY: {
			x = 3;
			break;
		}
		case RAINY: {
			x = 10;
			break;
		}
		case WINDY: {
			x = 15;
			break;
		}
		case STORM: {
			x = 20;
			break;
		}
		default:
			x = 0;
			break;
		}
		reduceContamination((int) ((100.0 - x) / 100.0) * getContamination());
	}

	@Override
	protected void updateSpeedLimit() {
		if (getContamination() > getContLimit()) {
			setSpeedLimit((int) (getMaxSpeed() * 0.5));
		} else {
			setSpeedLimit(getMaxSpeed());
		}

	}

	@Override
	protected int calculateVehicleSpeed(Vehicle v) throws VehicleException{
		if (getWeather().equals(Weather.STORM))
			v.setSpeed((int) (getSpeedLimit() * 0.8));	
		else
			v.setSpeed(getSpeedLimit());
		return v.getSpeed();
	}

}
