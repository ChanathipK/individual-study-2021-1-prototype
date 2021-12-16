package application.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import application.utility.CSVUtility;
import gui.element.GameHUD;
import gui.element.MoneyBox;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import logic.level.Level;
import platform.Coin;
import platform.FinishFlag;
import platform.HurtPlatform;
import platform.base.Collectable;
import platform.base.Damagable;
import platform.Book;
import player.Character;
import player.Enemy;
import player.Player;
import sharedObject.RenderableHolder;
import view.GameScene;
import view.ViewManager;

public class GameManager {
	public static HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private static ArrayList<Node> platforms = new ArrayList<Node>();

	private static Player player;
	private static boolean canJump;
	private static int levelWidth;
	private static boolean isMute;
	private static boolean isPressedNextLv;

	private static int levelCount;

	private static AnchorPane appRoot = new AnchorPane();
	private static AnchorPane gameRoot = new AnchorPane();
	private static AnchorPane uiRoot = new AnchorPane();

	private static final int BLOCK_WIDTH = 120;
	private static final int BLOCK_HEIGHT = 120;

	// Time
	private static int time;

	// Level Finish Checker
	private static boolean isDead;
	private static int finishPositionX;
	private static int finishPositionY;
	private static boolean isLevelFinish;

	// Player's stat
	private static String playerName;
	private static int playerCurrentHP;
	private static int playerMaxHP;
	private static int playerCoin;
	private static int playerTotalCoin;
	private static int playerEXP;
	private static Random random = new Random();

	// Item
	private static ArrayList<Node> coins = new ArrayList<Node>();
	private static ArrayList<Node> books = new ArrayList<Node>();
	private static int bookCount;
	private static boolean isMissingBook = false;

	static {
		RenderableHolder.loadResource();
		initializeLevelCount();
		setLevelWidth();
		initializeBookCount();
		setLevelPlatform();
		initializePlayer();
		initializePlayerMaxHP();
		initializePlayerCurrentHP();
		initializePlayerCoin();
		initializePlayerEXP();
		addPlayerToGameRoot();
		setGameRootLayoutX();
		addGameRoot();
		addUIRoot();
		setCanJump(false);
		setIsMute(false);
		setTime(40);
		initializeKeysValue();
		setIsLevelFinish(false);
	}

	/*
	 * ============================== PRIVATE STATIC METHOD
	 * ==============================
	 */
	/* ==================== USE IN CONSTRUCTOR ==================== */

	private static void initializeLevelCount() {
		levelCount = 0;
	}

	private static void setLevelWidth() {
		levelWidth = 0;
		levelWidth = Level.ALL_LEVEL[levelCount][0].length() * BLOCK_WIDTH;
	}

	private static void initializeBookCount() {
		bookCount = 0;
	}

