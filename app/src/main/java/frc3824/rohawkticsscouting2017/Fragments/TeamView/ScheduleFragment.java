package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_ScheduleFragment;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/17/16
 */
public class ScheduleFragment extends Fragment {

    private final static String TAG = "ScheduleFragment";

    private int mTeamNumber;

    public ScheduleFragment(){}


    public void setTeamNumber(int teamNumber)
    {
        mTeamNumber = teamNumber;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_schedule, container, false);

        ListView schedule = (ListView)view.findViewById(R.id.schedule);

        Database database = Database.getInstance();
        TeamLogistics info = database.getTeamLogistics(mTeamNumber);

        LVA_ScheduleFragment lva = new LVA_ScheduleFragment(getContext(), info.match_numbers, mTeamNumber);

        schedule.setAdapter(lva);

        return view;
    }

}
