package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

	private Controller _ctrl;
	private Boolean _stopped;

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

		changeCO2ClassButton = new JButton(new ImageIcon("resources/icons/co2class.png"));
		changeCO2ClassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCO2Class();
			}
		});

		changeWeatherButton = new JButton(new ImageIcon("resources/icons/weather.png"));
		changeWeatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeWeather();
			}
		});

		runButton = new JButton(new ImageIcon("resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});

		stopButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});

		JLabel ticks = new JLabel("Ticks:");
		
		SpinnerModel value = new SpinnerNumberModel(10, 0, 10000, 1);
		ticksSpinner = new JSpinner(value);
		Dimension dimensionSpinner = ticksSpinner.getPreferredSize();
		dimensionSpinner.width = 80;
		dimensionSpinner.height = stopButton.getPreferredSize().height;
		ticksSpinner.setPreferredSize(dimensionSpinner);
		ticksSpinner.setMinimumSize(dimensionSpinner);
		ticksSpinner.setMaximumSize(dimensionSpinner);
		
		exitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		
		toolBar.add(loadEventsFileButton);
		toolBar.addSeparator();
		toolBar.add(changeCO2ClassButton);
		toolBar.add(changeWeatherButton);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(ticks);
		toolBar.add(ticksSpinner);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.addSeparator();
		toolBar.add(exitButton);
		
		add(toolBar);
		setLayout(new BorderLayout());
        add(toolBar, BorderLayout.PAGE_START);

        this.setVisible(true);
	}

	private void loadEventsFile() throws Exception {
		try {
			JFileChooser jfc = new JFileChooser("resources/examples");

			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				_ctrl.reset();
				_ctrl.loadEvents(new FileInputStream(selectedFile.getAbsolutePath()));
			}
		} catch (Exception e) {
			throw new Exception("Failed to load: " + e.getMessage());
		}
	}

	private void changeCO2Class() {
		new ChangeCO2ClassDialog(_ctrl, (JFrame) SwingUtilities.getWindowAncestor(this));
	}

	private void changeWeather() {
		
	}

	private void run() {
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
				// TODO show error message
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
		// TODO Auto-generated method stub

	}

	private void stop() {
		_stopped = true;
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
