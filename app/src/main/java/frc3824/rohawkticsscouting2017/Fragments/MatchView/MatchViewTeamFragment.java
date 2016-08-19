package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/18/16
 */
public class MatchViewTeamFragment extends Fragment {

    private final static String TAG = "MatchViewTeamFragment";

    private View mView;

    public MatchViewTeamFragment() {}

    public void setTeam(Team team)
    {
        TextView teamNumber = (TextView)mView.findViewById(R.id.team_number);
        teamNumber.setText(String.valueOf(team.team_number));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_match_view_team, container, false);
        return mView;
    }

}
