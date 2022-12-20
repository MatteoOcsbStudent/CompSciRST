package branchesAreSoFun;

import javafx.scene.image.Image;

public class Tile {

	private String texture;
	private String DIRT = "/images/DirtTile.png";
	private String GRASS = "/images/GrassTile.png";
	private String TALLGRASS = "/images/TallGrassTile.png";
	private String TREE = "/images/TreeTile.png";
	
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
	
	public String getTexture() {
		return texture;
	}
}
