package ro.qoffice.everyonesdj.network;

import java.io.Serializable;
import java.util.ArrayList;

public class AndroidPlaylist implements Serializable {
    private final ArrayList<String> songs=new ArrayList<String>();
    private int currentSong=-1;

    public void setCurrentSong(int c) {
        currentSong = c;
    }

    public void addSong(String s) {
        songs.add(s);
    }

    public ArrayList<String> getList() {
        return songs;
    }

    public int getCurrentSong() {
        return currentSong;
    }
}
