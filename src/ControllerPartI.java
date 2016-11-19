

/**
 * Complete the implementation of this class in line with the FSP model
 */

import display.NumberCanvas;

public class ControllerPartI {

    public static int Max = 9;
    protected NumberCanvas passengers;
    protected int numPassenger = 0;

    public ControllerPartI(NumberCanvas nc) {
        passengers = nc;
    }

    public void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) wait(); // wait if platform is fully accommodated
        numPassenger++; // increment number of waiting passengers
        passengers.setValue(numPassenger); // update displayed passenger's number
        notifyAll(); // notify others that a new waiting passenger is added
    }

    public int getPassengers(int mcar) throws InterruptedException{
        if (mcar < 0) return 0; // neg invalid capacity, isnt allowed to take passenger
        while (numPassenger < mcar) wait(); // wait for enough passenger to fill up car
        numPassenger -= mcar; // update number of waiting passenger
        passengers.setValue(numPassenger); // update displayed passenger's number
        notifyAll(); // notify other that passengers had went on board
        return mcar; // return the number of passenger got into the car
    }

}

