package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 * Fragment that displays averages, totals and aggregated stats of all metrics
 */
public class AllMatchDataFragment extends Fragment {

    private final static String TAG = "AllMatchDataFragment";

    private Team mTeam;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_all_match_data, container, false);

        return view;
    }

    public void setTeam(Team team){
        mTeam = team;
    }
}
