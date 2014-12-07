package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class Bullet extends GameObject{
	
	private double damage;
	private boolean friendly = false;

	public Bullet(AffineTransform at, double speedx, double speedy, double damage){
		this.damage = damage;
		this.setVelX(speedx);
		this.setVelY(speedy);
		Polygon p = new Polygon();
		p.addPoint(4, 0);
		p.addPoint(6, 0);
		p.addPoint(7, 3);
		p.addPoint(8, 5);
		p.addPoint(6, 5);
		p.addPoint(6, 7);
		p.addPoint(7, 7);
		p.addPoint(7, 14);
		p.addPoint(1, 14);
		p.addPoint(1, 7);
		p.addPoint(2, 7);
		p.addPoint(2, 5);
		p.addPoint(0, 5);
		p.addPoint(1, 3);
		this.a = new Area(p);
		
		a.transform(at);
	}
	
	public void setFriendly(boolean friendly){
		this.friendly = friendly;
	}
	
	public boolean isFriendly(){
		return friendly;
	}
	
	public void collide(Mob mob){
		if(a.intersects(mob.getBounds())){
			mob.takeDamage(.5);
		}
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.GRAY);
		p.setLocation(p.getX() + velX, p.getY() + velY);
		at.translate(velX, velY);
		a.transform(at);
		g.fill(a);
		at.translate(-velX, -velY);
		a.transform(at);
	}
	
	public double getDamage(){
		return damage;
	}
	
	public Rectangle getBounds(){
		return a.getBounds();
	}

}
