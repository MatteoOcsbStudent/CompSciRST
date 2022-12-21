package branchesAreSoFun;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView{

	private final Image imgDIRT = new Image(getClass().getResource("/images/DirtTile.png").toString());
	private final Image imgGRASS = new Image(getClass().getResource("/images/GrassTile.png").toString());
	private final Image imgTALLGRASS = new Image(getClass().getResource("/images/TallGrassTile.png").toString());
	private final Image imgTREE = new Image(getClass().getResource("/images/TreeTile.png").toString());
	
	public Tile() {
		
	}
	public Tile(String tileType) {
		
		super();
		
		switch (tileType) {
		
		case "Dirt":
			this.setImage(imgDIRT);
			break;
			
		case "Grass":
			this.setImage(imgGRASS);
			break;
			
		case "TallGrass":
			this.setImage(imgTALLGRASS);
			break;
			
		case "Tree":
			this.setImage(imgTREE);
			break;
		}
	}
	
}
