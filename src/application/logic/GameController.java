package application.logic;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import player.Player;
import sharedObject.RenderableHolder;
import level.Level;
import logic.base.Jumpable;

public class GameController {
	private static HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private static ArrayList<Node> platforms = new ArrayList<Node>();
	private static Player player;
	private static boolean canJump;

	private String[] level;
	private static int levelWidth;
	private static boolean isMute = false;

	private static Pane appRoot = new Pane();
	private static Pane gameRoot = new Pane();
	private static Pane uiRoot = new Pane();

	public GameController() {
		setLevelWidth();
		setFirstLevelPlatform();
		player = new Player("/image/character_maleAdventurer_attack0.png", 5, 0, 50, 480);
		
		player.translateXProperty().addListener((obs, old, newValue) -> {
			int offSet = newValue.intValue();
			if (offSet > 400 && offSet < levelWidth - 400) {

			}
		});
	}

	public static int time =0;
	
	public static void platformStart() {
		initializePlatform();
		
		AnimationTimer timer = new AnimationTimer() {
			public void handle(long now) {
				update();
				Label t = new Label();
				t.setTextFill(Color.YELLOW);
				t.setText(String.valueOf(time/100));
				time+=1;
				if(uiRoot.getChildren().size()>0) uiRoot.getChildren().remove(0);
				uiRoot.getChildren().add(t);
			}
		};
		timer.start();
	}

	public static void update() {
		if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
			jumpPlayer();
		}
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
			movePlayerX(-5);
		}
		if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
			movePlayerX(5);
		}
		if (player.getVelocityY() < 10) {
			player.setVelocityY(player.getVelocityY() + 1);
		}
		movePlayerY((int) player.getVelocityY());
	}

	private static void movePlayerY(int value) {
		boolean movingDown = (value > 0);
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingDown) {
						if (player.getTranslateY() + 40 == platform.getTranslateY()) {
							return;
						}
					} else {
						if (player.getTranslateY() == platform.getTranslateY() + 60) {
							return;
						}
					}
				}
			}
			player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
		}
	}

	private static void movePlayerX(int value) {
		boolean movingRight = (value > 0);
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingRight) {
						if (player.getTranslateX() + 40 == platform.getTranslateX()) {
							return;
						}
					} else {
						if (player.getTranslateX() == platform.getTranslateX() + 60) {
							return;
						}
					}
				}
			}
			player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
		}
	}

	private static void jumpPlayer() {
		if (player instanceof Jumpable && canJump) {
			player.setVelocityY(player.getVelocityY() - 50);
			canJump = false;
		}
	}

	private static boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	private static void initializePlatform() {
		
	}

	private void setLevelWidth() {
		this.levelWidth = Level.LEVEL1[0].length() * 60;
	}

	private void setFirstLevelPlatform() {
		Rectangle bg = new Rectangle(800, 600);

		for (int i = 0; i < Level.LEVEL1.length; i++) {
			String line = Level.LEVEL1[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
				case '0':
					break;
				case '1':
					Node platform = createEntity(RenderableHolder.createImageViewForPlatform(j * 60, i * 60, "1"),j*60,i*60);
					platforms.add(platform);
					break;
				case '2':
					platforms.add(createEntity(RenderableHolder.createImageViewForPlatform(j * 60, i * 60, "2"),j,i));
					break;
				case '3':
					platforms.add(createEntity(RenderableHolder.createImageViewForPlatform(j * 60, i * 60, "3"),j,i));
					break;
				default:
					break;
				}
			}
		}
		Node p = createPlayer(new ImageView(RenderableHolder.entrance_background_Image),50,420);
		platforms.add(p);
		player = new Player("/image/character_maleAdventurer_attack0.png", 5, 0, 50, 420);
		player.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if (offset > 640 && offset < levelWidth - 640) {
				gameRoot.setLayoutX(-(offset - 640));
			}
		});
		
		appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
	}

	private Node createEntity(ImageView img,int x, int y) {
		Rectangle entity = new Rectangle(60,60);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(Color.AQUAMARINE);
		
		gameRoot.getChildren().add(entity);
	return entity;
	}
	
	private Node createPlayer(ImageView img,int x, int y) {
		Rectangle entity = new Rectangle(60,60);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(Color.DARKRED);
		
		gameRoot.getChildren().add(entity);
	return entity;
	}
	
	public static boolean IsMute() {
		return isMute;
	}

	public static void setMute(boolean isMute) {
		GameController.isMute = isMute;
	}
	private ImageView createImageViewForPlatform(int posX, int posY, int width, int height,String url) { 
		ImageView block = new ImageView(new Image(url));
		block.setFitWidth(width);
		block.setFitHeight(height);
		
		return block;
	}

	public static HashMap<KeyCode, Boolean> getKeys() {
		return keys;
	}

	public static void setKeys(HashMap<KeyCode, Boolean> keys) {
		GameController.keys = keys;
	}

	public ArrayList<Node> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(ArrayList<Node> platforms) {
		this.platforms = platforms;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String[] getLevel() {
		return level;
	}

	public void setLevel(String[] level) {
		this.level = level;
	}

	public int getLevelWidth() {
		return levelWidth;
	}

	public void setLevelWidth(int levelWidth) {
		this.levelWidth = levelWidth;
	}

	public static boolean isMute() {
		return isMute;
	}

	public static Pane getAppRoot() {
		return appRoot;
	}

	public void setAppRoot(Pane appRoot) {
		this.appRoot = appRoot;
	}

	public static Pane getGameRoot() {
		return gameRoot;
	}

	public void setGameRoot(Pane gameRoot) {
		this.gameRoot = gameRoot;
	}

	public static Pane getUiRoot() {
		return uiRoot;
	}

	public void setUiRoot(Pane uiRoot) {
		this.uiRoot = uiRoot;
	}

}
