package ludum.screens.game.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {

	protected Area a;
	protected Point2D p = new Point2D.Double(0,0);
	protected double velX = 0, velY = 0, rotation = 0;
	protected AffineTransform at = new AffineTransform();

	
	public boolean collide(Rectangle2D r){
		if(a.intersects(r)) return true;
		return false;
	}
	
	public void update(){	
	}

	public abstract void render(Graphics2D g);
	
	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}
	
	public Rectangle2D getBounds(){
		return a.getBounds();
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
