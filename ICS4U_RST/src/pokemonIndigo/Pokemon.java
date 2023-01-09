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
	int level;
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
	
	ArrayList <Integer> evoLevels = new ArrayList <Integer>();
	ArrayList <Move> movePool = new ArrayList <Move>();
	ArrayList <Integer> movePoolLevels = new ArrayList <Integer>();
	ArrayList <Move> currentMoves = new ArrayList <Move>();

	public Pokemon(String species, int level) {
		name = species;
		
		Image spriteFront = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Front.png").toString());
		Image spriteBack = new Image(getClass().getResource("/images/PokemonSprites/" + name + "Back.png").toString());
		
		File pokemonInfo = new File("data/pokemonFiles/" + name + ".txt");
		
		FileReader pokeFileReader;
		
		try {
			pokeFileReader = new FileReader(pokemonInfo);
			BufferedReader pokeStream = new BufferedReader(pokeFileReader);

			types[0] = pokeStream.readLine();
			types[1] = pokeStream.readLine();
			
			baseHP = Integer.parseInt(pokeStream.readLine());
			baseAtk = Integer.parseInt(pokeStream.readLine());
			baseDef = Integer.parseInt(pokeStream.readLine());
			baseSpd = Integer.parseInt(pokeStream.readLine());
			
			int amountOfMoves = Integer.parseInt(pokeStream.readLine());
			
			for (int i = 0; i < amountOfMoves; i++) {
				movePool.add(new Move(pokeStream.readLine()));
				movePoolLevels.add(Integer.parseInt(pokeStream.readLine()));
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (int i = 1; i < level; i++) {
			levelUp();
		}
	}
	
	public void levelUp () {
		level++;
		
		//HP Stat
		int hpIncrease = (int) Math.ceil(baseHP/50);
		totalHP += hpIncrease;
		currentHP += hpIncrease;
		
		//Atk stat
		int atkIncrease = (int) Math.ceil(baseAtk/50);
		attack += atkIncrease;
		
		//Def stat
		int defIncrease = (int) Math.ceil(baseDef/50);
		defense += defIncrease;
		
		int spdIncrease = (int) Math.ceil(baseSpd/50);
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
		
		for (int i = 0; i < 3; i++) {
			if (evoLevels.get(i) > level) {
			nextEvo = evoLevels.get(i);
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
