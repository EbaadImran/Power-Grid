import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class GridGraphics extends JFrame {
	private Board board;
	private GridPanel panel;

	public GridGraphics() throws IOException {
		board = new Board();
		setGraphics();

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"Cancel");
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
	}

	public void setGraphics() {
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new GridPanel();
		add(panel);
		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
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
					if (board.getPhase() == 1) {
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
								if (e.getX() >= 604 && e.getY() >= 500 && e.getX() <= 704 && e.getY() <= 550
										&& board.getRound() > 0) {
									board.getAuction().increasePass();
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
								} else if (board.getStep() == 2 && e.getX() >= 420 && e.getX() <= 530 && e.getY() >= 480
										&& e.getY() <= 590 && board.getPlayers()[board.getTurn()]
												.getMoney() >= board.getDeck().getMarket()[4].getNum()) {
									panel.setAuctionPopup(2);
									panel.setAuctionCard(board.getDeck().getMarket()[4]);
									panel.setPrice(board.getDeck().getMarket()[4].getNum());
								} else if (board.getStep() == 2 && e.getX() >= 780 && e.getX() <= 890 && e.getY() >= 480
										&& e.getY() <= 590 && board.getPlayers()[board.getTurn()]
												.getMoney() >= board.getDeck().getMarket()[5].getNum()) {
									panel.setAuctionPopup(2);
									panel.setAuctionCard(board.getDeck().getMarket()[5]);
									panel.setPrice(board.getDeck().getMarket()[5].getNum());
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
									panel.setPrice(panel.getPrice() + 1);
								}
							}
							if (panel.getAuctionPopup() == 3 && board.getAuction().checkEnd()) {
								Player winner = board.getPlayers()[board.getAuction().getHighestBidIndex()];
								if (winner.availablePlant()) {
									board.winAuction(winner.nextAvailablePlant());
									panel.setAuctionPopup(0);
									panel.setAuctionCard(null);
									panel.setPlayer(board.getTurn());
								} else {
									panel.setAuctionPopup(4);
								}
							} else if (panel.getAuctionPopup() == 4) {
								int i = (e.getX() - 580) / 105;
								if (i >= 0 && i <= 2 && e.getY() >= 500 && e.getY() <= 600) {
									board.winAuction(i);
									panel.setAuctionPopup(0);
									panel.setAuctionCard(null);
									panel.setPlayer(board.getTurn());
								}
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
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(0),
										board.getPlayers()[board.getTurn()].getPlants()[buyable[0]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(0));
								board.getMarket().removeResource(0, 1);
							} else if (board.getMarket().getPrice(1) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(1)
									&& buyable[1] != -1 && e.getX() >= 750 && e.getY() >= 490 && e.getX() <= 790
									&& e.getY() <= 510) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(1),
										board.getPlayers()[board.getTurn()].getPlants()[buyable[1]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(1));
								board.getMarket().removeResource(1, 1);
							} else if (board.getMarket().getPrice(2) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(2)
									&& buyable[2] != -1 && e.getX() >= 520 && e.getY() >= 632 && e.getX() <= 560
									&& e.getY() <= 652) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(2),
										board.getPlayers()[board.getTurn()].getPlants()[buyable[2]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(2));
								board.getMarket().removeResource(2, 1);
							} else if (board.getMarket().getPrice(3) != -1
									&& board.getPlayers()[board.getTurn()].getMoney() >= board.getMarket().getPrice(3)
									&& buyable[3] != -1 && e.getX() >= 750 && e.getY() >= 632 && e.getX() <= 790
									&& e.getY() <= 652) {
								board.getPlayers()[board.getTurn()].addRes(Resource.numToResource(3),
										board.getPlayers()[board.getTurn()].getPlants()[buyable[3]]);
								board.getPlayers()[board.getTurn()].removeMoney(board.getMarket().getPrice(3));
								board.getMarket().removeResource(3, 1);
							}
						}
					} else if (board.getPhase() == 3) {
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
							if (e.getX() >= 610 && e.getY() >= 50 && e.getX() <= 765 && e.getY() <= 110) {
								board.backTurn();
								panel.setPlayer(board.getTurn());
							}

						} else {
							int dist = board.getGraph().shortestToPlayer(board.getPlayers()[board.getTurn()].getTurn(),
									panel.getCityPopup().getName());
							int price = dist + (panel.getCityPopup().nextAvailableSpot() + 2) * 5;
							if (e.getX() >= 790 && e.getX() <= 805 && e.getY() >= 360 && e.getY() <= 375)
								panel.setCityPopup(null);
							else if (panel.getCityPopup().nextAvailableSpot() <= board.getStep()
									&& !panel.getCityPopup().occupantsSet()
											.contains(board.getPlayers()[board.getTurn()].getTurn())
									&& board.getPlayers()[board.getTurn()].getMoney() >= price && e.getX() >= 600
									&& e.getY() >= 540 && e.getX() <= 700 && e.getY() <= 590) {
								board.getPlayers()[board.getTurn()].addCity(panel.getCityPopup().getName());
								board.getPlayers()[board.getTurn()].removeMoney(price);
								panel.getCityPopup().occupy(board.getPlayers()[board.getTurn()].getTurn());
								panel.setCityPopup(null);
								if (board.getStep() == 0 && board.getPlayers()[board.getTurn()].getNumCities() == 7)
									board.setStep(1);
								else if (board.getPlayers()[board.getTurn()].getNumCities() == 17) {
									board.winPhase();
								}
							}
						}
					} else if (board.getPhase() == 4) {
						if (!panel.getMoneyPopup() && e.getX() >= 610 && e.getY() >= 50 && e.getX() <= 765
								&& e.getY() <= 110) {
							panel.setCitiesPowered(0);
							panel.setMoneyPopup(true);
						} else if (panel.getMoneyPopup()) {
							if (e.getX() >= 860 && e.getX() <= 875 && e.getY() >= 310 && e.getY() <= 325) {
								panel.setMoneyPopup(false);
							} else if (e.getX() >= 602 && e.getY() >= 614 && e.getX() <= 702 && e.getY() <= 645) {
								for(Card c : board.getPlayers()[board.getTurn()].getPlants())
									if(c != null)
										c.activate(false);
								board.getPlayers()[board.getTurn()]
										.addMoney(Board.PAYOUT[Math.min(20, panel.getCitiesPowered())]);
								panel.setMoneyPopup(false);
								panel.setCitiesPowered(-1);
								board.frontTurn();
								panel.setPlayer(board.getTurn());
							}
							// 500 + i*110, 400, 75, 75
							int i = (e.getX() - 500) / 110;
							if (i >= 0 && i <= 2 && e.getY() >= 400 && e.getY() <= 475) {
								Card c = board.getPlayers()[board.getTurn()].getPlants()[i];
								if (!c.activated() && c.getRes() != Resource.DOUBLE
										&& board.getPlayers()[board.getTurn()].showRes().get(c.getRes()) >= c.getCost()
										&& board.getPlayers()[board.getTurn()].getNumCities()
												- panel.getCitiesPowered() > 0) {
									panel.addCitiesPowered(Math.min(c.getMaxCities(),
											board.getPlayers()[board.getTurn()].getNumCities()
													- panel.getCitiesPowered()));
									int price = c.getCost();
									c.activate(true);
									for (int k = 0; k < price; k++) {
										board.getPlayers()[board.getTurn()].subtractRes(c.getRes());
									}
								} else if (!c.activated() && c.getRes() == Resource.DOUBLE
										&& board.getPlayers()[board.getTurn()].showRes().get(Resource.COAL)
												+ board.getPlayers()[board.getTurn()].showRes().get(Resource.OIL) >= c
														.getCost()
										&& board.getPlayers()[board.getTurn()].getNumCities()
												- panel.getCitiesPowered() > 0) {
									panel.addCitiesPowered(Math.min(c.getMaxCities(),
											board.getPlayers()[board.getTurn()].getNumCities()
													- panel.getCitiesPowered()));
									int price = c.getCost();
									c.activate(true);
									for (int k = 0; k < price; k++) {
										board.getPlayers()[board.getTurn()].subtractRes(c.getRes());
									}
								}
							}
						}
					} else if (board.getPhase() == 5) {
						if (!panel.getEndGamePopup() && e.getX() >= 610 && e.getY() >= 50 && e.getX() <= 765
								&& e.getY() <= 110) {
							panel.setCitiesPowered(0);
							panel.setEndGamePopup(true);
						} else if (panel.getEndGamePopup()) {
							if (e.getX() >= 860 && e.getX() <= 875 && e.getY() >= 310 && e.getY() <= 325) {
								panel.setEndGamePopup(false);
							} else if (e.getX() >= 602 && e.getY() >= 614 && e.getX() <= 702 && e.getY() <= 645) {
								for(Card c : board.getPlayers()[board.getTurn()].getPlants())
									if(c != null)
										c.activate(false);
								panel.addToWinsort(new WinSort(panel.getCitiesPowered(),
										board.getPlayers()[board.getTurn()].getMoney(),
										board.getPlayers()[board.getTurn()].getTurn()));
								panel.setEndGamePopup(false);
								panel.setCitiesPowered(-1);
								board.frontTurn();
								panel.setPlayer(board.getTurn());
								if(board.getTurn() == 0) {
									board.finishGame();
									panel.end(true);
								}
							}
							int i = (e.getX() - 500) / 110;
							if (i >= 0 && i <= 2 && e.getY() >= 400 && e.getY() <= 475) {
								Card c = board.getPlayers()[board.getTurn()].getPlants()[i];
								if (c.getRes() != Resource.DOUBLE
										&& board.getPlayers()[board.getTurn()].showRes().get(c.getRes()) >= c.getCost()
										&& board.getPlayers()[board.getTurn()].getNumCities()
												- panel.getCitiesPowered() > 0) {
									panel.addCitiesPowered(Math.min(c.getMaxCities(),
											board.getPlayers()[board.getTurn()].getNumCities()
													- panel.getCitiesPowered()));
									int price = c.getCost();
									c.activate(true);
									for (int k = 0; k < price; k++)
										board.getPlayers()[board.getTurn()].subtractRes(c.getRes());
								} else if (c.getRes() == Resource.DOUBLE
										&& board.getPlayers()[board.getTurn()].showRes().get(Resource.COAL)
												+ board.getPlayers()[board.getTurn()].showRes().get(Resource.OIL) >= c
														.getCost()
										&& board.getPlayers()[board.getTurn()].getNumCities()
												- panel.getCitiesPowered() > 0) {
									panel.addCitiesPowered(Math.min(c.getMaxCities(),
											board.getPlayers()[board.getTurn()].getNumCities()
													- panel.getCitiesPowered()));
									int price = c.getCost();
									c.activate(true);
									for (int k = 0; k < price; k++)
										board.getPlayers()[board.getTurn()].subtractRes(c.getRes());
								}
							}
						}
					} else if (board.getPhase() == 6) {
						if (!panel.ended() && e.getX() >= 610 && e.getY() >= 50 && e.getX() <= 765
								&& e.getY() <= 110) {
							panel.end(true);
						}
						else {
							if(e.getX() >= 871 && e.getY() >= 777 && e.getX() <= 1059 && e.getY() <= 845) {
								panel.end(false);
							}
						}
					}
					int i = (e.getX() - 1525) / 75;
					if (i > -1 && i < 4 && e.getY() >= 925 && e.getY() <= 995)
						panel.setPlayer(i);
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

	public void updateGamestate() {
		panel.setGamestate(board.getGamestate());
		panel.repaint();
	}

	public static void main(String[] args) throws IOException {
		new GridGraphics();
	}
}
