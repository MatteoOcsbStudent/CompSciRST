package pokemonIndigo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView {

	// Tile textures
	private final Image imgDIRT = new Image(getClass().getResource("/images/DirtTile.png").toString());
	private final Image imgGRASS = new Image(getClass().getResource("/images/GrassTile.png").toString());
	private final Image imgTALLGRASS = new Image(getClass().getResource("/images/TallGrassTile.png").toString());
	private final Image imgTREE = new Image(getClass().getResource("/images/TreeTile.png").toString());
	private final Image imgSTOOLRED = new Image(getClass().getResource("/images/PokemonCenterInside/StoolRed.png").toString());
	private final Image imgINSIDEFLOOR = new Image(getClass().getResource("/images/PokemonCenterInside/PInsideFloor.png").toString());
	private final Image imgINSIDEFLOORRED = new Image(getClass().getResource("/images/PokemonCenterInside/PInsideFloor.png").toString());
	private final Image imgINSIDEFLOORWHITE = new Image(getClass().getResource("/images/PokemonCenterInside/PInsideFloor.png").toString());
	private final Image imgWALLRIGHT = new Image(getClass().getResource("/images/PokemonCenterInside/WallRight.png").toString());
	private final Image imgWALLLEFT = new Image(getClass().getResource("/images/PokemonCenterInside/WallLeft.png").toString());
	private final Image imgWALLBACK = new Image(getClass().getResource("/images/PokemonCenterInside/WallBack.png").toString());
	
	//Barrier boolean
	private boolean barrier = false;
	
	private boolean wildEncounter = false;
	
	// Size
	private final int DIMENSION = 32;

	public Tile() {

	}

	public Tile(String tileType) {

		super();

		this.setFitWidth(DIMENSION);
		this.setFitHeight(DIMENSION);

		// Sets texture
		switch (tileType) {

		case "Dirt":
			this.setImage(imgDIRT);
			break;

		case "Grass":
			this.setImage(imgGRASS);
			break;

		case "TallGrass":
			this.setImage(imgTALLGRASS);
			wildEncounter = true;
			break;

		case "Tree":
			this.setImage(imgTREE);
			barrier = true;
			break;
		
		case "StoolRed":
			this.setImage(imgSTOOLRED);
			break;
			
		case "InsideFloor":
			this.setImage(imgINSIDEFLOOR);
			break;
			
		case "InsideFloorRed":
			this.setImage(imgINSIDEFLOORRED);
			break;
		
		case "InsideFloorWhite":
			this.setImage(imgINSIDEFLOORWHITE);
			break;
		}
	}

	public Tile(String tileType, int position) {

		super();

		this.setFitWidth(DIMENSION);
		this.setFitHeight(DIMENSION);

		switch (tileType) {

		case "PokemonCenter":

			// Modular image setting so that it can be called multiple times
			this.setImage(new Image(
					getClass().getResource("/images/PokemonCenter/PokemonCenter" + position + ".png").toString()));

			// Setting the building as a barrier other than the door
			if (position != 5) {
				barrier = true;
			}
			break;

		case "HouseSmallRed":
			this.setImage(new Image(
					getClass().getResource("/images/HouseSmallRed/HouseSmallRed" + position + ".png").toString()));
			if (position == 1 ||position == 2 ||position == 4 ||position == 5) {
				barrier = true;
			}
			break;
			
		case "HouseBigRed":
			this.setImage(new Image(
					getClass().getResource("/images/HouseBigRed/HouseBigRed" + position + ".png").toString()));
			if (position != 4 && position < 8) {
				barrier = true;
			}
			break;
			
		case "Gym":
			this.setImage(new Image(
					getClass().getResource("/images/Gym/Gym" + position + ".png").toString()));
			if (position >= 0 && position <= 20) {
				barrier = true;
			}
			break;
		case "Doormat":
			this.setImage(new Image(getClass().getResource("/images/PokemonCenterInside/Doormat" + position + ".png").toString()));;
			break;
			
		case "Table":
			this.setImage(new Image(getClass().getResource("/images/PokemonCenterInside/Table" + position + ".png").toString()));;
			barrier = true;
			break;
		
		case "WallRight":
			this.setImage(imgWALLRIGHT);
			break;
			
		case "WallLeft":
			this.setImage(imgWALLLEFT);
			break;
			
		case "WallBack":
			this.setImage(imgWALLBACK);
			break;
		}
	}

	//Checks for barrier
	public Boolean checkBarrier() {
		return barrier;
	}
	
	public Boolean checkEncounter() {
		return wildEncounter;
	}

}
