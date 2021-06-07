package kmeanspackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.StringReader;

public class KMeans 
{
  // Data members
  private double [][][] _data; // Array of all records in dataset
  private int [] _label;  // generated cluster labels
  private int[] _label2;
  private int[] _label3;
  private int [] _withLabel; // if original labels exist, load them to _withLabel
                              // by comparing _label and _withLabel, we can compute accuracy. 
                              // However, the accuracy function is not defined yet.
  private double [][][] _centroids; // centroids: the center of clusters
  private int _nrows, _ndims; // the number of rows and dimensions
  private int _numClusters; // the number of clusters;
  private int[] counts;
  
  private double[] _weights; 

  // Constructor; loads records from file <fileName>. 
  // if labels do not exist, set labelname to null
  public KMeans(double[][] col, double[][] texture, double[][] feature, double[] weights) 
  {
	  int trueLength = (int) (col.length);
	  System.out.println(trueLength);
	  _data = new double[trueLength][3][];
	  
	  _nrows = trueLength;
	  _ndims = 3;
	  
	  _weights = weights;
	  
	  for(int i=0; i<trueLength; i++){
		  _data[i][0] = new double[col[0].length];
		  _data[i][1] = new double[texture[0].length];
		  _data[i][2] = new double[feature[0].length];
	  }
	  for(int i=0; i<trueLength; i++){
		  /*_data[i][0] = col[i];
		  _data[i][1] = texture[i];
		  _data[i][2] = feature[i];*/
		  for(int j=0; j<col[0].length; j++){
			  _data[i][0][j] = col[i][j];
		  }
		  for(int j=0; j<texture[0].length; j++){
			  _data[i][1][j] = texture[i][j];
		  }
		  for(int j=0; j<feature[0].length; j++){
			  _data[i][2][j] = feature[i][j];
		  }
	  }
	  

  }
  
  // Perform k-means clustering with the specified number of clusters and
  // Eucliden distance metric. 
  // niter is the maximum number of iterations. If it is set to -1, the kmeans iteration is only terminated by the convergence condition.
  // centroids are the initial centroids. It is optional. If set to null, the initial centroids will be generated randomly.
  public void clustering(int numClusters, int niter, double [][][] centroids) 
  {
      _numClusters = numClusters;
      if (centroids !=null)
          _centroids = centroids;
      else{
        // randomly selected centroids
        _centroids = new double[_numClusters][3][];

        ArrayList idx= new ArrayList();
        double product = _data.length/numClusters;
        //System.out.println(product);
        for (int i=0; i<numClusters; i++){
          
        	int c;
        	do{
        		//Added 50 for middle value. Can be removed
        		c = (int) ((int) (Math.random())+(product*i)+50);
        		//System.out.println(c+" "+i);
        	}while(idx.contains(c));
        	// avoid duplicates
        	idx.add(c);
        	
        	// copy the value from _data[c]
        	_centroids[i] = new double[_ndims][];
        	_centroids[i][0] = new double[_data[i][0].length];
        	_centroids[i][1] = new double[_data[i][1].length];
        	_centroids[i][2] = new double[_data[i][2].length];
		  
        	for(int j=0; j<_centroids[i][0].length; j++){
        		_centroids[i][0][j] = _data[i][0][j];
        	}	
        	for(int j=0; j<_centroids[i][1].length; j++){
        		_centroids[i][1][j] = _data[i][1][j];
        	}
        	for(int j=0; j<_centroids[i][2].length; j++){
        		_centroids[i][2][j] = _data[i][2][j];
        	}
        	//System.out.println("GA Loop Centroid 3 Ended");
        	//System.out.println(i+"->"+numClusters);
        }
        //System.out.println("Selected random centroids");

      	}	
      
      	//printCentroids(_centroids);
      	double[][][] oricentroids = _centroids;

      	double [][][] c1 = _centroids;
      	double threshold = 0.001;
      	int round=0;

      	while (true){
      		// update _centroids with the last round results
      		_centroids = c1;
      		
      		//assign record to the closest centroid
      		//System.out.println(_nrows);
      		_label = new int[_nrows];
      		_label2 = new int[_nrows];
      		_label3 = new int[_nrows];
      		for (int i=0; i<_nrows; i++){
      			_label[i] = closest(_data[i]);
      			_label2[i] = closest2(_data[i], _label[i]);
      			_label3[i] = closest3(_data[i], _label2[i]);
      			//System.out.print(_label[i]+" ");
      		}
      		//System.out.println("");
        
      		// recompute centroids based on the assignments  
      		c1 = updateCentroids();
      		
      		round ++;
      		if ((niter >0 && round >=niter) || converge(_centroids, c1, threshold))
      			break;
      	}	

      	System.out.println("Clustering converges at round " + round);
      
      	//printClusters();
      
      	//printCentroids(_centroids, oricentroids);
  }

