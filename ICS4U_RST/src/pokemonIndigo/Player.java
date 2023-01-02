package pokemonIndigo;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Player {

	private ArrayList <Pokemon> team = new ArrayList<Pokemon>();
	private String name;
	private Image battleSprite;

	public Player(String playerName) {
		name = playerName;
	}
	
	public void addPokemon(Pokemon newPokemon) {
		team.add(newPokemon);
	}
	
	public String getName () {
		return name;
	}
	
	public Pokemon getPokemon(int index) {
		return team.get(index);
	}
	
	public void reorderTeam(int index1, int index2) {
		Pokemon temp2 = team.get(index2);
		Pokemon temp1 = team.get(index1);
		team.remove(index2);
		team.remove(index1);
		team.add(index2, temp1);
		team.add(index1, temp2);
		
	}
	
	public void removePokemon(int index) {
		team.remove(index);
	}
	
	public Image getBattleSprite() {
		return battleSprite;
	}

}

	