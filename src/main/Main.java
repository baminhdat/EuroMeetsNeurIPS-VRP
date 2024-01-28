package main;


import main.GeneticAlgorithm.GA;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.max;
import static main.Configurations.*;
import static main.InputLoader.*;

public class Main {
    public static ArrayList<OrderGenerator.Order> orders = new ArrayList<>();
    public static ArrayList<InputLoader.Pair> awayVehicles = new ArrayList<>();
    public static ArrayList<Integer> availableVehicles = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        InputLoader.input_file_name();
        InputLoader.load();
        System.out.println(InputLoader.problemName);
        for(int i=1;i <= maxVehicles;i++){
            availableVehicles.add(1);
        }
        //Static Variant
        if(OrderGenerator.mode.compareTo("Static")==0) {
            System.out.println("Static");
            orders.addAll(OrderGenerator.staticMode());
            int vehicles_number=availableVehicles.size();
            long start = System.nanoTime();
            while(true){
            GA solver = new GA(vehicles_number,orders);
            solver.run();
            if(solver.population.best.served_orders==solver.dim){
                long end = System.nanoTime();
                System.out.println("The GA ends in approximately " + (end - start) / 1000000000 + "s");
                solver.finalization();
                break;
            }
            else{
                System.out.println("Failed to solve with "+vehicles_number+" vehicles");
                vehicles_number++;
            }
            }
        }
        //Dynamic Variant
        if(OrderGenerator.mode.compareTo("Dynamic")==0){
            System.out.println("Dynamic");
            //How many epoch ?
            int max_epoch=0;
            for(int i=1;i<InputLoader.dim;i++){
                int tmp = (timeWindow[i].second-Prep_time)/Epoch_time;
                if(tmp>max_epoch) max_epoch=tmp;
            }
            //Start
            int current_epoch=0;
            for(int i=1;i<InputLoader.dim;i++){
                int tmp = (timeWindow[i].first-Prep_time)/Epoch_time;
                if(tmp<current_epoch) current_epoch=tmp;
            }
            int avg_max_vehicles= maxVehicles/3;
            //The tactic is dispatch or must - go orders and some of the closest orders
            ArrayList<OrderGenerator.Order> cur_epoch_orders = new ArrayList<>();
            timeWindow[0].first+=Prep_time;
            System.out.println("Epoch starts from "+current_epoch+" to "+max_epoch);
            int failed = 0;
            while(current_epoch<max_epoch-1){
                System.out.println("Current Epoch is: "+current_epoch);
                orders.addAll(OrderGenerator.dynamicMode(current_epoch));
                //Vehicles
                if(!awayVehicles.isEmpty()){
                    for(Pair p : awayVehicles){
                        p.second-=1;
                    }
                    for(int i=0; i< awayVehicles.size();i++){
                        if(awayVehicles.get(i).second==0){
                            availableVehicles.set(awayVehicles.get(i).first,1);
                            awayVehicles.remove(i);
                        }
                    }
                }
                //Choose orders to dispatch
                for(OrderGenerator.Order o: orders){
                if(max(current_epoch*Epoch_time+Prep_time+costMatrix[0][o.customerID],o.timeWindowStart)+Epoch_time>o.timeWindowEnd){
                    cur_epoch_orders.add(o);
                    }
                }
                orders.removeAll(cur_epoch_orders);
                orders.sort(new Comparator<OrderGenerator.Order>() {
                        @Override
                        public int compare(OrderGenerator.Order o1, OrderGenerator.Order o2) {
                            return Integer.compare(o1.timeWindowEnd,o2.timeWindowEnd);
                        }
                });
                //Start Solving
                if(cur_epoch_orders.isEmpty()){
                    current_epoch++;
                    continue;
                }
                int max = availableVehicles.size()-awayVehicles.size();
                int additional_vehicles=0;
                while(true){
                if(avg_max_vehicles+additional_vehicles>max){
                    System.out.println("Failed");
                    failed = 1;
                    break;
                }
                GA solver = new GA(avg_max_vehicles+additional_vehicles,cur_epoch_orders);
                solver.run();
                if(solver.population.best.served_orders<cur_epoch_orders.size()){
                    System.out.println("Failed to solve with "+(avg_max_vehicles+additional_vehicles)+" vehicles");
                    additional_vehicles+=1;
                }
                else{
                    int i = 0;
                    int vehicle_index = 0;
                    while(i<avg_max_vehicles+additional_vehicles){
                        if(availableVehicles.get(vehicle_index)==0){
                            vehicle_index+=1;
                        }
                        else{
                            availableVehicles.set(vehicle_index,0);
                            i++;
                        }
                    }
                    solver.finalization();
                    awayVehicles.addAll(solver.population.best.return_epoch);
                    cur_epoch_orders.clear();
                    break;
                }
                }
                if(failed==1){
                    break;
                }
                current_epoch++;
                timeWindow[0].first+=Epoch_time;
            }
            if(!awayVehicles.isEmpty()){
                for(Pair p : awayVehicles){
                    p.second-=1;
                }
                for(int i=0; i< awayVehicles.size();i++){
                    if(awayVehicles.get(i).second==0){
                        availableVehicles.set(awayVehicles.get(i).first,1);
                        awayVehicles.remove(i);
                    }
                }
            }
            orders.addAll(OrderGenerator.dynamicMode(max_epoch));
            cur_epoch_orders.addAll(orders);
            int max = availableVehicles.size()-awayVehicles.size();
            GA solver = new GA(max,orders);
            solver.run();
            for(OrderGenerator.Order o : orders ){
                System.out.print(o.customerID+" ");
            }
        }
        }
}