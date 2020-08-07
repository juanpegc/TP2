package simulator.model;

import java.util.List;
import java.lang.Math;

import org.json.JSONObject;

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

	//TODO Asegúrate de que la velocidad del vehículo es 0 cuando su estado no es Traveling
	
	protected Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		// TODO complete
	}

	protected void setSpeed(int s) {
		if (s < 0) {
			// throw exception
		} else
			speed = Math.min(s, speed);
	}

	protected void setContaminationClass(int c) {
		if (c < 0 || c > 10) {
			// throw exception
		} else
			contClass = c;
	}

	@Override
	void advance(int time) {
		if (status.equals(VehicleStatus.TRAVELING)) {
			contamination += ((location + speed) - location) * contClass;
			location = Math.min(location + speed, road.getLength());
			if (location >= road.getLength()) {
				// el vehículo entra en la cola del cruce correspondiente (llamando
				// a un método de la clase Junction).
				// Actualizar estado del vehículo
			}
		}
	}

	protected void moveToNextRoad() {
		if (status.equals(VehicleStatus.PENDING) || status.equals(VehicleStatus.WAITING)) {
			/*
			 * mueve el vehículo a la siguiente carretera. Este proceso se hace saliendo de
			 * la carretera actual y entrando a la siguiente carretera de su itinerario, en
			 * la localización 0. Para salir y entrar de las carreteras, debes utilizar el
			 * método correspondiente de la clase Road. Para encontrar la siguiente
			 * carretera, el vehículo debe preguntar al cruce en el cual está esperando (o
			 * al primero del itinerario en caso de que el estado del vehículo sea Pending)
			 * mediante una invocación al método correspondiente de la clase Junction.
			 * Observa que la primera vez que el vehículo llama a este método, el vehículo
			 * no sale de ninguna carretera ya que el vehículo todavía no ha empezado a
			 * circular y, que cuando el vehículo abandona el último cruce de su itinerario,
			 * entonces no puede entrar ya a ninguna carretera dado que ha finalizado su
			 * recorrido – no olvides modificar el estado del vehículo
			 */
		}
		else {
			//throw exception
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
