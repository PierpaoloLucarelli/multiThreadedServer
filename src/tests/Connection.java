/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author plucarelli
 */
public class Connection implements Runnable{
    
    private Socket socket;
    private DataOutputStream out;

    public Connection() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 8189);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {}
    }

    @Override
    public void run() {
        try {
            out.writeBytes("SELL BP 500");
            System.out.println("sending");
        } catch (IOException ex) {}
    }
    
}
