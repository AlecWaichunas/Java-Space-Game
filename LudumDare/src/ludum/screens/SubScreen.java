package ludum.screens;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class SubScreen {

	protected Dimension d;
	protected Point p = new Point(0,0);
	protected boolean Mouse1Down = false;
	
	public SubScreen(Dimension d){
		this.d = d;
		init();
	}
	
	protected abstract void init();
	public abstract void mouseClicked(int mouseButton);
	public abstract void update();
	public abstract void render(Graphics2D g);
	
	
	public void updateMousePos(Point p){
		this.p.setLocation(p);
	}
	
	public void toggleMouse1(boolean down){
		this.Mouse1Down = down;
	}
}
