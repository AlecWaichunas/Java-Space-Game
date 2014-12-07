package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class Rocket extends GameObject{
	
	private double damage;
	private boolean friendly = false;

	public Rocket(AffineTransform at, double speedx, double speedy, double damage){
		this.damage = damage;
		this.setVelX(speedx);
		this.setVelY(speedy);
		Polygon p = new Polygon();
		p.addPoint(5, 0);
		p.addPoint(10, 10);
		p.addPoint(10, 40);
		p.addPoint(0, 40);
		p.addPoint(0, 10);
		this.a = new Area(p);
		//Audio.playClip(Audio.rocket, -15f);
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
		g.setColor(Color.YELLOW);
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