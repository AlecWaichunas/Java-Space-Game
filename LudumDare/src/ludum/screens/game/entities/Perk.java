package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.Random;


public abstract class Perk extends GameObject{

	protected MainTurret mt;
	protected Area a;
	
	private Random r = new Random();
	private double velX, velY;
	private Point position = new Point(0,0);
	private Color caseColor = new Color(0,120, 0);
	private Dimension screenSize;
	private boolean wasOnScreen = false, onScreen = false;
	
	public Perk(MainTurret mt, Dimension screenSize){
		this.mt = mt;
		this.screenSize = screenSize;
		while(position.x >= 0 && position.x <= screenSize.width) 
			position.setLocation((r.nextInt(screenSize.width * 3)) - screenSize.width, 0);
		while(position.y >= 0 && position.y <= screenSize.height) 
			position.setLocation(position.x, (r.nextInt(screenSize.height * 3)) - screenSize.height);
		velX = (position.x < 0) ? r.nextDouble() * 1.25 : -r.nextDouble() * 1.25;
		velY = (position.y < 0) ? r.nextDouble() * 1.25 : -r.nextDouble() * 1.25;
		Polygon p = new Polygon();
		p.addPoint(0, 10);
		p.addPoint(10, 10);
		p.addPoint(15, 5);
		p.addPoint(20, 0);
		p.addPoint(40, 0);
		p.addPoint(45, 5);
		p.addPoint(50, 10);
		p.addPoint(40, 10);
		p.addPoint(35, 5);
		p.addPoint(25, 5);
		p.addPoint(20, 10);
		p.addPoint(60, 10);
		p.addPoint(60, 40);
		p.addPoint(0, 40);
		p.addPoint(0, 0);
		a = new Area(p);
	}
	
	public boolean collide(){
		if(getBounds().intersects(mt.getBounds())){
			onCollide();
			return true;
		}
		return false;
	}
	
	public void update(){
		position.setLocation(position.x + velX, position.y + velY);
		if(position.x > 0 && position.x < screenSize.width && position.y > 0 && position.y < screenSize.height){
			onScreen = true;
		}else if(onScreen){
			wasOnScreen = true;
			onScreen = false;
		}
	}
	
	protected abstract void onCollide();
	
	public void render(Graphics2D g){
		g.setColor(caseColor);
		g.translate(position.x, position.y);
		g.fill(a);
		g.translate(-position.x, -position.y);
	}
	
	public Rectangle getBounds(){
		Rectangle r = a.getBounds();
		r.setLocation(position.x, position.y);
		return r;
	}
	
	protected void changeCaseColor(Color color){
		caseColor = color;
	}
	
	protected Point getPosition(){
		return position;
	}
	
	public boolean wasOnScreen(){
		return wasOnScreen;
	}
	
}
