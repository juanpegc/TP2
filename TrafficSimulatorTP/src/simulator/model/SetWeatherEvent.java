package simulator.model;

import java.util.List;

import exceptions.RoadException;
import exceptions.WeatherException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private List<Pair<String, Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) throws WeatherException {
		super(time);
		if(ws == null) {
			throw new WeatherException("Invalid arguments");
		}
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) throws WeatherException, RoadException{
		Pair<String, Weather> w;
		for(int i = 0; i < ws.size(); i++) {
			w = ws.get(i);
			if(map.getRoad(w.getFirst()) == null) {
				throw new WeatherException("Road doesnt exist");
			}
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
		}
	}
}
