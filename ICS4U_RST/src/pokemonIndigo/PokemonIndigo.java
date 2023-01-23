package pokemonIndigo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import simpleIO.Console;
import simpleIO.Dialog;
import sortAlgorithim.Sort;

public class PokemonIndigo extends Application {

	// Declares window dimensions
	private final int CAMERAHEIGHT = 6;
	private final int CAMERAWIDTH = 10;

	// size of pokemon sprites
	private final int POKESPRITEDIMENSION = 148;

	// Scene height and width
	private final int sceneHeight = 384;
	private final int sceneWidth = 640;

	// which battle button is being selected
	private int battleButtonIndex, pokeMenuIndex, pokeSwapIndex = 0;

	// Which starter button is being selected
	private int starterChoiceIndex = 0;

	// Battle response to be displayed
	private int responseCounter = 0;

	// font sizes
	static final int TITLE_FONT = 50;
	static final int LARGE_FONT = 25;
	static final int MEDIUM_FONT = 20;
	static final int SMALL_FONT = 12;

	// Padding & Insets
	static final int GAP = 5;

	// Player sprite location on gridpane
	private int playerStackX;
	private int playerStackY;

	Label lblLoadingScreen;
	static final int LOADINGFONT = 30;

	// Locking movement
	private boolean movementLock = false;

	private boolean endBattle;

	// Direction of movement
	private String direction;

	// Tracks which battle menu is used
	private String battleMenu;

	// Boolean that tracks if the save was loaded
	private boolean loadedState;

	// The map name used for transitions
	private String currentMapName;

	private String pokeMenuOrigin;

	// Global reference for pokemon
	Pokemon playerPokemon;
	Pokemon opponentPokemon;

	// Player sprite stackpane
	StackPane playerStack = new StackPane();

	// Scenes
	Scene loading, scene, battleScene, pokeMenuScene;

	// Battle images
	ImageView opponentPokeSprite, playerPokeSprite;
	BackgroundImage backgroundImage;

	// Directional sprites
	ImageView playerUp = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerUp.png").toString());
	ImageView playerLeft = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerLeft.png").toString());
	ImageView playerRight = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerRight.png").toString());
	ImageView playerDown = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerDown.png").toString());


	// Menu Scenes' Datafields

	ImageView imgStarterChoice1, imgStarterChoice2, imgStarterChoice3;

	Label lblStarterText, lblStarterInstructions, lblContinueInstructions;

	Scene starterScene;

	String choice;

	Button btnReturn;

	// Main Menu Scene
	Scene mainMenuScene;

	// How To Play Scene
	Scene howToPlayScene;

	ImageView imgPokeBall1, imgPokeBall2, imgPokeBall3, imgPokeBall4, imgPokeBall5, imgPokeBall6, imgPokeMenuSprite;

