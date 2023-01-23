package pokemonIndigo;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Player {

	private ArrayList <Pokemon> team = new ArrayList<Pokemon>();
	private String name;

	public Player () {
		
	}
	
	public Player(String playerName) {
		name = playerName;
	}
	
	//Adding a pokemon to the player's team
	public void addPokemon(Pokemon newPokemon) {
		team.add(newPokemon);
	}
	
	public Pokemon getPokemon(int index) {
		return team.get(index);
	}
	
	public void reorderTeam(int index1, int index2) {
		Pokemon temp2 = team.get(index2);
		Pokemon temp1 = team.get(index1);
		team.set(index2, temp1);
		team.set(index1, temp2);
	
	}
	
	public int getTeamSize() {
		return team.size();
	}
	
	public String getName() {
		return name;
	}
	
	public void replaceTeamMember(Pokemon newPoke, int indexToReplace) {
		team.set(indexToReplace, newPoke);
	}
}

	