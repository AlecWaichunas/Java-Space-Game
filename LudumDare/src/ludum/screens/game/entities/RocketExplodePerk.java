package ludum.screens.game.entities;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import ludum.screens.game.GameScreen;

public class RocketExplodePerk extends Perk{

	private GameScreen gs;
	
	public RocketExplodePerk(MainTurret mt, Dimension screenSize, GameScreen gs) {
		super(mt, screenSize);
		this.gs = gs;
		Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(30, 0);
		p.addPoint(40, 10);
		p.addPoint(30, 20);
		p.addPoint(0, 20);
		p.translate(11, 15);
		a.subtract(new Area(p));
	}

	protected void onCollide() {
		for(int i = 0; i < 360; i += 10){
			double y = Math.sin(i), x = Math.cos(i);
			AffineTransform at = new AffineTransform();
			at.translate(getPosition().getX(), getPosition().getY());
			at.rotate(i + Math.PI/2, 4, 0);
			Rocket b = new Rocket(at, x * 3, y * 3, 15);
			b.setFriendly(true);
			gs.addRocket(b);
		}
	}
}
