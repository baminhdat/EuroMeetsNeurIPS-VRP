package main.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static main.Configurations.populationSize;
import static main.GeneticAlgorithm.GA.dim;

public class Population {
    public Individual best = new Individual();
    public ArrayList<Individual> individuals;
    public Population(){
        this.individuals=new ArrayList<>();
        int i=0;
        while(i<populationSize){
            Individual individual = new Individual();
            individual.initialize();
            individual.calculateFitness123();
            this.individuals.add(individual);
            i++;
        }
    }
    public ArrayList<Individual> selection(){
        ArrayList<Individual> selected = new ArrayList<Individual>();
        Collections.sort(individuals, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Long.compare(o1.fitness,o2.fitness);
            }
        });
        if(best.fitness==-1){
            best=individuals.get(0);
        }
        else if(best.fitness>individuals.get(0).fitness){
            best=individuals.get(0);
        }
        for(int i=0;i<populationSize/2;i++){
            selected.add(individuals.get(i));
        }
        return selected;
    }
    public ArrayList<Individual> createOffspring(ArrayList<Individual> selected){
        ArrayList<Individual> offspring = new ArrayList<Individual>();
        int i=0;
        while(i<populationSize){
            Individual p1 = selected.get((int)Math.random()*selected.size());
            Individual p2 = selected.get((int)Math.random()*selected.size());
            Individual p3 = new Individual();
            for(int j=0;j<dim;j++){
                if(Math.random()>0.5){
                    p3.solution.add(p1.solution.get(j));
                }
                else{
                    p3.solution.add(p2.solution.get(j));
                }
            }
            p3.mutate();
            p3.calculateFitness123();
            offspring.add(p3);
            i++;
        }
        return offspring;
    }

}
