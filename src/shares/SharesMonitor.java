/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

/**
 *
 * @author plucarelli
 */
public class SharesMonitor {
    
    private boolean theCritIsBusy;

    public SharesMonitor() {
        this.theCritIsBusy = false;
    }
    
    public synchronized void enterCrit(){
        while(theCritIsBusy){
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        theCritIsBusy = true;
    }
    
    public synchronized void exitCrit(){
        theCritIsBusy = false;
        notify();
    }
    
}
