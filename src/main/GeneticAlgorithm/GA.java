package main.GeneticAlgorithm;

import main.OrderGenerator.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        this.dim=this.orders.size();
        this.orders.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Integer.compare(o1.timeWindowEnd,o2.timeWindowEnd);
            }
        });
        this.population = new Population();
    }
    public void iterate(){
        ArrayList<Individual> temp = population.selection();
        ArrayList<Individual> off = population.createOffspring(temp);
        population.individuals.addAll(off);
        Collections.sort(population.individuals, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Long.compare(o1.fitness,o2.fitness);
            }
        });
        int i=0;
        while(i<populationSize){
                population.individuals.remove(population.individuals.size()-1);
                i++;
        }
    }
    public ArrayList<Order> finalization(){
        ArrayList<Order> served_orders = population.best.printSolution123();
        System.out.println("Total travelling time is: "+population.best.total_cost);
        System.out.println("Number of orders left unserved is: "+(dim-population.best.served_orders));
        return served_orders;
    }
}