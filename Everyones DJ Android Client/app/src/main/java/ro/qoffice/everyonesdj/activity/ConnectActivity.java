package ro.qoffice.everyonesdj.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.config.SettingsManager;
import ro.qoffice.everyonesdj.gui.GuiHelper;
import ro.qoffice.everyonesdj.network.NetworkClient;

public class ConnectActivity extends NetworkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Button btnConnect = (Button)findViewById(R.id.btnConnect);
        final EditText txtHost = (EditText)findViewById(R.id.txtHostnameIP);
        final GuiHelper gh = new GuiHelper(this);
        final SettingsManager sm = new SettingsManager(this);
        if (sm.getLastIP().length() > 0) {
            txtHost.setText(sm.getLastIP());
        }
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtHostName = txtHost.getText().toString();
                if (txtHostName.length() == 0) {
                    gh.OkDialog(getString(R.string.error),getString(R.string.unknown_host));
                    return;
                }
                sm.setLastIP(txtHostName);
                NetworkClient client = NetworkClient.getInstance(txtHostName,ConnectActivity.this);
                client.start();
            }
        });
    }
}