  // find the closest centroid for the record v 
  private int closest(double [][] v){
    double mindist = distmain(v, _centroids[0]);
    int label =0;
    for (int i=1; i<_numClusters; i++){
      double t = distmain(v, _centroids[i]);
      if (mindist>t){
        mindist = t;
        label = i;
      }
    }
    return label;
  }
  
  private int closest2(double[][] v, int low){
	  double mindist = distmain(v, _centroids[0]);
	    int label =0;
	    for (int i=1; i<_numClusters; i++){
	      double t = distmain(v, _centroids[i]);
	      if ((mindist>t)&&(t>low)){
	        mindist = t;
	        label = i;
	      }
	    }
	    return label;
  }
  
  private int closest3(double[][] v, int low2){
	  double mindist = distmain(v, _centroids[0]);
	    int label =0;
	    for (int i=1; i<_numClusters; i++){
	      double t = distmain(v, _centroids[i]);
	      if ((mindist>t)&&(t>low2)){
	        mindist = t;
	        label = i;
	      }
	    }
	    return label;
  }
  
  private double distmain(double[][] mat1, double[][] mat2){
	  
	  if(mat1.length==mat2.length){
		  double distmain = 0;
		  double[] dists = new double[mat1.length];
		  for(int l=0; l<mat1.length; l++){
			  dists[l] = dist(mat1[l], mat2[l]);
			  distmain += dists[l]*_weights[l];
		  }
		  return distmain;
	  }
	  return Double.MAX_VALUE;
  }

  // compute Euclidean distance between two vectors v1 and v2
  private double dist(double [] v1, double [] v2){
    double sum=0;
    for (int i=0; i<_ndims; i++){
      double d = v1[i]-v2[i];
      sum += d*d;
    }
    return Math.sqrt(sum);
  }

  // according to the cluster labels, recompute the centroids 
  // the centroid is updated by averaging its members in the cluster.
  // this only applies to Euclidean distance as the similarity measure.

  private double [][][] updateCentroids(){
    // initialize centroids and set to 0
    double [][][] newc = new double [_numClusters][][]; //new centroids 
    counts = new int[_numClusters]; // sizes of the clusters

    // intialize
    for (int i=0; i<_numClusters; i++){
      counts[i] =0;
      newc[i] = new double [_ndims][];
      
      newc[i][0] = new double[_data[i][0].length];
      newc[i][1] = new double[_data[i][1].length];
      newc[i][2] = new double[_data[i][2].length];
      
      
      for (int k=0; k<_data[i][0].length; k++)
        newc[i][0][k] =0;
      for (int k=0; k<_data[i][1].length; k++)
          newc[i][1][k] =0;
      for (int k=0; k<_data[i][1].length; k++)
          newc[i][1][k] =0;
    }

    ArrayList<Integer> labels = new ArrayList<Integer>();
    ArrayList<Integer> labels2 = new ArrayList<Integer>();
    for(int i=0; i<_label.length; i++){
    	labels2.add(_label[i]);
    }
    for (int i=0; i<_nrows; i++){
    	
    	int cn = _label[i]; // the cluster membership id for record i
    	for (int j=0; j<_data[i][0].length; j++){
    		newc[cn][0][j] += _data[i][0][j]; // update that centroid by adding the member data record
    	}
    	for (int j=0; j<_data[i][1].length; j++){
    		newc[cn][1][j] += _data[i][1][j]; // update that centroid by adding the member data record
    	}
    	for (int j=0; j<_data[i][2].length; j++){
    		newc[cn][2][j] += _data[i][2][j]; // update that centroid by adding the member data record
    	}
      counts[cn]++;
    }

    // finally get the average
    for (int i=0; i< _numClusters; i++){
      for(int j=0; j<_data[i][0].length; j++){
    	  newc[i][0][j]/=counts[i];
      }
      for(int j=0; j<_data[i][1].length; j++){
    	  newc[i][1][j]/=counts[i];
      }
      for(int j=0; j<_data[i][2].length; j++){
    	  newc[i][2][j]/=counts[i];
      }
    } 
    //printCentroids(newc);
    return newc;
  }

