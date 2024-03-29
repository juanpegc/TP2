package simulator.model;

import java.util.List;

import exceptions.SetContClassException;
import exceptions.VehicleException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String, Integer>> cs;

	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) throws SetContClassException {
		super(time);
		if (cs == null) {
			throw new SetContClassException("Invalid contamination");
		}
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) throws SetContClassException, VehicleException {
		Pair<String, Integer> c;
		for (int i = 0; i < cs.size(); i++) {
			c = cs.get(i);
			if (map.getVehicle(c.getFirst()) == null) {
				throw new SetContClassException("Invalid vehicle of contamination");
			}
			map.getVehicle(c.getFirst()).setContaminationClass((int)c.getSecond());
		}
		
	}

	@Override
	public String toString() {
		String aux = "";
		aux += "Change CO2 class: [";
		for(int i = 0; i < cs.size(); i++) {
			if(i != 0) aux += ",";
			Pair<String, Integer> c = cs.get(i);
			aux += "(" + c.getFirst() + "," + c.getSecond() + ")";
		}
		aux += "]";
		return aux;
	}
}
