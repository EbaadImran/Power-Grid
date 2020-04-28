
public class Card implements Comparable<Card>{
	private int num;
	private Resource res;
	private int cost;
	private int maxCities;
	private int storage = 0;
	
	public Card(int n, int r, int c, int max) {
		setNum(n);
		setRes(Resource.values()[r]);
		setCost(c);
		setMaxCities(max);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Resource getRes() {
		return res;
	}

	public void setRes(Resource res) {
		this.res = res;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getMaxCities() {
		return maxCities;
	}

	public void setMaxCities(int maxCities) {
		this.maxCities = maxCities;
	}
	
	public int getStorage() {
		return storage;
	}
	public void addStorage() {
		storage += 1;
	}
	public void subtractStorage() {
		storage -= getCost();
	}

	public int compareTo(Card oth) {
		return num - oth.num;
	}
}