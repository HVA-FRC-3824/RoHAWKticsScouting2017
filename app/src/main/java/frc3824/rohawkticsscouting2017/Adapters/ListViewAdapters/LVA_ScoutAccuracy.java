package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class LVA_ScoutAccuracy extends ArrayAdapter<ScoutAccuracy>{

    private final static String TAG = "LVA_ScoutAccuracy";

    ArrayList<ScoutAccuracy> mScoutAccuracies;
    Context mContext;

    public LVA_ScoutAccuracy(Context context, ArrayList<ScoutAccuracy> objects) {
        super(context, R.layout.list_item_scout_accuracy, objects);
        mScoutAccuracies = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_scout_accuracy, null);
        }

        if(position == 0){
            ((TextView) convertView.findViewById(R.id.name)).setText("Scout Name");
            ((TextView) convertView.findViewById(R.id.auto_mobility_error)).setText("Auto Mobility Error");
            ((TextView) convertView.findViewById(R.id.auto_gear_error)).setText("Auto Gear Error");
            ((TextView) convertView.findViewById(R.id.teleop_gear_error)).setText("Teleop Gear Error");
            ((TextView) convertView.findViewById(R.id.climb_error)).setText(String.valueOf("CLimb Error"));
        } else {
            ScoutAccuracy sa = mScoutAccuracies.get(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(sa.name);
            ((TextView) convertView.findViewById(R.id.auto_mobility_error)).setText(String.valueOf(sa.auto_mobility_error));
            ((TextView) convertView.findViewById(R.id.auto_gear_error)).setText(String.valueOf(sa.auto_gear_error));
            ((TextView) convertView.findViewById(R.id.teleop_gear_error)).setText(String.valueOf(sa.teleop_gear_error));
            ((TextView) convertView.findViewById(R.id.climb_error)).setText(String.valueOf(sa.climb_error));
        }
        return  convertView;
    }
}
