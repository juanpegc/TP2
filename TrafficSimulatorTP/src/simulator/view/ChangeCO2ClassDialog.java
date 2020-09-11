package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import exceptions.SetContClassException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private String cont[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

	public ChangeCO2ClassDialog(Controller ctrl, JFrame parent) {
		super(parent, true);
		this._ctrl = ctrl;
		if (_ctrl.getVehicles().size() == 0) {
			JOptionPane.showMessageDialog(null, "Error: No vehicles", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			initGUI();
		}
	}

	private void initGUI() {

		setTitle("Change CO2 Class");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(20, 20));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel();
		helpMsg.setText("<html><p style=\"width:mainPanel.getWidth()px\">"
				+ "Schedule an event to change the CO2 class of a vehicle after a given number of simulator ticks from now."
				+ "</p></html>");

		mainPanel.add(helpMsg, BorderLayout.NORTH);

		JPanel center = new JPanel(new FlowLayout());

		center.add(new JLabel("Vehicle:"));
		JComboBox<String> vehicles = new JComboBox<String>();
		for (Vehicle v : _ctrl.getVehicles()) {
			vehicles.addItem(v.getId());
		}
		vehicles.setPreferredSize(new Dimension(70, 20));
		center.add(vehicles);

		center.add(new JLabel("CO2 Class"));
		JComboBox<String> CO2class = new JComboBox<String>(cont);
		CO2class.setPreferredSize(new Dimension(70, 20));
		center.add(CO2class);

		center.add(new JLabel("Ticks:"));
		SpinnerModel value = new SpinnerNumberModel(1, 0, 10000, 1);
		JSpinner ticks = new JSpinner(value);
		ticks.setPreferredSize(new Dimension(70, 20));
		center.add(ticks);

		mainPanel.add(center, BorderLayout.CENTER);

		JPanel buttons = new JPanel();

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Pair<String, Integer> p = new Pair<String, Integer>((String)vehicles.getSelectedItem(), Integer.parseInt((String) CO2class.getSelectedItem()));
				List<Pair<String, Integer>> list = new ArrayList<>();
				list.add(p);
				Event event;
				try {
					event = new NewSetContClassEvent((_ctrl.getTime() + (int)ticks.getValue()), list);
					_ctrl.addEvent(event);
					setVisible(false);
				} catch (SetContClassException e1) {
					JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		buttons.add(cancelButton);
		buttons.add(okButton);

		mainPanel.add(buttons, BorderLayout.SOUTH);

		mainPanel.setVisible(true);

		setSize(450, 175);
		setLocationRelativeTo(null);
		setVisible(true);

	}

}
