import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Deck {
	private ArrayList<Card> deck;

	public Deck() throws IOException {
		BufferedReader f = new BufferedReader(new FileReader("cityInfo.txt"));
		deck = new ArrayList<>();
		for (int i = 0; i < 42; i++) {
			StringTokenizer temp = new StringTokenizer(f.readLine());
			deck.add(new Card(Integer.parseInt(temp.nextToken()), Integer.parseInt(temp.nextToken()),
					Integer.parseInt(temp.nextToken()), Integer.parseInt(temp.nextToken())));
		}
	}
}
