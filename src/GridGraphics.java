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
            	System.out.println(e.getX() + " " + e.getY());
            	if(panel.getScreen() == 0) { //start screen
            		panel.setScreen(1);
            	}
            	else if(panel.getScreen() == 1) { //choose regions
            		if(e.getX() >= 70 && e.getX() <= 170) { 
            			int i = (e.getY()-910) / 20;
            			int s = 0;
            			String ans = null;
            			for(String k : board.getRegions()) {
            				if(i == s) {
            					ans = k;
            					break;
            				}
            				s++;
            			}
            			if(ans != null)
            				board.removeRegion(ans);
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
