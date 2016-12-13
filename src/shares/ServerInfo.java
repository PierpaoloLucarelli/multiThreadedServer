/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import javax.swing.JTextArea;



/**
 *
 * @author plucarelli
 */
public class ServerInfo extends JTextArea{

    private JTextArea text;
    
    public ServerInfo() {
        text = new JTextArea();
    }
    
    public void log(String l){
        setText(this.getText() + l + "\n");
    }
    
   
    
}
