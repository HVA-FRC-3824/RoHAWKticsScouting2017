package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
public class SuperMatch {

    private final static String TAG = "SuperMatch";

    public int match_number;

    // Qualitative

    // Speed
    public int blue1_speed;
    public int blue2_speed;
    public int blue3_speed;
    public int red1_speed;
    public int red2_speed;
    public int red3_speed;


    // Pushing Power
    public int blue1_pushing_power;
    public int blue2_pushing_power;
    public int blue3_pushing_power;
    public int red1_pushing_power;
    public int red2_pushing_power;
    public int red3_pushing_power;

    // Control
    public int blue1_control;
    public int blue2_control;
    public int blue3_control;
    public int red1_control;
    public int red2_control;
    public int red3_control;

    //Notes
    public String notes;

    public SuperMatch() {}

    public SuperMatch(ScoutMap map)
    {
        try {
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);

            blue1_speed = map.getInt(Constants.Super_Scouting.Qualitative.Speed.BLUE1);
            blue2_speed = map.getInt(Constants.Super_Scouting.Qualitative.Speed.BLUE2);
            blue3_speed = map.getInt(Constants.Super_Scouting.Qualitative.Speed.BLUE3);
            red1_speed = map.getInt(Constants.Super_Scouting.Qualitative.Speed.RED1);
            red2_speed = map.getInt(Constants.Super_Scouting.Qualitative.Speed.RED2);
            red3_speed = map.getInt(Constants.Super_Scouting.Qualitative.Speed.RED3);

            blue1_pushing_power = map.getInt(Constants.Super_Scouting.Qualitative.Pushing_Power.BLUE1);
            blue2_pushing_power = map.getInt(Constants.Super_Scouting.Qualitative.Pushing_Power.BLUE2);
            blue3_pushing_power = map.getInt(Constants.Super_Scouting.Qualitative.Pushing_Power.BLUE3);
            red1_pushing_power = map.getInt(Constants.Super_Scouting.Qualitative.Pushing_Power.RED1);
            red2_pushing_power = map.getInt(Constants.Super_Scouting.Qualitative.Pushing_Power.RED2);
            red3_pushing_power = map.getInt(Constants.Super_Scouting.Qualitative.Pushing_Power.RED3);

            blue1_control = map.getInt(Constants.Super_Scouting.Qualitative.Control.BLUE1);
            blue2_control = map.getInt(Constants.Super_Scouting.Qualitative.Control.BLUE2);
            blue3_control = map.getInt(Constants.Super_Scouting.Qualitative.Control.BLUE3);
            red1_control = map.getInt(Constants.Super_Scouting.Qualitative.Control.RED1);
            red2_control = map.getInt(Constants.Super_Scouting.Qualitative.Control.RED2);
            red3_control = map.getInt(Constants.Super_Scouting.Qualitative.Control.RED3);

            notes = map.getString(Constants.Super_Scouting.NOTES);

        } catch (ScoutValue.TypeException e) {
            Log.e(TAG, e.getMessage());
        }
    }
    
    public ScoutMap toMap()
    {
        ScoutMap map = new ScoutMap();
        
        map.put(Constants.Intent_Extras.MATCH_NUMBER, match_number);

        map.put(Constants.Super_Scouting.Qualitative.Speed.BLUE1, blue1_speed);
        map.put(Constants.Super_Scouting.Qualitative.Speed.BLUE2, blue2_speed);
        map.put(Constants.Super_Scouting.Qualitative.Speed.BLUE3, blue3_speed);
        map.put(Constants.Super_Scouting.Qualitative.Speed.RED1, red1_speed);
        map.put(Constants.Super_Scouting.Qualitative.Speed.RED2, red2_speed);
        map.put(Constants.Super_Scouting.Qualitative.Speed.RED3, red3_speed);

        map.put(Constants.Super_Scouting.Qualitative.Pushing_Power.BLUE1, blue1_pushing_power);
        map.put(Constants.Super_Scouting.Qualitative.Pushing_Power.BLUE2, blue2_pushing_power);
        map.put(Constants.Super_Scouting.Qualitative.Pushing_Power.BLUE3, blue3_pushing_power);
        map.put(Constants.Super_Scouting.Qualitative.Pushing_Power.RED1, red1_pushing_power);
        map.put(Constants.Super_Scouting.Qualitative.Pushing_Power.RED2, red2_pushing_power);
        map.put(Constants.Super_Scouting.Qualitative.Pushing_Power.RED3, red3_pushing_power);

        map.put(Constants.Super_Scouting.Qualitative.Control.BLUE1, blue1_control);
        map.put(Constants.Super_Scouting.Qualitative.Control.BLUE2, blue2_control);
        map.put(Constants.Super_Scouting.Qualitative.Control.BLUE3, blue3_control);
        map.put(Constants.Super_Scouting.Qualitative.Control.RED1, red1_control);
        map.put(Constants.Super_Scouting.Qualitative.Control.RED2, red2_control);
        map.put(Constants.Super_Scouting.Qualitative.Control.RED3, red3_control);

        map.put(Constants.Super_Scouting.NOTES, notes);
        
        return map;
    }

}
