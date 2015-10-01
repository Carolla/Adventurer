package hic;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HeroSliders extends JDialog implements ChangeListener
{
	private final int XPOS = 100;
    private final int YPOS = 100;

    private final int W_WIDTH = 450;
    private final int W_HEIGHT = 500;
	
	private final int _SCORE_MIN = 8;
	private final int _SCORE_MAX = 18;
	
	private final JPanel _contentPanel = new JPanel();
	
	private ArrayList<JSlider> _allSliders = new ArrayList<JSlider>();
	
	private List<String> _attributes = Arrays.asList(
			"Strength", "Constitution", "Dexterity", "Charisma", "Intelligence", "Wisdom"
	);
	
	public HeroSliders(){
		setBounds(XPOS, YPOS, W_WIDTH, W_HEIGHT);
				
		_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(_contentPanel, BorderLayout.CENTER);
        _contentPanel.setLayout(new MigLayout());
        
		for(String attrib : _attributes){
			JSlider slider = new JSlider(JSlider.HORIZONTAL, _SCORE_MIN, _SCORE_MAX, _SCORE_MIN);
			slider.setMajorTickSpacing(2);
			slider.setMinorTickSpacing(1);
			slider.setSnapToTicks(true);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			
			slider.setBorder(BorderFactory.createTitledBorder(attrib));
			slider.addChangeListener(this);
			
			_allSliders.add(slider);
			
			_contentPanel.add(slider, "wrap");
		}
		
		setVisible(true);
	}
	
	public void stateChanged(ChangeEvent e){
	}
	
	public static void main(String args[]){
		new HeroSliders();
	}

}
