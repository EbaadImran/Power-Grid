import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Board {
	private Player[] players;
	private Market market;
	private Graph map;
	private HashSet<String> regions;
	private HashSet<String> availableRegions;
	private int turn;
	private int phase;
	private int step;
	public static final String[] TURN_COLORS = {"RED", "BLUE", "GREEN", "PURPLE"};
	
	public Board() throws IOException {
		players = new Player[4];
		for(int i = 0; i < 4; i++)
			players[i] = new Player(i);
		map = new Graph();
		regions = new HashSet<>();
		availableRegions = new HashSet<>();
		availableRegions.addAll(Graph.regions.keySet());
		market = new Market();
		turn = 0;
		phase = 0;
		step = 0;
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
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
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
	public ArrayList<Object> getGamestate() {
		ArrayList<Object> gs = new ArrayList<>();
		gs.add(players); //0
		gs.add(market); //1
		gs.add(map); //2
		gs.add(regions); //3
		gs.add(availableRegions); //4
		gs.add(turn); //5
		gs.add(phase); //6
		gs.add(step); //7
		return gs;
	}
}
