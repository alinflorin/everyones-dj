package edp.updater;

import edp.libraries.own.settings.SettingsManager;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class Main {
    public static String url = "http://edj.qoffice.ro/";
    public static String url1 = url + "latest.txt";
    public static String url2 = url + "player.jar";
    
    private static String md5() throws IOException, NoSuchAlgorithmException {
        String path = "player.jar";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(Paths.get(path)));
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
    
    private static String netmd5(){
        try {
            URL u = new URL(url1);
            URLConnection con = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return in.readLine();
        } catch (MalformedURLException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
    
    private static void update() throws MalformedURLException, IOException {
        URL u = new URL(url2);
        URLConnection con = u.openConnection();
        DataInputStream dis = new DataInputStream(con.getInputStream());
        File player = new File("player.jar");
        if (player.exists()) {
            player.delete();
        }
        player.createNewFile();
        FileOutputStream fos = new FileOutputStream(player);
        byte[] buff = new byte[1024];
        int n=0;
        while ((n=dis.read(buff, 0, buff.length)) != -1) {
            fos.write(buff, 0, n);
        }
        fos.flush();   
        fos.close();
        dis.close();
        Process p = Runtime.getRuntime().exec("java -jar player.jar");
    }
    
    public static void main(String[] args) {
        SettingsManager settings = SettingsManager.getSettingsManager();
        String tempurl = settings.getString("updaterurl");
        if (tempurl != null) {
            url = tempurl;
        }
        try {
            if (md5().equals(netmd5())) {
                Process p = Runtime.getRuntime().exec("java -jar player.jar");
            } else {
                update();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            try {
                Process p = Runtime.getRuntime().exec("java -jar player.jar");
            } catch (IOException ex) {
            }
        }
    }
}
