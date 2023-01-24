package pokemonIndigo;

import java.util.ArrayList;

public class Battle {

	// Pokemon in the battle
	Pokemon playerPokemon;
	Pokemon opponentPokemon;

	// Will always be player or opponent pokemon
	Pokemon firstToMove;
	Pokemon secondToMove;

	// Trainer player object reference
	Player trainer;

	// If pokemon has been fainted
	boolean opponentFainted;
	boolean playerFainted;

	// if player pokemon is going to evolve, holds health bar update in boot if true
	boolean goingToEvolve;

	// If move has no effect, does 0 damage, cannot apply status's
	boolean noEffect;

	// If battle is wild encounter or trainer
	boolean isTrainerBattle = false;

	// Move used
	Move opponentMove;
	Move fasterMove;
	Move slowerMove;

	// Turn to be executed
	String currentTurn;

	// Text to display, informing user what happened in the battle
	ArrayList<String> battleResponses = new ArrayList<String>();

	/**
	 * Overloaded Constructor Sets the pokemon to be battled against sets
	 * isTrainerBattle to false
	 * 
	 * @param Pokemon p1, Pokemon p2
	 */

	public Battle(Pokemon p1, Pokemon p2) {
		playerPokemon = p1;
		opponentPokemon = p2;
		isTrainerBattle = false;

		if (isTrainerBattle == false) {
			battleResponses.add("A wild " + opponentPokemon.getName() + " has appeared!");
		}

	}

	/**
	 * Overloaded Constructor Sets the trainer to be battled against sets
	 * isTrainerBattle to true
	 * 
	 * @param Pokemon p1, Player trainerToBattle
	 */
	public Battle(Pokemon p1, Player trainerToBattle) {

		trainer = trainerToBattle;

		playerPokemon = p1;
		opponentPokemon = trainer.getPokemon(0);
		isTrainerBattle = true;

		if (isTrainerBattle == true) {

			// UI text display
			battleResponses.add("You challenged " + trainer.getName() + "!");

			String nameNoTitle = trainer.getName().replace("GymLeader ", "");

			battleResponses.add(nameNoTitle + " sent out " + opponentPokemon.getName() + "!");
		}
	}

	/**
	 * Instance Method Randomizes opponents move Determines which pokemon goes
	 * first, based on pokemon speed and move priority
	 * 
	 * @param move playerMove
	 * @return
	 */

