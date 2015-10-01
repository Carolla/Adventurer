package hic.screenConfiguration;

import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ShowSize_Panel extends JPanel {

	@Override
	protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	System.out.println("height: " + this.getHeight() + ", width: "+ this.getWidth());
	}
	
	// Delete the following
//	public void paint(Graphics g) {         
//		// Dynamically calculate size information         
//		Dimension size = getSize();         
//		// diameter         
//		int d = Math.min(size.width, size.height);          
//		int x = (size.width - d)/2;         
//		int y = (size.height - d)/2;          
//		// draw circle (color already set to foreground)         
//		g.fillOval(x, y, d, d);         
//		g.setColor(Color.black);         
//		g.drawOval(x, y, d, d);     
//		}

}
