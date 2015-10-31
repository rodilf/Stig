package com.mygdx.stig;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Block extends Sprite {
//	int type;
//	int range;
	
//	public Block(int x, int y, int type) {
//		super(Textures.typeToTexture(type).getTexture());
//		setPosition(x * Stig.nodesize, y * Stig.nodesize +Stig.nodeOffsetY);
////		this.type = type;
//	}
	
	public Block(int x, int y, Texture texture) {
		super(texture);
		setPosition(x, y);
	}
	
	public boolean hit(int x, int y) {
		if (x >= getX() && x <= getX() + getWidth()
				&& y >= getY() && y <= getY() + getHeight())
			return true;
		else
			return false;
	}
}