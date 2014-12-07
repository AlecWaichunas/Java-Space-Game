package ludum.screens.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import ludum.audio.Audio;
import ludum.screens.MainScreen;
import ludum.screens.SubScreen;
import ludum.screens.game.entities.Bullet;
import ludum.screens.game.entities.BulletExplodePerk;
import ludum.screens.game.entities.Charger;
import ludum.screens.game.entities.Explosion;
import ludum.screens.game.entities.HealthPerk;
import ludum.screens.game.entities.MainTurret;
import ludum.screens.game.entities.Mob;
import ludum.screens.game.entities.Perk;
import ludum.screens.game.entities.Rocket;
import ludum.screens.game.entities.RocketExplodePerk;
import ludum.screens.game.entities.RocketShip;
import ludum.screens.game.entities.Shooter;
import ludum.screens.game.shop.Shop;

public class GameScreen extends SubScreen{

	private MainTurret mt;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Rocket> rockets = new ArrayList<Rocket>();
	private ArrayList<Mob> mobs;
	private ArrayList<Perk> perks;
	private ArrayList<Explosion> explosions;
	private long timer = System.currentTimeMillis(), spawnMob = 6000;
	private long timerForTimer = System.currentTimeMillis(), reduceSpawnMob = 1000 * 180;
	private Font f = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	private Color uglyYellow = new Color(200, 200, 0);
	private int points = 0;
	private Random r = new Random();
	private MainScreen ms;
	
	private Shop shop;
	private Rectangle shopButton;
	
	public GameScreen(Dimension d, MainScreen ms) {
		super(d);
		this.ms = ms;
	}
	
	protected void init() {
		mt = new MainTurret(this, this.d);
		shop = new Shop(d, mt);
		//spawn shooters
		mobs = new ArrayList<Mob>();
		perks = new ArrayList<Perk>();
		explosions = new  ArrayList<Explosion>();
		mobs.add(new Shooter(d, this));
		mobs.add(new Charger(d));
		perks.add(new RocketExplodePerk(mt, d, this));
		shopButton = new Rectangle(10, d.height - 90, 200, 25);
	}
	
	public void addBullet(Bullet b){
		bullets.add(b);
	}

	public void addRocket(Rocket r){
		rockets.add(r);
	}
	
	public void mouseClicked(int mouseButton) {
		if(shop.isVisible()){
			this.points = shop.mouseClicked(p, points);
			return;
		}
		if(shopButton.contains(this.p))
			shop.setVisible(true, points);
	}