	public void turnPlan(Move playerMove) {

		goingToEvolve = false;

		int opponentMoveIndex = 0;
		double random = Math.random();

		// Randomizing opponent's movechoice
		switch (opponentPokemon.getMovePoolSize()) {

		case 1:
			opponentMoveIndex = 0;
			break;
		case 2:
			if (random < 0.5) {
				opponentMoveIndex = 0;
			}

			if (random >= 0.5) {
				opponentMoveIndex = 1;
			}
			break;
		case 3:
			if (random <= 0.33) {
				opponentMoveIndex = 0;
			}

			if (random > 0.33 && random <= 0.66) {
				opponentMoveIndex = 1;
			}

			if (random > 0.66) {
				opponentMoveIndex = 2;
			}
			break;
		case 4:

			if (random <= 0.25) {
				opponentMoveIndex = 0;
			}

			if (0.25 < random && random <= 0.5) {
				opponentMoveIndex = 1;
			}
			if (0.5 < random && random < 0.75) {
				opponentMoveIndex = 2;
			}

			if (0.75 < random && random <= 1) {
				opponentMoveIndex = 3;
			}
			break;

		default:
			opponentMoveIndex = 0;
			break;
		}

		opponentMove = opponentPokemon.getMove(opponentMoveIndex);

		// Calculating which pokemon moves first
		// Stores the order which moves will be used
		// Priority Tie goes to speed
		if (playerMove.isPriority() == true && opponentMove.isPriority() == true) {
			if (playerPokemon.getSpeed() > opponentPokemon.getSpeed()) {
				firstToMove = playerPokemon;
				fasterMove = playerMove;
				secondToMove = opponentPokemon;
				slowerMove = opponentMove;
			} else if (opponentPokemon.getSpeed() > playerPokemon.getSpeed()) {
				firstToMove = opponentPokemon;
				fasterMove = opponentMove;
				secondToMove = playerPokemon;
				slowerMove = playerMove;
				// Speed tie, 50/50
			} else {
				double coinToss = Math.random();
				if (coinToss <= 0.5) {
					firstToMove = playerPokemon;
					fasterMove = playerMove;
					secondToMove = opponentPokemon;
					slowerMove = opponentMove;
				} else {
					firstToMove = opponentPokemon;
					fasterMove = opponentMove;
					secondToMove = playerPokemon;
					slowerMove = playerMove;
				}
			}
			// Priority
		} else if (playerMove.isPriority() == true) {
			firstToMove = playerPokemon;
			fasterMove = playerMove;
			secondToMove = opponentPokemon;
			slowerMove = opponentMove;
		} else if (opponentMove.isPriority() == true) {
			firstToMove = opponentPokemon;
			fasterMove = opponentMove;
			secondToMove = playerPokemon;
			slowerMove = playerMove;

			// If no priority, goes to speed
		} else if (playerPokemon.getSpeed() > opponentPokemon.getSpeed()) {
			firstToMove = playerPokemon;
			fasterMove = playerMove;
			secondToMove = opponentPokemon;
			slowerMove = opponentMove;
		} else if (opponentPokemon.getSpeed() > playerPokemon.getSpeed()) {
			firstToMove = opponentPokemon;
			fasterMove = opponentMove;
			secondToMove = playerPokemon;
			slowerMove = playerMove;

			// Speed tie, 50/50
		} else {
			double coinToss = Math.random();
			if (coinToss <= 0.5) {
				firstToMove = playerPokemon;
				fasterMove = playerMove;
				secondToMove = opponentPokemon;
				slowerMove = opponentMove;
			} else {
				firstToMove = opponentPokemon;
				fasterMove = opponentMove;
				secondToMove = playerPokemon;
				slowerMove = playerMove;
			}
		}

	}

	/**
	 * Instance Method runs first or second turn Calculates if pokemon takes turn,
	 * may not due to status
	 * 
	 * @param String firstOrSecond
	 * @return
	 */

	public void turnExecution(String firstOrSecond) {

		// Intializing variables based on which pokemon is attacking
		Pokemon attacking;
		Pokemon defending;
		Move usedMove;

		if (firstOrSecond.equals("First")) {
			currentTurn = "First";
			attacking = firstToMove;
			defending = secondToMove;
			usedMove = fasterMove;
		} else if (firstOrSecond.equals("Second")) {
			currentTurn = "Second";
			attacking = secondToMove;
			defending = firstToMove;
			usedMove = slowerMove;
		} else {
			attacking = opponentPokemon;
			defending = playerPokemon;
			usedMove = opponentMove;
		}

		if (playerPokemon.equals(attacking) && playerFainted == false
				|| opponentPokemon.equals(attacking) && opponentFainted == false) {
			// Status afflictions
			switch (attacking.getStatus()) {

			// 50% chance of missing the move
			case ("Confusion"):
				if (Math.random() < 0.5) {
					damageCalc(attacking, defending, usedMove);
				} else {
					battleResponses.add(attacking.getName() + " missed in confusion!");
				}
				break;
			// 30% chance of being fully paralyzed
			case ("Paralyze"):
				if (Math.random() < 0.3) {
					battleResponses.add(attacking.getName() + " is fully paralyzed!");
				} else {
					damageCalc(attacking, defending, usedMove);
				}
				break;
			// can't move, 33% chance to break out of sleep
			case ("Sleep"):
				battleResponses.add(attacking.getName() + " is sleeping");
				if (Math.random() < 0.33) {
					battleResponses.add(attacking.getName() + " woke up!");
					damageCalc(attacking, defending, usedMove);
					attacking.setStatus("Null");
				}
				break;

			// No status, moves normally
			case ("Null"):
				damageCalc(attacking, defending, usedMove);
				break;

			default:
				damageCalc(attacking, defending, usedMove);
				break;
			}

		}

	}

