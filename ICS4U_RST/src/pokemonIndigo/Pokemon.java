package pokemonIndigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import sortAlgorithim.Sort;

public class Pokemon {

	//Pokemon Stats
	int baseHP, baseAtk, baseDef, baseSpd, currentHP, totalHP, attack, defense, speed;

	//Pokemon Level Datafields
	int level = 0;
	int nextLevelUp, exp;

	//Pokemon Status
	String status = "Null";
	
	//Pokemon Name
	String name;
	
	//Pokemon first evolution Name
	String firstEvo;

	//Sprites
	Image spriteFront;
	Image spriteBack;

	//Types
	String[] types = new String[2];
	
	//Evolutions
	String[] evolutions;
	int[] evoLevels;
	
	//MovePool
	String[] movePool;
	int[] movePoolLevels;
	ArrayList<Move> currentMoves = new ArrayList<Move>();

	//File to be read for the all the content in a specific pokemon species
	File pokemonInfo;

	/**
	 * Overloaded Constructor 
	 * Creates the pokemon, setting its stats and moves
	 * according to the level and name given runs levelUp() to help with the logic
	 * @param species
	 * @param foundLevel
	 */
	public Pokemon(String species, int foundLevel) {

		// Setting name
		name = species;

		firstEvo = species;

		// Setting sprite based on pokemon name
		spriteFront = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Front.png").toString());
		spriteBack = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Back.png").toString());

		// Pulling information from file based on pokemon's name
		pokemonInfo = new File("data/pokemonFiles/" + name + ".txt");

		FileReader pokeFileReader;

		try {
			pokeFileReader = new FileReader(pokemonInfo);
			BufferedReader pokeStream = new BufferedReader(pokeFileReader);

			// Reading pokemon's types, returns null for second type if there is none
			types[0] = pokeStream.readLine();
			types[1] = pokeStream.readLine();

			// Reads base stats
			baseHP = Integer.parseInt(pokeStream.readLine());
			baseAtk = Integer.parseInt(pokeStream.readLine());
			baseDef = Integer.parseInt(pokeStream.readLine());
			baseSpd = Integer.parseInt(pokeStream.readLine());

			// amount of moves to be read
			int amountOfMoves = Integer.parseInt(pokeStream.readLine());
			movePool = new String[amountOfMoves];
			movePoolLevels = new int[amountOfMoves];

			// Reading move names and levels at which they're learned
			for (int i = 0; i < amountOfMoves; i++) {
				movePool[i] = pokeStream.readLine();
				movePoolLevels[i] = Integer.parseInt(pokeStream.readLine());
			}

			// Amount of evolutions available
			int amountOfEvos = Integer.parseInt(pokeStream.readLine());

			// Declaring evolution data fields
			evolutions = new String[amountOfEvos];
			evoLevels = new int[amountOfEvos];

			// Reading evolution names and levels at which evolution happens
			for (int i = 0; i < amountOfEvos; i++) {
				evolutions[i] = pokeStream.readLine();

				// Skipping base stats
				for (int skip = 0; skip < 4; skip++) {
					pokeStream.readLine();
				}

				evoLevels[i] = Integer.parseInt(pokeStream.readLine());

				// Skipping evolution's types
				for (int skip = 0; skip < 2; skip++) {
					pokeStream.readLine();
				}

			}

			pokeFileReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Levels up pokemon to instantiated level
		for (int i = 0; i < foundLevel; i++) {
			levelUp();

			if (getNextEvolution() != -1) {
				evolve(getNextEvolution());
			}

			// Checks for moves that can be learned
			for (int c = 0; c < movePoolLevels.length; c++) {

				// Learns move at correct level
				if (level == movePoolLevels[c]) {

					// Learns move if theres space, max is 4
					if (currentMoves.size() < 4) {

						changeMoveSet((movePool[c]));

						// Checking for oldest move to replace if pokemon already has 4 moves
					} else {

						// Populates an array with the index at which the moves a pokemon knows is in
						// movePool
						int[] levelsLearned = new int[4];

						for (int j = 0; j < 4; j++) {
							for (int a = 0; a < movePool.length; a++) {
								if (currentMoves.get(j).getName().equals(movePool[a])) {
									levelsLearned[j] = a;
								}
							}
						}

						// Sorts levelsLearned in ascending order
						Sort.selectionSort(levelsLearned, 1);

						// Defines the oldest move a pokemon knows from movePool
						int oldestMoveIndexMovePool = levelsLearned[0];

						// Replaces the oldest move in currentMoves with the new move to be leanred
						for (int b = 0; b < 4; b++) {

							if (movePool[oldestMoveIndexMovePool].equals(currentMoves.get(b).getName())) {
								changeMoveSet((movePool[c]), b);
							}

						}

					}
				}
			}
		}

	}

	/**
	 * Instance Method 
	 * adds stats to the pokemon by comparing their level to their
	 * base stats when the pokemon reaches a certain level threshhold, it runs the
	 * evolve() method to boost their power
	 */
	public void levelUp() {
		// Tracking level
		
		if(exp > (nextLevelUp)) {
			int tempExp = exp - (nextLevelUp);
			exp = tempExp;
		}
		
		level++;

		// Next level up 
		nextLevelUp = (int)(level * level * level) - ((level-1) * (level-1) * (level-1));
		
		//Resets exp to whats carried over from last threshhold
    
		// HP Stat
		int temp = totalHP;
		totalHP = ((2 * baseHP * level) / 100) + level + 10;
		currentHP = totalHP - (temp - currentHP);

		// Atk stat

		attack = ((2 * baseAtk * level) / 100) + 5;

		// Def stat

		defense = ((2 * baseDef * level) / 100) + 5;

		// Spd stat

		speed = ((2 * baseSpd * level) / 100) + 5;
	}

	/**Instance Method
	 * when ran, increases the base stats of the pokemon, and changes its name and sprite to make it seem stronger
	 * @param evolution
	 */
	public void evolve(int evolution) {

		// Changing name
		name = evolutions[evolution];

		// Changing sprites
		spriteFront = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Front.png").toString());
		spriteBack = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Back.png").toString());

		FileReader pokeFileReader;

		try {
			pokeFileReader = new FileReader(pokemonInfo);
			BufferedReader pokeStream = new BufferedReader(pokeFileReader);

			// Skips down the file until data is found
			String line = pokeStream.readLine();
			do {
				line = pokeStream.readLine();
			} while (line.equals(name) == false);

			// Sets new base stats
			baseHP = Integer.parseInt(pokeStream.readLine());
			baseAtk = Integer.parseInt(pokeStream.readLine());
			baseDef = Integer.parseInt(pokeStream.readLine());
			baseSpd = Integer.parseInt(pokeStream.readLine());

			// skip evolution level
			pokeStream.readLine();

			// Reads new types
			types[0] = pokeStream.readLine();
			types[1] = pokeStream.readLine();

			pokeFileReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**Mutator Method
	 * lowers hp by the amount of damage the pokemon takes
	 * @param damage
	 */
	public void hpChange(int damage) {
		currentHP -= damage;
	}

	/**Mutator Method
	 * lowers hp by the amount of damage the pokemon takes
	 * @param newStatus
	 */
	public void changeStatus(String newStatus) {
		status = newStatus;
	}

	/**Mutator Method
	 * sets the status a pokemon is afflicted with (example: Paralyze, Sleep,)
	 * @param affliction
	 */
	public void setStatus(String affliction) {
		status = affliction;
	}

	/**Mutator Method
	 * increases exp by the amount given
	 * @param expGain
	 */
	public void gainExp(int expGain) {
		exp += expGain;
	}
	/**Mutator Method
	 * sets the current HP to the given amount
	 * @param newHP
	 */
	
	public void setCurrentHP(int newHP) {
		currentHP = newHP;
	}

	/**Accessor Method
	 * returns the status the pokemon is afflicted with
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**Accessor Method
	 * returns the level the pokemon has
	 * @return level
	 */
	public int getLevel() {
		return level;
	}

	/**Accessor Method
	 * returns the current hp of the pokemon
	 * @return currentHP
	 */
	public int getCurrentHP() {
		return currentHP;
	}

	/**Accessor Method
	 * returns the current exp the pokemon has
	 * @return exp
	 */
	public int getExp() {
		return exp;
	}
	
	/**Accessor Method
	 * returns the next exp threshhold the pokemon has to level up
	 * @return nextLevelUp
	 */
	public int getNextLevelUp() {
		return nextLevelUp;
	}

	/**Accessor Method
	 * returns the front sprite of the pokemon
	 * @return spriteFront
	 */
	public Image getFrontSprite() {
		return spriteFront;
	}

	/**Accessor Method
	 * returns the back sprite of the pokemon
	 * @return spriteBack
	 */
	public Image getBackSprite() {
		return spriteBack;
	}

	/**Accessor Method
	 * returns the types of the pokemon (example: Fire, Water, Electric)
	 * @return types
	 */
	public String getTypes() {
		return types[0] + "-" + types[1];
	}

	/**Accessor Method
	 * returns the attack value of the pokemon
	 * @return attack
	 */
	public int getAttack() {
		return attack;
	}

	/**Accessor Method
	 * returns the defense value of the pokemon
	 * @return defense
	 */
	public int getDefense() {
		return defense;
	}

	/**Accessor Method
	 * returns the totalHP value of the pokemon
	 * @return totalHP
	 */
	public int getTotalHP() {
		return totalHP;
	}

	/**Accessor Method
	 * returns the speed value of the pokemon
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**Accessor Method
	 * returns the name of the pokemon
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**Accessor Method
	 * returns the level threshhold for the next evolution
	 * @return evolution
	 */
	public int getNextEvolution() {
		int evolution = -1;
		for (int i = 0; i < evoLevels.length; i++) {
			if (level == evoLevels[i]) {
				evolution = i;
			}
		}

		return evolution;
	}

	/**Accessor Method
	 * returns the name of the first evolution of the pokemon (for saving and loading purposes)
	 * @return firstEvo
	 */
	public String getFirstEvo() {
		return firstEvo;
	}

	/**Accessor Method
	 * returns the name of the next move to learn
	 * @return nextMoveLearn
	 */
	public String getNextMoveLearn() {

		String nextMoveLearn = "";

		for (int i = 0; i < movePoolLevels.length; i++) {
			if (level == movePoolLevels[i]) {
				nextMoveLearn = movePool[i];
			}
		}

		return nextMoveLearn;
	}

	/**Mutator Method
	 * changes the moveset of the pokemon
	 * @param newMoveName
	 * @param indexToReplace
	 */
	public void changeMoveSet(String newMoveName, int indexToReplace) {

		Move newMove = new Move(newMoveName);

		currentMoves.remove(indexToReplace);
		currentMoves.add(indexToReplace, newMove);
	}

	/**Mutator Method
	 * adds to the moveset of the pokemon (if there's less than 4 moves)
	 * @param newMoveName
	 */
	public void changeMoveSet(String newMoveName) {

		Move newMove = new Move(newMoveName);
		currentMoves.add(newMove);
	}

	/** Mutator Method
	 * Increases the pokemon's HP (For healing moves)
	 * @param heal
	 */
	public void moveHeal(int heal) {
		currentHP += heal;

		if (currentHP > totalHP) {
			currentHP = totalHP;
		}
	}

	/**Accessor Method
	 * returns the move at the given index
	 * @param index
	 * @return
	 */
	public Move getMove(int index) {
		return currentMoves.get(index);
	}

	/**Accessor Method
	 * returns the size of the pokemon's movepool
	 *@return currentMoves.size()
	 */
	public int getMovePoolSize() {
		return currentMoves.size();
	}

	public void setCurrentHp(int hp) {
		currentHP = hp;
	}
}
