import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Graph {
	private LinkedHashMap<String, City> cities;

	public static void main(String[] args) throws IOException {
		Graph g = new Graph();
		System.out.println(g);
		
		HashMap<String, Integer> shortestTest = g.dijkstras("seattle");
		System.out.println("Player 1 Distances from each City");
		for(String k : shortestTest.keySet())
			System.out.println(k + " " + shortestTest.get(k));
	}

	public Graph() throws IOException {
		cities = new LinkedHashMap<>();

		BufferedReader f = new BufferedReader(new FileReader("cityInfo.txt"));
		for (int i = 0; i < 42; i++) {
			StringTokenizer temp = new StringTokenizer(f.readLine());
			String color = temp.nextToken();
			String name = temp.nextToken();
			cities.put(name, new City(color, name, i));
		}

		for (String cityName : cities.keySet()) {
			StringTokenizer temp = new StringTokenizer(f.readLine());
			while (temp.hasMoreTokens())
				cities.get(cityName)
						.addPath(new Path(cities.get(temp.nextToken()), Integer.parseInt(temp.nextToken())));
		}
	}

	public LinkedHashMap<String, Integer> dijkstras(String src) {
		LinkedHashMap<String, Integer> dist = new LinkedHashMap<>();
		HashSet<String> vis = new HashSet<>();
		for (String k : cities.keySet())
			dist.put(k, Integer.MAX_VALUE);
		dist.put(src, 0);

		TreeMap<Integer, ArrayList<String>> pq = new TreeMap<>();
		HashMap<String, Integer> pqHelper = new HashMap<>();

		pq.put(0, new ArrayList<>());
		pq.get(0).add(src);
		pqHelper.put(src, 0);
		while (!pq.isEmpty()) {
			int smallDist = pq.firstKey();
			String id = pq.get(smallDist).remove(0);
			pqHelper.remove(id);
			if (pq.get(smallDist).isEmpty())
				pq.remove(smallDist);

			vis.add(id);

			if (smallDist > dist.get(id))
				continue;
			for (Path k : cities.get(id).getEdges()) {
				String toCity = k.getToCity().getName();
				if (!vis.contains(toCity)) {
					int altPath = dist.get(id) + k.getWeight();

					if (altPath < dist.get(toCity)) {
						dist.put(toCity, altPath);
						if (!pq.containsKey(altPath))
							pq.put(altPath, new ArrayList<>());
						if (!pqHelper.containsKey(toCity))
							pq.get(altPath).add(toCity);
						else {
							ArrayList<String> bruh = pq.get(pqHelper.get(toCity));
							bruh.remove(bruh.indexOf(toCity));
							if(bruh.isEmpty())
								pq.remove(pqHelper.get(toCity));
							pq.get(altPath).add(toCity);
						}
						pqHelper.put(toCity, altPath);
					}
				}
			}
		}

		return dist;
	}

	public City getCity(String name) {
		return cities.get(name);
	}

	public String toString() {
		String x = "";
		for(String k : cities.keySet())
		{
			x += k + ": " + cities.get(k).getEdges();
			x += "\n";
		}
		return x;
	}
}
