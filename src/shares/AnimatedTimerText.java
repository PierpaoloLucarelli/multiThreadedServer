/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;

/**
 *
 * @author plucarelli
 */
public class AnimatedTimerText extends JLabel{
    
    private Timer timer;
    private Date date = new Date();
    private TimerTask timerTask;

    public AnimatedTimerText() {
        setPreferredSize(new Dimension(200,200));
        timer = new Timer(true);
        timerTask = new TimerTask(){
            @Override
            public void run(){
                date = new Date();
                repaint();
            }
        };
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        timer.schedule(timerTask, 1000, 1000);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        setText(dateFormat.format(date));
    }
    
    
}
