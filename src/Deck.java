import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class Deck {
    private ArrayList<Card> deck;
    private Card[] market;

    public Deck() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("deck.txt"));
        deck = new ArrayList<>();
        market = new Card[8];

        for (int i = 0; i < 41; i++) {
            StringTokenizer temp = new StringTokenizer(f.readLine());
            deck.add(new Card(Integer.parseInt(temp.nextToken()), Integer.parseInt(temp.nextToken()),
                    Integer.parseInt(temp.nextToken()), Integer.parseInt(temp.nextToken())));
        }

        for (int i = 0; i < 8; i++)
            market[i] = deck.remove(0);
        Collections.shuffle(deck);
        for (int i = 0; i < 4; i++)
            deck.remove((int) (Math.random() * deck.size()));

        deck.add(0, new Card(13, 5, 0, 1));
        deck.add(new Card(0, 0, 0, 0));
    }

    public boolean draw() {
        Card card = deck.remove(0);
        if (card.getNum() == 0) {
            return true;
        }
        for (int i = 0; i < 8; i++) {
            if (market[i] == null) {
                market[i] = card;
                break;
            }
        }
        Arrays.sort(market);
        return false;
    }
    
    public void discard(Card c) {
    	deck.add(c);
    }

    public void buyCard(Card c) {
        for (int i = 0; i < market.length; i++) {
            if (c.getNum() == market[i].getNum())
                market[i] = null;
        }
    }

    public Card[] getMarket() {
        return market;
    }
}