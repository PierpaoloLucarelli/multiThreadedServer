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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import static shares.AdminGUI.serverInfo;


/**
 *
 * @author plucarelli
 */
public class Server extends SwingWorker{
    private List<Socket> children = new ArrayList();
    private ServerSocket socket;
    private Socket incoming;
    private DateFormat dateFormat;
    private final int PORTNUMBER = 8189;
    private SharesMonitor m;
    private volatile boolean running;
    private ThreadPoolExecutor pool;
    private final int NTHREADS = 10;
    private static final int QSIZE = 10; 

    public Server(SharesMonitor m) {
        this.m = m;
         pool = new ThreadPoolExecutor(
              NTHREADS,                 // core pool size 
              NTHREADS,                 // maximum pool size     
              50000L,                   // time to keep idle threads alive
              TimeUnit.MILLISECONDS,    // unit for time
              new LinkedBlockingQueue<>(QSIZE)  // queue used to hold tasks
        );
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); 
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        running = true;
        try {
            this.socket = new ServerSocket(PORTNUMBER) ;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop(){
        try {
            pool.shutdownNow();
            this.socket.close();
            children.stream().forEach((t) -> {
                try {
                    t.close();
                } catch (IOException ex) {}
            });
        } catch (IOException ex) {
            System.out.println("Error stopping server");
        }
    }

    public ServerSocket getSocket() {
        return socket;
    }
    
    public void updateGUI(String log){
        SwingUtilities.invokeLater(() -> {
            serverInfo.log(log);
        });
    }

    @Override
    protected Object doInBackground(){
         while(running) {
            try {
                incoming = socket.accept();
                ClientRequest req = new ClientRequest(incoming, m);
                Thread t = new Thread(req);
                pool.submit(t);
                children.add(incoming);
                updateGUI("Connected client to port: " +
                        PORTNUMBER + " at: " + dateFormat.format(new Date()) + "with IP of" +
                        incoming.getRemoteSocketAddress());
            } catch (IOException ex) {
                running = false;
            }
        }
        stop();
        return null;
    }
}
