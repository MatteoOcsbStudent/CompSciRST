package pokemonIndigo;

import java.util.ArrayList;

public class Battle {

	Pokemon playerPokemon;
	Pokemon opponentPokemon;
	
	boolean opponentFainted;
	boolean playerFainted;
	
	Move opponentMove;

	Pokemon firstToMove;
	Pokemon secondToMove;

	Move fasterMove;
	Move slowerMove;

	boolean isTrainerBattle = false;

	ArrayList <String> battleResponses  = new ArrayList <String>();

	public Battle(Pokemon p1, Pokemon p2, boolean trainer) {
		playerPokemon = p1;
		opponentPokemon = p2;
		isTrainerBattle = trainer;

		if (isTrainerBattle == false) {
			battleResponses.add("A wild " + opponentPokemon.getName() + " has appeared!");
		}

		if (isTrainerBattle == true) {
			battleResponses.add("Opponent sent out " + opponentPokemon.getName() + "!");
		}

	}

	public void turnPlan(Move playerMove) {

		int opponentMoveIndex = 0;
		double random = Math.random();

		//Randomizing opponent's movechoice
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

	public void turnExecution(String firstOrSecond) {

		// Intializing variables based on which pokemon is attacking
		Pokemon attacking;
		Pokemon defending;
		Move usedMove;

		if (firstOrSecond.equals("First")) {
			attacking = firstToMove;
			defending = secondToMove;
			usedMove = fasterMove;
		} else if (firstOrSecond.equals("Second")){
			attacking = secondToMove;
			defending = firstToMove;
			usedMove = slowerMove;
		} else { 
			attacking = opponentPokemon;
			defending = playerPokemon;
			usedMove = opponentMove;
		}
		
		if (playerPokemon.equals(attacking) && playerFainted == false || opponentPokemon.equals(attacking) && opponentFainted == false) {
		// Status afflictions
		switch (attacking.getStatus()) {

		// Pokemon takes burn damage at the start of the turn
		case ("Burn"):
			battleResponses.add(attacking.getName() + " has been hurt by it's burn");
			attacking.hpChange((int) attacking.getTotalHP() / 16);
			damageCalc(attacking, defending, usedMove);
			break;
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
		// poison, takes 1/12 total hp
		case ("Poison"):
			battleResponses.add(attacking.getName() + " took damage from posion");
			attacking.hpChange((int) attacking.getTotalHP() / 12);
			damageCalc(attacking, defending, usedMove);
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

	public Pokemon getFirstToMove() {
		return firstToMove;
	}

	public Pokemon getSecondToMove() {
		return secondToMove;
	}

	public void damageCalc(Pokemon attacking, Pokemon defending, Move usedMove) {
		
		double damage = 0;

		if (Math.random() * 100 <= usedMove.getAccuracy()) {
			// Types of both pokemon
			String[] attackingTypes = attacking.getTypes().split("-");
			String[] defendingTypes = defending.getTypes().split("-");

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
			damage = ((temp / 50) + 2) * stab * typeCompare(usedMove.getType(), defendingTypes[0])
					* typeCompare(usedMove.getType(), defendingTypes[1]);
			Math.round(damage);

			// Status moves
			if (usedMove.getStatus() != "Null") {

				// Healing
				if (usedMove.getStatus() == "Heal") {
					attacking.hpChange((int) -damage / usedMove.getStatusRate());
					battleResponses.add(attacking.getName() + " has healed themselves");
				}

				// Applying status
				else if ((Math.random() * 100) <= usedMove.getStatusRate()) {
					defending.setStatus(usedMove.getStatus());
					battleResponses.add(defending.getName() + " has been afflicted with " + usedMove.getStatus());
				}
			}

			battleResponses.add(attacking.getName() + " used " + usedMove.getName() + "!");

		} else {
			battleResponses.add(attacking.getName() + " missed!");
		}

		// Applies damage
		defending.hpChange((int) damage);
		if (defending.currentHP < 0) {
			battleResponses.add(defending.getName() + " has fainted");
			if(defending.equals(opponentPokemon)) {
				opponentFainted = true;
			} else if (defending.equals(playerPokemon)) {
				playerFainted = true;
			}
		}

	}

	public int typeCompare(String type1, String type2) {

		int amp = 1;

		if (amp == 0.5) {
			battleResponses.add("It was not very effective...");
		}

		if (amp == 2 || amp == 4) {
			battleResponses.add("It was super effective!");
		}

		return 1;
	}

	public String catchPokemon() {

		String result;
		int catchRate;

		// Catch catch a trainer's pokemon
		if (isTrainerBattle == true) {
			result = "You can't catch another trainer's Pokemon! are you crazy?";

			// Calculates catch rate
		} else {
			catchRate = (((3 * opponentPokemon.getTotalHP()) - (2 * opponentPokemon.getCurrentHP())) * 200)
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

	public boolean flee() {

		// 90% chance of fleeing
		boolean success = false;

		if (Math.random() < 1) {
			success = true;
		}

		return success;
	}

	public void switchPokemon(Pokemon newPokemon) {

		playerPokemon = newPokemon;
		battleResponses.add("You have sent out " + playerPokemon.getName());
	}

	public void switchPokemon() {

		Pokemon temp = opponentPokemon;

		// TODO - opponentPokemon = arraylist.get(tempindex+1)
	}

	public String battleResponses(int index) {
		
		return battleResponses.get(index);

	}
	
	public int responseAmount() {
		return battleResponses.size();
	}
	
	public void clearResponses() {
		battleResponses.clear();
	}

}
