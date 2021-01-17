package ro.qoffice.everyonesdj.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.network.WifiStateControl;

public class StartActivity extends Activity {
    private Button btnScan, btnConnect;
    private IntentFilter ifilter;
    private WifiStateControl wsc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        wsc = new WifiStateControl();
        wsc.setStartActivity(this);
        ifilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(wsc, ifilter);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, ConnectActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wsc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wsc, ifilter);
    }
}
