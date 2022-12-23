package branchesAreSoFun;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Boot extends Application {

	//Declares window dimensions
	private final int CAMERAHEIGHT = 13;
	private final int CAMERAWIDTH = 21; 
	
	//Temp hardcoded map loading
	TileGrid map = new TileGrid("routeOne");

	@Override
	public void start(Stage myStage) throws Exception {

		//Declaring gridpane
		GridPane root = new GridPane();
		StackPane playerStack = new StackPane();
		ImageView player = new ImageView(getClass().getResource("/images/TrainerSprites/PlayerUp.png").toString());
		int rootRow =-1;
		int rootCol =-1;
		
		//CHECK CameraHeight() - 1, it works, but isn't clean
		for (int row = map.getPlayerY() - (CAMERAHEIGHT - 1); row <= map.getPlayerY(); row++) {
			rootRow++;
			rootCol= - 1;
			for (int col = map.getPlayerX()- (CAMERAWIDTH/2) ; col <= map.getPlayerX() + (CAMERAWIDTH/2); col++) {
				rootCol++;
				root.add(map.getTile(row, col), rootCol, rootRow);
			}
		}
		playerStack = new StackPane(map.getTile(map.getPlayerY(), map.getPlayerX()), player);
		root.add(playerStack, 10, 12);
		
		Scene scene = new Scene(root);

		myStage.setTitle("Test");
		myStage.setScene(scene);
		myStage.show();
		
		

	}

	public static void main(String[] args) {
		launch(args);

	}

}
