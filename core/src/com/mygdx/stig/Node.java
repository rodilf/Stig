package com.mygdx.stig;

public class Node implements Comparable<Node> {
	Node parent;
	final int x, y;
	int f = 0;
	int g = 0;
	int h = 0;
	boolean walkable;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		
		walkable = true;
	}
	
	@Override
	public int compareTo(Node o) {
		if (this.f < o.f)
			return -1;
		else if (this.f > o.f)
			return 1;
		else return 0;
	}
}