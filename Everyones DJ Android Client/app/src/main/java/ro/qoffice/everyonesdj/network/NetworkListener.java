package ro.qoffice.everyonesdj.network;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import ro.qoffice.everyonesdj.R;
import ro.qoffice.everyonesdj.activity.PlaylistActivity;
import ro.qoffice.everyonesdj.activity.StartActivity;
import ro.qoffice.everyonesdj.config.SettingsManager;
import ro.qoffice.everyonesdj.player.EDJPlayer;
import ro.qoffice.everyonesdj.storage.StorageManager;
import android.content.Intent;

public class NetworkListener extends Thread {
    private NetworkClient ncl;
    private PlaylistActivity pla;
    public static boolean stop = false;

    public NetworkListener(NetworkClient ncl,PlaylistActivity pla) {
        this.ncl = ncl;
        this.pla = pla;
    }

    @Override
    public void run() {
        ncl.reset();
        while (ncl != null && ncl.isConnected() && !stop) {
            try {
                DataSet r = ncl.waitForResponse();
                if (r != null) {
                        if (r.getCommand().equals("NEWPLAYLIST")) {
                            if (r.getError()) {
                                pla.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(pla,pla.getText(R.string.playlist_retrieval_error),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                final AndroidPlaylist ap = (AndroidPlaylist) r.getValue();
                                pla.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pla.getSongs().clear();
                                        for (int i = 0; i < ap.getList().size(); i++) {
                                            String el = ap.getList().get(i);
                                            if (ap.getCurrentSong() == i) {
                                                el = pla.getString(R.string.playing_star) + el;
                                            }
                                            pla.getSongs().add(el);
                                        }
                                        pla.getAdapter().notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        if (r.getCommand().equals("RECVMP3")) {
                            if (r.getError()) {
                                pla.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(pla,pla.getString(R.string.mp3_fetching),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                StorageManager sm = StorageManager.getInstance();
                                EDJPlayer player = EDJPlayer.getInstance();
                                byte[] mp3 = (byte[]) r.getValue();
                                File song = sm.saveTempFile(mp3, pla.getCacheDir());
                                if (song == null || !song.exists()) {
                                    pla.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(pla, pla.getString(R.string.cache_dir_error), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    try {
                                        player.play(song);
                                    } catch (IOException e) {
                                        pla.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(pla, pla.getString(R.string.could_not_play_song), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        if (r.getCommand().equals("RETURNVOTE")) {
                            SettingsManager sm = new SettingsManager(pla);
                            if (r.getError()) {
                                pla.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(pla, pla.getString(R.string.could_not_vote), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                sm.setLastVoteTS(System.currentTimeMillis());
                                final AndroidPlaylist ap = (AndroidPlaylist) r.getValue();
                                pla.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pla.getAdapter().clear();
                                        for (int i = 0; i < ap.getList().size(); i++) {
                                            pla.getAdapter().add(ap.getList().get(i));
                                        }
                                        AlertDialog.Builder bld = new AlertDialog.Builder(pla);
                                        bld.setMessage(pla.getString(R.string.vote_again_5));
                                        bld.setTitle(pla.getString(R.string.voted));
                                        bld.setPositiveButton(pla.getString(R.string.ok2),new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        AlertDialog dlg = bld.create();
                                        dlg.show();
                                    }
                                });
                            }
                        }

                    ncl.reset();
                }
            } catch (IOException e) {
                pla.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(pla,pla.getString(R.string.disconnected),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(pla, StartActivity.class);
                        pla.startActivity(i);
                        pla.finish();
                    }
                });
                break;
            } catch (ClassNotFoundException e) {
                break;
            }
        }
    }
}
