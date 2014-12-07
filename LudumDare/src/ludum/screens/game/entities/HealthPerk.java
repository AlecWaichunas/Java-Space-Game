package ludum.screens.game.entities;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.geom.Area;


public class HealthPerk extends Perk{

	public HealthPerk(MainTurret mt, Dimension screenSize) {
		super(mt, screenSize);
		Polygon p = new Polygon();
		p.addPoint(8, 0);
		p.addPoint(12, 0);
		p.addPoint(12, 8);
		p.addPoint(20, 8);
		p.addPoint(20, 12);
		p.addPoint(12, 12);
		p.addPoint(12, 20);
		p.addPoint(8, 20);
		p.addPoint(8, 12);
		p.addPoint(0, 12);
		p.addPoint(0, 8);
		p.addPoint(8, 8);
		p.translate(20, 15); 
		a.subtract(new Area(p));
	}

	protected void onCollide() {
		mt.addHealth(20);
	}

	

}
