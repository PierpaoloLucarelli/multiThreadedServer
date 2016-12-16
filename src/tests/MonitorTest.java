/*
* This class proves that the monitor used correctly performs mutal exlusion
* By adding one to count when monitor enters crit and -1 when it leaves
* At the end of the tasks we can see that The total value is 0 as expecetd
* This is not enought to prove mutex but we can also see in the logs that Enter and Leave 
* Are called alternatively and never consecutively.
*/
package tests;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorTest {
    public static void main(String[] args){
        SharesMonitor m = new SharesMonitor();
        for(int i = 0 ; i < 4 ; i++){
            Task t = new Task(m);
            Thread th = new Thread(t);
            th.start();
            try {
                th.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("Summ of all operations = "+ m.count);
        
    }
}
