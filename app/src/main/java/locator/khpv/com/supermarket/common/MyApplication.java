package locator.khpv.com.supermarket.common;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.client.Firebase;

/**
 * Created by Administrator on 4/20/2016.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Fresco.initialize(this);
        Firebase.setAndroidContext(this);
    }
}
