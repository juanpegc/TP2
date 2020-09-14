package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;

	public MapByRoadComponent(Controller ctrl) {
		initGUI();
		setPreferredSize(new Dimension(300, 200));
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			Road r;
			for (int i = 0; i < _map.getRoads().size(); i++) {
				r = _map.getRoads().get(i);
				drawRoad(g, r, i);
				drawVehicles(g, r, i);
				drawImages(g, r, i);
			}
		}
	}

	public void drawRoad(Graphics g, Road r, int i) {

		int x1 = 50;
		int x2 = getWidth() - 100;
		int y = (i + 1) * 50;

		g.setColor(Color.BLACK);
		g.drawString(r.getId(), 10, y);
		g.drawLine(x1, y, x2, y);

		// draw a circle with center at (x,y) with radius _JRADIUS
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junction's identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getSrcJunc().getId(), x1, y);

		// choose a color for the dest junction depending on the traffic light of the
		// road
		Color destColor = _RED_LIGHT_COLOR;
		int idx = r.getDestJunc().getLightGreenIndex();
		if (idx != -1 && r.equals(r.getDestJunc().getInRoads().get(idx))) {
			destColor = _GREEN_LIGHT_COLOR;
		}

		// draw a circle with center at (x,y) with radius _JRADIUS
		g.setColor(destColor);
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junction's identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getDestJunc().getId(), x2, y);

	}

	private void drawVehicles(Graphics g, Road r, int i) {
		for (Vehicle v : r.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				// The calculation below compute the coordinate (vX,vY) of the vehicle on the
				// corresponding road. It is calculated relativly to the length of the road, and
				// the location on the vehicle.

				int x1 = 50;
				int x2 = getWidth() - 100;
				int y1 = (i + 1) * 50;

				double roadLength = Math.sqrt(Math.pow(x1 - x2, 2));
				double alpha = Math.atan(((double) Math.abs(x1 - x2)));
				double relLoc = roadLength * ((double) v.getLocation()) / ((double) r.getLength());
				double x = Math.sin(alpha) * relLoc;
				double y = Math.cos(alpha) * relLoc;
				int yDir = -1;

				int vX = x1 + (int) ((x2 - x1) * ((double) x / (double) roadLength));
				int vY = y1 + yDir * ((int) y);

				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));

				// draw an image of a car and it identifier
				g.drawImage(_car, vX, vY - 10, 16, 16, this);
				g.drawString(v.getId(), vX, vY - 10);

			}
		}
	}

	private void drawImages(Graphics g, Road r, int i) {
		int x = getWidth() - 90;
		int y = (i + 1) * 50;
		
		String im = "";
		switch (r.getWeather()) {
		case SUNNY:
			im = "sun.png";
			break;
		case CLOUDY:
			im = "cloud.png";
			break;
		case RAINY:
			im = "rain.png";
			break;
		case WINDY:
			im = "wind.png";
			break;
		case STORM:
			im = "storm.png";
			break;
		}
		Image image = loadImage(im);
		g.drawImage(image, x, y - 16, 32, 32, this);

		x = getWidth() - 50;
		int c = (int) Math
				.floor(Math.min((double) r.getContamination() / (1.0 + (double) r.getContLimit()), 1.0) / 0.19);
		im = "cont_" + c + ".png";
		image = loadImage(im);
		g.drawImage(image, x, y - 16, 32, 32, this);
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);

	}

	@Override
	public void onError(String err) {
	}

}
