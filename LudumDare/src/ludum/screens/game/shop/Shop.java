package ludum.screens.game.shop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import ludum.audio.Audio;
import ludum.screens.game.entities.MainTurret;

public class Shop {

	private boolean isVisible = false;
	private boolean backSelected = false;
	private MainTurret mt;
	private Dimension size;
	private Rectangle shopBorder;
	private String[] perks = {"Health", "Speed", "Rotation Speed", "Reload", 
			"Bullet Damage", "Bullet Speed", "Rockets"};
	private int selected = -1;
	private double[] amounts = new double[perks.length];
	private int[] perkPrice = {155, 80, 55, 105, 130, 105, 1500};
	private int[] maxAmount = {5000, 10, 15, 10, 20, 10, 1};
	private Polygon[] 	perkName = new Polygon[perks.length],
						amount = new Polygon[perks.length],
						price = new Polygon[perks.length],
						buy = new Polygon[perks.length];
	private Rectangle[] clickBoxes = new Rectangle[perks.length];
	private Rectangle backRectangle;
	
	private Color lightGreen = new Color(0, 255, 0);
	private Color darkGreen = new Color(0, 120, 0);
	private Color uglyYellow = new Color(200, 200, 0);
	private int points;
	
	private Font title = new Font(Font.MONOSPACED, Font.PLAIN, 40);
	private Font f = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	
	public Shop(Dimension size, MainTurret mt){
		this.size = size;
		this.mt = mt;
		Polygon name_template = new Polygon();
		name_template.addPoint(0, 0);
		name_template.addPoint((int) (size.width * .5D), 0);
		name_template.addPoint((int) (size.width * .5D) + 25, 25);
		name_template.addPoint((int) (size.width * .5D), 50);
		name_template.addPoint(0, 50);
		name_template.addPoint(25, 25);
		Polygon amount_template = new Polygon();
		amount_template.addPoint(0, 0);
		amount_template.addPoint(100, 0);
		amount_template.addPoint(100 + 20, 25);
		amount_template.addPoint(100, 50);
		amount_template.addPoint(0, 50);
		amount_template.addPoint(20, 25);
		Polygon price_template = new Polygon(amount_template.xpoints, 
				amount_template.ypoints, amount_template.npoints);
		Polygon buy_template = new Polygon(amount_template.xpoints, 
				amount_template.ypoints, amount_template.npoints);
		amount_template.translate((int) (size.width * .5D) + 25, 0);
		price_template.translate((int) (size.width * .5D) + 25 + 125, 0);
		buy_template.translate((int) (size.width * .5D) + 25 + 250, 0);
		shopBorder = new Rectangle(0, 50, (int) (size.width * .5D) + 25 + 375, 50 * perks.length);
		shopBorder.setLocation(size.width/2 - shopBorder.width/2, size.height/2 - (int) (shopBorder.height/1.5));
		for(int i = 0; i < perks.length; i++){
			perkName[i] = new Polygon(name_template.xpoints, 
					name_template.ypoints, name_template.npoints);
			amount[i] = new Polygon(amount_template.xpoints, 
					amount_template.ypoints, amount_template.npoints);
			price[i] = new Polygon(price_template.xpoints, 
				price_template.ypoints, price_template.npoints);
			buy[i] = new Polygon(buy_template.xpoints, 
					buy_template.ypoints, buy_template.npoints);
			
			clickBoxes[i] = new Rectangle( shopBorder.x + 
					perkName[i].getBounds().width + amount[i].getBounds().width + price[i].getBounds().width + 10,
					shopBorder.y + perkName[i].getBounds().height + (i * 50), 120, 50);
		}
		backRectangle = new Rectangle(size.width/2 - 200, perks.length * 50 + shopBorder.y + 125, 400, 50);
	}
	
	public int mouseClicked(Point mouse, int points){
		if(selected >= 0 && selected < perks.length){
			Audio.playClip(Audio.bullet_fireed, -25f);
			//buy
			if(perkPrice[selected] <= points){
				points -= perkPrice[selected];
				if(selected == 0){
					mt.addMaxHealth(mt.getMaxHealth() + 20);
					mt.addMaxHealth(80);//gives some health back :)
				}else if(selected == 1) mt.setSpeed(mt.getSpeed() + .25);
				else if(selected == 2) mt.setRotationSpeed(mt.getRotationSpeed() + 1);
				else if(selected == 3) mt.setReloadSpeed(mt.getReloadSpeed() - 20);
				else if(selected == 4) mt.setBulletDamage(mt.getBulletDamage() + .1);
				else if(selected == 5) mt.setBulletSpeed(mt.getBulletSpeed() + .2);
				else if(selected == 6) mt.setRockets(true);
			}
		}
		if(backSelected){
			Audio.playClip(Audio.bullet_fireed, -25f);
			this.setVisible(false, 0);
		}
		this.points = points;
		return points;
	}
	
