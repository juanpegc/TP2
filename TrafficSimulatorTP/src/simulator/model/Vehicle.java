package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

import org.json.JSONObject;

import exceptions.RoadException;
import exceptions.VehicleException;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{

	private List<Junction> itinerary; // Lista de cruces
	private int maxSpeed; // velocidad máxima
	private int speed; // velocidad actual
	private VehicleStatus status; // PENDING, TRAVELING, WAITING, ARRIVED;
	private Road road;
	private int location; // distancia desde el comienzo de la carretera
	private int contClass; // grado de contaminación (0 - 10)
	private int contamination; // contaminación total
	private int distance; // distancia total recorrida
	private int lastItinerary;

	protected Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws VehicleException {
		super(id);
		if (maxSpeed < 0 || contClass < 0 || contClass > 10 || itinerary.size() < 2) {
			throw new VehicleException("Invalid arguments");
		}
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		lastItinerary = 1;
		location = 0;
		contamination = 0;
		distance = 0;
		speed = 0;
		status = VehicleStatus.PENDING;
		road = null;
	}

	protected void setSpeed(int s) throws VehicleException {
		if (!status.equals(VehicleStatus.TRAVELING)) {
			speed = 0;
		} else {
			if (s < 0) {
				throw new VehicleException("Speed negative");
			} else
				speed = Math.min(s, maxSpeed);
		}
	}

	protected void setContaminationClass(int c) throws VehicleException {
		if (c < 0 || c > 10) {
			throw new VehicleException("Invalid contamination");
		} else
			contClass = c;
	}

	@Override
	void advance(int time) throws RoadException {
		
		if (status == VehicleStatus.TRAVELING) {
			int aux = location;
			if(location + speed >= road.getLength()) {
				distance += (road.getLength() - location);
				location = road.getLength();
			}
			else {
				distance += speed;
				location += speed;
			}
			
			int c = (location - aux) * contClass;
			road.addContamination(c);
			contamination += c;
			
			if (location == road.getLength()) {
				itinerary.get(lastItinerary).enter(this);
				status = VehicleStatus.WAITING;
				speed = 0;
			}
		}
	}

	protected void moveToNextRoad() throws VehicleException, RoadException {
		if (status == VehicleStatus.PENDING || status == VehicleStatus.WAITING) {
			if (lastItinerary == itinerary.size() - 1 && status != VehicleStatus.PENDING) {
				road.exit(this);
				status = VehicleStatus.ARRIVED;
			} else if (status == VehicleStatus.PENDING) {
				road = itinerary.get(lastItinerary - 1).roadTo(itinerary.get(lastItinerary));
				road.enter(this);
				location = 0;
				status = VehicleStatus.TRAVELING;
			} else {
				road.exit(this);
				road = itinerary.get(lastItinerary).roadTo(itinerary.get(++lastItinerary));
				location = 0;
				road.enter(this);
				status = VehicleStatus.TRAVELING;
			}
		} else {
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
	
	public String getCurrentJunction() {
		return itinerary.get(lastItinerary).getId();
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", distance);
		jo.put("co2", contamination);
		jo.put("class", contClass);
		jo.put("status", status.toString());
		if(status != VehicleStatus.ARRIVED && status != VehicleStatus.PENDING) {
			jo.put("road", road);
			jo.put("location", location);
		}
		return jo;
	}

	@Override
	public int compareTo(Vehicle o) {
		if(getLocation() > o.getLocation()) return -1;
		else if(getLocation() < o.getLocation()) return 1;
		else return 0;
	}
}
