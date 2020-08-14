package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private List<Pair<String, Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		if(ws == null) {
			//TODO throw exception
		}
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		Pair<String, Weather> w;
		for(int i = 0; i < ws.size(); i++) {
			w = ws.get(i);
			if(map.getRoad(w.getFirst()) == null) {
				//TODO throw exception
			}
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
		}
	}
}
