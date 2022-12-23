package branchesAreSoFun;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Boot extends Application {

	static final int TILESIZE = 10;

	TileGrid map = new TileGrid("routeOne");

	@Override
	public void start(Stage myStage) throws Exception {

		GridPane root = new GridPane();
		
		int rootRow =0;
		int rootCol =0;
		
		
		for (int row = map.getPlayerY() - map.getCameraHeight(); row < map.getPlayerY(); row++) {
			rootRow++;
			for (int col = map.getPlayerX()- map.getCameraWidth()/2 ; col < map.getPlayerX() + map.getCameraWidth()/2; col++) {
				rootCol++;
				root.add(map.getTile(row, col), rootRow, rootCol);
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
