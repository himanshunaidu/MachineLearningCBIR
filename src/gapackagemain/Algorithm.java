package gapackagemain;

import java.util.ArrayList;
import java.util.Random;

public class Algorithm {

    private static ArrayList<Integer> outerSegmentBuildArray;

    /* Public methods */
    
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);

        // Keep our best individual
        // Loop over the population size and create new individuals with
        // crossover
        Individual indiv1 = new Individual();
        Individual indiv2 = new Individual();
        
        for (int i = 0; i < pop.size(); i++) {
        	
            //Individual indiv1 = rouletteSelection(pop);
            //Individual indiv2 = rouletteSelection(pop);
        	indiv1 = rouletteSelection(pop);
        	//System.out.println(indiv1.getFitness());
            indiv2 = rouletteSelection(pop);
            //System.out.print("indiv1 ");
            //for(int j=0;j<indiv1.size();j++)
            //	System.out.print(indiv1.getGene(j)+" ");
            
            //System.out.println();
            //System.out.print("indiv2 ");
            //for(int j=0;j<indiv2.size();j++)
            //	System.out.print(indiv2.getGene(j)+" ");
            Individual newIndiv;
            if(indiv1.getFitness()<indiv2.getFitness())
            	newIndiv = crossover(indiv1,indiv2);
            else
            	newIndiv = crossover(indiv2,indiv1);
            newPopulation.saveIndividual(i, newIndiv);
            //System.out.println();
            //for(int j=0;j<newIndiv.size();j++)
            //	System.out.print(newIndiv.getGene(j)+" ");
            //System.out.println();
        }

        // Mutate population
        for (int i = 0; i < newPopulation.size(); i++) {
        	if(i%2==0)
        		mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }
    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
    	Random r = new Random();
        int x = r.nextInt(indiv.size());
        int y = r.nextInt(indiv.size());
        double X = indiv.getGene(x);
        double Y = indiv.getGene(y);
        indiv.setGene(y, X);
        indiv.setGene(x, Y);
    }

    // Select individuals for crossover
    public static Individual rouletteSelection(Population pop)
    {
        double totalScore = 0;
        double runningScore = 0;
        double invScore=0;
        for (int i=0;i<pop.size();i++)
        {        	
        	invScore=pop.getMaxFit()-pop.getIndividual(i).getFitness();
        	//System.out.println(invScore);
            totalScore += invScore;
        }
        float rnd = (float) (Math.random() * totalScore);
        //System.out.print("totscr = ");
        //System.out.println(totalScore);
        //System.out.print("rnd = ");
        //System.out.println(rnd);
        for (int i=0;i<pop.size();i++)
        {   
            if (    rnd>=runningScore &&
                    rnd<=runningScore+pop.getMaxFit()-pop.getIndividual(i).getFitness())
            {
                return pop.getIndividual(i);
            }
            //System.out.print("rn scr = ");
            //System.out.println(runningScore);
            runningScore+=pop.getMaxFit()-pop.getIndividual(i).getFitness();
        }
        return null;
    }
    public static Individual crossover(Individual p1,Individual p2){
		Individual x = new Individual();
		Random r = new Random();
		int c1 = r.nextInt(p1.size());
		int c2 = r.nextInt(p1.size());
		if(c1>c2){
			int temp = c1;
			c1=c2;
			c2=temp;
		}
		for(int i=0;i<c1;i++){
			x.setGene(i, p1.getGene(i));
		}
		for(int i=c2+1;i<p1.size();i++){
			x.setGene(i, p1.getGene(i));
		}
		double[] l = new double[c2-c1+1];
		double[] m = new double[c2-c1+1];
		
		for(int i=c1;i<=c2;i++){
			l[i-c1]=p1.getGene(i);
			for(int j=0;j<p2.size();j++){
				if(p2.getGene(j)==l[i-c1]){
					m[i-c1]=j;
					break;
				}
			}
		}
		for(int i=0;i<l.length;i++){
			for(int j=0;j<l.length-i-1;j++){
				if(m[j]<m[j+1]){
					double temp = m[j];
					m[j] = m[j+1];
					m[j+1] = temp;
					temp = l[j];
					l[j] = l[j+1];
					l[j+1] = temp;
				}
			}
		}
		for(int i=c1;i<=c2;i++){
			x.setGene(i, l[i-c1]);
		}
    	return x;
    }

}

