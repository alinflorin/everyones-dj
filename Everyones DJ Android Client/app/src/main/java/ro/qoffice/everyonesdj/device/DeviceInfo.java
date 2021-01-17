package ro.qoffice.everyonesdj.device;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Alin on 14.11.2014.
 */
public class DeviceInfo {

    public static String getDeviceId(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
