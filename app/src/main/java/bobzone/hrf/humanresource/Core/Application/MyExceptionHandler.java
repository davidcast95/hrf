package bobzone.hrf.humanresource.Core.Application;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import bobzone.hrf.humanresource.Model.User.UserModel;
import bobzone.hrf.humanresource.SplashScreen;


/**
 * Created by davidwibisono on 11/11/17.
 */

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandler(Activity a, Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler) {
        activity = a;
        this.mDefaultUncaughtExceptionHandler = mDefaultUncaughtExceptionHandler;
    }

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e("CRASH", throwable.getLocalizedMessage());
        Crashlytics.logException(throwable);

        Crashlytics.setUserIdentifier(UserModel.Companion.getInstance().getFullname());

        mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);


    }
}
