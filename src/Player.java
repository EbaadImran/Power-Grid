
public class Player implements Comparable<Player> {
	private Card[] plants;
	private int[] currStorage;
	private int[] maxStorage;
	private int money;
	private int turn;

	public Player(int t) {
		plants = new Card[3];
		currStorage = new int[4];
		maxStorage = new int[4];
		turn = t;
		money = 50;
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

	public void addPlant(Card c, int i) {
		Card rem = removePlant(i);
		plants[i] = c;
		if(rem != null)
			maxStorage[Resource.resourceToNum(rem.getRes())] -= rem.getCost() * 2;
		maxStorage[Resource.resourceToNum(c.getRes())] += c.getCost() * 2;
		currStorage[Resource.resourceToNum(rem.getRes())] = Math.min(currStorage[Resource.resourceToNum(rem.getRes())], maxStorage[Resource.resourceToNum(rem.getRes())]);
	}

	public Card removePlant(int i) {
		if(plants[i] == null)
			return null;
		Card c = plants[i];
		plants[i] = null;
		return c;
	}
	
	//FINISH LATER
	public int compareTo(Player oth) {
		return 0;
	}
}
