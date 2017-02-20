package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
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
    public long last_modified;
    public int total_points;

    // Autonomous
    public String auto_start_position;
    public boolean auto_baseline;
    public ArrayList<Gear> auto_gears;
    public int auto_high_goal_made;
    public int auto_high_goal_missed;
    public int auto_high_goal_correction;
    public int auto_low_goal_made;
    public int auto_low_goal_missed;
    public int auto_low_goal_correction;
    public int auto_hoppers;
    public int auto_points;

    // Teleop
    public ArrayList<Gear> teleop_gears;
    public int teleop_high_goal_made;
    public int teleop_high_goal_missed;
    public int teleop_high_goal_correction;
    public int teleop_low_goal_made;
    public int teleop_low_goal_missed;
    public int teleop_low_goal_correction;
    public int teleop_hoppers;
    public int teleop_picked_up_gears;
    public int teleop_points;

    // Endgame
    public String endgame_climb;
    public String endgame_climb_time;
    public int endgame_points;

    //Post Match
    public boolean no_show;
    public boolean stopped_moving;
    public boolean dq;
    public String notes;

    public boolean tags_blocked_shots;
    public boolean tags_pinned_robot;
    public boolean tags_defended_loading_station;
    public boolean tags_defended_airship;
    public boolean tags_broke;
    public boolean tags_dumped_all_hoppers;

    // Fouls
    public int fouls;
    public int tech_fouls;
    public boolean yellow_card;
    public boolean red_card;

    public TeamMatchData() {
        auto_gears = new ArrayList<>();
        teleop_gears = new ArrayList<>();
    }

    public TeamMatchData(ScoutMap map) {
        try {
            team_number = map.getInt(Constants.Intent_Extras.TEAM_NUMBER);
            match_number = map.getInt(Constants.Intent_Extras.MATCH_NUMBER);
            alliance_color = map.getString(Constants.Settings.ALLIANCE_COLOR);
            alliance_number = map.getInt(Constants.Settings.ALLIANCE_NUMBER);
            scout_name = map.getString(Constants.Match_Scouting.SCOUT_NAME);

            // GAME SPECIFIC
            auto_start_position = map.getString(Constants.Match_Scouting.Autonomous.AUTO_START_POSITION);
            auto_baseline = map.getBoolean(Constants.Match_Scouting.Autonomous.AUTO_BASELINE);
            auto_gears = (ArrayList<Gear>)map.getObject(Constants.Match_Scouting.Autonomous.AUTO_GEARS);
            auto_high_goal_made = map.getInt(Constants.Match_Scouting.Autonomous.AUTO_HIGH_GOAL_MADE);
            auto_high_goal_missed = map.getInt(Constants.Match_Scouting.Autonomous.AUTO_HIGH_GOAL_MISSED);
            auto_low_goal_made = map.getInt(Constants.Match_Scouting.Autonomous.AUTO_LOW_GOAL_MADE);
            auto_low_goal_missed = map.getInt(Constants.Match_Scouting.Autonomous.AUTO_LOW_GOAL_MISSED);
            auto_hoppers = map.getInt(Constants.Match_Scouting.Autonomous.AUTO_HOPPERS);

            teleop_gears = (ArrayList<Gear>)map.getObject(Constants.Match_Scouting.Teleop.TELEOP_GEARS);
            teleop_high_goal_made = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_HIGH_GOAL_MADE);
            teleop_high_goal_missed = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_HIGH_GOAL_MISSED);
            teleop_low_goal_made = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_LOW_GOAL_MADE);
            teleop_low_goal_missed = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_LOW_GOAL_MISSED);
            teleop_hoppers = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_HOPPERS);
            teleop_picked_up_gears = map.getInt(Constants.Match_Scouting.Teleop.TELEOP_PICKED_UP_GEARS);

            endgame_climb = map.getString(Constants.Match_Scouting.Endgame.ENDGAME_CLIMB);
            endgame_climb_time = map.getString(Constants.Match_Scouting.Endgame.ENDGAME_CLIMB_TIME);

            tags_blocked_shots = map.getBoolean(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BLOCK_SHOTS);
            tags_pinned_robot = map.getBoolean(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.PINNED_ROBOT);
            tags_defended_loading_station = map.getBoolean(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_LOADING_STATION);
            tags_defended_airship = map.getBoolean(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_AIRSHIP);
            tags_broke = map.getBoolean(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BROKE);
            tags_dumped_all_hoppers = map.getBoolean(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DUMPED_ALL_HOPPERS);

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
        map.put(Constants.Match_Scouting.Autonomous.AUTO_START_POSITION, auto_start_position);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_BASELINE, auto_baseline);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_GEARS, auto_gears);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_HIGH_GOAL_MADE, auto_high_goal_made);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_HIGH_GOAL_MISSED, auto_high_goal_missed);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_HIGH_GOAL_CORRECTION, auto_high_goal_correction);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_LOW_GOAL_MADE, auto_low_goal_made);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_LOW_GOAL_MISSED, auto_low_goal_missed);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_LOW_GOAL_CORRECTION, auto_low_goal_correction);
        map.put(Constants.Match_Scouting.Autonomous.AUTO_HOPPERS, auto_hoppers);

        map.put(Constants.Match_Scouting.Teleop.TELEOP_GEARS, teleop_gears);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_HIGH_GOAL_MADE, teleop_high_goal_made);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_HIGH_GOAL_MISSED, teleop_high_goal_missed);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_HIGH_GOAL_CORRECTION, teleop_high_goal_correction);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_LOW_GOAL_MADE, teleop_low_goal_made);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_LOW_GOAL_MISSED, teleop_low_goal_missed);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_LOW_GOAL_CORRECTION, teleop_low_goal_correction);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_HOPPERS, teleop_hoppers);
        map.put(Constants.Match_Scouting.Teleop.TELEOP_PICKED_UP_GEARS, teleop_picked_up_gears);

        map.put(Constants.Match_Scouting.Endgame.ENDGAME_CLIMB, endgame_climb);
        map.put(Constants.Match_Scouting.Endgame.ENDGAME_CLIMB_TIME, endgame_climb_time);


        map.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BLOCK_SHOTS, tags_blocked_shots);
        map.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.PINNED_ROBOT, tags_pinned_robot);
        map.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_LOADING_STATION, tags_defended_loading_station);
        map.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_AIRSHIP, tags_defended_airship);
        map.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BROKE, tags_broke);
        map.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DUMPED_ALL_HOPPERS, tags_dumped_all_hoppers);

        ////////////////

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
