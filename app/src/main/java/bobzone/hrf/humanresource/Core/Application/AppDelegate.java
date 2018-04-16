package bobzone.hrf.humanresource.Core.Application;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by davidwibisono on 11/11/17.
 */

public class AppDelegate extends Application {

    public static AppDelegate instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static AppDelegate getInstance() {
        return instance;
    }
}
