package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import exceptions.WeatherException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	public ChangeWeatherDialog(Controller ctrl, JFrame parent) {
		super(parent, true);
		this._ctrl = ctrl;
		if (_ctrl.getRoads().size() == 0) {
			JOptionPane.showMessageDialog(null, "Error: No roads", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			initGUI();
		}
	}

	private void initGUI() {
		setTitle("Change Road Weather");

		JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel();
		helpMsg.setText("<html><p style=\"width:mainPanel.getWidth()px\">"
				+ "Schedule an event to change the weather of a road after a given number of simulator ticks from now."
				+ "</p></html>");
		mainPanel.add(helpMsg, BorderLayout.NORTH);

		JPanel center = new JPanel();

		center.add(new JLabel("Road:"));
		JComboBox<String> road = new JComboBox<String>();
		for (Road r : _ctrl.getRoads())
			road.addItem(r.getId());
		road.setPreferredSize(new Dimension(80, 20));
		center.add(road);

		center.add(new JLabel("Weather:"));
		JComboBox<String> weather = new JComboBox<String>();
		for (Weather w : Weather.values())
			weather.addItem(w.toString());
		weather.setPreferredSize(new Dimension(80, 20));
		center.add(weather);

		center.add(new JLabel("Ticks:"));
		SpinnerModel model = new SpinnerNumberModel(1, 0, 10000, 1);
		JSpinner ticks = new JSpinner(model);
		ticks.setPreferredSize(new Dimension(80, 20));
		center.add(ticks);

		mainPanel.add(center, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Pair<String, Weather>> list = new ArrayList<>();
				list.add(new Pair<>((String) road.getSelectedItem(),
						Weather.valueOf(weather.getSelectedItem().toString())));
				try {
					Event event = new SetWeatherEvent(_ctrl.getTime() + (int) ticks.getValue(), list);
					_ctrl.addEvent(event);
				} catch (WeatherException e1) {
					JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		});

		buttons.add(cancelButton);
		buttons.add(okButton);

		mainPanel.add(buttons, BorderLayout.SOUTH);

		setSize(new Dimension(430, 175));
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
