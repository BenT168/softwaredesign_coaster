public class PlatformAccess {

    //Lock object
    private final Object platformLock = new Object();

    // Flag determining whether there is a car waiting on the platform
    protected boolean platformOccupied = false;

    public void arrive() throws InterruptedException {
        if (platformOccupied) {
            synchronized (platformLock) {
            // wait if there's a car on platform
            platformLock.wait();
            }
        }
        platformOccupied = true; // signify that there is a car on platform
    }

    public synchronized void depart() {
        if (platformOccupied) { // cannot depart unless there's a car on platform
            platformOccupied = false; // car had left and platform is accessible
            synchronized (platformLock) {
                platformLock.notifyAll(); // notify other that a car had departed
            }
        }
    }

}
