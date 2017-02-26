package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Data for FRC rankings for a specific team
 */
@IgnoreExtraProperties
public class TeamRankingData {

    private final static String TAG = "TeamRankingData";

    public int team_number;
    public long last_modified;

    // Ranking Data
    public int rank;
    public int RPs;
    public int wins;
    public int ties;
    public int losses;
    public int played;

    // Game Specific
    public int auto;
    public int scale_challenge;
    public int goals;
    public int defenses;

    public TeamRankingData(){}

}
