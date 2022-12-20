package branchesAreSoFun;


import java.io.File;


import javafx.scene.image.Image;

import simpleIO.Console;

public class TileGrid {

	public Tile[][] map;
	private int CAMERAHEIGHT, CAMERAWIDTH = 20;

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
			

		case "":
			
			break;

		}
	}
	
	public Image getTexture(int row, int col) {
		
			
	}
	
	
}

