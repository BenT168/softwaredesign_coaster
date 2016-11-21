public class PlatformAccess {

    // Flag determining whether there is a car waiting on the platform
    protected boolean platformOccupied = false;

    public void arrive() throws InterruptedException {
        if (platformOccupied) wait(); // wait if there's a car on platform
        platformOccupied = true; // signify that there is a car on platform
        notifyAll(); // notify other cars that a car had arrived
    }

    public synchronized void depart() {
        if (platformOccupied) { // cannot depart unless there's a car on platform
            platformOccupied = false; // car had left and platform is accessible
            notifyAll(); // notify other cars that a car had departed
        }
    }

}
