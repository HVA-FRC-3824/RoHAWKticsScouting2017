package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Data for FRC rankings for a specific team_number
 */
@IgnoreExtraProperties
public class TeamRankingData {

    private final static String TAG = "TeamRankingData";

    public int team_number;

    // Ranking Data
    public int rank;
    public int RPs;
    public int wins;
    public int ties;
    public int losses;
    public int played;

    public int first_tie_breaker;
    public int second_tie_breaker;

    public TeamRankingData(){}

}
