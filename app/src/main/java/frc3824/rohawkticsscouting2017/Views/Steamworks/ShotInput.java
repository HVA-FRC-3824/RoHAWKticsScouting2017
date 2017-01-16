package frc3824.rohawkticsscouting2017.Views.Steamworks;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
    private int mHopperCount;
    private EditText mHighGoalMade;
    private EditText mHighGoalMissed;
    private EditText mHopper;
    private EditText mLowGoalMade;
    private EditText mLowGoalMissed;

    public ShotInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        mShots = new Shots();
        mHopperCount = 0;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_shots_input, this);


        // Setup label and get key
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        mHopper = (EditText)findViewById(R.id.hopper);
        mHighGoalMade = (EditText)findViewById(R.id.high_goal_made);
        mHighGoalMissed = (EditText)findViewById(R.id.high_goal_missed);
        mLowGoalMade = (EditText)findViewById(R.id.low_goal_made);
        mLowGoalMissed = (EditText)findViewById(R.id.low_goal_missed);

        findViewById(R.id.high_goal_made_increment_1).setOnClickListener(this);
        findViewById(R.id.high_goal_made_increment_5).setOnClickListener(this);
        findViewById(R.id.high_goal_made_increment_10).setOnClickListener(this);
        findViewById(R.id.high_goal_missed_increment_1).setOnClickListener(this);
        findViewById(R.id.high_goal_missed_increment_5).setOnClickListener(this);
        findViewById(R.id.high_goal_missed_increment_10).setOnClickListener(this);
        findViewById(R.id.hopper_increment_1).setOnClickListener(this);
        findViewById(R.id.hopper_increment_5).setOnClickListener(this);
        findViewById(R.id.hopper_increment_10).setOnClickListener(this);
        findViewById(R.id.hopper_increment_50).setOnClickListener(this);
        findViewById(R.id.low_goal_dump).setOnClickListener(this);
        findViewById(R.id.low_goal_missed_increment_1).setOnClickListener(this);
        findViewById(R.id.low_goal_missed_increment_5).setOnClickListener(this);
        findViewById(R.id.low_goal_missed_increment_10).setOnClickListener(this);
    }


    @Override
    public String writeToMap(ScoutMap map) {
        map.put(mKey, mShots);
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                mShots = (Shots) map.getObject(mKey);
                mHighGoalMade.setText(String.valueOf(mShots.high_goal_made));
                mHighGoalMissed.setText(String.valueOf(mShots.high_goal_missed));
                mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
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
            case R.id.high_goal_made_increment_1:
                if(mHopperCount > 0){
                    mShots.high_goal_made++;
                    mHighGoalMade.setText(String.valueOf(mShots.high_goal_made));
                    mHopperCount--;
                    mHopper.setText(String.valueOf(mHopperCount));
                }
                break;
            case R.id.high_goal_made_increment_5:
                if(mHopperCount > 4){
                    mShots.high_goal_made+=5;
                    mHighGoalMade.setText(String.valueOf(mShots.high_goal_made));
                    mHopperCount-=5;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else {
                    mShots.high_goal_made+=mHopperCount;
                    mHighGoalMade.setText(String.valueOf(mShots.high_goal_made));
                    mHopperCount = 0;
                    mHopper.setText(String.valueOf(mHopperCount));
                }
                break;
            case R.id.high_goal_made_increment_10:
                if(mHopperCount > 9){
                    mShots.high_goal_made+=10;
                    mHighGoalMade.setText(String.valueOf(mShots.high_goal_made));
                    mHopperCount-=10;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else {
                    mShots.high_goal_made+=mHopperCount;
                    mHighGoalMade.setText(String.valueOf(mShots.high_goal_made));
                    mHopperCount = 0;
                    mHopper.setText(String.valueOf(mHopperCount));
                }
                break;
            case R.id.high_goal_missed_increment_1:
                if(mHopperCount > 0){
                    mShots.high_goal_missed++;
                    mHighGoalMissed.setText(String.valueOf(mShots.high_goal_missed));
                    mHopperCount--;
                    mHopper.setText(String.valueOf(mHopperCount));
                }
                break;
            case R.id.high_goal_missed_increment_5:
                if(mHopperCount > 4){
                    mShots.high_goal_missed+=5;
                    mHighGoalMissed.setText(String.valueOf(mShots.high_goal_missed));
                    mHopperCount-=5;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else {
                    mShots.high_goal_missed+=mHopperCount;
                    mHighGoalMissed.setText(String.valueOf(mShots.high_goal_missed));
                    mHopperCount = 0;
                    mHopper.setText(String.valueOf(mHopperCount));
                }
                break;
            case R.id.high_goal_missed_increment_10:
                if(mHopperCount > 9){
                    mShots.high_goal_missed+=10;
                    mHighGoalMissed.setText(String.valueOf(mShots.high_goal_missed));
                    mHopperCount-=10;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else {
                    mShots.high_goal_missed+=mHopperCount;
                    mHighGoalMissed.setText(String.valueOf(mShots.high_goal_missed));
                    mHopperCount = 0;
                    mHopper.setText(String.valueOf(mHopperCount));
                }
                break;
            case R.id.hopper_increment_1:
                mHopperCount++;
                mHopper.setText(String.valueOf(mHopperCount));
                break;
            case R.id.hopper_increment_5:
                mHopperCount+=5;
                mHopper.setText(String.valueOf(mHopperCount));
                break;
            case R.id.hopper_increment_10:
                mHopperCount+=10;
                mHopper.setText(String.valueOf(mHopperCount));
                break;
            case R.id.hopper_increment_50:
                mHopperCount+=50;
                mHopper.setText(String.valueOf(mHopperCount));
                break;
            case R.id.low_goal_dump:
                mShots.low_goal_made += mHopperCount;
                mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                mHopperCount = 0;
                mHopper.setText(String.valueOf(mHopperCount));
                break;
            case R.id.low_goal_missed_increment_1:
                if(mHopperCount > 0) {
                    mHopperCount--;
                    mShots.low_goal_missed ++;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mHopper.setText(String.valueOf(mHopperCount));
                } else if(mShots.low_goal_made > 0){
                    mShots.low_goal_made--;
                    mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                    mShots.low_goal_missed++;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                }
                break;
            case R.id.low_goal_missed_increment_5:
                if(mHopperCount > 4) {
                    mShots.low_goal_missed += 5;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mHopperCount -= 5;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else if(mHopperCount > 0) {
                    mShots.low_goal_missed += mHopperCount;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mHopperCount = 0;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else if(mShots.low_goal_made > 4){
                    mShots.low_goal_made -= 5;
                    mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                    mShots.low_goal_missed += 5;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                } else {
                    mShots.low_goal_missed += mShots.low_goal_made;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mShots.low_goal_made = 0;
                    mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                }
                break;
            case R.id.low_goal_missed_increment_10:
                if(mHopperCount > 9) {
                    mShots.low_goal_missed += 10;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mHopperCount -= 10;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else if(mHopperCount > 0) {
                    mShots.low_goal_missed += mHopperCount;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mHopperCount = 0;
                    mHopper.setText(String.valueOf(mHopperCount));
                } else if(mShots.low_goal_made > 9){
                    mShots.low_goal_made -= 10;
                    mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                    mShots.low_goal_missed += 10;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                } else {
                    mShots.low_goal_missed += mShots.low_goal_made;
                    mLowGoalMissed.setText(String.valueOf(mShots.low_goal_missed));
                    mShots.low_goal_made = 0;
                    mLowGoalMade.setText(String.valueOf(mShots.low_goal_made));
                }
                break;
        }
    }
}
