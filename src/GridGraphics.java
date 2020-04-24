import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

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
        this.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
            	if(panel.getScreen() == 0) { //start screen
            		panel.setScreen(1);
            	}
            	else if(panel.getScreen() == 1) { //choose regions
            		if(e.getX() >= 200 && e.getY() >= 910 && e.getX() <= 250 && e.getY() <= 960 && board.getRegions().size() >= 1) { 
            			board.removeRegion();
            		}
            		if(e.getX() >= 370 && e.getX() <= 470) {
            			int i = (e.getY()-910) / 20;
            			int s = 0;
            			String ans = null;
            			for(String k : board.getAvailableRegions()) {
            				if(i == s) {
            					ans = k;
            					break;
            				}
            				s++;
            			}
            			if(ans != null)
            				board.addRegion(ans);
            		}
            		if(e.getX() >= 1920/2-250 && e.getX() <= 1920/2+250 && e.getY() >= 1080/2-250 && e.getY() <= 1080/2+250 && board.getRegions().size() == 4) {
            			panel.setScreen(2);
            			board.lock();
            		}	
            	}
            	else if(panel.getScreen() == 2) { //main gui
            		System.out.println(e.getX() + " " + e.getY());
            		if(board.getPhase() == 3) {
            			if(panel.getCityPopup() == null)
            			{
            				Graph map = board.getGraph();
            				for(String k : map.getGraph().keySet()) {
	            				City test = map.getGraph().get(k);
	            				if(e.getX() >= test.getMinX() && e.getX() <= test.getMaxX() && e.getY() >= test.getMinY() && e.getY() <= test.getMaxY() && !board.getLocked().contains(test.getColor())) {
	            					panel.setCityPopup(test);
	            					break;
	            				}
            				}
            			}
            			else {
            				if(e.getX() >= 790 && e.getX() <= 805 && e.getY() >= 360 && e.getY() <= 375)
                				panel.setCityPopup(null);
            			} 
            		}
            		else if(board.getPhase() == 1) {
            			if(panel.getAuctionPopup() == 0 && e.getX() >= 1547 && e.getY() >= 366 && e.getX() <= 1666 && e.getY() <= 411) {
            				if(board.getAuction().getHighestBid() > 0)
            					panel.setAuctionPopup(3);
            				else
            					panel.setAuctionPopup(1);
            			}
            			else {
            				if(e.getX() >= 860 && e.getX() <= 875 && e.getY() >= 310 && e.getY() <= 325)
            					panel.setAuctionPopup(0);
            				if(panel.getAuctionPopup() == 1) {
            					if(e.getX() >= 604 && e.getY() >= 500 && e.getX() <= 704 && e.getY() <= 550) {
            						board.removeFromAuction(board.getPlayers()[board.getTurn()].getTurn());
            						panel.setAuctionPopup(0);
            					}
            					int i = (e.getX() - 420) / 120;
            					if(i > -1 && i < 4 && e.getY() >= 360 && e.getY() <= 470 && board.getPlayers()[board.getTurn()].getMoney() >= board.getDeck().getMarket()[i].getNum()) {
            						panel.setAuctionPopup(2);
            						panel.setAuctionCard(board.getDeck().getMarket()[i]);
            						panel.setPrice(board.getDeck().getMarket()[i].getNum());
            					}
            				}
            				else if(panel.getAuctionPopup() == 2) {
            					if(e.getX() >= 575 && e.getY() >= 525 && e.getX() <= 625 && e.getY() <= 575) {
            						panel.setAuctionPopup(1);
            						panel.setAuctionCard(null);
            					}
            					else if(e.getX() >= 647 && e.getX() <= 667 && e.getY() >= 536 && e.getY() <= 565 && panel.getPrice() > panel.getAuctionCard().getNum()) {
            						panel.setPrice(panel.getPrice() - 1);
            					}
            					else if(e.getX() >= 722 && e.getX() <= 746 && e.getY() >= 536 && e.getY() <= 565 && panel.getPrice() < board.getPlayers()[board.getTurn()].getMoney()) {
            						panel.setPrice(panel.getPrice() + 1);
            					}
            					else if(e.getX() >= 770 && e.getY() >= 525 && e.getX() <= 820 && e.getY() <= 575) {
            						board.getAuction().setAuction(panel.getAuctionCard(), panel.getPrice(), board.getTurn(), board.getAuctionOrder());
            						panel.setPrice(panel.getPrice() + 1);
            						panel.setAuctionPopup(3);
            					}
            				}
            				else if(panel.getAuctionPopup() == 3) {
            					if(e.getX() >= 575 && e.getY() >= 525 && e.getX() <= 625 && e.getY() <= 575) {
            						board.getAuction().pass(board.getAuction().getTurn());
            					}
            					else if(e.getX() >= 647 && e.getX() <= 667 && e.getY() >= 536 && e.getY() <= 565 && panel.getPrice() > board.getAuction().getHighestBid() + 1) {
            						panel.setPrice(panel.getPrice() - 1);
            					}
            					else if(e.getX() >= 722 && e.getX() <= 746 && e.getY() >= 536 && e.getY() <= 565 && panel.getPrice() < board.getPlayers()[board.getTurn()].getMoney()) {
            						panel.setPrice(panel.getPrice() + 1);
            					}
            					else if(e.getX() >= 770 && e.getY() >= 525 && e.getX() <= 820 && e.getY() <= 575) {
            						board.getAuction().bid(panel.getPrice(), board.getAuction().getTurn());
            						panel.setPrice(panel.getPrice() + 1);
            					}
            				}
            			}
            		}
            	}
            	updateGamestate();
            }
        });
        setUndecorated(true);
        setVisible(true);
	}
	public void updateGamestate() {
		panel.setGamestate(board.getGamestate());
		panel.repaint();
	}
	public static void main(String[] args) throws IOException {
		new GridGraphics();
	}
}
