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
    public long last_modified;

    // Qualitative

    // Speed
    public ArrayList<Qualitative> blue_speed;
    public ArrayList<Qualitative> red_speed;

    // Torque (Pushing Power)
    public ArrayList<Qualitative> blue_intake_ability;
    public ArrayList<Qualitative> red_intake_ability;

    // Control
    public ArrayList<Qualitative> blue_control;
    public ArrayList<Qualitative> red_control;

    // Defense
    public ArrayList<Qualitative> blue_defense;
    public ArrayList<Qualitative> red_defense;

    // Pilot
    public String blue1_pilot_rating;
    public String blue2_pilot_rating;
    public String blue3_pilot_rating;
    public String red1_pilot_rating;
    public String red2_pilot_rating;
    public String red3_pilot_rating;

    //Notes
    public String notes;

    public SuperMatchData() {
        blue_speed = new ArrayList<>();
        red_speed = new ArrayList<>();

        blue_intake_ability = new ArrayList<>();
        red_intake_ability = new ArrayList<>();

        blue_control = new ArrayList<>();
        red_control = new ArrayList<>();

        blue_defense = new ArrayList<>();
        red_defense = new ArrayList<>();
    }

    public SuperMatchData(ScoutMap map) {
        try {
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);
            scout_name = map.getString(Constants.Super_Scouting.SCOUT_NAME);

            blue_speed = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_SPEED);
            blue_intake_ability = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_INTAKE_ABILITY);
            blue_control = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_CONTROL);
            blue_defense = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.BLUE_DEFENSE);

            red_speed = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.RED_SPEED);
            red_intake_ability = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.RED_INTAKE_ABILITY);
            red_control = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.RED_CONTROL);
            red_defense = (ArrayList<Qualitative>)map.getObject(Constants.Super_Scouting.Qualitative.RED_DEFENSE);

            blue1_pilot_rating = map.getString(Constants.Super_Scouting.Miscellaneous.BLUE1_PILOT_RATING);
            blue2_pilot_rating = map.getString(Constants.Super_Scouting.Miscellaneous.BLUE2_PILOT_RATING);
            blue3_pilot_rating = map.getString(Constants.Super_Scouting.Miscellaneous.BLUE3_PILOT_RATING);
            red1_pilot_rating = map.getString(Constants.Super_Scouting.Miscellaneous.RED1_PILOT_RATING);
            red2_pilot_rating = map.getString(Constants.Super_Scouting.Miscellaneous.RED2_PILOT_RATING);
            red3_pilot_rating = map.getString(Constants.Super_Scouting.Miscellaneous.RED3_PILOT_RATING);


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
        map.put(Constants.Super_Scouting.Qualitative.BLUE_INTAKE_ABILITY, blue_intake_ability);
        map.put(Constants.Super_Scouting.Qualitative.BLUE_CONTROL, blue_control);
        map.put(Constants.Super_Scouting.Qualitative.BLUE_DEFENSE, blue_defense);

        map.put(Constants.Super_Scouting.Qualitative.RED_SPEED, red_speed);
        map.put(Constants.Super_Scouting.Qualitative.RED_INTAKE_ABILITY, red_intake_ability);
        map.put(Constants.Super_Scouting.Qualitative.RED_CONTROL, red_control);
        map.put(Constants.Super_Scouting.Qualitative.RED_DEFENSE, red_defense);

        map.put(Constants.Super_Scouting.Miscellaneous.BLUE1_PILOT_RATING, blue1_pilot_rating);
        map.put(Constants.Super_Scouting.Miscellaneous.BLUE2_PILOT_RATING, blue2_pilot_rating);
        map.put(Constants.Super_Scouting.Miscellaneous.BLUE3_PILOT_RATING, blue3_pilot_rating);
        map.put(Constants.Super_Scouting.Miscellaneous.RED1_PILOT_RATING, red1_pilot_rating);
        map.put(Constants.Super_Scouting.Miscellaneous.RED2_PILOT_RATING, red2_pilot_rating);
        map.put(Constants.Super_Scouting.Miscellaneous.RED3_PILOT_RATING, red3_pilot_rating);

        map.put(Constants.Super_Scouting.NOTES, notes);
        
        return map;
    }

}
