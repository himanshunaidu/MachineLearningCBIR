package mainpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DataExtractor {
	
	public int[] ids = null;
	public String[] names = null;
	public int[] labels = null;
	public String[] paths = null;
	public double[][] color = null;
	public double[][] texture = null;
	public double[][] feature = null;
	
	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cbir_project?useSSL=false";
    public static boolean connected = false;
    
    public static Connection connection = null;
    public static Statement statement = null;
    public static PreparedStatement pstatement = null;
    public static PreparedStatement rstatement = null;
    public static ResultSet resultset = null;
    public static ResultSetMetaData metadata = null;
	
	public DataExtractor(int limit){
		connectDB();
		retrieveFiles(limit);
		closeDB();
	}
	
	public void connectDB(){
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
	
	public void retrieveFiles(int limit){
    	int id, i=0, l=0, length=0, length1=0;
    	try {
			resultset = statement.executeQuery("Select count(*) from main");
			metadata = resultset.getMetaData();
			while(resultset.next()){
				length1 = resultset.getInt(1);
			}
			length=limit;
			
			//System.out.println("\n"+length);
			ids = new int[length];
			names = new String[length];
			labels = new int[length];
			paths = new String[length];
			color = new double[length][];
			texture = new double[length][];
			feature = new double[length][];
			
			resultset = statement.executeQuery("Select * from main limit "+limit);			
			while((resultset.next())&&(i<limit)){
				ids[i] = resultset.getInt(1);
				names[i] = resultset.getString(2);
				labels[i] = (Integer.parseInt(names[i].substring(0, names[i].length()-4))/100);
				
				paths[i] = resultset.getString(3);
				
				color[i] = processArray(resultset.getString(4));
				
				texture[i] = processArray(resultset.getString(5));
				
				feature[i] = processArray(resultset.getString(6));
				i++;
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Retrieval Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
	
	public double[] processArray(String array){
    	String[] hist1 = array.split(" ");
    	int length = hist1.length;
    	double[] hist2 = new double[hist1.length];
    	
    	for(int k=0; k<length; k++){
    		hist2[k] = Double.parseDouble(hist1[k]);
    	}
    	return hist2;
    }
	
	public void closeDB(){
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
	
	public void printArray(double[][] array){
		int i=0, j=0;
		try{
			for(i=0; i<array.length; i++){
				for(j=0; j<array[0].length; j++){
					System.out.print(array[i][j]+" ");
				}
				System.out.println("");
			}
		}
		catch(Exception e){
			//System.out.println(i+" "+j);
			e.printStackTrace();
		}
	}

}
