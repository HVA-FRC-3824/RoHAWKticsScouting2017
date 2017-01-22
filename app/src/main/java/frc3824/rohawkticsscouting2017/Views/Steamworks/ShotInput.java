package frc3824.rohawkticsscouting2017.Views.Steamworks;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Shots;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;
import frc3824.rohawkticsscouting2017.Views.SavableView;

/**
 * @author frc3824
 * Created: 1/9/17
 */
public class ShotInput extends SavableView implements View.OnClickListener {

    private final static String TAG = "ShotInput";
    private String mKey;
    private Shots mShots;
    private TextView mMade;
    private TextView mMissed;

    public ShotInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        mShots = new Shots();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_shots_input, this);


        // Setup label and get key
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        ((TextView)findViewById(R.id.label)).setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        mMade = (TextView) findViewById(R.id.made);
        mMissed = (TextView) findViewById(R.id.missed);

        findViewById(R.id.made_increment_1).setOnClickListener(this);
        findViewById(R.id.made_increment_5).setOnClickListener(this);
        findViewById(R.id.made_increment_10).setOnClickListener(this);
        findViewById(R.id.made_decrement_1).setOnClickListener(this);
        findViewById(R.id.made_decrement_5).setOnClickListener(this);
        findViewById(R.id.made_decrement_10).setOnClickListener(this);
        findViewById(R.id.missed_increment_1).setOnClickListener(this);
        findViewById(R.id.missed_increment_5).setOnClickListener(this);
        findViewById(R.id.missed_increment_10).setOnClickListener(this);
        findViewById(R.id.missed_decrement_1).setOnClickListener(this);
        findViewById(R.id.missed_decrement_5).setOnClickListener(this);
        findViewById(R.id.missed_decrement_10).setOnClickListener(this);
    }


    @Override
    public String writeToMap(ScoutMap map) {
        map.put(mKey + "_made", mShots.made);
        map.put(mKey + "_missed", mShots.missed);
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                mShots.made = map.getInt(mKey + "_made");
                mShots.missed = map.getInt(mKey + "_missed");
                mMade.setText(String.valueOf(mShots.made));
                mMissed.setText(String.valueOf(mShots.missed));
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, "Error: ", e);
                return e.getMessage();
            }
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.made_decrement_10:
                mShots.made -= 10;
                if(mShots.made < 0){
                    mShots.made = 0;
                }
                mMade.setText(String.valueOf(mShots.made));
                break;
            case R.id.made_decrement_5:
                mShots.made -= 5;
                if(mShots.made < 0){
                    mShots.made = 0;
                }
                mMade.setText(String.valueOf(mShots.made));
                break;
            case R.id.made_decrement_1:
                mShots.made --;
                if(mShots.made < 0){
                    mShots.made = 0;
                }
                mMade.setText(String.valueOf(mShots.made));
                break;
            case R.id.made_increment_10:
                mShots.made += 10;
                mMade.setText(String.valueOf(mShots.made));
                break;
            case R.id.made_increment_5:
                mShots.made += 5;
                mMade.setText(String.valueOf(mShots.made));
                break;
            case R.id.made_increment_1:
                mShots.made ++;
                mMade.setText(String.valueOf(mShots.made));
                break;
            case R.id.missed_decrement_10:
                mShots.missed -= 10;
                if(mShots.missed < 0){
                    mShots.missed = 0;
                }
                mMissed.setText(String.valueOf(mShots.missed));
                break;
            case R.id.missed_decrement_5:
                mShots.missed -= 5;
                if(mShots.missed < 0){
                    mShots.missed = 0;
                }
                mMissed.setText(String.valueOf(mShots.missed));
                break;
            case R.id.missed_decrement_1:
                mShots.missed --;
                if(mShots.missed < 0){
                    mShots.missed = 0;
                }
                mMissed.setText(String.valueOf(mShots.missed));
                break;
            case R.id.missed_increment_10:
                mShots.missed += 10;
                mMissed.setText(String.valueOf(mShots.missed));
                break;
            case R.id.missed_increment_5:
                mShots.missed += 5;
                mMissed.setText(String.valueOf(mShots.missed));
                break;
            case R.id.missed_increment_1:
                mShots.missed ++;
                mMissed.setText(String.valueOf(mShots.missed));
                break;

        }
    }
}
