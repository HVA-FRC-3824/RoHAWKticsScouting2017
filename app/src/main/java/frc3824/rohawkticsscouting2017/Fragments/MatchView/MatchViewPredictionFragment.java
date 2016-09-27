package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Alliance;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Statistics.AllianceCalculations;

/**
 * @author Andrew Messing
 *         Created: 8/18/16
 */
public class MatchViewPredictionFragment extends Fragment {

    private final static String TAG = "MatchViewPredictionFragment";

    private View mView;
    private Team mTeam1, mTeam2, mTeam3;
    private Alliance mAlliance;
    private AllianceCalculations mAC;

    public void setTeams(Team team1, Team team2, Team team3) {
        setTeams(team1, team2, team3, false);
    }

    public void setTeams(Team team1, Team team2, Team team3, boolean eliminations) {
        mTeam1 = team1;
        mTeam2 = team2;
        mTeam3 = team3;

        TextView alliancePredictedScore = (TextView)mView.findViewById(R.id.alliance_score);

        mAlliance = new Alliance();
        mAlliance.teams.add(mTeam1);
        mAlliance.teams.add(mTeam2);
        mAlliance.teams.add(mTeam3);

        mAC = new AllianceCalculations(mAlliance);
        alliancePredictedScore.setText(String.format("%d", Math.round(mAC.predictedScore())));
    }

    public void setOpponents(Team opp1, Team opp2, Team opp3) {
        TextView winProbability = (TextView)mView.findViewById(R.id.win_probability);
        Alliance alliance = new Alliance();
        alliance.teams.add(opp1);
        alliance.teams.add(opp2);
        alliance.teams.add(opp3);
        winProbability.setText(String.format("%0.2lf",mAC.winProbabilityOver(alliance)));
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_prediction, container, false);
        return mView;
    }


}