	public void update() {
		if(shop.isVisible()){
			shop.update(this.p);
			return;
		}
		if(System.currentTimeMillis() - timer >= spawnMob){
			double number = r.nextDouble();
			if(number > .97)
				perks.add(new HealthPerk(mt, d));
			else if(number > .935)
				perks.add(new BulletExplodePerk(mt, d, this));
			else if(number > .9)
				perks.add(new RocketExplodePerk(mt, d, this));
			else if(number > .65)
				mobs.add(new Charger(d));
			else if(number > .5)
				mobs.add(new RocketShip(d, this));
			else
				mobs.add(new Shooter(d, this));
			timer = System.currentTimeMillis();
		}
		if(System.currentTimeMillis() - timerForTimer >= reduceSpawnMob){
			timerForTimer = System.currentTimeMillis();
			spawnMob -= 150;
			if(spawnMob <= 400){
				spawnMob = 400;
				reduceSpawnMob = 1000000000;
			}
		}
		mt.setPos(p);
		mt.update(0, 0);
		//mobs
		for(int s = 0; s < mobs.size(); s++){
			mobs.get(s).update(mt.getBounds().x + 20, mt.getBounds().y + 20);
			if((mt.collide(mobs.get(s).getBounds()) || mobs.get(s).collide(mt.getBounds())) && 
					mobs.get(s).getTransparency() > 100){
				mt.takeDamage((mobs.get(s).getTransparency()/255) * 5);
				//explosion
				Audio.playClip(Audio.explosion, -10f);
				explosions.add(new Explosion(mobs.get(s).getBounds().getLocation()));
				mobs.remove(s);
			}
			if(s < mobs.size() && mobs.get(s).isDead()){
				mobs.get(s).addTransparency(-5);
				if(mobs.get(s).getTransparency() >= 255) mobs.remove(s);
			}
		}
		//explosions
		for(int e = 0; e < explosions.size(); e++){
			explosions.get(e).update();
			if(explosions.get(e).clean()){
				explosions.remove(e);
			}
		}
		//perks
		for(int p = 0; p < perks.size(); p++){
			perks.get(p).update();
			if(perks.get(p).collide()) perks.remove(p);
			if(p < perks.size() && perks.get(p).wasOnScreen()) perks.remove(p);
		}
		//bullets
		for(int b = 0; b < bullets.size(); b++){
			bullets.get(b).update();
			if(!bullets.get(b).isFriendly() && 
					bullets.get(b).collide(mt.getBounds()) && mt.collide(bullets.get(b).getBounds())){
				mt.takeDamage(bullets.get(b).getDamage());
				bullets.remove(b);
			}else if(bullets.get(b).isFriendly()){
				for(int m = 0; m < mobs.size(); m++){
					if(!mobs.get(m).isDead() && b < bullets.size() &&
							bullets.get(b).collide(mobs.get(m).getBounds())){
						mobs.get(m).takeDamage(bullets.get(b).getDamage());
						bullets.remove(b);
						if(mobs.get(m).isDead()){
							mobs.get(m).addTransparency(-5);
							if(m < mobs.size()) 
								if(mobs.get(m) instanceof ludum.screens.game.entities.Shooter) points += 10;
								else if(mobs.get(m) instanceof ludum.screens.game.entities.RocketShip) points += 20;
								else if(mobs.get(m) instanceof ludum.screens.game.entities.Charger) points += 25;
						}
					}
				}
			}
			if(b < bullets.size()){
				Rectangle2D bounds = bullets.get(b).getBounds();
				if(bounds.getX() < 0 || bounds.getX() > d.width + 7 ||
						bounds.getY() < 0 || bounds.getY() > d.height + 7) bullets.remove(b);
			}
		}
		//copied from bullets above ROCKETS
		for(int b = 0; b < rockets.size(); b++){
			rockets.get(b).update();
			if(!rockets.get(b).isFriendly() && 
					rockets.get(b).collide(mt.getBounds()) && mt.collide(rockets.get(b).getBounds())){
				mt.takeDamage(rockets.get(b).getDamage());
				rockets.remove(b);
			}else if(rockets.get(b).isFriendly()){
				for(int m = 0; m < mobs.size(); m++){
					if(!mobs.get(m).isDead() && b < rockets.size() &&
							rockets.get(b).collide(mobs.get(m).getBounds())){
						mobs.get(m).takeDamage(rockets.get(b).getDamage());
						rockets.remove(b);
						if(mobs.get(m).isDead()){
							mobs.get(m).addTransparency(-5);
							if(m < mobs.size() && 
									mobs.get(m) instanceof ludum.screens.game.entities.Shooter) points += 10;
						}
					}
				}
			}
			if(b < rockets.size()){
				Rectangle2D bounds = rockets.get(b).getBounds();
				if(bounds.getX() < 0 || bounds.getX() > d.width + 10 ||
						bounds.getY() < 0 || bounds.getY() > d.height + 22) rockets.remove(b);
			}
		}
		if(this.Mouse1Down) mt.setMouseButton(true);
		else mt.setMouseButton(false);
		if(mt.isDead()){
			mobs.clear();
			bullets.clear();
			rockets.clear();
			explosions.clear();
			ms.changeScreen(new GameOverScreen(d, ms, mt, 6000 - spawnMob, points));
		}
	}

	public void render(Graphics2D g) {
		if(!shop.isVisible()) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for(Bullet b : bullets)
				b.render(g);				
			for(Rocket r : rockets){
				r.render(g);
			}
			for(Perk p : perks){
				p.render(g);
			}
		}
		
		mt.render(g);
		for(Mob m : mobs)
			m.render(g);
		for(Explosion e : explosions)
			e.render(g);
		//Main turret health
		g.setColor(Color.WHITE);
		g.drawRect(10, d.height - 40, 400, 30);
		g.setColor(Color.GREEN);
		for(int i = 0; i < 400; i += 25){
			if(mt.getHealth()/mt.getMaxHealth() < i/400D) break;
			g.fillRect(((4 * (i % 25)) + i) + 14, d.height - 40 + 4, 17, 22);
		}
		if(shop.isVisible()){
			shop.render(g);
			return;
		}
		//points
		g.setFont(f);
		g.drawString("Points: " + points, 10, d.height - 45);
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(10, d.height - 65 - 20, 95, 25, 10, 10);
		g.setColor(Color.GREEN);
		if(shopButton.contains(p)) g.setColor(uglyYellow);
		g.drawString("S H O P", 15, d.height - 65);
	}

}
