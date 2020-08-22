package simulator.model;

import exceptions.JunctionException;
import exceptions.RoadException;
import exceptions.RoadMapException;

public abstract class NewRoadEvent extends Event {

	private String id;
	private String srcJunc;
	private String destJunc;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;

	public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);
		this.id = id;
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) throws RoadException, RoadMapException, JunctionException {
		Road r = createRoad(map);
		map.addRoad(r);
	}

	protected abstract Road createRoad(RoadMap map) throws RoadException;

	protected String getId() {
		return id;
	}

	protected String getSrcJunc() {
		return srcJunc;
	}

	protected String getDestJunc() {
		return destJunc;
	}

	protected int getLength() {
		return length;
	}

	protected int getCo2Limit() {
		return co2Limit;
	}

	protected int getMaxSpeed() {
		return maxSpeed;
	}

	protected Weather getWeather() {
		return weather;
	}

}
