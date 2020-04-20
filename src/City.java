import java.util.ArrayList;
import java.util.Arrays;

public class City 
{
	private String name;
	private String color;
	private int index;
	private int minX, minY, maxX, maxY;
	private int[] occupants;
	private ArrayList<Path> paths;

	public City(String c, String n, int i, int lx, int ly, int rx, int ry) {
		setColor(c);
		setName(n);
		setIndex(i);
		setMinX(lx);
		setMinY(ly);
		setMaxX(rx);
		setMaxY(ry);
		occupants = new int[3];
		Arrays.fill(occupants, -1);
		paths = new ArrayList<>();
	}
	public int[] occupants() {
		return occupants;
	}
	
	public void occupy(int t) {
		for(int i = 0; i < 3; i++) {
			if(occupants[i] == -1) {
				occupants[i] = t;
				break;
			}
		}
	}
	public void addPath(Path path) {
		paths.add(path);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Path> getEdges() {
		return paths;
	}
	
	public void clearEdges() {
		paths = new ArrayList<>();
	}

	public int getNumEdges() {
		return paths.size();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	
}
