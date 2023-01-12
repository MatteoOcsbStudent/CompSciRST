package pokemonIndigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

	private int playerSpawnX;
	private int playerSpawnY;
	
	private boolean exit = false;

	public TileGrid() {

	}

	public TileGrid(String mapName, int spawnpoint) {

		File tileLayout = null;

		// Declares file and spawnpoint based on chosen map
		switch (mapName) {

		case "Route One":
			tileLayout = new File("data/maps/routeOne.map");
			if (spawnpoint == 1) {
				playerX = 14;
				playerY = 50;
			} else if (spawnpoint == 2) {
				playerX = 24;
				playerY = 0;
			}
			currentMapName = "Route One";
			break;

		case "Orilon Town":
			tileLayout = new File("data/maps/orilonTown.map");
			if (spawnpoint == 1) {
				playerX = 10;
				playerY = 10;	
			} else if (spawnpoint == 2) {	
				playerX = 12;
				playerY = 0;
			}
			currentMapName = "Orilon Town";
			break;
			
		case "routeTwo":
			tileLayout = new File("");
			break;

		case "___ city":
			tileLayout = new File("");
			break;

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

					case '-':
						break;

					case 'T':
						map[y][x] = new Tile("Tree");
						break;

					case 'D':
						map[y][x] = new Tile("Dirt");
						break;

					case 'W':
						map[y][x] = new Tile("TallGrass");
						break;

					case 'G':
						map[y][x] = new Tile("Grass");
						break;

					case 'P':

						for (int position = 0; position < 3; position++) {
							map[y][x] = new Tile("PokemonCenter", (position + 1));
							x++;
						}
						x -= 3;

						for (int position = 3; position < 6; position++) {
							map[y + 1][x] = new Tile("PokemonCenter", (position + 1));
							x++;
						}
						x--;
						break;

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

					case 'B':

						for (int position = 0; position < 4; position++) {
							map[y][x] = new Tile("HouseBigRed", (position + 1));
							x++;
						}
						x -= 4;

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
					}
				}
			}

			mapStream.close();

		} catch (FileNotFoundException e) {
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

	public boolean checkExit(String mapName, int row, int col) {

		switch (mapName) {

		case "Orilon Town":
			if ((row >= 10 && row <= 15) && (col == 0)) {
				exit = true;
				nextMap = "Route One";
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
			} else if ((playerX >= 23 && playerX <= 25) && playerY == 0) {
				exit = true;
				nextMap = "Route One";
				playerSpawnX = 10;
				playerSpawnY = 11;
				spawnpoint = 1;
			} else {
				exit = false;
			}

		}
		return exit;
	}

	public String getNextMap() {
		return nextMap;
	}

	public int getNextSpawn() {
		return spawnpoint;
	}

	public String getName() {
		return currentMapName;
	}
}