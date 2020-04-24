import java.util.ArrayList;
import java.util.HashSet;

public class Auction {
	private Card auctionCard;
	private int highestBid;
	private int highestBidIndex;
	private int turn;
	private HashSet<Integer> participants;
	private ArrayList<Integer> order;
	private Player[] players;

	public Auction(Player[] p) {
		auctionCard = null;
		highestBid = 0;
		highestBidIndex = -1;
		turn = -1;
		participants = new HashSet<>();
		for (int i = 0; i < 4; i++)
			participants.add(i);
		order = null;
		players = p;
	}
	public ArrayList<Integer> getOrder() {
		return order;
	}
	public Card getCard() {
		return auctionCard;
	}
	
	public int getHighestBid() {
		return highestBid;
	}
	
	public int getHighestBidIndex() {
		return highestBidIndex;
	}
	
	public int getTurn() {
		return turn;
	}

	public void setAuction(Card c, int bid, int i, ArrayList<Integer> o) {
		auctionCard = c;
		order = new ArrayList<>();
		for(int k : o)
			order.add(k);
		for(int k = order.size() - 1; k >= 0; k--)
			if(!participants.contains(order.get(k)))
				order.remove(k);
		turn = i;
		bid(bid, order.indexOf(players[i].getTurn()));
	}
	
	public void bid(int bid, int i) {
		highestBid = bid;
		for(int k = 0; k < players.length; k++) {
			if(players[k].getTurn() == order.get(i))
				highestBidIndex = k;
		}
		nextTurn();
	}
	
	public void nextTurn() {
		turn = (turn + 1) % order.size();
		while(players[turn].getMoney() <= highestBid) {
			pass(turn);
		}
	}

	public void pass(int t) {
		order.remove(t);
		turn %= order.size();
	}
	
	public boolean checkEnd() {
		return order.size() == 1;
	}
	
	public void removeParticipant(int t) {
		participants.remove(t);
	}
	
	public void endPhase() {
		auctionCard = null;
		highestBid = 0;
		highestBidIndex = -1;
		turn = -1;
		participants = new HashSet<>();
		for (int i = 0; i < 4; i++)
			participants.add(i);
		order = null;
	}
}
