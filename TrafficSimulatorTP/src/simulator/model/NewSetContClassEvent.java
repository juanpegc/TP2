package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String, Integer>> cs;

	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if (cs == null) {
			// TODO throw exception
		}
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		Pair<String, Integer> c;
		for (int i = 0; i < cs.size(); i++) {
			c = cs.get(i);
			if (map.getVehicle(c.getFirst()) == null) {
				// TODO throw exception
			}
			map.getVehicle(c.getFirst()).setContaminationClass(c.getSecond()); //TODO parsear a int
		}

	}
}
