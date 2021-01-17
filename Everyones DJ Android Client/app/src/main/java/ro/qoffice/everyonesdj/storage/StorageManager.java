package ro.qoffice.everyonesdj.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alin on 16.11.2014.
 */
public class StorageManager {
    private static StorageManager instance = null;

    private StorageManager() {

    }

    public static StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    public File saveTempFile(byte[] content,File dir) {
        try {
            File temp = File.createTempFile("preview","mp3",dir);
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write(content);
            fos.close();
            return temp;
        } catch (IOException e) {
            return null;
        }
    }

}
