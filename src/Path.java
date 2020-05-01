public class Path implements Comparable {
    private int weight;
    private City toCity;

    public Path(City c, int w) {
        setToCity(c);
        weight = w;
    }

    public int compareTo(Object oth) {
        return weight - ((Path) oth).weight;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int w) {
        weight = w;
    }

    public String toString() {
        return toCity.getName() + " (" + weight + ")";
    }
}