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
			throw new WeatherException("Invalid weather");
		}
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) throws WeatherException, RoadException{
		Pair<String, Weather> w;
		for(int i = 0; i < ws.size(); i++) {
			w = ws.get(i);
			if(map.getRoad(w.getFirst()) == null) {
				throw new WeatherException("Invalid road of weather");
			}
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
		}
	}

	@Override
	public String toString() {
		String aux = "";
		aux += "Change Weather: [";
		for(int i = 0; i < ws.size(); i++) {
			if(i != 0) aux += ",";
			Pair<String, Weather> w = ws.get(i);
			aux += "(" + w.getFirst() + "," + w.getSecond().toString() + ")";
		}
		aux += "]";
		return aux;
	}
}
