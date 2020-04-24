import java.util.ArrayList;

public class Player implements Comparable<Player> {
	private Card[] plants;
	private int money;
	private int turn;
	private ArrayList<String> cities;
	//REDO ALL RESOURCE MANAGEMENT

	public Player(int t) {
		plants = new Card[3];
		turn = t;
		money = 50;
		cities = new ArrayList<>();
	}
	public Card[] getPlants() {
		return plants;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int getMoney() {
		return money;
	}

	public void addMoney(int amt) {
		money += amt;
	}

	public void removeMoney(int amt) {
		money -= amt;
	}

	public boolean availablePlant() {
		for (Card k : plants)
			if (k == null)
				return true;
		return false;
	}
	public int nextAvailablePlant() {
		for(int i = 0; i < 3; i++)
			if(plants[i] == null)
				return i;
		return -1;
	}

	public void addPlant(Card c, int i) {
		Card rem = removePlant(i);
		plants[i] = c;
		
	}

	public Card removePlant(int i) {
		if(plants[i] == null)
			return null;
		Card c = plants[i];
		plants[i] = null;
		return c;
	}
	
	public void addCity(String n) {
		cities.add(n);
	}
	
	public int getNumCities() {
		return cities.size();
	}
	
	public int biggestPlant() {
		int big = Integer.MIN_VALUE;
		for(Card k : plants)
			big = Math.max(big, k.getNum());
		return big;
	}
	
	//FINISH LATER
	public int compareTo(Player oth) {
		if(getNumCities() == oth.getNumCities())
			return oth.biggestPlant() - biggestPlant();
		return oth.getNumCities() - getNumCities();
	}
}
