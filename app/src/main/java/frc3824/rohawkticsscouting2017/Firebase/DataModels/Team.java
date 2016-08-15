package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

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

    public boolean pit_scouted;

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

    public void pitFromMap(ScoutMap map)
    {
        try {
            team_number = map.getInt(Constants.Intent_Extras.TEAM_NUMBER);

            pit_scouted = map.getBoolean(Constants.Pit_Scouting.PIT_SCOUTED);

            robot_image_filename = map.getString(Constants.Pit_Scouting.ROBOT_PICTURE_FILENAME);

            width = map.getDouble(Constants.Pit_Scouting.Dimensions.WIDTH);
            length = map.getDouble(Constants.Pit_Scouting.Dimensions.LENGTH);
            height = map.getDouble(Constants.Pit_Scouting.Dimensions.HEIGHT);
            weight = map.getDouble(Constants.Pit_Scouting.Dimensions.WEIGHT);

            programming_language = map.getString(Constants.Pit_Scouting.Miscellaneous.PROGRAMMING_LANGUAGE);

            notes = map.getString(Constants.Pit_Scouting.NOTES);

        } catch (ScoutValue.TypeException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public ScoutMap pitToMap()
    {
        ScoutMap map = new ScoutMap();

        map.put(Constants.Intent_Extras.TEAM_NUMBER, team_number);

        map.put(Constants.Pit_Scouting.PIT_SCOUTED, pit_scouted);

        map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILENAME, robot_image_filename);

        map.put(Constants.Pit_Scouting.Dimensions.WIDTH, width);
        map.put(Constants.Pit_Scouting.Dimensions.LENGTH, length);
        map.put(Constants.Pit_Scouting.Dimensions.HEIGHT, height);
        map.put(Constants.Pit_Scouting.Dimensions.WEIGHT, weight);

        map.put(Constants.Pit_Scouting.Miscellaneous.PROGRAMMING_LANGUAGE, programming_language);

        map.put(Constants.Pit_Scouting.NOTES, notes);

        return map;
    }

}