	final Image normalPokeBall = new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString());
	final Image faintedPokeBall = new Image(getClass().getResource("/images/Pokeballs/PokeballFainted.png").toString());
	final Image selectedNormalPokeBall = new Image(
			getClass().getResource("/images/Pokeballs/PokeballNormalSelected.png").toString());
	final Image selectedFaintedPokeBall = new Image(
			getClass().getResource("/images/Pokeballs/PokeballFaintedSelected.png").toString());

	// Battle labels/buttons
	Label lblFightButton, lblPokemonButton, lblCatchButton, lblRunButton, lblBattleResponse, lblPlayerHp, lblPlayerBar,
			lblOpponentHp, lblOpponentBar, lblCToContinue, lblXToBack;

	Label lblHowToPlay;

	int instructionsCount = 0;

	// PokeMenu labels
	Label lblPokeMenuName, lblPokeMenuHp, lblPokeMenuXp, lblPokeMenuMove1, lblPokeMenuMove2, lblPokeMenuMove3,
			lblPokeMenuMove4, lblPokeMenuC, lblPokeMenuX, lblPokeMenuStats, lblPokeMenuTypes;

	// Default player sprite is upwards facing
	ImageView playerSprite = playerUp;

	// main gridpane
	GridPane root;

	// Tilegrid reference
	TileGrid map;

	// player instantiation
	Player player = new Player();

	// Battle reference
	Battle battle;

	// healthBars

	ProgressBar playerHpBar;
	ProgressBar opponentHpBar;

	// xpBar

	ProgressBar xpBar;

	public void save(Stage myStage) {

		FileWriter saveFile;
		try {

			// Saved File
			saveFile = new FileWriter("data/saveFile");
			PrintWriter savePrinter = new PrintWriter(saveFile);

			// Saves the map's name
			savePrinter.println(currentMapName);

			// Saves the Spawnpoint
			savePrinter.println(map.getNextSpawn());

			// Saves player's direction
			savePrinter.println(direction);

			// Saves the X and Y of the player
			savePrinter.println(map.getPlayerY());
			savePrinter.println(map.getPlayerX());

			// Saves the Playerstack's X and Y
			savePrinter.println(playerStackX);
			savePrinter.println(playerStackY);

			// Saves the size of the player's team
			savePrinter.println(player.getTeamSize());

			// Saving Pokemon
			for (int i = 0; i < player.getTeamSize(); i++) {

				// Pokemon's Name
				savePrinter.println(player.getPokemon(i).getFirstEvo());

				// Pokemon's Level
				savePrinter.println(player.getPokemon(i).getLevel());

				// Pokemon's Current HP
				savePrinter.println(player.getPokemon(i).getCurrentHP());

				// Pokemon's Status (If afflicted)
				savePrinter.println(player.getPokemon(i).getStatus());

				// Size of Pokemon's move pool (1 to 4)
				savePrinter.println(player.getPokemon(i).getMovePoolSize());

				// Loops through all the moves, saving them
				for (int j = 0; j < player.getPokemon(i).getMovePoolSize(); j++) {

					savePrinter.println(player.getPokemon(i).getMove(j).getName());
				}
			}

			saveFile.close();

		} catch (IOException e) {
			e.printStackTrace();
			Console.print("Error with Loading, creating new game");
			myStage.setScene(starterScene);
		}

	}

	public void load(Stage myStage) {

		try {

			FileReader loadFile = new FileReader("data/saveFile");
			BufferedReader loadStream = new BufferedReader(loadFile);

			loadedState = true;

			// Reads map name
			currentMapName = loadStream.readLine();

			// Creates a new map based on the saved one
			map = new TileGrid(currentMapName, Integer.parseInt(loadStream.readLine()));

			direction = loadStream.readLine();

			// Sets the player's X and Y
			map.setPlayerY(Integer.parseInt(loadStream.readLine()));
			map.setPlayerX(Integer.parseInt(loadStream.readLine()));

			// Sets the playerStack's X and Y
			playerStackX = (Integer.parseInt(loadStream.readLine()));
			playerStackY = (Integer.parseInt(loadStream.readLine()));

			switch (direction) {

			case "Up":
				playerSprite = playerUp;
				break;

			case "Down":
				playerSprite = playerDown;
				break;

			case "Left":
				playerSprite = playerLeft;
				break;

			case "Right":
				playerSprite = playerRight;
				break;
			}

			// Checks the player's team size
			int teamSize = Integer.parseInt(loadStream.readLine());

			// Loops through every pokemon based on the team's size
			for (int i = 0; i < teamSize; i++) {

				// Adds a new pokemon using the name saved
				player.addPokemon(new Pokemon(loadStream.readLine(), Integer.parseInt(loadStream.readLine())));

				// Sets the pokemon's current HP
				player.getPokemon(i).setCurrentHP(Integer.parseInt(loadStream.readLine()));

				// Sets any status it is afflicted with
				player.getPokemon(i).setStatus(loadStream.readLine());

				// Sets the movepool's size
				int movePoolSize = Integer.parseInt(loadStream.readLine());

				for (int j = 0; j < movePoolSize; j++) {

					// Adds all the moves
					player.getPokemon(i).changeMoveSet((loadStream.readLine()), j);
				}
			}

			displayBoard(root);

			loadFile.close();

			myStage.setScene(scene);

		} catch (FileNotFoundException e) {
			Console.print("Save file not Found" + e.getMessage());
		} catch (NumberFormatException e) {
			Console.print("Number Format Exception" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage myStage) throws Exception {

		// Hardcoded start point for New Game
		map = new TileGrid("Orilon Town", 1);
		currentMapName = "Orilon Town";

		direction = "Up";

		// Declaring gridpane
		root = new GridPane();

		// Stack player sprite onto tile
		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), playerUp);
		root.add(playerStack, playerStackX, playerStackY);
		scene = new Scene(root);

		// call board display
		displayBoard(root);

		StackPane loadingPane = new StackPane();

		// Loading screen text
		lblLoadingScreen = new Label();
		lblLoadingScreen.setTextFill(Color.WHITE);
		lblLoadingScreen.setFont(Font.font(LOADINGFONT));
		loadingPane.getChildren().addAll(new Rectangle(sceneWidth, sceneHeight, Color.BLACK), lblLoadingScreen);

		// Loading screen
		loading = new Scene(loadingPane, sceneWidth, sceneHeight);

		/**
		 * Battle Scene
		 */

		// Battle UI gridpane and scene
		GridPane battleRoot = new GridPane();
		battleScene = new Scene(battleRoot, sceneWidth, sceneHeight);

		battleRoot.setHgap(GAP);
		battleRoot.setVgap(GAP);
		battleRoot.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Player's pokemon image
		playerPokeSprite = new ImageView();
		playerPokeSprite.setFitHeight(POKESPRITEDIMENSION);
		playerPokeSprite.setFitWidth(POKESPRITEDIMENSION);
		battleRoot.add(playerPokeSprite, 0, 6, 2, 6);

		// playerHpBar
		playerHpBar = new ProgressBar();
		playerHpBar.setPrefWidth(POKESPRITEDIMENSION);
		playerHpBar.setPrefHeight(POKESPRITEDIMENSION / 8);
		battleRoot.add(playerHpBar, 4, 8, 2, 1);

		// label for player pokemons name and level
		lblPlayerBar = new Label();
		lblPlayerBar.setTextFill(Color.BLACK);
		lblPlayerBar.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblPlayerBar, 4, 7, 1, 1);
		lblPlayerBar.setAlignment(Pos.BOTTOM_CENTER);

		// label for player pokemons hp
		lblPlayerHp = new Label();
		lblPlayerHp.setTextFill(Color.BLACK);
		lblPlayerHp.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblPlayerHp, 4, 9, 1, 1);
		lblPlayerHp.setAlignment(Pos.TOP_CENTER);

		// Opponent pokemon image
		opponentPokeSprite = new ImageView();
		opponentPokeSprite.setFitHeight(POKESPRITEDIMENSION);
		opponentPokeSprite.setFitWidth(POKESPRITEDIMENSION);
		battleRoot.add(opponentPokeSprite, 6, 0, 2, 6);

		// opponentHpBar
		opponentHpBar = new ProgressBar();
		opponentHpBar.setPrefWidth(POKESPRITEDIMENSION);
		opponentHpBar.setPrefHeight(POKESPRITEDIMENSION / 8);
		battleRoot.add(opponentHpBar, 2, 3, 2, 1);

		// Label for opponent pokemons name and level
		lblOpponentBar = new Label();
		lblOpponentBar.setTextFill(Color.BLACK);
		lblOpponentBar.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblOpponentBar, 2, 2, 1, 1);
		lblOpponentBar.setAlignment(Pos.BOTTOM_CENTER);

		// Label for opponent pokemons hp
		lblOpponentHp = new Label();
		lblOpponentHp.setTextFill(Color.BLACK);
		lblOpponentHp.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblOpponentHp, 2, 4, 1, 1);
		lblOpponentHp.setAlignment(Pos.TOP_CENTER);

		// Background image for battle
		backgroundImage = new BackgroundImage(map.getBackgroundImage(), BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

		battleRoot.setBackground(new Background(backgroundImage));

		// Fight button
		lblFightButton = new Label("Fight");
		lblFightButton.setFont(Font.font(MEDIUM_FONT));
		lblFightButton.setTextFill(Color.BLACK);
		battleRoot.add(lblFightButton, 0, 13, 1, 1);
		lblFightButton.setVisible(false);
		lblFightButton.setPrefWidth(148);
		lblFightButton.setAlignment(Pos.CENTER);

		// Pokemon button
		lblPokemonButton = new Label("Pokemon");
		lblPokemonButton.setFont(Font.font(MEDIUM_FONT));
		lblPokemonButton.setTextFill(Color.BLACK);
		battleRoot.add(lblPokemonButton, 2, 13, 1, 1);
		lblPokemonButton.setVisible(false);
		lblPokemonButton.setPrefWidth(148);
		lblPokemonButton.setAlignment(Pos.CENTER);

		// Catch button
		lblCatchButton = new Label("Catch");
		lblCatchButton.setFont(Font.font(MEDIUM_FONT));
		lblCatchButton.setTextFill(Color.BLACK);
		battleRoot.add(lblCatchButton, 4, 13, 1, 1);
		lblCatchButton.setVisible(false);
		lblCatchButton.setPrefWidth(148);
		lblCatchButton.setAlignment(Pos.CENTER);

		// Run button
		lblRunButton = new Label("Run");
		lblRunButton.setFont(Font.font(MEDIUM_FONT));
		lblRunButton.setTextFill(Color.BLACK);
		battleRoot.add(lblRunButton, 6, 13, 1, 1);
		lblRunButton.setVisible(false);
		lblRunButton.setPrefWidth(148);
		lblRunButton.setAlignment(Pos.CENTER);

		// in battle response label
		lblBattleResponse = new Label();
		lblBattleResponse.setFont(Font.font(MEDIUM_FONT));
		lblBattleResponse.setTextFill(Color.BLACK);
		battleRoot.add(lblBattleResponse, 0, 13, 6, 1);
		lblBattleResponse.setPrefWidth(888);

		// C to continue instruction
		lblCToContinue = new Label("'C' to continue ->");
		lblCToContinue.setFont(Font.font(SMALL_FONT));
		lblCToContinue.setTextFill(Color.BLACK);
		battleRoot.add(lblCToContinue, 6, 14, 1, 1);
		lblCToContinue.setAlignment(Pos.CENTER_RIGHT);

		// X to go back instruction
		lblXToBack = new Label("<- 'X' to go back");
		lblXToBack.setFont(Font.font(SMALL_FONT));
		lblXToBack.setTextFill(Color.BLACK);
		battleRoot.add(lblXToBack, 0, 14, 1, 1);
		lblXToBack.setAlignment(Pos.CENTER_LEFT);
		lblXToBack.setVisible(false);

		/**
		 * Starter Scene
		 */

		GridPane starterChoice = new GridPane();
		starterScene = new Scene(starterChoice, sceneWidth, sceneHeight);
		starterChoice.setGridLinesVisible(false);

		starterChoice.setHgap(GAP);
		starterChoice.setVgap(GAP);
		starterChoice.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Starter Choice Background
		Image starterBackground = new Image("images/BattleBackgrounds/PokemonSwitchBackground.png");

		BackgroundImage starterBackgroundImage = new BackgroundImage(starterBackground, BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

		starterChoice.setBackground(new Background(starterBackgroundImage));

		imgStarterChoice1 = new ImageView(
				new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
		imgStarterChoice1.setFitHeight(POKESPRITEDIMENSION);
		imgStarterChoice1.setFitWidth(POKESPRITEDIMENSION);
		starterChoice.add(imgStarterChoice1, 5, 10, 2, 6);

		imgStarterChoice2 = new ImageView(
				new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
		imgStarterChoice2.setFitHeight(POKESPRITEDIMENSION);
		imgStarterChoice2.setFitWidth(POKESPRITEDIMENSION);
		starterChoice.add(imgStarterChoice2, 37, 20, 1, 6);

		imgStarterChoice3 = new ImageView(
				new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
		imgStarterChoice3.setFitHeight(POKESPRITEDIMENSION);
		imgStarterChoice3.setFitWidth(POKESPRITEDIMENSION);
		starterChoice.add(imgStarterChoice3, 40, 10, 1, 6);

		// Starter choice label
		lblStarterText = new Label("Choose your Starter Pokemon!");
		lblStarterText.setFont(Font.font(MEDIUM_FONT));
		lblStarterText.setTextFill(Color.BLACK);
		lblStarterText.setAlignment(Pos.CENTER_LEFT);
		starterChoice.add(lblStarterText, 0, 43, 40, 7);
		lblStarterText.setPrefWidth(888);

		// C to continue instruction
		lblStarterInstructions = new Label("'C' to confirm your selection ->");
		lblStarterInstructions.setFont(Font.font(SMALL_FONT));
		lblStarterInstructions.setTextFill(Color.BLACK);
		starterChoice.add(lblStarterInstructions, 40, 49, 5, 1);
		lblStarterInstructions.setAlignment(Pos.CENTER_RIGHT);

		/**
		 * How to Play Scene
		 * 
		 */

		GridPane howToPlay = new GridPane();
		howToPlayScene = new Scene(howToPlay, sceneWidth, sceneHeight);
		howToPlay.setGridLinesVisible(false);

		howToPlay.setHgap(GAP);
		howToPlay.setVgap(GAP);
		howToPlay.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Title of How To Play Scene
		lblHowToPlay = new Label("How to Play");
		lblHowToPlay.setFont(Font.font(MEDIUM_FONT));
		lblStarterText.setAlignment(Pos.CENTER_LEFT);
		howToPlay.add(lblHowToPlay, 0, 0, 130, 10);
		lblHowToPlay.setPrefWidth(888);

		// Second Instruction Image
		ImageView imgInstructions1 = new ImageView("/images/MainMenu/WASDInstructions.png");
		imgInstructions1.setFitHeight(POKESPRITEDIMENSION);
		imgInstructions1.setFitWidth(POKESPRITEDIMENSION);
		howToPlay.add(imgInstructions1, 0, 10);

		// Second Instruction Image
		ImageView imgInstructions2 = new ImageView("/images/MainMenu/BattleInstruction.png");
		imgInstructions2.setFitHeight(POKESPRITEDIMENSION);
		imgInstructions2.setFitWidth(POKESPRITEDIMENSION);
		howToPlay.add(imgInstructions2, 25, 10);

		// First Instruction label
		Label lblInstructions1 = new Label("WASD Keys to move");
		lblInstructions1.setFont(Font.font(MEDIUM_FONT));
		lblInstructions1.setAlignment(Pos.CENTER_LEFT);
		howToPlay.add(lblInstructions1, 0, 15, 100, 10);
		lblInstructions1.setPrefWidth(888);

		// Second Instruction label
		Label lblInstructions2 = new Label("AD Keys to use the battle menu");
		lblInstructions2.setFont(Font.font(MEDIUM_FONT));
		lblInstructions2.setAlignment(Pos.CENTER_LEFT);
		howToPlay.add(lblInstructions2, 25, 15, 100, 10);
		lblInstructions2.setPrefWidth(888);

		// Prompt to continue the how to play scene
		lblContinueInstructions = new Label("'C' to continue ->");
		lblContinueInstructions.setFont(Font.font(SMALL_FONT));
		lblContinueInstructions.setAlignment(Pos.CENTER_LEFT);
		howToPlay.add(lblContinueInstructions, 25, 25);
		lblContinueInstructions.setPrefWidth(888);

		// Returning to main menu
		btnReturn = new Button("Return");
		btnReturn.setMinWidth(100);
		howToPlay.add(btnReturn, 0, 25);

		/**
		 * Main Menu Scene
		 */

		GridPane mainMenu = new GridPane();
		mainMenuScene = new Scene(mainMenu, sceneWidth, sceneHeight);
		mainMenu.setGridLinesVisible(false);

		mainMenu.setHgap(GAP);
		mainMenu.setVgap(GAP);
		mainMenu.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Title
		ImageView title = new ImageView("images/MainMenu/PokemonTitle.png");
		title.setFitWidth(500);
		title.setFitHeight(40);
		mainMenu.add(title, 13, 15, 100, 10);

		// Main Menu Background
		Image menuBackground = new Image("images/MainMenu/MenuBackground.png");

		BackgroundImage mainMenuBackgroundImage = new BackgroundImage(menuBackground, BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

		mainMenu.setBackground(new Background(mainMenuBackgroundImage));

		// New Game Button
		Button btnNewGame = new Button("New Game");
		btnNewGame.setMinWidth(100);
		btnNewGame.setOnAction(event -> myStage.setScene(starterScene));
		mainMenu.add(btnNewGame, 54, 35);

		// Load Button
		Button btnLoad = new Button("Load");
		btnLoad.setMinWidth(100);
		btnLoad.setOnAction(event -> load(myStage));
		mainMenu.add(btnLoad, 54, 40);

		// How to Play Button
		Button btnHowToPlay = new Button("How to Play");
		btnHowToPlay.setMinWidth(100);
		btnHowToPlay.setOnAction(event -> updateHowToPlay("Main Menu", myStage));
		mainMenu.add(btnHowToPlay, 54, 45);

		// Exit Button
		Button btnExit = new Button("Exit");
		btnExit.setMinWidth(100);
		btnExit.setOnAction(event -> System.exit(0));
		mainMenu.add(btnExit, 54, 50);

		// Key Logic for the How to Play Scene
		howToPlayScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				switch (event.getCode()) {

				case C:

					switch (instructionsCount) {

					// First Page of instructions
					case 0:
						imgInstructions1.setImage(
								new Image(getClass().getResource("/images/MainMenu/InteractButton.png").toString()));
						lblInstructions1.setText("Interact with the world\nby using 'C'");

						imgInstructions2.setImage(
								new Image(getClass().getResource("/images/MainMenu/PkmnCenterHeal.png").toString()));
						lblInstructions2.setText("Get healed at the Pokemon Center");

						lblContinueInstructions.setVisible(true);
						break;

					// Second Page of instructions
					case 1:
						imgInstructions1.setImage(
								new Image(getClass().getResource("/images/MainMenu/MoveInstructions.png").toString()));
						lblInstructions1.setText("Use powerful moves to\n defeat other pokemon");

						imgInstructions2.setImage(
								new Image(getClass().getResource("/images/MainMenu/CatchInstructions.png").toString()));
						lblInstructions2.setText("Catch pokemon along the way\nto add to your team");

						lblContinueInstructions.setVisible(true);
						break;

					// Third Page of instructions
					case 2:
						imgInstructions1.setImage(
								new Image(getClass().getResource("/images/MainMenu/SaveGame.png").toString()));
						lblInstructions1.setText("Press 'V' to\nsave the game, and 'Z'\n to revisit this screen");

						imgInstructions2.setImage(
								new Image(getClass().getResource("/images/MainMenu/SwapPokemon.png").toString()));
						lblInstructions2.setText(
								"Swap the pokemon in your team\nto enable amazing strategies\neither during fights or with 'P' in the \noverworld!");

						lblContinueInstructions.setVisible(false);
						break;
					}

					instructionsCount++;
					break;
				default:
					break;

				}
			}

		});

		/**
		 * Pokemon Menu
		 */

		// Gridpane for main pokemon menu
		GridPane pokeMenu = new GridPane();
		pokeMenuScene = new Scene(pokeMenu, sceneWidth, sceneHeight);

		// Plain white background
		pokeMenu.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

		pokeMenu.setHgap(GAP);
		pokeMenu.setVgap(GAP);
		pokeMenu.setPadding(new Insets(GAP, GAP, GAP, GAP));

		// Pokeball images
		imgPokeBall1 = new ImageView(normalPokeBall);
		pokeMenu.add(imgPokeBall1, 0, 0, 1, 1);

		imgPokeBall2 = new ImageView(normalPokeBall);
		pokeMenu.add(imgPokeBall2, 0, 1, 1, 1);

		imgPokeBall3 = new ImageView(normalPokeBall);
		pokeMenu.add(imgPokeBall3, 0, 2, 1, 1);

		imgPokeBall4 = new ImageView(normalPokeBall);
		pokeMenu.add(imgPokeBall4, 0, 3, 1, 1);

		imgPokeBall5 = new ImageView(normalPokeBall);
		pokeMenu.add(imgPokeBall5, 0, 4, 1, 1);

		imgPokeBall6 = new ImageView(normalPokeBall);
		pokeMenu.add(imgPokeBall6, 0, 5, 1, 1);

		// Sprite image
		imgPokeMenuSprite = new ImageView();
		imgPokeMenuSprite.setFitHeight(POKESPRITEDIMENSION / 1.5);
		imgPokeMenuSprite.setFitWidth(POKESPRITEDIMENSION / 1.5);
		pokeMenu.add(imgPokeMenuSprite, 4, 0, 2, 4);

		// Labels
		lblPokeMenuName = new Label();
		lblPokeMenuName.setTextFill(Color.BLACK);
		lblPokeMenuName.setFont(Font.font(MEDIUM_FONT));
		lblPokeMenuName.setAlignment(Pos.CENTER);
		pokeMenu.add(lblPokeMenuName, 1, 0, 12, 1);

		lblPokeMenuTypes = new Label();
		lblPokeMenuTypes.setTextFill(Color.BLACK);
		lblPokeMenuTypes.setFont(Font.font(SMALL_FONT));
		lblPokeMenuTypes.setAlignment(Pos.CENTER);
		lblPokeMenuTypes.setMinWidth(150);
		pokeMenu.add(lblPokeMenuTypes, 10, 1, 2, 2);

		lblPokeMenuStats = new Label();
		lblPokeMenuStats.setTextFill(Color.BLACK);
		lblPokeMenuStats.setFont(Font.font(SMALL_FONT));
		lblPokeMenuStats.setAlignment(Pos.CENTER);
		lblPokeMenuStats.setMinWidth(150);
		pokeMenu.add(lblPokeMenuStats, 7, 1, 2, 2);

		lblPokeMenuHp = new Label();
		lblPokeMenuHp.setTextFill(Color.BLACK);
		lblPokeMenuHp.setFont(Font.font(SMALL_FONT));
		lblPokeMenuHp.setAlignment(Pos.CENTER);
		pokeMenu.add(lblPokeMenuHp, 1, 1, 2, 1);

		lblPokeMenuXp = new Label();
		lblPokeMenuXp.setTextFill(Color.BLACK);
		lblPokeMenuXp.setFont(Font.font(SMALL_FONT));
		lblPokeMenuXp.setAlignment(Pos.CENTER);
		pokeMenu.add(lblPokeMenuXp, 1, 2, 2, 1);

		lblPokeMenuMove1 = new Label();
		lblPokeMenuMove1.setTextFill(Color.BLACK);
		lblPokeMenuMove1.setFont(Font.font(SMALL_FONT));
		lblPokeMenuMove1.setAlignment(Pos.TOP_CENTER);
		lblPokeMenuMove1.setMinWidth(150);
		lblPokeMenuMove1.setMinHeight(192);
		pokeMenu.add(lblPokeMenuMove1, 1, 5, 2, 10);

		lblPokeMenuMove2 = new Label();
		lblPokeMenuMove2.setTextFill(Color.BLACK);
		lblPokeMenuMove2.setFont(Font.font(SMALL_FONT));
		lblPokeMenuMove2.setAlignment(Pos.TOP_CENTER);
		lblPokeMenuMove2.setMinWidth(150);
		lblPokeMenuMove2.setMinHeight(192);
		pokeMenu.add(lblPokeMenuMove2, 4, 5, 2, 10);

		lblPokeMenuMove3 = new Label();
		lblPokeMenuMove3.setTextFill(Color.BLACK);
		lblPokeMenuMove3.setFont(Font.font(SMALL_FONT));
		lblPokeMenuMove3.setAlignment(Pos.TOP_CENTER);
		lblPokeMenuMove3.setMinWidth(150);
		lblPokeMenuMove3.setMinHeight(192);
		pokeMenu.add(lblPokeMenuMove3, 7, 5, 2, 10);

		lblPokeMenuMove4 = new Label();
		lblPokeMenuMove4.setTextFill(Color.BLACK);
		lblPokeMenuMove4.setFont(Font.font(SMALL_FONT));
		lblPokeMenuMove4.setAlignment(Pos.TOP_CENTER);
		lblPokeMenuMove4.setMinWidth(150);
		lblPokeMenuMove4.setMinHeight(192);
		pokeMenu.add(lblPokeMenuMove4, 10, 5, 2, 10);

		lblPokeMenuC = new Label();
		lblPokeMenuC.setTextFill(Color.BLACK);
		lblPokeMenuC.setFont(Font.font(SMALL_FONT));
		lblPokeMenuC.setAlignment(Pos.CENTER);
		pokeMenu.add(lblPokeMenuC, 12, 15, 1, 1);

		lblPokeMenuX = new Label("'X' to go back");
		lblPokeMenuX.setTextFill(Color.BLACK);
		lblPokeMenuX.setFont(Font.font(SMALL_FONT));
		lblPokeMenuX.setAlignment(Pos.CENTER);
		pokeMenu.add(lblPokeMenuX, 0, 15, 1, 1);

		// Moving player WASD
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				// Checks if the movement is locked before allowing you to move
				if (movementLock == false) {

					switch (event.getCode()) {
					case W:

						// Directional sprite
						playerSprite = playerUp;

						// Defines direction
						direction = "Up";
						if (map.getPlayerY() == 0
								|| map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkDoor() == true) {
							nextMap(myStage);
						} else
						// Makes sure you're not flying over trees and houses
						if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkBarrier() != true) {

							if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkEncounter() == true) {
								wildEncounter(myStage);
							}

							// Moves player's tilegrid location and stackpane location
							map.addPlayerY(-1);
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
						} else if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).checkBarrier() != true) {

							if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).checkEncounter() == true) {
								wildEncounter(myStage);
							}

							map.addPlayerX(-1);
							playerStackX--;
							displayBoard(root);
						}

						break;

					case S:

						playerSprite = playerDown;
						direction = "Down";
						if (map.getPlayerY() == map.getMapHeight() - 1) {
							nextMap(myStage);
						} else if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).checkBarrier() != true) {

							if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).checkEncounter() == true) {
								wildEncounter(myStage);
							}

							map.addPlayerY(1);
							playerStackY++;
							displayBoard(root);
						}

						break;

					case D:

						playerSprite = playerRight;
						direction = "Right";
						if (map.getPlayerX() == map.getMapWidth() - 1) {
							nextMap(myStage);
						} else if (map.getTile(map.getPlayerY(), map.getPlayerX() + 1).checkBarrier() != true) {

							if (map.getTile(map.getPlayerY(), map.getPlayerX()).checkEncounter() == true) {
								wildEncounter(myStage);
							}

							map.addPlayerX(1);
							playerStackX++;
							displayBoard(root);

						}

						break;

					case C:

						// Checks for interactions via where the player is facing
						switch (direction) {

						case "Right":
							// Checks if the heal NPC (Non-player-Character) is being faced and is only one
							// tile ahead
							if (map.getTile(map.getPlayerY(), map.getPlayerX() + 1).checkHeal() == true) {

								// Fully heals the team, and lets the user know with a prompt
								Dialog.print("Your Team has been healed");

								// Clears Status too
								for (int i = 0; i < player.getTeamSize(); i++) {
									player.getPokemon(i).setCurrentHP(player.getPokemon(i).totalHP);
									player.getPokemon(i).setStatus("Null");
								}
							} else if (map.getTile(map.getPlayerY(), map.getPlayerX() + 1).checkBattle() == true) {
								Console.print("test");
								// TODO The battle needs to start here
							}
							break;

						case "Left":
							if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).checkHeal() == true) {
								Dialog.print("Your Team has been healed");
								for (int i = 0; i < player.getTeamSize(); i++) {
									player.getPokemon(i).setCurrentHP(player.getPokemon(i).totalHP);
									player.getPokemon(i).setStatus("Null");
								}
							} else if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).checkBattle() == true) {
								Console.print("test");
								// TODO The battle needs to start here
							}
							break;

						case "Up":
							if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkHeal() == true) {
								Dialog.print("Your Team has been healed");
								for (int i = 0; i < player.getTeamSize(); i++) {
									player.getPokemon(i).setCurrentHP(player.getPokemon(i).totalHP);
									player.getPokemon(i).setStatus("Null");
								}
							} else if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).checkBattle() == true) {
								Console.print("test");
								// TODO The battle needs to start here
							}

							break;
						case "Down":
							if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).checkHeal() == true) {
								Dialog.print("Your Team has been healed");
								for (int i = 0; i < player.getTeamSize(); i++) {
									player.getPokemon(i).setCurrentHP(player.getPokemon(i).totalHP);
									player.getPokemon(i).setStatus("Null");
								}
							} else if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).checkBattle() == true) {
								Console.print("test");
								// TODO the battle needs to start here
							}
							break;

						}
						break;

					// Saves game
					case V:

						save(myStage);

						Dialog.print("Game Saved");

						break;

					case Z:

						updateHowToPlay("Overworld", myStage);
						break;

					case P:

						pokeMenuOrigin = "swapTeam";
						updatePokeMenu(myStage);
						myStage.setScene(pokeMenuScene);

						break;

					default:
						break;

					}
				}
			}
		});

		starterScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override

			public void handle(KeyEvent event) {
				switch (event.getCode()) {

				case A:

					if (movementLock == false) {

						if (starterChoiceIndex == 1 || starterChoiceIndex == 0) {
							starterChoiceIndex = 3;
							starterButtonUpdate();
						} else {
							starterChoiceIndex--;
							starterButtonUpdate();
						}
					}
					break;

				case D:
					if (movementLock == false) {

						if (starterChoiceIndex == 4 || starterChoiceIndex == 0) {
							starterChoiceIndex = 1;
							starterButtonUpdate();
						} else {
							starterChoiceIndex++;
							starterButtonUpdate();
						}
					}
					break;

				case C:

					player.addPokemon(new Pokemon(choice, 5));
					myStage.setScene(scene);
					Dialog.print(choice + " has been selected as your Starter Pokemon!");
				default:
					break;
				}
			}
		});

		// Controls for battle scene
		battleScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				switch (event.getCode()) {

				case A:

					if (movementLock == false) {
						// Scrolling through buttons right

						if (battleMenu.equals("General")) {

							if (battleButtonIndex == 1 || battleButtonIndex == 0) {
								battleButtonIndex = 4;
								buttonUpdate();
							} else {
								battleButtonIndex--;
								buttonUpdate();
							}

							// Only scrolls through as many buttons as there are moves
						} else if (battleMenu.equals("Moves") || battleMenu.equals("MovesToReplace")) {

							if (battleButtonIndex == 1 || battleButtonIndex == 0) {
								battleButtonIndex = playerPokemon.getMovePoolSize();
								buttonUpdate();
							} else {
								battleButtonIndex--;
								buttonUpdate();
							}

							// Only scrolls through two options, yes and no
						} else if (battleMenu.equals("MoveLearning") || battleMenu.equals("Release")) {
							if (battleButtonIndex == 1 || battleButtonIndex == 2) {
								battleButtonIndex = 3;
								buttonUpdate();
							} else {
								battleButtonIndex = 2;
								buttonUpdate();
							}
						}
					}
					break;

				case D:
					if (movementLock == false) {
						if (battleMenu.equals("General")) {
							if (battleButtonIndex == 4 || battleButtonIndex == 0) {
								battleButtonIndex = 1;
								buttonUpdate();
							} else {
								battleButtonIndex++;
								buttonUpdate();
							}
						} else if (battleMenu.equals("Moves") || battleMenu.equals("MovesToReplace")) {
							if (battleButtonIndex == playerPokemon.getMovePoolSize() || battleButtonIndex == 0) {
								battleButtonIndex = 1;
								buttonUpdate();
							} else {
								battleButtonIndex++;
								buttonUpdate();
							}
						} else if (battleMenu.equals("MoveLearning") || battleMenu.equals("Release")) {
							if (battleButtonIndex == 3 || battleButtonIndex == 4) {
								battleButtonIndex = 2;
								buttonUpdate();
							} else {
								battleButtonIndex = 3;
								buttonUpdate();
							}
						}

					}
					break;

				// 'Select' key. different functions based on menu
				case C:
					switch (battleMenu) {

					// Uses move
					case "Moves":
						useMove(myStage);
						break;

					// Changes next menu based on selected button
					case "General":

						switch (battleButtonIndex) {
						case 1:
							nextBattleMenu(myStage, "Moves");
							break;
						case 2:
							nextBattleMenu(myStage, "PokemonMenu");
							pokeMenuOrigin = "switch";
							break;
						case 3:
							nextBattleMenu(myStage, "Catch");
							break;
						case 4:
							nextBattleMenu(myStage, "Run");
							break;
						default:
							break;

						}

						break;

					// Cycles through battle responses
					case "battleResponses":

						// Start of battle
						if (lblBattleResponse.getText().contains("appeared!")) {
							nextBattleMenu(myStage, "General");
							movementLock = false;

							// go to main scene if ran away
						} else if (lblBattleResponse.getText().equals("You got away safely!")) {
							myStage.setScene(scene);

							// go to main scene if pokemon caught, add caught pokemon to player team
						} else if (lblBattleResponse.getText().contains("caught!")) {

							if (player.getTeamSize() < 6) {
								player.addPokemon(opponentPokemon);
								myStage.setScene(scene);
							} else {
								lblBattleResponse.setText("You have too many pokemon!");

							}
						} else if (lblBattleResponse.getText().equals("You have too many pokemon!")) {

							lblBattleResponse.setText("would you like to release one?");

						} else if (lblBattleResponse.getText().equals("would you like to release one?")) {

							nextBattleMenu(myStage, "Release");

						} else if (lblBattleResponse.getText().equals("Please select a pokemon to release")) {

							pokeMenuOrigin = "Release";
							updatePokeMenu(myStage);
							myStage.setScene(pokeMenuScene);

						} else if (lblBattleResponse.getText().contains("has been released")) {

							if (lblBattleResponse.getText()
									.equals("caught " + opponentPokemon.getName() + " has been released")) {
								myStage.setScene(scene);
							} else {
								player.replaceTeamMember(opponentPokemon, pokeMenuIndex - 1);
								lblBattleResponse.setText(opponentPokemon.getName() + " has been added to your team");
							}

						} else if (lblBattleResponse.getText().contains("has been added to your team")) {
							myStage.setScene(scene);
							// Shows yes and no options if prompted to learn a new move
						} else if (lblBattleResponse.getText().equals("Would you like to forget an old move?")) {

							nextBattleMenu(myStage, "MoveLearning");

							// shows moves to replace
						} else if (lblBattleResponse.getText().equals("Please select a move to replace")) {

							nextBattleMenu(myStage, "MovesToReplace");

							// Searches for available pokemon to send out
						} else if (lblBattleResponse.getText().contains("has fainted")
								&& battle.isPlayerFainted() == true) {

							int nonFaintedPokemon = player.getTeamSize();
							for (int i = 0; i < player.getTeamSize(); i++) {
								if (player.getPokemon(i).getCurrentHP() == 0) {
									nonFaintedPokemon--;
								}
							}

							// If available, prompts user to select a new pokemon
							if (nonFaintedPokemon > 0) {
								lblBattleResponse.setText("Please choose another pokemon to send out");

								// Otherwise, informs user they have no other pokemon
							} else {
								lblBattleResponse.setText("You have no other Pokemon able to fight...");
							}

							// Directs user to pokemon menu
						} else if (lblBattleResponse.getText().equals("Please choose another pokemon to send out")) {

							pokeMenuOrigin = "fainted";
							updatePokeMenu(myStage);
							myStage.setScene(pokeMenuScene);

							// If no other pokemon, tell user they blacked out, end battle
						} else if (lblBattleResponse.getText().equals("You have no other Pokemon able to fight...")) {
							lblBattleResponse.setText("... you blacked out!");
							endBattle = true;

							// If blacked out, sends back to pokemon center and heals all pokemon
						} else if (lblBattleResponse.getText().equals("... you blacked out!")) {
							// black out logic, sends back to pokemon center, heals all pokemon.

							// Initializes map as Pokemon Center (the one in Orilon Town)
							map = new TileGrid("Pokemon Center", 1);

							currentMapName = "Pokemon Center";

							playerStackY = map.getPlayerSpawnY() + 1;
							playerStackX = map.getPlayerSpawnX() - 1;

							// Displays the new board
							displayBoard(root);

							// Resets the scene to the overworld
							myStage.setScene(scene);

							// Fully heals the team, and lets the user know with a prompt
							Dialog.print("You've woken up at the Pokemon Center! \nTeam has been healed");

							for (int i = 0; i < player.getTeamSize(); i++) {
								player.getPokemon(i).setCurrentHP(player.getPokemon(i).totalHP);
								player.getPokemon(i).setStatus("Null");
							}

							// When player sends out new pokemon
						} else if (lblBattleResponse.getText().contains("You sent out")) {

							// resets responses
							responseCounter = 0;
							battle.clearResponses();

							// If from switch, opponent takes turn
							if (pokeMenuOrigin.equals("switch")) {
								battle.turnPlan(playerPokemon.getMove(0));
								battle.turnExecution("opponent only");
								nextBattleResponse();

								// Otherwise, next battle reponse, goes to general
							} else if (pokeMenuOrigin.equals("fainted")) {
								nextBattleMenu(myStage, "General");
							}

							// If no more responses
						} else if (nextBattleResponse() == true) {

							// If battle is to be ended, sets scene to main scene. resets endbattle
							if (endBattle == true) {
								myStage.setScene(scene);
								endBattle = false;

								// Otherwise, goes back to main battle menu
							} else {
								nextBattleMenu(myStage, "General");
							}
						}

						break;

					// Move learning menu
					case "MoveLearning":

						// Yes button, prompts for move to replace
						if (battleButtonIndex == 2) {
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse.setText("Please select a move to replace");

							// No button, informs user pokemon did not learn the move
						} else if (battleButtonIndex == 3) {
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse.setText(
									playerPokemon.getName() + " did not learn " + playerPokemon.getNextMoveLearn());
						}

						break;

					case "Release":

						// Yes button, prompts for pokemon to be released
						if (battleButtonIndex == 2) {
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse.setText("Please select a pokemon to release");

							// No button, informs user pokemon caught has been released
						} else if (battleButtonIndex == 3) {
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse.setText("caught " + opponentPokemon.getName() + " has been released");
						}

						break;

					case "MovesToReplace":

						// Moves to be replaced with a new move
						// Informs user of their selection, changes pokemons moveset respective to
						// selection
						switch (battleButtonIndex) {
						case 1:
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse
									.setText(playerPokemon.getName() + " forgot " + playerPokemon.getMove(0).getName()
											+ "... and learned " + playerPokemon.getNextMoveLearn());
							playerPokemon.changeMoveSet(playerPokemon.getNextMoveLearn(), 0);
							break;
						case 2:
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse
									.setText(playerPokemon.getName() + " forgot " + playerPokemon.getMove(1).getName()
											+ "... and learned " + playerPokemon.getNextMoveLearn());
							playerPokemon.changeMoveSet(playerPokemon.getNextMoveLearn(), 1);
							break;
						case 3:
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse
									.setText(playerPokemon.getName() + " forgot " + playerPokemon.getMove(2).getName()
											+ "... and learned " + playerPokemon.getNextMoveLearn());
							playerPokemon.changeMoveSet(playerPokemon.getNextMoveLearn(), 2);
							break;
						case 4:
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse
									.setText(playerPokemon.getName() + " forgot " + playerPokemon.getMove(3).getName()
											+ "... and learned " + playerPokemon.getNextMoveLearn());
							playerPokemon.changeMoveSet(playerPokemon.getNextMoveLearn(), 3);
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}

					break;

				case X:

					switch (battleMenu) {

					// Goes back to general battle menu
					case "Moves":
						nextBattleMenu(myStage, "General");
						break;

					// Goes back to prompt to forgetting an old move
					case "MovesToReplace":
						lblBattleResponse.setText("Would you like to forget an old move?");
						nextBattleMenu(myStage, "battleResponses");
						break;
					}

					break;

				default:
					break;

				}

			}
		});

		// Controls for pokemon menu

		pokeMenuScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				switch (event.getCode()) {

				case W:

					// Scrolls pokeballs and pokemon profiles up
					if (pokeMenuIndex == 0 || pokeMenuIndex == 1) {
						pokeMenuIndex = player.getTeamSize();
					} else {
						pokeMenuIndex--;
					}

					updatePokeMenu(myStage);
					break;

				case S:

					// Scrolls pokeballs and pokemon profils down
					if (pokeMenuIndex == player.getTeamSize()) {
						pokeMenuIndex = 1;
					} else {
						pokeMenuIndex++;
					}

					updatePokeMenu(myStage);
					break;

				default:
					break;

				case X:

					// Goes back to main scene if from main scene
					if (pokeMenuOrigin.equals("swapTeam")) {
						myStage.setScene(scene);

						// Goes back to battle if from battle
					} else if (pokeMenuOrigin.equals("switch")) {
						myStage.setScene(battleScene);
						nextBattleMenu(myStage, "General");
					} else if (pokeMenuOrigin.equals("Release")) {
						lblBattleResponse.setText("You have too many pokemon!");
						myStage.setScene(battleScene);
						nextBattleMenu(myStage, "battleResponses");
					}
					break;

				case C:

					// If swapping team around and pokemon hasn't been selected
					if (pokeMenuOrigin.equals("swapTeam") && pokeSwapIndex == 0) {

						// Select pokemon
						pokeSwapIndex = pokeMenuIndex;
						updatePokeMenu(myStage);

						// If swapping team and pokemon been selected
					} else if (pokeMenuOrigin.equals("swapTeam") && pokeSwapIndex != 0) {

						// Swaps selected pokemon, resets selected pokemon for swap value
						player.reorderTeam(pokeMenuIndex - 1, pokeSwapIndex - 1);
						pokeSwapIndex = 0;
						updatePokeMenu(myStage);

						// If to switch into battle
					} else if (pokeMenuOrigin.equals("switch") || pokeMenuOrigin.equals("fainted")) {

						// Can't switch into pokemon already in battle
						if (playerPokemon.equals(player.getPokemon(pokeMenuIndex - 1))) {
							Dialog.print("that pokemon is already in battle!");

							// Can't switch fainted pokemon into battle
						} else if (player.getPokemon(pokeMenuIndex - 1).getCurrentHP() == 0) {
							Dialog.print("That pokemon can't battle right now!");
						} else {

							// Sets playerPokemon
							playerPokemon = player.getPokemon(pokeMenuIndex - 1);

							// Swtiches pokemon in battle
							battle.switchPokemon(playerPokemon);

							// Update UI
							updateProgressBar("player");
							playerPokeSprite.setImage(playerPokemon.getBackSprite());

							// Update battle response and back to battle scene
							lblBattleResponse.setText("You sent out " + playerPokemon.getName());
							nextBattleMenu(myStage, "battleResponses");
							myStage.setScene(battleScene);

						}

					} else if (pokeMenuOrigin.equals("Release")) {
						lblBattleResponse
								.setText(player.getPokemon(pokeMenuIndex - 1).getName() + " has been released");
						nextBattleMenu(myStage, "battleResponses");
						myStage.setScene(battleScene);
					}

				}
			}
		});

		myStage.setTitle("Pokemon Indigo");
		myStage.setScene(mainMenuScene);
		myStage.show();

	}

	public void nextMap(Stage myStage) {

		// locks movement
		movementLock = true;

		// Checks to see if the player is on an exit tile
		if (map.checkExit(currentMapName) == true) {

			// Changes the currentMapName to the next map's
			currentMapName = map.getNextMap();

			// Changes the loading screen text to the new map's name
			lblLoadingScreen.setText("Now Entering: " + currentMapName + "...");

			// Sets the loading screen
			myStage.setScene(loading);

			// Puts the player in their new spawnpoint
			playerStackX = map.getPlayerSpawnX();
			playerStackY = map.getPlayerSpawnY();

			Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

				// Instantiates the new map
				map = new TileGrid(map.getNextMap(), map.getNextSpawn());

				// Displays the next map
				displayBoard(root);

				// Sets the scene to the new map
				myStage.setScene(scene);
			}));

			delay.play();
		}
		// removes the movement lock
		movementLock = false;
	}

	public void starterButtonUpdate() {

		switch (starterChoiceIndex) {

		// Checks the index for which pokemon is selected, and allows the user to choose
		// that pokemon as their starter
		case 1:
			lblStarterText.setText("Torchic, the Fire Chicken Pokemon!");

			// Changes images to display the look of the selected pokemon
			imgStarterChoice1
					.setImage(new Image(getClass().getResource("/images/PokemonSprites/TorchicFront.png").toString()));
			imgStarterChoice2
					.setImage(new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
			imgStarterChoice3
					.setImage(new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));

			choice = "Torchic";
			break;

		case 2:
			lblStarterText.setText("Totodile, the Water Crocodile Pokemon!");
			imgStarterChoice1
					.setImage(new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
			imgStarterChoice2
					.setImage(new Image(getClass().getResource("/images/PokemonSprites/TotodileFront.png").toString()));
			imgStarterChoice3
					.setImage(new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));

			choice = "Totodile";
			break;

		case 3:
			lblStarterText.setText("Turtwig, the Grass Turtle Pokemon!");
			imgStarterChoice1
					.setImage(new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
			imgStarterChoice2
					.setImage(new Image(getClass().getResource("/images/Pokeballs/PokeballNormal.png").toString()));
			imgStarterChoice3
					.setImage(new Image(getClass().getResource("/images/PokemonSprites/TurtwigFront.png").toString()));

			choice = "Turtwig";
			break;

		default:
			break;
		}

	}

	public void buttonUpdate() {

		Background blue = new Background(new BackgroundFill(Color.LIGHTBLUE, null, null));
		Background white = new Background(new BackgroundFill(Color.WHITE, null, null));
		switch (battleButtonIndex) {

		// Highlights fight button
		case 1:
			lblFightButton.setBackground(blue);
			lblRunButton.setBackground(white);
			lblPokemonButton.setBackground(white);
			break;

		// highlights pokemon button
		case 2:
			lblPokemonButton.setBackground(blue);
			lblFightButton.setBackground(white);
			lblCatchButton.setBackground(white);
			break;

		// highlights catch button
		case 3:
			lblCatchButton.setBackground(blue);
			lblRunButton.setBackground(white);
			lblPokemonButton.setBackground(white);
			break;

		// highlights run button
		case 4:
			lblRunButton.setBackground(blue);
			lblFightButton.setBackground(white);
			lblCatchButton.setBackground(white);
			break;

		default:
			break;
		}
	}

	public void updateHowToPlay(String playedFrom, Stage myStage) {
		instructionsCount = 0;
		lblContinueInstructions.setVisible(true);

		myStage.setScene(howToPlayScene);

		switch (playedFrom) {

		case "Main Menu":
			btnReturn.setOnAction(event -> myStage.setScene(mainMenuScene));
			break;

		case "Overworld":
			btnReturn.setOnAction(event -> myStage.setScene(scene));
		}

	}

	public void nextBattleMenu(Stage myStage, String newMenu) {

		// Different function based on current menu
		switch (newMenu) {

		// Moves menu to General
		case "General":

			battleMenu = "General";

			// Buttons changed back
			lblFightButton.setText("Fight");
			lblCatchButton.setText("Catch");
			lblPokemonButton.setText("Pokemon");
			lblRunButton.setText("Run");

			// Button text colors
			lblFightButton.setTextFill(Color.BLACK);
			lblCatchButton.setTextFill(Color.BLACK);
			lblPokemonButton.setTextFill(Color.BLACK);
			lblRunButton.setTextFill(Color.BLACK);

			// Setting visibility
			lblBattleResponse.setVisible(false);
			lblXToBack.setVisible(false);
			lblRunButton.setVisible(true);
			lblFightButton.setVisible(true);
			lblCatchButton.setVisible(true);
			lblPokemonButton.setVisible(true);

			break;

		// Changes menu to no controls, battle responses
		case "battleResponses":

			battleMenu = "battleResponses";

			// Setting visibility
			lblBattleResponse.setVisible(true);
			lblRunButton.setVisible(false);
			lblFightButton.setVisible(false);
			lblCatchButton.setVisible(false);
			lblPokemonButton.setVisible(false);
			lblXToBack.setVisible(false);

			break;

		// Changes menu to moves
		case "Moves":

			// Battle menu set to moves
			battleMenu = "Moves";

			// Shows x to go back instruction
			lblXToBack.setVisible(true);

			// Setting label to move name and setting color to the one corresponding of it's
			// type
			// Sets text to blank when pokemon doesn't have that many moves
			lblFightButton.setText(playerPokemon.getMove(0).getName());
			lblFightButton.setTextFill(typeColor(playerPokemon.getMove(0)));

			// Displays all moves available to be displayed
			if (playerPokemon.getMovePoolSize() > 1) {
				lblPokemonButton.setText(playerPokemon.getMove(1).getName());
				lblPokemonButton.setTextFill(typeColor(playerPokemon.getMove(1)));
			} else {
				lblPokemonButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 2) {
				lblCatchButton.setText(playerPokemon.getMove(2).getName());
				lblCatchButton.setTextFill(typeColor(playerPokemon.getMove(2)));
			} else {
				lblCatchButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 3) {
				lblRunButton.setText(playerPokemon.getMove(3).getName());
				lblRunButton.setTextFill(typeColor(playerPokemon.getMove(3)));
			} else {
				lblRunButton.setText("");
			}

			break;

		case "PokemonMenu":

			// Goes to pokemon menu, keeps track that it was from switch option in battle
			pokeMenuOrigin = "switch";
			updatePokeMenu(myStage);
			myStage.setScene(pokeMenuScene);

			break;

		case "Catch":

			// Catching pokemon
			battleMenu = "battleResponses";
			nextBattleMenu(myStage, "battleResponses");

			// displays response, calls catch
			lblBattleResponse.setText(battle.catchPokemon());

			if (lblBattleResponse.getText().contains("caught!")) {
				// do nothing if pokemon is caught

			}
			// if trainer battle, opponent does not take turn on catch attempt
			// informs user you cannot catch another trainers pokemon
			else if (lblBattleResponse.getText().equals("You can't catch another trainer's Pokemon! are you crazy?")) {

				// otherwise, wild pokemon takes turn
			} else {
				battle.turnPlan(playerPokemon.getMove(0));
				battle.turnExecution("Opponent only");
			}

			break;

		case "Run":

			// Sets button nonvisible
			battleMenu = "battleResponses";
			nextBattleMenu(myStage, "battleResponses");

			// If you got away, back to main scene
			if (battle.flee() == true) {
				lblBattleResponse.setText("You got away safely!");
				battle.clearResponses();
				responseCounter = 0;

			} else if (battle.isTrainerBattle == true) {
				lblBattleResponse.setText("");
				// Otherwise, battle continues, opponent takes turn
			} else {
				lblBattleResponse.setText("You couldn't get away!");
				battle.turnPlan(playerPokemon.getMove(0));
				battle.turnExecution("Opponent only");
			}

			break;

		case "MoveLearning":

			// sets battlemenu
			battleMenu = "MoveLearning";

			// Sets button text and visibility
			lblCatchButton.setText("No");
			lblPokemonButton.setText("Yes");
			lblCatchButton.setTextFill(Color.BLACK);
			lblPokemonButton.setTextFill(Color.BLACK);
			lblCatchButton.setVisible(true);
			lblPokemonButton.setVisible(true);
			lblRunButton.setVisible(false);
			lblFightButton.setVisible(false);
			lblXToBack.setVisible(false);
			lblBattleResponse.setVisible(false);

			break;

		case "Release":

			// sets battlemenu
			battleMenu = "Release";

			// Sets button text and visibility
			lblCatchButton.setText("No");
			lblPokemonButton.setText("Yes");
			lblCatchButton.setTextFill(Color.BLACK);
			lblPokemonButton.setTextFill(Color.BLACK);
			lblCatchButton.setVisible(true);
			lblPokemonButton.setVisible(true);
			lblRunButton.setVisible(false);
			lblFightButton.setVisible(false);
			lblXToBack.setVisible(false);
			lblBattleResponse.setVisible(false);

			break;

		case "MovesToReplace":

			// Battle menu set to moves
			battleMenu = "MovesToReplace";

			// Shows x to go back instruction
			lblXToBack.setVisible(true);
			lblBattleResponse.setVisible(false);
			lblRunButton.setVisible(true);
			lblFightButton.setVisible(true);
			lblCatchButton.setVisible(true);
			lblPokemonButton.setVisible(true);

			// Setting label to move name and setting color to the one corresponding of it's
			// type
			// Sets text to blank when pokemon doesn't have that many moves
			lblFightButton.setText(playerPokemon.getMove(0).getName());
			lblFightButton.setTextFill(typeColor(playerPokemon.getMove(0)));

			if (playerPokemon.getMovePoolSize() > 1) {
				lblPokemonButton.setText(playerPokemon.getMove(1).getName());
				lblPokemonButton.setTextFill(typeColor(playerPokemon.getMove(1)));
			} else {
				lblPokemonButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 2) {
				lblCatchButton.setText(playerPokemon.getMove(2).getName());
				lblCatchButton.setTextFill(typeColor(playerPokemon.getMove(2)));
			} else {
				lblCatchButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 3) {
				lblRunButton.setText(playerPokemon.getMove(3).getName());
				lblRunButton.setTextFill(typeColor(playerPokemon.getMove(3)));
			} else {
				lblRunButton.setText("");
			}
			break;

		default:
			break;
		}

	}

	public void updatePokeMenu(Stage myStage) {

		// Variables
		String lblPokeMenuMove1Text = "";
		String lblPokeMenuMove2Text = "";
		String lblPokeMenuMove3Text = "";
		String lblPokeMenuMove4Text = "";

		// C and X control instruction based on situation
		if (pokeMenuOrigin.equals("swapTeam") && pokeSwapIndex != 0) {
			lblPokeMenuC.setText("'C' to swap");
			lblPokeMenuX.setVisible(true);
		} else if (pokeMenuOrigin.equals("swapTeam") && pokeSwapIndex == pokeMenuIndex) {
			lblPokeMenuC.setText("'C' to deselect");
			lblPokeMenuX.setVisible(true);
		} else if (pokeMenuOrigin.equals("swapTeam")) {
			lblPokeMenuC.setText("'C' to select");
			lblPokeMenuX.setVisible(true);
		} else if (pokeMenuOrigin.equals("switch")) {
			lblPokeMenuC.setText("'C' to switch into battle");
			lblPokeMenuX.setVisible(true);
		} else if (pokeMenuOrigin.equals("fainted")) {
			lblPokeMenuX.setVisible(false);
		} else if (pokeMenuOrigin.equals("Release")) {
			lblPokeMenuC.setText("'C' to release");
			lblPokeMenuX.setVisible(true);
		}

		switch (player.getTeamSize()) {

		// Only enough pokeballs for teamsize are visible
		case 1:
			imgPokeBall1.setVisible(true);
			imgPokeBall2.setVisible(false);
			imgPokeBall3.setVisible(false);
			imgPokeBall4.setVisible(false);
			imgPokeBall5.setVisible(false);
			imgPokeBall6.setVisible(false);
			break;

		case 2:
			imgPokeBall1.setVisible(true);
			imgPokeBall2.setVisible(true);
			imgPokeBall3.setVisible(false);
			imgPokeBall4.setVisible(false);
			imgPokeBall5.setVisible(false);
			imgPokeBall6.setVisible(false);
			break;

		case 3:
			imgPokeBall1.setVisible(true);
			imgPokeBall2.setVisible(true);
			imgPokeBall3.setVisible(true);
			imgPokeBall4.setVisible(false);
			imgPokeBall5.setVisible(false);
			imgPokeBall6.setVisible(false);
			break;

		case 4:
			imgPokeBall1.setVisible(true);
			imgPokeBall2.setVisible(true);
			imgPokeBall3.setVisible(true);
			imgPokeBall4.setVisible(true);
			imgPokeBall5.setVisible(false);
			imgPokeBall6.setVisible(false);
			break;

		case 5:
			imgPokeBall1.setVisible(true);
			imgPokeBall2.setVisible(true);
			imgPokeBall3.setVisible(true);
			imgPokeBall4.setVisible(true);
			imgPokeBall5.setVisible(true);
			imgPokeBall6.setVisible(false);
			break;

		case 6:
			imgPokeBall1.setVisible(true);
			imgPokeBall2.setVisible(true);
			imgPokeBall3.setVisible(true);
			imgPokeBall4.setVisible(true);
			imgPokeBall5.setVisible(true);
			imgPokeBall6.setVisible(true);

		}

		// Pokemon to display
		Pokemon selectedPokemon;

		if (pokeMenuIndex == 0) {
			// Null pointer fix
			selectedPokemon = player.getPokemon(pokeMenuIndex);
		} else {
			selectedPokemon = player.getPokemon(pokeMenuIndex - 1);
		}

		// Pokemons types
		String selectedPokemonTypes[] = selectedPokemon.getTypes().split("-");

		switch (pokeMenuIndex) {

		// Displays pokemon who's pokeball is highlighted/selected
		case 1:

			// Sets image display to pokemons sprite
			imgPokeMenuSprite.setImage(new Image(getClass()
					.getResource("/images/PokemonSprites/" + selectedPokemon.getName() + "Front.png").toString()));

			// Starts lable with name and level
			String lblPokeMenuNameText = (selectedPokemon.getName() + " L." + selectedPokemon.getLevel());

			// Adds status if possible
			if (selectedPokemon.getStatus().equals("Null") == false) {
				if (selectedPokemon.getStatus().substring(selectedPokemon.getStatus().length() - 1).equals("e")) {
					lblPokeMenuNameText += "\t\t\t" + selectedPokemon.getStatus() + "d";
				} else {
					lblPokeMenuNameText += "\t\t\t" + selectedPokemon.getStatus() + "ed";
				}
			}
			// Sets text
			lblPokeMenuName.setText(lblPokeMenuNameText);

			// Sets hp and xp fraction label
			lblPokeMenuHp.setText(selectedPokemon.getCurrentHP() + "/" + selectedPokemon.getTotalHP() + "HP");
			lblPokeMenuXp.setText(selectedPokemon.getExp() + "/" + selectedPokemon.getNextLevelUp() + "EXP");

			// Move 1 text starts with type, damage, and accuracy
			lblPokeMenuMove1Text = (selectedPokemon.getMove(0).getName() + "\nType: "
					+ selectedPokemon.getMove(0).getType() + "\nDamage: " + selectedPokemon.getMove(0).getDamage()
					+ "\nAccuracy: " + selectedPokemon.getMove(0).getAccuracy());

			// If status applied by move
			if (selectedPokemon.getMove(0).getStatus().equals("Null") == false) {

				// If status is heal, message differs
				if (selectedPokemon.getMove(0).getStatus().equals("Heal")) {
					lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " will\n"
							+ selectedPokemon.getMove(0).getStatus() + "for \n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% of damage");
					// Otherwise, displays default status application informtation
				} else {
					lblPokeMenuMove1Text += (selectedPokemon.getMove(0).getAccuracy() + "\n\n"
							+ selectedPokemon.getMove(0).getName() + " has a\n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% chance" + "\nto inflict "
							+ selectedPokemon.getMove(0).getStatus());
				}

			}

			// Adds priority if move has priority
			if (selectedPokemon.getMove(0).isPriority() == true) {
				lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " has \nspeed priority");
			}

			// Sets text
			lblPokeMenuMove1.setText(lblPokeMenuMove1Text);

			// If theres another move to be displayed, same process as move 1
			if (selectedPokemon.getMovePoolSize() > 1) {
				lblPokeMenuMove2.setVisible(true);

				lblPokeMenuMove2Text = (selectedPokemon.getMove(1).getName() + "\nType: "
						+ selectedPokemon.getMove(1).getType() + "\nDamage: " + selectedPokemon.getMove(1).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(1).getAccuracy());

				if (selectedPokemon.getMove(1).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(1).getStatus().equals("Heal")) {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " will\n"
								+ selectedPokemon.getMove(1).getStatus() + " for \n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has a\n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(1).getStatus());
					}

				}

				if (selectedPokemon.getMove(1).isPriority() == true) {
					lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove2.setText(lblPokeMenuMove2Text);

			} else {
				// If no move 2, set to invisilbe
				lblPokeMenuMove2.setVisible(false);
			}

			// Same process repeated for all moves
			if (selectedPokemon.getMovePoolSize() > 2) {
				lblPokeMenuMove3.setVisible(true);

				lblPokeMenuMove3Text = (selectedPokemon.getMove(2).getName() + "\nType: "
						+ selectedPokemon.getMove(2).getType() + "\nDamage: " + selectedPokemon.getMove(2).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(2).getAccuracy());

				if (selectedPokemon.getMove(2).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(2).getStatus().equals("Heal")) {
						lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " will\n"
								+ selectedPokemon.getMove(2).getStatus() + " for \n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove3Text += (selectedPokemon.getMove(2).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(2).getName() + " has a\n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(2).getStatus());
					}

				}

				if (selectedPokemon.getMove(2).isPriority() == true) {
					lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove3.setText(lblPokeMenuMove3Text);

			} else {
				lblPokeMenuMove3.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 3) {

				lblPokeMenuMove4.setVisible(true);

				lblPokeMenuMove4Text = (selectedPokemon.getMove(3).getName() + "\nType: "
						+ selectedPokemon.getMove(3).getType() + "\nDamage: " + selectedPokemon.getMove(3).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(3).getAccuracy());

				if (selectedPokemon.getMove(3).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(3).getStatus().equals("Heal")) {
						lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " will\n"
								+ selectedPokemon.getMove(3).getStatus() + " for \n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove4Text += (selectedPokemon.getMove(3).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(3).getName() + " has a\n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(3).getStatus());
					}

				}

				if (selectedPokemon.getMove(3).isPriority() == true) {
					lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove4.setText(lblPokeMenuMove4Text);

			} else {
				lblPokeMenuMove4.setVisible(false);
			}

			// Sets stats label with attack, defense and speed
			lblPokeMenuStats.setText("Attack: " + selectedPokemon.getAttack() + "\nDefense: "
					+ selectedPokemon.getDefense() + "\nSpeed: " + selectedPokemon.getSpeed());

			// Displays both types if pokemon has 2 types
			// Displays one type if pokemon has 1 type
			if (selectedPokemonTypes[1].equals("Null")) {
				lblPokeMenuTypes.setText("Types:\n" + selectedPokemonTypes[0]);
			} else {
				lblPokeMenuTypes.setText("Types: \n" + selectedPokemonTypes[0] + "\n" + selectedPokemonTypes[1]);
			}

			// Sets to highlighted pokeball image
			if (player.getPokemon(0).getCurrentHP() == 0) {
				// Fainted ball if pokemon hp is 0
				imgPokeBall1.setImage(selectedFaintedPokeBall);
			} else {
				// Otherwise, normal ball
				imgPokeBall1.setImage(selectedNormalPokeBall);
			}

			// Sets all other pokeballs to not highlighted, stays fainted ball if pokemon is
			// fainted
			if (pokeSwapIndex != 2 && player.getTeamSize() > 1) {
				if (player.getPokemon(1).getCurrentHP() == 0) {
					imgPokeBall2.setImage(faintedPokeBall);
				} else {
					imgPokeBall2.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 3 && player.getTeamSize() > 2) {
				if (player.getPokemon(2).getCurrentHP() == 0) {
					imgPokeBall3.setImage(faintedPokeBall);
				} else {
					imgPokeBall3.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 4 && player.getTeamSize() > 3) {
				if (player.getPokemon(3).getCurrentHP() == 0) {
					imgPokeBall4.setImage(faintedPokeBall);
				} else {
					imgPokeBall4.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 5 && player.getTeamSize() > 4) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall5.setImage(faintedPokeBall);
				} else {
					imgPokeBall5.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 6 && player.getTeamSize() > 5) {
				if (player.getPokemon(5).getCurrentHP() == 0) {
					imgPokeBall6.setImage(faintedPokeBall);
				} else {
					imgPokeBall6.setImage(normalPokeBall);
				}
			}

			break;

		// Same process for cases 2 through 6
		case 2:

			imgPokeMenuSprite.setImage(new Image(getClass()
					.getResource(
							"/images/PokemonSprites/" + player.getPokemon(pokeMenuIndex - 1).getName() + "Front.png")
					.toString()));

			String lblPokeMenuNameText2 = (selectedPokemon.getName() + " L." + selectedPokemon.getLevel());

			if (selectedPokemon.getStatus().equals("Null") == false) {
				if (selectedPokemon.getStatus().substring(selectedPokemon.getStatus().length() - 1).equals("e")) {
					lblPokeMenuNameText2 += "\t\t\t" + selectedPokemon.getStatus() + "d";
				} else {
					lblPokeMenuNameText2 += "\t\t\t" + selectedPokemon.getStatus() + "ed";
				}
			}

			lblPokeMenuName.setText(lblPokeMenuNameText2);

			lblPokeMenuHp.setText(selectedPokemon.getCurrentHP() + "/" + selectedPokemon.getTotalHP() + "HP");
			lblPokeMenuXp.setText(selectedPokemon.getExp() + "/" + selectedPokemon.getNextLevelUp() + "EXP");

			lblPokeMenuMove1Text = (selectedPokemon.getMove(0).getName() + "\nType: "
					+ selectedPokemon.getMove(0).getType() + "\nDamage: " + selectedPokemon.getMove(0).getDamage()
					+ "\nAccuracy: " + selectedPokemon.getMove(0).getAccuracy());

			if (selectedPokemon.getMove(0).getStatus().equals("Null") == false) {

				if (selectedPokemon.getMove(0).getStatus().equals("Heal")) {
					lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " will\n"
							+ selectedPokemon.getMove(0).getStatus() + "for \n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% of damage");
				} else {
					lblPokeMenuMove1Text += (selectedPokemon.getMove(0).getAccuracy() + "\n\n"
							+ selectedPokemon.getMove(0).getName() + " has a\n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% chance" + "\nto inflict "
							+ selectedPokemon.getMove(0).getStatus());
				}

			}

			if (selectedPokemon.getMove(0).isPriority() == true) {
				lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " has \nspeed priority");
			}

			lblPokeMenuMove1.setText(lblPokeMenuMove1Text);

			if (selectedPokemon.getMovePoolSize() > 1) {
				lblPokeMenuMove2.setVisible(true);

				lblPokeMenuMove2Text = (selectedPokemon.getMove(1).getName() + "\nType: "
						+ selectedPokemon.getMove(1).getType() + "\nDamage: " + selectedPokemon.getMove(1).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(1).getAccuracy());

				if (selectedPokemon.getMove(1).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(1).getStatus().equals("Heal")) {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " will\n"
								+ selectedPokemon.getMove(1).getStatus() + " for \n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has a\n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(1).getStatus());
					}

				}

				if (selectedPokemon.getMove(1).isPriority() == true) {
					lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove2.setText(lblPokeMenuMove2Text);

			} else {
				lblPokeMenuMove2.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 2) {
				lblPokeMenuMove3.setVisible(true);

				lblPokeMenuMove3Text = (selectedPokemon.getMove(2).getName() + "\nType: "
						+ selectedPokemon.getMove(2).getType() + "\nDamage: " + selectedPokemon.getMove(2).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(2).getAccuracy());

				if (selectedPokemon.getMove(2).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(2).getStatus().equals("Heal")) {
						lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " will\n"
								+ selectedPokemon.getMove(2).getStatus() + " for \n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove3Text += (selectedPokemon.getMove(2).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(2).getName() + " has a\n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(2).getStatus());
					}

				}

				if (selectedPokemon.getMove(2).isPriority() == true) {
					lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove3.setText(lblPokeMenuMove3Text);

			} else {
				lblPokeMenuMove3.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 3) {

				lblPokeMenuMove4.setVisible(true);

				lblPokeMenuMove4Text = (selectedPokemon.getMove(3).getName() + "\nType: "
						+ selectedPokemon.getMove(3).getType() + "\nDamage: " + selectedPokemon.getMove(3).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(3).getAccuracy());

				if (selectedPokemon.getMove(3).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(3).getStatus().equals("Heal")) {
						lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " will\n"
								+ selectedPokemon.getMove(3).getStatus() + " for \n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove4Text += (selectedPokemon.getMove(3).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(3).getName() + " has a\n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(3).getStatus());
					}

				}

				if (selectedPokemon.getMove(3).isPriority() == true) {
					lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove4.setText(lblPokeMenuMove4Text);

			} else {
				lblPokeMenuMove4.setVisible(false);
			}

			lblPokeMenuStats.setText("Attack: " + selectedPokemon.getAttack() + "\nDefense: "
					+ selectedPokemon.getDefense() + "\nSpeed: " + selectedPokemon.getSpeed());

			if (selectedPokemonTypes[1].equals("Null")) {
				lblPokeMenuTypes.setText("Types:\n" + selectedPokemonTypes[0]);
			} else {
				lblPokeMenuTypes.setText("Types: \n" + selectedPokemonTypes[0] + "\n" + selectedPokemonTypes[1]);
			}

			if (player.getPokemon(1).getCurrentHP() == 0) {
				imgPokeBall2.setImage(selectedFaintedPokeBall);
			} else {
				imgPokeBall2.setImage(selectedNormalPokeBall);
			}

			if (pokeSwapIndex != 1 && player.getTeamSize() > 0) {
				if (player.getPokemon(0).getCurrentHP() == 0) {
					imgPokeBall1.setImage(faintedPokeBall);
				} else {
					imgPokeBall1.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 3 && player.getTeamSize() > 2) {
				if (player.getPokemon(2).getCurrentHP() == 0) {
					imgPokeBall3.setImage(faintedPokeBall);
				} else {
					imgPokeBall3.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 4 && player.getTeamSize() > 3) {
				if (player.getPokemon(3).getCurrentHP() == 0) {
					imgPokeBall4.setImage(faintedPokeBall);
				} else {
					imgPokeBall4.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 5 && player.getTeamSize() > 4) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall5.setImage(faintedPokeBall);
				} else {
					imgPokeBall5.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 6 && player.getTeamSize() > 5) {
				if (player.getPokemon(5).getCurrentHP() == 0) {
					imgPokeBall6.setImage(faintedPokeBall);
				} else {
					imgPokeBall6.setImage(normalPokeBall);
				}
			}

			break;

		case 3:

			imgPokeMenuSprite.setImage(new Image(getClass()
					.getResource(
							"/images/PokemonSprites/" + player.getPokemon(pokeMenuIndex - 1).getName() + "Front.png")
					.toString()));

			String lblPokeMenuNameText3 = (selectedPokemon.getName() + " L." + selectedPokemon.getLevel());

			if (selectedPokemon.getStatus().equals("Null") == false) {
				if (selectedPokemon.getStatus().substring(selectedPokemon.getStatus().length() - 1).equals("e")) {
					lblPokeMenuNameText3 += "\t\t\t" + selectedPokemon.getStatus() + "d";
				} else {
					lblPokeMenuNameText3 += "\t\t\t" + selectedPokemon.getStatus() + "ed";
				}
			}

			lblPokeMenuName.setText(lblPokeMenuNameText3);

			lblPokeMenuHp.setText(selectedPokemon.getCurrentHP() + "/" + selectedPokemon.getTotalHP() + "HP");
			lblPokeMenuXp.setText(selectedPokemon.getExp() + "/" + selectedPokemon.getNextLevelUp() + "EXP");

			lblPokeMenuMove1Text = (selectedPokemon.getMove(0).getName() + "\nType: "
					+ selectedPokemon.getMove(0).getType() + "\nDamage: " + selectedPokemon.getMove(0).getDamage()
					+ "\nAccuracy: " + selectedPokemon.getMove(0).getAccuracy());

			if (selectedPokemon.getMove(0).getStatus().equals("Null") == false) {

				if (selectedPokemon.getMove(0).getStatus().equals("Heal")) {
					lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " will\n"
							+ selectedPokemon.getMove(0).getStatus() + "for \n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% of damage");
				} else {
					lblPokeMenuMove1Text += (selectedPokemon.getMove(0).getAccuracy() + "\n\n"
							+ selectedPokemon.getMove(0).getName() + " has a\n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% chance" + "\nto inflict "
							+ selectedPokemon.getMove(0).getStatus());
				}

			}

			if (selectedPokemon.getMove(0).isPriority() == true) {
				lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " has \nspeed priority");
			}

			lblPokeMenuMove1.setText(lblPokeMenuMove1Text);

			if (selectedPokemon.getMovePoolSize() > 1) {
				lblPokeMenuMove2.setVisible(true);

				lblPokeMenuMove2Text = (selectedPokemon.getMove(1).getName() + "\nType: "
						+ selectedPokemon.getMove(1).getType() + "\nDamage: " + selectedPokemon.getMove(1).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(1).getAccuracy());

				if (selectedPokemon.getMove(1).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(1).getStatus().equals("Heal")) {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " will\n"
								+ selectedPokemon.getMove(1).getStatus() + " for \n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has a\n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(1).getStatus());
					}

				}

				if (selectedPokemon.getMove(1).isPriority() == true) {
					lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove2.setText(lblPokeMenuMove2Text);

			} else {
				lblPokeMenuMove2.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 2) {
				lblPokeMenuMove3.setVisible(true);

				lblPokeMenuMove3Text = (selectedPokemon.getMove(2).getName() + "\nType: "
						+ selectedPokemon.getMove(2).getType() + "\nDamage: " + selectedPokemon.getMove(2).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(2).getAccuracy());

				if (selectedPokemon.getMove(2).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(2).getStatus().equals("Heal")) {
						lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " will\n"
								+ selectedPokemon.getMove(2).getStatus() + " for \n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove3Text += (selectedPokemon.getMove(2).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(2).getName() + " has a\n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(2).getStatus());
					}

				}

				if (selectedPokemon.getMove(2).isPriority() == true) {
					lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove3.setText(lblPokeMenuMove3Text);

			} else {
				lblPokeMenuMove3.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 3) {

				lblPokeMenuMove4.setVisible(true);

				lblPokeMenuMove4Text = (selectedPokemon.getMove(3).getName() + "\nType: "
						+ selectedPokemon.getMove(3).getType() + "\nDamage: " + selectedPokemon.getMove(3).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(3).getAccuracy());

				if (selectedPokemon.getMove(3).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(3).getStatus().equals("Heal")) {
						lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " will\n"
								+ selectedPokemon.getMove(3).getStatus() + " for \n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove4Text += (selectedPokemon.getMove(3).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(3).getName() + " has a\n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(3).getStatus());
					}

				}

				if (selectedPokemon.getMove(3).isPriority() == true) {
					lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove4.setText(lblPokeMenuMove4Text);

			} else {
				lblPokeMenuMove4.setVisible(false);
			}

			lblPokeMenuStats.setText("Attack: " + selectedPokemon.getAttack() + "\nDefense: "
					+ selectedPokemon.getDefense() + "\nSpeed: " + selectedPokemon.getSpeed());

			if (selectedPokemonTypes[1].equals("Null")) {
				lblPokeMenuTypes.setText("Types:\n" + selectedPokemonTypes[0]);
			} else {
				lblPokeMenuTypes.setText("Types: \n" + selectedPokemonTypes[0] + "\n" + selectedPokemonTypes[1]);
			}

			if (player.getPokemon(2).getCurrentHP() == 0) {
				imgPokeBall3.setImage(selectedFaintedPokeBall);
			} else {
				imgPokeBall3.setImage(selectedNormalPokeBall);
			}

			if (pokeSwapIndex != 1 && player.getTeamSize() > 0) {
				if (player.getPokemon(0).getCurrentHP() == 0) {
					imgPokeBall1.setImage(faintedPokeBall);
				} else {
					imgPokeBall1.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 2 && player.getTeamSize() > 1) {
				if (player.getPokemon(1).getCurrentHP() == 0) {
					imgPokeBall2.setImage(faintedPokeBall);
				} else {
					imgPokeBall2.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 4 && player.getTeamSize() > 3) {
				if (player.getPokemon(3).getCurrentHP() == 0) {
					imgPokeBall4.setImage(faintedPokeBall);
				} else {
					imgPokeBall4.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 5 && player.getTeamSize() > 4) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall5.setImage(faintedPokeBall);
				} else {
					imgPokeBall5.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 6 && player.getTeamSize() > 5) {
				if (player.getPokemon(5).getCurrentHP() == 0) {
					imgPokeBall6.setImage(faintedPokeBall);
				} else {
					imgPokeBall6.setImage(normalPokeBall);
				}
			}

			break;

		case 4:

			imgPokeMenuSprite.setImage(new Image(getClass()
					.getResource(
							"/images/PokemonSprites/" + player.getPokemon(pokeMenuIndex - 1).getName() + "Front.png")
					.toString()));

			String lblPokeMenuNameText4 = (selectedPokemon.getName() + " L." + selectedPokemon.getLevel());

			if (selectedPokemon.getStatus().equals("Null") == false) {
				if (selectedPokemon.getStatus().substring(selectedPokemon.getStatus().length() - 1).equals("e")) {
					lblPokeMenuNameText4 += "\t\t\t" + selectedPokemon.getStatus() + "d";
				} else {
					lblPokeMenuNameText4 += "\t\t\t" + selectedPokemon.getStatus() + "ed";
				}
			}

			lblPokeMenuName.setText(lblPokeMenuNameText4);

			lblPokeMenuHp.setText(selectedPokemon.getCurrentHP() + "/" + selectedPokemon.getTotalHP() + "HP");
			lblPokeMenuXp.setText(selectedPokemon.getExp() + "/" + selectedPokemon.getNextLevelUp() + "EXP");

			lblPokeMenuMove1Text = (selectedPokemon.getMove(0).getName() + "\nType: "
					+ selectedPokemon.getMove(0).getType() + "\nDamage: " + selectedPokemon.getMove(0).getDamage()
					+ "\nAccuracy: " + selectedPokemon.getMove(0).getAccuracy());

			if (selectedPokemon.getMove(0).getStatus().equals("Null") == false) {

				if (selectedPokemon.getMove(0).getStatus().equals("Heal")) {
					lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " will\n"
							+ selectedPokemon.getMove(0).getStatus() + "for \n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% of damage");
				} else {
					lblPokeMenuMove1Text += (selectedPokemon.getMove(0).getAccuracy() + "\n\n"
							+ selectedPokemon.getMove(0).getName() + " has a\n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% chance" + "\nto inflict "
							+ selectedPokemon.getMove(0).getStatus());
				}

			}

			if (selectedPokemon.getMove(0).isPriority() == true) {
				lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " has \nspeed priority");
			}

			lblPokeMenuMove1.setText(lblPokeMenuMove1Text);

			if (selectedPokemon.getMovePoolSize() > 1) {
				lblPokeMenuMove2.setVisible(true);

				lblPokeMenuMove2Text = (selectedPokemon.getMove(1).getName() + "\nType: "
						+ selectedPokemon.getMove(1).getType() + "\nDamage: " + selectedPokemon.getMove(1).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(1).getAccuracy());

				if (selectedPokemon.getMove(1).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(1).getStatus().equals("Heal")) {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " will\n"
								+ selectedPokemon.getMove(1).getStatus() + " for \n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has a\n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(1).getStatus());
					}

				}

				if (selectedPokemon.getMove(1).isPriority() == true) {
					lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove2.setText(lblPokeMenuMove2Text);

			} else {
				lblPokeMenuMove2.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 2) {
				lblPokeMenuMove3.setVisible(true);

				lblPokeMenuMove3Text = (selectedPokemon.getMove(2).getName() + "\nType: "
						+ selectedPokemon.getMove(2).getType() + "\nDamage: " + selectedPokemon.getMove(2).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(2).getAccuracy());

				if (selectedPokemon.getMove(2).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(2).getStatus().equals("Heal")) {
						lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " will\n"
								+ selectedPokemon.getMove(2).getStatus() + " for \n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove3Text += (selectedPokemon.getMove(2).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(2).getName() + " has a\n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(2).getStatus());
					}

				}

				if (selectedPokemon.getMove(2).isPriority() == true) {
					lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove3.setText(lblPokeMenuMove3Text);

			} else {
				lblPokeMenuMove3.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 3) {

				lblPokeMenuMove4.setVisible(true);

				lblPokeMenuMove4Text = (selectedPokemon.getMove(3).getName() + "\nType: "
						+ selectedPokemon.getMove(3).getType() + "\nDamage: " + selectedPokemon.getMove(3).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(3).getAccuracy());

				if (selectedPokemon.getMove(3).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(3).getStatus().equals("Heal")) {
						lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " will\n"
								+ selectedPokemon.getMove(3).getStatus() + " for \n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove4Text += (selectedPokemon.getMove(3).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(3).getName() + " has a\n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(3).getStatus());
					}

				}

				if (selectedPokemon.getMove(3).isPriority() == true) {
					lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove4.setText(lblPokeMenuMove4Text);

			} else {
				lblPokeMenuMove4.setVisible(false);
			}

			lblPokeMenuStats.setText("Attack: " + selectedPokemon.getAttack() + "\nDefense: "
					+ selectedPokemon.getDefense() + "\nSpeed: " + selectedPokemon.getSpeed());

			if (selectedPokemonTypes[1].equals("Null")) {
				lblPokeMenuTypes.setText("Types:\n" + selectedPokemonTypes[0]);
			} else {
				lblPokeMenuTypes.setText("Types: \n" + selectedPokemonTypes[0] + "\n" + selectedPokemonTypes[1]);
			}

			if (player.getPokemon(3).getCurrentHP() == 0) {
				imgPokeBall4.setImage(selectedFaintedPokeBall);
			} else {
				imgPokeBall4.setImage(selectedNormalPokeBall);
			}

			if (pokeSwapIndex != 1 && player.getTeamSize() > 0) {
				if (player.getPokemon(0).getCurrentHP() == 0) {
					imgPokeBall1.setImage(faintedPokeBall);
				} else {
					imgPokeBall1.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 2 && player.getTeamSize() > 1) {
				if (player.getPokemon(1).getCurrentHP() == 0) {
					imgPokeBall2.setImage(faintedPokeBall);
				} else {
					imgPokeBall2.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 3 && player.getTeamSize() > 2) {
				if (player.getPokemon(3).getCurrentHP() == 0) {
					imgPokeBall3.setImage(faintedPokeBall);
				} else {
					imgPokeBall3.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 5 && player.getTeamSize() > 4) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall5.setImage(faintedPokeBall);
				} else {
					imgPokeBall5.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 6 && player.getTeamSize() > 5) {
				if (player.getPokemon(5).getCurrentHP() == 0) {
					imgPokeBall6.setImage(faintedPokeBall);
				} else {
					imgPokeBall6.setImage(normalPokeBall);
				}
			}

			break;

		case 5:

			imgPokeMenuSprite.setImage(new Image(getClass()
					.getResource(
							"/images/PokemonSprites/" + player.getPokemon(pokeMenuIndex - 1).getName() + "Front.png")
					.toString()));

			String lblPokeMenuNameText5 = (selectedPokemon.getName() + " L." + selectedPokemon.getLevel());

			if (selectedPokemon.getStatus().equals("Null") == false) {
				if (selectedPokemon.getStatus().substring(selectedPokemon.getStatus().length() - 1).equals("e")) {
					lblPokeMenuNameText5 += "\t\t\t" + selectedPokemon.getStatus() + "d";

				} else {
					lblPokeMenuNameText5 += "\t\t\t" + selectedPokemon.getStatus() + "ed";
				}
			}

			lblPokeMenuName.setText(lblPokeMenuNameText5);

			lblPokeMenuHp.setText(selectedPokemon.getCurrentHP() + "/" + selectedPokemon.getTotalHP() + "HP");
			lblPokeMenuXp.setText(selectedPokemon.getExp() + "/" + selectedPokemon.getNextLevelUp() + "EXP");

			lblPokeMenuMove1Text = (selectedPokemon.getMove(0).getName() + "\nType: "
					+ selectedPokemon.getMove(0).getType() + "\nDamage: " + selectedPokemon.getMove(0).getDamage()
					+ "\nAccuracy: " + selectedPokemon.getMove(0).getAccuracy());

			if (selectedPokemon.getMove(0).getStatus().equals("Null") == false) {

				if (selectedPokemon.getMove(0).getStatus().equals("Heal")) {
					lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " will\n"
							+ selectedPokemon.getMove(0).getStatus() + "for \n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% of damage");
				} else {
					lblPokeMenuMove1Text += (selectedPokemon.getMove(0).getAccuracy() + "\n\n"
							+ selectedPokemon.getMove(0).getName() + " has a\n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% chance" + "\nto inflict "
							+ selectedPokemon.getMove(0).getStatus());
				}

			}

			if (selectedPokemon.getMove(0).isPriority() == true) {
				lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " has \nspeed priority");
			}

			lblPokeMenuMove1.setText(lblPokeMenuMove1Text);

			if (selectedPokemon.getMovePoolSize() > 1) {
				lblPokeMenuMove2.setVisible(true);

				lblPokeMenuMove2Text = (selectedPokemon.getMove(1).getName() + "\nType: "
						+ selectedPokemon.getMove(1).getType() + "\nDamage: " + selectedPokemon.getMove(1).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(1).getAccuracy());

				if (selectedPokemon.getMove(1).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(1).getStatus().equals("Heal")) {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " will\n"
								+ selectedPokemon.getMove(1).getStatus() + " for \n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has a\n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(1).getStatus());
					}

				}

				if (selectedPokemon.getMove(1).isPriority() == true) {
					lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove2.setText(lblPokeMenuMove2Text);

			} else {
				lblPokeMenuMove2.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 2) {
				lblPokeMenuMove3.setVisible(true);

				lblPokeMenuMove3Text = (selectedPokemon.getMove(2).getName() + "\nType: "
						+ selectedPokemon.getMove(2).getType() + "\nDamage: " + selectedPokemon.getMove(2).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(2).getAccuracy());

				if (selectedPokemon.getMove(2).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(2).getStatus().equals("Heal")) {
						lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " will\n"
								+ selectedPokemon.getMove(2).getStatus() + " for \n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove3Text += (selectedPokemon.getMove(2).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(2).getName() + " has a\n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(2).getStatus());
					}

				}

				if (selectedPokemon.getMove(2).isPriority() == true) {
					lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove3.setText(lblPokeMenuMove3Text);

			} else {
				lblPokeMenuMove3.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 3) {

				lblPokeMenuMove4.setVisible(true);

				lblPokeMenuMove4Text = (selectedPokemon.getMove(3).getName() + "\nType: "
						+ selectedPokemon.getMove(3).getType() + "\nDamage: " + selectedPokemon.getMove(3).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(3).getAccuracy());

				if (selectedPokemon.getMove(3).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(3).getStatus().equals("Heal")) {
						lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " will\n"
								+ selectedPokemon.getMove(3).getStatus() + " for \n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove4Text += (selectedPokemon.getMove(3).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(3).getName() + " has a\n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(3).getStatus());
					}

				}

				if (selectedPokemon.getMove(3).isPriority() == true) {
					lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove4.setText(lblPokeMenuMove4Text);

			} else {
				lblPokeMenuMove4.setVisible(false);
			}

			lblPokeMenuStats.setText("Attack: " + selectedPokemon.getAttack() + "\nDefense: "
					+ selectedPokemon.getDefense() + "\nSpeed: " + selectedPokemon.getSpeed());

			if (selectedPokemonTypes[1].equals("Null")) {
				lblPokeMenuTypes.setText("Types:\n" + selectedPokemonTypes[0]);
			} else {
				lblPokeMenuTypes.setText("Types: \n" + selectedPokemonTypes[0] + "\n" + selectedPokemonTypes[1]);
			}

			if (player.getPokemon(4).getCurrentHP() == 0) {
				imgPokeBall5.setImage(selectedFaintedPokeBall);
			} else {
				imgPokeBall5.setImage(selectedNormalPokeBall);
			}

			if (pokeSwapIndex != 1 && player.getTeamSize() > 0) {
				if (player.getPokemon(0).getCurrentHP() == 0) {
					imgPokeBall1.setImage(faintedPokeBall);
				} else {
					imgPokeBall1.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 2 && player.getTeamSize() > 1) {
				if (player.getPokemon(1).getCurrentHP() == 0) {
					imgPokeBall2.setImage(faintedPokeBall);
				} else {
					imgPokeBall2.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 3 && player.getTeamSize() > 2) {
				if (player.getPokemon(3).getCurrentHP() == 0) {
					imgPokeBall3.setImage(faintedPokeBall);
				} else {
					imgPokeBall3.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 4 && player.getTeamSize() > 3) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall4.setImage(faintedPokeBall);
				} else {
					imgPokeBall4.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 6 && player.getTeamSize() > 5) {
				if (player.getPokemon(5).getCurrentHP() == 0) {
					imgPokeBall6.setImage(faintedPokeBall);
				} else {
					imgPokeBall6.setImage(normalPokeBall);
				}
			}

			break;

		case 6:

			imgPokeMenuSprite.setImage(new Image(getClass()
					.getResource(
							"/images/PokemonSprites/" + player.getPokemon(pokeMenuIndex - 1).getName() + "Front.png")
					.toString()));

			String lblPokeMenuNameText6 = (selectedPokemon.getName() + " L." + selectedPokemon.getLevel());

			if (selectedPokemon.getStatus().equals("Null") == false) {
				if (selectedPokemon.getStatus().substring(selectedPokemon.getStatus().length() - 1).equals("e")) {
					lblPokeMenuNameText6 += "\t\t\t" + selectedPokemon.getStatus() + "d";
				} else {
					lblPokeMenuNameText6 += "\t\t\t" + selectedPokemon.getStatus() + "ed";
				}
			}

			lblPokeMenuName.setText(lblPokeMenuNameText6);

			lblPokeMenuHp.setText(selectedPokemon.getCurrentHP() + "/" + selectedPokemon.getTotalHP() + "HP");
			lblPokeMenuXp.setText(selectedPokemon.getExp() + "/" + selectedPokemon.getNextLevelUp() + "EXP");

			lblPokeMenuMove1Text = (selectedPokemon.getMove(0).getName() + "\nType: "
					+ selectedPokemon.getMove(0).getType() + "\nDamage: " + selectedPokemon.getMove(0).getDamage()
					+ "\nAccuracy: " + selectedPokemon.getMove(0).getAccuracy());

			if (selectedPokemon.getMove(0).getStatus().equals("Null") == false) {

				if (selectedPokemon.getMove(0).getStatus().equals("Heal")) {
					lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " will\n"
							+ selectedPokemon.getMove(0).getStatus() + "for \n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% of damage");
				} else {
					lblPokeMenuMove1Text += (selectedPokemon.getMove(0).getAccuracy() + "\n\n"
							+ selectedPokemon.getMove(0).getName() + " has a\n"
							+ selectedPokemon.getMove(0).getStatusRate() + "% chance" + "\nto inflict "
							+ selectedPokemon.getMove(0).getStatus());
				}

			}

			if (selectedPokemon.getMove(0).isPriority() == true) {
				lblPokeMenuMove1Text += ("\n\n" + selectedPokemon.getMove(0).getName() + " has \nspeed priority");
			}

			lblPokeMenuMove1.setText(lblPokeMenuMove1Text);

			if (selectedPokemon.getMovePoolSize() > 1) {
				lblPokeMenuMove2.setVisible(true);

				lblPokeMenuMove2Text = (selectedPokemon.getMove(1).getName() + "\nType: "
						+ selectedPokemon.getMove(1).getType() + "\nDamage: " + selectedPokemon.getMove(1).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(1).getAccuracy());

				if (selectedPokemon.getMove(1).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(1).getStatus().equals("Heal")) {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " will\n"
								+ selectedPokemon.getMove(1).getStatus() + " for \n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has a\n"
								+ selectedPokemon.getMove(1).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(1).getStatus());
					}

				}

				if (selectedPokemon.getMove(1).isPriority() == true) {
					lblPokeMenuMove2Text += ("\n\n" + selectedPokemon.getMove(1).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove2.setText(lblPokeMenuMove2Text);

			} else {
				lblPokeMenuMove2.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 2) {
				lblPokeMenuMove3.setVisible(true);

				lblPokeMenuMove3Text = (selectedPokemon.getMove(2).getName() + "\nType: "
						+ selectedPokemon.getMove(2).getType() + "\nDamage: " + selectedPokemon.getMove(2).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(2).getAccuracy());

				if (selectedPokemon.getMove(2).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(2).getStatus().equals("Heal")) {
						lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " will\n"
								+ selectedPokemon.getMove(2).getStatus() + " for \n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove3Text += (selectedPokemon.getMove(2).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(2).getName() + " has a\n"
								+ selectedPokemon.getMove(2).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(2).getStatus());
					}

				}

				if (selectedPokemon.getMove(2).isPriority() == true) {
					lblPokeMenuMove3Text += ("\n\n" + selectedPokemon.getMove(2).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove3.setText(lblPokeMenuMove3Text);

			} else {
				lblPokeMenuMove3.setVisible(false);
			}

			if (selectedPokemon.getMovePoolSize() > 3) {

				lblPokeMenuMove4.setVisible(true);

				lblPokeMenuMove4Text = (selectedPokemon.getMove(3).getName() + "\nType: "
						+ selectedPokemon.getMove(3).getType() + "\nDamage: " + selectedPokemon.getMove(3).getDamage()
						+ "\nAccuracy: " + selectedPokemon.getMove(3).getAccuracy());

				if (selectedPokemon.getMove(3).getStatus().equals("Null") == false) {

					if (selectedPokemon.getMove(3).getStatus().equals("Heal")) {
						lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " will\n"
								+ selectedPokemon.getMove(3).getStatus() + " for \n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% of damage");
					} else {
						lblPokeMenuMove4Text += (selectedPokemon.getMove(3).getAccuracy() + "\n\n"
								+ selectedPokemon.getMove(3).getName() + " has a\n"
								+ selectedPokemon.getMove(3).getStatusRate() + "% chance" + "\nto inflict "
								+ selectedPokemon.getMove(3).getStatus());
					}

				}

				if (selectedPokemon.getMove(3).isPriority() == true) {
					lblPokeMenuMove4Text += ("\n\n" + selectedPokemon.getMove(3).getName() + " has \nspeed priority");
				}

				lblPokeMenuMove4.setText(lblPokeMenuMove4Text);

			} else {
				lblPokeMenuMove4.setVisible(false);
			}

			lblPokeMenuStats.setText("Attack: " + selectedPokemon.getAttack() + "\nDefense: "
					+ selectedPokemon.getDefense() + "\nSpeed: " + selectedPokemon.getSpeed());

			if (selectedPokemonTypes[1].equals("Null")) {
				lblPokeMenuTypes.setText("Types:\n" + selectedPokemonTypes[0]);
			} else {
				lblPokeMenuTypes.setText("Types: \n" + selectedPokemonTypes[0] + "\n" + selectedPokemonTypes[1]);
			}

			if (player.getPokemon(5).getCurrentHP() == 0) {
				imgPokeBall6.setImage(selectedFaintedPokeBall);
			} else {
				imgPokeBall6.setImage(selectedNormalPokeBall);
			}

			if (pokeSwapIndex != 1 && player.getTeamSize() > 0) {
				if (player.getPokemon(0).getCurrentHP() == 0) {
					imgPokeBall1.setImage(faintedPokeBall);
				} else {
					imgPokeBall1.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 2 && player.getTeamSize() > 1) {
				if (player.getPokemon(1).getCurrentHP() == 0) {
					imgPokeBall2.setImage(faintedPokeBall);
				} else {
					imgPokeBall2.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 3 && player.getTeamSize() > 2) {
				if (player.getPokemon(3).getCurrentHP() == 0) {
					imgPokeBall3.setImage(faintedPokeBall);
				} else {
					imgPokeBall3.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 4 && player.getTeamSize() > 3) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall4.setImage(faintedPokeBall);
				} else {
					imgPokeBall4.setImage(normalPokeBall);
				}
			}

			if (pokeSwapIndex != 5 && player.getTeamSize() > 4) {
				if (player.getPokemon(4).getCurrentHP() == 0) {
					imgPokeBall5.setImage(faintedPokeBall);
				} else {
					imgPokeBall5.setImage(normalPokeBall);
				}
			}

			break;

		default:
			break;
		}

	}

	public void useMove(Stage myStage) {

		switch (battleButtonIndex) {

		// Queues up a turn with selected move
		case 1:
			battle.turnPlan(playerPokemon.getMove(0));
			break;

		case 2:
			battle.turnPlan(playerPokemon.getMove(1));
			break;
		case 3:
			battle.turnPlan(playerPokemon.getMove(2));
			break;

		case 4:
			battle.turnPlan(playerPokemon.getMove(3));
			break;
		}

		// Exectues whole turn
		battle.turnExecution("First");
		battle.turnExecution("Second");

		// Calls battle response, sets battle menu to responses
		nextBattleResponse();
		nextBattleMenu(myStage, "battleResponses");

	}

	public boolean nextBattleResponse() {

		Boolean lastResponse = false;

		// Cycles through responses
		if (responseCounter < battle.responseAmount()) {
			lblBattleResponse.setText(battle.battleResponses(responseCounter));

			// Updates hp bars if pokemon was fainted
			if (battle.battleResponses(responseCounter).contains("fainted")) {
				if (battle.isOpponentFainted() == true) {
					updateProgressBar("opponent");

					// If wild encounter, endbattle
					if (battle.isTrainerBattle == false) {
						endBattle = true;
					}
				} else if (battle.isPlayerFainted() == true) {
					updateProgressBar("player");
					updateProgressBar("opponent");
				}
			}

			// Updates progress bar if pokemon levels up
			if (battle.battleResponses(responseCounter).contains("leveled up")) {
				if (battle.isGoingToEvolve() == true) {
					// waits to update progress bar if pokemon is going to evolve
				} else {
					updateProgressBar("player");
				}
			}

			// Updates sprite and hp bar if pokemon evolved
			if (battle.battleResponses(responseCounter).contains("evolved")) {
				playerPokeSprite.setImage(playerPokemon.getBackSprite());
				updateProgressBar("player");
			}

			responseCounter++;

			// resets responses, updates health bars, returns true
		} else {
			responseCounter = 0;
			updateProgressBar("player");
			updateProgressBar("opponent");
			battle.clearResponses();
			lastResponse = true;
		}

		return lastResponse;

	}

	public Color typeColor(Move move) {

		Color color = Color.BLACK;

		// Chooses color based on move's type

		switch (move.getType()) {

		case ("Fire"):

			color = Color.RED;
			break;

		case ("Grass"):

			color = Color.GREEN;
			break;

		case ("Water"):

			color = Color.DARKBLUE;
			break;

		case ("Fighting"):

			color = Color.ORANGE;
			break;

		case ("Ground"):

			color = Color.DARKGOLDENROD;
			break;

		case ("Rock"):

			color = Color.SADDLEBROWN;
			break;

		case ("Flying"):

			color = Color.SKYBLUE;
			break;

		case ("Bug"):

			color = Color.OLIVEDRAB;
			break;

		case ("Psychic"):

			color = Color.MAGENTA;
			break;

		case ("Normal"):

			color = Color.GREY;
			break;

		case ("Electric"):

			color = Color.YELLOW;
			break;

		case ("Dark"):

			color = Color.BLACK;
			break;

		case ("Dragon"):

			color = Color.MIDNIGHTBLUE;
			break;

		case ("Ice"):

			color = Color.AQUAMARINE;
			break;

		case ("Ghost"):

			color = Color.INDIGO;
			break;

		case ("Poison"):

			color = Color.BLUEVIOLET;
			break;

		case ("Steel"):

			color = Color.DIMGRAY;
			break;

		case ("Fairy"):

			color = Color.HOTPINK;
			break;
		default:
			break;
		}

		return color;
	}

	public void updateProgressBar(String bar) {
		switch (bar) {

		case "player":
			// Updates progress bar and progress bar labels for player
			playerHpBar.setProgress((double) playerPokemon.getCurrentHP() / playerPokemon.getTotalHP());
			lblPlayerBar.setText((playerPokemon.getName() + " L." + playerPokemon.getLevel()).toUpperCase());
			lblPlayerHp.setText(playerPokemon.getCurrentHP() + "/" + playerPokemon.getTotalHP() + "HP");
			break;
		case "opponent":

			// Updates progress bar and progress bar labels for opponent
			opponentHpBar.setProgress((double) opponentPokemon.getCurrentHP() / opponentPokemon.getTotalHP());
			lblOpponentBar.setText((opponentPokemon.getName() + " L." + opponentPokemon.getLevel()).toUpperCase());
			lblOpponentHp.setText(opponentPokemon.getCurrentHP() + "/" + opponentPokemon.getTotalHP() + "HP");
			break;
		}
	}

	public void gymBattle(Stage myStage) {

		// TODO Implement this gymLeader instantiation to the battle up in Case 'C' of
		// overworld

		Player gymLeader = new Player("Jack");

		// Puts levels of all team members in an array
		int[] teamLevels = new int[player.getTeamSize()];

		for (int i = 0; i < player.getTeamSize(); i++) {
			teamLevels[i] = player.getPokemon(i).getLevel();
		}

		// Sort in descending order
		Sort.selectionSort(teamLevels, 2);

		int highestLevel = teamLevels[0];

		for (int i = 0; i < 3; i++) {
			double randomEncounter = Math.random();
			String encounter;

			// 8.3% chance for every pokemon, randomizes the first 3 pokemon the gym leader
			// will have

			if (randomEncounter <= 0.083) {
				encounter = "Aron";
			} else if (randomEncounter > 0.083 && randomEncounter <= 0.166) {
				encounter = "Azurill";
			} else if (randomEncounter > 0.166 && randomEncounter <= 0.249) {
				encounter = "Beldum";
			} else if (randomEncounter > 0.249 && randomEncounter <= 0.332) {
				encounter = "Elekid";
			} else if (randomEncounter > 0.332 && randomEncounter <= 0.415) {
				encounter = "Gastly";
			} else if (randomEncounter > 0.415 && randomEncounter <= 0.498) {
				encounter = "Gible";
			} else if (randomEncounter > 0.498 && randomEncounter <= 0.581) {
				encounter = "Golett";
			} else if (randomEncounter > 0.581 && randomEncounter <= 0.664) {
				encounter = "Pawniard";
			} else if (randomEncounter > 0.664 && randomEncounter <= 0.747) {
				encounter = "Seedot";
			} else if (randomEncounter > 0.747 && randomEncounter <= 0.83) {
				encounter = "Shinx";
			} else if (randomEncounter > 0.83 && randomEncounter <= 0.913) {
				encounter = "Yanma";
			} else {
				encounter = "Starly";
			}

			// Sets pokemon

			gymLeader.addPokemon(new Pokemon(encounter, highestLevel - 1));
		}

		// Sets the last pokemon you'll have to fight as the starter that has an
		// advantage over yours
		switch (choice) {

		case "Torchic":
			gymLeader.addPokemon(new Pokemon("Totodile", highestLevel + 1));
			break;

		case "Totodile":
			gymLeader.addPokemon(new Pokemon("Turtwig", highestLevel + 1));
			break;

		case "Turtwig":
			gymLeader.addPokemon(new Pokemon("Torchic", highestLevel + 1));
			break;
		}
	}

	public void wildEncounter(Stage myStage) {

		// 10% chance of wild encounter happening
		if (Math.random() * 100 < 10) {

			// Puts levels of all team members in an array
			int[] teamLevels = new int[player.getTeamSize()];

			for (int i = 0; i < player.getTeamSize(); i++) {
				teamLevels[i] = player.getPokemon(i).getLevel();
			}

			// Sort in descending order
			Sort.selectionSort(teamLevels, 2);

			int highestLevel = teamLevels[0];

			double randomLevelChange = Math.random();

			// 40% chance of encounter being 5 or 4 levels down from your highest leveled
			// pokemon
			// 20% chance of encounter being 3 level down from your highest level pokemon

			if (randomLevelChange < 0.40) {
				highestLevel -= 3;
			} else if (randomLevelChange >= 0.40 && randomLevelChange <= 0.80) {
				highestLevel -= 2;
			} else {
				highestLevel -= 1;
			}

			double randomEncounter = Math.random();
			String encounter;

			// 8.3% chance for every pokemon

			if (randomEncounter <= 0.083) {
				encounter = "Aron";
			} else if (randomEncounter > 0.083 && randomEncounter <= 0.166) {
				encounter = "Azurill";
			} else if (randomEncounter > 0.166 && randomEncounter <= 0.249) {
				encounter = "Beldum";
			} else if (randomEncounter > 0.249 && randomEncounter <= 0.332) {
				encounter = "Elekid";
			} else if (randomEncounter > 0.332 && randomEncounter <= 0.415) {
				encounter = "Gastly";
			} else if (randomEncounter > 0.415 && randomEncounter <= 0.498) {
				encounter = "Gible";
			} else if (randomEncounter > 0.498 && randomEncounter <= 0.581) {
				encounter = "Golett";
			} else if (randomEncounter > 0.581 && randomEncounter <= 0.664) {
				encounter = "Pawniard";
			} else if (randomEncounter > 0.664 && randomEncounter <= 0.747) {
				encounter = "Seedot";
			} else if (randomEncounter > 0.747 && randomEncounter <= 0.83) {
				encounter = "Shinx";
			} else if (randomEncounter > 0.83 && randomEncounter <= 0.913) {
				encounter = "Yanma";
			} else {
				encounter = "Starly";
			}

			// Sets pokemon

			opponentPokemon = new Pokemon(encounter, highestLevel);

			Boolean firstNonFainted = false;

			// Finds first nonfainted pokemon to send into battle

			for (int i = 0; i < player.getTeamSize(); i++) {

				if (firstNonFainted == false) {
					if (player.getPokemon(i).getCurrentHP() != 0) {
						firstNonFainted = true;
						playerPokemon = player.getPokemon(i);
					}
				}

			}

			// Instantiates new battle, istrainer false

			battle = new Battle(playerPokemon, opponentPokemon, false);

			// Sets the sprites for the pokemon
			playerPokeSprite.setImage(playerPokemon.getBackSprite());
			opponentPokeSprite.setImage(opponentPokemon.getFrontSprite());

			// Updates progress bars

			updateProgressBar("player");
			updateProgressBar("opponent");

			// battlemenu to responses, calls next response

			responseCounter = 0;
			battleMenu = "battleResponses";
			nextBattleResponse();

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

		// Top & Bottom & Left & Right
		if ((botBarrier == true || topBarrier == true) && (leftBarrier == true || rightBarrier == true)) {
			if (direction.equals("Left") || direction.equals("Right")) {
				playerStackX++;
			}
			if (direction.equals("Right") || direction.equals("Left")) {
				playerStackX--;
			}
		}

		// Fixing loading bug with facing up or down and barriers
		if (loadedState == true && (direction.equals("Up") || direction.equals("Down"))
				&& (topBarrier != true && botBarrier != true) && (leftBarrier == true || rightBarrier == true)) {
			if (direction.equals("Up")) {
				playerStackY--;
			} else if (direction.equals("Down")) {
				playerStackY++;
			}
		}
		loadedState = false;

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