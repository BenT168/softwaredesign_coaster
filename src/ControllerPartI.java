/**
 * Complete the implementation of this class in line with the FSP model
 */

import display.NumberCanvas;

public class ControllerPartI {

    protected NumberCanvas passengers;

    //Max number of passengers
    public static int Max = 9;

    //Current number of passengers on the platform
    protected int numPassenger;

    public ControllerPartI(NumberCanvas nc) {
        passengers = nc;
    }

    public synchronized void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) wait(); // wait if platform is fully accommodated
        numPassenger++; // increment number of waiting passengers
        passengers.setValue(numPassenger); // update displayed passenger's number
        notifyAll(); // notify others that a new waiting passenger is added
    }

    public synchronized int getPassengers(int mcar) throws InterruptedException{
        if (mcar <= 0) return 0; // invalid capacity, car isnt allowed to take people
        while (numPassenger < mcar) wait(); // wait 4 enough passenger to fill up car
        numPassenger -= mcar; // update number of waiting passenger
        passengers.setValue(numPassenger); // update displayed passenger's number
        notifyAll(); // notify other that passengers had went on board
        return mcar; // return the number of passenger got into the car
    }

}
