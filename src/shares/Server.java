/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author plucarelli
 */
public class Server {
    public ServerSocket socket;
    SharesMonitor m = new SharesMonitor();

    public Server() {
        try {
            this.socket = new ServerSocket(8189) ;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while(true) { 
                Socket incoming;
            try {
                incoming = socket.accept();
                ClientRequest req = new ClientRequest(incoming, m);
                Thread t = new Thread(req);
                t.start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
