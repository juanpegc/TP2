package simulator.model;

import java.util.List;

import org.json.JSONObject;

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
			Weather weather) {
		super(id);
		if (maxSpeed <= 0 || contLimit < 0 || length <= 0 || srcJunc == null || destJunc == null || weather == null) {
			// throw exception
		}
		speedLimit = maxSpeed;
		this.length = length;
		// ...
	}

	protected void enter(Vehicle v) {
		if (v.getLocation() == 0 && v.getSpeed() == 0) {
			vehicles.add(v);
		} else {
			// throw exception
		}
	}

	protected void exit(Vehicle v) {

		// Elimina al vehiculo de vehicles
	}

	protected void setWeather(Weather w) {
		if (w == null) {
			// throw exception
		} else
			this.weather = w;
	}

	protected void addContamination(int c) {
		if (c >= 0)
			contamination += c;
		else {
			// throw exception
		}
	}

	protected abstract void reduceTotalContamination();

	protected abstract void updateSpeedLimit();

	protected abstract int calculateVehicleSpeed(Vehicle v);

	@Override
	void advance(int time) {
		// TODO
		/*
		 * (1) llama a reduceTotalContamination para reducir la contaminación total, es
		 * decir, la disminución de CO2.(2) llama a updateSpeedLimit para establecer el
		 * límite de velocidad de la carretera en el paso de simulación actual. (3)
		 * recorre la lista de vehículos (desde el primero al último) y, para cada
		 * vehículo: a) pone la velocidad del vehículo al valor devuelto por
		 * calculateVehicleSpeed. b) llama al método advance del vehículo. Recuerda
		 * ordenar la lista de vehículos por su localización al final del método.
		 * 
		 */

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

	public void reduceContamination(int c) {
		contamination -= c;
		if(contamination < 0) contamination = 0;
	}
	
	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather);
		jo.put("co2", contamination);
		jo.put("vehicles", vehicles);
		return jo;
	}

}
