package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.EventView;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.TeamNumberCheck;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 10/9/16
 *
 *
 */
public class LVA_EventViewDrawer extends ArrayAdapter<TeamNumberCheck> {

    private final static String TAG = "LVA_EventViewDrawer";

    private Context mContext;
    private ArrayList<TeamNumberCheck> mTeamNumbers;
    private EventView mEventView;
    private boolean mEnabled;

    public LVA_EventViewDrawer(Context context, ArrayList<TeamNumberCheck> objects, EventView eventView) {
        super(context, R.layout.list_item_event_view_drawer, objects);
        mTeamNumbers = objects;
        mContext = context;
        mEventView = eventView;
        mEnabled = true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_event_view_drawer, null);
        }

        final TeamNumberCheck team_number = mTeamNumbers.get(position);

        String label = "";
        switch (position){
            case 0:
                label = "All";
                break;
            case 1:
                label = "Top 5";
                break;
            case 2:
                label = "Top 10";
                break;
            case 3:
                label = "Top 24";
                break;
            default:
                label = String.valueOf(team_number.team_number);
        }
        ((TextView) convertView.findViewById(R.id.team_number)).setText(label);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

        checkBox.setChecked(team_number.check);

        if(position < 4){
            checkBox.setEnabled(true);
        } else {
            checkBox.setEnabled(mEnabled);
        }

        return convertView;
    }

    public void setEnabled(boolean enabled){
        mEnabled = enabled;
    }

    public boolean getEnabled(){
        return mEnabled;
    }


}
