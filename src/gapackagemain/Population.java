package gapackagemain;

import java.util.Random;

public class Population {
	Individual[] individuals;

    /*
     * Constructors
     */
    // Create a population
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize; i++) {
                Individual newIndividual = new Individual();
                if(i==0){
                	newIndividual.generateIndividual(false);
                }
                else{
                	newIndividual.generateIndividual(true);
                }
                for(int j=0;j<newIndividual.size();j++)
                	System.out.print(newIndividual.getGene(j)+" ");
                System.out.println();
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }
    
    public int size() {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find the most fit
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness(
            		) <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        //System.out.println("Fittest "+fittest);
        return fittest;
    }
    public double getMaxFit(){
    	double ret=0;
    	for(int i=0;i<size();i++){
    		if(ret<getIndividual(i).getFitness()){
    			ret = getIndividual(i).getFitness();
    		}
    	}
    	return ret;
    }
    public void calcFit(){
    	for (int i = 0; i < size(); i++) {
            getIndividual(i).getFitness();
        }
    }


}
