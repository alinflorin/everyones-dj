package edp.network;
import edp.player.EDPlayer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Timer;

public class Server extends Thread{
    private ServerSocket ss;
    private final EDPlayer player;
    private final List<Voter> voters;
    private final List<ClientThread> clients;
    private final Purger prg;
    private final Timer t;
    
    public Server(EDPlayer p) {
        voters = Collections.synchronizedList(new ArrayList<Voter>());
        clients = Collections.synchronizedList(new ArrayList<ClientThread>());
        player = p;
        t = new Timer();
        prg = new Purger(this);
    }
    
    @Override
    public void run() {
        try {
            ss = new ServerSocket(39567);
            t.schedule(prg, 0, 5000);
            while (!ss.isClosed()) {
                try {
                    Socket client = ss.accept();
                    ClientThread ct = new ClientThread(this,player,client);
                    clients.add(ct);
                    ct.start();
                } catch (IOException e) {}
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(player.getGUI(), "Communication error! Check if port 39567 is available for the voting server!", "Server error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void stopServer() {
        try {
            synchronized (clients) {
            for (ClientThread ct : clients) {
               ct.stopThread();
            }
            clients.clear();
            }
            synchronized(voters) {
		voters.clear();
            }
            ss.close();
            prg.cancel();
            this.join();
            System.out.println("LOG: Stopped server successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(player.getGUI(), "Could not stop voting server!", "Server error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException ex) {
            System.out.println("LOG: Could not stop server thread.");
        }
    }
    
    public synchronized void addVoter(Voter v) {
            voters.add(v);
    }
    
    public synchronized boolean didVote(Voter v) {
            return voters.contains(v);
    }
    
    public synchronized void purge() {
        System.out.println("LOG: Purging...");
        for(Voter v : voters) {
            if (v.canVoteAgain()) {
                voters.remove(v);
            }
        }
    }
    
    public boolean isOn() {
        return !ss.isClosed();
    }
    
    public List<ClientThread> getClients() {
        synchronized(clients) {
        return clients;
        }
    }
}
