package branchesAreSoFun;

import java.io.File;
import javafx.scene.image.Image;
import simpleIO.Console;

public class TileGrid {

	public Tile[][] map = new Tile [10][10];
	private int CAMERAHEIGHT, CAMERAWIDTH = 20;

	public TileGrid() {
		
	}
	public TileGrid(String mapName) {
		
		File tileLayout;
		
		switch (mapName) {
		
		case "routeOne":
			tileLayout = new File("");
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
			

		case "test":
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					map[i][j] = new Tile ("Tree");
				}
			}
			
			break;

		}
	}
	
	public Tile getTile(int row, int col) {
			return map[row][col];
	}
	
	
}