  // check convergence condition
  // max{dist(c1[i], c2[i]), i=1..numClusters < threshold
  private boolean converge(double [][][] c1, double [][][] c2, double threshold){
    // c1 and c2 are two sets of centroids 
    double maxv = 0;
    for (int i=0; i< _numClusters; i++){
        double d= distmain(c1[i], c2[i]);
        if (maxv<d)
            maxv = d;
    } 

    if (maxv <threshold)
      return true;
    else
      return false;
    
  }
  public double[][][] getCentroids()
  {
    return _centroids;
  }
  
  public int[] getCounts(){
	  return counts;
  }

  public int [] getLabel()
  {
    return _label;
  }

  public int nrows(){
    return _nrows;
  }

  public void printResults(){
      System.out.println("Label:");
     for (int i=0; i<_nrows; i++)
        System.out.println(_label[i]);
     System.out.println("Centroids:");
     for (int i=0; i<_numClusters; i++){
    	System.out.print((i+1)+": ");
        for(int j=0; j<_ndims; j++){
        	for(int k=0; k<_centroids[i][j].length; k++){
        		System.out.print(_centroids[i][j][k] + " ");
        	}
        }
           
        System.out.println("\n"+counts[i]);
     }

  }
  
  public static void printCentroids(double[][][] centroids, double[][][] oricentroids){
	  int i=0, k=0;
	  try{
	  for(i=0; i<centroids.length; i++){
			System.out.println((i+1)+"");
			for(k=0; k<centroids[i][2].length; k++){
				System.out.print(centroids[i][2][k]+" ");
			}
			System.out.println("");
			for(k=0; k<centroids[i][2].length; k++){
				System.out.print(oricentroids[i][2][k]+" ");
			}
			System.out.println("");
    }}
	  catch(Exception e){
		  System.out.println(i+" "+k);
		  e.printStackTrace();
		  System.exit(1);
	  }
  }
  
  public static void printClusters(int[] _label){
	  int length = _label.length;
	  System.out.println(length);
	  //System.out.println(_label[2]);
	  for(int i=0; i<(length/10); i++){
		  System.out.print(_label[i]+" ");
	  }
	  System.out.println("");
  }


  /*public static void main( String[] astrArgs ) 
  {
    
     KMeans KM = new KMeans();
     KM.clustering(2, 10, null); // 2 clusters, maximum 10 iterations
     KM.printResults();

     /** using CSVHelper to parse strings
     CSVHelper csv = new CSVHelper();
     StringReader r= new StringReader("x,y,z");
     try{
       ArrayList<String> ss = csv.parseLine(r);
       for (String v:ss)
        System.out.println(v);
     }catch(Exception e){
       System.err.println(e);
     }
     

  }*/
  
  public void setWithLabel(int[] labels){
	  this._withLabel = labels;
  }
  
  public double getPerformance(){
	  double performance=0;
	  int length = (this._label.length>this._withLabel.length)?this._withLabel.length:this._label.length;
	  for(int i=0; i<length; i++){
		  if((_label[i]==_withLabel[i])||(_label2[i]==_withLabel[i])||(_label3[i]==_withLabel[i])){
			  performance+=1;
		  }
	  }
	  return (performance/length);
  }
}
