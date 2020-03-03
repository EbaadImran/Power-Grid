import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
	private ArrayList<Object> gs;
	
	public GridPanel() {
		gs = new ArrayList<Object>();
		setSize(1920, 1080);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("powergrid.PNG").getImage(), 0, 0, 1920, 1080, null);
		g.setFont(new Font(Font.SERIF , Font.BOLD, 50));
		g.setColor(new Color(72, 72, 72));
		g.drawString("CLICK TO CONTINUE", 760, 850);
	}
	public void setGamestate(ArrayList<Object> g) {
		gs = g;
	}
}
