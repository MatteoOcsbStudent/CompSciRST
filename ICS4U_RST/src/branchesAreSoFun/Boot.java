package branchesAreSoFun;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Boot extends Application {

	static final int TILESIZE = 10;
	private final int CAMERAHEIGHT = 13;
	private final int CAMERAWIDTH = 21; 
	TileGrid map = new TileGrid("routeOne");

	@Override
	public void start(Stage myStage) throws Exception {

		GridPane root = new GridPane();
		
		int rootRow =-1;
		int rootCol =-1;
		
		
//		for(int row = 0; row < 20; row ++) {
//		for (int col = 0; col < 21; col++) {
//			root.add(map.getTile(row, col), row, col);
//			}
//		}
		
		
		//CHECK (map.getCameraHeight() - 1), it works, but isn't clean
		for (int row = map.getPlayerY() - (CAMERAHEIGHT - 1); row <= map.getPlayerY(); row++) {
			rootRow++;
			rootCol= - 1;
			for (int col = map.getPlayerX()- (CAMERAWIDTH/2) ; col <= map.getPlayerX() + (CAMERAWIDTH/2); col++) {
				rootCol++;
				root.add(map.getTile(row, col), rootCol, rootRow);
			}
		}

		Scene scene = new Scene(root);

		myStage.setTitle("Test");
		myStage.setScene(scene);
		myStage.show();
		
		

	}

	public static void main(String[] args) {
		launch(args);

	}

}
