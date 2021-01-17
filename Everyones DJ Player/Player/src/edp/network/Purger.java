package edp.network;
import java.util.TimerTask;

public class Purger extends TimerTask{
    private Server srv;
    
    public Purger(Server s) {
        srv = s;
    }
    
    @Override
    public void run() {
        srv.purge();
    }
}
