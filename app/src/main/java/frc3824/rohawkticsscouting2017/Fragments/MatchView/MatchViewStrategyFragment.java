package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 *
 */
public class MatchViewStrategyFragment extends Fragment{

    private final static String TAG = "MatchViewStrategyFragment";
    private View mView;

    public MatchViewStrategyFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_strategy, container, false);
        return mView;
    }

    public void setMatch(int match_number){
        Database database = Database.getInstance();
        Match match = database.getMatch(match_number);

        setMatch(match.teams);
    }

    public void setMatch(ArrayList<Integer> teams){
        Database database = Database.getInstance();

        Team[] blue_alliance = new Team[3];
        Team[] red_alliance = new Team[3];
        for(int i = 0; i < teams.size(); i++){
            if(i < 3){
                blue_alliance[i] = database.getTeam(teams.get(i));
            } else {
                red_alliance[i - 3] = database.getTeam(teams.get(i));
            }
        }
    }

}
