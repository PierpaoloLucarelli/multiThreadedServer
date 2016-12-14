/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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

/**
 *
 * @author plucarelli
 */
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
    private JScrollPane scroll;
    private Thread worker;
    
    public AdminGUI() {
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
        
        time = new JLabel(dateFormat.format(new Date()));
        p.add(time, BorderLayout.NORTH);
        
        JPanel areas = new JPanel(new GridLayout(2,1));
        marketInfo = new JTextArea(ShareMarket.getInstance().toString());
        JPanel infoArea = new JPanel(new BorderLayout());
        infoArea.add(new JLabel("Real time shares"), BorderLayout.NORTH);
        serverInfo = new ServerInfo();
        scroll = new JScrollPane(serverInfo);
        infoArea.add(marketInfo, BorderLayout.CENTER);
        areas.add(scroll);
        
        areas.add(infoArea);
        p.add(areas, BorderLayout.CENTER);
        
        startServer = new JButton(new AbstractAction("start"){
            @Override
            public void actionPerformed(ActionEvent e){
                server = new Server();
                serverInfo.log("Server Started at: " 
                        + dateFormat.format(new Date()));
                worker = new Thread(server);
                worker.start();
            }
        });
        
        stopServer = new JButton(new AbstractAction("stop"){
            @Override
            public void actionPerformed(ActionEvent e){
                serverInfo.log("Server Stopped at: " 
                        + dateFormat.format(new Date()));
                server.stop();
            }
        });
        
        timeTimer = new javax.swing.Timer(1000, (ActionEvent e) -> {
            time.setText(dateFormat.format(new Date()));
        });
        timeTimer.start();
        
        sharesTimer = new javax.swing.Timer(100, (ActionEvent e) -> {
            marketInfo.setText(ShareMarket.getInstance().toString());
        });
        sharesTimer.start();
        
        JPanel buttons = new JPanel(new GridLayout(2,1));
        buttons.add(startServer);
        buttons.add(stopServer);
        
        p.add(buttons, BorderLayout.EAST);
        
        pack();
        setVisible(true);
    }
    
    public static void main(String[] args){
        ShareMarket shares = ShareMarket.getInstance();
        
        Share bp = new Share(100, "BP", 1000);
        Share sky = new Share(130, "SKY", 1000);
        Share tsco = new Share(98, "TSCO", 1000);
        Share vod = new Share(100, "VOD", 1000);
        
        shares.addShare(sky, sky.getCode());
        shares.addShare(bp, bp.getCode());
        shares.addShare(tsco, tsco.getCode());
        shares.addShare(vod, vod.getCode());
        
         Runnable createAndShowGUI = () -> {
            new AdminGUI();
        };
        
        SwingUtilities.invokeLater(createAndShowGUI);
    }
    
}
