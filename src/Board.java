import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Board {
	private Player[] players;
	private Market market;
	private Graph map;
	private HashSet<String> regions;
	private HashSet<String> availableRegions;
	private HashSet<String> locked;
	private int turn;
	private int phase;
	private int step;
	public static final String[] TURN_COLORS = {"RED", "BLUE", "GREEN", "PURPLE"};
	public static final int[] PAYOUT = {10, 22, 33, 44, 54, 64, 73, 82, 90, 98, 105, 112, 118, 124, 129, 134, 138, 142, 145, 148, 150};
	
	public Board() throws IOException {
		players = new Player[4];
		for(int i = 0; i < 4; i++)
			players[i] = new Player(i);
		map = new Graph();
		regions = new HashSet<>();
		availableRegions = new HashSet<>();
		availableRegions.addAll(Graph.regions.keySet());
		locked = new HashSet<>();
		market = new Market();
		turn = 0;
		phase = 0;
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
		setAvailableRegions();
	}
	public void removeRegion(String r) {
		regions.remove(r);
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
		return gs;
	}
}
