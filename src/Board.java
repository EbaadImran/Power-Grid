import java.io.IOException;

public class Board {
	private Player[] players;
	private Market market;
	private Graph map;
	private int turn;
	
	public Board() throws IOException {
		players = new Player[4];
		map = new Graph();
	}
}
