import java.util.Random;
import java.util.concurrent.Semaphore;

public class Stimulate {
    static int waiting = 0;                     //A variable to store the number of riders waiting in the boarding area
    static Semaphore mutex = new Semaphore(1);  //To protect waiting_riders
    static Semaphore bus = new Semaphore(0);    //Sgnals when the bus has arrived
    static Semaphore boarded = new Semaphore(0);//Signals that a rider has boarded
    static double mean_rider = 3000, mean_bus = 12000;           //Declaring the mean of the exponential distributions of inter-arrival times of riders and buses


    public static void main(String[] args) {
        int bus_id = 0, rider_id = 0;                           //Ids are assigned for buses and riders for the easy demonstration of the program
        long diff_bus = 0, diff_rider = 0, timeCurrent = 0, time_prev_bus = System.currentTimeMillis(), time_prev_rider = System.currentTimeMillis();
        double waitTimeForRider = 0, waitTimeForBus = 0;

        waitTimeForBus =calNextBusInterval();
        waitTimeForRider=calNextRiderInterval();
//        System.out.println("Bustime value is "+waitTimeForBus);

        while (true) {

            timeCurrent = System.currentTimeMillis();
            diff_rider = timeCurrent - time_prev_rider;                      //Calculating the time passed after the previous rider
            diff_bus = timeCurrent - time_prev_bus;                          //Calculating the time passed after the previous bus


            if (diff_rider >= waitTimeForRider) {                           //check interval for next rider
                Rider newRider = new Rider(rider_id++);
                newRider.start();
                time_prev_rider = timeCurrent;
                waitTimeForRider=calNextRiderInterval();
            }
            if (diff_bus >= waitTimeForBus) {                           //check interval for next bus
                Bus newBus = new Bus(bus_id++);
                newBus.start();
                time_prev_bus = timeCurrent;
                waitTimeForBus =calNextBusInterval();
            }
        }

    }
    //Calculating the time before the next bus arrives
    private static double calNextBusInterval(){
        double newRandom = new Random().nextDouble();
        return Math.round(Math.log10(newRandom) * -1 * mean_bus);
//        double lambda = 1 / mean_bus;
//        return Math.round(-Math.log(1 - newRandom) / lambda);
    }

    private static double calNextRiderInterval(){
        double newRandom = new Random().nextDouble();
        return Math.round(Math.log10(newRandom) * -1 * mean_rider);  //Calculating the time before the next bus arrives
//        double lambda = 1 / mean_bus;
//        return Math.round(-Math.log(1 - newRandom) / lambda);
    }
}

