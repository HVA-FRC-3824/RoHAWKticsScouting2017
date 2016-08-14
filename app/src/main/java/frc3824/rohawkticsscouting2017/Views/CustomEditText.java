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
public class CustomEditText extends CustomScoutView {

    private final static String TAG = "CustomEditText";

    private EditText mEditText;
    private String mKey;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_edittext, this);

        mEditText = (EditText) findViewById(R.id.edittext);

        // Setup label and get key
        TextView label = (TextView) findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomScoutView);
        label.setText(typedArray.getString(R.styleable.CustomScoutView_label));
        mKey = typedArray.getString(R.styleable.CustomScoutView_key);
        typedArray.recycle();
    }

    @Override
    public String writeToMap(ScoutMap map)
    {
        map.put(mKey, String.valueOf(mEditText.getText()));
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map)
    {
        if(map.contains(mKey))
        {
            try {
                mEditText.setText(map.getString(mKey));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }
        return "";
    }
}
