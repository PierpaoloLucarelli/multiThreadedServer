/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author plucarelli
 */
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
        this.printShares();
        this.printComands();
        while(!done && !incoming.isClosed()){
            String input="";
            try { input = in.readLine();
            } catch (IOException ex){}
            String[] comand = input.split(" ", 3);
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
        }
        closeConnection();
        updateGUI("Client with IP " + incoming.getRemoteSocketAddress() 
                + " disconnected at time: "+ dateFormat.format(new Date()));
    }
    
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
    
    public void buyShare(String share, String numShares){
        int quantity = Integer.parseInt(numShares);
        monitor.enterCrit();
        ShareMarket m = ShareMarket.getInstance();
        Share boughtShare = m.buyShare(share, quantity);
        monitor.exitCrit();
        if(boughtShare!=null){
            out.println("ORDER CONFIRMED");
            this.printComands();
            updateGUI(quantity + " " + share + " shares BOUGHT at: " 
                + dateFormat.format(new Date()) + " from user " 
                + incoming.getRemoteSocketAddress());
        }
        else out.println("Purchase unsucessfull not enough shares available");
    }
    
    public void sellShare(String share, String numShares){
        int quantity = Integer.parseInt(numShares);
        monitor.enterCrit();
        ShareMarket m = ShareMarket.getInstance();
        m.sellShare(share, quantity);
        monitor.exitCrit();
        out.println("SOLD CORRECTLY");
        printComands();
        updateGUI(quantity + " " + share + " shares SOLD at: " 
                + dateFormat.format(new Date()) + " from user " 
                + incoming.getRemoteSocketAddress());
    }
    
    public void printShares(){
        ShareMarket m = ShareMarket.getInstance();
        out.println(m);
    }
    
    public void printComands(){
        out.println("Enter a buy/sell order in format BUY/SELL stockname number"
                + ", or\n enter QUIT to quit:");
    }
    
    public void closeConnection(){
        try {
            System.out.println("closed");
            incoming.close() ;
        } catch (IOException ex) {}
    } 
    
    public void updateGUI(String log){
        SwingUtilities.invokeLater(() -> {
            serverInfo.log(log);
        });
    }
    
}
