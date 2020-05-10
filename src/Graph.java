import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Graph {
    private LinkedHashMap<String, City> cities;
    public static HashMap<String, HashSet<String>> regions;

    public Graph() throws IOException {
        cities = new LinkedHashMap<>();
        regions = new HashMap<>();
        
        InputStream inputStream = Graph.class.getResourceAsStream("/cityInfo.txt");
		InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader f = new BufferedReader(inputReader);
        for (int i = 0; i < 42; i++) {
            StringTokenizer temp = new StringTokenizer(f.readLine());
            String color = temp.nextToken();
            String name = temp.nextToken();
            int minX = Integer.parseInt(temp.nextToken());
            int minY = Integer.parseInt(temp.nextToken());
            int maxX = Integer.parseInt(temp.nextToken());
            int maxY = Integer.parseInt(temp.nextToken());
            cities.put(name, new City(color, name, i, minX, minY, maxX, maxY));
        }

        for (String cityName : cities.keySet()) {
            StringTokenizer temp = new StringTokenizer(f.readLine());
            while (temp.hasMoreTokens())
                cities.get(cityName)
                        .addPath(new Path(cities.get(temp.nextToken()), Integer.parseInt(temp.nextToken())));
        }

        inputStream = Graph.class.getResourceAsStream("/regions.txt");
		inputReader = new InputStreamReader(inputStream);
        f = new BufferedReader(inputReader);
        for(int i = 0; i < 6; i++) {
            String region = f.readLine();
            regions.put(region, new HashSet<>());

            StringTokenizer temp = new StringTokenizer(f.readLine());
            while(temp.hasMoreTokens())
                regions.get(region).add(temp.nextToken());
        }
    }
    public int shortestToPlayer(int t, String c) {
        LinkedHashMap<String, Integer> dist = dijkstras(c);
        int shortest = Integer.MAX_VALUE;
        for(String k : dist.keySet()) {
            if(cities.get(k).occupantsSet().contains(t) && !k.equals(c))
                shortest = Math.min(shortest, dist.get(k));
        }
        if(shortest == Integer.MAX_VALUE)
            shortest = 0;
        return shortest;
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
    public void removeRegion(String c) {
        for(String k : cities.keySet()) {
            City city = cities.get(k);
            if(city.getColor().equals(c)) {
                city.clearEdges();
            }
            else {
                for(int i = 0; i < city.getEdges().size(); i++) {
                    Path path = city.getEdges().get(i);
                    if(path.getToCity().getColor().equals(c))
                        city.getEdges().remove(i);
                }
            }
        }
    }
    public City getCity(String name) {
        return cities.get(name);
    }
    public LinkedHashMap<String, City> getGraph() {
        return cities;
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