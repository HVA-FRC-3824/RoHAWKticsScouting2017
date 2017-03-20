package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * Class to store the logistical information about a match (teams, scores)
 */
@IgnoreExtraProperties
public class Match {

    private final static String TAG = "Match";

    public int match_number;
    public long last_modified;

    public ArrayList<Integer> team_numbers;

    public ArrayList<Integer> predicted_scores;
    public ArrayList<Integer> predicted_auto;
    public ArrayList<Integer> predicted_kpa_rp;
    public ArrayList<Integer> predicted_rotor_rp;

    public Match() {
        predicted_scores = new ArrayList<>();
        predicted_auto = new ArrayList<>();
        predicted_kpa_rp = new ArrayList<>();
        predicted_rotor_rp = new ArrayList<>();
    }

    @Exclude
    public boolean isBlue(int team_number) throws Exception {
        if(team_numbers.size() != 6)
        {
            throw new Exception("Not enough teams in match");
        }

        for(int i = 0; i < 3; i++)
        {
            if(team_number == team_numbers.get(i))
                return true;
        }
        return false;
    }

    @Exclude
    public boolean isRed(int team_number) throws Exception {
        if(team_numbers.size() != 6)
        {
            throw new Exception("Not enough teams in match");
        }

        for(int i = 3; i < 6; i++)
        {
            if(team_number == team_numbers.get(i))
                return true;
        }
        return false;
    }
}
