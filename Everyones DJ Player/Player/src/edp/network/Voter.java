package edp.network;

import java.util.Objects;

public class Voter {

    private final String ip;
    private final String id;
    private long lastvote;

    public Voter(String ip, String id) {
        this.ip = ip;
        this.id = id;
    }

    public void voted() {
        lastvote = System.currentTimeMillis();
    }

    public String getIp() {
        return ip;
    }

    public String getId() {
        return id;
    }

    public long getLast() {
        return lastvote;
    }

    public boolean canVoteAgain() {
        return (System.currentTimeMillis() - lastvote > 1000 * 60 * 5);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.ip);
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Voter other = (Voter) obj;
        if (!Objects.equals(this.ip, other.ip)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
