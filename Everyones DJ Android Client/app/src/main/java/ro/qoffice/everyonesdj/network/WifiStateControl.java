package ro.qoffice.everyonesdj.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.activity.StartActivity;

public class WifiStateControl extends BroadcastReceiver {
    private StartActivity sa=null;
    private Button btnScan,btnConnect;
    private NetworkManager nman;

    public WifiStateControl() {
    }

    public void setStartActivity(StartActivity sa) {
        this.sa = sa;
        btnConnect = (Button)sa.findViewById(R.id.btnConnect);
        btnScan = (Button)sa.findViewById(R.id.btnScan);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (sa != null) {
            nman = NetworkManager.getInstance(sa);
            if (nman.isWifiConnected()) {
                btnScan.setEnabled(true);
            } else {
                btnScan.setEnabled(false);
                Toast.makeText(sa, R.string.wifi_disabled,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
