package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Aggregated data for a specific team
 */
@IgnoreExtraProperties
public class TeamCalculatedData {

    private final static String TAG = "TeamCalculatedData";

    /*
        Calculated Data
    */

    public int team_number;

    // Autonomous


    // Teleop

    // Endgame

    // Post Match
    public LowLevelStats no_show;
    public LowLevelStats stopped_moving;
    public LowLevelStats dq;

    // Fouls
    public LowLevelStats fouls;
    public LowLevelStats tech_fouls;
    public LowLevelStats yellow_cards;
    public LowLevelStats red_cards;

    // Qualitative
    public double zscore_speed;
    public int rank_speed;

    public double zscore_torque;
    public int rank_torque;

    public double zscore_control;
    public int rank_control;

    public double zscore_defense;
    public int rank_defense;

    public TeamCalculatedData() {}
}
