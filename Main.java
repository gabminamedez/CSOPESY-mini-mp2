import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static int numPassengers;
    public static int capacity;
    public static int numCars;

    public static int boarders = 0;
    public static int unboarders = 0;

    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore mutex2 = new Semaphore(1);
    public static Semaphore boardQueue = new Semaphore(0);
    public static Semaphore unboardQueue = new Semaphore(0);
    public static Semaphore allAboard = new Semaphore(0);
    public static Semaphore allAshore = new Semaphore(0);

    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter number of passenger processes: ");
        numPassengers = reader.nextInt();
        System.out.print("Enter capacity of each car: ");
        capacity = reader.nextInt();
        System.out.print("Enter number of cars: ");
        numCars = reader.nextInt();
        reader.close();

        for(int i = 0; i < numCars; i++){
            new Car(i).start();
        }
    }
}

class Passenger extends Thread {
    public void run(){
        
    }

    public void board(){
        boarders += 1;
        if(boarders == capacity){
            allAboard.signal();
            boarders = 0;
        }
    }

    public void unboard(){
        unboarders += 1;
        if(unboarders == capacity){
            allAshore.signal();
            unboarders = 0;
        }
    }
}
 
class Car extends Thread {
    public int id;

    Car(int id){
        this.id = id;
    }
 
    public void run(){
        System.out.println("Car " + id + " running.");
    }

    public void load(){

    }

    public void unload(){
        
    }
}