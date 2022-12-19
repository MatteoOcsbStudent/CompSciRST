package branchesAreSoFun;

import javafx.scene.image.Image;

public class Tile {

	private Image texture;
	private Image DIRT;
	private Image GRASS;
	private Image TALLGRASS;
	private Image TREE;
	
	public Tile(String tileType) {
		switch (tileType) {
		
		case "Dirt":
			texture = DIRT;
			break;
			
		case "Grass":
			texture = GRASS;
			break;
			
		case "TallGrass":
			texture = TALLGRASS;
			break;
			
		case "Tree":
			texture = TREE;
			break;
		}
	}
	
	public Image getTexture() {
		return texture;
	}
}
