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

public class MainTurret extends Mob{

	private Point mouse = new Point();
	private boolean mouseButtonDown = false;
	private int rotationS = 4;
	private double rotation = Math.toRadians(0), rotationSpeed = Math.toRadians(rotationS), goalRotation = 0;
	private Point center;
	private GameScreen gs;
	
	private boolean canShoot = true;
	private long timer = System.currentTimeMillis();
	private long reloadSpeed = 400;
	private double bulletDamage = .5;
	private double bulletSpeed = 3;
	private boolean rockets = false;
	
	public MainTurret(GameScreen gs, Dimension screenSize){
		super(screenSize, 100);
		this.gs = gs;
		this.speed = 2.5;
		center = new Point((int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
		Polygon p = new Polygon();
		p.addPoint(20, 0);
		p.addPoint(40, 40);
		p.addPoint(20, 30);
		p.addPoint(0, 40);
		this.setArea(new Area(p), Color.BLUE);
	}
	
	public boolean collide(Rectangle r) {
		if(getBounds().intersects(r)) return true;
		return false;
	}

	public void update(double xx, double yy) {
		if((rotation < 0 && rotation < goalRotation - Math.PI))
			rotation -= rotationSpeed;
		else if((rotation > 0 && rotation > goalRotation + Math.PI))
			rotation += rotationSpeed;
		else if(rotation > goalRotation + rotationSpeed)
			rotation -= rotationSpeed;
		else if(rotation < goalRotation - rotationSpeed)
			rotation += rotationSpeed;
		if(rotation < -Math.PI || rotation > Math.PI) 
			rotation = (rotation > 0) ? -Math.PI : Math.PI;
		
		double y = Math.sin(rotation), x = Math.cos(rotation);
		center.setLocation(center.x + x * speed, center.y + y * speed);
		
		if(mouseButtonDown && canShoot && gs != null){
			//shoot;
			AffineTransform at = new AffineTransform();
			at.translate(center.x, center.y);
			at.rotate(rotation + Math.PI/2, 4, 0);
			if(!isRockets()){
				Audio.playClip(Audio.bullet_fireed, -25f);
				Bullet b = new Bullet(at, x * bulletSpeed, y * bulletSpeed, bulletDamage);
				b.setFriendly(true);
				gs.addBullet(b);
			}else{
				Rocket b = new Rocket(at, x * bulletSpeed, y * bulletSpeed, bulletDamage);
				b.setFriendly(true);
				gs.addRocket(b);	
			}
			canShoot = false;
			timer = System.currentTimeMillis();
		}
		if(System.currentTimeMillis() - timer >= reloadSpeed){
			canShoot = true;
		}
	}

	public void render(Graphics2D g) {
		AffineTransform at = new AffineTransform();
		at.rotate(rotation + Math.PI/2, 20, 20);
		g.translate(center.x - 20, center.y - 20);
		
		getArea().transform(at);
		g.setColor(color);
		g.fill(getArea());

		g.translate(-center.x + 20, -center.y + 20);
		at.rotate((-rotation + Math.PI/2) * 2, 20, 20);
		getArea().transform(at);
	}
	
	public void setPos(Point p){
		mouse.setLocation(p);
		//Update Rotation
		goalRotation = Math.atan2(p.y - center.y, p.x - center.x);
	}
	
	
	//everything from down below is for the shop
	
	
	public void addHealth(double health){
		this.health += health;
		if(this.health > maxHealth) this.health = maxHealth;
	}
	
	public void setMouseButton(boolean down){
		this.mouseButtonDown = down;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public int getRotationSpeed(){
		return rotationS;
	}
	
	public void setRotationSpeed(int num){
		this.rotationS = num;
		this.rotationSpeed = Math.toRadians(num);
	}
	
	public long getReloadSpeed() {
		return reloadSpeed;
	}

	public void setReloadSpeed(long reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}

	public double getBulletDamage() {
		return bulletDamage;
	}

	public void setBulletDamage(double bulletDamage) {
		this.bulletDamage = bulletDamage;
	}

	public double getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(double bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public boolean isRockets() {
		return rockets;
	}

	public void setRockets(boolean rockets) {
		this.rockets = rockets;
	}
	
	public void addMaxHealth(double newMaxHealth){
		this.maxHealth = newMaxHealth;
	}


	public Rectangle getBounds(){
		Rectangle r = getArea().getBounds();
		r.setLocation(center.x - 20, center.y - 20);
		return r;
	}

}
