package main.GeneticAlgorithm;

import main.OrderGenerator.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static main.Configurations.maxGeneration;
import static main.Configurations.populationSize;
public class GA {
    //The base for the Genetic Algorithm
    //The solution approach is cluster first route second
    public static int dim;
    public static int maxvehicles;
    public static ArrayList<Order> orders;
    public Population population;
    //Constructor
    public GA(int maxvehicles ,ArrayList<Order> orders){
        this.maxvehicles = maxvehicles;
        this.orders = new ArrayList<>();
        this.orders.addAll(orders);
        this.dim=this.orders.size()-1;
        this.population = new Population();
//        Collections.sort(this.orders, new Comparator<Order>() {
//            @Override
//            public int compare(Order o1, Order o2) {
//                return Integer.compare(o1.timeWindowEnd-o1.timeWindowStart,o2.timeWindowEnd-o2.timeWindowStart);
//            }
//        });
    }
    public void run(){
        for(int i=0;i<maxGeneration;i++){
            iterate();
        }
    }
    public void iterate(){
        population.sort();
        ArrayList<Individual> off = new ArrayList<>();
        ArrayList<Individual> temp = population.binarySelection();
        off.addAll(population.createOffspring(temp));
        population.individuals.clear();
        population.individuals.addAll(off);
        Collections.sort(population.individuals, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Integer.compare(o1.fitness,o2.fitness);
            }
        });
        int i=0;
        while(i<populationSize){
                population.individuals.remove(population.individuals.size()-1);
                i++;
        }
    }
    public void finalization(){
        population.best.printSolution();
        System.out.println("Total travelling time is: "+population.best.total_cost);
    }
}