package branchesAreSoFun;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView{

	//Tile textures
	private final Image imgDIRT = new Image(getClass().getResource("/images/DirtTile.png").toString());
	private final Image imgGRASS = new Image(getClass().getResource("/images/GrassTile.png").toString());
	private final Image imgTALLGRASS = new Image(getClass().getResource("/images/TallGrassTile.png").toString());
	private final Image imgTREE = new Image(getClass().getResource("/images/TreeTile.png").toString());
	
	//Size
	private final int DIMENSION = 32;
	
	public Tile() {
		
	}
	public Tile(String tileType) {
		
		super();
		
		this.setFitWidth(DIMENSION);
		this.setFitHeight(DIMENSION);
		
		//Sets texture 
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
