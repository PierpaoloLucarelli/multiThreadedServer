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
public class RunApplication {
    public static void main(String[] args){
        
        ShareMarket shares = ShareMarket.getInstance();
        
        Share bp = new Share(100, "BP", 1000);
        Share sky = new Share(130, "SKY", 1000);
        Share tsco = new Share(98, "TSCO", 1000);
        Share vod = new Share(100, "VOD", 1000);
        
        shares.addShare(sky, sky.getCode());
        shares.addShare(bp, bp.getCode());
        shares.addShare(tsco, tsco.getCode());
        shares.addShare(vod, vod.getCode());
        
        Server s = new Server();
        s.run();
    }
}