	/**
	 * Instance Method Determines if pokemon's move misses or not based on move's
	 * accuracy calculates damage and adds it if move doesn't miss and is effective
	 * applies status of usedMove to defending pokemon at the status application
	 * rate of usedMove, if applicable attacking takes damage from their status if
	 * applicable if defending is fainted and attacking is a player's pokemon, gains
	 * exp levels up if gained enough xp. Checks for move learning and evolution
	 * opportunity Adds all actions taken to battleResponses for UI display
	 * 
	 * @param Pokemon attacking, Pokemon defending, Move usedMove
	 * @return
	 */
	public void damageCalc(Pokemon attacking, Pokemon defending, Move usedMove) {

		playerFainted = false;
		opponentFainted = false;

		double damage = 0;

		// If move lands
		if (Math.random() * 100 <= usedMove.getAccuracy()) {

			battleResponses.add(attacking.getName() + " used " + usedMove.getName() + "!");

			// Doesn't calculate damage or display effectiveness if damage = 0
			if (usedMove.getDamage() != 0) {
				// Types of attacking pokemon
				String[] attackingTypes = attacking.getTypes().split("-");

				// Portion of damage equation
				double temp = ((2 * attacking.getLevel()) / 5.0 + 2) * usedMove.getDamage()
						* (attacking.getAttack() / defending.getDefense());

				// Calculating stab (same type attack bonus)
				double stab;
				if (attackingTypes[0] == usedMove.getType() || attackingTypes[1] == usedMove.getType()) {
					stab = 1.5;
				} else {
					stab = 1;
				}

				// Last part of damage equation
				damage = ((temp / 50) + 2) * stab * typeCompare(usedMove.getType(), defending);

				Math.round(damage);
			}
			// Status moves
			if (usedMove.getStatus() != "Null" && noEffect == false) {

				// Healing
				if (usedMove.getStatus().equals("Heal")) {
					attacking.moveHeal((int) (damage / usedMove.getStatusRate()));
					battleResponses.add(attacking.getName() + " has healed themselves");
				}

				// Applying status
				else if ((Math.random() * 100) <= usedMove.getStatusRate()) {

					if (usedMove.getStatus().equals(defending.getStatus())) {
						// Do nothing if pokemon already has the status
					} else {
						defending.setStatus(usedMove.getStatus());
						battleResponses.add(defending.getName() + " has been afflicted with " + usedMove.getStatus());
					}
				}
			}
		} else {
			battleResponses.add(attacking.getName() + " missed!");
		}

		switch (attacking.getStatus()) {

		// Takes 1/12 max hp damage at the end of the turn
		case ("Poison"):
			battleResponses.add(attacking.getName() + " took damage from posion");
			attacking.hpChange((int) attacking.getTotalHP() / 12);

			break;

		// Takes 1/16 max hp at the end of the turn
		case ("Burn"):
			battleResponses.add(attacking.getName() + " has been hurt by it's burn");
			attacking.hpChange((int) attacking.getTotalHP() / 16);
			break;
		}

		if (damage <= 1 && noEffect == false && usedMove.getDamage() != 0) {
			damage = 1;
		}

		// Applies damage
		defending.hpChange((int) damage);

		// Determines if defending pokemon fainted
		if (defending.currentHP <= 0) {

			// Sets to zero for health bar purposes
			defending.setCurrentHP(0);

			// Informs user it was fainted
			battleResponses.add(defending.getName() + " has fainted");

			// If opponent fainted, gain exp
			if (defending.equals(opponentPokemon)) {
				opponentFainted = true;
				int expGain = (int) (((64 * opponentPokemon.getLevel()) / 7) * 12);
				battleResponses.add(attacking.getName() + " gained " + expGain + " exp");
				playerPokemon.gainExp(expGain);

				// Level up as many times as necessary for exp gain
				do {
					attacking.levelUp();
					battleResponses.add(attacking.getName() + " leveled up to " + playerPokemon.getLevel() + "!");

					// If evolution level is hit, evolve pokemon and inform user
					if (attacking.getNextEvolution() != -1) {

						goingToEvolve = true;
						String previousEvolution = attacking.getName();
						attacking.evolve(attacking.getNextEvolution());

						battleResponses.add(previousEvolution + " has evolved into " + attacking.getName() + "!");
					}

					// checks if move can be learned
					if (attacking.getNextMoveLearn() != "") {

						// If less than 4 moves, automatically learn new one
						if (attacking.getMovePoolSize() < 4) {
							battleResponses.add(attacking.getName() + " has learned " + attacking.getNextMoveLearn());
							attacking.changeMoveSet(attacking.getNextMoveLearn());

							// Otherwise, ask user if they would like to learn the new move
						} else {
							battleResponses
									.add(attacking.getName() + " is trying to learn " + attacking.getNextMoveLearn());
							battleResponses.add("Would you like to forget an old move?");
						}
					}
					// Levels up as many times as possible
				} while (attacking.getExp() >= attacking.getNextLevelUp());

			} else if (defending.equals(playerPokemon)) {
				playerFainted = true;
				battleResponses.add(playerPokemon.getName() + " has fainted");

			}
		}

	}

