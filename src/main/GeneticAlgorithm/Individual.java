package main.GeneticAlgorithm;

import main.Configurations;
import main.InputLoader;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static main.GeneticAlgorithm.GA.*;
import static main.GeneticAlgorithm.RouteBuilder.Delivery;
import static main.InputLoader.capacity;
import static main.InputLoader.costMatrix;

public class Individual {
    //An individual, or a solution is (dim-1)-long
    //The address id ranges from 1 to dim-1, or 0<=n<=dim-2
    //The nth element of the solution Arraylist holding value x (0<x<maxVehicles+1)
    //means the (n+1)th address will be served by vehicle x
    public ArrayList<Integer> solution;
    public RouteBuilder[] routeList;
    public ArrayList<InputLoader.Pair> return_epoch;
    public int fitness;
    public int total_cost;
    public int capacity_overload;
    public Individual(){
        this.solution= new ArrayList<>();
        this.fitness=-1;
        this.total_cost=0;
        this.served_orders=0;
        this.capacity_overload=0;
        this.routeList = new RouteBuilder[maxvehicles+1];
    }
    public void initialize(){
        Random rand = new Random();
        for(int i=0;i<dim;i++){
            this.solution.add(rand.nextInt(maxvehicles)+1);
        }
    }
    public int served_orders;
    public void calculateFitness() {
        int current_vehicle = 1;
        while (current_vehicle <= maxvehicles) {
            routeList[current_vehicle] = new RouteBuilder();
            for (int i = 0; i < dim; i++) {
                if (solution.get(i) == current_vehicle) {
                    routeList[current_vehicle].customerList.add(i+1);
                }
            }
            routeList[current_vehicle].buildRoute();
            current_vehicle++;
            }
        redistribute();
        current_vehicle--;
        while(current_vehicle >= 1){
            total_cost += routeList[current_vehicle].min_cost;
            served_orders += routeList[current_vehicle].max_served_orders;
            Delivery d = routeList[current_vehicle].head.next;
            capacity_overload+=max(0,routeList[current_vehicle].capacity-capacity);
            current_vehicle--;
        }
        this.fitness = total_cost-served_orders*10000+capacity_overload*80000;
    }
    public void redistribute() {
        for(int current_vehicle = 1; current_vehicle <=maxvehicles; current_vehicle++){
            if(routeList[current_vehicle].customerList.isEmpty()) continue;
            for(Integer i : routeList[current_vehicle].customerList){
                int vehicle_index=-1;
                Delivery add_before = new Delivery();
                int cost=900000;
                for(int v = 1; v <= maxvehicles && v != current_vehicle; v++){
                    Delivery d = routeList[v].tail;
                    while(d!=routeList[v].head){
                        int temp = d.prev.finish_time+costMatrix[orders.get(d.prev.order_ID).customerID][orders.get(i).customerID];
                        if(temp <= orders.get(i).timeWindowEnd && temp + costMatrix[orders.get(i).customerID][orders.get(d.order_ID).customerID] + orders.get(i).serviceTime <= d.start_time + d.delay){
                            int cost_if_insert = costMatrix[orders.get(d.prev.order_ID).customerID][orders.get(i).customerID]+costMatrix[orders.get(i).customerID][orders.get(d.order_ID).customerID]-costMatrix[orders.get(d.prev.order_ID).customerID][orders.get(d.order_ID).customerID];
                            if(cost_if_insert<cost){
                                vehicle_index=v;
                                add_before = d;
                                cost = cost_if_insert;
                            }
                        }
                        d=d.prev;
                    }
                }
                if(vehicle_index!=-1){
                    Delivery run = routeList[vehicle_index].head.next;
                    while(run!=add_before){
                        run = run.next;
                    }
                    Delivery delivery = new Delivery();
                    delivery.order_ID = i;
                    delivery.start_time = run.prev.finish_time;
                    delivery.finish_time = delivery.start_time+costMatrix[orders.get(run.prev.order_ID).customerID][orders.get(i).customerID]+orders.get(i).serviceTime;
                    delivery.delay = orders.get(i).timeWindowEnd - delivery.finish_time + orders.get(i).serviceTime;
                    run.prev.next = delivery;
                    delivery.prev = run.prev;
                    delivery.next = run;
                    run.prev = delivery;
                    int delay_reduction = max(0,delivery.finish_time- run.start_time);
                    if(delay_reduction>0){
                        while(run!= null){
                            run.start_time +=delay_reduction;
                            run.finish_time += delay_reduction;
                            run.delay -= delay_reduction;
                            run = run.next;
                        }
                    }
                    routeList[vehicle_index].capacity += orders.get(i).demand;
                    routeList[vehicle_index].max_served_orders += 1;
                    routeList[vehicle_index].min_cost += cost;
                }
            }
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
    public void printSolution(){
        this.return_epoch = new ArrayList<>();
        int current_vehicle = 1;
        while (current_vehicle <= maxvehicles) {
                System.out.println("Vehicle " + current_vehicle + ":");
                Delivery d = routeList[current_vehicle].head;
                System.out.print("Route is: ");
                while(d!=null){
                    System.out.print(orders.get(d.order_ID).customerID+" ");
                    d=d.next;
                }
                System.out.println();
//                if(!routeList[current_vehicle].customerList.isEmpty()) {
//                    System.out.print("Orders left unserved are: ");
//                    for (Integer i : routeList[current_vehicle].customerList) {
//                        System.out.print(i + " ");
//                    }
//                }
//                else System.out.print("Served all assigned orders");
//                System.out.println();
                System.out.println(routeList[current_vehicle].min_cost+" "+routeList[current_vehicle].capacity+" "+routeList[current_vehicle].max_served_orders);
                InputLoader.Pair p = new InputLoader.Pair();
                p.first= current_vehicle;
                p.second= routeList[current_vehicle].min_cost/ Configurations.Epoch_time+1;
                return_epoch.add(p);
            current_vehicle++;
        }
    }
}
