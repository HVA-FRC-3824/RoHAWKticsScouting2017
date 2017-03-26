package frc3824.rohawkticsscouting2017.Statistics;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 *
 */
public class TeamCalculations {

    private final static String TAG = "TeamCalculations";

    private Team mTeam;
    private Database mDatabase;

    public TeamCalculations(int team_number) {
        mDatabase = Database.getInstance();
        mTeam = mDatabase.getTeam(team_number);
    }

    public TeamCalculations(Team team) {
        mTeam = team;
        mDatabase = Database.getInstance();
    }

    public int numberOfCompletedMatches()
    {
        return mTeam.completed_matches.size();
    }

}
