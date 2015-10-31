package com.mygdx.stig;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LaunchMenu {
	
	int x, y;
	int id;
	
	ButtonBlock launchMenu;
	ButtonBlock launchButton;
	
	String localScore;
	String[] highScore;
	
	public LaunchMenu(int x, int y) {
		highScore = new String[5];

		launchMenu = new ButtonBlock(x, y, Stig.textures.LAUNCHMENU);
		
		launchButton = new ButtonBlock(x +20, y + 44, Stig.textures.BIGBUTTON);
		launchButton.setSize(256, 64);
		
		this.x = x;
		this.y = y;
	}
	
	public void setup(int id, String localScore, String[] highScore) {
		this.id = id;
		this.localScore = localScore;

		if(!Stig.client.isConnected()) {
			for(int i = 0; i < 5; ++i)
				this.highScore[i] = "Not Connected";
		}
		else
			this.highScore = highScore;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(Stig.textures.MENUBACKGROUND, 0, 0);
		
		launchMenu.draw(batch);
		launchButton.draw(batch);
		
		for(int i = 0; i < 5; ++i) {
			Stig.font24.draw(batch, highScore[i], x +18, 386 - i*20);
		}
		Stig.font24.draw(batch, localScore, x +18, y +36);
		if(id < 10)
			Stig.font54.draw(batch, "Launch" + 0 + id, launchButton.getX() +8, launchButton.getY() +50);
		else
			Stig.font54.draw(batch, "Launch" + id, launchButton.getX() +8, launchButton.getY() +50);
	}
}