package branchesAreSoFun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class TileGrid {

	//Double array of Tiles
	private Tile[][] map;
	
	//Dimensions of loaded map
	private int mapWidth;
	private int mapHeight;
	
	//Player index location
	private int playerX;
	private int playerY;

	public TileGrid() {
		
	}
	public TileGrid(String mapName) {
		
		File tileLayout = null;
		
		//Declares file and spawnpoint based on chosen map
		switch (mapName) {
		
		case "routeOne":
			tileLayout = new File("data/routeOne.map");
			playerX = 14;
			playerY = 50;
			break;
		
		case "___ town":
			tileLayout = new File("");
			break;
			
		case "routeTwo":
			tileLayout = new File("");
			break;
		
		case "___ city":
			tileLayout = new File("");
			break;

		}
		
		//Reads map textfile
		try {
			FileReader mapReader = new FileReader(tileLayout);
			BufferedReader mapStream = new BufferedReader(mapReader);
			
			//Reads dimensions
			mapWidth = Integer.valueOf(mapStream.readLine());
			mapHeight = Integer.valueOf(mapStream.readLine());
			
			//Declares map double array dimensions
			map = new Tile [mapHeight][mapWidth];
			
			//Reads a row
			for (int y = 0; y < mapHeight; y++) {
				String temp = mapStream.readLine();
				
				//Reads each character in the row, declaring corresponding tile at the index
				for (int x = 0; x < temp.length(); x++) {
					char tileType = temp.charAt(x);
					
					switch (tileType) {
					
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
	
	public int getPlayerX () {
		return playerX;
	}
	
	public int getPlayerY () {
		return playerY;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public void setPlayerX(int movement) {
		playerX+=movement;
	}
	
	public void setPlayerY(int movement) {
		playerY+=movement;
	}
}

