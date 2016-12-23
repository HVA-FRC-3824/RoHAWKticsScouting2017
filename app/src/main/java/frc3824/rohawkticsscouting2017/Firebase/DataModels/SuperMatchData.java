package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

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

    // Qualitative

    // Speed
    public ArrayList<Integer> blue_speed;
    public ArrayList<Integer> red_speed;

    // Torque (Pushing Power)
    public ArrayList<Integer> blue_torque;
    public ArrayList<Integer> red_torque;

    // Control
    public ArrayList<Integer> blue_control;
    public ArrayList<Integer> red_control;

    // Defense
    public ArrayList<Integer> blue_defense;
    public ArrayList<Integer> red_defense;

    //Notes
    public String notes;

    public SuperMatchData() {}

    public SuperMatchData(ScoutMap map) {
        try {
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);
            scout_name = map.getString(Constants.Super_Scouting.SCOUT_NAME);

            blue_speed = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_SPEED);
            blue_torque = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_TORQUE);
            blue_control = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_CONTROL);
            blue_defense = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_DEFENSE);

            red_speed = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_SPEED);
            red_torque = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_TORQUE);
            red_control = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_CONTROL);
            red_defense = (ArrayList<Integer>)map.getObject(Constants.Super_Scouting.Qualitative.RED_DEFENSE);

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
