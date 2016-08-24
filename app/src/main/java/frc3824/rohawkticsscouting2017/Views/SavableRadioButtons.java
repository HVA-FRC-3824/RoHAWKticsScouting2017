package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Savable set of radiobuttons that has a label attached.
 */
public class SavableRadioButtons extends SavableView {

    private final static String TAG = "CustomRadioButtons";

    private String[] mResourceStrings;
    private RadioGroup mRadioButtons;
    private String mKey;

    public SavableRadioButtons(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_radio_buttons, this);

        // Setup label and get key
        TextView label = (TextView) findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        // Get RadioButtons
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableRadioButtons);
        int radioValuesId = typedArray.getResourceId(R.styleable.SavableRadioButtons_radio_values, 0);
        mResourceStrings = context.getResources().getStringArray(radioValuesId);

        mRadioButtons = (RadioGroup) findViewById(R.id.radiobuttons);

        for(int i = 0; i < mResourceStrings.length; i++)
        {
            RadioButton radioButton = new RadioButton(context, attrs);
            radioButton.setText(mResourceStrings[i]);
            radioButton.setId(i);
            mRadioButtons.addView(radioButton);
        }
        mRadioButtons.check(0);
    }

    @Override
    public String writeToMap(ScoutMap map) {
        if (mRadioButtons.getCheckedRadioButtonId() != -1)
        {
            map.put(mKey, mResourceStrings[mRadioButtons.getCheckedRadioButtonId()]);
        }

        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map)
    {
        if(map.contains(mKey))
        {
            try {
                String selectedString = map.getString(mKey);
                int index = Arrays.asList(mResourceStrings).indexOf(selectedString);
                if(index > -1)
                {
                    mRadioButtons.check(index);
                }
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }

        return "";
    }
}
