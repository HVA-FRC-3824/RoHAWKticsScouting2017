package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * Data collected by match scouts
 */
@IgnoreExtraProperties
public class TeamMatchData {

    private final static String TAG = "TeamMatchData";

    public int match_number;
    public int team_number;
    public String alliance_color;
    public int alliance_number;
    public String scout_name;

    public int total_points; // calculated by server

    // Autonomous
    public int auto_points; // calculated by server
    public boolean auto_baseline;
    public Gears auto_gears;
    public Shots auto_shots;
    public int auto_hoppers;

    // Teleop
    public int teleop_points; // calculated by server
    public Gears teleop_gears;
    public Shots teleop_shots;
    public int teleop_hoppers;
    public int teleop_picked_up_gears;

    // Endgame
    public int endgame_points; // calculated by server
    public String endgame_climb;

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

    public TeamMatchData() {
        total_points = -1; // Required for determining if point calculation have been made on the server
        auto_gears = new Gears();
        teleop_gears = new Gears();
        auto_shots = new Shots();
        teleop_shots = new Shots();
    }

    public TeamMatchData(ScoutMap map) {
        try {
            team_number = map.getInt(Constants.Intent_Extras.TEAM_NUMBER);
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);
            alliance_color = map.getString(Constants.Settings.ALLIANCE_COLOR);
            alliance_number = map.getInt(Constants.Settings.ALLIANCE_NUMBER);
            scout_name = map.getString(Constants.Match_Scouting.SCOUT_NAME);

            // GAME SPECIFIC
            auto_baseline = map.getBoolean(Constants.Match_Scouting.Autonomous.AUTO_BASELINE);
            auto_gears = (Gears)map.getObject(Constants.Match_Scouting.Autonomous.AUTO_GEARS);
            auto_shots = (Shots)map.getObject(Constants.Match_Scouting.Autonomous.AUTO_SHOTS);
            auto_hoppers = map.getInt(Constants.Match_Scouting.Autonomous.AUTO_HOPPERS);

            teleop_gears = (Gears)map.getObject(Constants.Match_Scouting.Teleop.TELEOP_GEARS);
            teleop_shots = (Shots)map.getObject(Constants.Match_Scouting.Teleop.TELEOP_SHOTS);
            teleop_hoppers = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_HOPPERS);
            teleop_picked_up_gears = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_PICKED_UP_GEARS);

            endgame_climb = map.getString(Constants.Match_Scouting.Endgame.ENDGAME_CLIMB);
            ////////////////

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

    @Exclude
    public ScoutMap toMap() {
        ScoutMap map = new ScoutMap();
        map.put(Constants.Intent_Extras.TEAM_NUMBER, team_number);
        map.put(Constants.Intent_Extras.MATCH_NUMBER, match_number);
        map.put(Constants.Settings.ALLIANCE_COLOR, alliance_color);
        map.put(Constants.Settings.ALLIANCE_NUMBER, alliance_number);
        map.put(Constants.Match_Scouting.SCOUT_NAME, scout_name);

        // GAME SPECIFIC

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
