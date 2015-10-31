package com.mygdx.stig;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMenu {
	MenuButtonBlock[][] menuButtons;
	Random random;
	
	public GameMenu() {
		menuButtons = new MenuButtonBlock[4][4];
		random = new Random();
		
		int spacing = 80; //size of the button itself(64) is included.
		
		int jy = 360;
		int id = 0;
		for (int j = 0; j < 4; ++j) {
			int ix = 16;
			for (int i = 0; i < 4; ++i) {
				menuButtons[i][j] = new MenuButtonBlock(ix, jy, Stig.textures.BIGBUTTON);
				menuButtons[i][j].id = id;
				
				ix += spacing;
				id += 1;
			}
			jy -= spacing;
		}
	}
	
	boolean hit(int x, int y) {
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				if(menuButtons[i][j].hit(x, y)) {
					random.setSeed(menuButtons[i][j].id);
					Stig.id = menuButtons[i][j].id;
					Stig.resources = random.nextInt(12) + 7;
					Stig.hinders = random.nextInt(8);
					
					Punkt pTest;
					for(int c = 0; c < Stig.hinders; ++c) {
						while(true) {
							pTest = new Punkt(random.nextInt(Stig.nodeamountX -4) +2, random.nextInt(Stig.nodeamountY -4) +2);
							if(Stig.testNodes(pTest.x, pTest.y)) {
								if(Stig.testPlacing(pTest.x, pTest.y))
									break;
							}
						}
						Stig.hinderBlock[c].setPosition(pTest.x * Stig.nodesize, pTest.y * Stig.nodesize + Stig.nodeOffsetY);
						Stig.blockNodes(pTest.x, pTest.y, false);
					}
					Stig.path = Stig.findPath(Stig.startPunkt.x, Stig.startPunkt.y, Stig.goalPunkt.x, Stig.goalPunkt.y);
					
					return true;
				}
			}
		}
		return false;
	}
	
	void draw(SpriteBatch batch) {
		batch.draw(Stig.textures.MENUBACKGROUND, 0, 0);
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				menuButtons[i][j].draw(batch);
				
				if (menuButtons[i][j].id < 10)
					Stig.font54.draw(batch, "0" + menuButtons[i][j].id, menuButtons[i][j].getX() +2, menuButtons[i][j].getY() +56);
				else
					Stig.font54.draw(batch, "" + menuButtons[i][j].id, menuButtons[i][j].getX() +2, menuButtons[i][j].getY() +56);
			}
		}
	}
}