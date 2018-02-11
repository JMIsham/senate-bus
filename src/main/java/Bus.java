import java.util.logging.Level;
import java.util.logging.Logger;

public class Bus extends Thread{
    private int riders_to_board;   //A variable to hold the number of riders available for the bus
    private int bus_id;
    private int rider_count = 0;   //A variable to hold the number of riders boarded into the bus

    Bus(int index) {
        this.bus_id = index;
    }

    /**
     *  When the bus arrived at the stop it will lock the bus stop and inform all the raiders.
     *  Bus will allow raiders to get int on by one.
     */
    public void run() {
        try {
            Stimulate.mutex.acquire();                             //bus locks the mutex
            System.out.println("Bus " + bus_id + " arrived at the bus stop !!");//The number of riders available for the bus is 50 out of all the passengers in the boarding area. If the waiting passengers is less than 50, then all can get in.

            riders_to_board = Math.min(Stimulate.waiting, 50);
            System.out.println("waiting  : " + Stimulate.waiting + " To board : "+riders_to_board);

            for (int i = 0; i < riders_to_board; i++) {     //A loop to get all the available riders  on board
                System.out.println("Bus " + bus_id + " released for "+i+"th rider");
                Stimulate.bus.release();                              //bus signals that it has arrived and can take a passenger on board
                Stimulate.boarded.acquire();                          //Allows one rider to get on board
                System.out.println("Bus " + bus_id + " acquired boarded !!!!! !!");
            }

            Stimulate.waiting = Math.max((Stimulate.waiting - 50), 0); // calculates the remaining waiting
            Stimulate.mutex.release();

        } catch (InterruptedException ex) {              //Exception if the above procedure got interupted in the middle
            Logger.getLogger(Bus.class.getName()).log(Level.SEVERE, "Bus " + bus_id + "'s thread got interrupted !!", ex);
        }

        System.out.println("Bus " + bus_id + " departed with " + riders_to_board + " riders on board!");
    }
}
