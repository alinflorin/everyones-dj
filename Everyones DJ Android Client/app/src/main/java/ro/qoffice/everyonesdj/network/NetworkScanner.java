package ro.qoffice.everyonesdj.network;
import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import ro.qoffice.everyonesdj.activity.ScanActivity;

public class NetworkScanner extends Thread {
    private Context ctx;
    private ScanActivity sa;
    private NetworkManager nman;
    private List<ScanThread> currentThreads;
    private static boolean scanning = false;
    public static boolean stop=false;

    public NetworkScanner(ScanActivity sa) {
        this.sa = sa;
        ctx = sa.getApplicationContext();
        nman = NetworkManager.getInstance(ctx);
        currentThreads = Collections.synchronizedList(new ArrayList<ScanThread>());
    }

    public void run() {
        String myip = nman.getIP();
        String zone = nman.getIPZone();
        int myend = nman.getIPEnding();
        for (int i=1;i<=254;i++) {
            if (stop) {
                break;
            }
            if (scanning) {
                i--;
                if (currentThreads.size() == 0) {
                    scanning = false;
                }
            } else {
                if (myend == i) {
                    continue;
                }
                if (currentThreads.size() < 20) {
                    String scanip = zone + i;
                    currentThreads.add(new ScanThread(this,scanip));
                } else {
                    scanning = true;
                    startScanning();
                }
            }
        }
        if (currentThreads.size() > 0 && !stop) {
            synchronized (currentThreads) {
                Iterator i = currentThreads.iterator();
                while (i.hasNext()) {
                    if (stop) break;
                    ((ScanThread) i.next()).start();
                }
            }
        }
        sa.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sa.hideLoading();
            }
        });
        stop = false;
    }

    private void startScanning() {
        synchronized (currentThreads) {
            if (!stop) {
                Iterator i = currentThreads.iterator();
                while (i.hasNext()) {
                    if (stop) break;
                    ((ScanThread) i.next()).start();

                }
            }
        }
    }

    public void finishedThread(final ScanThread t) {
        if (t.isValid()) {
            sa.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sa.addToList(t.getIp());

                }
            });

        }
        currentThreads.remove(t);
    }
}
