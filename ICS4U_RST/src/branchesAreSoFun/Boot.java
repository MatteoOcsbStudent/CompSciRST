package branchesAreSoFun;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Boot extends Application{
	
		static final int GAP = 15;
		static final int LARGE_FONT = 40;
		static final int MEDIUM_FONT = 20;
		static final int SMALL_FONT = 12;

		private Label lblResponse;
		private ImageView imgBuddy;

		@Override
		public void start(Stage myStage) throws Exception {

			VBox vbox = new VBox();
			HBox hbox = new HBox();
			vbox.getChildren().add(hbox);
			
			
			for (int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					new ImageView(.getTexture(i , j))
				}
			}
	
			Scene scene = new Scene(vbox);

			myStage.setTitle("Test");
			myStage.setScene(scene);
			myStage.show();

	}
		
	public static void main(String[] args) {
			launch(args);

	}



}
		

