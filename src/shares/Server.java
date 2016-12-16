/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
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


public class Server extends SwingWorker{
    private List<Socket> children = new ArrayList();
    private ServerSocket socket;
    private Socket incoming;
    private DateFormat dateFormat;
    private final int PORTNUMBER = 8189;
    private SharesMonitor m;
    private volatile boolean running;
    private ThreadPoolExecutor pool;
    private final int NTHREADS = 4; // Limit of clients connected
    private static final int QSIZE = 10; // 

    // Create a ThredPoolExecutor to run the client connections
    public Server(SharesMonitor m) {
        this.m = m;
         pool = new ThreadPoolExecutor(
              NTHREADS,
              NTHREADS,
              50000L,  
              TimeUnit.MILLISECONDS,   
              new LinkedBlockingQueue<>(QSIZE)
        );
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); 
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        running = true;
        try {
            // create serversocket on port 
            this.socket = new ServerSocket(PORTNUMBER) ;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // as this class extends from swingWorker we will implemnet the doInBackgorund method
    // this ensures that the EDT is not blocked when GUI starts Server
    @Override 
    protected Object doInBackground(){
         while(running) {
            try {
                incoming = socket.accept(); // will cause excpt when GUI closes socket
                ClientRequest req = new ClientRequest(incoming, m);
                Thread t = new Thread(req);
                pool.submit(t); // submit the client connection to the pool
                children.add(incoming);
                updateGUI("Connected client to port: " +
                        PORTNUMBER + " at: " + dateFormat.format(new Date()) + "with IP of" +
                        incoming.getRemoteSocketAddress());
            } catch (IOException ex) {
                running = false; // stop the while loop
            }
        }
        stop(); // close connections
        return null;
    }
    
    public void stop(){
        try {
            // shutdown the pool
            pool.shutdownNow();
            this.socket.close();
            // disconnect each client
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
    
    // safely update GUI 
    public void updateGUI(String log){
        SwingUtilities.invokeLater(() -> {
            serverInfo.log(log);
        });
    }

 
}
