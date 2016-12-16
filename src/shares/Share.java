/*
 * Pierpaolo Lucarelli 1400571
 * CM3033 Coursework 2016/2017
 * MultiThreaded Java server and Admin GUI
 */
package shares;

public class Share {
    
    private double price;
    
    private String code;
    
    private int numAvailable;

    public Share(double price, String code, int numAvailable) {
        this.price = price;
        this.code = code;
        this.numAvailable = numAvailable;
    }
    
    // check if the number of required shares is available
    public boolean numAreAvailable(int numShares){
        return numAvailable >= numShares;
    }
    // buy a share
    public void buy(int numShares){
        this.numAvailable -= numShares;
    }
    
    // sell share
    public void sell(int numShares){
        this.numAvailable+=numShares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    @Override
    public String toString() {
        String output = "";
        output += "Share code: " + this.code;
        output += "\nShares Available: " + this.numAvailable;
        output += "\nShare prices: " + this.price;
        return output;
    }
    
   
    
    
}
