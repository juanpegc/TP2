package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String, Integer>> l = new ArrayList<>();
		for (Object o : data.getJSONArray("info")) {
			String vehicle = (String) ((JSONObject) o).get("vehicle");
			Integer cont = (((JSONObject)o).getInt("class"));
			l.add(new Pair<>(vehicle, cont));
		}

		return new NewSetContClassEvent(data.getInt("time"), l);
	}

}
