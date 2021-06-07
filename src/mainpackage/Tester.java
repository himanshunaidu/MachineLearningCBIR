package mainpackage;

import kmeanspackage.KMeans;

public class Tester {
	
	public static void Test1(){
		DataExtractor de = new DataExtractor(2500);
		
		//de.printArray(de.color);
		System.out.println("\n");
		//de.printArray(de.texture);
		System.out.println("\n");
		//de.printArray(de.feature);
		System.out.println("\n");
		
		//double[] weights = {0.5842294906081678,	0.35323817057612683, 0.07993613454407711};
		double[] weights = {0.2, 0.25, 0.55};
		KMeans kmean = new KMeans(de.color, de.texture, de.feature, weights);
		kmean.clustering(25, 100, null);
		int[] counts = kmean.getCounts();
		int count=0;
		for(int i=0; i<counts.length; i++){
			System.out.print(counts[i]+" ");
			count+=counts[i];
		}
		System.out.println("");
		kmean.setWithLabel(de.labels);
		
		KMeans.printClusters(kmean.getLabel());
		KMeans.printClusters(de.labels);
		double per = kmean.getPerformance();
		System.out.println("\n"+per);
		//System.out.println("\n"+count+" "+counts.length);
		//kmean.printResults();
	}

}
