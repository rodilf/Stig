package com.mygdx.stig;

import com.badlogic.gdx.graphics.Texture;

public class ButtonBlock extends Block {
	
	public ButtonBlock(int x, int y, Texture texture) {
		super(x, y, texture);
//		setPosition(x, y);
	}
	
	public boolean hit(int x, int y) {
		if (x >= getX() && x <= getX() + getWidth()
				&& y >= getY() && y <= getY() + getHeight())
			return true;
		else
			return false;
	}
}