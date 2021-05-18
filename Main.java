import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static int numPassengers;
    public static int capacity;
    public static int numCars;

    public static int boarders = 0;
    public static int unboarders = 0;
    public static int unloadeds = 0;

    public static Semaphore mutex1 = new Semaphore(1);
    public static Semaphore mutex2 = new Semaphore(1);
    public static Semaphore boardQueue = new Semaphore(0);
    public static Semaphore unboardQueue = new Semaphore(0);
    public static Semaphore allAboard = new Semaphore(0);
    public static Semaphore allAshore = new Semaphore(0);
    public static Semaphore[] loadingArea;
    public static Semaphore[] unloadingArea;

    public static Passenger passengers[];
    public static Car cars[];

    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter number of passenger processes: ");
        numPassengers = reader.nextInt();
        passengers = new Passenger[numPassengers];
        System.out.print("Enter capacity of each car: ");
        capacity = reader.nextInt();
        if(numPassengers <= capacity){
            System.out.println("Number of passengers must be greater than car capacity!");
            System.exit(0);
        }
        System.out.print("Enter number of cars: ");
        numCars = reader.nextInt();
        cars = new Car[numCars];
        loadingArea = new Semaphore[numCars];
        unloadingArea  = new Semaphore[numCars];
        reader.close();

        for(int i = 0; i < numCars; i++){
            loadingArea[i] = new Semaphore(0);
            unloadingArea[i] = new Semaphore(0);
        }
        loadingArea[0].release(); 
        unloadingArea[0].release();

        for(int i = 0; i < numPassengers; i++){
            passengers[i] = new Passenger(i);
        }

        for(int i = 0; i < numCars; i++){
            cars[i] = new Car(i);
        }

        for(int i = 0; i < numPassengers; i++){
            passengers[i].start();
        }

        for(int i = 0; i < numCars; i++){
            cars[i].start();
        }
    }
}

class Passenger extends Thread {
    public int id;

    Passenger(int id){
        this.id = id;
    }

    public void run(){
        while(true){
            try{
                wander();
    
                Main.boardQueue.acquire(); 
                board();
        
                Main.unboardQueue.acquire();
                unboard();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void wander(){
        try{
            System.out.println("Passenger [" + id + "] is wandering.");
            sleep(Math.round(Math.random() * 1000)); 
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void board(){
        try{
            Main.mutex1.acquire();
            System.out.println("Passenger ["+ id +"] is now boarding.");
            Main.boarders += 1;
            if(Main.boarders == Main.capacity){
                Main.allAboard.release();
                Main.boarders = 0;
            }
            Main.mutex1.release();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void unboard(){
        try{
            Main.mutex2.acquire();
            System.out.println("Passenger ["+ id +"] is now unboarding.");
            Main.unboarders += 1;
            if(Main.unboarders == Main.capacity){
                Main.allAshore.release();
                Main.unboarders = 0;
            }
            Main.mutex2.release();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
 
class Car extends Thread {
    public int id;

    Car(int id){
        this.id = id;
    }
 
    public void run(){
        while(true){
            try{
                Main.loadingArea[id].acquire();
                load();
                Main.boardQueue.release(Main.capacity);
                Main.allAboard.acquire();
                System.out.println("All aboard car ["+ id +"]!");
                Main.loadingArea[next(id)].release();

                drive();

                Main.unloadingArea[id].acquire();
                unload();
                Main.unboardQueue.release(Main.capacity);
                Main.allAshore.acquire();
                System.out.println("All ashore from car ["+ id +"]!");
                Main.unloadeds += 1;
                if(Main.unloadeds == Main.numCars){
                    System.out.println("All rides completed!");
                    Main.unloadeds = 0;
                }
                Main.unloadingArea[next(id)].release();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void load(){
        System.out.println("Car [" + id + "] is now ready for boarding.");
    }

    public void drive(){
        try{
            System.out.println("Car [" + id + "] is running.");
            sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void unload(){
        System.out.println("Car [" + id + "] is now ready for unboarding.");
    }

    public int next(int num){
        return (num + 1) % (Main.capacity);
    }
}