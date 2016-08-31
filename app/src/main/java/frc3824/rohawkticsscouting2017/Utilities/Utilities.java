package frc3824.rohawkticsscouting2017.Utilities;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import frc3824.rohawkticsscouting2017.Views.SavableEditText;
import frc3824.rohawkticsscouting2017.Views.SavableNumeric;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Various Utility Functions
 */
public class Utilities {

    private final static String TAG = "Utilities";

    public static void setupUi(final Activity activity, View view)
    {
        // Setup touch listener for non-textbox views to hide the keyboard.
        if(!(view instanceof SavableEditText) && !(view instanceof SavableNumeric) && !(view instanceof EditText)){
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        // If layout is a container, iterate over children and seed recursion.
        if(view instanceof ViewGroup)
        {
            int childCount = ((ViewGroup) view).getChildCount();
            for(int i = 0; i < childCount; i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUi(activity, innerView);
            }
        }
    }

    private static void hideSoftKeyboard(Activity activity)
    {
        if(activity.getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static int dpToPixels(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
