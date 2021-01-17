package edp.libraries.own.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class SettingsManager {

    private static SettingsManager instance = null;
    private Properties props;
    private FileWriter writer;
    private FileReader reader;

    private SettingsManager() {
        props = new Properties();
        try {
            reader = new FileReader("settings.edp");
            props.load(reader);
            reader.close();
        } catch (FileNotFoundException ex) {
            try {
                new File("settings.edp").createNewFile();
            } catch (IOException ex1) {
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Settings file couldn't be read.", "Settings error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static SettingsManager getSettingsManager() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public String getString(String key) {
        return props.getProperty(key);
    }

    public void putString(String key, String val) {
        props.put(key, val);
        try {
            writer = new FileWriter("settings.edp");
            props.store(writer, "EDP Settings");
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Settings file couldn't be written.", "Settings error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
