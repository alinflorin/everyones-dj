package ro.qoffice.everyonesdj.network;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.activity.NetworkActivity;
import ro.qoffice.everyonesdj.config.Cfg;
import ro.qoffice.everyonesdj.gui.GuiHelper;

public class NetworkClient extends Thread {
    private Socket s;
    private String ip;
    private Context ctx;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static NetworkClient instance;

    private NetworkClient(String ip,Context ctx) {
        this.ip = ip;
        this.ctx = ctx;
    }

    public static NetworkClient getInstance(String ip,Context ctx) {
        if (instance == null) {
            instance = new NetworkClient(ip,ctx);
        } else {
            instance.stopCl();
            instance = null;
            instance = new NetworkClient(ip,ctx);
        }
        return instance;
    }

    public static NetworkClient getInstance() {
        return instance;
    }

    public DataSet sendAndGetResponse(DataSet ds) {
        try {
            out.writeObject(ds);
            out.flush();
            return (DataSet)in.readObject();
        } catch (IOException e) {
            ((Activity)ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx,ctx.getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        } catch (ClassNotFoundException e) {
            ((Activity)ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx,ctx.getString(R.string.player_should_be_updated),Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

    }

    public void send(DataSet ds) {
        try {
            out.writeObject(ds);
            out.flush();
        } catch (IOException e) {
            ((Activity)ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx,ctx.getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public DataSet waitForResponse() throws IOException, ClassNotFoundException {
        return (DataSet)in.readObject();
    }

    public boolean isConnected() {
        return s.isConnected();
    }

    public void reset() {
        try {
            in.reset();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void stopCl() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (s != null) s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            s = new Socket(ip, Cfg.SCANPORT);
            out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());
            ((NetworkActivity)ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((NetworkActivity) ctx).goToWelcome();
                }
            });
        } catch (IOException e) {
            GuiHelper gh = new GuiHelper(ctx);
            gh.OkDialog(ctx.getString(R.string.error), ctx.getString(R.string.net_com_err));
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (s!=null && s.isConnected()) s.close();
                this.join();
            } catch (InterruptedException e1) {
            } catch (IOException e1) {
            }
            return;
        }
    }
}
