package ro.qoffice.everyonesdj.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.network.NetworkClient;
import ro.qoffice.everyonesdj.network.NetworkScanner;

public class ScanActivity extends NetworkActivity {
    private ArrayList<String> listItems=new ArrayList<String>();
    private ListAdapter la;
    private NetworkScanner nscan;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        list = (ListView)findViewById(R.id.hostList);
        la = new ArrayAdapter<String>(this,R.layout.list_item,listItems);
        list.setAdapter(la);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetworkScanner.stop = true;
                TextView host = (TextView)view;
                String ip = host.getText().toString();
                NetworkClient ncl = NetworkClient.getInstance(ip,ScanActivity.this);
                ncl.start();
            }
        });
        if (nscan != null) {
            NetworkScanner.stop = true;
            try {
                nscan.join();
            } catch (InterruptedException e) {
            }
            nscan = null;
            NetworkScanner.stop = false;
        }
        nscan = new NetworkScanner(this);
        nscan.start();
    }

    public void addToList(String ip) {
        ArrayAdapter<String> adp = (ArrayAdapter<String>) list.getAdapter();
        adp.add(ip);
    }

    public void hideLoading() {
        LinearLayout ll = (LinearLayout)findViewById(R.id.scanWrapper);
        ll.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (nscan != null) {
            NetworkScanner.stop = true;
            try {
                nscan.join();
            } catch (InterruptedException e) {

            }
        }
        NetworkScanner.stop = false;
    }
}
