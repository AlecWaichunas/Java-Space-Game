package ludum;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import ludum.events.WindowEvents;
import ludum.screens.MainScreen;

@SuppressWarnings("serial")
public class MainFrame extends Frame{

	private Dimension size = new Dimension(900, 900 * 3 / 4);
	
	public MainFrame(){
		
		MainScreen ms = new MainScreen(size);
		Engine e = new Engine(ms);
		
		setTitle("Star Destoryer");
		setResizable(false);
		add(ms, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		//setAlwaysOnTop(true);
		addWindowListener(new WindowEvents());
		setVisible(true);
		e.start();
	}
	
}
