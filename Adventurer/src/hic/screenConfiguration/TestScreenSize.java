package hic.screenConfiguration;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class TestScreenSize
{
    public static void main(String [] args)
    {
        final JFrame frame = new JFrame();
        final GraphicsConfiguration config = frame.getGraphicsConfiguration();

        final int left = Toolkit.getDefaultToolkit().getScreenInsets(config).left;
        final int right = Toolkit.getDefaultToolkit().getScreenInsets(config).right;
        final int top = Toolkit.getDefaultToolkit().getScreenInsets(config).top;
        final int bottom = Toolkit.getDefaultToolkit().getScreenInsets(config).bottom;

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width - left - right;
        final int height = screenSize.height - top - bottom;

        frame.setResizable(false);
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        frame.setVisible(true);
    }
}