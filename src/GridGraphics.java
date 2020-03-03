import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;

public class GridGraphics extends JFrame {
	Board board;
	GridPanel panel;
	
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
            	
            }
        });
        setUndecorated(true);
        setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		new GridGraphics();
	}
}
