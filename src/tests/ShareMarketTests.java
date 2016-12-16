/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import shares.Share;
import shares.ShareMarket;

/**
 *
 * @author plucarelli
 */
public class ShareMarketTests {
    public static void main(String[] args){
        
        // create the market
        ShareMarket m = new ShareMarket();
        Share bp = new Share(100, "BP", 1000);
        Share sky = new Share(130, "SKY", 1000);
        Share tsco = new Share(98, "TSCO", 1000);
        Share vod = new Share(100, "VOD", 1000);
        m.addShare(sky, sky.getCode());
        m.addShare(bp, bp.getCode());
        m.addShare(tsco, tsco.getCode());
        m.addShare(vod, vod.getCode());
        
        // test the buy method
        Share s = m.buyShare("BP", 900);
        System.out.println("You have bought:\n" + s);
        
        // test market toString()
        System.out.println("\nUpdated market: \n-------\n"+m);
    }
}
