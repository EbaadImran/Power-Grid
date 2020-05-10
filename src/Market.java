
public class Market {
	private int[] markets;
	private final int[] CAP = { 24, 24, 24, 12 };

	public Market() {
		markets = new int[4];
		markets[0] += 24;
		markets[1] += 18;
		markets[2] += 6;
		markets[3] += 2;
	}

	public void addResource(int res, int amt) {
		markets[res] += amt;
	}
	
	public void removeResource(int res, int amt) {
		markets[res] -= amt;
	}
	
	public int getPrice(int res) {
		if(markets[res] == 0)
			return -1;
		if(res < 3) {
			return 8 - (markets[res] - 1)/3;
		}
		else if(markets[res] < 5) {
			return 10 + (4 - markets[res]) * 2;
		}
		return 13 - markets[res];
	}
	
	public int getCapacity(int res) {
		return CAP[res] - markets[res];
	}

	public int getAmount(int res) {
		return markets[res];
	}
	public void refreshMarket(int s) {
		for(int i = 0; i < 4; i++) {
			addResource(i, Board.RESET_MARKET[s][i]);
			markets[i] = Math.min(markets[i], CAP[i]);
		}
	}
}
