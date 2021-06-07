package gapackagemain;

import mainpackage.*;
import java.util.Scanner;

import kmeanspackage.KMeans;

public class FitnessCalc {

	    /* Public methods */
	    // Set a candidate solution as a byte array

	    // To make it easier we can use this method to set our candidate solution
	    // with string of 0s and 1s

	    // Calculate individuals fitness by comparing it to our candidate solution
	    static double getFitness(Individual ind) {
	        KMeans k = new KMeans(GA.de.color, GA.de.texture, GA.de.feature, ind.getGenes());
	        //System.out.println("Kmeans Passed");
	    	k.clustering(GA.numclusters, GA.niter, GA.centroids);
	    	//System.out.println("Clustering Passed");
	    	k.setWithLabel(GA.de.labels);
	    	//System.out.println(k.getPerformance());
	    	return k.getPerformance();
	    }
}
