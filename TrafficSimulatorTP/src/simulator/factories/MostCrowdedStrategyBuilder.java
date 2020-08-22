package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		try {
			return new MostCrowdedStrategy(data.getInt("timeslot"));
		} catch (JSONException e) {
			return new MostCrowdedStrategy(1);
		}
	}

}
