package com.mygdx.stig;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy extends Sprite {
	private List<Node> path;
	
	boolean go;
	boolean goal;
	
	int speed = 2;
	float mx, my;
	
	int mit;								//Movement iterator
	
	public Enemy() {
		super(Stig.textures.ENEMY);
		go = false;
		goal = false;
	}
	
	public Enemy(List<Node> path) {
		super(Stig.textures.ENEMY);
		go = false;
		goal = false;
		setPath(path);
	}
	
	public void move() {					//behöver en översyn
		
		if (path.size() != mit) {
			
			this.setPosition(this.getX() + mx, this.getY() + my);
			
			if (this.getX() == path.get(mit +1).x
					&& this.getY() == path.get(mit +1).y) {
				++mit;
				
				if(path.size() - mit > 1) {
					mx = (path.get(mit +1).x - path.get(mit).x) /speed;
					my = (path.get(mit +1).y - path.get(mit).y) /speed;
				}
				else
					++mit;
			}
		}
		else
			resetPos();
	}
	
//	public void move() {					//behöver en översyn
//		
//		if (!path.isEmpty()) {
//			
//			this.setPosition(this.getX() + mx, this.getY() + my);
//			
//			if (this.getX() == path.get(1).x
//					&& this.getY() == path.get(1).y) {
//				path.remove(0);
////				++mit;
//				
//				if(path.size() > 1) {
////				if(path.size() - mit > 1) {
//					mx = (path.get(1).x - path.get(0).x) /speed;
//					my = (path.get(1).y - path.get(0).y) /speed;
//				}
//				else
////					++mit;
//					path.remove(0);
//			}
//		}
//		else
//			resetPos();
//	}
	
	public void setPath(List<Node> path) {
		if (path == null) {
			//Do Nothing
		}
		else if (!path.isEmpty()) {
//			this.path = new LinkedList<Node>(path);
			this.path = path;
			mit = 0;
			
			setPosition(path.get(0).x, path.get(0).y);
			
			mx = (path.get(1).x - path.get(0).x) /2;
			my = (path.get(1).y - path.get(0).y) /2;
		}
	}
	
	public void resetPos() {
		goal = true;
		go = false;
		
		setPosition(Stig.startPunkt.x, Stig.startPunkt.y);
	}
}