	private static void setLevelPlatform() {
		Rectangle background = new Rectangle(ViewManager.getScreenWidth(), ViewManager.getScreenHeight());
		background.setFill(new ImagePattern(RenderableHolder.normalLevelImage));
		for (int i = 0; i < Level.ALL_LEVEL[levelCount].length; i++) {
			String line = Level.ALL_LEVEL[levelCount][i];
			for (int j = 0; j < line.length(); j++) {
				ImageView platform;
				switch (line.charAt(j)) {
				case '0':
					break;
				case '1':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "1");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case '2':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "2");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case '3':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "3");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case '4':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "4");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case '5':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "5");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case 'A':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "f1");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case 'B':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "f2");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case 'C':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "f3");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case 'D':
					platform = RenderableHolder.createImageViewForPlatform(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, "f4");
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;

				// Item
				case 'M':
					platform = new Coin(RenderableHolder.coin, BLOCK_WIDTH, BLOCK_HEIGHT, j * BLOCK_WIDTH,
							i * BLOCK_HEIGHT);
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					// For collecting item logic
					coins.add(platform);
					break;
				case 'K':
					platform = new Book(RenderableHolder.bookOne, BLOCK_WIDTH, BLOCK_HEIGHT, j * BLOCK_WIDTH,
							i * BLOCK_HEIGHT);
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					// For collecting item logic
					books.add(platform);
					bookCount++;
					break;
				case 'k':
					platform = new Book(RenderableHolder.bookTwo, BLOCK_WIDTH, BLOCK_HEIGHT, j * BLOCK_WIDTH,
							i * BLOCK_HEIGHT);
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					books.add(platform);
					bookCount++;
					break;
				case 'H':
					platform = new HurtPlatform(RenderableHolder.hurtPlatformOne, BLOCK_WIDTH, BLOCK_HEIGHT,
							j * BLOCK_WIDTH, i * BLOCK_HEIGHT);
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					break;
				case 'X':
					platform = new FinishFlag(RenderableHolder.finish, BLOCK_WIDTH, BLOCK_HEIGHT, j * BLOCK_WIDTH,
							i * BLOCK_HEIGHT);
					gameRoot.getChildren().add(platform);
					platforms.add(platform);
					finishPositionX = j * BLOCK_WIDTH;
					finishPositionY = i * BLOCK_HEIGHT;
					break;
				default:
					break;
				}
			}
		}
		appRoot.getChildren().add(background);
	}

	private static void initializePlayer() {
		player = new Player(RenderableHolder.spritePlayerStanding, 0, 0, 200, 1);
	}

	private static void initializePlayerMaxHP() {
		playerMaxHP = 250;
	}

	private static void initializePlayerCurrentHP() {
		playerCurrentHP = playerMaxHP;
	}

	private static void initializePlayerCoin() {
		playerCoin = 0;
		playerTotalCoin = 0;
	}

	private static void initializePlayerEXP() {
		playerEXP = 0;
	}

	private static void addPlayerToGameRoot() {
		gameRoot.getChildren().add(player);
		// test add player to platforms
		platforms.add(player);
	}

	private static void setGameRootLayoutX() {
		player.translateXProperty().addListener((observer, oldValue, newValue) -> {
			int offSet = newValue.intValue();
			if (offSet > 400 && offSet < levelWidth - 400) {
				gameRoot.setLayoutX(-(offSet - 400));
			}
		});
	}

	private static void addGameRoot() {
		appRoot.getChildren().add(gameRoot);
	}

	private static void addUIRoot() {
		appRoot.getChildren().add(uiRoot);
	}

	private static void setTime(int time) {
		GameManager.time = time;
	}

	private static void setCanJump(boolean canJump) {
		GameManager.canJump = canJump;
	}

	private static void initializeKeysValue() {
		keys.put(KeyCode.W, false);
		keys.put(KeyCode.A, false);
		keys.put(KeyCode.D, false);
	}

	private static void levelCountInclement() {
		levelCount++;
	}

	/* ==================== USED TO SET UP NEW LEVEL ==================== */

	private static void clearUIRoot() {
		uiRoot.getChildren().clear();
	}

	private static void clearGameRoot() {
		gameRoot.getChildren().clear();
	}

	private static void clearAppRoot() {
		appRoot.getChildren().clear();
	}

	private static void clearPlatforms() {
		platforms.clear();
	}

	private static void resetFinishPosition() {
		finishPositionX = 0;
		finishPositionY = 0;
	}

	/* ==================== USED IN update() METHOD ==================== */

	private static void movePlayerX(int value) {
		boolean movingRight = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingRight) {
						if (player.getTranslateX() + player.getWidth() + 1 == platform.getTranslateX()) {
							if (platform instanceof Collectable) {
							} else if (platform instanceof Damagable) {
								setPlayerCurrentHP(getPlayerCurrentHP() - 5 - random.nextInt(5));
								return;
							} else {
								return;
							}
						}
					} else {
						if (player.getTranslateX() == platform.getTranslateX() + BLOCK_WIDTH + 1) {
							if (platform instanceof Collectable) {
							} else if (platform instanceof Damagable) {
								setPlayerCurrentHP(getPlayerCurrentHP() - 5 - random.nextInt(5));
								return;
							} else {
								return;
							}
						}
					}
				}
			}
			player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
		}
	}

	private static void movePlayerY(int value) {
		boolean movingDown = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingDown) {
						if (player.getTranslateY() + player.getHeight() == platform.getTranslateY()) {
							if (platform instanceof Collectable) {
							} else if (platform instanceof Damagable) {
								setPlayerCurrentHP(getPlayerCurrentHP() - 1);
								setCanJump(true);
								return;
							} else {
								setCanJump(true);
								return;
							}
						}
					} else {
						if (player.getTranslateY() == platform.getTranslateY() + BLOCK_HEIGHT) {
							if (platform instanceof Collectable) {

							} else if (platform instanceof Damagable) {
								setPlayerCurrentHP(getPlayerCurrentHP() - 1);
								return;
							} else {
								return;
							}
						}
					}
				}
			}
			player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
		}
	}

	private static void jumpPlayer(int jumpHeight) {
		if (canJump) {
			player.setVelocityY(player.getVelocityY() - jumpHeight);
			setCanJump(false);
		}
	}

	private static boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	private static void setPlayerCurrentHP(int HP) {
		playerCurrentHP = HP;
	}

	private static void checkCoinCollect() {
		for (Node coin : coins) {
			boolean boundXLeftMin = coin.getTranslateX() <= player.getTranslateX() + player.getWidth();
			boolean boundXLeftMax = player.getTranslateX() + player.getWidth() <= coin.getTranslateX() + BLOCK_WIDTH;
			boolean boundXRightMin = coin.getTranslateX() <= player.getTranslateX();
			boolean d = player.getTranslateX() <= coin.getTranslateX() + BLOCK_WIDTH;

			// e, f, g, h check translateY
			boolean boundYTopMin = coin.getTranslateY() <= player.getTranslateY() + player.getHeight();
			boolean boundYTopMax = player.getTranslateY() + player.getHeight() <= coin.getTranslateY() + BLOCK_HEIGHT;
			boolean boundYBottomMin = coin.getTranslateY() <= player.getTranslateY();
			boolean boundYBottomMax = player.getTranslateY() <= coin.getTranslateY() + BLOCK_HEIGHT;

			if (((boundXLeftMin && boundXLeftMax) || (boundXRightMin && d))
					&& ((boundYTopMin && boundYTopMax) || (boundYBottomMin && boundYBottomMax))) {
				GameManager.setPlayerCoin(GameManager.getPlayerCoin() + 5);
				gameRoot.getChildren().remove(coin);
				platforms.remove(coin);
				coins.remove(coin);
				break;
			}
		}
	}

	private static void checkBookCollect() {
		for (Node book : books) {
			boolean boundXLeftMin = book.getTranslateX() <= player.getTranslateX() + player.getWidth();
			boolean boundXLeftMax = player.getTranslateX() + player.getWidth() <= book.getTranslateX() + BLOCK_WIDTH;
			boolean boundXRightMin = book.getTranslateX() <= player.getTranslateX();
			boolean d = player.getTranslateX() <= book.getTranslateX() + BLOCK_WIDTH;

			// e, f, g, h check translateY
			boolean boundYTopMin = book.getTranslateY() <= player.getTranslateY() + player.getHeight();
			boolean boundYTopMax = player.getTranslateY() + player.getHeight() <= book.getTranslateY() + BLOCK_HEIGHT;
			boolean boundYBottomMin = book.getTranslateY() <= player.getTranslateY();
			boolean boundYBottomMax = player.getTranslateY() <= book.getTranslateY() + BLOCK_HEIGHT;

			if (((boundXLeftMin && boundXLeftMax) || (boundXRightMin && d))
					&& ((boundYTopMin && boundYTopMax) || (boundYBottomMin && boundYBottomMax))) {
				gameRoot.getChildren().remove(book);
				platforms.remove(book);
				books.remove(book);
				break;
			}
		}
	}

	/*
	 * ============================== PUBLIC STATIC METHOD
	 * ==============================
	 */

	public static void update() {
		if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
			jumpPlayer(32);
		}
		if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
			movePlayerX(-5);
		}
		if (isPressed(KeyCode.D) && player.getTranslateX() <= levelWidth - 5 - player.getWidth()) {
			movePlayerX(5);
		}
		if (player.getVelocityY() < 10) {
			player.setVelocityY(player.getVelocityY() + 1);
		}
		movePlayerY(player.getVelocityY());
		player.update();
		checkCoinCollect();
		checkBookCollect();
		MoneyBox.updateMoneyText();
		
		// Check level finish
		if (player.getTranslateY() + player.getHeight() >= finishPositionY
				&& player.getTranslateX() + player.getWidth() == finishPositionX) {
			setIsLevelFinish(true);
			setIsPressedNextLv(false);
		}
		if (isLevelFinish && bookCount != Level.getBookCount(levelCount)) {
			setIsMissingBook(true);
		}

		if (player.getTranslateY() > 600) {
			setIsDead(true);
		}

		if (getPlayerCurrentHP() <= 0) {
			setIsDead(true);
		}
	}

	public static void setUpNextLevel() {
		setIsLevelFinish(false);
		levelCountInclement();
		resetFinishPosition();
		setLevelWidth();
		initializeBookCount();
		clearUIRoot();
		clearGameRoot();
		clearAppRoot();
		clearPlatforms();
		setLevelPlatform();
		initializePlayer();
		setPlayerCurrentHP(playerMaxHP);
		addPlayerToGameRoot();
		setGameRootLayoutX();
		addGameRoot();
		addUIRoot();
		setCanJump(true);
		setIsMute(false);
		setTime(120);
		initializeKeysValue();
		GameScene.createPauseGameLeaderboardSubScene();
	}

	/*
	 * ============================== GETTER/SETTER ==============================
	 */

	public static AnchorPane getAppRoot() {
		return appRoot;
	}

	public static void setAppRoot(AnchorPane appRoot) {
		GameManager.appRoot = appRoot;
	}

	public static void setIsMute(boolean isMute) {
		GameManager.isMute = isMute;
	}

	public static boolean getIsMute() {
		return isMute;
	}

	public static int getBlockWidth() {
		return BLOCK_WIDTH;
	}

	public static int getBlockHeight() {
		return BLOCK_HEIGHT;
	}

	public static void setKeysValue(KeyCode keyCode, boolean value) {
		keys.put(keyCode, value);
	}

	public static boolean getKeysValue(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	public static void setIsLevelFinish(boolean isFinish) {
		isLevelFinish = isFinish;
	}

	public static boolean getIsLevelFinish() {
		return isLevelFinish;
	}

	public static void setIsPressedNextLv(boolean bool) {
		isPressedNextLv = bool;
	}

	public static boolean isPressedNextLv() {
		return isPressedNextLv;
	}

	public static int getLevelCount() {
		return levelCount;
	}

	public static AnchorPane getUIRoot() {
		return uiRoot;
	}

	public static int getTime() {
		return time;
	}

	/*
	 * ============================== SET PLAYER'S STATS
	 * ==============================
	 */

	public static void setPlayerMaxHP(int maxHP) {
		playerMaxHP = maxHP;
	}

	// setPlayerCurrentHP is not a public method

	public static void setPlayerCoin(int coin) {
		playerCoin = coin;
	}

	public static void setPlayerTotalCoin(int coin) {
		playerTotalCoin = coin;
	}

	public static void setPlayerEXP(int EXP) {
		playerEXP = EXP;
	}

	public static void setPlayerName(String name) {
		playerName = name;
	}

	public static void setIsDead(boolean isDead) {
		GameManager.isDead = isDead;
	}

	public static void setIsMissingBook(boolean isMissing) {
		isMissingBook = isMissing;
	}

	public static void makeJumpHigh() {
		GameManager.player.setVelocityY(player.getVelocityY() + 3);
	}

	public static void makeRunFast() {
		GameManager.player.setVelocityY(player.getVelocityX() + 3);
	}

	public static void upgradePlayerMaxHP() {
		int newHP = (int) ((1 + Math.random()) / 2) * GameManager.getPlayerMaxHP();
		GameManager.setPlayerMaxHP(newHP);
	}

	public static void upgradeMapTime() {
		int time = (int) ((int) ((1 + Math.random()) / 2) * GameScene.getTimeMapSecond());
		GameScene.setTimeMapSecond(time);
	}

	public static void updateLeaderboard(int timeRemained) {
		String name = GameManager.getPlayerName();
		String level = String.valueOf(GameManager.getLevelCount() + 1);
		int exp = GameManager.getPlayerEXP();
		int newExp = calculateNewExp(exp, timeRemained);
		String calculatedExp = String.valueOf(newExp);
		String[] data = { name, level, calculatedExp };
		CSVUtility.appendToCSV(data);
	}

	public static void updateLeaderboard() {
		String name = GameManager.getPlayerName();
		String level = String.valueOf(GameManager.getLevelCount() + 1);
		int exp = GameManager.getPlayerEXP();
		String calculatedExp = String.valueOf(exp);
		String[] data = { name, level, calculatedExp };
		CSVUtility.appendToCSV(data);
	}
	
	public static int calculateNewExp(int exp, int timeRemained) {
		int timeMap = (int) GameScene.getTimeMapSecond();
		int factor = (GameManager.levelCount + 1) * 100;
		double portion = (timeMap-timeRemained) / timeMap;
		int newExp = exp + (int) (portion * factor);
		GameManager.setPlayerEXP(newExp);
		return newExp;
	}
	
	public static int calculateFinalLevelExp() {
		int exp = GameManager.getPlayerEXP();
		if(exp<320) {
			exp = 320;
			GameManager.setPlayerEXP(exp);
			GameManager.updateLeaderboard();
		}
		return exp;
	}

	/*
	 * ============================== GET PLAYER'S STATS
	 * ==============================
	 */

	public static int getPlayerMaxHP() {
		return playerMaxHP;
	}

	public static int getPlayerCurrentHP() {
		return playerCurrentHP;
	}

	public static int getPlayerCoin() {
		return playerCoin;
	}

	public static int getPlayerTotalCoin() {
		return playerTotalCoin;
	}

	public static int getPlayerEXP() {
		return playerEXP;
	}

	public static boolean isDead() {
		return isDead;
	}

	public static boolean isMissingBook() {
		return isMissingBook;
	}

	public static String getPlayerName() {
		return playerName;
	}

}
