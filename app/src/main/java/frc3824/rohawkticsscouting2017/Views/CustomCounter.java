package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Savable Counter that has a label attached. Clicking anywhere in the field increments the counter.
 * Long clicks decrement the counter.
 */
public class CustomCounter extends CustomScoutView implements View.OnClickListener, View.OnLongClickListener{

    private final static String TAG = "CustomCounter";

    private TextView mCounter;
    private int mCount;
    private String mKey;

    public CustomCounter(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.custom_counter, this);

        // Setup label and get key
        TextView label = (TextView)findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomScoutView);
        label.setText(typedArray.getString(R.styleable.CustomScoutView_label));
        mKey = typedArray.getString(R.styleable.CustomScoutView_key);
        typedArray.recycle();

        // Setup counter
        mCount = 0;

        mCounter = (TextView) findViewById(R.id.counter);
        mCounter.setText(Integer.toString(mCount));

        setOnClickListener(this);
        setOnLongClickListener(this);

    }

    @Override
    public void onClick(View view) {
        mCount ++;
        mCounter.setText(Integer.toString(mCount));
    }

    @Override
    public boolean onLongClick(View view) {
        if(mCount > 0)
        {
            mCount --;
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        mCounter.setText(Integer.toString(mCount));
        return true;
    }

    public int getCount() { return mCount; }

    public void setCount(int count) {
        mCount = count;
        mCounter.setText(Integer.toString(mCount));
    }

    @Override
    public String writeToMap(ScoutMap map)
    {
        map.put(mKey, mCount);
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map)
    {
        if(map.contains(mKey))
        {
            try {
                mCount = map.getInt(mKey);
                mCounter.setText(Integer.toString(mCount));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }

        return "";
    }
}
