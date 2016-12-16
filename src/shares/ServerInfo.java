/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
 */
package shares;

import javax.swing.JTextArea;




public class ServerInfo extends JTextArea{

    private JTextArea text; // this will contain the information about the Shares 
    
    public ServerInfo() {
        text = new JTextArea();
    }
    
    public void log(String l){ // method for logging text to the textarea
        setText(this.getText() + l + "\n");
    }
    
   
    
}
