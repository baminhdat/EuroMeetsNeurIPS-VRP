package main.GeneticAlgorithm;

import main.InputLoader;

import java.util.ArrayList;

import static java.lang.Math.max;
import static main.GeneticAlgorithm.GA.orders;
import static main.InputLoader.costMatrix;
import static main.InputLoader.timeWindow;
public class RouteBuilder {
    public class Detail {
        public int cost;
        public int served;
    }
//    //public long cost;
//    public long capacity_overload;
//    public long timewarp;
    public ArrayList<Integer> customerList;
    public ArrayList<Integer> route;
    public int size;
    public int capacity;
    public RouteBuilder(ArrayList<Integer> customerlist){
        this.size=customerlist.size();
        this.customerList = new ArrayList<Integer>(this.size);
        this.customerList.addAll(customerlist);
        this.route = new ArrayList<Integer>();
        //this.route.addAll(customerlist);
//        this.max_served_orders=this.evaluate().served;
//        this.min_cost=this.evaluate().cost;
        this.min_cost=0;
        this.max_served_orders=0;
        this.capacity=0;
    }
    public void buildRoute1(){
        int cur = 0;
        int time_now=timeWindow[0].first;
        while(!customerList.isEmpty()){
            int next=-1;
            int min_invalidate=900;
            for(Integer i : customerList) {
                if (time_now + costMatrix[cur][orders.get(i).customerID] <= orders.get(i).timeWindowEnd&&capacity+orders.get(i).demand<=InputLoader.capacity) {
                    int invalidate = 0;
                    int spec_time = max(time_now + costMatrix[cur][orders.get(i).customerID], orders.get(i).timeWindowStart) + orders.get(i).serviceTime;
                    for (Integer j : customerList) {
                        if (spec_time > orders.get(j).timeWindowEnd && (!j.equals(i))) {
                            invalidate += 1;
                        }
                    }
                    if (invalidate < min_invalidate) {
                        next = i;
                        min_invalidate = invalidate;
                    }
                }
            }
            if(next!=-1){
            time_now = max(time_now+costMatrix[cur][next],orders.get(next).timeWindowStart)+orders.get(next).serviceTime;
            capacity+=orders.get(next).demand;
            customerList.remove(Integer.valueOf(next));
            min_cost+=costMatrix[cur][next];
            max_served_orders+=1;
            route.add(next);
            cur=next;}
            else break;
        }
        min_cost+=costMatrix[cur][0];
    }
    public void buildRoute(){
        for(int i=0;i<size;i++){
            for(int j=i+1;j<size;j++){
                int temp = customerList.get(i);
                customerList.set(i,customerList.get(j));
                customerList.set(j,temp);
                Detail d = evaluate();
                if(d.served>max_served_orders){
                    max_served_orders=d.served;
                    min_cost=d.cost;
                    route.clear();
                    route.addAll(customerList);
                }
                else if(d.served==max_served_orders){
                    if(min_cost>d.cost){
                        min_cost=d.cost;
                        route.clear();
                        route.addAll(customerList);
                    }
                }
            }
        }
    }
    public void finalization(){
        int time_now=timeWindow[0].first;
        int tmp_capacity=0;
        int cur_address=0;
        customerList.clear();
        customerList.addAll(route);
        route.clear();
        for(Integer i: customerList){
            if(tmp_capacity+orders.get(i).demand<=InputLoader.capacity){
                if(time_now+costMatrix[cur_address][orders.get(i).customerID]<=orders.get(i).timeWindowEnd){
                    route.add(i);
                    tmp_capacity+=orders.get(i).demand;
                    time_now=max(time_now+costMatrix[cur_address][orders.get(i).customerID],orders.get(i).timeWindowStart)+orders.get(i).serviceTime;
                    cur_address=orders.get(i).customerID;
                }
            }
        }
    }
    public Detail evaluate(){
        int tmp_capacity=0;
        int tmp_cost=0;
        int tmp_served=0;
        int cur_address=0;
        int time_now=timeWindow[0].first;
        for(Integer i : customerList){
            if(tmp_capacity+orders.get(i).demand<=InputLoader.capacity){
                if(time_now+costMatrix[cur_address][orders.get(i).customerID]<=orders.get(i).timeWindowEnd){
                    tmp_capacity+=orders.get(i).demand;
                    tmp_cost+=costMatrix[cur_address][orders.get(i).customerID];
                    tmp_served+=1;
                    time_now=max(time_now+costMatrix[cur_address][orders.get(i).customerID],orders.get(i).timeWindowStart)+orders.get(i).serviceTime;
                    cur_address=orders.get(i).customerID;
                }
            }
        }
        if(time_now+costMatrix[cur_address][0]>timeWindow[0].second){
            Detail d = new Detail();
            d.served=-1;
            return d;
        }
        else{
            tmp_cost+=costMatrix[cur_address][0];
            Detail d = new Detail();
            d.served=tmp_served;
            d.cost=tmp_cost;
            return d;
        }
    }
    public int max_served_orders;
    public int min_cost;
}
