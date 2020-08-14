package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		return new NewJunctionEvent(
				data.getInt("time"),
				data.getString("id"),
				lssFactory.createInstance(data.getJSONObject("ls_strategy")),
				dqsFactory.createInstance(data.getJSONObject("dq_strategy")),
				(int)(data.getJSONArray("coor")).get(0),
				(int)(data.getJSONArray("coor")).get(1)
				);
	}

}
