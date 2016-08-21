package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 * Team Match Data
 */
public class TMD {

    private final static String TAG = "TMD";

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

    public TMD() {}

    public TMD(ScoutMap map)
    {
        try {
            team_number = map.getInt(Constants.Intent_Extras.TEAM_NUMBER);
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);


            fouls = map.getInt(Constants.Match_Scouting.Fouls.FOUL);
            tech_fouls = map.getInt(Constants.Match_Scouting.Fouls.TECH_FOUL);
            yellow_card = map.getBoolean(Constants.Match_Scouting.Fouls.YELLOW_CARD);
            red_card = map.getBoolean(Constants.Match_Scouting.Fouls.RED_CARD);

            no_show = map.getBoolean(Constants.Match_Scouting.PostMatch.NO_SHOW);
            stopped_moving = map.getBoolean(Constants.Match_Scouting.PostMatch.STOPPED_MOVING);
            dq = map.getBoolean(Constants.Match_Scouting.PostMatch.DQ);
            notes = map.getString(Constants.Match_Scouting.PostMatch.NOTES);
        } catch (ScoutValue.TypeException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public ScoutMap toMap()
    {
        ScoutMap map = new ScoutMap();
        map.put(Constants.Intent_Extras.TEAM_NUMBER, team_number);
        map.put(Constants.Intent_Extras.MATCH_NUMBER, match_number);

        map.put(Constants.Match_Scouting.Fouls.FOUL, fouls);
        map.put(Constants.Match_Scouting.Fouls.TECH_FOUL, tech_fouls);
        map.put(Constants.Match_Scouting.Fouls.YELLOW_CARD, yellow_card);
        map.put(Constants.Match_Scouting.Fouls.RED_CARD, red_card);

        map.put(Constants.Match_Scouting.PostMatch.DQ, dq);
        map.put(Constants.Match_Scouting.PostMatch.STOPPED_MOVING, stopped_moving);
        map.put(Constants.Match_Scouting.PostMatch.NO_SHOW, no_show);
        map.put(Constants.Match_Scouting.PostMatch.NOTES, notes);

        return map;
    }

}
