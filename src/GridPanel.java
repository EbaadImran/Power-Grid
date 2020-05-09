import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
	private ArrayList<Object> gs;
	private int screen;
	private HashMap<String, Color> colors;
	private City cityPopup;
	private Card auctionCard;
	private int auctionPopup;
	private int auctionPrice;
	private int viewingPlayer;
	private boolean resourcePopup;
	private boolean moneyPopup;
	private int citiesPowered;

	public GridPanel() {
		gs = new ArrayList<Object>();
		setSize(1920, 1080);
		screen = 0;
		cityPopup = null;
		auctionPopup = 0;
		resourcePopup = false;
		moneyPopup = false;
		citiesPowered = -1;
		colors = new HashMap<>();
		colors.put("PURPLE", new Color(148, 105, 125));
		colors.put("BLUE", new Color(93, 145, 144));
		colors.put("RED", new Color(183, 98, 93));
		colors.put("BROWN", new Color(161, 137, 105));
		colors.put("YELLOW", new Color(196, 154, 30));
		colors.put("GREEN", new Color(132, 155, 48));
		colors.put("pri", new Color(25, 25, 25));
		colors.put("sec", new Color(241, 205, 67));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (screen == 0) {
			g.drawImage(new ImageIcon("powergrid.PNG").getImage(), 0, 0, 1920, 1080, null);
			g.setFont(new Font("Courier", Font.BOLD, 50));
			g.setColor(colors.get("pri"));
			g.drawString("CLICK TO CONTINUE", 1350, 1040);
		} else if (screen == 1) {
			g.drawImage(new ImageIcon("map.png").getImage(), 0, 0, 1920, 1080, null);
			HashSet<String> regions = (HashSet<String>) gs.get(3);
			HashSet<String> availableRegions = (HashSet<String>) gs.get(4);
			g.setFont(new Font("Courier", Font.BOLD | Font.ITALIC, 50));
			g.setColor(colors.get("pri"));
			g.drawString("Selected:", 50, 900);
			g.drawString("Choose:", 350, 900);

			int y = 925;
			g.setFont(new Font("Courier", Font.BOLD, 25));
			for (String k : availableRegions) {
				g.setColor(colors.get(k));
				g.drawString(k, 375, y);
				y += 20;
			}

			y = 925;
			g.setFont(new Font("Courier", Font.BOLD, 25));
			for (String k : regions) {
				g.setColor(colors.get(k));
				g.drawString(k, 75, y);
				y += 20;
			}

			if (regions.size() >= 1)
				g.drawImage(new ImageIcon("undo.png").getImage(), 200, 910, 50, 50, null);

			if (regions.size() == 4) {
				g.drawImage(new ImageIcon("lock.png").getImage(), 1920 / 2 - 250, 1080 / 2 - 250, 500, 500, null);
				g.setColor(colors.get("pri"));
				g.setFont(new Font("Courier", Font.BOLD, 50));
				g.drawString("LOCK IN", 1920 / 2 - 100, 1080 / 2 + 160);
			}
		} else if (screen == 2) {
			g.drawImage(new ImageIcon("gui.png").getImage(), 0, 0, 1920, 1080, null);

			Player[] players = (Player[]) gs.get(0);
			Market market = (Market) gs.get(1);
			Graph map = (Graph) gs.get(2);
			Deck deck = (Deck) gs.get(9);
			Auction auction = (Auction) gs.get(10);
			Card[] plants = deck.getMarket();
			HashSet<String> locked = (HashSet<String>) gs.get(5);
			int step = (int) gs.get(8);
			int phase = (int) gs.get(7);
			int turn = (int) gs.get(6);
			int round = (int) gs.get(11);
			int moneyOnGUI = players[viewingPlayer].getMoney();
			int currMoney = players[turn].getMoney();

			if (phase == 1) {
				g.setColor(colors.get("sec"));
				g.fillRect(610, 50, 158, 60);
				g.setColor(colors.get("pri"));
				g2.setStroke(new BasicStroke(7));
				g2.drawRect(610, 50, 158, 60);
				g.setFont(new Font("Courier", Font.BOLD, 18));
				g.drawString("GO TO AUCTION", 620, 85);

			} else if (phase == 2) {
				g.setColor(colors.get("sec"));
				g.fillRect(610, 50, 158, 60);
				g.setColor(colors.get("pri"));
				g2.setStroke(new BasicStroke(7));
				g2.drawRect(610, 50, 158, 60);
				g.setFont(new Font("Courier", Font.BOLD, 18));
				g.drawString("GO TO MARKET", 622, 85);
			} else if (phase == 3) {
				g.setColor(colors.get("sec"));
				g.fillRect(610, 50, 155, 60);
				g.setColor(colors.get("pri"));
				g2.setStroke(new BasicStroke(7));
				g2.drawRect(610, 50, 155, 60);
				g.setFont(new Font("Courier", Font.BOLD, 18));
				g.drawString("END BUILDING", 622, 85);
			} else if (phase == 4) {
				g.setColor(colors.get("sec"));
				g.fillRect(610, 50, 155, 60);
				g.setColor(colors.get("pri"));
				g2.setStroke(new BasicStroke(7));
				g2.drawRect(610, 50, 155, 60);
				g.setFont(new Font("Courier", Font.BOLD, 18));
				g.drawString("POWER CITIES", 622, 85);
			}

			for (String k : map.getGraph().keySet()) {
				City c = map.getGraph().get(k);
				if (locked.contains(c.getColor())) {
					g.drawImage(new ImageIcon("cloud.png").getImage(), c.getMinX() - 10, c.getMinY(), 83, 50, null);
				}
				else {
					int[] occ = c.occupants();
					for(int i = 0; i < 3; i++) {
						if(occ[i] != -1) {
							g.setColor(colors.get(Board.TURN_COLORS[occ[i]]));
							g.fillOval(c.getMinX() + 10 + i*16, c.getMinY() + 35, 10, 10);
						}
					}
				}
			}

			for (int i = 0; i < 24; i++) {
				g.setColor(new Color(180, 108, 2));
				g.fillRect(50 + (i / 3) * 24 + i * 45, 940, 30, 30);
				g.setColor(Color.DARK_GRAY);
				g.fillRect(50 + (i / 3) * 69 + i * 30, 972, 28, 28);
				g.setColor(Color.YELLOW);
				g.fillRect(50 + (i / 3) * 24 + i * 45, 1002, 30, 30);
			}
			for (int i = 0; i < 8; i++) {
				g.setColor(Color.RED);
				g.fillRect(140 + i * 159, 972, 28, 28);
			}

			for (int i = 24 - market.getAmount(0); i < 24; i++) {
				g.setColor(new Color(125, 74, 0));
				g.fillOval(50 + (i / 3) * 24 + i * 45, 940, 30, 30);
			}
			for (int i = 24 - market.getAmount(1); i < 24; i++) {
				g.setColor(Color.BLACK);
				g.fillOval(50 + (i / 3) * 69 + i * 30, 972, 28, 28);
			}
			for (int i = 24 - market.getAmount(2); i < 24; i++) {
				g.setColor(new Color(160, 160, 20));
				g.fillOval(50 + (i / 3) * 24 + i * 45, 1002, 30, 30);
			}
			for (int i = 12 - (market.getAmount(3)); i < 8; i++) {
				g.setColor(new Color(132, 15, 15));
				g.fillOval(140 + i * 159, 972, 28, 28);
			}

			g.setColor(new Color(132, 15, 15));
			if (market.getAmount(3) >= 4)
				g.fillOval(1342, 949, 25, 25);
			if (market.getAmount(3) >= 3)
				g.fillOval(1405, 949, 25, 25);
			if (market.getAmount(3) >= 2)
				g.fillOval(1342, 999, 25, 25);
			if (market.getAmount(3) >= 1)
				g.fillOval(1405, 999, 25, 25);

			if (cityPopup != null) {
				g.drawImage(new ImageIcon("cityinfo.png").getImage(), 490, 350, 329, 255, null);
				g.drawImage(new ImageIcon("x.png").getImage(), 790, 360, 15, 15, null);

				String name = cityPopup.getName().replaceAll("_", " ").toUpperCase();
				g.setColor(colors.get("pri"));
				g.setFont(new Font("Courier", Font.BOLD, 35));
				g.drawString(name, 510, 450);

				int dist = map.shortestToPlayer(players[turn].getTurn(), cityPopup.getName());
				int price = dist + (cityPopup.nextAvailableSpot() + 2) * 5;
				HashSet<Integer> occ = cityPopup.occupantsSet();

				g.setColor(colors.get(Board.TURN_COLORS[players[turn].getTurn()]));
				g.setFont(new Font("Courier", Font.BOLD, 15));
				g.drawString(cityPopup.nextAvailableSpot() <= step && !occ.contains(players[turn].getTurn())
						? "$" + price + " (Distance: " + dist + ")"
						: "UNAVAILABLE", 620, 498);
				String displayOcc = "";
				for (int k : occ)
					displayOcc += Board.TURN_COLORS[k];
				if (displayOcc.equals(""))
					displayOcc = "NONE";
				g.drawString(displayOcc, 700, 525);
				if (cityPopup.nextAvailableSpot() <= step && !occ.contains(players[turn].getTurn())
						&& players[turn].getMoney() >= price)
					g.drawImage(new ImageIcon("buy.png").getImage(), 600, 540, 100, 50, null);
			} else if (auctionPopup >= 1) {
				g.drawImage(new ImageIcon("auction.png").getImage(), 408, 286, 493, 382, null);
				g.drawImage(new ImageIcon("x.png").getImage(), 860, 310, 15, 15, null);
				for (int i = 0; i < 4; i++) {
					g.drawImage(new ImageIcon("" + plants[i].getNum() + ".png").getImage(), i * 120 + 420, 360, 110,
							110, null);
				}
				if (auctionPopup == 1 && round > 0) {
					g.setColor(colors.get("pri"));
					g.fillOval(604, 500, 100, 50);
					g.setColor(colors.get("sec"));
					g.setFont(new Font("Courier", Font.BOLD, 25));
					g.drawString("PASS", 623, 530);
				} else if (auctionPopup == 2) {
					g.drawImage(new ImageIcon("" + auctionCard.getNum() + ".png").getImage(), 450, 500, 100, 100, null);
					g.drawImage(new ImageIcon("undo.png").getImage(), 575, 525, 50, 50, null);
					g.setColor(colors.get("pri"));
					g.setFont(new Font("Courier", Font.BOLD, 50));
					g.drawString("-", 640, 565);
					g.setFont(new Font("Courier", Font.BOLD, 25));
					g.drawString("" + auctionPrice, 680, 558);
					g.setFont(new Font("Courier", Font.BOLD, 50));
					g.drawString("+", 720, 565);
					g.drawImage(new ImageIcon("confirm.png").getImage(), 770, 525, 50, 50, null);
				} else if (auctionPopup == 3) {
					g.drawImage(new ImageIcon("" + auctionCard.getNum() + ".png").getImage(), 450, 500, 100, 100, null);
					g.drawImage(new ImageIcon("pass.png").getImage(), 575, 525, 50, 50, null);
					g.setColor(colors.get("pri"));
					g.setFont(new Font("Courier", Font.BOLD, 50));
					g.drawString("-", 640, 565);
					g.setFont(new Font("Courier", Font.BOLD, 25));
					g.drawString("" + auctionPrice, 680, 558);
					g.setFont(new Font("Courier", Font.BOLD, 50));
					g.drawString("+", 720, 565);
					g.drawImage(new ImageIcon("confirm.png").getImage(), 770, 525, 50, 50, null);

					int highestBid = auction.getHighestBid();
					int highestBidIndex = auction.getHighestBidIndex();
					String auctionColor = Board.TURN_COLORS[auction.getOrder().get(auction.getTurn())];
					g.setFont(new Font("Courier", Font.BOLD, 25));
					g.setColor(colors.get("pri"));
					g.drawString("Highest Bid: ", 575, 600);
					g.drawString("Auction Turn: ", 575, 640);
					g.setColor(colors.get(Board.TURN_COLORS[players[highestBidIndex].getTurn()]));
					g.drawString("" + highestBid, 760, 600);
					g.setColor(colors.get(auctionColor));
					g.drawString(auctionColor, 775, 640);
				}
			} else if (resourcePopup) {
				g.drawImage(new ImageIcon("resourceMarket.png").getImage(), 408, 286, 493, 382, null);
				g.drawImage(new ImageIcon("x.png").getImage(), 860, 310, 15, 15, null);
				g.setColor(colors.get("pri"));
				g.setFont(new Font("Courier", Font.BOLD, 25));
				String s0 = market.getPrice(0) == -1 ? "N/A" : "$" + market.getPrice(0);
				g.drawString(s0, 520, 380);
				String s1 = market.getPrice(1) == -1 ? "N/A" : "$" + market.getPrice(1);
				g.drawString(s1, 750, 380);
				String s2 = market.getPrice(2) == -1 ? "N/A" : "$" + market.getPrice(2);
				g.drawString(s2, 520, 532);
				String s3 = market.getPrice(3) == -1 ? "N/A" : "$" + market.getPrice(3);
				g.drawString(s3, 750, 532);
				int[] buyable = availableResource(players[turn]);
				if (market.getPrice(0) != -1 && players[turn].getMoney() >= market.getPrice(0) && buyable[0] != -1)
					g.drawImage(new ImageIcon("buy.png").getImage(), 520, 490, 40, 20, null);
				if (market.getPrice(1) != -1 && players[turn].getMoney() >= market.getPrice(1) && buyable[1] != -1)
					g.drawImage(new ImageIcon("buy.png").getImage(), 750, 490, 40, 20, null);
				if (market.getPrice(2) != -1 && players[turn].getMoney() >= market.getPrice(2) && buyable[2] != -1)
					g.drawImage(new ImageIcon("buy.png").getImage(), 520, 632, 40, 20, null);
				if (market.getPrice(3) != -1 && players[turn].getMoney() >= market.getPrice(3) && buyable[3] != -1)
					g.drawImage(new ImageIcon("buy.png").getImage(), 750, 632, 40, 20, null);
				g.setColor(colors.get(Board.TURN_COLORS[players[turn].getTurn()]));
				g.drawString(players[turn].getMoney() + " ELEKTROS", 572, 513);
			} else if (moneyPopup) { // finish later
				g.drawImage(new ImageIcon("bureaucracy.png").getImage(), 408, 286, 493, 382, null);
				g.drawImage(new ImageIcon("x.png").getImage(), 860, 310, 15, 15, null);
				for (int i = 0; i < 3; i++) {
					Card currCard = players[turn].getPlants()[i];
					if (currCard != null) {
						g.drawImage(new ImageIcon("" + currCard.getNum() + ".png").getImage(), 500 + i * 110, 400, 75,
								75, null);
						if (currCard.getRes() != Resource.DOUBLE
								&& players[turn].showRes().get(currCard.getRes()) < currCard.getCost()
								|| players[turn].getNumCities() - citiesPowered == 0) {
							g.drawImage(new ImageIcon("x.png").getImage(), 500 + i * 110, 400, 75, 75, null);
						} else if (currCard.getRes() == Resource.DOUBLE && players[turn].showRes().get(Resource.COAL)
								+ players[turn].showRes().get(Resource.OIL) < currCard.getCost()
								|| players[turn].getNumCities() - citiesPowered == 0) {
							g.drawImage(new ImageIcon("x.png").getImage(), 500 + i * 110, 400, 75, 75, null);
						}
					} else {
						g.drawImage(new ImageIcon("empty.png").getImage(), 500 + i * 110, 400, 75, 75, null);
					}
				}
				g.setColor(colors.get(Board.TURN_COLORS[players[turn].getTurn()]));
				g.drawString("" + (players[turn].getNumCities() - citiesPowered), 695, 500);
				g.drawString("" + citiesPowered, 692, 522);
				g.drawString("x" + players[turn].showRes().get(Resource.COAL), 508, 575);
				g.drawString("x" + players[turn].showRes().get(Resource.OIL), 602, 575);
				g.drawString("x" + players[turn].showRes().get(Resource.GARBAGE), 717, 575);
				g.drawString("x" + players[turn].showRes().get(Resource.URANIUM), 825, 575);
			}

			for (int i = 0; i < 4; i++) {
				g.drawImage(new ImageIcon("" + plants[i].getNum() + ".png").getImage(), i * 130 + 1360, 50, 125, 125,
						null);
				g.drawImage(new ImageIcon("" + plants[i + 4].getNum() + ".png").getImage(), i * 130 + 1360, 180, 125,
						125, null);
			}

			g.setColor(colors.get(Board.TURN_COLORS[players[turn].getTurn()]));
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawString(Board.TURN_COLORS[players[turn].getTurn()], 1575, 470);
			g.drawString("" + (step + 1), 1780, 470);
			g.drawString(Board.PHASES[phase], 1478, 527);

			for (int i = 0; i < 4; i++)
				g.drawImage(new ImageIcon(Board.TURN_COLORS[players[i].getTurn()] + ".png").getImage(), 1525 + i * 75,
						925, 70, 70, null);

			Player display = players[viewingPlayer];
			for (int i = 0; i < 3; i++) {
				Card c = display.getPlants()[i];
				if (c != null)
					g.drawImage(new ImageIcon("" + c.getNum() + ".png").getImage(), 1400 + i * 150, 765, 140, 140,
							null);
				else
					g.drawImage(new ImageIcon("empty.png").getImage(), 1400 + i * 150, 765, 140, 140, null);
			}

			g.setColor(colors.get(Board.TURN_COLORS[display.getTurn()]));
			g.setFont(new Font("Courier", Font.BOLD, 30));
			g.drawString(center(Board.TURN_COLORS[display.getTurn()]), 1685, 600);
			g.drawString("" + moneyOnGUI + " ELEKTROS", 1365, 610);
			g.setFont(new Font("Courier", Font.BOLD, 18));
			String cities = players[viewingPlayer].getNumCities() > 0
					? players[viewingPlayer].getCities().toString().toUpperCase().replace("_", " ").substring(1,
							players[viewingPlayer].getCities().toString().length() - 1)
					: "NONE";
			g.drawString("Cities: " + cities, 1365, 650);
			g.setFont(new Font("Courier", Font.BOLD, 30));
			for (int i = 0; i < 4; i++) {
				g.drawString("x" + players[viewingPlayer].showRes().get(Resource.numToResource(i)), 1450 + i * 122,
						728);
			}
		}
	}

	public int[] availableResource(Player curr) {
		int[] rtn = new int[4];
		Arrays.fill(rtn, -1);
		for (int i = 0; i < 3; i++) {
			if (curr.getPlants()[i] != null && curr.canIBuy(curr.getPlants()[i])) {
				if (curr.getPlants()[i].getRes() != Resource.DOUBLE)
					rtn[Resource.resourceToNum(curr.getPlants()[i].getRes())] = i;
				else {
					rtn[Resource.resourceToNum(Resource.COAL)] = i;
					rtn[Resource.resourceToNum(Resource.OIL)] = i;
				}
			}
		}
		return rtn;
	}

	public void setGamestate(ArrayList<Object> g) {
		gs = g;
	}

	public int getScreen() {
		return screen;
	}

	public void setScreen(int s) {
		screen = s;
	}

	public void setCityPopup(City c) {
		cityPopup = c;
	}

	public City getCityPopup() {
		return cityPopup;
	}

	public void setAuctionPopup(int phase) {
		auctionPopup = phase;
	}

	public int getAuctionPopup() {
		return auctionPopup;
	}

	public void setAuctionCard(Card c) {
		auctionCard = c;
	}

	public Card getAuctionCard() {
		return auctionCard;
	}

	public void setPrice(int p) {
		auctionPrice = p;
	}

	public int getPrice() {
		return auctionPrice;
	}

	public void setPlayer(int p) {
		viewingPlayer = p;
	}

	public int getPlayer() {
		return viewingPlayer;
	}

	public void setResourcePopup(boolean b) {
		resourcePopup = b;
	}

	public boolean getResourcePopup() {
		return resourcePopup;
	}

	public void setMoneyPopup(boolean b) {
		moneyPopup = b;
	}

	public boolean getMoneyPopup() {
		return moneyPopup;
	}

	public void addCitiesPowered(int a) {
		citiesPowered += a;
	}

	public void setCitiesPowered(int a) {
		citiesPowered = a;
	}

	public int getCitiesPowered() {
		return citiesPowered;
	}

	public String center(String col) {
		int amtC = (8 - col.length()) / 2;
		for (int i = 0; i < amtC; i++)
			col = " " + col;
		return col;
	}
}
