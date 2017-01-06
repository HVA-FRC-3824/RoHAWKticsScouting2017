package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 10/9/16
 *
 *
 */
public class LVA_PitScoutDrawer extends ArrayAdapter<Integer> {

    private final static String TAG = "LVA_PitScoutDrawer";
    private ArrayList<Integer> mTeamNumbers;
    private Context mContext;
    private Database mDatabase;

    public LVA_PitScoutDrawer(Context context, ArrayList<Integer> objects) {
        super(context, R.layout.list_item_pit_scout_drawer, objects);
        mTeamNumbers = objects;
        mContext = context;
        mDatabase = Database.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_pit_scout_drawer, null);
        }

        int team_number = mTeamNumbers.get(position);

        TextView tnView = (TextView)convertView.findViewById(R.id.team_number);
        tnView.setText(String.format("%d", team_number));

        TeamPitData teamPitData = mDatabase.getTeamPitData(team_number);

        if(teamPitData != null){
            convertView.setBackgroundColor(Color.GREEN);

            if(teamPitData.robot_image_filepaths.size() != 0){
                convertView.findViewById(R.id.image).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.image).setVisibility(View.GONE);
            }
        } else {
            convertView.setBackgroundColor(Color.RED);
            convertView.findViewById(R.id.image).setVisibility(View.GONE);
        }

        return convertView;
    }
}
