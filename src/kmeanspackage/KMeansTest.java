package kmeanspackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class KMeansTest {
	
	public static int valid=0, ucount=0;
	public static double[][][] data = null;
    public static double[][] hists = null;
    public static double[] weights = null;
    public static String[] paths = null;
    public static double[] compares = null;
	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cbir?useSSL=false";
    public static boolean connected = false;
    
    public static Connection connection = null;
    public static Statement statement = null;
    public static PreparedStatement pstatement = null;
    public static PreparedStatement rstatement = null;
    public static ResultSet resultset = null;
    public static ResultSetMetaData metadata = null;
	
	public static void main(String[] args){
		connectDB();
		retrieveFiles(1000);
		/*for(int i=0; i<hists.length; i++){
			System.out.println((i+1)+"");
			for(int j=0; j<hists[0].length; j++){
				System.out.print(hists[i][j]+" ");
			}
			System.out.println("");
		}*/
		weights = new double[3];
		data = new double[3][][];
		for(int i=0; i<3; i++){
			data[i] = hists;
			weights[i] = 1;
		}
		
		KMeans kmean = new KMeans(hists, hists, hists, weights);
		kmean.clustering(10, 20, null);
		
		double[][][] centroids = kmean.getCentroids();
		KMeans.printCentroids(centroids, centroids);
	}
	
	public static void connectDB(){
    	try {
    		//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
			pstatement = connection.prepareStatement("Insert into images"+
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
	
	public static void retrieveFiles(int limit){
    	int id, i=0, l=0, length=0, length1=0;
    	String path, name, array;
    	try {
			resultset = statement.executeQuery("Select * from images limit "+limit);
			metadata = resultset.getMetaData();
			//int numberOfColumns = metadata.getColumnCount();
			while(resultset.next()){
				length++;
			}
			//System.out.println("\n"+length);
			hists = new double[length][];
			paths = new String[length];
			compares = new double[length];
			
			resultset = statement.executeQuery("Select * from images limit "+limit);			
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
    	}
    	return hist2;
    }

}
//745