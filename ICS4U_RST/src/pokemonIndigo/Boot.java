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

public class Boot extends Application {

	// Declares window dimensions
	private final int CAMERAHEIGHT = 6;
	private final int CAMERAWIDTH = 10;

	// size of pokemon sprites
	private final int POKESPRITEDIMENSION = 148;

	// Scene height and width
	private final int sceneHeight = 384;
	private final int sceneWidth = 640;

	// which battle button is being selected
	private int battleButtonIndex = 0;

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

	private int currentBattlePoke;

	// Locking movement
	private boolean movementLock = false;

	private boolean endBattle;

	// Direction of movement
	private String direction;

	// Tracks which battle menu is used
	private String battleMenu;

	// The map name used for transitions
	private String currentMapName;

	// Global reference for pokemon
	Pokemon playerPokemon;
	Pokemon opponentPokemon;

	// Player sprite stackpane
	StackPane playerStack = new StackPane();

	// Scenes
	Scene loading, scene, battleScene;

	// Battle images
	ImageView opponentPokeSprite, playerPokeSprite;
	BackgroundImage backgroundImage;

	// Directional sprites
	ImageView playerUp = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerUp.png").toString());
	ImageView playerLeft = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerLeft.png").toString());
	ImageView playerRight = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerRight.png").toString());
	ImageView playerDown = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerDown.png").toString());

	// Battle labels/buttons
	Label lblFightButton;
	Label lblPokemonButton;
	Label lblCatchButton;
	Label lblRunButton;
	Label lblBattleResponse;
	Label lblPlayerHp;
	Label lblPlayerBar;
	Label lblOpponentHp;
	Label lblOpponentBar;
	Label lblCToContinue;
	Label lblXToBack;

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

