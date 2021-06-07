package gapackagemain;

import kmeanspackage.KMeans;
import mainpackage.DataExtractor;

public class GA {
	
	public static DataExtractor de = new DataExtractor(2500);
	public static final int numclusters = 25;
	public static final int niter = 100;
	public static final double[][][] centroids = null;
	
	public static double[] getWeights(){
		//DataExtractor de = new DataExtractor(10);
		
		Population pop = new Population(5, true);
		//System.out.println("Pop Evolve Passed");
		pop.calcFit();
		//System.out.println("CalcFit Passed");
		Individual sol = pop.getFittest();
		
		int genval=1;
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        
        boolean run = true;
        int similarcount = 0;
        int generations = 100;
        int i=0; 
        

		/*de.printArray(de.color);
		System.out.println("\n");
		de.printArray(de.texture);
		System.out.println("\n");
		de.printArray(de.feature);
		System.out.println("\n");*/
        
        while((run==true)&&(i<generations)){
        	generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + 
            	pop.getFittest().getFitness());
            
            if(sol.getFitness()<pop.getFittest().getFitness()){
            	sol = pop.getFittest();
            	genval = generationCount;
            	similarcount=0;
            }
            else{
            	similarcount++;
            }
            pop = Algorithm.evolvePopulation(pop);
            if(similarcount>10){
            	run=false;
            }
            i++;
        }
        
        System.out.println("Optimal Solution found!");
        System.out.println("Generation: " + genval);
        System.out.println("Genes:");
        for(int j=0;j<sol.size();j++)
        	System.out.println(sol.getGene(j));
        System.out.println("\n"+sol.getFitness());
        return sol.getGenes();
	}

}
//520