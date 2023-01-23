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

	int baseHP;
	int baseAtk;
	int baseDef;
	int baseSpd;
	int level = 0;
	int nextLevelUp;
	int exp;
	int currentHP;
	int attack;
	int defense;
	int speed;
	int totalHP;

	String status = "Null";
	String name;
	String firstEvo;

	Image spriteFront;
	Image spriteBack;

	String[] types = new String[2];
	String[] evolutions;
	String[] movePool;
	int[] movePoolLevels;
	int[] evoLevels;

	ArrayList<Move> currentMoves = new ArrayList<Move>();

	File pokemonInfo;

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
			
			if(getNextEvolution() != -1) {
				evolve(getNextEvolution());
			}

			// Checks for moves that can be learned
			for (int c = 0; c < movePoolLevels.length; c++) {

				// Learns move at correct level
				if (level == movePoolLevels[c]) {

					// Learns move if theres space, max is 4
					if (currentMoves.size() < 4) {
            
						changeMoveSet((movePool[c]));
					
					//Checking for oldest move to replace if pokemon already has 4 moves
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

	public void levelUp() {
		// Tracking level
		level++;

		// Next level up 
		nextLevelUp = level * level * level - ((level-1) * (level-1) * (level-1));
		
		//Resets exp to whats carried over from last threshhold
		
		if (exp > ((level-1) * (level-1) * (level-1))) {
			int tempXp = exp;
			exp = (tempXp) - ((level-1) * (level-1) * (level-1));
		}
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

	public void hpChange(int damage) {
		currentHP -= damage;
	}

	public void changeStatus(String newStatus) {
		status = newStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String affliction) {
		status = affliction;
	}

	public void gainExp(int expGain) {
		exp += expGain;

		if (exp >= nextLevelUp) {
			levelUp();
		}

	}

	public int getLevel() {
		return level;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int newHP) {
		currentHP = newHP;
	}
	
	public int getExp() {
		return exp;
	}

	public int getNextLevelUp() {
		return nextLevelUp;
	}

	public Image getFrontSprite() {
		return spriteFront;
	}

	public Image getBackSprite() {
		return spriteBack;
	}

	public String getTypes() {
		return types[0] + "-" + types[1];
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getTotalHP() {
		return totalHP;
	}

	public int getSpeed() {
		return speed;
	}

	public String getName() {
		return name;
	}
	
	public int getNextEvolution() {
		int evolution = -1;
		for (int i = 0; i < evoLevels.length; i++) {
			if (level == evoLevels[i]) {
				evolution = i;
			}
		}
		
		return evolution;
	}

	public String getFirstEvo() {
		return firstEvo;
	}

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
	
	public String getNextMoveLearn() {
		
		String nextMoveLearn = "";
		
		for (int i = 0; i < movePoolLevels.length; i++) {
			if (level == movePoolLevels[i]) {
				nextMoveLearn = movePool[i];
			}
		}
		
		return nextMoveLearn;
	}

	public void changeMoveSet(String newMoveName, int indexToReplace) {
		
		Move newMove = new Move(newMoveName);
		
		currentMoves.remove(indexToReplace);
		currentMoves.add(indexToReplace, newMove);
	}

	public void changeMoveSet(String newMoveName) {
		
		Move newMove = new Move(newMoveName);
		currentMoves.add(newMove);
	}
	
	public void moveHeal(int heal) {
		currentHP+=heal;
		
		if (currentHP > totalHP) {
			currentHP = totalHP;
		}
	}

	public Move getMove(int index) {
		return currentMoves.get(index);
	}

	public int getMovePoolSize() {
		return currentMoves.size();
	}

	public void setCurrentHp(int hp) {
		currentHP = hp;
	}
}
