package lbppackage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class LBPTest {
	
	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cbir_project?useSSL=false";
    public static boolean connected = false;
    
    public static Connection connection = null;
    public static Statement statement = null;
    public static PreparedStatement pstatement = null;
    public static PreparedStatement rstatement = null;
    public static ResultSet resultset = null;
    public static ResultSetMetaData metadata = null;
	
	public static int valid = 0;
	
	public static final File dir = new File("<<Corel 10K Databse: Corel2 File>>");
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
    
    public static byte[][][] lbps = null;
    public static String[] paths = null;
    public static double[] compares = null;
    public static byte[][] resultLBP = null;
    public static double[] resultHist = null;
    
    public static final int max = 17;
    public static final int min = 0;
	
	public static void main(String[] ar){
		
		JFrame app = new JFrame();
		BufferedImage image;
        //image = ImageIO.read(new File("E:/VIT/6. Winter Sem 2017-18/Course/Lab/Content Based Image Restoration/CBIR/Corel 10K/Corel/1.jpg"));
        ImageIcon icon;
        //icon = new ImageIcon("E:/VIT/6. Winter Sem 2017-18/Course/Lab/Content Based Image Restoration/CBIR/Corel 10K/Corel/1.jpg");
        
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
        try {
			image = ImageIO.read(file);
		
        icon = new ImageIcon(file.getAbsolutePath());
        int height = image.getHeight(), width = image.getWidth();
        double[][] data = getFullRGB(image);
        
        
		//LBP.printMatrix(data);
		
		LBP lbp = new LBP(max-1,2);
		//System.out.println("Neighbourhood");
		LBP.printMatrix(lbp.neighbourhood);
		resultLBP = lbp.getLBP(data);
		
		//System.out.println("VarianceImage");
		LBP.printMatrix(resultLBP);
		
		System.out.println("Minimum: "+LBPTest.min(resultLBP));
		System.out.println("Maximum: "+LBPTest.max(resultLBP));
		
		resultHist = lbp.getHistogram(resultLBP, max, min);
		double length = 0;
		for(int i=0; i<resultHist.length; i++){
			System.out.print(resultHist[i]+" ");
			length += resultHist[i];
		}
		
		//System.out.println(("\n"+resultLBP.length*resultLBP[0].length)+"\n"+length);
		
		connectDB();
		processFiles(10000);
		/*System.out.println(data.length+" "+data[0].length);
		System.out.println(resultLBP.length+" "+resultLBP[0].length);
		System.out.println(image.getRGB(5, 5));*/
		closeDB();
		
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static double[] getFullLBP(BufferedImage image){
		double[][] data = getFullRGB(image);
        
        
		//LBP.printMatrix(data);
		
		LBP lbp = new LBP(max-1,2);
		//System.out.println("Neighbourhood");
		//LBP.printMatrix(lbp.neighbourhood);
		resultLBP = lbp.getLBP(data);
		resultHist = lbp.getHistogram(resultLBP, max, min);
		return resultHist;
	}
	
	public static double[][] getFullRGB(BufferedImage image){
		int width = image.getWidth(), height = image.getHeight();
		double[][] data = new double[width][height];
		for(int i=0; i<width; i++){
        	for(int j=0; j<height; j++){
        		data[i][j] = (double)image.getRGB(i, j);
        	}
        }
		return data;
	}
	
	public static void processFiles(int limit){
        int i=0, j=0, k=0;
        int[][][] rgbtemp;
        double[][] data;
        byte[][] lbptemp;
        double[] histtemp;
        LBP lbp = new LBP(16, 2);
        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(ImageFilter)) {
                BufferedImage img = null;
                if(i==limit){
                    break;
                }

                try {
                    img = ImageIO.read(f);
                    data = getFullRGB(img);

                    // you probably want something more involved here
                    // to display in your UI
                    lbptemp = lbp.getLBP(data);
                    histtemp = lbp.getHistogram(lbptemp, max, min);
                    
                    for(j=0; j<lbptemp.length; j++){
                    	for(k=0; k<lbptemp[0].length; k++){
                    		//System.out.print(lbptemp[j][k]+" ");
                    	}
                    }
                    //System.out.print("\n");
                    
                    for(j=0; j<histtemp.length; j++){
                    	System.out.print(histtemp[j]+" ");
                    }
                    System.out.println("");
                    
                    storeFiles(i+1, f.getName(), f.getAbsolutePath(), histtemp);                    
                    
                } catch (final IOException e) {
                    // handle errors here
                }
                i++;
            }
        }
    }
	
	public static void connectDB(){
    	try {
    		//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
			pstatement = connection.prepareStatement("Insert into lbp_images2"+
						"(id, Name, Path, Histogram) "+
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
    
    public static void storeFiles(int id, String name, String path, double[] hist){
    	
    	try {
			pstatement.setInt(1, id);
			pstatement.setString(2, name);
			pstatement.setString(3, path);
			String hists = "";
			/*for(int l=0; l<(array.length-1); l++){
				for(int m=0; m<array[l].length; m++){
					lbp += array[l][m]+" ";
				}
				lbp += "\n";
			}
			lbp+=array[array.length-1];
			
			pstatement.setString(4, lbp);*/
			for(int l=0; l<hist.length; l++){
				hists += hist[l]+" ";
			}
			pstatement.setString(4, hists);
			
			System.out.println(hists.length());
			
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
			resultset = statement.executeQuery("Select * from lbp_images2");
			metadata = resultset.getMetaData();
			//int numberOfColumns = metadata.getColumnCount();
			while(resultset.next()){
				length++;
			}
			//System.out.println("\n"+length);
			lbps = new byte[length][][];
			paths = new String[length];
			compares = new double[length];
			
			resultset = statement.executeQuery("Select * from lbp_images2");			
			while((resultset.next())&&(i<limit)){
				id = resultset.getInt(1);
				name = resultset.getString(2);				
				paths[i] = resultset.getString(3);
				array = resultset.getString(4);
				//System.out.println(array);
				lbps[i] = processArray(array);
				i++;
				/*length1 = lbps[i].length;
				System.out.print(id+" "+name);
				for(l=0; l<length1; l++){
					System.out.print(" "+lbps[i][l]);
				}
				System.out.print("\n");*/
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Retrieval Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    public static byte[][] processArray(String array){
    	String[] hist1 = array.split("\n");
    	int length = hist1.length;
    	String[][] hist2 = new String[length][];
    	for(int n=0; n<length; n++){
    		hist2[n] = hist1[n].split(" ");
    	}
    	int length1 = hist2[0].length;
    	
    	byte[][] hist3 = new byte[length][length1];
    	
    	for(int m=0; m<length; m++){
    		for(int n=0; n<length1; n++){
    			//hist3[m][n] = Byte.parseByte(hist2[m][n]);
    			System.out.println(hist2[m][n]);
    		}
    	}
    	
    	return hist3;
    }
    
    public static double[] processNumArray(String array){
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
    
    public static byte min(byte[][] resultLBP){
    	int length=resultLBP.length, length1 = resultLBP[0].length;
    	byte min = resultLBP[0][0];
    	for(int i=0; i<length; i++){
    		for(int j=0; j<length1; j++){
    			if(resultLBP[i][j]<min){
    				min = resultLBP[i][j];
    			}
    		}
    	}
    	return min;
    }
    
    public static byte max(byte[][] resultLBP){
    	int length=resultLBP.length, length1 = resultLBP[0].length;
    	byte max = resultLBP[0][0];
    	for(int i=0; i<length; i++){
    		for(int j=0; j<length1; j++){
    			if(resultLBP[i][j]>max){
    				max = resultLBP[i][j];
    			}
    		}
    	}
    	return max;
    }
    
    

}
