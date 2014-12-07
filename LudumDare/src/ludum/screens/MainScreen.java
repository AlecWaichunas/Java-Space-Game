package ludum.screens;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import ludum.events.Mouse;

@SuppressWarnings("serial")
public class MainScreen extends Canvas{

	private SubScreen ss;
	private Mouse m = new Mouse();
	private BufferedImage stars;
	
	public MainScreen(Dimension size){
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		addMouseListener(m);
		addMouseMotionListener(m);
		stars = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) stars.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < pixels.length; i++){
			if(Math.random() < .0025)
				pixels[i] = 0xFFFFFF;
			else
				pixels[i] = 0;
		}
		changeScreen(new TitleScreen(size, this));
	}
	
	public void changeScreen(SubScreen ss){
		this.ss = ss;
		m.setScreen(ss);
	}
	
	public void update(){
		ss.update();
	}
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.drawImage(stars, 0, 0, null);
		ss.render(g);
		
		bs.show();
		g.dispose();
	}
	
}
