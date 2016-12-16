/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
 */
package shares;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.SwingUtilities;
import static shares.AdminGUI.serverInfo;


public class ClientRequest implements Runnable{
    private final Socket incoming;
    private final DateFormat dateFormat;
    private final SharesMonitor monitor;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;

    public ClientRequest(Socket incoming, SharesMonitor m){
        this.incoming = incoming;
        this.monitor = m;
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    @Override
    public void run(){
        this.connectionInit();
        monitor.enterCrit();
        this.printShares(); // crticial section !
        monitor.exitCrit();
        this.printComands(); // show commands to client in telnet
        while(!done && !incoming.isClosed()){ // while socket is open and !done 
            String input="";
            try { input = in.readLine();
            } catch (IOException ex){}
            String[] comand = input.split(" "); //brake down telnet input in array of words
            if(comand.length==3 || comand[0].equals("QUIT")){
                if(comand[0].matches("BUY||SELL||QUIT")){
                    switch(comand[0]){
                        case "BUY":
                            this.buyShare(comand[1],comand[2]);
                            break;
                        case "SELL":
                            this.sellShare(comand[1], comand[2]);
                            break;
                        case "QUIT":
                            done = true;
                            break;
                    }
                } else 
                    out.println("Invalid Command");
            } else 
                    out.println("Invalid Command");
        }
        closeConnection(); // clos ethe socket
        updateGUI("Client with IP " + incoming.getRemoteSocketAddress() 
                + " disconnected at time: "+ dateFormat.format(new Date()));
    }
    
    // initialise the in and out readers
    public void connectionInit(){
        try {
            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            out = new PrintWriter(incoming.getOutputStream(),true);
            out.println("You are connected to the share price quotation server:  "  
                + "\n\rHost Name: " + incoming.getInetAddress().getHostName()
                + "\n\rIP Adress: " + incoming.getInetAddress().getHostAddress()
                + "\n\rOn port " + incoming.getLocalPort() 
            ); 
        } catch (IOException ex) {}
    }
    
    // buy a share Critical code!
    public void buyShare(String share, String numShares){
        int quantity = Integer.parseInt(numShares);
        if(quantity>0){ // check if num shares bought > 0
            monitor.enterCrit(); // ENTER CRITICAL SECTION
            ShareMarket m = ShareMarket.getInstance();
            Share boughtShare = m.buyShare(share, quantity);
            monitor.exitCrit(); // LEAVE CRITICAL SECTION
            if(boughtShare!=null){
                out.println("ORDER CONFIRMED");
                this.printComands();
                updateGUI(quantity + " " + share + " shares BOUGHT at: " 
                    + dateFormat.format(new Date()) + " from user " 
                    + incoming.getRemoteSocketAddress());
            }
            else out.println("Purchase unsucessfull");
        } else out.println("Enter a number of shares bigger than 0");
    }
    
    
    // sell a share CRITICAL CODE!
    public void sellShare(String share, String numShares){
        int quantity = Integer.parseInt(numShares);
        if(quantity>0){ //check if num sold shares > 0
            monitor.enterCrit(); // ENTER CRITICL
            ShareMarket m = ShareMarket.getInstance();
            boolean sold = m.sellShare(share, quantity);
            monitor.exitCrit(); // LEAVE CRITICAL
            if(sold){ // if sale successfull
                out.println("SOLD CORRECTLY");
                printComands();
                updateGUI(quantity + " " + share + " shares SOLD at: " 
                        + dateFormat.format(new Date()) + " from user " 
                        + incoming.getRemoteSocketAddress());
            } else out.println("Something went wrong");
        } else out.println("Enter a number of shares bigger than 0");
    }
    
    //
    public void printShares(){
        ShareMarket m = ShareMarket.getInstance();
        out.println(m);
    }
    
    public void printComands(){
        out.println("Enter a buy/sell order in format BUY/SELL stockname number"
                + ", or\n enter QUIT to quit:");
    }
    
    // close the socket
    public void closeConnection(){
        try {
            System.out.println("closed");
            incoming.close() ;
        } catch (IOException ex) {}
    } 
    
    
    //safely call an update on the GUI
    public void updateGUI(String log){
        SwingUtilities.invokeLater(() -> {
            serverInfo.log(log);
        });
    }
    
}
