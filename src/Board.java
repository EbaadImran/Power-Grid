import java.io.IOException;

public class Board {
	private Player[] players;
	private Market market;
	private Graph map;
	private int turn;
	private int phase;
	
	public Board() throws IOException {
		players = new Player[4];
		map = new Graph();
		market = new Market();
		turn = 0;
		
	}
	
	public int getTurn() {
		return turn;
	}
	public void nextTurn() {
		turn = (turn + 1) % 4;
	}
	public int getPhase() {
		return phase;
	}
	public void nextPhase() {
		phase = (phase + 1) % 5;
	}
}
