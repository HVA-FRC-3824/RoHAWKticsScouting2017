package frc3824.rohawkticsscouting2017;

import android.app.Application;
import android.content.Context;

/**
 * @author frc3824
 * Created: 4/8/2017
 */

public class AppContext extends Application {
    private static AppContext mSingleton;

    public AppContext(){
        super();
        mSingleton = this;
    }

    public static Context getDefaultContext(){
        return mSingleton.getApplicationContext();
    }
}
