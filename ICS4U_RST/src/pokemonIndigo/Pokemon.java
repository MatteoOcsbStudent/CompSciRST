package pokemonIndigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;

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

	String status;
	String name;

	Image spriteFront;
	Image spriteBack;

	String[] types = new String[2];
	String[] evolutions;
	int[] evoLevels;

	ArrayList<Move> movePool = new ArrayList<Move>();
	ArrayList<Integer> movePoolLevels = new ArrayList<Integer>();
	ArrayList<Move> currentMoves = new ArrayList<Move>();
	
	File pokemonInfo;

	public Pokemon(String species, int foundLevel) {

		// Setting name
		name = species;

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

			// Reading move names and levels at which they're learned
			for (int i = 0; i < amountOfMoves; i++) {
				movePool.add(new Move(pokeStream.readLine()));
				movePoolLevels.add(Integer.parseInt(pokeStream.readLine()));
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

				// Skipping evolution types
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

		for (int i = 0; i < foundLevel; i++) {
			levelUp();
		}
	}

	public void levelUp() {
		//Tracking level
		level++;

		//Checking for evolution or move learning opportunity
		for (int i = 0; i < evoLevels.length; i++) {
			if (level == evoLevels[i]) {
				evolve(i);
			}
			
			if (level == movePoolLevels.get(i)) {
				changeMoveSet(movePool.get(i));
			}
		}

		// HP Stat

		totalHP = ((2 * baseHP * level) / 100) + level + 10;

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

	public void gainExp(int expGain) {
		exp += expGain;
	}

	public int getLevel() {
		return level;
	}

	public int getCurrentHP() {
		return currentHP;
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

	public void evolve(int evolution) {

		//Changing name
		name = evolutions[evolution];

		//Changing sprites
		spriteFront = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Front.png").toString());
		spriteBack = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Back.png").toString());

		FileReader pokeFileReader;

		
		try {
			pokeFileReader = new FileReader(pokemonInfo);
			BufferedReader pokeStream = new BufferedReader(pokeFileReader);

			//Skips down the file until data is found
			String line = pokeStream.readLine();
			do {
				 line = pokeStream.readLine();
			} while (line.equals(name) == false) ;
			

			//Sets new base stats
			baseHP = Integer.parseInt(pokeStream.readLine());
			baseAtk = Integer.parseInt(pokeStream.readLine());
			baseDef = Integer.parseInt(pokeStream.readLine());
			baseSpd = Integer.parseInt(pokeStream.readLine());
			
			//skip evolution level
			pokeStream.readLine();
			
			//Reads new types
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

	public void changeMoveSet(Move newMove, int indexToReplace) {
		
		currentMoves.remove(indexToReplace);
		currentMoves.add(indexToReplace, newMove);
	}
	
	public void changeMoveSet(Move newMove) {
		currentMoves.add(newMove);
	}

	public Move getMove(int index) {
		return currentMoves.get(index);
	}
}
