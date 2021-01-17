package edp.player;

import edp.gui.PlayerGUI;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jlgui.basicplayer.BasicPlayer;

public class SeekThread extends Thread {

    private final PlayerGUI gui;
    private final EDPlayer edp;
    private boolean sem = false;
    private int i = 0;

    public SeekThread(PlayerGUI gui, EDPlayer edp) {
        this.edp = edp;
        this.gui = gui;
    }

    @Override
    public void run() {
        all:
        for (i = 0; i < edp.getCurrent().getDuration() / 1000 && sem == false; i++) {

            switch (edp.getStatus()) {
                case BasicPlayer.PLAYING:
                    long millis = 1000 * i;
                    String x = String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    if (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) < 10) {
                        x = x.replace(":", ":0");
                    }
                    gui.currentTime.setText(x);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SeekThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case BasicPlayer.PAUSED:
                    i--;
                    break;

                default:
                    break all;
            }
        }
    }

    public void kill() {
        this.sem = true;
    }

    public void seek(int val) {
        i = val;
    }
}
