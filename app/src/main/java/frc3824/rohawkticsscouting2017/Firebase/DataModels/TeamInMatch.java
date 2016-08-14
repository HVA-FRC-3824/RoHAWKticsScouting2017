package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author Andrew Messing
 *         Created: 8/13/16
 */
public class TeamInMatch {

    private final static String TAG = "TeamInMatch";

    public int match_number;
    public int team_number;

    // Autonomous

    // Teleop

    // Endgame

    //Post Match
    public boolean no_show;
    public boolean stopped_moving;
    public boolean dq;
    public String notes;

    // Fouls
    public int fouls;
    public int tech_fouls;
    public boolean yellow_card;
    public boolean red_card;

    public TeamInMatch() {}

}
