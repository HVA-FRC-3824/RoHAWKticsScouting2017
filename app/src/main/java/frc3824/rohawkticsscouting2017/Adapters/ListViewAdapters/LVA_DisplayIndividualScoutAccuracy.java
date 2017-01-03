package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutedMatchAccuracy;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class LVA_DisplayIndividualScoutAccuracy extends ArrayAdapter<ScoutedMatchAccuracy> {

    private final static String TAG = "LVA_DisplayIndividualScoutAccuracy";

    ArrayList<ScoutedMatchAccuracy> mScoutedMatchAccuracies;
    Context mContext;

    public LVA_DisplayIndividualScoutAccuracy(Context context, ArrayList<ScoutedMatchAccuracy> objects) {
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
            ((TextView) convertView.findViewById(R.id.match_list)).setText("Match Number");
            ((TextView)convertView.findViewById(R.id.alliance)).setText("Alliance");
            ((TextView) convertView.findViewById(R.id.total_error)).setText(String.valueOf("Total Error"));
            ((TextView) convertView.findViewById(R.id.auto_error)).setText(String.valueOf("Auto Error"));
            ((TextView) convertView.findViewById(R.id.teleop_error)).setText(String.valueOf("Teleop Error"));
            ((TextView) convertView.findViewById(R.id.endgame_error)).setText(String.valueOf("Endgame Error"));
        } else {
            ScoutedMatchAccuracy sma = mScoutedMatchAccuracies.get(position);

            ((TextView) convertView.findViewById(R.id.match_list)).setText(sma.match_number);
            ((TextView)convertView.findViewById(R.id.alliance)).setText(String.format("%s %d", sma.alliance_color, sma.alliance_number));
            ((TextView) convertView.findViewById(R.id.total_error)).setText(String.valueOf(sma.total_error));
            ((TextView) convertView.findViewById(R.id.auto_error)).setText(String.valueOf(sma.auto_error));
            ((TextView) convertView.findViewById(R.id.teleop_error)).setText(String.valueOf(sma.teleop_error));
            ((TextView) convertView.findViewById(R.id.endgame_error)).setText(String.valueOf(sma.endgame_error));
        }
        return  convertView;
    }
}
