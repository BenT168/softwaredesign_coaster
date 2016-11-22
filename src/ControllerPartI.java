import display.NumberCanvas;

public class ControllerPartI {

    protected NumberCanvas passengers;

    //Max number of passengers
    public static int Max = 9;

    //Current number of passengers on the platform
    protected int numPassenger;

    //Lock object
    private final Object controllerLock = new Object();

    public ControllerPartI(NumberCanvas nc) {
        passengers = nc;
        numPassenger = 0;
    }

    public void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) {
            synchronized (controllerLock){
                // wait if platform is fully accommodated
                controllerLock.wait();
            }
        }
        numPassenger++; // increment number of waiting passengers
        passengers.setValue(numPassenger); // update displayed passenger's number
        synchronized (controllerLock){
            // notify others that a new waiting passenger is added
            controllerLock.notifyAll();
        }
    }

    public int getPassengers(int mcar) throws InterruptedException{
        if (mcar < 0) return 0; // neg invalid capacity, isnt allowed to take people
        while (numPassenger < mcar) {
            synchronized (controllerLock){
                // wait for enough passenger to fill up the car
                controllerLock.wait();
            }
        }
        numPassenger -= mcar; // update number of waiting passenger
        passengers.setValue(numPassenger); // update displayed passenger's number
        synchronized (controllerLock){
            controllerLock.notifyAll(); // notify other passengers had went on board
        }
        return mcar; // return the number of passenger got into the car
    }
}

