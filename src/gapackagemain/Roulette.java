package gapackagemain;

import java.util.Random;

public class Roulette {

    public static Individual rouletteSelection(Population pop)
    {
        float totalScore = 0;
        float runningScore = 0;
        for (int i=0;i<pop.size();i++)
        {
            totalScore += pop.getIndividual(i).getFitness();
        }

        float rnd = (float) (Math.random() * totalScore);

        for (int i=0;i<pop.size();i++)
        {   
            if (    rnd>=runningScore &&
                    rnd<=runningScore+pop.getIndividual(i).getFitness())
            {
                return pop.getIndividual(i);
            }
            runningScore+=pop.getIndividual(i).getFitness();
        }
        return null;
    }
}
