/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;
/**
 *
 * @author plucarelli
 */
public class Task implements Runnable{
    SharesMonitor m;
    public Task(SharesMonitor m) {
        this.m = m;
    }
    

    @Override
    public void run() {
        for(int j = 0 ; j < 100 ; j++){
            m.enterCrit();
            for(int i = 0 ; i < 15 ; i++){
                
            }
            m.exitCrit();
        }
    }
    
}