	/**
	 * Instance Method Compares types for effectiveness, and determines damage
	 * amplification
	 * 
	 * @param String type1, Pokemon defending
	 * @return double amp
	 */
	public double typeCompare(String type1, Pokemon defending) {

		noEffect = false;
		double amp = 1;
		String[] defendingTypes = defending.getTypes().split("-");

		for (int i = 0; i < defendingTypes.length; i++) {

			if (defendingTypes[i] != "Null") {
				switch (type1) {

				case ("Fire"):

					switch (defendingTypes[i]) {

					case "Fire":
						amp *= 0.5;
						break;

					case "Water":
						amp *= 0.5;
						break;

					case "Rock":
						amp *= 0.5;
						break;

					case "Dragon":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 2;
						break;

					case "Ice":
						amp *= 2;
						break;

					case "Bug":
						amp *= 2;
						break;

					case "Steel":
						amp *= 2;
						break;
					}
					break;

				case ("Grass"):

					switch (defendingTypes[i]) {

					case "Fire":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 0.5;
						break;

					case "Poison":
						amp *= 0.5;
						break;

					case "Flying":
						amp *= 0.5;
						break;

					case "Bug":
						amp *= 0.5;
						break;

					case "Dragon":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Water":
						amp *= 2;
						break;

					case "Ground":
						amp *= 2;
						break;

					case "Rock":
						amp *= 2;
						break;

					}
					break;

				case ("Water"):

					switch (defendingTypes[i]) {

					case "Water":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 0.5;
						break;

					case "Dragon":
						amp *= 0.5;
						break;

					case "Fire":
						amp *= 2;
						break;

					case "Ground":
						amp *= 2;
						break;

					case "Rock":
						amp *= 2;
						break;

					}

					break;

				case ("Fighting"):

					switch (defendingTypes[i]) {

					case "Poison":
						amp *= 0.5;
						break;

					case "Flying":
						amp *= 0.5;
						break;

					case "Psychic":
						amp *= 0.5;
						break;

					case "Bug":
						amp *= 0.5;
						break;

					case "Fairy":
						amp *= 0.5;
						break;

					case "Normal":
						amp *= 2;
						break;

					case "Ice":
						amp *= 2;
						break;

					case "Rock":
						amp *= 2;
						break;

					case "Dark":
						amp *= 2;
						break;

					case "Steel":
						amp *= 2;
						break;

					case "Ghost":
						amp *= 0;
						break;

					}

					break;

				case ("Ground"):

					switch (defendingTypes[i]) {

					case "Grass":
						amp *= 0.5;
						break;

					case "Bug":
						amp *= 0.5;
						break;

					case "Fire":
						amp *= 2;
						break;

					case "Electric":
						amp *= 2;
						break;

					case "Poison":
						amp *= 2;
						break;

					case "Rock":
						amp *= 2;
						break;

					case "Steel":
						amp *= 2;
						break;

					case "Flying":
						amp *= 0;
						break;

					}

					break;

				case ("Rock"):

					switch (defendingTypes[i]) {

					case "Fighting":
						amp *= 0.5;
						break;

					case "Ground":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Fire":
						amp *= 2;
						break;

					case "Ice":
						amp *= 2;
						break;

					case "Flying":
						amp *= 2;
						break;

					case "Bug":
						amp *= 2;
						break;

					}

					break;

				case ("Flying"):

					switch (defendingTypes[i]) {

					case "Electric":
						amp *= 0.5;
						break;

					case "Rock":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 2;
						break;

					case "Fighting":
						amp *= 2;
						break;

					case "Bug":
						amp *= 2;
						break;

					}

					break;

				case ("Bug"):

					switch (defendingTypes[i]) {

					case "Fire":
						amp *= 0.5;
						break;

					case "Fighting":
						amp *= 0.5;
						break;

					case "Poison":
						amp *= 0.5;
						break;

					case "Flying":
						amp *= 0.5;
						break;

					case "Ghost":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Fairy":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 2;
						break;

					case "Psychic":
						amp *= 2;
						break;

					case "Dark":
						amp *= 2;
						break;

					}

					break;

				case ("Psychic"):

					switch (defendingTypes[i]) {

					case "Psychic":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Fighting":
						amp *= 2;
						break;

					case "Posion":
						amp *= 2;
						break;

					case "Dark":
						amp *= 0;
						break;

					}

					break;

				case ("Normal"):

					switch (defendingTypes[i]) {

					case "Rock":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Ghost":
						amp *= 0;
						break;

					}

					break;

				case ("Electric"):

					switch (defendingTypes[i]) {

					case "Grass":
						amp *= 0.5;
						break;

					case "Electric":
						amp *= 0.5;
						break;

					case "Dragon":
						amp *= 0.5;
						break;

					case "Water":
						amp *= 2;
						break;

					case "Flying":
						amp *= 0.5;
						break;

					case "Ground":
						amp *= 0;
						break;

					}

					break;

				case ("Dark"):

					switch (defendingTypes[i]) {

					case "Fighting":
						amp *= 0.5;
						break;

					case "Dark":
						amp *= 0.5;
						break;

					case "Fariy":
						amp *= 0.5;
						break;

					case "Psychic":
						amp *= 2;
						break;

					case "Ghost":
						amp *= 2;
						break;

					}

					break;

				case ("Dragon"):

					switch (defendingTypes[i]) {

					case "Steel":
						amp *= 0.5;
						break;

					case "Dragon":
						amp *= 2;
						break;

					case "Fairy":
						amp *= 0;
						break;

					}

					break;

				case ("Ice"):

					switch (defendingTypes[i]) {

					case "Fire":
						amp *= 0.5;
						break;

					case "Water":
						amp *= 0.5;
						break;

					case "Ice":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 2;
						break;

					case "Ground":
						amp *= 2;
						break;

					case "Flying":
						amp *= 2;
						break;

					case "Dragon":
						amp *= 2;
						break;

					}

					break;

				case ("Ghost"):

					switch (defendingTypes[i]) {

					case "Dark":
						amp *= 0.5;
						break;

					case "Psychic":
						amp *= 2;
						break;

					case "Ghost":
						amp *= 2;
						break;

					case "Normal":
						amp *= 0;
						break;

					}

					break;

				case ("Poison"):

					switch (defendingTypes[i]) {

					case "Poison":
						amp *= 0.5;
						break;

					case "Ground":
						amp *= 0.5;
						break;

					case "Rock":
						amp *= 0.5;
						break;

					case "Ghost":
						amp *= 0.5;
						break;

					case "Grass":
						amp *= 2;
						break;

					case "Fairy":
						amp *= 2;
						break;

					case "Steel":
						amp *= 0;
						break;

					}

					break;

				case ("Steel"):

					switch (defendingTypes[i]) {

					case "Fire":
						amp *= 0.5;
						break;

					case "Water":
						amp *= 0.5;
						break;

					case "Electric":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Ice":
						amp *= 2;
						break;

					case "Rock":
						amp *= 2;
						break;

					case "Fairy":
						amp *= 2;
						break;

					}

					break;

				case ("Fairy"):

					switch (defendingTypes[i]) {

					case "Fire":
						amp *= 0.5;
						break;

					case "Poison":
						amp *= 0.5;
						break;

					case "Steel":
						amp *= 0.5;
						break;

					case "Fighting":
						amp *= 2;
						break;

					case "Ghost":
						amp *= 2;
						break;

					}

				default:
					break;
				}
			}

		}
		if (amp == 0) {
			battleResponses.add("It had no effect...");
			noEffect = true;
		}

		else if (amp == 0.5 || amp == 0.25) {
			battleResponses.add("It was not very effective...");
		}

		else if (amp == 2 || amp == 4) {
			battleResponses.add("It was super effective!");
		}

		return amp;
	}

