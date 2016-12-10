/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shares;

/**
 *
 * @author plucarelli
 */
public class Share {
    
    private double price;
    
    private String code;
    
    private int numAvailable;

    public Share(double price, String code, int numAvailable) {
        this.price = price;
        this.code = code;
        this.numAvailable = numAvailable;
    }
    
    public boolean numAreAvailable(int numShares){
        return numAvailable >= numShares;
    }
    
    public void buy(int numShares){
        this.numAvailable -= numShares;
    }
    
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
