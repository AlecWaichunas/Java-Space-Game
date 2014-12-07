package ludum.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ludum.screens.SubScreen;

public class Mouse implements MouseListener, MouseMotionListener{

	private SubScreen ss;
	
	public void setScreen(SubScreen ss){
		this.ss = ss;
	}
	
	public void mouseDragged(MouseEvent e) {
		ss.updateMousePos(e.getPoint());
	}

	public void mouseMoved(MouseEvent e) {
		ss.updateMousePos(e.getPoint());
	}

	public void mouseClicked(MouseEvent e) {
		ss.mouseClicked(e.getButton());
	}

	public void mousePressed(MouseEvent e) {
		ss.toggleMouse1(true);
	}

	public void mouseReleased(MouseEvent e) {
		ss.toggleMouse1(false);
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
