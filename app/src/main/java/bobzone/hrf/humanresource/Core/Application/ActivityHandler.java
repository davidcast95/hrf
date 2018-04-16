package bobzone.hrf.humanresource.Core.Application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by davidwibisono on 11/11/17.
 */

public class ActivityHandler extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, Thread.getDefaultUncaughtExceptionHandler()));
    }
}
