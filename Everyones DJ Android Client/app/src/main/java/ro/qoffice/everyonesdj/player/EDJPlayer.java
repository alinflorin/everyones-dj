package ro.qoffice.everyonesdj.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;

public class EDJPlayer {
    private MediaPlayer player;
    private static EDJPlayer instance=null;
    public static String path=null;

    private EDJPlayer() {
        player = new MediaPlayer();
        player.setOnCompletionListener(new EDJOnCompletion());
        player.setOnPreparedListener(new EDJOnPrepared());
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public static EDJPlayer getInstance() {
        if (instance == null) {
            instance = new EDJPlayer();
        }
        return instance;
    }

    public void play(File f) throws IOException {
        try {
            player.setDataSource(f.getAbsolutePath());
            path = f.getAbsolutePath();
            player.prepareAsync();
        } catch(IllegalStateException e) {}
    }

    public void stop() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        if (path != null) {
            File f = new File(path);
            if (f.exists()) f.delete();
        }
    }
}
