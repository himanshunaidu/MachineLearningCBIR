package colorfeaturepackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import msdfeaturepackage.HistPanel;
import msdfeaturepackage.MSDCompute;

public class CBIRLab {
	
	private static int lnum = 10;
    private static int anum = 3;
    private static int bnum = 3;

    private static int cnum = lnum * anum * bnum;
    private static int onum = 18;
	
	public static int valid=0, ucount=0;
    public static double[][] hists = null;
    public static String[] paths = null;
    public static double[] compares = null;
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cbir_project?useSSL=false";
    public static boolean connected = false;
    
    public static Connection connection = null;
    public static Statement statement = null;
    public static PreparedStatement pstatement = null;
    public static PreparedStatement rstatement = null;
    public static ResultSet resultset = null;
    public static ResultSetMetaData metadata = null;
    
    static GridBagLayout layout;
	static GridBagConstraints constraints;
	
	public static final File dir = new File("<<Corel 10K Database Corel 2 File>>");
    public static final String[] EXTENSIONS = new String[]{
        "jpg", "png"
    };
    
    public static final FilenameFilter ImageFilter = new FilenameFilter(){
        @Override
        public boolean accept(File dir, String name) {
             for(final String ext: EXTENSIONS){
                 if(name.endsWith("."+ext)){
                     return true;
                 }
             }
             return false;
        }
        
    };
    
    public static void main(String[] args) throws IOException{
        
    	JFrame app = new JFrame();
    	connectDB();
    	
    	//Original Code to take in image input
    	BufferedImage image;
        ImageIcon icon;
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = chooser.showOpenDialog(app);
        if(result==JFileChooser.CANCEL_OPTION){
        	System.exit(1);
        }
        File file = chooser.getSelectedFile();
        for(final String ext: EXTENSIONS){
            if(file.getName().endsWith("."+ext)){
                valid=1;
            }
        }
        if(valid==0){
        	System.exit(1);
        }
        image = ImageIO.read(file);
        icon = new ImageIcon(file.getAbsolutePath());
        
        double[] features = getHist(image);
        
        for(int i=0; i<features.length; i++){
        	System.out.print(""+features[i]+" ");
        }
        //System.out.println("\n"+features.length);
        
        JLabel imageLabel = new JLabel(icon);
        
        HistPanel histPanel = new HistPanel(features);
        Border border1 = BorderFactory.createTitledBorder("Histogram");
        histPanel.setBorder(border1);
        histPanel.setPreferredSize(new Dimension(144, 80));        
        
        JPanel panel = new JPanel();
        JLabel[] images = new JLabel[10];
        
        /*app.add(histPanel);
        
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.pack();
        app.setVisible(true);*/
        
        processFiles(10000);
        closeDB();
    }
    
    public static double[] getHist(BufferedImage image){
    	int[][][] RGB = getRGB(image, image.getWidth(), image.getHeight());
        /*for(int i=0; i<(image.getWidth()); i++){
            for(int j=0; j<(image.getHeight()); j++){
                System.out.println(RGB[0][i][j]+", "+RGB[1][i][j]+", "+RGB[2][i][j]);
            }
        }*/
        int[][] ori;
        int[][] ColorX;
        double[][][] LAB;
        
        //double[] hist = new double[cnum+onum];
        
        LAB = ColorSpace.RGB2Lab(RGB, image.getWidth(), image.getHeight());
        //System.out.println("Start Print: "+" "+LAB.length+" "+LAB[0].length+" "+LAB[0][0].length);
        
        /*for(int i=0; i<LAB.length; i++){
        	for(int j=0; j<LAB[0].length; j++){
        		for(int k=0; k<LAB[0][0].length; k++){
        			System.out.print(""+LAB[i][j][k]+" ");
        		}
        		System.out.println("");
        	}
        	System.out.println("");
        }*/
        ColorX = ColorQuantization.cLab(LAB, lnum, anum, bnum, image.getWidth(), image.getHeight());
        /*for(int j=0; j<ColorX.length; j++){
    		for(int k=0; k<ColorX[0].length; k++){
    			System.out.print(""+ColorX[j][k]+" ");
    		}
    		System.out.println("");
    	}*/
        ori = Edge.maxgrad_and_mingrad_Lab(LAB, onum, image.getWidth(), image.getHeight());
        /*for(int j=0; j<ori.length; j++){
    		for(int k=0; k<ori[0].length; k++){
    			System.out.print(""+ori[j][k]+" ");
    		}
    		System.out.println("");
    	}*/
        
        int D=1;
        double[] features = CDH.compute(ColorX, ori, LAB, image.getWidth(), image.getHeight(), cnum, onum, D);
        return features;
    }
    
