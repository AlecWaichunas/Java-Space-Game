package ludum.screens.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Explosion extends GameObject{

	private Point pos;
	private int radius = 0;
	private int transparency = 255;
	private int maxRadius = 100;
	private Color innerColor = new Color(200, 200, 0, transparency),
				outerColor = new Color(200, 0, 0, transparency);
	
	public Explosion(Point pos){
		this.pos = pos;
	}
	
	public boolean clean(){
		if(radius >= maxRadius && transparency <= 0){
			return true;
		}
		return false;
	}
	
	
	public void update(){
		if(radius >= maxRadius){
			transparency -= 5;
			if(transparency < 0) transparency = 0;
			innerColor = new Color(200, 200, 0, transparency);
			outerColor = new Color(200, 0, 0, transparency);
		}else{
			radius += 4;
			transparency -= 4;
		}
	}
	
	public void render(Graphics2D g) {
		g.setColor(outerColor);
		g.fillOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);
		g.setColor(innerColor);
		g.fillOval(pos.x - radius/2, pos.y - radius/2, radius, radius);
		
	}

}
