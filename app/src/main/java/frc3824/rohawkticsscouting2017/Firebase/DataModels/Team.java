package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
public class Team {

    private final static String TAG = "Team";

    public int team_number;
    public String nickname;

    public ArrayList<Integer> match_numbers;

    /*
        Pit Scouting
    */

    // Robot Image
    public String robot_image_filename;

    // Dimensions
    public double weight;
    public double width;
    public double length;
    public double height;

    // Miscellaneous

    public String programming_language;

    // Notes
    public String notes;


    /*
        Calculated Data
    */

    // Autonomous


    // Teleop

    // Endgame

    // Post Match
    public LowLevelStats no_show;
    public LowLevelStats stopped_moving;
    public LowLevelStats dq;
    Map<Integer, String> match_notes;
    Map<Integer, String> super_notes;

    // Fouls
    public LowLevelStats fouls;
    public LowLevelStats tech_fouls;
    public LowLevelStats yellow_cards;
    public LowLevelStats red_cards;

    public Team() {}

}
