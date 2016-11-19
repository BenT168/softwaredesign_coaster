

/**
 * Complete the implementation of this class in line with the FSP model
 */

import display.NumberCanvas;

public class Controller {

  public static int Max = 9;
  protected NumberCanvas passengers;
  protected int numPassenger = 0;

  public Controller(NumberCanvas nc) {
    passengers = nc;
  }

  public void newPassenger() throws InterruptedException {
      while (numPassenger >= Max) wait(); // wait if the platform is fully accommodated
      numPassenger++; // increment the number of waiting passengers
      passengers.setValue(numPassenger); // update the displayed passenger's number
      notifyAll(); // notify others that a new waiting passenger is added
  }

  public int getPassengers(int mcar) throws InterruptedException{
      if (mcar < 0) throw new InterruptedException("ERROR: " +
              "the given car capacity is negative.");
      while (numPassenger < mcar) wait(); // wait for enough passenger to depart
      numPassenger -= mcar; // update the number of waiting passenger
      passengers.setValue(numPassenger); // update the displayed passenger's number
      notifyAll(); // notify other that passengers had went on board
      return mcar; // return the number of passenger got into the car
  }

  public synchronized void goNow() {
    // complete implementation for part II
  }

}
