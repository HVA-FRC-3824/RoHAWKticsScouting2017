package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutedMatchAccuracy;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class LVA_IndividualScoutAccuracy extends ArrayAdapter<ScoutedMatchAccuracy> {

    private final static String TAG = "LVA_IndividualScoutAccuracy";

    ArrayList<ScoutedMatchAccuracy> mScoutedMatchAccuracies;
    Context mContext;

    public LVA_IndividualScoutAccuracy(Context context, ArrayList<ScoutedMatchAccuracy> objects) {
        super(context, R.layout.list_item_scouted_match_accuracy, objects);
        mScoutedMatchAccuracies = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_scouted_match_accuracy, null);
        }

        if(position == 0){
            ((TextView) convertView.findViewById(R.id.match_number)).setText("Match Number");
            ((TextView) convertView.findViewById(R.id.alliance)).setText("Alliance");
            ((TextView) convertView.findViewById(R.id.auto_mobility_error)).setText("Auto Mobility Error");
            ((TextView) convertView.findViewById(R.id.auto_gear_error)).setText("Auto Rotor Error");
            ((TextView) convertView.findViewById(R.id.teleop_gear_error)).setText("Teleop Rotor Error");
            ((TextView) convertView.findViewById(R.id.climb_error)).setText("Endgame Error");
        } else {
            ScoutedMatchAccuracy sma = mScoutedMatchAccuracies.get(position);

            ((TextView) convertView.findViewById(R.id.match_number)).setText(String.valueOf(sma.match_number));
            ((TextView)convertView.findViewById(R.id.alliance)).setText(String.format("%s %d", sma.alliance_color, sma.alliance_number));
            ((TextView) convertView.findViewById(R.id.auto_mobility_error)).setText(String.valueOf(sma.auto_mobility_error));
            ((TextView) convertView.findViewById(R.id.auto_gear_error)).setText(String.valueOf(sma.auto_gear_error));
            ((TextView) convertView.findViewById(R.id.teleop_gear_error)).setText(String.valueOf(sma.teleop_gear_error));
            ((TextView) convertView.findViewById(R.id.climb_error)).setText(String.valueOf(sma.climb_error));
        }
        return  convertView;
    }
}
