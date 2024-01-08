package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputLoader {
    //This class extracts and retrieves information from the problem's input file
    //Naively done by exploiting the file's VRPLib format
    //dim is total number of customers + 1 (Depot)
    //distance matrix shows travelling cost between 2 addresses
    //coor shows coordinates of addresses
    //timeWindow shows customer's timeWindow, the depot's timeWindow represent working time
    //coor and timeWindow use a pair of integer as data type
    //For coor, the 1st int is x-coordinate and the 2nd is y-coordinate
    //For timeWindow, the 1st int is the start of timeWindow and the 2nd is the end of timeWindow
    public static String problemName;
    public static int dim;
    public static int maxVehicles;
    public static int capacity;
    public static int[][] costMatrix;
    public static class Pair{
        public int first;
        public int second;
    }
    public static Pair[] coor;
    public static int[] demand;
    public static int[] serviceTime;
    public static Pair[] timeWindow;
    //Load the problem to start solving
    public static String fileName;
    public static void input_file_name () {
        Scanner input = new Scanner(System.in);
        fileName = input.nextLine();
        OrderGenerator.chooseMode();
        input.close();
    }
    public static void load () throws FileNotFoundException{
        String filePath = "D:\\IdeaProjects\\EuroMeetsNeurIPS-VRP\\src\\Problems\\"+fileName+".txt";
        Scanner scanner= new Scanner(new File(filePath));
        problemName=scanner.nextLine().substring(7);
    //    System.out.println(problemName);
        scanner.skip("[^0-9]*");
        dim=scanner.nextInt();
    //    System.out.println(dim);
        scanner.skip("[^0-9]*");
        maxVehicles=scanner.nextInt();
    //    System.out.println(maxVehicles);
        scanner.skip("[^0-9]*");
        capacity=scanner.nextInt();
    //    System.out.println(capacity);
        scanner.skip("[^0-9]*");
        costMatrix = new int [dim][dim];
        for(int i=0;i<dim;i++){
            for(int j=0;j<dim;j++){
                costMatrix[i][j]= scanner.nextInt();
    //            System.out.print(costMatrix[i][j]+" ");
            }
    //        System.out.println();
        }
        scanner.skip("[^0-9]*");
        coor = new Pair[dim];
        for(int i=0;i<dim;i++){
            scanner.nextInt();
            coor[i] = new Pair();
            coor[i].first = scanner.nextInt();
            coor[i].second = scanner.nextInt();
    //        System.out.println(coor[i].first+" "+coor[i].second);
        }
        demand = new int[dim];
        serviceTime = new int[dim];
        timeWindow = new Pair[dim];
        scanner.skip("[^0-9]*");
        for(int i=0;i<dim;i++){
            scanner.nextInt();
            demand[i]=scanner.nextInt();
    //        System.out.println(demand[i]);
        }
        scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        scanner.skip("[^0-9]*");
            for(int i=0;i<dim;i++){
                scanner.nextInt();
                serviceTime[i]=scanner.nextInt();
    //            System.out.println(serviceTime[i]);
            }
            scanner.skip("[^0-9]*");
            for(int i=0;i<dim;i++){
                scanner.nextInt();
                timeWindow[i] = new Pair();
                timeWindow[i].first=scanner.nextInt();
                timeWindow[i].second=scanner.nextInt();
    //            System.out.println(timeWindow[i].first+" "+timeWindow[i].second);
            }
        scanner.close();
    }
}
