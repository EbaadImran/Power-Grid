import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Deck {
	private ArrayList<Card> deck;
	private Card[] market;

	public Deck() throws IOException {
		BufferedReader f = new BufferedReader(new FileReader("cityInfo.txt"));
		deck = new ArrayList<>();
		market = new Card[8];
		
		for (int i = 0; i < 42; i++) {
			StringTokenizer temp = new StringTokenizer(f.readLine());
			deck.add(new Card(Integer.parseInt(temp.nextToken()), Integer.parseInt(temp.nextToken()),
					Integer.parseInt(temp.nextToken()), Integer.parseInt(temp.nextToken())));
		}
		for (int i = 0; i < 8; i++)
			market[i] = deck.remove(0);
	}
}