	public void save() {

		FileWriter saveFile;
		try {
			
			//Saved File
			saveFile = new FileWriter("data/saveFile");
			PrintWriter savePrinter = new PrintWriter(saveFile);

			//Saves the map's name
			savePrinter.println(currentMapName);

			//Saves the Spawnpoint
			savePrinter.println(map.getNextSpawn());
			
			//Saves player's direction
			savePrinter.println(direction);

			//Saves the X and Y of the player
			savePrinter.println(map.getPlayerY());
			savePrinter.println(map.getPlayerX());

			//Saves the Playerstack's X and Y
			savePrinter.println(playerStackX);
			savePrinter.println(playerStackY);

			//Saves the size of the player's team
			savePrinter.println(player.getTeamSize());
			
			//Saving Pokemon
			for (int i = 0; i < player.getTeamSize(); i++) {
				
				//Pokemon's Name
				savePrinter.println(player.getPokemon(i).getFirstEvo());
				
				//Pokemon's Level
				savePrinter.println(player.getPokemon(i).getLevel());
				
				//Pokemon's Current HP
				savePrinter.println(player.getPokemon(i).getCurrentHP());
				
				//Pokemon's Status (If afflicted)
				savePrinter.println(player.getPokemon(i).getStatus());
				
				//Size of Pokemon's move pool (1 to 4)
				savePrinter.println(player.getPokemon(i).getMovePoolSize());
				
				//Loops through all the moves, saving them
				for (int j = 0; j < player.getPokemon(i).getMovePoolSize(); j++) {
					
					savePrinter.println(player.getPokemon(i).getMove(j).getName());
				}
			}

			saveFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void load(Stage myStage) {

		try {

			FileReader loadFile = new FileReader("data/saveFile");
			BufferedReader loadStream = new BufferedReader(loadFile);

			//Reads map name
			currentMapName = loadStream.readLine();

			//Creates a new map based on the saved one
			map = new TileGrid(currentMapName, Integer.parseInt(loadStream.readLine()));
			
			direction = loadStream.readLine();

			//Sets the player's X and Y
			map.setPlayerY(Integer.parseInt(loadStream.readLine()));
			map.setPlayerX(Integer.parseInt(loadStream.readLine()));

			//Sets the playerStack's X and Y
			playerStackX = (Integer.parseInt(loadStream.readLine()));
			playerStackY = (Integer.parseInt(loadStream.readLine()));

			//Checks the player's team size
			int teamSize = Integer.parseInt(loadStream.readLine());
			
			//Loops through every pokemon based on the team's size
			for (int i = 0; i < teamSize; i++) {
				
				//Adds a new pokemon using the name saved
				player.addPokemon(new Pokemon(loadStream.readLine(), Integer.parseInt(loadStream.readLine())));
				
				//Sets the pokemon's current HP
				player.getPokemon(i).setCurrentHP(Integer.parseInt(loadStream.readLine()));
				
				//Sets any status it is afflicted with
				player.getPokemon(i).setStatus(loadStream.readLine());
				
				//Sets the movepool's size
				int movePoolSize = Integer.parseInt(loadStream.readLine());
				
				for (int j = 0; j < movePoolSize; j++) {
					
					//Adds all the moves
					player.getPokemon(i).changeMoveSet(new Move(loadStream.readLine()), j);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage myStage) throws Exception {

		player.addPokemon(new Pokemon("Torchic", 15));
		
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

		//Loading screen text
		lblLoadingScreen = new Label();
		lblLoadingScreen.setTextFill(Color.WHITE);
		lblLoadingScreen.setFont(Font.font(LOADINGFONT));
		loadingPane.getChildren().addAll(new Rectangle(sceneWidth, sceneHeight, Color.BLACK), lblLoadingScreen);

		//Loading screen 
		loading = new Scene(loadingPane, sceneWidth, sceneHeight);

		/**
		 * Battle Scene
		 */

		//Battle UI gridpane and scene
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

		//label for player pokemons name and level
		lblPlayerBar = new Label();
		lblPlayerBar.setTextFill(Color.BLACK);
		lblPlayerBar.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblPlayerBar, 4, 7, 1, 1);
		lblPlayerBar.setAlignment(Pos.BOTTOM_CENTER);

		//label for player pokemons hp
		lblPlayerHp = new Label();
		lblPlayerHp.setTextFill(Color.BLACK);
		lblPlayerHp.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblPlayerHp, 4, 9, 1, 1);
		lblPlayerHp.setAlignment(Pos.TOP_CENTER);

		// xpBar
		xpBar = new ProgressBar();

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

		//Label for opponent pokemons name and level
		lblOpponentBar = new Label();
		lblOpponentBar.setTextFill(Color.BLACK);
		lblOpponentBar.setFont(Font.font(SMALL_FONT));
		battleRoot.add(lblOpponentBar, 2, 2, 1, 1);
		lblOpponentBar.setAlignment(Pos.BOTTOM_CENTER);

		//Label for opponent pokemons hp
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
		 * Main Menu Scene
		 */

		GridPane mainMenu = new GridPane();
		Scene mainMenuScene = new Scene(mainMenu, sceneWidth, sceneHeight);
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

		Button btnNewGame = new Button("New Game");
		btnNewGame.setMinWidth(100);
		btnNewGame.setOnAction(event -> myStage.setScene(scene));
		mainMenu.add(btnNewGame, 54, 35);

		Button btnLoad = new Button("Load");
		btnLoad.setMinWidth(100);
		btnLoad.setOnAction(event -> load(myStage));
		mainMenu.add(btnLoad, 54, 40);

		Button btnExit = new Button("Exit");
		btnExit.setMinWidth(100);
		btnExit.setOnAction(event -> System.exit(0));
		mainMenu.add(btnExit, 54, 45);

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

					case V:

						save();
						Console.print("Game Saved");

						break;
					default:
						break;

					}
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

							//Only scrolls through as many buttons as there are moves
						} else if (battleMenu.equals("Moves") || battleMenu.equals("MovesToReplace")) {
              
							if (battleButtonIndex == 1 || battleButtonIndex == 0) {
								battleButtonIndex = playerPokemon.getMovePoolSize();
								buttonUpdate();
							} else {
								battleButtonIndex--;
								buttonUpdate();
							}

							//Only scrolls through two options, yes and no
						} else if (battleMenu.equals("MoveLearning")) {
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
						} else if (battleMenu.equals("MoveLearning")) {
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

						//Start of battle
						if (lblBattleResponse.getText().contains("appeared!")) {
							nextBattleMenu(myStage, "General");
							movementLock = false;

						//Do nothing if got away safely
						} else if (lblBattleResponse.getText().equals("You got away safely!")) {

						//Do nothing if caught pokemon
						} else if (lblBattleResponse.getText().contains("caught!")) {

						//Shows yes and no options if prompted to learn a new move
						} else if (lblBattleResponse.getText().equals("Would you like to forget an old move?")) {

							nextBattleMenu(myStage, "MoveLearning");
						
						//shows moves to replace
						} else if (lblBattleResponse.getText().equals("Please select a move to replace")) {

							nextBattleMenu(myStage, "MovesToReplace");
						
						//Searches for available pokemon to send out
						} else if (lblBattleResponse.getText().equals(playerPokemon.getName() + " has fainted")){
							
							int nonFaintedPokemon = player.getTeamSize();
							for (int i = 0; i < player.getTeamSize(); i++) {
								if (player.getPokemon(i).getCurrentHP() == 0) {
									nonFaintedPokemon--;
								}
							}
							
							//If available, prompts user to select a new pokemon
							if (nonFaintedPokemon > 0) {
								lblBattleResponse.setText("Please choose another pokemon to send out");
							
							//Otherwise, informs user they have no other pokemon
							} else {
								lblBattleResponse.setText("You have no other Pokemon able to fight...");
							}
						
						//If no other pokemon, tell user they blacked out, end battle
						} else if (lblBattleResponse.getText().equals("You have no other Pokemon able to fight...")) {
							lblBattleResponse.setText("... you blacked out!");
							endBattle = true;
							
						//If blacked out, sends back to pokemon center and heals all pokemon
						} else if (lblBattleResponse.getText().equals("... you blacked out")) {
							//TODO - black out logic, sends back to pokemon center, heals all pokemon.
						
						//If no more responses
						} else if (nextBattleResponse() == true) {

							//If battle is to be ended, sets scene to main scene. resets endbattle
							if (endBattle == true) {
								myStage.setScene(scene);
								endBattle = false;
							
							//Otherwise, goes back to main battle menu
							} else {
								nextBattleMenu(myStage, "General");
							}
						}

						break;

					//Move learning menu
					case "MoveLearning":

						//Yes button, prompts for move to replace
						if (battleButtonIndex == 2) {
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse.setText("Please select a move to replace");
						
						//No button, informs user pokemon did not learn the move
						} else if (battleButtonIndex == 3) {
							nextBattleMenu(myStage, "battleResponses");
							lblBattleResponse.setText(
									playerPokemon.getName() + " did not learn " + playerPokemon.getNextMoveLearn());
						}

						break;

					case "MovesToReplace":

						//Moves to be replaced with a new move
						//Informs user of their selection, changes pokemons moveset respective to selection
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

		myStage.setTitle("Pokemon Indigo");
		myStage.setScene(mainMenuScene);
		myStage.show();

	}

	public void nextMap(Stage myStage) {

		// locks movement
		movementLock = true;

		// Checks to see if the player is on an exit tile
		map.checkExit(currentMapName);

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

			// removes the movement lock
			movementLock = false;
		}));

