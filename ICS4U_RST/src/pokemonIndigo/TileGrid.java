package pokemonIndigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.image.Image;
import simpleIO.Console;

public class TileGrid {

	// Double array of Tiles
	private Tile[][] map;

	// Dimensions of loaded map
	private int mapWidth;
	private int mapHeight;

	// Player index location
	private int playerX;
	private int playerY;

	// The current name of the map saved on the side
	String currentMapName;
	String nextMap;

	// spawnpoint number associated with location of player
	private int spawnpoint;

	// Playerstack's spawn
	private int playerSpawnX;
	private int playerSpawnY;

	// Exit tile boolean
	private boolean exit = false;

	// Background Images
	private Image backgroundImageForest = new Image("images/BattleBackgrounds/Forest.png", 640, 348, false, false);
	private Image backgroundImageGym = new Image("images/BattleBackgrounds/Gym.png", 640, 348, false, false);
	private Image backgroundImage;

	public TileGrid() {
	}

	public TileGrid(String mapName, int newSpawnpoint) {

		File tileLayout = null;

		spawnpoint = newSpawnpoint;
		
		// Declares file and spawnpoint based on chosen map
		switch (mapName) {

		case "Route One":

			// Calls the map file
			tileLayout = new File("data/maps/routeOne.map");

			// Sets Background image
			backgroundImage = backgroundImageForest;

			// Sets the spawnpoint
			if (spawnpoint == 1) {
				playerX = 14;
				playerY = 50;
			} else if (spawnpoint == 2) {
				playerX = 24;
				playerY = 0;
			}

			// Names the map accordingly
			currentMapName = "Route One";
			break;

		case "Orilon Town":
			tileLayout = new File("data/maps/orilonTown.map");
			backgroundImage = backgroundImageForest;
			if (spawnpoint == 1) {
				playerX = 10;
				playerY = 10;
			} else if (spawnpoint == 2) {
				playerX = 12;
				playerY = 0;
			} else if (spawnpoint == 3) {
				playerX = 21;
				playerY = 7;
			}
			currentMapName = "Orilon Town";
			break;

		case "Horizon City":
			tileLayout = new File("data/maps/horizonCity.map");
			backgroundImage = backgroundImageForest;
			if (spawnpoint == 1) {
				playerX = 24;
				playerY = 21;
			} else if (spawnpoint == 2) {
				playerX = 5;
				playerY = 13;
			} else if (spawnpoint == 3) {
				playerX = 24;
				playerY = 8;
			}
			currentMapName = "Horizon City";
			break;
			
		case "Pokemon Center":
			tileLayout = new File("data/maps/pokemonCenter.map");
			playerX = 10;
			playerY = 12;
			currentMapName = "Pokemon Center";
			break;
		
		case "Gym":
			tileLayout = new File("data/maps/gym.map");
			playerX = 11;
			playerY = 14;
		}

		// Reads map textfile
		try {
			FileReader mapReader = new FileReader(tileLayout);
			BufferedReader mapStream = new BufferedReader(mapReader);

			// Reads dimensions
			mapWidth = Integer.valueOf(mapStream.readLine());
			mapHeight = Integer.valueOf(mapStream.readLine());

			// Declares map double array dimensions
			map = new Tile[mapHeight][mapWidth];

			// Reads a row
			for (int y = 0; y < mapHeight; y++) {
				String temp = mapStream.readLine();

				// Reads each character in the row, declaring corresponding tile at the index
				for (int x = 0; x < temp.length(); x++) {
					char tileType = temp.charAt(x);

					switch (tileType) {

					// If its a null tile
					case '-':
						break;

					// Tree Tile
					case 'T':
						map[y][x] = new Tile("Tree");
						break;

					// Dirt Tile
					case 'D':
						map[y][x] = new Tile("Dirt");
						break;

					// TallGrass Tile
					case 'W':
						map[y][x] = new Tile("TallGrass");

						break;

					// Normal Grass Tile
					case 'G':
						map[y][x] = new Tile("Grass");
						break;

					// Floor
					case 'F':
						map[y][x] = new Tile("InsideFloor");
						break;

					case 'L':
						map[y][x] = new Tile("WallLeft");
						break;

					case 'R':
						map[y][x] = new Tile("WallRight");
						break;

					case 'H':
						map[y][x] = new Tile("WallBack");
						break;
					
					case 'U':
						map[y][x] = new Tile("BlackTile");
						break;

					// Pokemon Center
					case 'P':

						// 2 loops for the 2 rows it spans
						for (int position = 0; position < 3; position++) {

							// Sets the specific tile image to the specific tile it should be on
							map[y][x] = new Tile("PokemonCenter", (position + 1));

							// Adds one to X manually to let the image keep being loaded over the null tiles
							// (check text files)
							x++;
						}

						// Resets X for the next row
						x -= 3;

						for (int position = 3; position < 6; position++) {
							map[y + 1][x] = new Tile("PokemonCenter", (position + 1));
							x++;
						}
						x--;
						break;

					// Small Red House
					case 'S':

						for (int position = 0; position < 3; position++) {
							map[y][x] = new Tile("HouseSmallRed", (position + 1));
							x++;
						}
						x -= 3;

						for (int position = 3; position < 6; position++) {
							map[y + 1][x] = new Tile("HouseSmallRed", (position + 1));
							x++;
						}
						x -= 3;

						for (int position = 6; position < 9; position++) {
							map[y + 2][x] = new Tile("HouseSmallRed", (position + 1));
							x++;
						}
						x--;
						break;

					// Big Red House
					case 'B':

						for (int rows = 0; rows < 3; rows++) {
							for (int position = 0; position < 4; position++) {
								map[y][x] = new Tile("HouseBigRed", (position + 1));
								x++;
							}
							x -= 4;
						}

						for (int position = 4; position < 8; position++) {
							map[y + 1][x] = new Tile("HouseBigRed", (position + 1));
							x++;
						}
						x -= 4;

						for (int position = 8; position < 12; position++) {
							map[y + 2][x] = new Tile("HouseBigRed", (position + 1));
							x++;
						}
						x--;
						break;

					case 'Y':
						for (int position = 0; position < 5; position++) {
							map[y][x] = new Tile("Gym", (position + 1));
							x++;
						}
						x -= 5;

						for (int position = 5; position < 10; position++) {
							map[y + 1][x] = new Tile("Gym", (position + 1));
							x++;
						}
						x -= 5;

						for (int position = 10; position < 15; position++) {
							map[y + 2][x] = new Tile("Gym", (position + 1));
							x++;
						}
						x -= 5;

						for (int position = 15; position < 20; position++) {
							map[y + 3][x] = new Tile("Gym", (position + 1));
							x++;
						}
						x -= 5;

						for (int position = 20; position < 25; position++) {
							map[y + 4][x] = new Tile("Gym", (position + 1));
							x++;
						}
						x--;
						break;

					case 'A':
						for (int position = 0; position < 2; position++) {
							map[y][x] = new Tile("Table", (position + 1));
							x++;
						}
						x -= 2;
						for (int position = 2; position < 4; position++) {
							map[y + 1][x] = new Tile("Table", (position + 1));
							x++;
						}
						x--;
						break;

					case 'M':
						for (int position = 0; position < 2; position++) {
							map[y][x] = new Tile("Doormat", (position + 1));
							x++;
						}
						x--;
						break;

					}

				}
			}

			mapStream.close();

		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Tile getTile(int row, int col) {
		return map[row][col];
	}

	public int getPlayerX() {
		return playerX;
	}

	public int getPlayerY() {
		return playerY;
	}

	public int getPlayerSpawnX() {
		return playerSpawnX;
	}

	public int getPlayerSpawnY() {
		return playerSpawnY;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public void setPlayerX(int movement) {
		playerX += movement;
	}

	public void setPlayerY(int movement) {
		playerY += movement;
	}

	// Checks for an exit tile
	public boolean checkExit(String mapName) {

		switch (mapName) {

		case "Orilon Town":

			// Checks for the hardcoded values for exit tiles
			if ((playerX >= 10 && playerX <= 15) && (playerY == 0)) {

				// Sets the exit tile as true
				exit = true;

				// Sets the next map's name
				nextMap = "Route One";

				// Sets the new player spawn
				playerSpawnX = 10;
				playerSpawnY = 11;

				// Sets the specific spawn entrance
				spawnpoint = 1;

			} else if (playerY == 7) {
				exit = true;
				
				nextMap = "Pokemon Center";
				
				playerSpawnX = 10;
				playerSpawnY = 11;
				
				spawnpoint = 1;
			
		} else {
				exit = false;
			}
			break;

		case "Route One":
		
			if ((playerX >= 9 && playerX <= 19) && (playerY == 50)) {
				
				exit = true;
				
				nextMap = "Orilon Town";
				
				playerSpawnX = 10;
				playerSpawnY = 0;
				
				spawnpoint = 2;
				
			} else if ((playerX >= 22 && playerX <= 25) && playerY == 0) {
				
				exit = true;
				
				nextMap = "Horizon City";
				
				playerSpawnX = 10;
				playerSpawnY = 11;
				
				spawnpoint = 1;
			
			} else {
				
				exit = false;
			
			}
			break;

		case "Horizon City":
			
			if ((playerX >= 22 && playerX <= 25) && playerY == 21) {
				
				exit = true;
				
				nextMap = "Route One";
				
				playerSpawnX = 10;
				playerSpawnY = 0;
				
				spawnpoint = 2;
			} else if (playerY == 13) {
				exit = true;
				
				nextMap = "Pokemon Center";
				
				playerSpawnY = 11;
				playerSpawnX = 10;
				
				spawnpoint = 2;
			} else if (playerY == 8) {
				exit = true;
				
				nextMap = "Gym";
				
				playerSpawnY = 11;
				playerSpawnX = 10;
			} else {
				exit = false;
			}
			break;

		case "Pokemon Center":
			if ((playerX == 9 || playerX == 10) && playerY == 12) {
				
				exit = true;
				
				if (spawnpoint == 1) {
					
					nextMap = "Orilon Town";
					
					playerSpawnX = 13;					
					playerSpawnY = 7;
					
					spawnpoint = 3;

				} else if (spawnpoint == 2) {
					nextMap = "Horizon City";
					
					playerSpawnX = 5;
					playerSpawnY = 7;
					
					spawnpoint = 2;
				
				} else {
					
					exit = false;
				
				}
				break;
			}
			
		case "Gym":
			if ((playerX == 10 || playerX == 11) && playerY == 14) {
			
				nextMap = "Horizon City";
				
				playerSpawnX = 10;
				playerSpawnY = 7;
				
				spawnpoint = 3;
			} else {
				exit = false;
			}
			
			break;
		}
		return exit;
	}

	public String getNextMap() {
		currentMapName = nextMap;
		return nextMap;
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public int getNextSpawn() {
		return spawnpoint;
	}

	public String getName() {
		return currentMapName;
	}
}