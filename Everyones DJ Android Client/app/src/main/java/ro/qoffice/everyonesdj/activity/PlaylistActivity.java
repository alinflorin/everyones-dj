package ro.qoffice.everyonesdj.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.config.SettingsManager;
import ro.qoffice.everyonesdj.device.DeviceInfo;
import ro.qoffice.everyonesdj.gui.GuiHelper;
import ro.qoffice.everyonesdj.network.DataSet;
import ro.qoffice.everyonesdj.network.NetworkClient;
import ro.qoffice.everyonesdj.network.NetworkListener;
import ro.qoffice.everyonesdj.player.EDJPlayer;

public class PlaylistActivity extends ActionBarActivity {

    private NetworkClient ncl;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> songs = new ArrayList<String>();
    private final EDJPlayer player = EDJPlayer.getInstance();
    private int selected = -1;
    private NetworkListener nl;
    private ListView plist;
    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }
    public ListView getListView() {
        return plist;
    }
    public ArrayList<String> getSongs() {
        return songs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ncl = NetworkClient.getInstance();
        plist = (ListView) findViewById(R.id.playlist);
        adapter = new ArrayAdapter<String>(this, R.layout.playlist_item, songs);
        plist.setAdapter(adapter);
        nl = new NetworkListener(ncl, this);
        nl.start();
        if (ncl == null) {
            GuiHelper gh = new GuiHelper(this);
            DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(PlaylistActivity.this, StartActivity.class);
                    startActivity(i);
                    finish();
                }
            };
            gh.OkDialog(getString(R.string.error), getString(R.string.network_unavail), ocl);
            return;
        }
        plist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = (int)id;
                player.stop();

            }
        });
        plist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selected = (int)id;
                player.stop();
                ncl.send(new DataSet(DeviceInfo.getDeviceId(PlaylistActivity.this), "PREVIEW", position));
                return true;
            }
        });
        ((Button)findViewById(R.id.btnVote)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == -1) {
                    Toast.makeText(PlaylistActivity.this,getString(R.string.select_a_song_first),Toast.LENGTH_SHORT).show();
                } else {
                    long ts = System.currentTimeMillis();
                    SettingsManager sm = new SettingsManager(PlaylistActivity.this);
                    long saved_ts = sm.getLastVoteTS();
                    ncl.send(new DataSet(DeviceInfo.getDeviceId(PlaylistActivity.this),"VOTE",selected));
                }
            }
        });
        ncl.send(new DataSet(DeviceInfo.getDeviceId(this), "GETPLAYLIST", null));
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ncl.stopCl();
        NetworkListener.stop = true;
        try {
            nl.join();
        } catch (InterruptedException e) {}
        NetworkListener.stop = false;
    }
}