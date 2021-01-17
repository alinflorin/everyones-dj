package edp.gui;

import edp.libraries.thirdparty.gui.FileDrop;
import edp.player.PlayList;
import edp.player.Song;
import java.io.File;

public class DragAndDropListener implements FileDrop.Listener {

    private final PlayList pls;
    private final PlayerGUI gui;

    public DragAndDropListener(PlayerGUI g, PlayList p) {
        pls = p;
        gui = g;
    }

    private String getExt(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    @Override
    public void filesDropped(File[] files) {
        for (File f : files) {
            if (f.isDirectory()) {
                pls.scanDirectory(f);
            } else {
                Song s = new Song(f);
                pls.addSongs(new Song[]{s});
            }
        }
        gui.refreshList();
        gui.sendPlaylist();
    }
}
