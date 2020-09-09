package simulator.view;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import simulator.control.Controller;

public class ChangeCO2ClassDialog {

	private Controller controller;
	private JFrame parent;

	public ChangeCO2ClassDialog(Controller controller, JFrame parent) {
		this.controller = controller;
		this.parent = parent;
		init();
	}

	private void init() {
		JDialog d = new JDialog(parent, "Change CO2 Class");
		d.setTitle("Change CO2 Class");
		d.setVisible(true);
		d.add(new JLabel(
				"Schedule an event to change the CO2 class of a vehicle after a given number of simulator ticks from now."));
	}

}
