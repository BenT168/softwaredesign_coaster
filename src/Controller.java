import display.NumberCanvas;

public class Controller {

    protected NumberCanvas passengers;

    //Max number of passengers
    public static int Max = 9;

    //Current number of passengers on the platform
    protected int numPassenger;

    //Boolean allowing car to leave early
    protected boolean button = false;
    private int carsize;

    public Controller(NumberCanvas nc) {
        passengers = nc;
        numPassenger = 0;
    }

    public synchronized void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) {

                // wait if platform is fully accommodated
             wait();

        }
        numPassenger++; // increment number of waiting passengers
        passengers.setValue(numPassenger); // update displayed passenger's number

            // notify others that a new waiting passenger is added
        notifyAll();

    }

    public synchronized int getPassengers(int mcar) throws InterruptedException{
        if (mcar <= 0) return 0;  // neg invalid capacity, isnt allowed to take people

        carsize = mcar;

        // button is not pressed & not enough  passenger to fill up the car
        while (numPassenger < mcar && !button) wait();
                // wait for enough passenger to fill up the car

        // button is pressed w/o waiting for enough passenger to fill up the car
        if (button && numPassenger < mcar ) {
            //Update the number of canvas as number of available passengers
            mcar = numPassenger;
        }

        numPassenger -= mcar; // update number of waiting passenger
        passengers.setValue(numPassenger); // update displayed passenger's number


        //Awaken entry lock since there is now space on the platform
        notifyAll();

        // reset the button after being called
        button = false;
        return mcar; // return the number of passenger got into the car
    }

    public synchronized void goNow() {
        if (numPassenger > 0 && numPassenger < carsize ) {
            // ensure that there is at least 1 passenger
            button = true; // signify that button is pressed
            notifyAll(); // notify other that the car can go now
        }
    }

}
