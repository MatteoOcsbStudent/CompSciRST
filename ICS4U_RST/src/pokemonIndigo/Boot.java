package pokemonIndigo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import simpleIO.Console;

public class Boot extends Application {

	// Declares window dimensions
	private final int CAMERAHEIGHT = 6;
	private final int CAMERAWIDTH = 10;

	// size of pokemon sprites
	private final int pokeSpriteDimension = 150;

	// which battle button is being selected
	private int battleButtonIndex = 0;

	// font sizes
	static final int LARGE_FONT = 25;
	static final int MEDIUM_FONT = 18;
	static final int SMALL_FONT = 12;

	// Padding & Insets
	static final int GAP = 1;

	// Player sprite location on gridpane
	private int playerStackX;
	private int playerStackY;

	Label lblLoadingScreen;
	static final int LOADINGFONT = 30;

	// Locking movement
	private boolean movementLock = false;

	// Direction of movement
	private String direction;

	// Records what menu is being used
	private String menu = "default";

	// The map name used for transitions
	private String currentMapName;

	StackPane playerStack = new StackPane();

	Scene loading, scene, battleScene;

	ImageView opponentPokeSprite, playerPokeSprite;
	BackgroundImage backgroundImage;

	// Directional sprites
	ImageView playerUp = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerUp.png").toString());
	ImageView playerLeft = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerLeft.png").toString());
	ImageView playerRight = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerRight.png").toString());
	ImageView playerDown = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerDown.png").toString());

	// Battle labels/buttons
	Label lblfightButton;
	Label lblPokemonButton;
	Label lblCatchButton;
	Label lblRunButton;

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

		Pokemon playerPoke = new Pokemon("Yanma", 52);
		Pokemon opponent = new Pokemon("Yanma", 52);

		// Declaring gridpane
		root = new GridPane();
		StackPane loadingPane = new StackPane();

		lblLoadingScreen = new Label();
		lblLoadingScreen.setTextFill(Color.WHITE);
		lblLoadingScreen.setFont(Font.font(LOADINGFONT));
		loadingPane.getChildren().addAll(new Rectangle(640, 384, Color.BLACK), lblLoadingScreen);

		// call board display
		displayBoard(root);

