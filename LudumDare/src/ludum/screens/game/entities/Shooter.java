package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import ludum.audio.Audio;
import ludum.screens.game.GameScreen;

public class Shooter extends Mob{

	private Point position = new Point(0,0), center;
	private double rotation = 0;
	private GameScreen gs;
	
	private long timer = System.currentTimeMillis(),
				reloadTime = 2000;
	private boolean canShoot = true;
	
	public Shooter(Dimension screenSize, GameScreen gs) {
		super(screenSize, 1);
		this.speed = 2;
		this.gs = gs;
		r.setSeed(System.currentTimeMillis());
		center = new Point((int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
		
		while(position.x >= 0 && position.x <= screenSize.width) 
			position.setLocation((r.nextInt(screenSize.width * 3)) - screenSize.width, 0);
		while(position.y >= 0 && position.y <= screenSize.height) 
			position.setLocation(position.x, (r.nextInt(screenSize.height * 3)) - screenSize.height);
		rotation = Math.atan2(center.y - position.y, center.x - position.x);
		
		Polygon p = new Polygon();
		p.addPoint(0, 10);
		p.addPoint(5, 10);
		p.addPoint(10, 15);
		p.addPoint(15, 15);
		p.addPoint(15, 0);
		p.addPoint(20, 0);
		p.addPoint(30, 15);
		p.addPoint(35, 15);
		p.addPoint(40, 20);
		p.addPoint(35, 25);
		p.addPoint(30, 25);
		p.addPoint(20, 40);
		p.addPoint(15, 40);
		p.addPoint(15, 25);
		p.addPoint(10, 25);
		p.addPoint(5, 30);
		p.addPoint(0, 30);
		this.setArea(new Area(p), Color.CYAN);
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
				at.rotate(rotation + Math.PI/2, 4, 7);
				double y = Math.sin(rotation), x = Math.cos(rotation);
				Audio.playClip(Audio.bullet_fireed, -25f);
				gs.addBullet(new Bullet(at, x * 3, y * 3, 0.5));
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
		at.rotate(rotation, 20, 20);
		g.translate(position.x, position.y);
		
		getArea().transform(at);
		g.setColor(color);
		g.fill(getArea());
		
		at.rotate((-rotation) * 2, 20, 20);
		g.translate(-position.x, -position.y);
		getArea().transform(at);
	}
	
	public Rectangle getBounds(){
		Rectangle r = getArea().getBounds();
		r.setLocation(position.x, position.y);
		return r;
	}
	
}
