package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

import org.json.JSONObject;

import exceptions.RoadException;
import exceptions.VehicleException;

public class Vehicle extends SimulatedObject {

	private List<Junction> itinerary; // Lista de cruces
	private int maxSpeed; // velocidad máxima
	private int speed; // velocidad actual
	private VehicleStatus status; // PENDING, TRAVELING, WAITING, ARRIVED;
	private Road road;
	private int location; // distancia desde el comienzo de la carretera
	private int contClass; // grado de contaminación (0 - 10)
	private int contamination; // contaminación total
	private int distance; // distancia total recorrida
	private int currentJunction;

	protected Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws VehicleException{
		super(id);
		if (maxSpeed < 0 || contClass < 0 || contClass > 10 || itinerary.size() < 2) {
			throw new VehicleException("Invalid arguments");
		}
		this._id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		currentJunction = 0;
		status = VehicleStatus.PENDING;
	}

	protected void setSpeed(int s) throws VehicleException{
		if (!status.equals(VehicleStatus.TRAVELING)) {
			speed = 0;
		} else {
			if (s < 0) {
				throw new VehicleException("speed negative");
			} else
				speed = Math.min(s, speed);
		}
	}

	protected void setContaminationClass(int c) throws VehicleException {
		if (c < 0 || c > 10) {
			throw new VehicleException("Invalid contamination");
		} else
			contClass = c;
	}

	@Override
	void advance(int time) {
		if (status.equals(VehicleStatus.TRAVELING)) {
			contamination += ((location + speed) - location) * contClass;
			location = Math.min(location + speed, road.getLength());
			if (location >= road.getLength()) {
				currentJunction++;
				itinerary.get(currentJunction).enter(this);
				this.status = VehicleStatus.WAITING;
				speed = 0;
			}
		}
	}

	protected void moveToNextRoad() throws VehicleException, RoadException {
		if (status.equals(VehicleStatus.PENDING) || status.equals(VehicleStatus.WAITING)) {
			if (currentJunction == itinerary.size() - 1) {
				road.exit(this);
				status = VehicleStatus.ARRIVED;
			} else if (status.equals(VehicleStatus.PENDING)) {
				road = itinerary.get(currentJunction).roadTo(itinerary.get(++currentJunction));
				road.enter(this);
				location = 0;
				status = VehicleStatus.TRAVELING;
			} else {
				road.exit(this);
				road = itinerary.get(currentJunction).roadTo(itinerary.get(++currentJunction));
				location = 0;
				status = VehicleStatus.TRAVELING;
			}
		}
		else {
			throw new VehicleException("VehicleStatus incorrect");
		}
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return location;
	}

	public int getContClass() {
		return contClass;
	}

	public int getContamination() {
		return contamination;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", distance);
		jo.put("co2", contamination);
		jo.put("class", contClass);
		jo.put("status", status);
		jo.put("road", road);
		jo.put("location", location);
		return jo;
	}

}
