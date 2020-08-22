package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int max = 0;
		int j = -1;
		if (roads.isEmpty())
			return -1;
		else if (currGreen == -1) {
			for (int i = 0; i < qs.size(); i++) {
				if (qs.get(i).size() > max) {
					max = qs.get(i).size();
					j = i;
				}
			}
			return j;
		} else if (currTime - lastSwitchingTime < timeSlot)
			return currGreen;
		else {
			for (int i = (currGreen + 1) % roads.size(); i < roads.size(); i++) { //TODO mal
				if (qs.get(i).size() > max) {
					max = qs.get(i).size();
					j = i;
				}
			}
			return j;
		}
	}
}
