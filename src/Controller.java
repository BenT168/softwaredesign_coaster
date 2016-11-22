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
    private final Object entryLock = new Object();
    private final Object controllerLock = new Object();

    public Controller(NumberCanvas nc) {
        passengers = nc;
        numPassenger = 0;
    }

    public void newPassenger() throws InterruptedException {
        while (numPassenger >= Max) {
            synchronized (entryLock){
                // wait if platform is fully accommodated
                entryLock.wait();
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
        if (mcar < 0) return 0;  // neg invalid capacity, isnt allowed to take people

        // button is not pressed & not enough  passenger to fill up the car
        while (numPassenger < mcar && !button) {
            synchronized (controllerLock){
                // wait for enough passenger to fill up the car
                controllerLock.wait();
            }
        }

        // button is pressed w/o waiting for enough passenger to fill up the car
        if (button && numPassenger < mcar) {
            //Fetch number of available passengers
            int passCnt = numPassenger;
            //Remove passengers from platform and update number canvas
            numPassenger = numPassenger - passCnt;
            passengers.setValue(numPassenger);
            synchronized (entryLock){
                //Wake up entry lock in case it is waiting
                entryLock.notify();
            }
            //Reset go now boolean
            button = false;
            //Return the number of passengers that were put into the car
            return passCnt;
        }

        numPassenger -= mcar; // update number of waiting passenger
        button = false; // release the button after being called
        passengers.setValue(numPassenger); // update displayed passenger's number

        synchronized (entryLock){
            //Awaken entry lock since there is now space on the platform
            entryLock.notifyAll();
        }

        return mcar; // return the number of passenger got into the car
    }

    public synchronized void goNow() {
        if (numPassenger > 0) { // ensure that there is at least 1 passenger
            button = true; // signify that button is pressed
            synchronized (controllerLock){
                controllerLock.notifyAll(); // notify other that the car can go now
            }
        }
    }

}
