package msdfeaturepackage;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HistPanel extends JPanel{
	
	private double hist[];
	private JLabel title;
	private JPanel north;
	
	public HistPanel(double[] array){
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		hist = new double[array.length];
		hist = array;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.BLUE);
		g.fillRect(this.getWidth()/4, 0, 3, this.getHeight());
		g.fillRect(this.getWidth()/4, this.getHeight()-3, this.getWidth()/2, 3);
		
		g.setColor(Color.BLACK);
		int length = hist.length;
		for(int i=0; i<length; i++){
			g.fillRect((this.getWidth()/4)+(this.getWidth()*i/(2*length)), this.getHeight()-(int)(hist[i]*1000), 
					(this.getWidth()/(2*length)), (int)(hist[i]*1000));
		}
	}

}
