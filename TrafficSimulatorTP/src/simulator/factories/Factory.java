package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.SetContClassException;
import exceptions.WeatherException;

public interface Factory<T> {
	public T createInstance(JSONObject info) throws JSONException, SetContClassException, WeatherException;
}
