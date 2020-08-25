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
		int nextGreen = -1, lengthAux;

		if (roads != null && roads.size() > 1) {
			if (currGreen == -1) {
				nextGreen = 0;
				lengthAux = roads.get(0).getLength();
				for (int i = 1; i < roads.size(); i++)
					if (roads.get(i).getLength() > lengthAux) {
						nextGreen = i;
						lengthAux = roads.get(i).getLength();
					}
			} else if (currTime - lastSwitchingTime < timeSlot) {
				nextGreen = currGreen;
			} else {
				nextGreen = (currGreen + 1) % roads.size();
				lengthAux = roads.get(nextGreen).getLength();
				int posAux = nextGreen, i = 0;
				while (i < roads.size()) {
					if (roads.get(i).getLength() > lengthAux) {
						nextGreen = i;
						lengthAux = roads.get(i).getLength();
					}
					posAux = (posAux + 1) % roads.size();
					i++;
				}
			}
		}

		return nextGreen;
	}
}
