/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import static shares.AdminGUI.serverInfo;


/**
 *
 * @author plucarelli
 */
public class Server implements Runnable{
    private List<Thread> children = new ArrayList();
    private ServerSocket socket;
    private Socket incoming;
    private DateFormat dateFormat;
    private final int PORTNUMBER = 8189;
    SharesMonitor m = new SharesMonitor();
    private volatile boolean running;

    public Server() {
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        running = true;
        try {
            this.socket = new ServerSocket(PORTNUMBER) ;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        while(running) {
            try {
                incoming = socket.accept();
                ClientRequest req = new ClientRequest(incoming, m);
                Thread t = new Thread(req);
                children.add(t);
                t.start();
                updateGUI("Connected client to port: " +
                        PORTNUMBER + " at: " + dateFormat.format(new Date()) + "with IP of" +
                        incoming.getRemoteSocketAddress());
            } catch (IOException ex) {
                System.out.println("Server has been interrupted");
            }
        }
    }
    
    public void stop(){
        try {
            running = false;
            this.socket.close();
            children.stream().forEach((t) -> {
                t.interrupt();
            });
        } catch (IOException ex) {
            System.out.println("Error stopping server");
        }
    }
    
    public void updateGUI(String log){
        SwingUtilities.invokeLater(() -> {
            serverInfo.log(log);
        });
    }
}
