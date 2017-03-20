package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Statistics.AllianceCalculations;

/**
 * @author frc3824
 * Created: 8/18/16
 *
 *
 */
public class MatchViewPredictionFragment extends Fragment {

    private final static String TAG = "MatchViewPredictionFragment";

    private View mView;
    private ArrayList<Integer> mTeams;

    public void setMatch(int match_number){
        Database database = Database.getInstance();
        Match match = database.getMatch(match_number);
        setMatch(match.team_numbers);
    }

    public void setMatch(ArrayList<Integer> teams){
        mTeams = teams;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_prediction, container, false);
        Database database = Database.getInstance();
        ArrayList<Team> blue_alliance = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            blue_alliance.add(database.getTeam(mTeams.get(i)));
        }
        AllianceCalculations blue_alliance_calculations = new AllianceCalculations(blue_alliance);
        double blue_predicted_score = blue_alliance_calculations.predictedScore();


        ArrayList<Team> red_alliance = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            red_alliance.add(database.getTeam(mTeams.get(i + 3)));
        }
        AllianceCalculations red_alliance_calculations = new AllianceCalculations(red_alliance);
        double red_predicted_score = red_alliance_calculations.predictedScore();

//        double blue_win_probability = 100.0 * blue_alliance_calculations.winProbabilityOver(red_alliance_calculations);
//        double red_win_probability = 100 * red_alliance_calculations.winProbabilityOver(blue_alliance_calculations);

        double blue_pressure_chance = blue_alliance_calculations.pressureChance();
        double red_pressure_chance = red_alliance_calculations.pressureChance();

        double blue_rotor_chance = blue_alliance_calculations.rotorChance();
        double red_rotor_chance = red_alliance_calculations.rotorChance();

        ((TextView)mView.findViewById(R.id.blue_alliance_score)).setText(String.format("%03.2f", blue_predicted_score));
        ((TextView)mView.findViewById(R.id.red_alliance_score)).setText(String.format("%03.2f", red_predicted_score));
//        ((TextView)mView.findViewById(R.id.blue_win_probability)).setText(String.format("%03.2f", blue_win_probability));
//        ((TextView)mView.findViewById(R.id.red_win_probability)).setText(String.format("%03.2f", red_win_probability));
        ((TextView)mView.findViewById(R.id.blue_pressure_probability)).setText(String.format("%03.2f", blue_pressure_chance));
        ((TextView)mView.findViewById(R.id.red_pressure_probability)).setText(String.format("%03.2f", red_pressure_chance));
        ((TextView)mView.findViewById(R.id.blue_rotor_probability)).setText(String.format("%03.2f", blue_rotor_chance));
        ((TextView)mView.findViewById(R.id.red_rotor_probability)).setText(String.format("%03.2f", red_rotor_chance));

        return mView;
    }


}
