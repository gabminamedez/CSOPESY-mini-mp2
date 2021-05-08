import java.util.*;

public class Main {
    public static int numPassengers;
    public static int capacity;
    public static int numCars;

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
}
 
class Car extends Thread {
    private int id;

    public Car(int id){
        this.id = id;
    }
 
    public void run(){
        System.out.println("Car " + id + " running.");
    }
}