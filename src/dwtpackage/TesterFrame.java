package dwtpackage;

import java.awt.Graphics;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

public class TesterFrame extends JFrame{
	
	static BufferedImage image, transformedImage;
	static JPanel panel;
	static JButton button;
	static DWTProcessor dwt;
	
	public TesterFrame(){
		dwt = new DWTProcessor();
		panel = new JPanel();
		button = new JButton("Transform Image");
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, panel);
		add(BorderLayout.SOUTH, button);
		
		button.addActionListener(new DWTListener());
	}
	
	public class DWTListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				image = dwt.openImage(panel, image);
				transformedImage = dwt.transform(1, transformedImage, image);
				/*transformedImage = Scale.newresize(transformedImage, transformedImage.getWidth()*4, 
						transformedImage.getHeight());*/
				Graphics g = panel.getGraphics();
	            g.drawImage(transformedImage, 0, 0, null);
	            g.dispose();
				
			} catch (InvalidWidthHeightException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Cannot get Image");
				e1.printStackTrace();
				System.exit(1);
			}
			
		}
		
	}

}
