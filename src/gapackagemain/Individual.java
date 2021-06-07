package gapackagemain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Individual {
	static int defaultGeneLength = 3;
    private double[] genes = new double[defaultGeneLength];
    // Cache
    private double fitness = 0;

    // Create a random individual
    public void generateIndividual(boolean random) {
    	Random r = new Random();
    	double gene;
    	if(random==false){
    		double[] genes2 = {0, 0, 1};
    		genes = genes2;
    	}
    	else{
    		for(int i=0; i<defaultGeneLength; i++){
    			gene = r.nextDouble();
    			genes[i] = gene;
    		}
    	}
    }

    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = 3;
    }
    
    public double getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, double value) {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
    public int size() {
        return genes.length;
    }

	public double[] getGenes() {
		return genes;
	}

	public void setGenes(double[] genes) {
		this.genes = genes;
	}
	
	public double getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }
}
