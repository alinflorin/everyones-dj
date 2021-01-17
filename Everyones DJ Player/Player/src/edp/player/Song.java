package edp.player;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Song implements Serializable{
    private String artist="",title="",path,filename;
    private int votes=0,playlistno=-1;
    private long duration,durationms;
    private boolean valid=false;
    private int bitrate=-1;
    private int channels=2;
    
    public Song(File f) {
        if (f.exists() && !f.isDirectory() && f.canRead() && f.isFile()) {
            valid = true;
            this.path = f.getAbsolutePath();
            this.filename = f.getName();
            try {
                AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(f);
                AudioFormat baseFormat = baseFileFormat.getFormat();
                Map properties = baseFileFormat.properties();
                Map properties2 = baseFormat.properties();
                try {
                    channels = (int)properties.get("mp3.channels");
                } catch (NullPointerException e) {}
                try {
                    bitrate = (int)properties2.get("bitrate");
                }catch (NullPointerException e) {}
                try {
                    durationms = (long)properties.get("duration");
                    duration = durationms/1000;
                }catch (NullPointerException e) {}
                try{
                    title = properties.get("title").toString();
                }catch (NullPointerException e) {
                    title = this.filename.replace(".mp3", "");
                }
                try {
                    artist = properties.get("author").toString();
                }catch (NullPointerException e) {}
                if (title.length() == 0) {
                    title = this.filename.replace(".mp3", "");
                }
            } catch (UnsupportedAudioFileException | IOException ex) {
                valid = false;
            }
        }
    }

    public void setPlaylistNo(int playlist_no) {
        this.playlistno = playlist_no;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getPlaylistNo() {
        return playlistno;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public int getVotes() {
        return votes;
    }

    public boolean isValid() {
        return valid;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getChannels() {
        return channels;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song) {
            Song s = (Song)obj;
            return s.path.equals(this.path);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.path);
        return hash;
    }
}
