package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.DisplayIndividualScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class LVA_DisplayScoutAccuracy extends ArrayAdapter<ScoutAccuracy>{

    private final static String TAG = "LVA_DisplayScoutAccuracy";

    ArrayList<ScoutAccuracy> mScoutAccuracies;
    Context mContext;

    public LVA_DisplayScoutAccuracy(Context context, ArrayList<ScoutAccuracy> objects) {
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
            ((TextView) convertView.findViewById(R.id.total_error)).setText(String.valueOf("Total Error"));
            ((TextView) convertView.findViewById(R.id.auto_error)).setText(String.valueOf("Auto Error"));
            ((TextView) convertView.findViewById(R.id.teleop_error)).setText(String.valueOf("Teleop Error"));
            ((TextView) convertView.findViewById(R.id.endgame_error)).setText(String.valueOf("Endgame Error"));
        } else {
            ScoutAccuracy sa = mScoutAccuracies.get(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(sa.name);
            ((TextView) convertView.findViewById(R.id.total_error)).setText(String.valueOf(sa.total_error));
            ((TextView) convertView.findViewById(R.id.auto_error)).setText(String.valueOf(sa.auto_error));
            ((TextView) convertView.findViewById(R.id.teleop_error)).setText(String.valueOf(sa.teleop_error));
            ((TextView) convertView.findViewById(R.id.endgame_error)).setText(String.valueOf(sa.endgame_error));

            final String scout_name = sa.name;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DisplayIndividualScoutAccuracy.class);
                    intent.putExtra(Constants.Intent_Extras.SCOUTER, scout_name);
                    mContext.startActivity(intent);
                }
            });
        }
        return  convertView;
    }
}
