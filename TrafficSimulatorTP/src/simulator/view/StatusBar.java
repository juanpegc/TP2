package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{

	private Controller _ctrl;
	private JLabel labelTime;
	private JLabel labelEvent;
	
	public StatusBar(Controller ctrl) {
		this._ctrl = ctrl;

		labelTime = new JLabel();
     	setTime(0);
        Dimension dimensionLabelTime = labelTime.getPreferredSize();
        dimensionLabelTime.width = 150;
        labelTime.setMinimumSize(dimensionLabelTime);
        labelTime.setPreferredSize(dimensionLabelTime);
        
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        Dimension dimensionSeparator = separator.getPreferredSize();
        dimensionSeparator.height = labelTime.getPreferredSize().height + 15;
        separator.setMinimumSize(dimensionSeparator);
        separator.setPreferredSize(dimensionSeparator);
         
        labelEvent = new JLabel("Welcome!");
        
        add(labelTime);
        add(separator);
        add(labelEvent);
        
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        setLayout(new FlowLayout(FlowLayout.LEFT, 10,0));
        this.setVisible(true);
	}
	
	public void setTime(int time) {
		if(labelTime != null) {
			labelTime.setText("Time: " + time);
		}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setTime(time);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setTime(time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		if(labelEvent != null) {
			labelEvent.setText("Event added " + e.toString());
		}
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
