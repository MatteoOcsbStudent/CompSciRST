package pokemonIndigo;

import java.util.ArrayList;

public class Player {

	//Arraylist containing the pokemon on the player's team
	private ArrayList <Pokemon> team = new ArrayList<Pokemon>();
	
	//Player's name
	private String name;

	public Player () {
		
	}
	
	/**Overloaded Constructor
	 * Sets the player's name
	 * @param playerName
	 */
	public Player(String playerName) {
		name = playerName;
	}
	
	/**Accessor Method
	 * returns a pokemon at a specified index
	 * @param index
	 * @return team.get(index)
	 */
	public Pokemon getPokemon(int index) {
		return team.get(index);
	}
	
	/**Accessor Method
	 * returns the size of the player's team
	 * @return team.size()
	 */
	public int getTeamSize() {
		return team.size();
	}
	
	/**Accessor Method
	 * returns the name of the player
	 * @return name
	 */
	public String getName() {
		return name;
	}
	

	/**Mutator Method
	 * removes a pokemon from the player's team
	 * @param index
	 */
	public void replaceTeamMember(Pokemon newPoke, int indexToReplace) {
		team.set(indexToReplace, newPoke);

	}
	
	/**Mutator Method
	 * adds a pokemon to the player's team
	 * @param newPokemon
	 */
		public void addPokemon(Pokemon newPokemon) {
			team.add(newPokemon);
		}
	
	/**Mutator Method
	 * changes the position of two pokemon, swapping them
	 * @param index1
	 * @param index2
	 */
	public void reorderTeam(int index1, int index2) {
		Pokemon temp2 = team.get(index2);
		Pokemon temp1 = team.get(index1);
		team.set(index2, temp1);
		team.set(index1, temp2);
	
	}
}

	