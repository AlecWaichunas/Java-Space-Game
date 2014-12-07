package ludum.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ludum.audio.Audio;
import ludum.screens.game.GameScreen;
import ludum.screens.game.entities.MainTurret;

public class TitleScreen extends SubScreen{

	private MainScreen ms;
	
	private Color lightGreen = new Color(0, 255, 0);
	private Color darkGreen = new Color(0, 120, 0);
	private Color uglyYellow = new Color(200, 200, 0);
	
	private Font title = new Font(Font.MONOSPACED, Font.PLAIN, 40);
	
	private String gameTitle = "STAR DESTROYER";
	private String[] options = {"play", "info"};
	private Rectangle[] clickButtons = new Rectangle[options.length];
	private int selected = -1;
	
	private MainTurret mt;
	
	public TitleScreen(Dimension d, MainScreen ms) {
		super(d);
		mt = new MainTurret(null, d);
		this.ms = ms;
		for(int i = 0; i < options.length; i++){
			clickButtons[i] = new Rectangle(d.width/2 - 300, d.height/4 + 105 + (i * 55), 600, 45);
		}
	}

	protected void init() {
	
	}

	public void mouseClicked(int mouseButton) {
		if(selected == 0){
			ms.changeScreen(new GameScreen(d, ms));
			Audio.playClip(Audio.bullet_fireed, -25f);
		}else if(selected == 1){
			ms.changeScreen(new InfoScreen(d, ms));
			Audio.playClip(Audio.bullet_fireed, -25f);
		}
	}

	public void update() {
		boolean found = false;
		for(int i = 0; i < clickButtons.length; i++){
			if(clickButtons[i].contains(p)){
				selected = i;
				found = true;
				break;
			}
		}
		if(!found) selected = -1;
		mt.update(0, 0);
		mt.setPos(p);
	}

	public void render(Graphics2D g) {
		mt.render(g);
		g.setFont(title);
		g.setColor(uglyYellow);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(gameTitle, d.width/2 - fm.stringWidth(gameTitle)/2, d.height/4 - 20);
		for(int i = 0; i < options.length; i++){
			if(selected == i) g.setColor(lightGreen);
			else g.setColor(darkGreen);
			g.drawString(options[i], d.width/2 - fm.stringWidth(options[i])/2, d.height/4 + 145 + (i * 55));
		}
		
	}

	
	
}
