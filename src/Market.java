
public class Market {
	private int[] markets;
	private final int[] CAP = {24, 24, 24, 12, };

	public Market() {
		markets = new int[4];
	}

	public void addResource(int res, int amt) {
		markets[res] += amt;
	}
	public int getCapacity(int res) {
		return CAP[res] - markets[res];
	}
	public int getAmount(int res) {
		return markets[res];
	}
}
