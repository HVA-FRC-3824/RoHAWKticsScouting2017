package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author Andrew Messing
 * Created: 8/20/16
 *
 * Team Calculated Data
 */
public class TCD {

    private final static String TAG = "TCD";

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
}
