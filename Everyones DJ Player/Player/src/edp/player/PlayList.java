package edp.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlayList implements Serializable {

    private ArrayList<Song> list;

    public PlayList() {
        list = new ArrayList<>();
    }

    public void loadPlaylistFromFile() throws IOException, ClassNotFoundException {
        File plist = new File("playlist.edp");
        try {
            ArrayList<Song> savedlist;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plist))) {
                savedlist = (ArrayList<Song>) ois.readObject();
            }
            this.list = savedlist;
        } catch (FileNotFoundException ex) {
            plist.createNewFile();
        }
    }

    public void savePlaylistToFile() {
        File plist = new File("playlist.edp");
        try {
            if (!plist.exists()) {
                plist.createNewFile();
            }
            ArrayList<Song> newlist = (ArrayList<Song>) this.list.clone();
            newlist.stream().forEach((s) -> {
                s.setVotes(0);
            });
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plist))) {
                oos.writeObject(newlist);
                oos.flush();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void scanDirectory(File f) {
        File[] all = f.listFiles();
        for (File d : all) {
            if (d.canRead()) {
                if (d.isDirectory()) {
                    scanDirectory(d);
                } else {
                    String ext = "";
                    int i = d.getName().lastIndexOf(".");
                    if (i >= 0) {
                        ext = d.getName().substring(i + 1).toLowerCase();
                    }
                    if ("mp3".equals(ext)) {
                        addSongs(new Song[]{new Song(d)});
                    }
                }
            }
        }
    }

    public void refreshIndexes() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPlaylistNo(i + 1);
        }
    }

    public void addSongs(Song[] songs) {
        for (Song s : songs) {
            if (s.isValid()) {
                s.setPlaylistNo(list.size() + 1);
                list.add(s);
            }
        }
        savePlaylistToFile();
    }

    public void removeSongs(Song[] songs) {
        for (Song s : songs) {
            if (list.contains(s)) {
                list.remove(s);
            }
        }
        refreshIndexes();
        savePlaylistToFile();
    }

    public ArrayList<Song> getList() {
        return list;
    }

    public int getSize() {
        return list.size();
    }

    public Song getAt(int i) {
        return list.get(i);
    }

    public void setAt(Song s, int i) {
        list.set(i, s);
    }

    private int otherSong() {
        Random r = new Random(System.currentTimeMillis());
        int low = 0;
        int high = getSize();
        return r.nextInt(high - low) + low;
    }

    public Song getMostVoted() {
        int max = 0;
        int idmax = -1;
        for (Song s : list) {
            if (s.getVotes() > max) {
                max = s.getVotes();
                idmax = s.getPlaylistNo();
            }
        }
        if (idmax == -1) {
            return null;
        }
        return list.get(idmax - 1);
    }

    public Song nextSong() {
        int i = otherSong();
        return list.get(i);
    }
}
