/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
 */
package tests;

// this class will ensure that the GUI, Server and CLient Threads will have mutual access
// over the Shared data ShareMarket
public class SharesMonitor {
    
    int count = 0;
    
    // flag to check if some Thread is in the critical section
    private volatile boolean theCritIsBusy;

    public SharesMonitor() {
        this.theCritIsBusy = false;
    }
    
    // enter and wait if crit is busy
    public synchronized void enterCrit(){
        while(theCritIsBusy){
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        theCritIsBusy = true; // enter the monitor and signal that its busy
        System.out.println("E");
        count++;
    }
    
    public synchronized void exitCrit(){
        theCritIsBusy = false; // leave the monitor and signal not busy
        notify(); // notify other threads
        System.out.println("L");
        count--;
    }
    
}