	public void update(Point mouse){
		amounts[0] = mt.getMaxHealth();
		amounts[1] = mt.getSpeed();
		amounts[2] = mt.getRotationSpeed();
		amounts[3] = mt.getReloadSpeed()/100D;
		amounts[4] = mt.getBulletDamage();
		amounts[5] = mt.getBulletSpeed();
		amounts[6] = (mt.isRockets()) ? 1 : 0;
		boolean isOneSelected = false;
		for(int i = 0; i < perks.length; i++){
			if(amounts[i] < maxAmount[i] && clickBoxes[i].contains(mouse)){
				selected = i;
				isOneSelected = true;
				break;
			}
		}
		if(isOneSelected == false) selected = -1;
		if(backRectangle.contains(mouse)) backSelected = true;
		else backSelected = false;
	}
	
	public void render(Graphics2D g){
		g.setFont(title);
		g.setColor(darkGreen);
		FontMetrics fm = g.getFontMetrics();
		g.drawString("S H O P", size.width/2 - fm.stringWidth("S H O P")/2, 50);
		g.setFont(f);
		fm = g.getFontMetrics();
		g.drawString("Points: " + points, size.width/2 - fm.stringWidth("Points: " + points)/2, 100);
		g.drawString("U P G R A D E", shopBorder.x + (int) (size.width * .5D)/2 - 60, shopBorder.y + 30);
		g.drawString("#", shopBorder.x + (int) (size.width * .5D) + 50 + 30, shopBorder.y + 30);
		g.drawString("$", shopBorder.x + (int) (size.width * .5D) + 35 + 170, shopBorder.y + 30);
		g.drawString("+", shopBorder.x + (int) (size.width * .5D) + 35 + 290, shopBorder.y + 30);
		g.translate(shopBorder.x, shopBorder.y);
		for(int i = 0; i < perks.length; i++){
			g.translate(0, 50);
			if(i % 2 == 0) g.setColor(darkGreen);
			else g.setColor(lightGreen);
			g.fill(perkName[i]);
			g.fill(price[i]);
			g.fill(amount[i]);
			if(selected == i) g.setColor(uglyYellow);
			g.fill(buy[i]);
			if(i % 2 == 0) g.setColor(lightGreen);
			else g.setColor(darkGreen);
			g.draw(perkName[i]);
			g.drawString(perks[i], perkName[i].getBounds().width/2 - fm.stringWidth(perks[i])/2, 
					perkName[i].getBounds().height/2 + 10);
			g.draw(amount[i]);
			g.drawString(amounts[i] + "", 
					perkName[i].getBounds().width + 
					(amount[i].getBounds().width/2 - fm.stringWidth(amounts[i] + "")/2), 
					perkName[i].getBounds().height/2 + 10);
			g.draw(price[i]);
			g.drawString(perkPrice[i] + "", 
					perkName[i].getBounds().width + amount[i].getBounds().width +
					(price[i].getBounds().width/2 - fm.stringWidth(perkPrice[i] + "")/2), 
					perkName[i].getBounds().height/2 + 10);
			g.draw(buy[i]);
			g.drawString("+", 
					perkName[i].getBounds().width + amount[i].getBounds().width + price[i].getBounds().width +
					(buy[i].getBounds().width/2), 
					perkName[i].getBounds().height/2 + 10);
		}
		g.translate(0, perks.length * -50);
		g.translate(-shopBorder.x, -shopBorder.y);
		if(backSelected) g.setColor(darkGreen);
		else g.setColor(uglyYellow);
		g.drawString("B A C K", size.width/2 - fm.stringWidth("B A C K")/2, perks.length * 50 + shopBorder.y + 150);
	}
	
	public void setVisible(boolean visible, int points){
		this.isVisible = visible;
		this.points = points;
	}
	
	public boolean isVisible(){
		return isVisible;
	}
	
}
