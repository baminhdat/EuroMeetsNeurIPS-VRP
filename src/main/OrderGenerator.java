package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.max;
import static main.Configurations.Epoch_time;
import static main.Configurations.Prep_time;
import static main.InputLoader.*;
public class OrderGenerator {
    public static class Order{
        public int customerID;
        public int timeWindowStart;
        public int timeWindowEnd;
        public int serviceTime;
        public int demand;
    }
    public static String mode;
    public static void chooseMode(){
        Scanner input = new Scanner(System.in);
        mode = input.nextLine();
        input.close();
    }
    public static ArrayList<Order> staticMode(){
        ArrayList<Order> orders = new ArrayList<>();
        for(int i=0;i<dim;i++){
        Order o = new Order();
        o.customerID=i;
        o.timeWindowStart=timeWindow[i].first;
        o.timeWindowEnd=timeWindow[i].second;
        o.demand=demand[i];
        o.serviceTime=serviceTime[i];
        orders.add(o);
        }
        return orders;
    }
    public static ArrayList<Order>  dynamicMode(int current_epoch){
        ArrayList<Order> orders = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<100;i++){
            Order o = new Order();
            o.customerID=rand.nextInt(dim-1)+1;
            int tmp = rand.nextInt(dim-1)+1;
            o.timeWindowStart=timeWindow[tmp].first;
            o.timeWindowEnd=timeWindow[tmp].second;
            tmp = rand.nextInt(dim-1)+1;
            o.demand=demand[tmp];
            tmp = rand.nextInt(dim-1)+1;
            o.serviceTime=serviceTime[tmp];
            boolean valid = true;
            int earliestArrival = max(current_epoch*Epoch_time+Prep_time+costMatrix[0][o.customerID],o.timeWindowStart);
            if(earliestArrival>o.timeWindowEnd){
                valid = false;
            }
            if(earliestArrival+o.serviceTime+costMatrix[o.customerID][0]>timeWindow[0].second){
                valid = false;
            }
            if(valid){
            orders.add(o);
            }
        }
        return orders;
    }
}