		delay.play();

		// removes the movement lock
		movementLock = false;
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
			lblFightButton.setText(player.getPokemon(currentBattlePoke).getMove(0).getName());
			lblFightButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(0)));

			//Displays all moves available to be displayed
			if (playerPokemon.getMovePoolSize() > 1) {
				lblPokemonButton.setText(player.getPokemon(currentBattlePoke).getMove(1).getName());
				lblPokemonButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(1)));
			} else {
				lblPokemonButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 2) {
				lblCatchButton.setText(player.getPokemon(currentBattlePoke).getMove(2).getName());
				lblCatchButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(2)));
			} else {
				lblCatchButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 3) {
				lblRunButton.setText(player.getPokemon(currentBattlePoke).getMove(3).getName());
				lblRunButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(3)));
			} else {
				lblRunButton.setText("");
			}

			break;

		case "PokemonMenu":

			break;

		case "Catch":

			// Catching pokemon
			battleMenu = "battleResponses";
			nextBattleMenu(myStage, "battleResponses");

			// displays response
			lblBattleResponse.setText(battle.catchPokemon());

			//Delay if needed when caught pokemon
			Timeline delayCatch = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
				myStage.setScene(scene);
			}));

			// If caught, adds to player array, back to main scene
			if (lblBattleResponse.getText().contains("has been caught!")) {
				player.addPokemon(opponentPokemon);
				delayCatch.play();

				// if trainer battle, opponent does not take turn on catch attempt
				// informs user you cannot catch another trainers pokemon
			} else if (lblBattleResponse.getText()
					.equals("You can't catch another trainer's Pokemon! are you crazy?")) {

				// otherwise, wild pokemon takes turn
			} else {
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
				movementLock = true;

				Timeline delayRun = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
					myStage.setScene(scene);
					movementLock = false;
				}));

				delayRun.play();

				// Otherwise, battle continues, opponent takes turn
			} else {
				lblBattleResponse.setText("You couldn't get away!");
				battle.turnExecution("Opponent only");
			}

			break;

		case "MoveLearning":

			//sets battlemenu
			battleMenu = "MoveLearning";

			//Sets button text and visibility
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
			lblFightButton.setText(player.getPokemon(currentBattlePoke).getMove(0).getName());
			lblFightButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(0)));

			if (playerPokemon.getMovePoolSize() > 1) {
				lblPokemonButton.setText(player.getPokemon(currentBattlePoke).getMove(1).getName());
				lblPokemonButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(1)));
			} else {
				lblPokemonButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 2) {
				lblCatchButton.setText(player.getPokemon(currentBattlePoke).getMove(2).getName());
				lblCatchButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(2)));
			} else {
				lblCatchButton.setText("");
			}

			if (playerPokemon.getMovePoolSize() > 3) {
				lblRunButton.setText(player.getPokemon(currentBattlePoke).getMove(3).getName());
				lblRunButton.setTextFill(typeColor(player.getPokemon(currentBattlePoke).getMove(3)));
			} else {
				lblRunButton.setText("");
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
		
		//Calls battle response, sets battle menu to responses
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
						
						//If wild encounter, endbattle
						if (battle.isTrainerBattle == false) {
							endBattle = true;
						}
					} else if (battle.isPlayerFainted() == true){
							updateProgressBar("player");
					}
			}
			
			//Updates progress bar if pokemon levels up
			if (battle.battleResponses(responseCounter).contains("leveled up")) {
				if (battle.isGoingToEvolve() == true) {
					//waits to update progress bar if pokemon is going to evolve
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
		case "xp":

			break;
		}
	}

	public void wildEncounter(Stage myStage) {

		// 10% chance of wild encounter happening
		if (Math.random() * 100 < 10) {

			// Sets pokemon

			opponentPokemon = new Pokemon("Torchic", 13);

			playerPokemon = player.getPokemon(0);

			//Instantiates new battle, not trainer 
			battle = new Battle(playerPokemon, opponentPokemon, false);

			// Sets the sprites for the pokemon
			playerPokeSprite.setImage(playerPokemon.getBackSprite());
			opponentPokeSprite.setImage(opponentPokemon.getFrontSprite());

			//Updates progress bar
			updateProgressBar("player");
			updateProgressBar("opponent");

			//battlemenu to responses, calls next response
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