package ro.qoffice.everyonesdj.activity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.gui.GuiHelper;
import ro.qoffice.everyonesdj.network.DataSet;
import ro.qoffice.everyonesdj.network.NetworkClient;

public class WelcomeActivity extends Activity {
    private NetworkClient ncl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ncl = NetworkClient.getInstance();
        ((Button)findViewById(R.id.btnSkip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this,PlaylistActivity.class);
                startActivity(i);
                WelcomeActivity.this.finish();
            }
        });
        if (ncl == null) {
            GuiHelper gh = new GuiHelper(this);
            DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(WelcomeActivity.this,StartActivity.class);
                    startActivity(i);
                    finish();
                }
            };
            gh.OkDialog(getString(R.string.error),getString(R.string.network_unavail),ocl);
            return;
        }
        //Get banner
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataSet r = ncl.sendAndGetResponse(new DataSet(null,"GETBANNER",null));
                if (r.getError() == false)  {
                    byte[] arr = (byte[])r.getValue();
                    final Bitmap bmp = BitmapFactory.decodeByteArray(arr,0,arr.length);
                    WelcomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView)findViewById(R.id.img)).setImageBitmap(bmp);
                        }
                    });
                }
            }
        }).start();
    }
}
