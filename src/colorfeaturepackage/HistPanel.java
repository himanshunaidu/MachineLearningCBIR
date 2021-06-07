package colorfeaturepackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HistPanel extends JPanel{
	
	private double hist[];
	private JLabel title;
	private JPanel north;
	private double norm;
	
	public HistPanel(double[] array){
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		hist = new double[array.length];
		hist = normalize(array);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.BLUE);
		g.fillRect(this.getWidth()/4, 0, 3, this.getHeight());
		g.fillRect(this.getWidth()/4, this.getHeight()-3, this.getWidth()/2, 3);
		
		g.setColor(Color.BLACK);
		int length = hist.length;
		for(int i=0; i<length; i++){
			g.fillRect((this.getWidth()/4)+(this.getWidth()*i/(2*length)), this.getHeight()-(int)(hist[i]), 
					(this.getWidth()/(2*length)), (int)(hist[i]));
		}
	}
	
	public double[] normalize(double[] array){
		double[] a1 = array;
		Arrays.sort(a1);
		double min = a1[0], max = a1[a1.length-1];
		for(int i=0; i<array.length; i++){
			array[i] = (array[i]-min)/(max-min);
		}
		return array;
	}

}
