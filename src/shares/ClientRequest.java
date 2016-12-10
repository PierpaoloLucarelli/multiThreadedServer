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

/**
 *
 * @author plucarelli
 */
public class ClientRequest implements Runnable{
    private Socket incoming;
    private SharesMonitor monitor;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;

    public ClientRequest(Socket incoming, SharesMonitor m){
        this.incoming = incoming;
        this.monitor = m;
    }

    @Override
    public void run() {
        System.out.println("I am running");
        this.connectionInit();
        this.printShares();
        while(!done){
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
                }
            }
        }
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
        if(boughtShare!=null)
            out.println("You have sucessfully bought the share: " + boughtShare.getCode());
        else out.println("Purchase unsucessfull not enough shares available");
        printShares();
    }
    
    public void sellShare(String share, String numShares){
        int quantity = Integer.parseInt(numShares);
        monitor.enterCrit();
        ShareMarket m = ShareMarket.getInstance();
        m.sellShare(share, quantity);
        monitor.exitCrit();
        out.println("Sold " + quantity + " " + share + " shares");
        printShares();
    }
    
    public void printShares(){
        ShareMarket m = ShareMarket.getInstance();
        out.println(m);
    }
    
    
    
    
}
