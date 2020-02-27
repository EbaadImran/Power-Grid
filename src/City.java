import java.util.ArrayList;

public class City 
{
	private String name;
	private String color;
	private int index;
	private ArrayList<Path> paths;

	public City(String c, String n, int i) {
		setColor(c);
		setName(n);
		index = i;
		paths = new ArrayList<>();
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

	public int getNumEdges() {
		return paths.size();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
