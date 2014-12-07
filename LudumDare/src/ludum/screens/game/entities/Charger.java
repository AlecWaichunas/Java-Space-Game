package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class Charger extends Mob{

	private Point position = new Point(0,0), center;
	private double rotation = 0;
	
	public Charger(Dimension screenSize) {
		super(screenSize, .5);
		this.speed = 4;
		r.setSeed(System.currentTimeMillis());
		center = new Point((int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
		
		while(position.x >= 0 && position.x <= screenSize.width) 
			position.setLocation((r.nextInt(screenSize.width * 3)) - screenSize.width, 0);
		while(position.y >= 0 && position.y <= screenSize.height) 
			position.setLocation(position.x, (r.nextInt(screenSize.height * 3)) - screenSize.height);
		rotation = Math.atan2(center.y - position.y, center.x - position.x);
		
		Polygon p = new Polygon();
		p.addPoint(20, 0);
		p.addPoint(40, 40);
		p.addPoint(35, 35);
		p.addPoint(30, 35);
		p.addPoint(25, 40);
		p.addPoint(15, 40);
		p.addPoint(10, 35);
		p.addPoint(5, 35);
		p.addPoint(0, 40);
		this.setArea(new Area(p), Color.RED);
	}

	public boolean collide(Rectangle r) {
		if(getArea().intersects(r)) return true;
		return false;
	}

	public void update(double xx, double yy) {
		position.setLocation(Math.cos(rotation) * speed + position.x, position.y + Math.sin(rotation) * speed);
		if(this.isDead())
			this.speed -= .1;
		else rotation = Math.atan2(yy - position.y, xx - position.x);
	}

	public void render(Graphics2D g) {
		AffineTransform at = new AffineTransform();
		at.rotate(rotation + Math.PI/2, 20, 20);
		g.translate(position.x, position.y);
		
		getArea().transform(at);
		g.setColor(color);
		g.fill(getArea());
		
		at.rotate((-rotation - Math.PI/2) * 2, 20, 20);
		g.translate(-position.x, -position.y);
		getArea().transform(at);
	}
	
	public Rectangle getBounds(){
		Rectangle r = getArea().getBounds();
		r.setLocation(position.x, position.y);
		return r;
	}
	
}
