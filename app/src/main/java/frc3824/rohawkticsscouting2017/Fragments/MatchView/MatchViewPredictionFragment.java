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
 *         Created: 8/18/16
 */
public class MatchViewPredictionFragment extends Fragment {

    private final static String TAG = "MatchViewPredictionFragment";

    private View mView;
    private Team mTeam1, mTeam2, mTeam3;

    public void setTeams(Team team1, Team team2, Team team3)
    {
        mTeam1 = team1;
        mTeam2 = team2;
        mTeam3 = team3;

        TextView winProbability = (TextView)mView.findViewById(R.id.win_probability);

        TextView alliancePredictedScore = (TextView)mView.findViewById(R.id.alliance_score);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_match_view_prediction, container, false);

        return mView;
    }


}
