package ro.qoffice.everyonesdj.config;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsManager {
    private SharedPreferences sp;
    private Context ctx;

    public SettingsManager(Context ctx) {
        this.ctx = ctx;
        sp = ctx.getSharedPreferences("ro.qoffice.everyonesdj",Context.MODE_PRIVATE);
    }


    public long getLastVoteTS() {
        return sp.getLong("ro.qoffice.everyonesdj.lastvotets",-1);
    }

    public void setLastVoteTS(long v) {
        sp.edit().putLong("ro.qoffice.everyonesdj.lastvotets",v).commit();
    }


    public String getLastIP() {
        return sp.getString("ro.qoffice.everyonesdj.lastip","");
    }

    public void setLastIP(String ip) {
        sp.edit().putString("ro.qoffice.everyonesdj.lastip",ip).commit();
    }



}
