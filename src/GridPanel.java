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
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		g.drawString("CLICK TO CONTINUE", 50, 50);
	}
	public void setGamestate(ArrayList<Object> g) {
		gs = g;
	}
}
