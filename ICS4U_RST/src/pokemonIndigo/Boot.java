package pokemonIndigo;

import java.awt.Color;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import simpleIO.Console;

public class Boot extends Application {

	// Declares window dimensions
	private final int CAMERAHEIGHT = 6;
	private final int CAMERAWIDTH = 10;

	// Player sprite location on gridpane
	private int playerStackX;
	private int playerStackY;

	// Direction of movement
	private String direction;

	// The map name used for transitions
	private String currentMapName;

	StackPane playerStack = new StackPane();

	Scene loading, scene;

	// Directional sprites
	ImageView playerUp = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerUp.png").toString());
	ImageView playerLeft = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerLeft.png").toString());
	ImageView playerRight = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerRight.png").toString());
	ImageView playerDown = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerDown.png").toString());

	// Default player sprite is upwards facing
	ImageView playerSprite = playerUp;

	GridPane root;

	TileGrid map;

	@Override
	public void start(Stage myStage) throws Exception {

		// Temp hardcoded map loading
		map = new TileGrid("Orilon Town", 1);
		currentMapName = "Orilon Town";
		playerStackX = 10;
		playerStackY = 11;
		direction = "Up";

		Pokemon temp = new Pokemon("Torchic", 35);

		// Declaring gridpane
		root = new GridPane();
		GridPane loadingPane = new GridPane();

		// call board display
		displayBoard(root);

		// Stack player sprite onto tile
		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), playerUp);
		root.add(playerStack, playerStackX, playerStackY);

		scene = new Scene(root);
		loading = new Scene(loadingPane);
		loading.setFill(javafx.scene.paint.Color.BLACK);

		myStage.setTitle("Test");
		myStage.setScene(scene);
		myStage.show();

		// Moving player WASD
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case W:
					myStage.setScene(loading);
					// Directional sprite
					playerSprite = playerUp;
					
					// Defines direction
					direction = "Up";
					if (map.getPlayerY() == 0) {

						nextMap(myStage);
					} else
					// Makes sure you're not flying over trees
					if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).getTexture() != true) {

						// Moves player's tilegrid location and stackpane location
						map.setPlayerY(-1);
						playerStackY--;

						// Displays new board
						displayBoard(root);
					}
					break;

				case A:
					playerSprite = playerLeft;
					direction = "Left";
					if (map.getPlayerX() == 0) {
						nextMap(myStage);
					} else if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).getTexture() != true) {
						map.setPlayerX(-1);
						playerStackX--;
						displayBoard(root);

					}
					break;

				case S:
					playerSprite = playerDown;
					direction = "Down";
					if (map.getPlayerY() == map.getMapHeight() - 1) {
						nextMap(myStage);
					} else if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).getTexture() != true) {
						map.setPlayerY(1);
						playerStackY++;
						displayBoard(root);
					}
					break;

				case D:
					playerSprite = playerRight;
					direction = "Right";
					if (map.getPlayerX() == map.getMapWidth() - 1) {
						nextMap(myStage);
					} else if (map.getTile(map.getPlayerY(), map.getPlayerX() + 1).getTexture() != true) {
						map.setPlayerX(1);
						playerStackX++;
						displayBoard(root);

					}
					break;

				default:
					break;

				}
			}
		});

	}

	public void nextMap(Stage myStage) {

		map.checkExit(currentMapName, map.getPlayerX(), map.getPlayerY());
		playerStackX = map.getPlayerSpawnX();
		playerStackY = map.getPlayerSpawnY();
		map = new TileGrid(map.getNextMap(), map.getNextSpawn());
		displayBoard(root);
		currentMapName = map.getName();
		myStage.setScene(scene);
	}

	public void displayBoard(GridPane root) {

		int displayUp = CAMERAHEIGHT;
		int displayDown = CAMERAHEIGHT;
		int displayLeft = CAMERAWIDTH;
		int displayRight = CAMERAWIDTH;
		int barrierCount = 0;
		boolean topBarrier = false;
		boolean botBarrier = false;
		boolean leftBarrier = false;
		boolean rightBarrier = false;

		// Adjusting camera based on 'out of bounds' areas, or barriers

		// BOTTOM
		if (map.getPlayerY() + CAMERAHEIGHT > map.getMapHeight()) {
			displayDown = CAMERAHEIGHT - (map.getPlayerY() + CAMERAHEIGHT - map.getMapHeight());
			displayUp = CAMERAHEIGHT + (map.getPlayerY() + CAMERAHEIGHT - map.getMapHeight());
			barrierCount++;
			botBarrier = true;
		}

		// RIGHT SIDE
		if (map.getPlayerX() + CAMERAWIDTH > map.getMapWidth()) {
			displayRight = CAMERAWIDTH - (map.getPlayerX() + CAMERAWIDTH - map.getMapWidth());
			displayLeft = CAMERAWIDTH + (map.getPlayerX() + CAMERAWIDTH - map.getMapWidth());
			barrierCount++;
			rightBarrier = true;
		}

		// TOP
		if (map.getPlayerY() - CAMERAHEIGHT < 0) {
			displayDown = CAMERAHEIGHT - (map.getPlayerY() - CAMERAHEIGHT);
			displayUp = CAMERAHEIGHT + (map.getPlayerY() - CAMERAHEIGHT);
			barrierCount++;
			topBarrier = true;
		}

		// LEFT SIDE
		if (map.getPlayerX() - CAMERAWIDTH < 0) {
			displayRight = CAMERAWIDTH - (map.getPlayerX() - CAMERAWIDTH);
			displayLeft = CAMERAWIDTH + (map.getPlayerX() - CAMERAWIDTH);
			barrierCount++;
			leftBarrier = true;
		}

		// Checking for barriers, adjusting playersprite appropriately

		// Top & Bottom
		if ((botBarrier == true || topBarrier == true) && leftBarrier != true && rightBarrier != true) {
			if (direction.equals("Left") && (map.getPlayerX() - 1) != (map.getMapWidth() - (CAMERAWIDTH + 1))) {
				playerStackX++;
			}
			if (direction.equals("Right") && (map.getPlayerX() + 1) != (CAMERAWIDTH + 1)) {
				playerStackX--;
			}
		}

		// Left & Right
		if ((leftBarrier == true || rightBarrier == true) && topBarrier != true && botBarrier != true) {
			if (direction.equals("Up") && (map.getPlayerY() - 1) != (map.getMapHeight() - (CAMERAHEIGHT + 1))) {
				playerStackY++;
			}
			if (direction.equals("Down") && (map.getPlayerY() - 1) != (CAMERAHEIGHT - 1)) {
				playerStackY--;
			}
		}

		// Player sprite is in direct middle if no barriers are encountered
		if (barrierCount == 0) {
			playerStackX = 10;
			playerStackY = 6;
		}

		// Clears gridpane to print new board
		root.getChildren().clear();

		int rootRow = -1;
		int rootCol = -1;

		// Prints tiles in correct gridpane locations, using player tileGrid location as
		// an epicenter
		for (int row = map.getPlayerY() - displayUp; row < map.getPlayerY() + displayDown; row++) {
			rootRow++;
			rootCol = -1;
			for (int col = map.getPlayerX() - displayLeft; col < map.getPlayerX() + displayRight; col++) {
				rootCol++;
				root.add(map.getTile(row, col), rootCol, rootRow);
			}
		}

		// Displays player sprite in correct location
		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), playerSprite);
		root.add(playerStack, playerStackX, playerStackY);
	}

	public static void main(String[] args) {
		launch(args);

	}

}