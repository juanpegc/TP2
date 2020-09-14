package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private Boolean _stopped;
	public static Boolean charged;

	private JButton loadEventsFileButton;
	private JButton changeCO2ClassButton;
	private JButton changeWeatherButton;
	private JButton runButton;
	private JButton stopButton;
	private JSpinner ticksSpinner;
	private JButton exitButton;

	private JToolBar toolBar;

	public ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		toolBar = new JToolBar();
		initGUI();
	}

	private void initGUI() {

		loadEventsFileButton = new JButton(new ImageIcon("resources/icons/open.png"));
		loadEventsFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadEventsFile();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Error: Can't load file", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		loadEventsFileButton.setToolTipText("Load Events File to Simulator");

		changeCO2ClassButton = new JButton(new ImageIcon("resources/icons/co2class.png"));
		changeCO2ClassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCO2Class();
			}
		});
		changeCO2ClassButton.setToolTipText("Change CO2 Class of a Vehicle");

		changeWeatherButton = new JButton(new ImageIcon("resources/icons/weather.png"));
		changeWeatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeWeather();
			}
		});
		changeWeatherButton.setToolTipText("Change Weather of a Road");

		runButton = new JButton(new ImageIcon("resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (charged != null && charged) {
					enableToolBar(false);
					run();
				} else
					JOptionPane.showMessageDialog(null, "No file loaded", "Invalid Option", JOptionPane.WARNING_MESSAGE);
			}
		});
		runButton.setToolTipText("Run the simulator");

		stopButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		stopButton.setToolTipText("Stop the simulator");

		JLabel ticks = new JLabel("Ticks:");

		SpinnerModel value = new SpinnerNumberModel(10, 0, 10000, 1);
		ticksSpinner = new JSpinner(value);
		Dimension dimensionSpinner = ticksSpinner.getPreferredSize();
		dimensionSpinner.width = 80;
		dimensionSpinner.height = stopButton.getPreferredSize().height;
		ticksSpinner.setPreferredSize(dimensionSpinner);
		ticksSpinner.setMinimumSize(dimensionSpinner);
		ticksSpinner.setMaximumSize(dimensionSpinner);
		ticksSpinner.setToolTipText("Simulation tick to run: 1-10000");

		exitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		exitButton.setToolTipText("Exit the simulator");

		toolBar.add(loadEventsFileButton);
		toolBar.addSeparator();
		toolBar.add(changeCO2ClassButton);
		toolBar.add(changeWeatherButton);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(ticks);
		toolBar.add(ticksSpinner);
		toolBar.add(Box.createGlue());
		toolBar.addSeparator();
		toolBar.add(exitButton);

		add(toolBar);
		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.PAGE_START);

		this.setVisible(true);
	}

	private void loadEventsFile() throws Exception {
		charged = false;
		try {
			JFileChooser jfc = new JFileChooser("resources/examples");

			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				_ctrl.reset();
				_ctrl.loadEvents(new FileInputStream(selectedFile.getAbsolutePath()));
				charged = true;
			}
		} catch (Exception e) {
			throw new Exception("Failed to load: " + e.getMessage());
		}
	}

	private void changeCO2Class() {
		new ChangeCO2ClassDialog(_ctrl, (JFrame) SwingUtilities.getWindowAncestor(this));
	}

	private void changeWeather() {
		new ChangeWeatherDialog(_ctrl, (JFrame) SwingUtilities.getWindowAncestor(this));
	}

	private void run() {
		_stopped = false;
		run_sim((int) ticksSpinner.getValue());
	}

	private void exit() {
		int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
		if (input == 0)
			System.exit(0);
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void enableToolBar(boolean b) {
		if (loadEventsFileButton != null && changeCO2ClassButton != null && changeWeatherButton != null
				&& runButton != null && ticksSpinner != null && exitButton != null) {
			loadEventsFileButton.setEnabled(b);
			changeCO2ClassButton.setEnabled(b);
			changeWeatherButton.setEnabled(b);
			runButton.setEnabled(b);
			ticksSpinner.setEnabled(b);
			exitButton.setEnabled(b);
		}

	}

	private void stop() {
		_stopped = true;
		enableToolBar(true);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
	}

}
