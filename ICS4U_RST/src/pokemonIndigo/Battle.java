package pokemonIndigo;

public class Battle {

	Pokemon playerPokemon;
	Pokemon opponentPokemon;
	
	int turnCounter;
	
	boolean isTrainerBattle = false;

	public Battle (Pokemon p1, Pokemon p2, boolean trainer) {
		playerPokemon = p1;
		opponentPokemon = p2;	
	}
	
	public void turnExecution(int chosenMoveIndex) {
		
		Pokemon firstToMove;
		Pokemon secondToMove;
		
		//Calculating which pokemon moves first
		if (playerPokemon.getSpeed() > opponentPokemon.getSpeed()) {
			firstToMove = playerPokemon;
			secondToMove = opponentPokemon;
		} else if (opponentPokemon.getSpeed() > playerPokemon.getSpeed()) {
			firstToMove = opponentPokemon;
			secondToMove = playerPokemon;
		
		//Speed tie, 50/50
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
		
		damageCalc(firstToMove, secondToMove, firstToMove.getMove(chosenMoveIndex));
		
		damageCalc(secondToMove, firstToMove, firstToMove.getMove(chosenMoveIndex));
		
		
		
		
	}
	
	public int damageCalc(Pokemon attacking, Pokemon defending, Move usedMove) {
		
		//Types of both pokemon
		String [] attackingTypes = attacking.getTypes().split("-");
		String [] defendingTypes = defending.getTypes().split("-");
		
		//Portion of damage equation
		double temp = ((2*attacking.getLevel())/5.0 + 2) * usedMove.getDamage() * (attacking.getAttack()/defending.getDefense());
		
		//Calculating stab (same type attack bonus)
		double stab;
		if(attackingTypes[0] == usedMove.getType() || attackingTypes[1] == usedMove.getType()) {
			stab = 1.5;
		} else {
			stab = 1;
		}
		
		//Last part of damage equation
		double damage = ((temp/50)+2) * stab * typeCompare(usedMove.getType(), defendingTypes[0]) * typeCompare(usedMove.getType(), defendingTypes[1]);
		Math.round(damage);	
		return (int) damage;
	}
	
	public int typeCompare(String type1, String type2) {
		
		return 1;
	}
	public boolean catchPokemon() {
		
		return false;
	}
	
	public boolean flee() {
		
		return false;
	}

}
