package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 *         Created: 8/11/16
 */
public class CustomNumeric extends CustomScoutView {

    private final static String TAG = "CustomNumeric";

    private EditText mNumeric;
    private String mKey;

    public CustomNumeric(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_numeric, this);

        mNumeric = (EditText) findViewById(R.id.numeric);

        // Setup label and get key
        TextView label = (TextView) this.findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomScoutView);
        label.setText(typedArray.getString(R.styleable.CustomScoutView_label));
        mKey = typedArray.getString(R.styleable.CustomScoutView_key);
        typedArray.recycle();
    }

    @Override
    public String writeToMap(ScoutMap map)
    {
        if(String.valueOf(mNumeric.getText()).equals(""))
            map.put(mKey, 0.0);
        else
            map.put(mKey, Double.valueOf(String.valueOf(mNumeric.getText())));
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map)
    {
        if(map.contains(mKey))
        {
            try {
                mNumeric.setText(String.valueOf(map.getDouble(mKey)));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }

        return "";
    }
}
