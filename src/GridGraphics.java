import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GridGraphics extends JFrame {
	private Board board;
	private GridPanel panel;

	public GridGraphics() throws IOException {
		board = new Board();
		setGraphics();
	}

	public void setGraphics() {
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new GridPanel();
		add(panel);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (panel.getScreen() == 0) { // start screen
					panel.setScreen(1);
				} else if (panel.getScreen() == 1) { // choose regions
					if (e.getX() >= 200 && e.getY() >= 910 && e.getX() <= 250 && e.getY() <= 960
							&& board.getRegions().size() >= 1) {
						board.removeRegion();
					}
					if (e.getX() >= 370 && e.getX() <= 470) {
						int i = (e.getY() - 910) / 20;
						int s = 0;
						String ans = null;
						for (String k : board.getAvailableRegions()) {
							if (i == s) {
								ans = k;
								break;
							}
							s++;
						}
						if (ans != null)
							board.addRegion(ans);
					}
					if (e.getX() >= 1920 / 2 - 250 && e.getX() <= 1920 / 2 + 250 && e.getY() >= 1080 / 2 - 250
							&& e.getY() <= 1080 / 2 + 250 && board.getRegions().size() == 4) {
						panel.setScreen(2);
						board.lock();
					}
				} else if (panel.getScreen() == 2) { // main gui
					System.out.println(e.getX() + " " + e.getY());
					if (board.getPhase() == 3) {
						if (panel.getCityPopup() == null) {
							Graph map = board.getGraph();
							for (String k : map.getGraph().keySet()) {
								City test = map.getGraph().get(k);
								if (e.getX() >= test.getMinX() && e.getX() <= test.getMaxX()
										&& e.getY() >= test.getMinY() && e.getY() <= test.getMaxY()
										&& !board.getLocked().contains(test.getColor())) {
									panel.setCityPopup(test);
									break;
								}
							}
						} else {
							if (e.getX() >= 790 && e.getX() <= 805 && e.getY() >= 360 && e.getY() <= 375)
								panel.setCityPopup(null);
						}
					} else if (board.getPhase() == 1) {
						if (panel.getAuctionPopup() == 0 && e.getX() >= 610 && e.getY() >= 50 && e.getX() <= 768
								&& e.getY() <= 110) {
							if (board.getAuction().getHighestBid() > 0)
								panel.setAuctionPopup(3);
							else
								panel.setAuctionPopup(1);
						} else {
							if (e.getX() >= 860 && e.getX() <= 875 && e.getY() >= 310 && e.getY() <= 325)
								panel.setAuctionPopup(0);
							if (panel.getAuctionPopup() == 1) {
								if (e.getX() >= 604 && e.getY() >= 500 && e.getX() <= 704 && e.getY() <= 550) {
									board.removeFromAuction(board.getPlayers()[board.getTurn()].getTurn());
									panel.setAuctionPopup(0);
									panel.setPlayer(board.getTurn());
								}
								int i = (e.getX() - 420) / 120;
								if (i > -1 && i < 4 && e.getY() >= 360 && e.getY() <= 470
										&& board.getPlayers()[board.getTurn()]
												.getMoney() >= board.getDeck().getMarket()[i].getNum()) {
									panel.setAuctionPopup(2);
									panel.setAuctionCard(board.getDeck().getMarket()[i]);
									panel.setPrice(board.getDeck().getMarket()[i].getNum());
								}
							} else if (panel.getAuctionPopup() == 2) {
								if (e.getX() >= 575 && e.getY() >= 525 && e.getX() <= 625 && e.getY() <= 575) {
									panel.setAuctionPopup(1);
									panel.setAuctionCard(null);
								} else if (e.getX() >= 647 && e.getX() <= 667 && e.getY() >= 536 && e.getY() <= 565
										&& panel.getPrice() > panel.getAuctionCard().getNum()) {
									panel.setPrice(panel.getPrice() - 1);
								} else if (e.getX() >= 722 && e.getX() <= 746 && e.getY() >= 536 && e.getY() <= 565
										&& panel.getPrice() < board.getPlayers()[board.getTurn()].getMoney()) {
									panel.setPrice(panel.getPrice() + 1);
								} else if (e.getX() >= 770 && e.getY() >= 525 && e.getX() <= 820 && e.getY() <= 575) {
									board.getAuction().setAuction(panel.getAuctionCard(), panel.getPrice(),
											board.getTurn(), board.getAuctionOrder());
									panel.setPrice(panel.getPrice() + 1);
									panel.setAuctionPopup(3);
								}
							} else if (panel.getAuctionPopup() == 3) {
								if (e.getX() >= 575 && e.getY() >= 525 && e.getX() <= 625 && e.getY() <= 575) {
									board.getAuction().pass(board.getAuction().getTurn());
								} else if (e.getX() >= 647 && e.getX() <= 667 && e.getY() >= 536 && e.getY() <= 565
										&& panel.getPrice() > board.getAuction().getHighestBid() + 1) {
									panel.setPrice(panel.getPrice() - 1);
								} else if (e.getX() >= 722 && e.getX() <= 746 && e.getY() >= 536 && e.getY() <= 565
										&& panel.getPrice() < board.getPlayers()[board.getTurn()].getMoney()) {
									panel.setPrice(panel.getPrice() + 1);
								} else if (e.getX() >= 770 && e.getY() >= 525 && e.getX() <= 820 && e.getY() <= 575) {
									board.getAuction().bid(panel.getPrice(), board.getAuction().getTurn());
									System.out.println(board.getAuction().getOrder());
									panel.setPrice(panel.getPrice() + 1);
								}
							}
							if (panel.getAuctionPopup() == 3 && board.getAuction().checkEnd()) {
								Player winner = board.getPlayers()[board.getAuction().getHighestBidIndex()];
								if (winner.availablePlant())
									board.winAuction(winner.nextAvailablePlant());
								else {
									// TODO: MAKE DISCARD POWERPLANT
									board.winAuction(0);
								}
								// TODO: CHECK IF ALL SKIPPED
								// TODO: NO PASS ROUND 1
								panel.setAuctionPopup(0);
								panel.setAuctionCard(null);
								panel.setPlayer(board.getTurn());
							}
						}
					} else if (board.getPhase() == 2) {
						if (!panel.getResourcePopup() && e.getX() >= 610 && e.getY() >= 50 && e.getX() <= 768
								&& e.getY() <= 110) {
							panel.setResourcePopup(true);
						} else if (panel.getResourcePopup()) {
							// 860, 310, 15, 15
							if (e.getX() >= 860 && e.getY() >= 310 && e.getX() <= 875 && e.getY() <= 325) {
								panel.setResourcePopup(false);
							} else if (e.getX() >= 602 && e.getY() >= 614 && e.getX() <= 702 && e.getY() <= 645) {
								panel.setResourcePopup(false);
								board.backTurn();
								panel.setPlayer(board.getTurn());
							}
							int[] buyable = availableResource();
							if (board.getMarket().getPrice(0) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(0)
									&& buyable[0] != -1 && e.getX() >= 520 && e.getY() >= 490 && e.getX() <= 560
									&& e.getY() <= 510) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(0), board.getPlayers()[board.getTurn()].getPlants()[buyable[0]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(0));
								board.getMarket().removeResource(0, 1);
							}
							else if (board.getMarket().getPrice(1) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(1)
									&& buyable[1] != -1 && e.getX() >= 750 && e.getY() >= 490 && e.getX() <= 790
									&& e.getY() <= 510) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(1), board.getPlayers()[board.getTurn()].getPlants()[buyable[1]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(1));
								board.getMarket().removeResource(1, 1);
							}
							else if (board.getMarket().getPrice(2) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(2)
									&& buyable[2] != -1 && e.getX() >= 520 && e.getY() >= 632 && e.getX() <= 560
									&& e.getY() <= 652) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(2), board.getPlayers()[board.getTurn()].getPlants()[buyable[2]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(2));
								board.getMarket().removeResource(2, 1);
							}
							else if (board.getMarket().getPrice(3) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(3)
									&& buyable[3] != -1 && e.getX() >= 750 && e.getY() >= 632 && e.getX() <= 790
									&& e.getY() <= 652) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(3), board.getPlayers()[board.getTurn()].getPlants()[buyable[3]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(3));
								board.getMarket().removeResource(3, 1);
							}
						}
					}

					if (panel.getCityPopup() == null && panel.getAuctionPopup() == 0) { // NO POPUPS
						// 1525 + i*75, 925, 70, 70
						int i = (e.getX() - 1525) / 75;
						if (i > -1 && i < 4 && e.getY() >= 925 && e.getY() <= 995)
							panel.setPlayer(i);
					}

				}
				updateGamestate();
			}
		});
		setUndecorated(true);
		setVisible(true);
	}

	public int[] availableResource() {
		Player curr = board.getPlayers()[board.getTurn()];
		int[] rtn = new int[4];
		Arrays.fill(rtn, -1);
		for (int i = 0; i < 3; i++) {
			if (curr.getPlants()[i] != null && curr.canIBuy(curr.getPlants()[i])) {
				if(curr.getPlants()[i].getRes() != Resource.DOUBLE)
					rtn[Resource.resourceToNum(curr.getPlants()[i].getRes())] = i;
				else {
					rtn[Resource.resourceToNum(Resource.COAL)] = i;
					rtn[Resource.resourceToNum(Resource.OIL)] = i;
				}
			}
		}
		return rtn;
	}

	public void updateGamestate() {
		panel.setGamestate(board.getGamestate());
		panel.repaint();
	}

	public static void main(String[] args) throws IOException {
		new GridGraphics();
	}
}
