package ro.qoffice.everyonesdj.player;

import android.media.MediaPlayer;

/**
 * Created by Alin on 16.11.2014.
 */
public class EDJOnPrepared implements MediaPlayer.OnPreparedListener {
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
