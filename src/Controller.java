import display.NumberCanvas;

public class Controller {

    protected NumberCanvas passengers;

    //Max number of passengers
    public static int Max = 9;

    //Current number of passengers on the platform
    protected int numPassenger;

    //Size of the car
    private int carSize;

    //Boolean allowing car to leave early
    protected boolean button = false;


    public Controller(NumberCanvas nc) {
        passengers = nc;
        numPassenger = 0;
    }

    public synchronized void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) wait(); // wait if platform is fully accommodated
        numPassenger++; // increment number of waiting passengers
        passengers.setValue(numPassenger); // update displayed passenger's number
        notifyAll(); // notify others that a new waiting passenger is added
    }

    public synchronized int getPassengers(int mcar) throws InterruptedException{
        if (mcar <= 0) return 0; // invalid capacity, car isnt allowed to take people
        carSize = mcar;

        // wait for enough passenger to fill up the car
        while (numPassenger < mcar && !button) wait();

        // button is pressed w/o waiting for enough passenger to fill up the car
        if (button && numPassenger < mcar) {
            mcar = numPassenger; // car capacity is restricted to numPassenger
        }

        numPassenger -= mcar; // update number of waiting passenger
        passengers.setValue(numPassenger); // update displayed passenger's number
        notifyAll(); // notify other that passengers had went on board
        button = false; // release the button after being called
        return mcar; // return the number of passenger got into the car
    }

    public synchronized void goNow() {
        // ensure that there is at least 1 passenger but less than car size
        if (numPassenger > 0 && numPassenger < carSize) {
            button = true; // signify that button is pressed
            notifyAll(); // notify other that the car can go now
        }
    }

}