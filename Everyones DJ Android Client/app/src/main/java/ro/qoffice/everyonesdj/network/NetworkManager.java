package ro.qoffice.everyonesdj.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Alin on 20.10.2014.
 */
public class NetworkManager {
    private static NetworkManager instance=null;
    private Context ctx;
    private WifiManager wman;
    private ConnectivityManager cman;

    private NetworkManager(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        wman = (WifiManager)this.ctx.getSystemService(Context.WIFI_SERVICE);
        cman = (ConnectivityManager)this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetworkManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new NetworkManager(ctx);
        }
        return instance;
    }


    public boolean isWifiConnected() {
        NetworkInfo mWifi = cman.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public String getIP() {
        WifiInfo wifiInfo = wman.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return String.format("%d.%d.%d.%d", (ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
    }

    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByName(getIP());
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public String getIPZone() {
        WifiInfo wifiInfo = wman.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return String.format("%d.%d.%d.", (ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff));
    }


    public int getIPEnding() {
        WifiInfo wifiInfo = wman.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return (ip >> 16 & 0xff);
    }

}
