package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Aggregated data for a specific team
 */
@IgnoreExtraProperties
public class TeamCalculatedData {

    private final static String TAG = "TeamCalculatedData";

    /*
        Calculated Data
    */

    public int team_number;

    public LowLevelStats total_points;

    // Autonomous
    public LowLevelStats auto_points;
    public LowLevelStats auto_baseline;
    public LowLevelStats auto_total_gears_placed;
    public LowLevelStats auto_near_gears_placed;
    public LowLevelStats auto_center_gears_placed;
    public LowLevelStats auto_far_gears_placed;
    public LowLevelStats auto_total_gears_dropped;
    public LowLevelStats auto_near_gears_dropped;
    public LowLevelStats auto_center_gears_dropped;
    public LowLevelStats auto_far_gears_dropped;
    public LowLevelStats auto_high_goal_made;
    public LowLevelStats auto_high_goal_missed;
    public LowLevelStats auto_low_goal_made;
    public LowLevelStats auto_low_goal_missed;
    public LowLevelStats auto_hoppers;

    // Teleop
    public LowLevelStats teleop_points;
    public LowLevelStats teleop_total_gears_placed;
    public LowLevelStats teleop_near_gears_placed;
    public LowLevelStats teleop_center_gears_placed;
    public LowLevelStats teleop_far_gears_placed;
    public LowLevelStats teleop_total_gears_dropped;
    public LowLevelStats teleop_near_gears_dropped;
    public LowLevelStats teleop_center_gears_dropped;
    public LowLevelStats teleop_far_gears_dropped;
    public LowLevelStats teleop_high_goal_made;
    public LowLevelStats teleop_high_goal_missed;
    public LowLevelStats teleop_low_goal_made;
    public LowLevelStats teleop_low_goal_missed;
    public LowLevelStats teleop_hoppers;
    public LowLevelStats teleop_picked_up_gears;

    // Endgame
    public LowLevelStats endgame_points;
    public int endgame_climb_success;
    public int endgame_climb_fell;
    public int endgame_climb_failed;

    // Post Match
    public LowLevelStats no_show;
    public LowLevelStats stopped_moving;
    public LowLevelStats dq;

    // Fouls
    public LowLevelStats fouls;
    public LowLevelStats tech_fouls;
    public LowLevelStats yellow_cards;
    public LowLevelStats red_cards;

    // Qualitative
    public double zscore_speed;
    public int rank_speed;

    public double zscore_torque;
    public int rank_torque;

    public double zscore_control;
    public int rank_control;

    public double zscore_defense;
    public int rank_defense;

    public TeamCalculatedData() {}
}
