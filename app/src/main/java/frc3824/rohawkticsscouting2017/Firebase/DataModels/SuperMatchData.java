package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * Class to store the data collected by the super scout during a match
 */
public class SuperMatchData {

    private final static String TAG = "SuperMatchData";

    public int match_number;
    public String scout_name;
    public long last_modified;

    // Qualitative

    // Speed
    public Map<String, Integer> blue_speed;
    public Map<String, Integer> red_speed;

    // Torque (Pushing Power)
    public Map<String, Integer> blue_torque;
    public Map<String, Integer> red_torque;

    // Control
    public Map<String, Integer> blue_control;
    public Map<String, Integer> red_control;

    // Defense
    public Map<String, Integer> blue_defense;
    public Map<String, Integer> red_defense;

    //Notes
    public String notes;

    public SuperMatchData() {
        blue_speed = new HashMap<>();
        red_speed = new HashMap<>();

        blue_torque = new HashMap<>();
        red_torque = new HashMap<>();

        blue_control = new HashMap<>();
        red_control = new HashMap<>();

        blue_defense = new HashMap<>();
        red_defense = new HashMap<>();
    }

    public SuperMatchData(ScoutMap map) {
        try {
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);
            scout_name = map.getString(Constants.Super_Scouting.SCOUT_NAME);

            blue_speed = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_SPEED);
            blue_torque = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_TORQUE);
            blue_control = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_CONTROL);
            blue_defense = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_DEFENSE);

            red_speed = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_SPEED);
            red_torque = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_TORQUE);
            red_control = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_CONTROL);
            red_defense = (Map<String, Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_DEFENSE);

            notes = map.getString(Constants.Super_Scouting.NOTES);

        } catch (ScoutValue.TypeException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Exclude
    public ScoutMap toMap() {
        ScoutMap map = new ScoutMap();
        
        map.put(Constants.Intent_Extras.MATCH_NUMBER, match_number);
        map.put(Constants.Super_Scouting.SCOUT_NAME, scout_name);

        map.put(Constants.Super_Scouting.Qualitative.BLUE_SPEED, blue_speed);
        map.put(Constants.Super_Scouting.Qualitative.BLUE_TORQUE, blue_torque);
        map.put(Constants.Super_Scouting.Qualitative.BLUE_CONTROL, blue_control);
        map.put(Constants.Super_Scouting.Qualitative.BLUE_DEFENSE, blue_defense);

        map.put(Constants.Super_Scouting.Qualitative.RED_SPEED, red_speed);
        map.put(Constants.Super_Scouting.Qualitative.RED_TORQUE, red_torque);
        map.put(Constants.Super_Scouting.Qualitative.RED_CONTROL, red_control);
        map.put(Constants.Super_Scouting.Qualitative.RED_DEFENSE, red_defense);

        map.put(Constants.Super_Scouting.NOTES, notes);
        
        return map;
    }

}
