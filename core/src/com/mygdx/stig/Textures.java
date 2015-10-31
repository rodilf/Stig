package com.mygdx.stig;

import com.badlogic.gdx.graphics.Texture;

public class Textures {
	//blocks
	Texture BLACK;
	Texture GRAY;
	Texture ENEMY;
	Texture BOMB;
//	 Texture BLUE;
	
	//selects
	Texture RED_SELECT;
	Texture GREEN_SELECT;
	Texture RED_CIRCLE;
	Texture GREEN_CIRCLE;
	
	//others
	Texture BACKGROUND;
	Texture MENUBACKGROUND;
	Texture LAUNCHMENU;
	Texture BIGBUTTON;
//	Texture LONGBUTTON;
	Texture GO;
	Texture NO;
	Texture BACKBUTTON;
	
	public Textures() {
		setup();
	}
	
	public void setup() {
		BLACK = new Texture("black.png");
		GRAY = new Texture("gray.png");
		ENEMY = new Texture("enemy1.png");
		BOMB = new Texture("circle.png");
		
		RED_SELECT = new Texture("select_red.png");
		GREEN_SELECT = new Texture("select_green.png");
		RED_CIRCLE = new Texture("select_red_circle.png");
		GREEN_CIRCLE = new Texture("select_green_circle.png");
		
		BACKGROUND = new Texture("background3.png");
		MENUBACKGROUND = new Texture("backgroundmenu2.png");
		LAUNCHMENU = new Texture("launchmenu4.png");
		BIGBUTTON = new Texture("buttonbig.png");
//		LONGBUTTON = new Texture("buttonlong.png");
		GO = new Texture("go.png");
		NO = new Texture("no.png");
		BACKBUTTON = new Texture("backarrow.png");
	}
	
	public void dispose() {
		BLACK.dispose();
		GRAY.dispose();
		ENEMY.dispose();
		BOMB.dispose();
		
		RED_SELECT.dispose();
		GREEN_SELECT.dispose();
		RED_CIRCLE.dispose();
		GREEN_CIRCLE.dispose();
		
		BACKGROUND.dispose();
		MENUBACKGROUND.dispose();
		LAUNCHMENU.dispose();
		BIGBUTTON.dispose();
//		LONGBUTTON.dispose();
		GO.dispose();
		NO.dispose();
		BACKBUTTON.dispose();
	}
	
//	 TextureRegion BLACK = new TextureRegion(new Texture("black.png"));
//	 TextureRegion GRAY = new TextureRegion(new Texture("gray.png"));
//	 TextureRegion ENEMY = new TextureRegion(new Texture("enemy1.png"));
//	 TextureRegion BOMB = new TextureRegion(new Texture("circle.png"));
//	 TextureRegion BLUE = new TextureRegion(new Texture("blue.png"));
//	
//	//selects
//	 TextureRegion RED = new TextureRegion(new Texture("select_red.png"));
//	 TextureRegion GREEN = new TextureRegion(new Texture("select_green.png"));
//	 TextureRegion RED_CIRCLE = new TextureRegion(new Texture("select_red_circle.png"));
//	 TextureRegion GREEN_CIRCLE = new TextureRegion(new Texture("select_green_circle.png"));
//	
//	//others
//	 TextureRegion BACKGROUND = new TextureRegion(new Texture("background3.png"));
//	 TextureRegion MENUBACKGROUND = new TextureRegion(new Texture("backgroundmenu2.png"));
//	 TextureRegion LAUNCHMENU = new TextureRegion(new Texture("launchmenu4.png"));
//	 TextureRegion BIGBUTTON = new TextureRegion(new Texture("buttonbig.png"));
//	 TextureRegion LONGBUTTON = new TextureRegion(new Texture("buttonlong.png"));
//	 TextureRegion GO = new TextureRegion(new Texture("go.png"));
//	 TextureRegion NO = new TextureRegion(new Texture("no.png"));
//	 TextureRegion BACKBUTTON = new TextureRegion(new Texture("backarrow.png"));
	
	
//	public  TextureRegion typeToTexture(int type) {
//		TextureRegion returnTexture;
//		switch (type) {
//		case 0: returnTexture = BLACK;
//		break;
//		case 1: returnTexture = BOMB;
//		break;
//		case 2: returnTexture = GRAY;
//		break;
//		case 3: returnTexture = BLUE;
//		break;
//		case 4: returnTexture = RED;
//		break;
//		case 5: returnTexture = GREEN;
//		break;
//		case 6: returnTexture = RED_CIRCLE;
//		break;
//		case 7: returnTexture = GREEN_CIRCLE;
//		break;
//		
//		default: returnTexture = BLACK;
//		break;
//		}
//		return returnTexture;
//	}
}