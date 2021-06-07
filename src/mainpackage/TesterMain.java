package mainpackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import colorfeaturepackage.CBIRLab;
import gapackagemain.GA;
import lbppackage.LBP;
import lbppackage.LBPTest;
import msdfeaturepackage.CBIRLab2;
import msdfeaturepackage.HistPanel;
import msdfeaturepackage.MSDCompute;

public class TesterMain{
	
	//GA ga = new GA();
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
    
    public static int[] ids = null;
    public static String[] names =  null;
    public static String[] paths = null;
    public static double[] compares = null;
    public static double[][] hists = null;
    public static double[][] lbps = null;
    public static double[][] msds = null;
    
    public static double[] hist = null;
    public static double[] lbp = null;
    public static double[] msd = null;
	
    public static final int max = 17;
    public static final int min = 0;
    
    public static double[] weights = null;
    
    private static JFrame app;
    private static JLabel mainLabel;
    private static JButton button;
    private static JLabel[] images;
    private static JPanel mainPanel, imagePanel;
    
    private static BufferedImage image;
    private static ImageIcon icon;
    private static int valid;
	
	public static void main(String[] args){
		
		//weights = GA.getWeights();
		double[] weights1 = {0.2677765866589640000, 0.0016142586086540511, 0.9219724530090173000};
		weights = weights1;
		
		
		app = new JFrame();
    	
		mainLabel = new JLabel();
        button = new JButton("Find Similar");
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));
        mainPanel.add(mainLabel);
        mainPanel.add(button);
        images = new JLabel[10];
        imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(2, 5));
        for(int i=0; i<images.length; i++){
        	images[i] = new JLabel();
        	imagePanel.add(images[i]);
        }
        
        button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
					connectDB();
					valid=0;
			    	
			        System.out.println("\n\n\nStarting File Chooser");
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
			        
			        hist = CBIRLab.getHist(image);
			        lbp = LBPTest.getFullLBP(image);
			        msd = CBIRLab2.getMSD(image);
			        
			        retrieveFiles(2500);
			        //printMainArrays();
			        
			        compares = mainCompare();
			        for(int i=0; i<compares.length; i++){
			        	System.out.print(compares[i]+" ");
			        }
			        
			        closeDB();
			        
			        sortMainArrays();
			        
			        mainLabel.setIcon(icon);
			        
			        for(int i=0; i<images.length; i++){
			        	File f = new File(paths[i]);
			        	System.out.println(paths[i]);
			        	BufferedImage img = ImageIO.read(f);
			        	ImageIcon ic = new ImageIcon(f.getAbsolutePath());
			        	images[i].setIcon(ic);
			        }
				}catch(IOException e){
					JOptionPane.showMessageDialog(null, "File Retrieval Error. Sorry for the inconvenience!!!");
					e.printStackTrace();
				}
			}
        	
        });
        
        app.setLayout(new GridLayout(2, 1));
        app.add(mainPanel);
        app.add(imagePanel);
        
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.pack();
        app.setVisible(true);
	}
	
	public static void connectDB(){
    	try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
			pstatement = connection.prepareStatement("Insert into msd_images2"+
						"(id, Name, Path, Array) "+
						"Values(?, ?, ?, ?)");
			connected = true;
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database Connection Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
	
	public static void retrieveFiles(int limit){
    	int id, i=0, l=0, length=0, length1=0;
    	String array1, array2, array3;
    	try {
			resultset = statement.executeQuery("Select * from main");
			metadata = resultset.getMetaData();
			while(resultset.next()){
				length1 = resultset.getInt(1);
			}
			
			length = limit;
			ids = new int[length];
			names =  new String[length];
			hists = new double[length][];
			lbps = new double[length][];
			msds = new double[length][];
			paths = new String[length];
			compares = new double[length];
			
			//printMainArrays();
			resultset = statement.executeQuery("Select * from main limit "+limit);			
			while((resultset.next())&&(i<limit)){
				ids[i] = resultset.getInt(1);
				names[i] = resultset.getString(2);				
				paths[i] = resultset.getString(3);
				array1 = resultset.getString(4);
				//System.out.println(array1);
				hists[i] = processArray(array1);
				array2 = resultset.getString(5);
				//System.out.println(array2);
				lbps[i] = processArray(array2);
				array3 = resultset.getString(6);
				//System.out.println(array3);
				msds[i]	= processArray(array3);
				
				i++;
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Retrieval Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
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
	
	public static void printMainArrays(){
		int i=0, j=0;
		//System.out.println(hists.length+"\n");
		//System.out.println(hists[0].length+"\n");
		try{
		System.out.print("CDH: ");
        for(i=0; i<hists.length; i++){
        	for(j=0; j<hists[i].length; j++){
        		System.out.print(hists[i][j]+" ");
        	}
        	System.out.println("");
        }
        System.out.println("\n");
        System.out.print("LBP: ");
        for(i=0; i<lbps.length; i++){
        	for(j=0; j<lbps[i].length; j++){
        		System.out.print(lbps[i][j]+" ");
        	}
        	System.out.println("");
        }
        System.out.println("\n");
        System.out.print("MSD: ");
        for(i=0; i<msds.length; i++){
        	for(j=0; j<msds[i].length; j++){
        		System.out.print(msds[i][j]+" ");
        	}
        	System.out.println("");
        }
        System.out.println("\n");
		}catch(Exception e){
			System.out.println("\n"+i+" "+j);
			e.printStackTrace();
		}
	}
	
	public static double[] processArray(String array){
    	String[] hist1 = array.split(" ");
    	int length = hist1.length;
    	double[] hist2 = new double[hist1.length];
    	
    	for(int k=0; k<length; k++){
    		hist2[k] = Double.parseDouble(hist1[k]);
    		//System.out.print(hist2[k]+" ");
    	}
    	//System.out.println("");
    	return hist2;
    }
	
	public static double[] mainCompare(){
		double[] histD = compareFiles(hist, hists);
		double[] lbpD = compareFiles(lbp, lbps);
		double[] msdD = compareFiles(msd, msds);
		
		double[] D = new double[histD.length];
		
		for(int i=0; i<histD.length; i++){
			D[i] = histD[i]*weights[0]+lbpD[i]*weights[1]+msdD[i]*weights[2];
		}
		
		return D;
	}
	
	public static double[] compareFiles(double[] hist2, double[][] mains){
    	int length1 = mains.length;
    	int length2 = hist2.length;
    	int m=0, n=0;
    	double com;
    	double com1[] = new double[length1];
    	
    	//System.out.println(hists[0].length);
    	for(m=0; m<length1; m++){
    		com = 0;
    		for(n=0; n<length2; n++){
    			com+= Math.pow(hist2[n]-mains[m][n], 2);
    		}
    		com = Math.sqrt(com)/length2;
    		com1[m] = com;
    	}
    	
    	return com1;
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

	

}
//495