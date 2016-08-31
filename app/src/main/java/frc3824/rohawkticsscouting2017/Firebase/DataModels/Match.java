package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
@IgnoreExtraProperties
public class Match {

    private final static String TAG = "Match";

    public int match_number;

    public List<Integer> teams;

    public List<Integer> scores;

    public Match() {}

    @Exclude
    public boolean isBlue(int team_number) throws Exception {
        if(teams.size() != 6)
        {
            throw new Exception("Not enough teams in match");
        }

        for(int i = 0; i < 3; i++)
        {
            if(team_number == teams.get(i))
                return true;
        }
        return false;
    }

    @Exclude
    public boolean isRed(int team_number) throws Exception {
        if(teams.size() != 6)
        {
            throw new Exception("Not enough teams in match");
        }

        for(int i = 3; i < 6; i++)
        {
            if(team_number == teams.get(i))
                return true;
        }
        return false;
    }
}
