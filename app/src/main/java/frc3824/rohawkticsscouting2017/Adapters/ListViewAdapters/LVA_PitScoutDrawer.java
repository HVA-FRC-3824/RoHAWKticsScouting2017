package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.TeamNumberCheck;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 10/9/16
 *
 *
 */
public class LVA_PitScoutDrawer extends ArrayAdapter<TeamNumberCheck> {

    private final static String TAG = "LVA_PitScoutDrawer";
    private ArrayList<TeamNumberCheck> mTeamNumbers;
    private Context mContext;

    public LVA_PitScoutDrawer(Context context, ArrayList<TeamNumberCheck> objects) {
        super(context, R.layout.list_item_pit_scout_drawer, objects);
        mTeamNumbers = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_pit_scout_drawer, null);
        }

        TeamNumberCheck tnc = mTeamNumbers.get(position);

        TextView team_number = (TextView)convertView.findViewById(R.id.team_number);
        team_number.setText(String.format("%d", tnc.team_number));

        if(tnc.check){
            convertView.setBackgroundColor(Color.GREEN);
        } else {
            convertView.setBackgroundColor(Color.RED);
        }

        return convertView;
    }
}
