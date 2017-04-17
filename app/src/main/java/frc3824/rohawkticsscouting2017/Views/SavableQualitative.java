package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_Qualitative;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Qualitative;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;
import frc3824.rohawkticsscouting2017.Views.DragSortListView.DragSortListView;

/**
 * @author frc3824
 * Created: 8/24/16
 *
 *
 */
public class SavableQualitative extends SavableView {

    private final static String TAG = "SavableQualitative";

    private String mKey;

    private ArrayList<TextView> mTeamLabels;
    private ArrayList<Spinner> mSpinners;
    private ArrayList<String> mTeamNumbers;

    public SavableQualitative(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_qualitative, this);

        // Setup label and get key
        TextView label = (TextView) findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        mTeamLabels = new ArrayList<>();
        mTeamLabels.add((TextView)findViewById(R.id.team1_label));
        mTeamLabels.add((TextView)findViewById(R.id.team2_label));
        mTeamLabels.add((TextView)findViewById(R.id.team3_label));

        mSpinners = new ArrayList<>();
        mSpinners.add((Spinner)findViewById(R.id.team1));
        mSpinners.add((Spinner)findViewById(R.id.team2));
        mSpinners.add((Spinner)findViewById(R.id.team3));

        String values[] = new String[]{"3", "2", "1"};

        for(Spinner s: mSpinners){
            s.setAdapter(new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, values));
        }
    }

    public void setTeams(ArrayList<Integer> teams) {
        mTeamNumbers = new ArrayList<>();
        for(int i = 0; i < teams.size(); i++){
            mTeamLabels.get(i).setText(String.valueOf(teams.get(i)));
            mTeamNumbers.add(String.valueOf(teams.get(i)));
        }
    }

    @Override
    public String writeToMap(ScoutMap map) {
        Map<String, Integer> values = new HashMap<>();
        for(int i = 0; i < mTeamNumbers.size(); i++){
            values.put(mTeamNumbers.get(i), mSpinners.get(i).getSelectedItemPosition() + 1);
        }
        map.put(mKey, values);
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                Map<String, Integer> values = (Map<String, Integer>)map.getObject(mKey);
                for(int i = 0; i < mTeamNumbers.size(); i++){
                    mSpinners.get(i).setSelection(values.get(mTeamNumbers.get(i)) - 1);
                }
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }

        return "";
    }
}
