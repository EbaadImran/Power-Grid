import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
	private ArrayList<Object> gs;
	private int screen;
	private HashMap<String, Color> colors;
	
	public GridPanel() {
		gs = new ArrayList<Object>();
		setSize(1920, 1080);
		screen = 0;
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
		if(screen == 0) {
			g.drawImage(new ImageIcon("powergrid.PNG").getImage(), 0, 0, 1920, 1080, null);
			g.setFont(new Font("Courier", Font.BOLD, 50));
			g.setColor(colors.get("pri"));
			g.drawString("CLICK TO CONTINUE", 1350, 1040);
		}
		else if(screen == 1) {
			g.drawImage(new ImageIcon("map.png").getImage(), 0, 0, 1920, 1080, null);
			HashSet<String> regions = (HashSet<String>) gs.get(3);
			HashSet<String> availableRegions = (HashSet<String>) gs.get(4);
			g.setFont(new Font("Courier", Font.BOLD | Font.ITALIC, 50));
			g.setColor(colors.get("pri"));
			g.drawString("Selected:", 50, 900);
			g.drawString("Choose:", 350, 900);
			
			int y = 925;
			g.setFont(new Font("Courier", Font.BOLD, 25));
			for(String k : availableRegions) {
				g.setColor(colors.get(k));
				g.drawString(k, 375, y);
				y += 20;
			}
			
			y = 925;
			g.setFont(new Font("Courier", Font.BOLD, 25));
			for(String k : regions) {
				g.setColor(colors.get(k));
				g.drawString(k, 75, y);
				y += 20;
			}
			
			if(regions.size() == 4) {
				g.drawImage(new ImageIcon("lock.png").getImage(), 1920/2-250, 1080/2-250, 500, 500, null);
				g.setColor(colors.get("pri"));
				g.setFont(new Font("Courier", Font.BOLD, 50));
				g.drawString("LOCK IN", 1920/2-100, 1080/2+160);
			}
		}
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
}
