package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author frc3824
 * Created: 8/12/16
 *
 * Savable spinner with a label attached
 */
public class SavableSpinner extends SavableView {

    private final static String TAG = "CustomSpinner";

    private String[] mResourceStrings;
    private String mKey;
    private Spinner mSpinner;


    public SavableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_spinner, this);

        // Setup label and get key
        TextView label = (TextView) this.findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableSpinner);
        int spinnerValueId = typedArray.getResourceId(R.styleable.SavableSpinner_spinner_values, 0);
        mResourceStrings = context.getResources().getStringArray(spinnerValueId);
        typedArray.recycle();

        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, mResourceStrings);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public String writeToMap(ScoutMap map) {
        map.put(mKey, String.valueOf(mSpinner.getSelectedItem()));
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                mSpinner.setSelection(Arrays.asList(mResourceStrings).indexOf(map.getString(mKey)));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }
        return "";
    }
}
