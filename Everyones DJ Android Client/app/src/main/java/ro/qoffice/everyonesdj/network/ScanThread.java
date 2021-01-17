package ro.qoffice.everyonesdj.network;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import ro.qoffice.everyonesdj.config.Cfg;

/**
 * Created by Alin on 22.10.2014.
 */
public class ScanThread extends Thread {
    private String ip;
    private NetworkScanner nscan;
    private Socket s;
    private boolean valid;

    public ScanThread(NetworkScanner sc,String ip) {
        nscan = sc;
        this.ip = ip;
    }

    @Override
    public void run() {
        try {
            s = new Socket(ip, Cfg.SCANPORT);
            valid = true;
        } catch (IOException e) {
            valid = false;
        } finally {
            if (s!=null) {
                try {
                    s.close();
                } catch (IOException e) {
                    Log.e("SOCKET", "ERROR");
                }
            }
        }
        nscan.finishedThread(this);
    }

    public String getIp() {
        return ip;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScanThread that = (ScanThread) o;

        if (!ip.equals(that.ip)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }
}
