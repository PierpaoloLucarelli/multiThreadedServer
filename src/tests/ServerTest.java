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
public class ServerTest{
    
    public static void main(String[] args){
        for(int i = 0 ; i < 4 ; i++){
            Connection c = new Connection();
            Thread t = new Thread(c);
            t.start();
        }
    }
    
}
