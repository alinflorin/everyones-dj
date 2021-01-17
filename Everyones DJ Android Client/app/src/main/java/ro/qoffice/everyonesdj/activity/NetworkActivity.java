package ro.qoffice.everyonesdj.activity;
import android.app.Activity;
import android.content.Intent;
public abstract class NetworkActivity extends Activity {
    public void goToWelcome() {
        Intent i = new Intent(this,WelcomeActivity.class);
        startActivity(i);
        finish();
    }
}
