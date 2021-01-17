package edp.player;

import edp.gui.PlayerGUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import ro.qoffice.everyonesdj.network.AndroidPlaylist;

public class EDPlayer {

    public static boolean reallyStopped = true;
    public PlayList playlist;
    private PlayerGUI gui;
    private Song current = null;
    private BasicPlayer player;
    private SeekThread st;
    private String search = "";
    private EDPlayerListener edpl;
    private Song lastp = null;

    public EDPlayer(PlayerGUI gui) {
        this.gui = gui;
        playlist = new PlayList();
        player = new BasicPlayer();
        edpl = new EDPlayerListener(this);
        player.addBasicPlayerListener(edpl);
        try {
            playlist.loadPlaylistFromFile();
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    public void play() {
        if (current != null) {
            try {
                player.play();
                player.setGain((gui.volume.getValue() * 1.0f) / 100f);
                st = new SeekThread(gui, this);
                st.start();
                gui.btnPlay.setIcon(new ImageIcon(getClass().getResource("/edp/gui/images/pause.png")));
                reallyStopped = false;
            } catch (BasicPlayerException ex) {
            }
        }
    }

    public void stop() {
        reallyStopped = true;
        try {
            player.stop();
            if (st != null) {
                st.kill();
            }
            gui.currentTime.setText("0:00");
            gui.btnPlay.setIcon(new ImageIcon(getClass().getResource("/edp/gui/images/play.png")));

        } catch (BasicPlayerException ex) {
        }
    }

    public void pause() {
        try {
            player.pause();
            gui.btnPlay.setIcon(new ImageIcon(getClass().getResource("/edp/gui/images/play.png")));
        } catch (BasicPlayerException ex) {
        }
    }

    public void resume() {
        try {
            player.resume();
            gui.btnPlay.setIcon(new ImageIcon(getClass().getResource("/edp/gui/images/pause.png")));
        } catch (BasicPlayerException ex) {
        }
    }

    public void setSong(Song s) {
        try {
            stop();
            player.open(new File(s.getPath()));
            current = s;
            s.setVotes(0);
            gui.lblSong.setText(getCurrent().getArtist() + " - " + getCurrent().getTitle());
            gui.sendPlaylist();
        } catch (BasicPlayerException ex) {
        }
    }

    public Song getCurrent() {
        return current;
    }

    public int getStatus() {
        return player.getStatus();
    }

    public void setVolume(double vol) {
        try {
            player.setGain(vol);
        } catch (BasicPlayerException ex) {

        }
    }

    public void setSearch(String x) {
        search = x;
    }

    public String getSearch() {
        return search;
    }

    public void seek(long l) throws BasicPlayerException {
        player.seek(l);
    }

    public void next() {
        Song s = playlist.getMostVoted();
        if (s == null || (lastp != null && s == lastp)) {
            s = playlist.nextSong();
        } else {
            s.setVotes(0);
        }
        gui.refreshList();
        setSong(s);
        play();
        lastp = s;
        gui.highlight(s.getPlaylistNo());
    }

    public PlayerGUI getGUI() {
        return gui;
    }

    public AndroidPlaylist exportForNetwork() {
        AndroidPlaylist ap = new AndroidPlaylist();
        int i = 0;
        for (Song s : playlist.getList()) {
            ap.addSong((i + 1) + ". " + s.getArtist() + " - " + s.getTitle() + "\n(" + s.getVotes() + " votes)");
            if (s == this.getCurrent()) {
                ap.setCurrentSong(i);
            }
            i++;
        }
        return ap;
    }

    public byte[] cutSeconds(Song s) {
        int lim;
        if (s.getBitrate() == -1) {
            lim = 1024 * 500;
        } else {
            lim = (s.getBitrate() / 8) * 10;
        }
        File f = new File(s.getPath());
        try {
            byte[] all = Files.readAllBytes(f.toPath());
            if (all.length < lim) {
                return all;
            }
            return Arrays.copyOf(all, lim);
        } catch (IOException e) {
            return null;
        }
    }
}
