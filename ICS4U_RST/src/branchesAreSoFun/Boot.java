package branchesAreSoFun;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Boot extends Application {

	// Declares window dimensions
	private final int CAMERAHEIGHT = 6;
	private final int CAMERAWIDTH = 10;
	private int playerStackX;
	private int playerStackY;

	StackPane playerStack = new StackPane();

	ImageView playerUp = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerUp.png").toString());
	ImageView playerLeft = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerLeft.png").toString());
	ImageView playerRight = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerRight.png").toString());
	ImageView playerDown = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerDown.png").toString());

	TileGrid map;

	@Override
	public void start(Stage myStage) throws Exception {

		// Temp hardcoded map loading
		map = new TileGrid("routeOne");
		playerStackX = 10;
		playerStackY = 11;

		// Declaring gridpane
		GridPane root = new GridPane();

		// call board display
		displayBoard(root);

		// Stack player sprite onto tile
		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), playerUp);
		root.add(playerStack, playerStackX, playerStackY);

		Scene scene = new Scene(root);

		myStage.setTitle("Test");
		myStage.setScene(scene);
		myStage.show();

		// Moving player WASD
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case W:
					if (map.getTile(map.getPlayerY() - 1, map.getPlayerX()).getTexture() != "Tree") {
						map.setPlayerY(-1);
						playerStackY--;
						displayBoard(root);
					}
					break;
				case A:
					if (map.getTile(map.getPlayerY(), map.getPlayerX() - 1).getTexture() != "Tree") {
						map.setPlayerX(-1);
						playerStackX--;
						displayBoard(root);

					}

					break;
				case S:
					if (map.getTile(map.getPlayerY() + 1, map.getPlayerX()).getTexture() != "Tree") {
						map.setPlayerY(1);
						playerStackY++;
						displayBoard(root);
					}
					break;
				case D:
					if (map.getTile(map.getPlayerY(), map.getPlayerX() + 1).getTexture() != "Tree") {
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

	public void displayBoard(GridPane root) {

		int displayUp = CAMERAHEIGHT;
		int displayDown = CAMERAHEIGHT;
		int displayLeft = CAMERAWIDTH;
		int displayRight = CAMERAWIDTH;

		// BOTTOM
		if (map.getPlayerY() + CAMERAHEIGHT > map.getMapHeight()) {
			displayDown = CAMERAHEIGHT - (map.getPlayerY() + CAMERAHEIGHT - map.getMapHeight());
			displayUp = CAMERAHEIGHT + (map.getPlayerY() + CAMERAHEIGHT - map.getMapHeight());
			

			// RIGHT SIDE
		}

		if (map.getPlayerX() + CAMERAWIDTH > map.getMapWidth()) {
			displayRight = CAMERAWIDTH + (map.getPlayerX() + CAMERAWIDTH - map.getMapWidth());
			displayLeft = CAMERAWIDTH - (map.getPlayerX() + CAMERAWIDTH - map.getMapWidth());

			// TOP
		}

		if (map.getPlayerY() - CAMERAHEIGHT < 0) {
			displayDown = CAMERAHEIGHT - (map.getPlayerY() - CAMERAHEIGHT);
			displayUp = CAMERAHEIGHT + (map.getPlayerY() - CAMERAHEIGHT);

			// LEFT SIDE
		}

		if (map.getPlayerX() - CAMERAWIDTH < 0) {
			displayRight = CAMERAWIDTH - (map.getPlayerX() - CAMERAWIDTH);
			displayLeft = CAMERAWIDTH + (map.getPlayerX() - CAMERAWIDTH);

		} else {
			playerStackX = 10;
			playerStackY = 6;
		}

		root.getChildren().clear();

		int rootRow = -1;
		int rootCol = -1;

		for (int row = map.getPlayerY() - displayUp; row < map.getPlayerY() + displayDown; row++) {
			rootRow++;
			rootCol = -1;
			for (int col = map.getPlayerX() - displayLeft; col <= map.getPlayerX() + displayRight; col++) {
				rootCol++;
				root.add(map.getTile(row, col), rootCol, rootRow);
			}
		}

		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), playerUp);
		root.add(playerStack, playerStackX, playerStackY);
	}

	public static void main(String[] args) {
		launch(args);

	}

}