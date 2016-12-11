/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextArea;

/**
 *
 * @author plucarelli
 */
public class MarketInfoLabel extends JTextArea{
    private Timer timer;
    private TimerTask timerTask;
    private String output;

    public MarketInfoLabel() {
        this.output = ShareMarket.getInstance().toString();
        this.setEditable(false);
        timer = new Timer(true);
        timerTask = new TimerTask(){
            @Override
            public void run(){
                output = ShareMarket.getInstance().toString();
                repaint();
            }
        };
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        timer.schedule(timerTask, 100, 100);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setText(output);
    }
}
