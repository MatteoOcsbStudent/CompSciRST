package pokemonIndigo;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Pokemon {

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

	public Pokemon(String species) {
		name = species;
	}
	
	public void levelUp () {
		level++;
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
