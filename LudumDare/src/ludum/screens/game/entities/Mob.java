package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.Random;

public abstract class Mob {

	protected Area a;
	protected double health, maxHealth;
	protected Color color;
	protected Dimension screenSize;
	protected Random r = new Random();
	protected double speed = 1;
	
	public Mob(Dimension screenSize, double health){
		this.health = health;
		this.maxHealth = health;
		this.screenSize = screenSize;
	}
	
	public Area getArea(){
		return a;
	}
	
	protected void setArea(Area a, Color color){
		this.a = a;
		this.color = color;
	}
	
	public abstract boolean collide(Rectangle r);
	public abstract void update(double x, double y);
	public abstract void render(Graphics2D g);
	
	public void takeDamage(double damage){
		health -= damage;
	}
	
	public double getHealth(){
		return health;
	}
	
	public double getMaxHealth(){
		return maxHealth;
	}
	
	public Rectangle getBounds(){
		return getArea().getBounds();
	}
	
	public double getRandomDamage(){
		return (r.doubles(4).sum() * 10);
	}
	
	public boolean isDead(){
		return (health <= 0);
	}
	
	public void addTransparency(int number){
		int alpha = (color.getAlpha() + number) % 255;
		if(alpha < 0) alpha = 0;
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
	
	public int getTransparency(){
		return color.getAlpha();
	}
	
}
