/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
 */
package shares;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public final class AdminGUI extends JFrame{
    
    private JPanel p;
    private JLabel time;
    private Timer timeTimer;
    private Timer sharesTimer;
    private JTextArea marketInfo;
    private JButton startServer;
    private JButton stopServer;
    private Server server;
    public static ServerInfo serverInfo;
    private final DateFormat dateFormat;
    private final SharesMonitor m;
    private JScrollPane scroll;
    
    public AdminGUI() { // initialize the shared mutex monitor
        this.m = new SharesMonitor();
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        setTitle("Stock Makret Server Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        createView();
    }
    
    public void createView(){
        p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(800,800));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(p);
        
        // time label to be uipdated every second
        time = new JLabel(dateFormat.format(new Date()));
        p.add(time, BorderLayout.NORTH);
        
        JPanel areas = new JPanel(new GridLayout(2,1));
        
        // text area for information of the company shares
        marketInfo = new JTextArea(ShareMarket.getInstance().toString());
        marketInfo.setEditable(false);
        JPanel infoArea = new JPanel(new BorderLayout());
        infoArea.add(new JLabel("Real time shares"), BorderLayout.NORTH);
        // text area for logs of the client actions
        serverInfo = new ServerInfo();
        serverInfo.setEditable(false);
        scroll = new JScrollPane(serverInfo); // make scrollable
        infoArea.add(marketInfo, BorderLayout.CENTER);
        areas.add(scroll);
        
        areas.add(infoArea);
        p.add(areas, BorderLayout.CENTER);
        
        // start server button
        startServer = new JButton(new AbstractAction("start"){
            @Override
            public void actionPerformed(ActionEvent e){
                server = new Server(m); // create worker thread
                server.execute(); // run worker
                serverInfo.log("Server Started at: " 
                        + dateFormat.format(new Date()));
            }
        });
        
        stopServer = new JButton(new AbstractAction("stop"){
            @Override
            public void actionPerformed(ActionEvent e){
                serverInfo.log("Server Stopped at: " 
                        + dateFormat.format(new Date()));
                try {
                    server.getSocket().close(); // this trigger an execption in Server which causes the Thread to shutdown
                } catch (IOException ex) {}
            }
        });
        
        // update the time label every second
        timeTimer = new javax.swing.Timer(1000, (ActionEvent e) -> {
            time.setText(dateFormat.format(new Date()));
        });
        timeTimer.start(); // start the timer
        
        // update the shares info every 100ms 
        sharesTimer = new javax.swing.Timer(100, (ActionEvent e) -> {
//            m.enterCrit();
            marketInfo.setText(ShareMarket.getInstance().toString());
//            m.exitCrit();
        });
        sharesTimer.start(); // start the timer
        
        JPanel buttons = new JPanel(new GridLayout(2,1));
        buttons.add(startServer);
        buttons.add(stopServer);
        
        p.add(buttons, BorderLayout.EAST);
        
        pack();
        setVisible(true);
    }
    
    public static void main(String[] args){
        ShareMarket shares = ShareMarket.getInstance();
        // create inital shares
        Share bp = new Share(100, "BP", 1000);
        Share sky = new Share(130, "SKY", 1000);
        Share tsco = new Share(98, "TSCO", 1000);
        Share vod = new Share(100, "VOD", 1000);
        
        // add shares to the share Obj
        shares.addShare(sky, sky.getCode());
        shares.addShare(bp, bp.getCode());
        shares.addShare(tsco, tsco.getCode());
        shares.addShare(vod, vod.getCode());
        
        Runnable createAndShowGUI = () -> {
            new AdminGUI();
        };
        
        // safely invoke the GUI on the EDT
        SwingUtilities.invokeLater(createAndShowGUI);
    }
    
}
