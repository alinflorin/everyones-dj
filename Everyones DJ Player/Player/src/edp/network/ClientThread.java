package edp.network;

import edp.player.EDPlayer;
import edp.libraries.own.settings.SettingsManager;
import edp.player.Song;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ro.qoffice.everyonesdj.network.DataSet;
import java.nio.file.Files;
import java.util.Objects;
import ro.qoffice.everyonesdj.network.AndroidPlaylist;

public class ClientThread extends Thread {

    private final Socket s;
    private final EDPlayer player;
    private final Server srv;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean stop = false;

    public ClientThread(Server srv, EDPlayer edp, Socket sck) {
        s = sck;
        this.srv = srv;
        player = edp;
        try {
            out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            try {
                s.close();
            } catch (IOException ex1) {

            }
        }
    }

    public void send(DataSet ds) {
        try {
            out.writeObject(ds);
            out.flush();
        } catch (IOException e) {
        }
    }

    public void removeMyself() {
        synchronized (srv.getClients()) {
            srv.getClients().remove(this);
        }
    }

    @Override
    public void run() {
        while (!stop && s.isConnected()) {
            try {
                DataSet recv = (DataSet) in.readObject();
                DataSet r = new DataSet();
                switch (recv.getCommand()) {
                    case "GETBANNER":
                        if (SettingsManager.getSettingsManager().getString("banner") == null) {
                            r.setError(true);
                        } else {
                            File f = new File(SettingsManager.getSettingsManager().getString("banner"));
                            byte[] arr = Files.readAllBytes(f.toPath());
                            r.setValue(arr);
                        }
                        break;

                    case "GETPLAYLIST":
                        r.setCommand("NEWPLAYLIST");
                        AndroidPlaylist l = player.exportForNetwork();
                        r.setValue(l);
                        break;

                    case "PREVIEW":
                        r.setCommand("RECVMP3");
                        int sid = (int) recv.getValue();
                        Song sng = player.playlist.getAt(sid);
                        if (sng == null) {
                            r.setError(true);
                        } else {
                            byte[] prev = player.cutSeconds(sng);
                            if (prev == null) {
                                r.setError(true);
                            } else {
                                r.setValue(prev);
                            }
                        }
                        break;

                    case "VOTE":
                        r.setCommand("RETURNVOTE");
                        int si = (int) recv.getValue();
                        Song son = player.playlist.getAt(si);
                        if (son == null) {
                            r.setError(true);
                        } else {
                            if (player.getCurrent().equals(son)) {
                                r.setError(true);
                            } else {
                                Voter v = new Voter(s.getInetAddress().toString(), recv.getId());
                                if (srv.didVote(v)) {
                                    r.setError(true);
                                } else {
                                    v.voted();
                                    srv.addVoter(v);
                                    son.setVotes(son.getVotes() + 1);
                                    player.getGUI().refreshList();
                                    r.setValue(player.exportForNetwork());
                                }
                            }
                        }
                        break;
                }
                out.writeObject(r);
                out.flush();
                out.reset();
            } catch (NullPointerException | IOException | ClassNotFoundException ex) {
                this.removeMyself();
                break;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.s);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientThread other = (ClientThread) obj;
        return Objects.equals(this.s, other.s);
    }

    public void stopThread() {
        stop = true;
        try {
            s.close();
        } catch (IOException x) {
        }
    }
}
