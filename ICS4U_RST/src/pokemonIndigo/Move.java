package pokemonIndigo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Move {

	private int damage;
	private int accuracy;
	private int statusRate;
	
	private boolean priority;

	private String name;
	private String type;
	private String status;

	public Move(String moveName) {
		name = moveName;

		FileReader moveFile;
		try {
			moveFile = new FileReader("data/moves/" + name + ".move");
			BufferedReader moveStream = new BufferedReader(moveFile);

			type = moveStream.readLine();
			damage = Integer.parseInt(moveStream.readLine());
			accuracy = Integer.parseInt(moveStream.readLine());
			status = moveStream.readLine();
			statusRate = Integer.parseInt(moveStream.readLine());
			String priorityString = moveStream.readLine();
			
			if (priorityString.equals("True")) {
				priority = true;
			} else { 
				priority = false;
			}
			
			moveFile.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getDamage() {
		return damage;
	}

	public String getType() {
		return type;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public String getStatus() {
		return status;
	}

	public int getStatusRate() {
		return statusRate;
	}

	public String getName() {
		return name;
	}
}
