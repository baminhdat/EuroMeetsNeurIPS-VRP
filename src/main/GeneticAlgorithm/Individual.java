package main.GeneticAlgorithm;

import main.Configurations;
import main.InputLoader;
import main.OrderGenerator;

import java.util.ArrayList;
import java.util.Random;

import static main.GeneticAlgorithm.GA.*;
public class Individual {
    //An individual, or a solution is (dim-1)-long
    //The address id ranges from 1 to dim-1, or 0<=n<=dim-2
    //The nth element of the solution Arraylist holding value x (0<x<maxVehicles+1)
    //means the (n+1)th address will be served by vehicle x
    public ArrayList<Integer> solution;
    public ArrayList<InputLoader.Pair> return_epoch;
    public long fitness;
    public long total_cost;
    public long total_capacity_overload;
    public long total_timewarp;
    public Individual(){
        this.solution= new ArrayList<>();
        this.fitness=-1;
        this.total_cost=0;
        this.total_capacity_overload=0;
        this.total_timewarp=0;
        this.served_orders=0;
    }
    public void initialize(){
        Random rand = new Random();
        for(int i=0;i<dim;i++){
            this.solution.add(rand.nextInt(maxvehicles)+1);
        }
    }
    public int served_orders;
    public void calculateFitness123() {
        int current_vehicle = 1;
        while (current_vehicle <= maxvehicles) {
            ArrayList<Integer> customerList = new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                if (solution.get(i) == current_vehicle) {
                    customerList.add(i);
                }
            }
            if (!customerList.isEmpty()) {
                RouteBuilder routeBuilder = new RouteBuilder(customerList);
                routeBuilder.buildRoute1();
                total_cost += routeBuilder.min_cost;
                served_orders += routeBuilder.max_served_orders;
            }
            current_vehicle++;
            this.fitness = total_cost-served_orders*50000;
        }
    }
    public void mutate() {
        Random rand = new Random();
        if (Math.random() < 0.1) {
            int s1 = rand.nextInt(dim);
            int s2 = rand.nextInt(dim);
            int tmp = solution.get(s1);
            solution.set(s1,solution.get(s2));
            solution.set(s2,tmp);
        }
    }
    public ArrayList<OrderGenerator.Order> printSolution123(){
        this.return_epoch = new ArrayList<>();
        ArrayList<OrderGenerator.Order> served = new ArrayList<>();
        //for(Integer i: solution){
        //    System.out.print(i+" ");
        //}
        //System.out.println();
        int current_vehicle = 1;
        while (current_vehicle <= maxvehicles) {
            ArrayList<Integer> customerList = new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                if (solution.get(i) == current_vehicle) {
                    customerList.add(i);
                }
            }
            if (!customerList.isEmpty()) {
                RouteBuilder routeBuilder = new RouteBuilder(customerList);
                routeBuilder.buildRoute1();
                //routeBuilder.finalization();
                System.out.println("Vehicle " + current_vehicle + ":");
                System.out.print("Orders left unserved are: ");
                for (Integer i: routeBuilder.customerList){
                    System.out.print(i+" ");
                }
                System.out.println();
                System.out.print("Orders served are: ");
                for (Integer i : routeBuilder.route) {
                    System.out.print(orders.get(i).customerID + " ");
                    served.add(orders.get(i));
                }
                //System.out.print(0);
                System.out.println();
                System.out.println(routeBuilder.min_cost+" "+routeBuilder.max_served_orders);
                InputLoader.Pair p = new InputLoader.Pair();
                p.first=current_vehicle;
                p.second= routeBuilder.min_cost/ Configurations.Epoch_time+1;
                return_epoch.add(p);
            }
            current_vehicle++;
        }
        return served;
    }
}
