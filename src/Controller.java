import display.NumberCanvas;

public class Controller {

    protected NumberCanvas passengers;

    //Max number of passengers
    public static int Max = 9;

    //Current number of passengers on the platform
    protected int numPassenger;

    //Boolean allowing car to leave early
    protected boolean button = false;

    //Lock object
    private final Object lock = new Object();

    public Controller(NumberCanvas nc) {
        passengers = nc;
        numPassenger = 0;
    }

    public void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) {
            synchronized (lock){
                // wait if platform is fully accommodated
                lock.wait();
            }
        }
        numPassenger++; // increment number of waiting passengers
        passengers.setValue(numPassenger); // update displayed passenger's number
        synchronized (lock){
            // notify others that a new waiting passenger is added
            lock.notifyAll();
        }

    }

    public int getPassengers(int mcar) throws InterruptedException{
        if (mcar < 0) return 0;  // neg invalid capacity, isnt allowed to take people

        // button is not pressed & the waiting passenger is less than the car capacity
        while (numPassenger < mcar && !button) {
            synchronized (lock){
                // wait for enough passenger to fill up the car
                lock.wait();
            }
        }

        // button is pressed w/o waiting for enough passenger to fill up the car
        if (button && numPassenger < mcar) {
            mcar = numPassenger; // car capacity is restricted to numPassenger
        }

        numPassenger -= mcar; // update number of waiting passenger
        button = false; // release the button after being called
        passengers.setValue(numPassenger); // update displayed passenger's number

        synchronized (lock){
            lock.notifyAll(); // notify other that passengers had went on board
        }

        return mcar; // return the number of passenger got into the car
    }

    public synchronized void goNow() {
        if (numPassenger > 0) { // ensure that there is at least 1 passenger
            button = true; // signify that button is pressed
            synchronized (lock){
                lock.notifyAll(); // notify other that the car can go now
            }
        }
    }

}