	/**
	 * Instance Method runs catch rate arithmetic and randomizes success can't catch
	 * a trainer's pokemon
	 * 
	 * @param
	 * @return String result
	 */

	public String catchPokemon() {

		String result;
		int catchRate;

		// Catch catch a trainer's pokemon
		if (isTrainerBattle == true) {
			result = "You can't catch another trainer's Pokemon! are you crazy?";

			// Calculates catch rate
		} else {
			catchRate = (((3 * opponentPokemon.getTotalHP()) - (2 * opponentPokemon.getCurrentHP())) * 150)
					/ (3 * opponentPokemon.getTotalHP());

			// If random number below catchrate, return that pokemon has been caught
			if (Math.random() * 100 < catchRate) {
				result = opponentPokemon.getName() + " has been caught!";
			} else {
				result = opponentPokemon.getName() + " has broke free!";
			}
		}

		return result;
	}

	/**
	 * Instance Method 85% of fleeing battle if not a trainer battle
	 * 
	 * @param
	 * @return Boolean
	 */
	public boolean flee() {

		boolean success = false;

		// Can't flee a trainer battle
		if (isTrainerBattle == false) {
			// 85% chance of fleeing

			if (Math.random() < 0.85) {

				success = true;
			}
		}
		return success;
	}

	/**
	 * Mutator Method changes playerPokemon
	 * 
	 * @param newPokemon
	 */

