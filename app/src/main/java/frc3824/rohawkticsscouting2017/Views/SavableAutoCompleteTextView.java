package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 * Created: 8/12/16
 *
 * Savable textbox that can autocomplete with an array of options
 */
public class SavableAutoCompleteTextView extends SavableView {

    // TAG is shortened so that it can be used with log
    private final static String TAG = "CustomAutoCompleteText";

    private String[] mResourceStrings;
    private String mKey;
    private AutoCompleteTextView mAutocompleteTextView;


    public SavableAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_autocomplete_textview, this);

        // Setup label and get key
        TextView label = (TextView) findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableAutoCompleteTextView);
        int autocompleteValueId = typedArray.getResourceId(R.styleable.SavableAutoCompleteTextView_autocomplete_values, 0);
        mResourceStrings = context.getResources().getStringArray(autocompleteValueId);
        typedArray.recycle();

        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_textview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mResourceStrings);
        mAutocompleteTextView.setAdapter(adapter);
    }

    @Override
    public String writeToMap(ScoutMap map)
    {
        map.put(mKey, mAutocompleteTextView.getText().toString());
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map)
    {
        if(map.contains(mKey))
        {
            try {
                mAutocompleteTextView.setText(map.getString(mKey));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }
        return "";
    }
}
