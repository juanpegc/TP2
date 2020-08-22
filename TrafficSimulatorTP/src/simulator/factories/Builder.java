package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.SetContClassException;
import exceptions.WeatherException;

public abstract class Builder<T> {
	protected String _type;

	Builder(String type) {
		if (type == null)
			throw new IllegalArgumentException("Invalid type: " + type);
		else
			_type = type;
	}

	public T createInstance(JSONObject info) throws JSONException, SetContClassException, WeatherException {

		T b = null;

		if (_type != null && _type.equals(info.getString("type"))) {
			b = createTheInstance(info.has("data") ? info.getJSONObject("data") : null);
		}

		return b;
	}

	protected abstract T createTheInstance(JSONObject data) throws JSONException, SetContClassException, WeatherException;
}
