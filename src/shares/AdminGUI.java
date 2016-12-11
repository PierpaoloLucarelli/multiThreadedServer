/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author plucarelli
 */
public class AdminGUI extends JFrame{
    
    private JPanel p;
    private JLabel time;
    private MarketInfoLabel marketInfo;
    private JButton startServer;
    private Server server;
    
    
    public AdminGUI() {
    
        setTitle("Stock Makret Server Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        createView();
    }
    
    public void createView(){
        p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(500,500));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(p);
        
        time = new AnimatedTimerText();
        p.add(time, BorderLayout.PAGE_START);
        
        p.add(new JLabel("Real time shares"), BorderLayout.CENTER);
        marketInfo = new MarketInfoLabel();
        p.add(marketInfo, BorderLayout.CENTER);
        
        startServer = new JButton(new AbstractAction("start"){
            @Override
            public void actionPerformed(ActionEvent e){
                Thread worker = new Thread(new Server());
                worker.start();
            }
        });
        
        p.add(startServer, BorderLayout.SOUTH);
        
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