	public void switchPokemon(Pokemon newPokemon) {

		playerPokemon = newPokemon;
	}

	/**
	 * Mutator Method changes opponentPokemon
	 * 
	 * @param nextPokemon
	 */
	public void nextOpponentPokemon(Pokemon nextPokemon) {

		opponentPokemon = nextPokemon;

		String nameNoTitle = trainer.getName().replace("GymLeader ", "");

		battleResponses.add(nameNoTitle + " sent out " + opponentPokemon.getName() + "!");

	}

	/**
	 * Accessor Method
	 * 
	 * @param index
	 * @return battleResponses at index
	 */
	public String battleResponses(int index) {

		return battleResponses.get(index);

	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return battleResponses size
	 */

	public int responseAmount() {
		return battleResponses.size();
	}

	/**
	 * Mutator Method Clears battleResponses
	 * 
	 * @param
	 * @return
	 */
	public void clearResponses() {
		battleResponses.clear();
	}

	/**
	 * Accessor Method
	 * 
	 * @param firstToMove pokemon
	 * @return
	 */
	public Pokemon getFirstToMove() {
		return firstToMove;
	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return secondToMove pokemon
	 */
	public Pokemon getSecondToMove() {
		return secondToMove;
	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return current turn
	 */
	public String getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return isTrainerBattle
	 */
	public boolean isTrainerBattle() {
		return isTrainerBattle;
	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return isOpponentFainted
	 */
	public boolean isOpponentFainted() {
		return opponentFainted;
	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return isPlayerFainted
	 */
	public boolean isPlayerFainted() {
		return playerFainted;
	}

	/**
	 * Accessor Method
	 * 
	 * @param
	 * @return isGoingToEvolve
	 */
	public boolean isGoingToEvolve() {
		return goingToEvolve;
	}

}