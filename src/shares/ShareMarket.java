/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author plucarelli
 */
public class ShareMarket {

    private static ShareMarket instance = null;
    private static Map<String,Share> shares = new HashMap<>();

    public ShareMarket() {}
    
    public static ShareMarket getInstance(){
        if(instance==null)
            instance = new ShareMarket();
        return instance;
    }
    
    public Share buyShare(String share, int quantity){
        Share s = shares.get(share);
        if(s.numAreAvailable(quantity)){
            s.buy(quantity);
            return new Share(s.getPrice(), s.getCode(), quantity);
        } return null;
    }
    
    public void sellShare(String share, int quantity){
        shares.get(share).sell(quantity);
    }
    
    public void addShare(Share s, String code){
        shares.put(code, s);
    }

    @Override
    public String toString() {
        String output = "";
        output = shares.values().stream().map((s) -> s.toString() + "\n\n").reduce(output, String::concat);
        return output;
    }
    
    public String generateLabelText(){
        String output = "<html>";
        Iterator it = shares.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            output += pair.getValue().toString();
            output += "<br>";
            it.remove(); // avoids a ConcurrentModificationException
        }
        output += "</html>";
        return output;
    }
    
    
}
