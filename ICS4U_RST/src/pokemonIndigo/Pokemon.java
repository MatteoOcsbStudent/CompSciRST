package pokemonIndigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
	
	String [] types = new String[2];
	String [] evolutions;
	int [] evoLevels;
	
	ArrayList <Move> movePool = new ArrayList <Move>();
	ArrayList <Integer> movePoolLevels = new ArrayList <Integer>();
	ArrayList <Move> currentMoves = new ArrayList <Move>();

	public Pokemon(String species, int foundLevel) {
		
		//Setting name
		name = species;
		
		//Setting sprite based on pokemon name
		spriteFront = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Front.png").toString());
		spriteBack = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Back.png").toString());
		
		//Pulling information from file based on pokemon's name
		File pokemonInfo = new File("data/pokemonFiles/" + name + ".txt");
		
		FileReader pokeFileReader;
		
		try {
			pokeFileReader = new FileReader(pokemonInfo);
			BufferedReader pokeStream = new BufferedReader(pokeFileReader);

			//Reading pokemon's types, returns null for second type if there is none
			types[0] = pokeStream.readLine();
			types[1] = pokeStream.readLine();
			
			//Reads base stats
			baseHP = Integer.parseInt(pokeStream.readLine());
			baseAtk = Integer.parseInt(pokeStream.readLine());
			baseDef = Integer.parseInt(pokeStream.readLine());
			baseSpd = Integer.parseInt(pokeStream.readLine());
			
			//amount of moves to be read
			int amountOfMoves = Integer.parseInt(pokeStream.readLine());
			
			//Reading move names and levels at which they're learned
			for (int i = 0; i < amountOfMoves; i++) {
				movePool.add(new Move(pokeStream.readLine()));
				movePoolLevels.add(Integer.parseInt(pokeStream.readLine()));
			}
			
			//Amount of evolutions available
			int amountOfEvos = Integer.parseInt(pokeStream.readLine());
			
			//Declaring evolution data fields 
			evolutions = new String [amountOfEvos];
			evoLevels = new int [amountOfEvos];
			
			//Reading evolution names and levels at which evolution happens
			for (int i = 0; i < amountOfEvos; i++) {
				evolutions[i] = pokeStream.readLine();
				
				//Skipping base stats
				for (int skip = 0; skip < 4; skip++) {
					pokeStream.readLine();
				}
				
				evoLevels[i] = Integer.parseInt(pokeStream.readLine());
				
				//Skipping evolution types
				for (int skip = 0; skip < 2; skip++) {
					pokeStream.readLine();
				}
				
			}
			
			
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
	
	public void levelUp () {
		level++;
		//HP Stat
		//int hpIncrease = (int) Math.ceil(baseHP/25.0);
		//totalHP += hpIncrease;
		//currentHP += hpIncrease;
		
		totalHP = ((2*baseHP*level)/100) + level + 10;
		
		//Atk stat
		int atkIncrease = (int) Math.ceil(baseAtk/25.0);
		attack += atkIncrease;
		
		//Def stat
		int defIncrease = (int) Math.ceil(baseDef/25.0);
		defense += defIncrease;
		
		//Spd stat
		int spdIncrease = (int) Math.ceil(baseSpd/25.0);
		speed += spdIncrease;
	}
	
	public void hpChange(int damage) {
		currentHP -= damage;
	}
	
	public void changeStatus (String newStatus) {
		status = newStatus;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void gainExp(int expGain) {
		exp+= expGain;
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
	
	public int getNextEvo() {
		
		int nextEvo = -1;
		
		for (int i = 0; i < 1; i++) {
			if (evoLevels[i] > level) {
			nextEvo = evoLevels[i];
			}
		}
		return nextEvo;
	}
	
	public void evolve() {
		
	}
	
	public Move getNextMove() {
		Move nextMove = null;
		
		for (int i = 0; i < 100; i++) {
			if (movePoolLevels.get(i) > level) {
			nextMove = movePool.get(i);
			}
		}
		
		return nextMove;
	}
	
	public void changeMoveSet(Move newMove, int index) {
		currentMoves.remove(index);
		currentMoves.add(index, newMove);
	}
	
	public Move getMove(int index) {
		return currentMoves.get(index);
	}
}
