/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
 */
package shares;

import java.util.HashMap;
import java.util.Map;

public class ShareMarket {

    // Used the Singleton patternt to create a shared Object between the various Threads
    // This pattern makes sure that only one instance is created and it can be retunred by 
    // invoking the getInstance method
    private static ShareMarket instance = null;
    private static Map<String,Share> shares = new HashMap<>();

    public ShareMarket() {}
    
    public static ShareMarket getInstance(){
        if(instance==null)
            instance = new ShareMarket();
        return instance;
    }
    
    // buy a share
    public Share buyShare(String share, int quantity){
        Share s = shares.get(share); // get share from hashmap
        if(s!=null){ // if share exists
            if(s.numAreAvailable(quantity)){ // check if num of shares are abailable
                s.buy(quantity);
                return new Share(s.getPrice(), s.getCode(), quantity);
            } return null;
        } return null;
    }
    
    // sell a share
    public boolean sellShare(String share, int quantity){
        Share s = shares.get(share); // get share from hahsmap
        if(s!=null){ // if share exists
            s.sell(quantity); // sell n shares
            return true;
        } else return false;   
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
}
