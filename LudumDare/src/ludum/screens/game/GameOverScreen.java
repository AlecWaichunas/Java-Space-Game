package ludum.screens.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ludum.audio.Audio;
import ludum.screens.MainScreen;
import ludum.screens.SubScreen;
import ludum.screens.TitleScreen;
import ludum.screens.game.entities.MainTurret;

public class GameOverScreen extends SubScreen{

	private MainScreen ms;
	
	private Color lightGreen = new Color(0, 255, 0);
	private Color darkGreen = new Color(0, 120, 0);
	private Color uglyYellow = new Color(200, 200, 0);
	
	private Font title = new Font(Font.MONOSPACED, Font.PLAIN, 40);
	private Font f = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	
	private String[] options = {"Replay", "Main Menu"};
	private Rectangle[] clickButtons = new Rectangle[options.length];
	private int selected = -1;
	
	private MainTurret mt;
	private int score = 0;
	
	public GameOverScreen(Dimension d, MainScreen ms, MainTurret mt, long spawnTimeDifference, int points) {
		super(d);
		this.mt = mt;
		this.ms = ms;
		for(int i = 0; i < options.length; i++){
			clickButtons[i] = new Rectangle(d.width/2 - 100, d.height/2 + 35 + (i * 35), 200, 25);
		}
		
		score += points;
		score += spawnTimeDifference * 2;
		score += (mt.getMaxHealth() - 100) * 8;
		score += ((mt.getSpeed() - 2.5)/.25) * 80;
		score += (mt.getRotationSpeed() - 4) * 55;
		score += ((400 - mt.getReloadSpeed())/20) * 105;
		score += ((mt.getBulletDamage() - .5)/.1) * 130;
		score += ((mt.getBulletSpeed() - 3)/.2) * 105;
		score += (mt.isRockets()) ? 1500 : 0;
	}

	protected void init() {

	}

	public void mouseClicked(int mouseButton) {
		if(selected == 0){
			Audio.playClip(Audio.bullet_fireed, -25f);
			ms.changeScreen(new GameScreen(d, ms));
		}
		else if(selected == 1){
			Audio.playClip(Audio.bullet_fireed, -25f);
			ms.changeScreen(new TitleScreen(d, ms));
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
		g.drawString("Y O U    L O S T", d.width/2 - fm.stringWidth("Y O U    L O S T")/2, d.height/2 - 20);
		g.setFont(f);
		fm = g.getFontMetrics();
		g.drawString("Score: " + score, d.width/2 - fm.stringWidth("Score: " + score)/2, d.height/2 + 20);
		for(int i = 0; i < options.length; i++){
			if(selected == i) g.setColor(lightGreen);
			else g.setColor(darkGreen);
			g.drawString(options[i], d.width/2 - fm.stringWidth(options[i])/2, d.height/2 + 55 + (i * 35));
		}
	}

}
