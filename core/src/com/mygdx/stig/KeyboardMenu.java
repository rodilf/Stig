package com.mygdx.stig;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class KeyboardMenu {
	
	ButtonBlock[] buttons;
	
	int buttonWidth = 32;
	int buttonHeight = 48;
	int spacing = 2;
	
	boolean caps = false;
	
	String putin;
	
	public KeyboardMenu() {
		buttons = new ButtonBlock[42];
		
		putin = new String("");
		
		for(int i = 0; i < 42; ++i) {
			buttons[i] = new ButtonBlock(0,0,Stig.textures.BIGBUTTON);
			buttons[i].setSize(buttonWidth, buttonHeight);
		}
		
		setup();
	}
	
	void draw(SpriteBatch batch) {
		batch.draw(Stig.textures.MENUBACKGROUND, 0, 0);
		batch.draw(Stig.textures.MENUBACKGROUND, 0, 0);
		for(int i = 0; i < 42; ++i) {
			buttons[i].draw(batch);
//			if(i == 11 && caps)
//				Stig.font1.draw(batch, Character.toString(Character.toUpperCase(getChar(i))), buttons[i].getX() + 1, buttons[i].getY() + 36);
			if(i == 38)
				Stig.font34.draw(batch, "space", buttons[i].getX() + 48, buttons[i].getY() + 36);
			else if(i == 41)
				Stig.font34.draw(batch, "end", buttons[i].getX() + 8, buttons[i].getY() + 36);
			else {
				if(caps)
					Stig.font34.draw(batch, Character.toString(Character.toUpperCase(getChar(i))), buttons[i].getX() + 7, buttons[i].getY() + 36);
				else
					Stig.font34.draw(batch, Character.toString(getChar(i)), buttons[i].getX() + 7, buttons[i].getY() + 36);
			}
		}
		Stig.font34.draw(batch, putin, buttonWidth *0.5f, buttonHeight *6);
	}
	
	void setCaps() {
		if(caps) {
			caps = false;
		}
		else {
			caps = true;
		}
	}
	
	boolean hit(int x, int y) {
		for(int i = 0; i < 42; ++i) {
			if(buttons[i].hit(x, y)) {
				if(i == 29) {	//caps lock
					setCaps();
				}
				else if(i == 37) {	//delete
					if(putin.length() > 0)
						putin = putin.substring(0, putin.length() -1);
				}
				else if(i == 41) {	//enter
					if(putin.length() > 0) {
						return true;
//						keyboard = false;
//						menu = true;
//						prefs.putString("name", keyboardMenu.putin);
					}
				}
				else {
//					putin += buttons[i].i;
					if(putin.length() < 14) {
						if(caps)
							putin += Character.toUpperCase(getChar(i));
						else
							putin += getChar(i);
					}
				}
			}
		}
		return false;
	}
	
	char getChar(int i) {
		switch(i) {
		case 0: return '1';
		case 1: return '2';
		case 2: return '3';
		case 3: return '4';
		case 4: return '5';
		case 5: return '6';
		case 6: return '7';
		case 7: return '8';
		case 8: return '9';
		case 9: return '0';
		
		case 10: return 'q';
		case 11: return 'w';
		case 12: return 'e';
		case 13: return 'r';
		case 14: return 't';
		case 15: return 'y';
		case 16: return 'u';
		case 17: return 'i';
		case 18: return 'o';
		case 19: return 'p';
		
		case 20: return 'a';
		case 21: return 's';
		case 22: return 'd';
		case 23: return 'f';
		case 24: return 'g';
		case 25: return 'h';
		case 26: return 'j';
		case 27: return 'k';
		case 28: return 'l';
		
		case 29: return '^';
		case 30: return 'z';
		case 31: return 'x';
		case 32: return 'c';
		case 33: return 'v';
		case 34: return 'b';
		case 35: return 'n';
		case 36: return 'm';
		case 37: return '<';
		
		case 38: return ' ';
		case 39: return ',';
		case 40: return '.';
		case 41: return '-';
		
		default: return '?';
		}
	}
	
	void setup() {
		int n = 0;
		int nx = 0;
		int ny = buttonHeight * 4 + spacing * 3;
		
		//row 1: digits
		for(int i = 0; i < 10; ++i) {
			buttons[n].setPosition(nx, ny);
			nx += buttonWidth + spacing;
			
			++n;
		}
		
		nx = 0;
		ny -= buttonHeight + spacing;
		
//		row 2: qwertyuiop
		for(int i = 0; i < 10; ++i) {
			buttons[n].setPosition(nx, ny);
			nx += buttonWidth + spacing;
			
			++n;
		}
		
		nx = buttonWidth / 2 + spacing / 2;
		ny -= buttonHeight + spacing;
		
//		row 3: asdfghjkl
		for(int i = 0; i < 9; ++i) {
			buttons[n].setPosition(nx, ny);
			nx += buttonWidth + spacing;
			++n;
		}
		
		nx = 0;
		ny -= buttonHeight + spacing;
		
//		row 4: [shift]zxcvbnm[del]
		for(int i = 0; i < 9; ++i) {
			buttons[n].setPosition(nx, ny);
			if(i == 0 || i == 8) {
				buttons[n].setSize(buttonWidth + (buttonWidth / 2 + spacing / 2), buttonHeight);
				nx += buttonWidth + (buttonWidth / 2 + spacing / 2) + spacing;
			}
			else
				nx += buttonWidth + spacing;
			
			++n;
		}
		
		nx = 0;
		ny -= buttonHeight + spacing;
		
//		row 5: [space],.[enter]
		for(int i = 0; i < 4; ++i) {
			buttons[n].setPosition(nx, ny);
			if(i == 0) {
				buttons[n].setSize(buttonWidth + (buttonWidth / 2 + spacing / 2) + spacing + 4 * buttonWidth + 3 * spacing, buttonHeight);
				nx += (buttonWidth + (buttonWidth / 2 + spacing / 2) + spacing + 4 * buttonWidth + 3 * spacing) + spacing;
			}
			else if(i == 3) {
				buttons[n].setSize(2 * buttonWidth + (buttonWidth / 2 + spacing / 2), buttonHeight);
				nx += (2 * buttonWidth + (buttonWidth / 2 + spacing / 2) + spacing);
			}
			else
				nx += buttonWidth + spacing;
			
			++n;
		}
	}
}