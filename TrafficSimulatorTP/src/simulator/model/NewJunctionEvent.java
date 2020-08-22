package simulator.model;

import exceptions.JunctionException;

public class NewJunctionEvent extends Event {

	Junction j;
	String id;
	LightSwitchingStrategy lsStrategy;
	DequeuingStrategy dqStrategy;
	int xCoor;
	int yCoor;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor,
			int yCoor) {
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	@Override
	void execute(RoadMap map) throws JunctionException {
		j = new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor);
		map.addJunction(j);
	}

}
