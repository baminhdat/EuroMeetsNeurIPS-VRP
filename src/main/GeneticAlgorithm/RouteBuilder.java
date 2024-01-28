package main.GeneticAlgorithm;

import java.util.ArrayList;

import static java.lang.Math.max;
import static main.GeneticAlgorithm.GA.orders;
import static main.InputLoader.costMatrix;
import static main.InputLoader.timeWindow;
public class RouteBuilder {
    public ArrayList<Integer> customerList;
    public RouteBuilder(){
        this.customerList = new ArrayList<>();
        this.min_cost=0;
        this.max_served_orders=0;
        this.capacity=0;
        this.head = new Delivery();
        head.order_ID=0;
        head.start_time=timeWindow[0].first;
        head.finish_time=timeWindow[0].first;
        head.delay=90000;
        this.tail = head;
    }
    public int capacity;
    public static class Delivery {
        public int order_ID;
        public int start_time;
        public int finish_time;
        public int delay;
        public Delivery prev = null;
        public Delivery next = null;
    }
    public Delivery head;
    public Delivery tail;
    public void buildRoute(){
        int cur = 0;
        int time_now=timeWindow[0].first;
        while(!customerList.isEmpty()){
            int next=-1;
            int min_time=50000;
            int min_invalidate=900;
            for(Integer i : customerList) {
                if (time_now + costMatrix[orders.get(cur).customerID][orders.get(i).customerID] <= orders.get(i).timeWindowEnd) {
                    int invalidate = 0;
                    int spec_time = max(time_now + costMatrix[orders.get(cur).customerID][orders.get(i).customerID], orders.get(i).timeWindowStart) + orders.get(i).serviceTime;
                    for (Integer j : customerList) {
                        if (spec_time + costMatrix[orders.get(i).customerID][orders.get(j).customerID] > orders.get(j).timeWindowEnd && (!j.equals(i))) {
                            invalidate += 1;
                        }
                    }
                    if (invalidate < min_invalidate) {
                        next = i;
                        min_time=spec_time;
                        min_invalidate = invalidate;
                    }
                    else if(invalidate == min_invalidate){
                        if(spec_time<min_time){
                            next=i;
                            min_time=spec_time;
                        }
                    }
                }
            }
            if(next!=-1){
            capacity+=orders.get(next).demand;
            customerList.remove(Integer.valueOf(next));
            min_cost+=costMatrix[orders.get(cur).customerID][orders.get(next).customerID];
            max_served_orders+=1;
            Delivery d = new Delivery();
            d.order_ID=next;
            d.start_time=max(time_now,orders.get(next).timeWindowStart-costMatrix[orders.get(cur).customerID][orders.get(next).customerID]);
            d.finish_time=d.start_time+costMatrix[orders.get(cur).customerID][orders.get(next).customerID]+orders.get(next).serviceTime;
            d.delay=max(0,orders.get(next).timeWindowEnd-d.finish_time+orders.get(next).serviceTime);
            tail.next=d;
            d.prev=tail;
            tail=d;
            time_now = d.finish_time;
        //    time_now = max(time_now+costMatrix[orders.get(cur).customerID][orders.get(next).customerID],orders.get(next).timeWindowStart)+orders.get(next).serviceTime;
            cur=next;
            }
            else break;
        }
        Delivery return_to_epoch = new Delivery();
        time_now+=costMatrix[orders.get(cur).customerID][0];
        return_to_epoch.order_ID=0;
        return_to_epoch.start_time=time_now;
        return_to_epoch.finish_time=time_now;
        return_to_epoch.delay=timeWindow[0].second-time_now;
        return_to_epoch.prev=tail;
        tail.next=return_to_epoch;
        tail=return_to_epoch;
        Delivery run = tail;
        int temp = run.delay;
        while(run.prev!=null){
            if(run.prev.delay>temp){
                run.prev.delay=temp;
            }
            else temp=run.prev.delay;
            run=run.prev;
        }
        min_cost+=costMatrix[cur][0];
    }
    public int max_served_orders;
    public int min_cost;
}
