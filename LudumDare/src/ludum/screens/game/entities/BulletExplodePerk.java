package ludum.screens.game.entities;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import ludum.screens.game.GameScreen;

public class BulletExplodePerk extends Perk{

	private GameScreen gs;
	
	public BulletExplodePerk(MainTurret mt, Dimension screenSize, GameScreen gs) {
		super(mt, screenSize);
		this.gs = gs;
		Polygon stripe1 = new Polygon();
		stripe1.addPoint(0, 0);
		stripe1.addPoint(0, 6);
		stripe1.addPoint(18, 6);
		stripe1.addPoint(18, 0);
		Polygon stripe2 = new Polygon(stripe1.xpoints, stripe1.ypoints, stripe1.npoints);
		stripe2.translate(0, 8);
		Polygon stripe3 = new Polygon(stripe1.xpoints, stripe1.ypoints, stripe1.npoints);
		stripe3.translate(0, 16);
		Polygon head = new Polygon();
		head.addPoint(0, 3);
		head.addPoint(8, 3);
		head.addPoint(15, 10);
		head.addPoint(15, 20);
		head.addPoint(8, 27);
		head.addPoint(0, 27);
		
		head.translate(35, 11);
		
		stripe1.translate(15, 15);
		stripe2.translate(15, 15);
		stripe3.translate(15, 15);
		a.subtract(new Area(head));
		a.subtract(new Area(stripe1));
		a.subtract(new Area(stripe2));
		a.subtract(new Area(stripe3));
	}

	protected void onCollide() {
		for(int i = 0; i < 360; i += 10){
			double y = Math.sin(i), x = Math.cos(i);
			AffineTransform at = new AffineTransform();
			at.translate(getPosition().getX(), getPosition().getY());
			at.rotate(i + Math.PI/2, 4, 0);
			Bullet b = new Bullet(at, x * 3, y * 3, 3);
			b.setFriendly(true);
			gs.addBullet(b);
		}
	}

}
