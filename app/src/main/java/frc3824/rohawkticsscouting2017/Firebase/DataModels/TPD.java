package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 *         Created: 8/20/16
 */
public class TPD {

    private final static String TAG = "TPD";

    /*
        Pit Scouting
    */
    public int team_number;

    public boolean pit_scouted;

    // Robot Image
    public String robot_image_filepath;
    public String robot_image_url;

    // Dimensions
    public double weight;
    public double width;
    public double length;
    public double height;

    // Miscellaneous

    public String programming_language;

    // Notes
    public String notes;

    public TPD(){}

    public TPD(ScoutMap map)
    {
        try {
            team_number = map.getInt(Constants.Intent_Extras.TEAM_NUMBER);

            pit_scouted = map.getBoolean(Constants.Pit_Scouting.PIT_SCOUTED);

            robot_image_filepath = map.getString(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);

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

    public ScoutMap toMap()
    {
        ScoutMap map = new ScoutMap();

        map.put(Constants.Intent_Extras.TEAM_NUMBER, team_number);

        map.put(Constants.Pit_Scouting.PIT_SCOUTED, pit_scouted);

        map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH, robot_image_filepath);
        map.put(Constants.Pit_Scouting.ROBOT_PICTURE_URL, robot_image_url);

        map.put(Constants.Pit_Scouting.Dimensions.WIDTH, width);
        map.put(Constants.Pit_Scouting.Dimensions.LENGTH, length);
        map.put(Constants.Pit_Scouting.Dimensions.HEIGHT, height);
        map.put(Constants.Pit_Scouting.Dimensions.WEIGHT, weight);

        map.put(Constants.Pit_Scouting.Miscellaneous.PROGRAMMING_LANGUAGE, programming_language);

        map.put(Constants.Pit_Scouting.NOTES, notes);

        return map;
    }
}
