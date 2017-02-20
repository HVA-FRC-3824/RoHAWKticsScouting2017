package frc3824.rohawkticsscouting2017.Views.Steamworks;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Gear;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;
import frc3824.rohawkticsscouting2017.Views.SavableView;

/**
 * @author frc3824
 * Created: 1/14/17
 */
public class GearsInput extends SavableView implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "Gears";
    private RadioGroup numGears;
    private Spinner mLocation;
    private CheckBox mPlaced;
    private CheckBox mDropped;
    private Button add, edit, delete;
    private String mKey;
    private ArrayList<Gear> mGears;
    private Context mContext;
    private List<String> mLocationList;

    public GearsInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_gears_input, this);

        // Setup label and get key
        TextView label = (TextView)findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        mLocation = (Spinner)findViewById(R.id.location);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, Constants.Match_Scouting.Custom.Gears.LOCATIONS);
        mLocation.setAdapter(arrayAdapter);
        mLocationList = Arrays.asList(Constants.Match_Scouting.Custom.Gears.LOCATIONS);

        mGears = new ArrayList<>();

        numGears = (RadioGroup)findViewById(R.id.radio_group);
        mPlaced = (CheckBox)findViewById(R.id.placed);
        mDropped = (CheckBox)findViewById(R.id.dropped);


        RadioButton radioButton = new RadioButton(context, attrs);
        radioButton.setId(0);
        numGears.addView(radioButton);
        numGears.check(0);

        numGears.setOnCheckedChangeListener(this);


        add = (Button)findViewById(R.id.add_button);
        add.setOnClickListener(this);
        edit = (Button)findViewById(R.id.edit_button);
        edit.setOnClickListener(this);
        delete = (Button)findViewById(R.id.delete_button);
        delete.setOnClickListener(this);

        mContext = context;
    }


    @Override
    public String writeToMap(ScoutMap map) {
        map.put(mKey, mGears);
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                mGears = (ArrayList<Gear>) map.getObject(mKey);
                if(mGears.size() > 0){
                    Gear gear = mGears.get(0);

                    mLocation.setSelection(mLocationList.indexOf(gear.location));
                    mPlaced.setChecked(gear.placed);
                    mDropped.setChecked(gear.dropped);

                    add.setVisibility(GONE);
                    edit.setVisibility(VISIBLE);
                    delete.setVisibility(VISIBLE);
                }
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, "Error: ", e);
                return e.getMessage();
            }
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        int whichGear = numGears.getCheckedRadioButtonId();
        Gear gear;

        switch(v.getId()){
            case R.id.edit_button:
                    gear = mGears.get(whichGear);

                    gear.location = mLocationList.get(mLocation.getSelectedItemPosition());
                    gear.placed = mPlaced.isChecked();
                    gear.dropped = mDropped.isChecked();
                    if(!(gear.placed || gear.dropped) || gear.placed && gear.dropped){
                        return;
                    }
                    mGears.set(whichGear, gear);
                    if(whichGear + 1 < mGears.size()) {
                        gear = mGears.get(whichGear + 1);

                        mLocation.setSelection(mLocationList.indexOf(gear.location));
                        mPlaced.setChecked(gear.placed);
                        mDropped.setChecked(gear.dropped);
                        numGears.check(whichGear + 1);
                    } else {
                        mLocation.setSelection(0);
                        mPlaced.setChecked(false);
                        mDropped.setChecked(false);
                        numGears.check(whichGear + 1);
                    }

                break;
            case R.id.add_button:
                    gear = new Gear();
                    gear.location = mLocationList.get(mLocation.getSelectedItemPosition());
                    gear.placed = mPlaced.isChecked();
                    gear.dropped = mDropped.isChecked();
                    if(!(gear.placed || gear.dropped) || gear.placed && gear.dropped){
                        return;
                    }
                    mGears.add(gear);

                    mLocation.setSelection(0);
                    mPlaced.setChecked(false);
                    mDropped.setChecked(false);
                    RadioButton radioButton = new RadioButton(mContext);
                    radioButton.setId(whichGear + 1);
                    numGears.addView(radioButton);
                    numGears.check(whichGear + 1);
                break;
            case R.id.delete_button:
                int lastRadio = mGears.size();
                mGears.remove(whichGear);
                numGears.removeViewAt(lastRadio);
                numGears.check(whichGear);
                if (whichGear < mGears.size()) {
                    gear = mGears.get(whichGear);

                    mLocation.setSelection(mLocationList.indexOf(gear.location));
                    mPlaced.setChecked(gear.placed);
                    mDropped.setChecked(gear.dropped);
                } else {
                    mLocation.setSelection(0);
                    mPlaced.setChecked(false);
                    mDropped.setChecked(false);
                    add.setVisibility(VISIBLE);
                    edit.setVisibility(GONE);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == mGears.size()){
            mLocation.setSelection(0);
            mPlaced.setChecked(false);
            mDropped.setChecked(false);
            add.setVisibility(VISIBLE);
            edit.setVisibility(GONE);
            delete.setVisibility(GONE);
        } else {
            Gear gear = mGears.get(checkedId);

            mLocation.setSelection(mLocationList.indexOf(gear.location));
            mPlaced.setChecked(gear.placed);
            mDropped.setChecked(gear.dropped);

            add.setVisibility(GONE);
            edit.setVisibility(VISIBLE);
            delete.setVisibility(VISIBLE);
        }
    }
}
