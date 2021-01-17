package edp.player;

import java.util.Map;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class EDPlayerListener implements BasicPlayerListener {

    private EDPlayer edp;

    public EDPlayerListener(EDPlayer e) {
        edp = e;
    }

    @Override
    public void opened(Object o, Map map) {
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        switch (bpe.getCode()) {
            case BasicPlayerEvent.STOPPED:
                if (!EDPlayer.reallyStopped) {
                    edp.next();
                }
                break;
        }
    }

    @Override
    public void setController(BasicController bc) {
    }
}
