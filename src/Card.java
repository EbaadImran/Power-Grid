
public class Card {
	private int num;
	private Resource res;
	private int cost;
	private int maxCities;
	
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
}