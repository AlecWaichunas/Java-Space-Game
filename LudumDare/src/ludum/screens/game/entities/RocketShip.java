package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import ludum.screens.game.GameScreen;

public class RocketShip extends Mob{

	private Point position = new Point(0,0), center;
	private double rotation = 0;
	private GameScreen gs;
	
	private long timer = System.currentTimeMillis(),
				reloadTime = 2000;
	private boolean canShoot = true;
	
	public RocketShip(Dimension screenSize, GameScreen gs) {
		super(screenSize, 1.5);
		this.speed = 1;
		this.gs = gs;
		r.setSeed(System.currentTimeMillis());
		center = new Point((int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
		
		while(position.x >= 0 && position.x <= screenSize.width) 
			position.setLocation((r.nextInt(screenSize.width * 3)) - screenSize.width, 0);
		while(position.y >= 0 && position.y <= screenSize.height) 
			position.setLocation(position.x, (r.nextInt(screenSize.height * 3)) - screenSize.height);
		rotation = Math.atan2(center.y - position.y, center.x - position.x);
		
		Polygon p = new Polygon();
		p.addPoint(20, 0);
		p.addPoint(40, 35);
		p.addPoint(35, 40);
		p.addPoint(30, 30);
		p.addPoint(25, 35);
		p.addPoint(20, 30);
		p.addPoint(15, 35);
		p.addPoint(10, 30);
		p.addPoint(5, 40);
		p.addPoint(0, 35);
		this.setArea(new Area(p), Color.PINK);
	}

	public boolean collide(Rectangle r) {
		if(getArea().intersects(r)) return true;
		return false;
	}

	public void update(double xx, double yy) {
		position.setLocation(Math.cos(rotation) * speed + position.x, position.y + Math.sin(rotation) * speed);
		if(!isDead()){
			rotation = Math.atan2(yy - position.y, xx - position.x);
			if(canShoot){
				AffineTransform at = new AffineTransform();
				at.translate(position.x, position.y);
				at.rotate(rotation + Math.PI/2, 5, 20);
				double y = Math.sin(rotation), x = Math.cos(rotation);
				gs.addRocket(new Rocket(at, x * 8, y * 8, 10));
				canShoot = false;
				timer = System.currentTimeMillis();
			}	
		}else
			speed -= .01;
		if(System.currentTimeMillis() - timer >= reloadTime){
			canShoot = true;
		}
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
