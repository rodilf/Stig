package com.mygdx.stig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Stig extends ApplicationAdapter {
	SpriteBatch batch;
	
	static Client client;
	
	static String ip = "78.72.38.30";								//IP to connect to.
//	static String ip = "localhost";
	static int tcpPort = 33331, udpPort = 33331;					//Ports to connect on.
	
	List<Block> blocks;
	static Node[][] nodes;											//The nodes used for pathfinding.
	
	static int nodeamountX = 21;									//Amount of nodes created(width).
	static int nodeamountY = 25;									//Amount of nodes created(height).
	static int nodesize = 16;

	static int nodeOffsetY = 40;									//Y-offset for the item menu in-game.
	
	Enemy e;														//The enemy(the pathing thing).
	static List<Node> path;											//The path found via pathfinding.
	
	static Punkt startPunkt = new Punkt(10 * nodesize, 1 * nodesize + nodeOffsetY);				//Point where the pathfinding starts.
	static Punkt goalPunkt = new Punkt(10 * nodesize, 23 * nodesize + nodeOffsetY);				//Point where the pathfinding ends.
																	//Blocks used to select stuff in-game.
	Block redBlock;													//Appears if the placement of a wall block is invalid.
	Block greenBlock;												//Appears if the placement of a wall block is valid.
	Block redCircle;												//Appears if the placement of a bomb is invalid.
	Block greenCircle;												//Appears if the placement of a bomb is valid.
	
	Block wallButton;												//Button for selecting the wall block item.
	Block bombButton;												//Button for selecting the bomb item.
	Block goButton;													//Button making the enemy move.
	Block noButton;													//Button for cancelling the enemy movement.
	Block backButton;												//Button to go back to the menu.
	
	int selectId = 0;												//Indicate which item is selected.
	
	Punkt mouse = new Punkt();										//Mouse pointer position.
	Punkt mouseSnap = new Punkt();									//Mouse position snapping to grid.
	
	boolean nohinder;												//If the path is hindered or not.
	boolean bomb = false;											//Whether the block selected with the bomb item is eligible for termination.
	
	Block bomer;													//Block for easing the removal of a certain block(used with the bomb item).
	
	ShapeRenderer shape;											//For drawing the pathing line.
	
	boolean keyboard = false;										//if the keyboard input menu is open or not.
	boolean menu = true;											//If the menu is opened or not.
	boolean launchmenu = false;										//If the launch menu is open or not.
	
	boolean pathLine = true;										//if the pathing line is enabled or not.
	
	KeyboardMenu keyboardMenu;										//input menu
	
	GameMenu gameMenu;												//level selection menu.
	
	LaunchMenu launchMenu;											//The launch menu itself.
	
	int buttonId;													//Indicates which level is selected.
	
	static BitmapFont font24;										//18x18 pix
	static BitmapFont font34;
	static BitmapFont font54;
	
	int time = 0;													//Time (number of iterations required to get to the goal).
	
	static int resources;											//Amount of resources available (number of placeable blocks for each level).
	static int hinders;												//number of random generated hinder blocks for each level.
	
	static Block[] hinderBlock;										//The actual hinder blocks(currently limited to 8).
	
	String name;													//The name of the currently active player(needs some kind of login blah blah). 20
	
	String localscore;												//string for launchmenu localscore.
	
	char blank = ' ';
	
	static int id;													//Id(Number) of current level.
	
	Preferences prefs;												//Used for saving local highscores.
	
	ScorePacket activePacket;										//Packet of the currently active level id, score and name for highscore purposes.
	
	String[][] scoreString;											//Top 5 highscores for each level(aquired from the server).

    Camera camera;
    FitViewport viewport;
    
    float aspect = 1.0f;											//difference in size witdh when screen is scaled compared to preferred size(336 * 440).
    
    int worldX;														//used to convert from screen coords to game coords.
	int worldY;														//used to convert from screen coords to game coords.
	
	final int WIDTH = 336;											//preferred width.
	final int HEIGHT = 440;											//preferred height.
	
	public static Textures textures;
    
	@Override
	public void create() {
		client = new Client();
		client.getKryo().register(ScorePacket.class);
		client.getKryo().register(IdPacket.class);
		client.getKryo().register(StringPacket.class);
		client.getKryo().register(String[].class);
		client.start();
		
		scoreString = new String[16][];
		
		client.addListener(new Listener() {
			
			public void received(Connection c, Object o) {
				
				if(o instanceof StringPacket) {
					System.out.println("Received Score from Server.");
					scoreString[((StringPacket) o).id] = ((StringPacket) o).s;
					if(launchmenu)
						launchMenu.highScore = scoreString[((StringPacket) o).id];
				}
			}
		});
		
//		try {
//			client.connect(1000, ip, tcpPort, udpPort);
//		} catch (IOException e1) {
//			System.out.println("Connection Failed.");
//		}
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		batch = new SpriteBatch();
		textures = new Textures();
		
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		prefs = Gdx.app.getPreferences("prefs");
		
//		name = prefs.getString("name", "defvalue");
		if(true) {
//		if(name.equals("defvalue")) {
			keyboard = true;
			menu = false;
		}
		
		activePacket = new ScorePacket();
		
		keyboardMenu = new KeyboardMenu();
		gameMenu = new GameMenu();
		launchMenu = new LaunchMenu((WIDTH - textures.LAUNCHMENU.getWidth()) / 2, (int) (HEIGHT * 0.4f));
		
		shape = new ShapeRenderer();
		
		font24 = new BitmapFont(Gdx.files.internal("fonts/telegrama24.fnt"));
//		font0 = new BitmapFont();
		font34 = new BitmapFont(Gdx.files.internal("fonts/telegrama34.fnt"));
//		font1.scale(1);
		font54 = new BitmapFont(Gdx.files.internal("fonts/telegrama54.fnt"));
//		font2.scale(2);
		
//		random = new Random(1337);
		
		input();

		blocks = new ArrayList<Block>();
		
		nodeSetup();
		
		backButton = new Block(4, 4, textures.BACKBUTTON);
		wallButton = new Block((int) backButton.getX() + 112, 4, textures.GRAY);
		bombButton = new Block((int) wallButton.getX() + 40, 4, textures.BOMB);
		goButton = new Block((int) bombButton.getX() + 40, 4, textures.GO);
		noButton = new Block((int) bombButton.getX() + 40, 4, textures.NO);
		
		redBlock = new Block(1, 1, textures.RED_SELECT);
		greenBlock = new Block(1, 1, textures.GREEN_SELECT);
		redCircle = new Block(1, 1, textures.RED_CIRCLE);
		greenCircle = new Block(1, 1, textures.GREEN_CIRCLE);
		
		e = new Enemy();
		e.setPosition(startPunkt.x, startPunkt.y);
		
		hinderBlock = new Block[8];
		for (int c = 0; c < 8; ++c) {
			hinderBlock[c] = new Block(0, 0, textures.BLACK);
		}
		
		shape.setProjectionMatrix(viewport.getCamera().combined);
		batch.setProjectionMatrix(viewport.getCamera().combined);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mouse.x = (int) (Gdx.input.getX() / aspect - viewport.getLeftGutterWidth() / aspect);
		mouse.y = (int) (reverseY(Gdx.input.getY()) / aspect - viewport.getBottomGutterHeight() / aspect);
		
		enemyMovement();
		
		batch.begin();
		
		batch.draw(textures.BACKGROUND, 0, nodeOffsetY);
		
		wallButton.draw(batch);
		bombButton.draw(batch);
		if(e.go)
			noButton.draw(batch);
		else
			goButton.draw(batch);
		font34.draw(batch, "" + time, goButton.getX() + 42, nodeOffsetY -4);
		drawBlocks();
		for(int i = 0; i < hinders; ++i) {
			hinderBlock[i].draw(batch);
		}
		
		e.draw(batch);
		
		selections();
		backButton.draw(batch);
		if(resources < 10)
			font34.draw(batch, "" + resources, wallButton.getX() - 42, nodeOffsetY -4);
		else
			font34.draw(batch, "" + resources, wallButton.getX() - 52, nodeOffsetY -4);	
		
		if(id < 10)
			font34.draw(batch, "" + id, viewport.getWorldWidth() - 24, viewport.getWorldHeight());
		else
			font34.draw(batch, "" + id, viewport.getWorldWidth() - 40, viewport.getWorldHeight());
		
		if (menu)
			gameMenu.draw(batch);
		if(launchmenu)
			launchMenu.draw(batch);
		if(keyboard)
			keyboardMenu.draw(batch);
		batch.end();
		
		if (!menu && !keyboard && pathLine)
			drawPathingLine();
	}
	
	
	public static List<Node> findPath(int oldX, int oldY, int newX, int newY) {
		List<Node> openList = new LinkedList<Node>();
		List<Node> closedList = new LinkedList<Node>();
		Node oldP = new Node(oldX, oldY);
		openList.add(oldP);
		Node current;
		
		while (true) {
			Collections.sort(openList);
			
			if (openList.isEmpty()) {
				System.out.println("nuller");
				return null;
			}
			current = openList.get(0);
			closedList.add(current);
			openList.remove(current);
			
			if (current.x == newX
					&& current.y == newY) {
//				System.out.println("path found!");
				return createPath(oldP, current);
			}
			Node[] adjacent = getAdjacent(current);
			for (int i = 0; i < 8; ++i) {
				if (!closedList.contains(adjacent[i])) {
					if (adjacent[i].walkable && testDiagonalAccess(adjacent, i)) {
						if (!openList.contains(adjacent[i])) {
							adjacent[i].parent = current;
							adjacent[i].h = Math.abs(adjacent[i].x - newX) + Math.abs(adjacent[i].y - newY);
							
							if (i == 0 || i == 2 || i == 5 || i == 7)
								adjacent[i].g = adjacent[i].parent.g + 14;
							else
								adjacent[i].g = adjacent[i].parent.g + 10;
							
							adjacent[i].f = adjacent[i].g + adjacent[i].h;
							openList.add(adjacent[i]);
						}
						else {
							if (i == 0 || i == 2 || i == 5 || i == 7) {
								if (current.g + 14 < adjacent[i].g) {
									adjacent[i].parent = current;
									adjacent[i].g = adjacent[i].parent.g + 14;
									adjacent[i].f = adjacent[i].g + adjacent[i].h;
								}
							}
							else {
								if (current.g + 10 < adjacent[i].g) {
									adjacent[i].parent = current;
									adjacent[i].g = adjacent[i].parent.g + 10;
									adjacent[i].f = adjacent[i].g + adjacent[i].h;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static Node[] getAdjacent(Node node) {					//Returns the nodes around the specified Node(N);
		Node[] adjacent = new Node[8];								//[0][1][2]
																	//[3][N][4]
		int it = 0;													//[5][6][7]
		
		for(int y = 1; y >= -1; --y) {
			for(int x = -1; x <= 1; ++x) {
				if(x == 0 && y == 0)
					continue;
				adjacent[it] = nodes[node.x / nodesize + x ][(node.y - nodeOffsetY) / nodesize + y];
				it++;
			}
		}	
		return adjacent;
	}
	
	static boolean testDiagonalAccess(Node[] node, int i) {			//Checks if you can walk diagonally or have to go around to get to the specified node. int i is the number of a node from getAdjacent(Node)
		if (i == 1 || i == 3 || i == 4 || i == 6)					//the node tested is not diagonal.
			return true;
		else if (i == 0) {
			if (node[1].walkable && node[3].walkable)				//checks if the nodes next to the diagonal node are walkable or not.
				return true;
		}
		else if (i == 2) {
			if (node[1].walkable && node[4].walkable)
				return true;
		}
		else if (i == 5) {
			if (node[3].walkable && node[6].walkable)
				return true;
		}
		else if (i == 7) {
			if (node[4].walkable && node[6].walkable)
				return true;
		}
		return false;
	}
	
	public static List<Node> createPath(Node start, Node goal) {
		List<Node> pat = new LinkedList<Node>();
		Node current = goal;
		
		while (true) {
			if (current.x == start.x
				&& current.y == start.y) {
				pat.add(current);
				break;
			}
			pat.add(current);
			current = current.parent;
		}
		Collections.reverse(pat);
		return pat;
	}
	
	void enemyMovement() {
		if (e.go) {
			e.move();
			++time;
		}

		if (e.goal) {
			e.goal = false;
			if(!prefs.contains("localhighscore" +id ) || prefs.getInteger("localhighscore" +id) < time) {
				prefs.putInteger("localhighscore" +id, time);
				activePacket.id = id;
				activePacket.name = name;
				activePacket.score = time;
				
				if(client.isConnected()) {
					System.out.println("Sending score to Server.");
					client.sendTCP(activePacket);
				}
				else
					System.out.println("Failed to send score to Server, not connected.");
			}
		}
	}
	
	static void blockNodes(int x, int y, boolean walkable) {
		nodes[x][y].walkable = walkable;
		nodes[x + 1][y].walkable = walkable;
		nodes[x][y + 1].walkable = walkable;
		nodes[x + 1][y + 1].walkable = walkable;
	}
	
	void setAllNodes(boolean walkable) {
		for (int i = 2; i < nodeamountX -2; ++i) {
			for (int j = 2; j < nodeamountY -2; ++j) {
				nodes[i][j].walkable = walkable;
			}
		}
	}
	
	void nodeSetup() {
		nodes = new Node[nodeamountX][nodeamountY];
		for (int i = 0; i < nodeamountX; ++i) {
			for (int j = 0; j < nodeamountY; ++j) {
				nodes[i][j] = new Node(i * nodesize, j * nodesize + nodeOffsetY);
				
				if (i == 0
						|| i == nodeamountX - 1
						|| j == 0
						|| j == nodeamountY - 1) {
					nodes[i][j].walkable = false;
				}
				if (i == 1
						|| i == nodeamountX - 2
						|| j == 1
						|| j == nodeamountY - 2) {
					nodes[i][j].walkable = false;
				}
			}
		}
		
		nodes[startPunkt.x / nodesize][(startPunkt.y - nodeOffsetY) /nodesize].walkable = true;
		nodes[goalPunkt.x / nodesize][(goalPunkt.y - nodeOffsetY) /nodesize].walkable = true;
	}
	
	static boolean testPlacing(int x, int y) {						//checks if the blocking the specified nodes would block the path or not, usually called after testNodes();
		blockNodes(x, y, false);
		path = findPath(startPunkt.x, startPunkt.y, goalPunkt.x, goalPunkt.y);
		if (path == null) {
			blockNodes(x, y, true);
			System.out.println("Path Blocked .:test:.");
			return false;
		}
		blockNodes(x, y, true);
		return true;
	}
	
	static boolean testNodes(int x, int y) {						//checks if any of the nodes the block would be placed on is blocked.
		if(!nodes[x][y].walkable
				|| !nodes[x +1][y].walkable
				|| !nodes[x][y +1].walkable
				|| !nodes[x +1][y +1].walkable)
			return false;
		
		return true;
	}
	
	void drawBlocks() {
		for (Block block : blocks) {
			block.draw(batch);
		}
	}
	
	void drawPathingLine() {
		shape.begin(ShapeType.Line);
		shape.setColor(Color.GREEN);
		
		if (path != null && !path.isEmpty())
			for (int i = 0; i < path.size() -1; ++i) {
				shape.line(path.get(i).x + nodesize /2, path.get(i).y + nodesize /2, path.get(i +1).x + nodesize /2, path.get(i +1).y + nodesize /2);
			}
		shape.end();
	}
	
	public int reverseY(int y) {
		return Gdx.graphics.getHeight() - y;
	}
	
	boolean mouseWalkable(int x, int y) {
		int i = x / nodesize;
		int j = (y - nodeOffsetY) / nodesize;
		if (i < 0
				|| i > nodeamountX -1
				|| j < 0
				|| j > nodeamountY -1) {
			return false;
		}
		return (testNodes(i, j));
	}
	
	void updateMouseSnap() {
		mouseSnap.x = mouse.x - (mouse.x - nodesize /2) % nodesize - nodesize /2;
		mouseSnap.y = mouse.y - (mouse.y - nodesize /2) % nodesize;

	}
	
	void selections() {
		updateMouseSnap();
		if (!e.go) {
			if (selectId == 1) {
				redBlock.setPosition(mouseSnap.x, mouseSnap.y);
				greenBlock.setPosition(mouseSnap.x, mouseSnap.y);
				
				if (mouseWalkable((int) redBlock.getX(), (int) redBlock.getY())) {
					nohinder = true;
					greenBlock.draw(batch);
				}
				else {
					nohinder = false;
					redBlock.draw(batch);
				}
			}
			else if (selectId == 2) {
				redCircle.setPosition(mouseSnap.x, mouseSnap.y);
				bomb = false;
				for (Block block : blocks) {
					if (block.hit(mouse.x, mouse.y)) {
						bomb = true;
						bomer = block;
						break;
					}
				}
				if(bomb) {
					greenCircle.setPosition((int) bomer.getX(),(int) bomer.getY());
					greenCircle.draw(batch);
				}
				else
					redCircle.draw(batch);
			}
		}
	}
	
	void input() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				
				worldX = (int) (screenX / aspect - viewport.getLeftGutterWidth() / aspect);
				worldY = (int) (reverseY(screenY) / aspect - viewport.getBottomGutterHeight() / aspect);
				
				if(keyboard) {
					if(keyboardMenu.hit(worldX, worldY)) {
						keyboard = false;
						menu = true;
						prefs.putString("name", keyboardMenu.putin);
					}
				}
				
				else if(launchmenu) {
					if(!launchMenu.launchMenu.hit(worldX, worldY))
						launchmenu = false;
					if(launchMenu.launchButton.hit(worldX, worldY)) {
						menu = false;
						launchmenu = false;
					}
				}
				
				else if(menu && !launchmenu && !keyboard) {
					if (gameMenu.hit(worldX, worldY)) {
						IdPacket idpacket = new IdPacket();
						idpacket.id = id;
						if(client.isConnected())
							client.sendTCP(idpacket);
						
						localscore = prefs.getString("name");
						
						for(int i = 0; i < 20 - prefs.getString("name").length() - lengthOfScore(prefs.getInteger("localhighscore" +id, 0)); ++i) {
							localscore += blank;
						}
						
						launchMenu.setup(id, localscore + prefs.getInteger("localhighscore" +id, 0), scoreString[id]);
						launchmenu = true;
					}
				}
				
				if(!menu && !launchmenu && !keyboard) {
					if (backButton.hit(worldX, worldY)) {
						blocks.clear();
						setAllNodes(true);
						menu = true;
						id = -1;
						time = 0;
					}
					else if (wallButton.hit(worldX, worldY)) {
						if(resources > 0)
							selectId = 1;
					}
					else if (bombButton.hit(worldX, worldY)) {
						selectId = 2;
					}
					
					if (e.go) {
						if (noButton.hit(worldX, worldY)) {
							time = 0;
							e.resetPos();
						}
					}
					else {
						if (goButton.hit(worldX, worldY)) {
							e.setPath(path);
							time = 0;
							e.go = true;
						}
					}
				}
				return true;
			}
			
			int lengthOfScore(int i) {
				if(i < 0)
					return -1;
				else if(i < 10)
					return 1;
				else if(i < 100)
					return 2;
				else
					return 3;
			}
			
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (!e.go) {
					if (selectId == 1
							&& nohinder) {
						blockNodes((int) greenBlock.getX() / nodesize, ((int) greenBlock.getY() - nodeOffsetY) / nodesize, false);
						path = findPath(startPunkt.x, startPunkt.y, goalPunkt.x, goalPunkt.y);
						if (path == null) {
							blockNodes((int) greenBlock.getX() / nodesize, ((int) greenBlock.getY() - nodeOffsetY) / nodesize, true);
							System.out.println("Path Blocked");
							path = findPath(startPunkt.x, startPunkt.y, goalPunkt.x, goalPunkt.y);
						}
						else {
							blocks.add(new Block((int) greenBlock.getX(), (int) greenBlock.getY(), textures.GRAY));
							resources -=1;
						}
					}
					if (selectId == 2
							&& bomb) {
//						System.out.println("Bomb");
						blocks.remove(bomer);
						resources +=1;
						blockNodes((int) bomer.getX() / nodesize, ((int) bomer.getY() - nodeOffsetY) / nodesize, true);
						path = findPath(startPunkt.x, startPunkt.y, goalPunkt.x, goalPunkt.y);
					}
				}
				selectId = 0;
				
				return true;
			}
		});
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		
		aspect = viewport.getViewportWidth() / viewport.getWorldWidth();
	}
	
	@Override
	public void pause() {
		prefs.flush();
//		textures.dispose();
	}
	
	@Override
	public void resume() {
//		batch = new SpriteBatch();
//		shape = new ShapeRenderer();
//		
//		font0 = new BitmapFont();
//		font1 = new BitmapFont();
//		font1.scale(1);
//		font2 = new BitmapFont();
//		font2.scale(2);
		textures.setup();
	}

	@Override
	public void dispose() {
		batch.dispose();
		shape.dispose();
		
		font24.dispose();
		font34.dispose();
		font54.dispose();
		
		textures.dispose();
	}
}