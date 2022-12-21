package branchesAreSoFun;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Boot extends Application{
	
		static final int TILESIZE = 10;

		TileGrid map = new TileGrid("test");
		
		@Override
		public void start(Stage myStage) throws Exception {

			GridPane root = new GridPane();
			
			for (int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					root.add(map.getTile(i, j), i, j);
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
		

