package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
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
    private boolean all;
    private EventView mEv;

    public LVA_EventViewDrawer(Context context, ArrayList<TeamNumberCheck> objects, EventView ev) {
        super(context, R.layout.list_item_event_view_drawer, objects);
        mTeamNumbers = objects;
        mContext = context;
        mEv = ev;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_event_view_drawer, null);
        }

        if(position == 0){
            final TeamNumberCheck team_number = mTeamNumbers.get(position);

            ((TextView) convertView.findViewById(R.id.team_number)).setText("All");

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            checkBox.setChecked(team_number.check);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    all = b;
                    if(b) {
                        for (int i = 0; i < mTeamNumbers.size(); i++) {
                            mTeamNumbers.get(i).check = true;
                        }
                    } else {
                        mTeamNumbers.get(0).check = false;
                    }
                    notifyDataSetChanged();
                    mEv.updateChart();
                }
            });
        } else {

            final TeamNumberCheck team_number = mTeamNumbers.get(position);

            ((TextView) convertView.findViewById(R.id.team_number)).setText(String.valueOf(team_number.team_number));

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            if(all){
                checkBox.setChecked(true);
                checkBox.setEnabled(false);
            } else {
                checkBox.setChecked(team_number.check);
                checkBox.setEnabled(true);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        team_number.check = b;
                        mEv.updateChart();
                    }
                });
            }
        }

        return convertView;
    }

    public ArrayList<Integer> getChecked(){
        ArrayList<Integer> team_numbers = new ArrayList<>();
        for(int i = 1; i < mTeamNumbers.size(); i++){
            TeamNumberCheck tnc = mTeamNumbers.get(i);
            if(tnc.check) {
                team_numbers.add(tnc.team_number);
            }
        }
        return team_numbers;
    }
}
