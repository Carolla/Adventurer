package hic.screenConfiguration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import chronos.Chronos;

public class WideScreenPanel extends JPanel {

	
	public WideScreenPanel() {
		
		// Check current width of panel
		System.out.println("WS Panel Width: " + getSize().getWidth());
		
		// Set Panel Options
		setLayout(new MigLayout("", "[:20%:20%][:80%:80%]", "[:70%:70%][:30%:30%]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setVisible(true);
		
		// Button panel setup
		JPanel under_ButtonsPanel = new JPanel(new MigLayout("", "[grow,center]", "[grow,center]"));
		under_ButtonsPanel.setBackground(Color.LIGHT_GRAY);
		add(under_ButtonsPanel, "cell 0 0,grow");
		
		JPanel component_ButtonsPanel = new JPanel();
		component_ButtonsPanel.setBackground(Color.PINK);
		under_ButtonsPanel.add(component_ButtonsPanel, "width max(50%),height max(50%)");
		
		
		// Setup Panel underneath the image panel
		JPanel under_ImagePanel = new JPanel(new MigLayout("", "[grow,center]", "[grow,center]"));
		under_ImagePanel.setBackground(Color.LIGHT_GRAY);
		add(under_ImagePanel, "cell 1 0,grow");
		
		// Setup the panel to which the image (JLabel) gets added
		JPanel component_ImagePanel = new ShowSize_Panel();
		component_ImagePanel.setBackground(new Color(135, 206, 250));
		under_ImagePanel.add(component_ImagePanel, "width max(50%),height max(50%)");
		
		// Print size of image panel component after applying MigLayout constraints
		System.out.println("Size of Image Panel: " + under_ImagePanel.getSize().toString());
		
		// See if sizes show
		under_ImagePanel.setVisible(true);
		component_ImagePanel.setVisible(true);
		
//		Rectangle rect = new Rectangle(0, 0, 1550, 850);
//		component_ImagePanel.paintImmediately(rect);
		
//		this.repaint();
//		component_ImagePanel.setVisible(true);
//		component_ImagePanel.repaint();
		
		System.out.println("under_ImagePanel size: " + under_ImagePanel.getSize().toString());
		System.out.println("component_ImagePanel size: " + component_ImagePanel.getSize().toString());
		
		
		/*
		 * As indicated by the previous output, the constraints do not factor out
		 * to actual pixel dimensions on the components, therefore extra measures
		 * must be taken to acquire the proper dimensions to use on the component
		 * being added to it's MigLayout constrained parent.
		 */
		
		// Calculate width and height for image
//		double imageWidth = 
		
		// Setup the main image using a JLabel
		JLabel mainImage_label = new JLabel();
        String imagePath = IMAGE_PATH + TOWN_IMAGE;
        // Set width and height for image to be scaled to
//        int imageWidth = 100;
//        int imageHeight = 100;
        int imageWidth = (int) (1594 * 0.8 * 0.8 * 0.5);
        
//        int imgWidth = (getContentPane().getSize().getWidth() * appPanMult * uImgPanMult * imgPanMult);
//        int imgWidth = (cPaneWidth * appPanMult * uImgPanMult * imgPanMult);
//        int imgPanelWidth = (int) (1594 * 0.8 * 0.8 * 0.5);
        
        int imageHeight = (int) (872 * 0.8 * 0.7 * 0.5);
        // Set up a container to hold the image
        ImageIcon imageContainer_icon = new ImageIcon(imagePath);
        // Extract the image from the container
        Image originalImage = imageContainer_icon.getImage();
        // Scale the image
        Image scaledImage = getScaledImage(originalImage, imageWidth, imageHeight);
        // Re-set the icon's image to the scaled version
        imageContainer_icon.setImage(scaledImage);
        // Re-set the label's icon with the newly scaled image
        mainImage_label.setIcon(imageContainer_icon);
        // Add the label to the Image Panel
		component_ImagePanel.add(mainImage_label);
		
		
		// Heros panel setup
		JPanel under_HerosPanel = new JPanel(new MigLayout("", "[grow,center]", "[grow,center]"));
		under_HerosPanel.setBackground(Color.LIGHT_GRAY);
		add(under_HerosPanel, "cell 0 1,grow");
		
		JPanel component_HerosPanel = new JPanel();
		component_HerosPanel.setBackground(new Color(221, 160, 221));
		under_HerosPanel.add(component_HerosPanel, "width max(50%),height max(50%)");
	}
	
    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
//    System.out.println(this + " - height: " + this.getHeight() + ", width: "+ this.getWidth());
    System.out.println("height: " + this.getHeight() + ", width: "+ this.getWidth());
    }

	/* private constants */
	private final int PANEL_WIDTH = 800;
	private final int PANEL_HEIGHT = 450;
	
    private static final String IMAGE_PATH = Chronos.WORKSPACE + Constants.FILE_SEPARATOR + "Adventurer"
            + Constants.FILE_SEPARATOR + "resources"
            + Constants.FILE_SEPARATOR + "images" + Constants.FILE_SEPARATOR;
    private static final String TOWN_IMAGE = "ext_Quasqueton.JPG";
}