		// Stack player sprite onto tile
		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), playerUp);
		root.add(playerStack, playerStackX, playerStackY);

		scene = new Scene(root);
		loading = new Scene(loadingPane, 640, 384);

		/**
		 * Battle Scene
		 */

		GridPane battleRoot = new GridPane();
		battleScene = new Scene(battleRoot, 640, 384);
		battleRoot.setGridLinesVisible(false);

		battleRoot.setHgap(GAP);
		battleRoot.setVgap(GAP);
		battleRoot.setPadding(new Insets(GAP, GAP, GAP, GAP));

		playerPokeSprite = new ImageView(playerPoke.getBackSprite());
		playerPokeSprite.setFitHeight(pokeSpriteDimension);
		playerPokeSprite.setFitWidth(pokeSpriteDimension);
		battleRoot.add(playerPokeSprite, 4, 10, 10, 1);

		opponentPokeSprite = new ImageView(opponent.getFrontSprite());
		opponentPokeSprite.setFitHeight(pokeSpriteDimension);
		opponentPokeSprite.setFitWidth(pokeSpriteDimension);
		battleRoot.add(opponentPokeSprite, 45, 5, 10, 1);

		backgroundImage = new BackgroundImage(map.getBackgroundImage(), BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

		battleRoot.setBackground(new Background(backgroundImage));

		lblfightButton = new Label("     Fight     ");
		lblfightButton.setFont(Font.font(LARGE_FONT));
		lblfightButton.setTextFill(Color.BLACK);
		battleRoot.add(lblfightButton, 0, 20, 15, 1);
		GridPane.setHalignment(lblfightButton, HPos.CENTER);

		lblPokemonButton = new Label("     Pokemon     ");
		lblPokemonButton.setFont(Font.font(LARGE_FONT));
		lblPokemonButton.setTextFill(Color.BLACK);
		battleRoot.add(lblPokemonButton, 15, 20, 15, 1);
		GridPane.setHalignment(lblPokemonButton, HPos.CENTER);

		lblCatchButton = new Label("     Catch     ");
		lblCatchButton.setFont(Font.font(LARGE_FONT));
		lblCatchButton.setTextFill(Color.BLACK);
		battleRoot.add(lblCatchButton, 30, 20, 15, 1);
		GridPane.setHalignment(lblCatchButton, HPos.CENTER);

		lblRunButton = new Label("     Run     ");
		lblRunButton.setFont(Font.font(LARGE_FONT));
		lblRunButton.setTextFill(Color.BLACK);
		battleRoot.add(lblRunButton, 45, 20, 15, 1);
		GridPane.setHalignment(lblRunButton, HPos.CENTER);

		// Moving player WASD
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				// Checks if the movement is locked before allowing you to move
				if (movementLock == false) {

					switch (event.getCode()) {
					case W:

						if (menu.equals("Battle")) {
							//W doesn't do anything in battle menu
						} else {

							// Directional sprite
							playerSprite = playerUp;

							// Defines direction
							direction = "Up";
							if (map.getPlayerY() == 0) {
								nextMap(myStage);
							} else
							// Makes sure you're not flying over trees and houses
							if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkBarrier() != true) {

								if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkEncounter() == true) {
									wildEncounter(myStage);
								}
								// Moves player's tilegrid location and stackpane location
								map.setPlayerY(-1);
								playerStackY--;

								// Displays new board
								displayBoard(root);
							}
						}

						break;

					case A:

						// Moves through highlighted buttons in a battle instead of movement
						if (menu.equals("Battle")) {
							if (battleButtonIndex == 1 || battleButtonIndex == 0) {
								battleButtonIndex = 4;
								buttonUpdate();
							} else {
								battleButtonIndex--;
								buttonUpdate();
							}
						} else {
							playerSprite = playerLeft;
							direction = "Left";
							if (map.getPlayerX() == 0) {
								nextMap(myStage);
							} else if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).checkBarrier() != true) {
								if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkEncounter() == true) {
									wildEncounter(myStage);
								}
								map.setPlayerX(-1);
								playerStackX--;
								displayBoard(root);
							}
						}
						break;

					case S:

						if (menu.equals("Battle")) {
							Console.print("Test");
						} else {
							playerSprite = playerDown;
							direction = "Down";
							if (map.getPlayerY() == map.getMapHeight() - 1) {
								nextMap(myStage);
							} else if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).checkBarrier() != true) {
								if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkEncounter() == true) {
									wildEncounter(myStage);
								}
								map.setPlayerY(1);
								playerStackY++;
								displayBoard(root);
							}
						}
						break;

					case D:

						if (menu.equals("Battle")) {
							if (battleButtonIndex == 4 || battleButtonIndex == 0) {
								battleButtonIndex = 1;
								buttonUpdate();
							} else {
								battleButtonIndex++;
								buttonUpdate();
							}
						} else {
							playerSprite = playerRight;
							direction = "Right";
							if (map.getPlayerX() == map.getMapWidth() - 1) {
								nextMap(myStage);
							} else if (map.getTile(map.getPlayerY(), map.getPlayerX() + 1).checkBarrier() != true) {
								if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkEncounter() == true) {
									wildEncounter(myStage);
								}
								map.setPlayerX(1);
								playerStackX++;
								displayBoard(root);
							}
						}
						break;

					default:
						break;

					}
				}
			}
		});

		myStage.setTitle("Pokemon Indigo");
		myStage.setScene(scene);
		myStage.show();

	}

	public void nextMap(Stage myStage) {

		// locks movement
		movementLock = true;

		// Checks to see if the player is on an exit tile
		map.checkExit(currentMapName, map.getPlayerX(), map.getPlayerY());

		// Changes the currentMapName to the next map's
		currentMapName = map.getNextMap();

		// Changes the loading screen text to the new map's name
		lblLoadingScreen.setText("Now Entering: " + currentMapName + "...");

		// Sets the loading screen
		myStage.setScene(loading);

		// Puts the player in their new spawnpoint
		playerStackX = map.getPlayerSpawnX();
		playerStackY = map.getPlayerSpawnY();

		// Instantiates the new map
		map = new TileGrid(map.getNextMap(), map.getNextSpawn());

		// Displays the next map
		displayBoard(root);

		// Sets the scene to the new map
		myStage.setScene(scene);

		// removes the movement lock
		movementLock = false;
	}

	public void buttonUpdate() {

		switch (menu) {
		case "Battle":

			if (battleButtonIndex == 1) {
				lblfightButton.setTextFill(Color.BLUE);	
			}

			if (battleButtonIndex == 2) {
				lblPokemonButton.setTextFill(Color.BLUE);
			}

			if (battleButtonIndex == 3) {
				lblCatchButton.setTextFill(Color.BLUE);
			}

			if (battleButtonIndex == 4) {
				lblRunButton.setTextFill(Color.BLUE);
			}

		}
	}

	public void wildEncounter(Stage myStage) {

		// 20% chance of wild encounter happening
		if (Math.random() * 100 < 20) {

			// Sets pokemon
			Pokemon playerPoke = new Pokemon("Torchic", 20);
			Pokemon opponent = new Pokemon("Totodile", (playerPoke.getLevel() - 2));

			// Sets the sprites for the pokemon
			playerPokeSprite.setImage(playerPoke.getBackSprite());
			opponentPokeSprite.setImage(opponent.getFrontSprite());

			// Battle Logic goes Here

			// Sets the Battle Scene
			menu = "Battle";
			myStage.setScene(battleScene);
		}
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