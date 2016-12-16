/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author plucarelli
 */
public class ServerTest{
    
    static DataOutputStream out;
//    static Socket socket;
    static BufferedReader in;
    
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
        // tried to create a telnet connection thought the Java socket class
        // I managed to get a connection but I didnt manage to send a comand to the Server
        Connection c = new Connection();
        Thread t = new Thread(c);
        t.start();
//        Socket socket = new Socket(InetAddress.getByName(null), 8189);
//        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//        out.writeBytes("BUY BP 500");
//        out.flush();
    }
    
    public static void runComand(String cmd){
        while(true){
            try {
                out.writeBytes(cmd);
                break;
            } catch (IOException ex) {
                Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            boolean done = true;
            while(done ){
                System.out.println(in.readLine());
            }
        } catch (IOException ex) {
        }
    }
    
}
