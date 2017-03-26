package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 *
 */
public class MatchViewAllianceFragment extends Fragment{

    private final static String TAG = "MatchViewAllianceFragment";
    private View mView;
    private boolean mBlue;
    private ArrayList<Integer> mTeams;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_alliance, container, false);
        FragmentManager fm = getChildFragmentManager();

        MatchViewTeamFragment team1 = (MatchViewTeamFragment)fm.findFragmentById(R.id.team1);
        MatchViewTeamFragment team2 = (MatchViewTeamFragment)fm.findFragmentById(R.id.team2);
        MatchViewTeamFragment team3 = (MatchViewTeamFragment)fm.findFragmentById(R.id.team3);
        if(mBlue){
            mView.setBackgroundColor(Color.BLUE);
            team1.setTeam(mTeams.get(0));
            team2.setTeam(mTeams.get(1));
            team3.setTeam(mTeams.get(2));
        } else {
            mView.setBackgroundColor(Color.RED);
            team1.setTeam(mTeams.get(3));
            team2.setTeam(mTeams.get(4));
            team3.setTeam(mTeams.get(5));
        }
        return mView;
    }

    public void setMatchAlliance(int match_number, boolean blue){
        Database database = Database.getInstance();
        Match match = database.getMatch(match_number);
        setMatchAlliance(match.team_numbers, blue);
    }

    public void setMatchAlliance(ArrayList<Integer> teams, boolean blue){
        mTeams = teams;
        mBlue = blue;
    }
}
