
package tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class Connection implements Runnable{
    
    private Socket socket;
    private DataOutputStream out;

    public Connection() {
        try {
            socket = new Socket(InetAddress.getLoopbackAddress(), 8189);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {}
    }

    @Override
    public void run() {
        try {
                out.writeBytes("BUY BP 500");
                out.flush();
        } catch (IOException ex) {}
    }
    
}
