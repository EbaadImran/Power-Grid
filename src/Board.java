import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

public class Board {
	private Player[] players;
	private Market market;
	private Deck deck;
	private Auction auction;
	private Graph map;
	private HashSet<String> regions;
	private HashSet<String> availableRegions;
	private HashSet<String> locked;
	private Stack<String> lockOrder;
	private ArrayList<Integer> auctionOrder;
	private int turn;
	private int phase;
	private int step;
	public static final String[] TURN_COLORS = {"RED", "BLUE", "GREEN", "PURPLE"};
	public static final String[] PHASES = {"ORDER", "AUCTION", "BUYING", "BUILDING", "BUREAUCRACY"};
	public static final int[] PAYOUT = {10, 22, 33, 44, 54, 64, 73, 82, 90, 98, 105, 112, 118, 124, 129, 134, 138, 142, 145, 148, 150};
	public static final int[][] RESET_MARKET = {{5, 3, 2, 1}, {6, 4, 3, 2}, {4, 5, 4, 2}};
	
	public Board() throws IOException {
		players = new Player[4];
		for(int i = 0; i < 4; i++)
			players[i] = new Player(i);
		for(int i = 0; i < 4; i++) {
			int r = (int) (Math.random() * 4);
			Player temp = players[i];
			players[i] = players[r];
			players[r] = temp;
		}
		map = new Graph();
		deck = new Deck();
		auction = new Auction(players);
		regions = new HashSet<>();
		availableRegions = new HashSet<>();
		availableRegions.addAll(Graph.regions.keySet());
		locked = new HashSet<>();
		lockOrder = new Stack<>();
		auctionOrder = new ArrayList<>();
		market = new Market();
		turn = 0;
		phase = 0;
		nextPhase();
		step = 0;
	}
	public Graph getGraph() {
		return map;
	}
	public int getTurn() {
		return turn;
	}
	public void nextTurn() {
		turn = (turn + 1) % 4;
		if(turn == 0)
			nextPhase();
	}
	public int getPhase() {
		return phase;
	}
	public void nextPhase() {
		phase = (phase + 1) % 5;
		if(phase == 0)
			turnOrder();
		else if(phase == 1) {
			for(int i = 0; i < 4; i++)
				auctionOrder.add(players[i].getTurn());
		}
		else if(phase == 2) {
			auction.endPhase();
		}
	}
	public Player[] getPlayers() {
		return players;
	}
	public Deck getDeck() {
		return deck;
	}
	public Auction getAuction() {
		return auction;
	}
	public ArrayList<Integer> getAuctionOrder() {
		return auctionOrder;
	}
	public void removeFromAuction(int t) {
		for(int i = 0; i < auctionOrder.size(); i++)
			if(auctionOrder.get(i) == t) {
				auctionOrder.remove(i);
				break;
			}
		if(auctionOrder.size() == 0) {
			nextPhase();
		}
		else {
			for(int i = 0; i < 4; i++) {
				if(players[i].getTurn() == auctionOrder.get(0)) {
					turn = i;
				}
			}
		}
				
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public void turnOrder() {
		Arrays.sort(players);
		nextPhase();
	}
	public HashSet<String> getRegions() {
		return regions;
	}
	public void addRegion(String r) {
		regions.add(r);
		lockOrder.push(r);
		setAvailableRegions();
	}
	public void removeRegion() {
		regions.remove(lockOrder.pop());
		setAvailableRegions();
	}
	public HashSet<String> getAvailableRegions() {
		return availableRegions;
	}
	public void setAvailableRegions() {
		availableRegions = new HashSet<>();
		if(regions.size() == 0) {
			availableRegions.addAll(Graph.regions.keySet());
			return;
		}
		else if(regions.size() == 4) {
			return;
		}
		for(String k : regions)
			availableRegions.addAll(Graph.regions.get(k));
		availableRegions.removeAll(regions);
	}
	public void lock() {
		locked.addAll(Graph.regions.keySet());
		locked.removeAll(regions);
		for(String k : locked)
			map.removeRegion(k);
	}
	public HashSet<String> getLocked() {
		return locked;
	}
	public void winAuction(int slot) {
		int player = auction.getHighestBidIndex();
		int price = auction.getHighestBid();
		removeFromAuction(players[player].getTurn());
		players[player].removeMoney(price);
		players[player].addPlant(auction.getCard(), slot);
		auction.bid(0, 0);
		deck.buyCard(auction.getCard());
		deck.draw();
	}
	public boolean checkWin() {
		for(Player k : players)
			if(k.getNumCities() == 17)
				return true;
		return false;
	}
	public ArrayList<Object> getGamestate() {
		ArrayList<Object> gs = new ArrayList<>();
		gs.add(players); //0
		gs.add(market); //1
		gs.add(map); //2
		gs.add(regions); //3
		gs.add(availableRegions); //4
		gs.add(locked); //5
		gs.add(turn); //6
		gs.add(phase); //7
		gs.add(step); //8
		gs.add(deck); //9
		gs.add(auction); //10
		return gs;
	}
}
