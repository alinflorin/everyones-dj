package ro.qoffice.everyonesdj.player;

import android.media.MediaPlayer;

import java.io.File;

/**
 * Created by Alin on 16.11.2014.
 */
public class EDJOnCompletion implements MediaPlayer.OnCompletionListener {
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (EDJPlayer.path != null) {
            File f = new File(EDJPlayer.path);
            if (f.exists()) f.delete();
        }
    }
}
