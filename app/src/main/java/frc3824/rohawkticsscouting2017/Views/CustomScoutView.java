package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Abstract base class for all custom widgets that save and retrieve data
 */
public abstract class CustomScoutView extends RelativeLayout{

    private final static String TAG = "CustomScoutView";

    public CustomScoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String writeToMap(ScoutMap map) {
        Log.e(TAG, "Base writeToMap");
        return "Base writeToMap";
    }

    public String restoreFromMap(ScoutMap map) {
        Log.e(TAG, "Base restoreFromMap");
        return "Base restoreFromMap";
    }
}
