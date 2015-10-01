package hic.screenConfiguration;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StandardScreenPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public StandardScreenPanel() {
		setLayout(new MigLayout("", "[50%,grow][50%,grow]", "[:70%:70%][:30%:30%]"));
		setSize(PANEL_WIDTH, PANEL_HEIGHT);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel pnl_Image = new JPanel();
		pnl_Image.setBackground(Color.PINK);
		add(pnl_Image, "cell 0 0 2 1,grow");
		
		JPanel pnl_Buttons = new JPanel();
		pnl_Buttons.setBackground(Color.GRAY);
		add(pnl_Buttons, "cell 0 1,grow");
		
		JPanel pnl_HerosInParty = new JPanel();
		pnl_HerosInParty.setBackground(Color.LIGHT_GRAY);
		add(pnl_HerosInParty, "cell 1 1,grow");
	}
	
	/* private constants */
	private final int PANEL_WIDTH = 800;
	private final int PANEL_HEIGHT = 600;

}
