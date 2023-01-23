package pokemonIndigo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Move {

	//Move specifications
	private int damage, accuracy, statusRate;
	
	private boolean priority;

	private String name, type, status;

	/**Overloaded Constructor
	 * sets the specifications of the move by reading the file name
	 * @param moveName
	 */
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**Accessor Method
	 * returns the damage the move deals
	 * @return damage
	 */
	public int getDamage() {
		return damage;
	}

	/**Accessor Method
	 * returns the type value of the move
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**Accessor Method
	 * returns the accuracy value of the move 
	 * @return accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}

	/**Accessor Method
	 * returns the status name that the move has
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**Accessor Method
	 * returns the percentage chance that the status has of being applied
	 * @return statusRate
	 */
	public int getStatusRate() {
		return statusRate;
	}

	/**Accessor Method
	 * returns the name of the move
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**Accessor Method
	 * returns a boolean stating whether or not the move has priority (will always hit first)
	 * @return priority
	 */
	public boolean isPriority() {
		return priority;
	}
}
