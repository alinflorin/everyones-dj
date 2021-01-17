package ro.qoffice.everyonesdj.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ro.qoffice.everyonesdj.R;

/**
 * Created by Alin on 22.10.2014.
 */
public class GuiHelper {
    private Context ctx;

    public GuiHelper(Context c) {
        ctx = c;
    }

    public void OkDialog(final String title, final String message) {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog dlg = new AlertDialog.Builder(ctx).create();
                dlg.setTitle(title);
                dlg.setMessage(message);
                dlg.setButton(AlertDialog.BUTTON_POSITIVE,ctx.getString(R.string.ok),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });
    }

    public void OkDialog(final String title, final String message, final DialogInterface.OnClickListener ocl) {
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog dlg = new AlertDialog.Builder(ctx).create();
                dlg.setTitle(title);
                dlg.setMessage(message);
                dlg.setButton(AlertDialog.BUTTON_POSITIVE, ctx.getString(R.string.ok), ocl);
                dlg.show();
            }
        });
    }

}
