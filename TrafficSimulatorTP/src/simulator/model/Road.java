package simulator.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.RoadException;
import exceptions.VehicleException;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {

	private Junction srcJunc;
	private Junction destJunc;
	private int length; // Longitud de la carretera
	private int maxSpeed; // Velocidad máxima permitida
	private int speedLimit; // Límite de velocidad
	private int contLimit; // límite de contaminación
	private Weather weather; // Condiciones ambientales
	private int contamination; // Contaminación total
	private List<Vehicle> vehicles; // Lista de vehículos en la carretera

	protected Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws RoadException {
		super(id);
		if (maxSpeed <= 0 || contLimit < 0 || length <= 0 || srcJunc == null || destJunc == null || weather == null) {
			throw new RoadException("Invalid arguments");
		}
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.length = length;
		this.maxSpeed = maxSpeed;
		speedLimit = maxSpeed;
		contamination = 0;
		this.contLimit = contLimit;
		this.weather = weather;
		vehicles = new SortedArrayList<>();
	}

	protected void enter(Vehicle v) throws RoadException {
		if (v.getLocation() == 0 && v.getSpeed() == 0) {
			vehicles.add(v);
		} else {
			throw new RoadException("Invalid vehicle");
		}
	}

	protected void exit(Vehicle v) {
		vehicles.remove(v);
	}

	protected void setWeather(Weather w) throws RoadException {
		if (w == null) {
			throw new RoadException("Invalid weather");
		} else
			this.weather = w;
	}

	protected void addContamination(int c) throws RoadException {
		if (c >= 0)
			contamination += c;
		else {
			throw new RoadException("Invalid contamination");
		}
	}

	public void reduceContamination(int c) {
		contamination = c;
		if (contamination < 0)
			contamination = 0;
	}

	protected abstract void reduceTotalContamination();

	protected abstract void updateSpeedLimit();

	protected abstract int calculateVehicleSpeed(Vehicle v) throws VehicleException;

	@Override
	void advance(int time) throws VehicleException, RoadException {
		reduceTotalContamination();
		updateSpeedLimit();
		for (Vehicle v : vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}

	}

	public Junction getSrcJunc() {
		return srcJunc;
	}

	public Junction getDestJunc() {
		return destJunc;
	}

	public int getLength() {
		return length;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int s) {
		speedLimit = s;
	}

	public int getContLimit() {
		return contLimit;
	}

	public Weather getWeather() {
		return weather;
	}

	public int getContamination() {
		return contamination;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather.toString());
		jo.put("co2", contamination);

		JSONArray jsonArray = new JSONArray();
		for (Vehicle v : vehicles) {
			jsonArray.put(v.getId());
		}
		jo.put("vehicles", jsonArray);
		return jo;
	}

	public String toString() {
		String aux = "";
		aux += getId() + ":[";
		for (int i = 0; i < vehicles.size(); i++) {
			if (vehicles.get(i).getStatus() == VehicleStatus.WAITING) {
				aux += vehicles.get(i).getId();
				if (i != vehicles.size() - 1)
					aux += ", ";
			}
		}
		aux += "]";
		return aux;
	}

}
