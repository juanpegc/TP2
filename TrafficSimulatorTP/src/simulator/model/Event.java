package simulator.model;

import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.RoadMapException;
import exceptions.SetContClassException;
import exceptions.VehicleException;
import exceptions.WeatherException;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		if(getTime() > o.getTime()) return 1;
		else if(getTime() < o.getTime()) return -1;
		else return 0;
	}

	abstract void execute(RoadMap map) throws SetContClassException, WeatherException, VehicleException, JunctionException, RoadException, RoadMapException;
}
