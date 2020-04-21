import java.util.ArrayList;
import java.util.HashSet;

public class Auction {
	private Card auctionCard;
	private int highestBid;
	private int highestBidIndex;
	private int turn;
	private HashSet<Integer> participants;
	private ArrayList<Integer> order;

	public Auction() {
		auctionCard = null;
		highestBid = 0;
		highestBidIndex = -1;
		turn = -1;
		participants = new HashSet<>();
		for (int i = 0; i < 4; i++)
			participants.add(i);
		order = null;
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
		order = o;
		for(int k = order.size() - 1; k >= 0; k--)
			if(!participants.contains(order.get(k)))
				order.remove(k);
		for(int k = 0; k < order.size(); k++)
			if(order.get(k) == i)
				turn = k;
		bid(bid, i);
	}
	
	public void bid(int bid, int i) {
		highestBid = bid;
		highestBidIndex = i;
		nextTurn();
	}
	
	public void nextTurn() {
		turn = (turn + 1) % order.size();
	}

	public void pass(int t) {
		for(int i = 0; i < order.size(); i++)
			if(order.get(i) == t) {
				order.remove(i);
				return;
			}	
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
