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
	private final Image imgBLACKTILE = new Image(getClass().getResource("/images/PokemonCenterInside/BlackTile.png").toString());
	private final Image imgNURSEJOY = new Image(getClass().getResource("/images/OtherCharacters/NurseJoy.png").toString());
	private final Image imgGYMLEADER = new Image(getClass().getResource("/images/OtherCharacters/GymLeaderJack.png").toString());
	
	//Barrier boolean, the player shouldnt be allowed to go through these tiles
	private boolean barrier = false;
	
	//door Boolean so that it can run a special exit clause
	private boolean door = false;
	
	//tiles that prompt a wild encounter
	private boolean wildEncounter = false;
	
	//tiles that when interacted with, heal the team
	private boolean heal = false;
	
	//tiles that when interacted with, start a battle
	private boolean battle = false;
	
	// Size
	private final int DIMENSION = 32;

	public Tile() {

	}

	/**Overloaded Constructor
	 * sets the image file for a specific tile, depending on the name given
	 * @param tileType
	 */
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
		case "WallRight":
			this.setImage(imgWALLRIGHT);
			barrier = true;
			break;
			
		case "WallLeft":
			this.setImage(imgWALLLEFT);
			barrier = true;
			break;
			
		case "WallBack":
			this.setImage(imgWALLBACK);
			barrier = true;
			break;
		
		case "BlackTile":
			this.setImage(imgBLACKTILE);
			break;
		
		case "NurseJoy":
			this.setImage(imgNURSEJOY);
			barrier = true;
			heal = true;
			break;
			
		case "GymLeader":
			this.setImage(imgGYMLEADER);
			barrier = true;
			battle = true;
		}
	}

	/**Overloaded Constructor
	 * similar to the constructor above, this is for looped tiles that take more than one tile of space, like a building
	 * requires position to loop the tile and call multiple different files
	 * @param tileType
	 * @param position
	 */
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
			} else if (position == 5) {
				door = true;
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
			} else if (position == 23) {
				door = true;
			}
			break;
		case "Doormat":
			this.setImage(new Image(getClass().getResource("/images/PokemonCenterInside/Doormat" + position + ".png").toString()));;
			break;
			
		case "Table":
			this.setImage(new Image(getClass().getResource("/images/PokemonCenterInside/Table" + position + ".png").toString()));;
			barrier = true;
			break;
		}
	}

	/**Accessor Method
	 * returns boolean stating whether or not the you can move onto the tile
	 * @return barrier
	 */
	public Boolean checkBarrier() {
		return barrier;
	}
	
	/**Accessor Method
	 * returns boolean stating whether or not the tile counts as an exit
	 * @return door
	 */
	public Boolean checkDoor() {
		return door;
	}
	
	/**Accessor Method
	 * returns a boolean stating whether or not the tile triggers an encounter
	 * @return wildEncounter
	 */
	public Boolean checkEncounter() {
		return wildEncounter;
	}
	
	/**Accessor Method
	 * returns a boolean stating whether or not the tile heals the player's team
	 * @return heal
	 */
	public Boolean checkHeal() {
		return heal;
	}
	
	/**Accessor Method
	 * returns a boolean stating whether or not the tile triggers a trainer battle
	 * @return battle
	 */
	public Boolean checkBattle() {
		return battle;
	}

}

