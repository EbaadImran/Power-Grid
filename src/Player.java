import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Player implements Comparable<Player> {
	private Card[] plants;
	private int money;
	private int turn;
	private ArrayList<String> cities;

	private HashMap<Resource, Integer> Dlist = new HashMap<>();

	public Player(int t) {
		plants = new Card[3];
		turn = t;
		money = 50;
		cities = new ArrayList<>();
		Dlist.put(Resource.COAL, 0);
		Dlist.put(Resource.OIL, 0);
		Dlist.put(Resource.GARBAGE, 0);
		Dlist.put(Resource.URANIUM, 0);
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
		for (int i = 0; i < 3; i++)
			if (plants[i] == null)
				return i;
		return -1;
	}

	public void addPlant(Card c, int i) {
		Card rem = plants[i];
		plants[i] = c;
		if(rem != null) {
			int storage = rem.getStorage();
			if(rem.getRes() == Resource.DOUBLE) {
				int cAmt = 0;
				int oAmt = 0;
				for(int k = 0; k < storage; k++) {
					if(Dlist.get(Resource.COAL) > 0) {
						cAmt++;
						Dlist.put(Resource.COAL, Dlist.get(Resource.COAL) - 1);
					} else if(Dlist.get(Resource.OIL) > 0) {
						oAmt++;
						Dlist.put(Resource.OIL, Dlist.get(Resource.OIL) - 1);
					}
				}
				int k = 0;
				while(cAmt > 0 && k < 3) {
					if(addRes(Resource.COAL, plants[k]))
						cAmt--;
					else
						k++;
				}
				k = 0;
				while(oAmt > 0 && k < 3) {
					if(addRes(Resource.OIL, plants[k]))
						oAmt--;
					else
						k++;
				}
			} else {
				int k = 0;
				while(storage > 0 && k < 3) {
					if(addRes(rem.getRes(), plants[k]))
						storage--;
					else
						k++;
				}
			}
		}
	}
	
	public int[] availableResource() {
		int[] rtn = new int[4];
		Arrays.fill(rtn, -1);
		for (int i = 0; i < 3; i++) {
			if (getPlants()[i] != null && canIBuy(getPlants()[i])) {
				if (getPlants()[i].getRes() != Resource.DOUBLE)
					rtn[Resource.resourceToNum(getPlants()[i].getRes())] = i;
				else {
					rtn[Resource.resourceToNum(Resource.COAL)] = i;
					rtn[Resource.resourceToNum(Resource.OIL)] = i;
				}
			}
		}
		return rtn;
	}

	public void addCity(String n) {
		cities.add(n);
	}

	public int getNumCities() {
		return cities.size();
	}
	
	public ArrayList<String> getCities() {
		return cities;
	}

	public int biggestPlant() { 
        int big = Integer.MIN_VALUE;
        for (Card k : plants) {
            if(k != null)
                big = Math.max(big, k.getNum());
        }
        return big;
    }

	public boolean addRes(Resource res, Card c)// incase it can't store more resources it will throw a false.
	{
		if (c.getRes() != Resource.DOUBLE && res == c.getRes()) {
			if (c.getStorage() < 2 * c.getCost()) {
				c.addStorage();
				return true;
			}
		} else if (c.getRes() == Resource.DOUBLE) {
			if (res == Resource.OIL || res == Resource.COAL) {
				if (c.getStorage() < 2 * c.getCost()) {
					c.addStorage();
					Dlist.put(res, Dlist.get(res) + 1);
					return true;
				}
			}
		}
		return false;

	}

	public boolean subtractRes(Resource res)
	{
		if(res != Resource.DOUBLE) {
			for(int i = 0; i < 3; i++) {
				if(plants[i] != null && (plants[i].getRes() == res || (plants[i].getRes() == Resource.DOUBLE && (res == Resource.COAL || res == Resource.OIL))) && plants[i].getStorage() >= 1) {
					plants[i].subtractStorage();
					if(plants[i].getRes() == Resource.DOUBLE)
						if(Dlist.get(Resource.COAL) > 0)
							Dlist.put(Resource.COAL, Dlist.get(Resource.COAL) - 1);
						else if(Dlist.get(Resource.OIL) > 0)
							Dlist.put(Resource.OIL, Dlist.get(Resource.OIL) - 1);
					return true;
				}
			}
		} else {
			for(int i = 0; i < 3; i++) {
				if(plants[i] != null && (plants[i].getRes() == Resource.COAL || plants[i].getRes() == Resource.OIL || plants[i].getRes() == Resource.DOUBLE) && plants[i].getStorage() >= 1) {
					plants[i].subtractStorage();
					if(plants[i].getRes() == Resource.DOUBLE)
						if(Dlist.get(Resource.COAL) > 0)
							Dlist.put(Resource.COAL, Dlist.get(Resource.COAL) - 1);
						else if(Dlist.get(Resource.OIL) > 0)
							Dlist.put(Resource.OIL, Dlist.get(Resource.OIL) - 1);
					return true;
				}
			}
		}
		return false;
	}

	public HashMap<Resource, Integer> showRes() {
		Resource coal = Resource.COAL;
		Resource oil = Resource.OIL;
		HashMap<Resource, Integer> list = new HashMap<>();
		list.put(Resource.COAL, 0);
		list.put(Resource.OIL, 0);
		list.put(Resource.GARBAGE, 0);
		list.put(Resource.URANIUM, 0);
		for (int x = 0; x < plants.length; x++) {
			if (plants[x] != null && plants[x].getRes() != Resource.DOUBLE) {
				list.put(plants[x].getRes(), plants[x].getStorage());// gets current storage of all plants(non hybrid)
			}
		}
		list.put(coal, Dlist.get(coal) + list.get(coal));// adds the resources that are held in hybrid plants
		list.put(oil, Dlist.get(oil) + list.get(oil));

		return list;
	}

	public boolean canIBuy(Card c)// can you buy specific to a plant
	{
		if (c.getStorage() == 2 * c.getCost()) {
			return false;
		}
		return true;
	}

	public int compareTo(Player oth) {
		if (getNumCities() == oth.getNumCities())
			return oth.biggestPlant() - biggestPlant();
		return oth.getNumCities() - getNumCities();
	}
}

class WinSort implements Comparable<WinSort> {
	private int citiesPowered;
	private int money;
	private int turn;
	
	public WinSort(int c, int m, int t) {
		setCitiesPowered(c);
		setMoney(m);
		setTurn(t);
	}
	
	public int compareTo(WinSort oth) {
		if(citiesPowered == oth.citiesPowered)
			return oth.money - money;
		return oth.citiesPowered - citiesPowered;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCitiesPowered() {
		return citiesPowered;
	}

	public void setCitiesPowered(int citiesPowered) {
		this.citiesPowered = citiesPowered;
	}
}
