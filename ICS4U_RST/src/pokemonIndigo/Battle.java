package pokemonIndigo;

public class Battle {

	Pokemon playerPokemon;
	Pokemon opponentPokemon;

	Pokemon firstToMove;
	Pokemon secondToMove;

	boolean isTrainerBattle = false;

	String statusAfflictionStart;
	String statusAfflictionBattle;
	String statusApplied;
	String effectiveness;
	String move;
	String caught;
	String faint;

	public Battle(Pokemon p1, Pokemon p2, boolean trainer) {
		playerPokemon = p1;
		opponentPokemon = p2;

		// Calculating which pokemon moves first
		if (playerPokemon.getSpeed() > opponentPokemon.getSpeed()) {
			firstToMove = playerPokemon;
			secondToMove = opponentPokemon;
		} else if (opponentPokemon.getSpeed() > playerPokemon.getSpeed()) {
			firstToMove = opponentPokemon;
			secondToMove = playerPokemon;

			// Speed tie, 50/50
		} else {
			double coinToss = Math.random();
			if (coinToss <= 0.5) {
				firstToMove = playerPokemon;
				secondToMove = opponentPokemon;
			} else {
				firstToMove = opponentPokemon;
				secondToMove = playerPokemon;
			}
		}
	}

	public void turnExecution(Pokemon attacking, Pokemon defending, int chosenMoveIndex) {

		// Status afflictions
		switch (attacking.getStatus()) {

		// Pokemon takes burn damage at the start of the turn
		case ("Burn"):
			statusAfflictionStart = attacking.getName() + " has been hurt by it's burn";
			attacking.hpChange((int) attacking.getTotalHP() / 16);
			damageCalc(attacking, defending, attacking.getMove(chosenMoveIndex));
			break;
		// 50% chance of missing the move
		case ("Confusion"):
			if (Math.random() < 0.5) {
				damageCalc(attacking, defending, attacking.getMove(chosenMoveIndex));
			} else {
				statusAfflictionBattle = attacking.getName() + " missed in confusion!";
			}
			break;
		// 30% chance of being fully paralyzed
		case ("Paralyze"):
			if (Math.random() < 0.3) {
				statusAfflictionBattle = attacking.getName() + " is fully paralyzed!";
			} else {
				damageCalc(attacking, defending, attacking.getMove(chosenMoveIndex));
			}
			break;
		// can't move, 33% chance to break out of sleep
		case ("Sleep"):
			statusAfflictionBattle = attacking.getName() + " is sleeping";
			if (Math.random() < 0.33) {
				statusAfflictionBattle = attacking.getName() + " woke up!";
				damageCalc(attacking, defending, attacking.getMove(chosenMoveIndex));
				attacking.setStatus("Null");
			}
			break;
		// poison, takes 1/12 total hp
		case ("Poison"):
			statusAfflictionStart = attacking.getName() + " took damage from posion";
			attacking.hpChange((int) attacking.getTotalHP() / 12);
			damageCalc(attacking, defending, attacking.getMove(chosenMoveIndex));
			break;

		//No status, moves normally
		case ("Null"):
			damageCalc(attacking, defending, attacking.getMove(chosenMoveIndex));
			break;
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
		
		if (Math.random()*100 > usedMove.getAccuracy()) {
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
				statusApplied = attacking.getName() + " has healed themselves";
			}

			// Applying status
			else if ((Math.random() * 100) <= usedMove.getStatusRate()) {
				defending.setStatus(usedMove.getStatus());
				statusApplied = defending.getName() + " has been afflicted with " + usedMove.getStatus();
			}
		}
		
		move = attacking.getName() + " used " + usedMove.getName() + "!";

		} else {
			move = attacking.getName() + " missed!";
		}
		
		//Applies damage
		defending.hpChange((int)damage);
		if (defending.currentHP < 0) {
			faint = defending.getName() + " has fainted";
		}
		
	}

	public int typeCompare(String type1, String type2) {

		return 1;
	}

	public String catchPokemon() {
	
		String result;
		int catchRate;
		
		//Catch catch a trainer's pokemon 
		if (isTrainerBattle = true) {
		result = "You can't catch another trainer's Pokemon! are you crazy?";
		
		//Calculates catch rate
		} else {
			catchRate = (((3*opponentPokemon.getTotalHP()) - (2*opponentPokemon.getCurrentHP())) * 200)/(3*opponentPokemon.getTotalHP());
			
			//If random number below catchrate, return that pokemon has been caught 
			if (Math.random()*100 < catchRate) {
				result = opponentPokemon.getName() + " has been caught!";
			} else {
				result = opponentPokemon.getName() + " has broke free!";
			}
		}
		
		return result;
	}

	public boolean flee() {

		//85% chance of fleeing
		boolean success = false;
		
		if (Math.random() < 0.85) {
			success = true;
		}
		
		return success;
	}

}
