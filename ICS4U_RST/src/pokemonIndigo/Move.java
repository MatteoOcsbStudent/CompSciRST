package pokemonIndigo;

public class Move {

	private int damage;
	private int accuracy;
	private int statusRate;
	
	private String name;
	private String type;
	private String status;
	
	public Move(String moveName) {
		name = moveName;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public String getType() {
		return type;
	}
	
	public int getAccuracy() {
		return accuracy;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getStatusRate() {
		return statusRate;
	}
	
	public String getName() {
		return name;
	}
}