    public static void processFiles(int limit){
        int i=0, j=0;
        int[][][] rgbtemp;
        double[] featuretemp;
        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(ImageFilter)) {
                BufferedImage img = null;
                if(i==limit){
                    break;
                }

                try {
                    img = ImageIO.read(f);

                    // you probably want something more involved here
                    // to display in your UI
                    featuretemp = getHist(img);
                    for(j=0; j<featuretemp.length; j++){
                    	System.out.print(featuretemp[j]+" ");
                    }
                    System.out.print("\n");
                    
                    storeFiles(i+1, f.getName(), f.getAbsolutePath(), featuretemp);                    
                    
                } catch (final IOException e) {
                    // handle errors here
                }
                i++;
            }
        }
    }
    
    public static int[][][] getRGB(BufferedImage image, int wid, int hei){
        int i, j, pixel;
        Color col;
        int[][][] RGB = new int[3][wid][hei];
        for(i=0; i<wid; i++){
            for(j=0; j<hei; j++){
                pixel = image.getRGB(i, j);
                col = new Color(pixel, true);
                RGB[0][i][j] = col.getRed();
                RGB[1][i][j] = col.getGreen();
                RGB[2][i][j] = col.getBlue();
            }
        }
        
        return RGB;
    }
    
    public static void connectDB(){
    	try {
    		//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
			pstatement = connection.prepareStatement("Insert into cdh_images2"+
						"(id, Name, Path, Array) "+
						"Values(?, ?, ?, ?)");
			connected = true;
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database Connection Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} /*catch (ClassNotFoundException e) {
			closeDB();
			JOptionPane.showMessageDialog(null, "Database Class Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}*/
    }
    
    public static void storeFiles(int id, String name, String path, double[] array){
    	
    	try {
			pstatement.setInt(1, id);
			pstatement.setString(2, name);
			pstatement.setString(3, path);
			String hist = "";
			for(int k=0; k<(array.length-1); k++){
				hist += array[k]+" ";
			}
			hist+=array[array.length-1];
			
			pstatement.setString(4, hist);
			
			int result = pstatement.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Updation Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    public static void retrieveFiles(int limit){
    	int id, i=0, l=0, length=0, length1=0;
    	String path, name, array;
    	try {
			resultset = statement.executeQuery("Select * from cdh_images2");
			metadata = resultset.getMetaData();
			//int numberOfColumns = metadata.getColumnCount();
			while(resultset.next()){
				length++;
			}
			//System.out.println("\n"+length);
			hists = new double[length][];
			paths = new String[length];
			compares = new double[length];
			
			resultset = statement.executeQuery("Select * from images");			
			while((resultset.next())&&(i<limit)){
				id = resultset.getInt(1);
				name = resultset.getString(2);				
				paths[i] = resultset.getString(3);
				array = resultset.getString(4);
				//System.out.println(array);
				hists[i] = processArray(array);
				i++;
				/*length1 = hists[i].length;
				System.out.print(id+" "+name);
				for(l=0; l<length1; l++){
					System.out.print(" "+hists[i][l]);
				}
				System.out.print("\n");*/
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Retrieval Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    public static double[] processArray(String array){
    	String[] hist1 = array.split(" ");
    	int length = hist1.length;
    	double[] hist2 = new double[hist1.length];
    	
    	for(int k=0; k<length; k++){
    		hist2[k] = Double.parseDouble(hist1[k]);
    		System.out.print(hist2[k]+" ");
    	}
    	System.out.println("");
    	return hist2;
    }
    
    public static double[] compareFiles(double[] hist2){
    	int length1 = hists.length;
    	int length2 = hist2.length;
    	int m=0, n=0;
    	double com;
    	double com1[] = new double[length1];
    	
    	//System.out.println(hists[0].length);
    	for(m=0; m<length1; m++){
    		com = 0;
    		for(n=0; n<length2; n++){
    			com+= Math.pow(hist2[n]-hists[m][n], 2);
    		}
    		com = Math.sqrt(com)/length2;
    		com1[m] = com;
    	}
    	
    	return com1;
    }
    
    public static void closeDB(){
    	if(connected==true){
    		try {
				//resultset.close();
				pstatement.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Could not close database", "Error",
						JOptionPane.ERROR_MESSAGE);
			}    		
    	}
    }
    
    public static void sortMainArrays(){
    	double temp1;
    	String temp2;
    	
    	for (int i = 0; i < compares.length - 1; i++)  
        {  
            int index = i;  
            for (int j = i + 1; j < compares.length; j++){  
                if (compares[j] < compares[index]){  
                    index = j;//searching for lowest index  
                }  
            }  
            temp1 = compares[index];   
            compares[index] = compares[i];  
            compares[i] = temp1;  
            
            temp2 = paths[index];
            paths[index] = paths[i];
            paths[i] = temp2;
        }  
    }
    
    public static void addComponent(JFrame frame, Component comp, int x, int y, int width, int height){
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(comp, constraints);
		frame.add(comp);
	}

}
//835