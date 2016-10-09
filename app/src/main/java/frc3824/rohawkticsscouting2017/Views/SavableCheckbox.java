package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Savable Checkbox that has a label attached. Clicking anywhere in the field modifies the checkbox.
 */
public class SavableCheckbox extends SavableView implements View.OnClickListener{

    private final static String TAG = "CustomCheckbox";

    private CheckBox mCheckbox;
    private String mKey;

    public SavableCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_checkbox, this);

        mCheckbox = (CheckBox)findViewById(R.id.checkbox);

        // Set label and get key
        TextView label = (TextView)findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        // Clicking anywhere on the widget will affect the checkbox
        setOnClickListener(this);
    }

    /**
     * Clicking anywhere in the view modifies the checkbox
     *
     * @param view this view
     */
    @Override
    public void onClick(View view) {
        mCheckbox.setChecked(!mCheckbox.isChecked());
    }

    public boolean isChecked() { return mCheckbox.isChecked(); }

    public void setChecked(boolean checked) { mCheckbox.setChecked(checked); }

    /**
     * Saves the checkbox value to the map
     *
     * @param map the map that the value is saved to
     * @return any error messages
     */
    @Override
    public String writeToMap(ScoutMap map) {
        map.put(mKey, mCheckbox.isChecked());
        return "";
    }

    /**
     * Restores the checkbox values based on the map
     *
     * @param map the map that the checkbox value is restored from
     * @return any error messages
     */
    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                setChecked(map.getBoolean(mKey));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }
        return "";
    }
}
