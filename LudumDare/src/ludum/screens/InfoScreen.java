package ludum.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ludum.audio.Audio;
import ludum.screens.game.entities.MainTurret;

public class InfoScreen extends SubScreen{

	private MainScreen ms;
	private MainTurret mt;
	
	private Color lightGreen = new Color(0, 255, 0);
	private Color darkGreen = new Color(0, 120, 0);
	private Color uglyYellow = new Color(200, 200, 0);
	
	private Font title = new Font(Font.MONOSPACED, Font.PLAIN, 40);
	private Font f = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	private String info = "Star Destroyer was created in 48 hours for the    Ludum Dare 31, entire game on one screen."
			+ " Your    objective is to kill the surrounding ships and buy updrades for your ship in the shop.";
	
	private boolean backSelected = false;
	private Rectangle back;
	
	public InfoScreen(Dimension d, MainScreen ms) {
		super(d);
		this.ms = ms;
		mt = new MainTurret(null, d);
		back = new Rectangle((int) d.getWidth()/2 - 300, (int) d.getHeight() * 3 / 4, 600, 30);
	}

	protected void init() {

	}

	public void mouseClicked(int mouseButton) {
		if(backSelected){
			ms.changeScreen(new TitleScreen(d, ms));
			Audio.playClip(Audio.bullet_fireed, -25f);
		}
	}

	public void update() {
		if(back.contains(p)) backSelected = true;
		else backSelected = false;
		mt.update(0,0);
		mt.setPos(p);
	}

	public void render(Graphics2D g) {
		mt.render(g);
		g.setFont(title);
		g.setColor(uglyYellow);
		FontMetrics fm = g.getFontMetrics();
		g.drawString("Info", d.width/2 - fm.stringWidth("Info")/2, d.height/4 - 20);
		g.setColor(darkGreen);
		g.setFont(f);
		fm = g.getFontMetrics();
		//g.drawString(info, d.width/2 - 300, d.height/4 + 100);
		renderWordWrap(g, info, fm, d.width/2 - 300, d.height/4 + 100, 600);
		if(!backSelected) g.setColor(lightGreen);
		g.drawString("Back", d.width/2 - fm.stringWidth("Back")/2, back.y + 20);
	}
	
	private void renderWordWrap(Graphics2D g, String s, FontMetrics fm, int x, int y, int xConstraint){
		int lastIndex = 0;
		for(int i = 0; i < s.length(); i++){
			if(fm.stringWidth(s.substring(lastIndex, i)) > xConstraint){
				g.drawString(s.substring(lastIndex, i - 1), x, y);
				lastIndex = i - 1;
				y += fm.getHeight();
			}
		}
		g.drawString(s.substring(lastIndex, s.length()), x, y);
	}

}
