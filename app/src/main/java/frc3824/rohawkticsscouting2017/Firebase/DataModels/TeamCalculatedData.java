package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Aggregated data for a specific team_number
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
    public int auto_start_position_far;
    public int auto_start_position_center;
    public int auto_start_position_near;
    public LowLevelStats auto_baseline;
    public GearResults auto_gears;
    public ShootingResults auto_shooting;
    public LowLevelStats auto_hoppers;
    public LowLevelStats auto_points;

    // Teleop
    public GearResults teleop_gears;
    public ShootingResults teleop_shooting;
    public LowLevelStats teleop_hoppers;
    public LowLevelStats teleop_picked_up_gears;
    public LowLevelStats teleop_points;

    // Endgame
    public ClimbResults climb;
    public LowLevelStats endgame_points;

    // Post Match
    public LowLevelStats no_show;
    public LowLevelStats stopped_moving;
    public LowLevelStats dq;

    // Fouls
    public LowLevelStats fouls;
    public LowLevelStats tech_fouls;
    public LowLevelStats yellow_cards;
    public LowLevelStats red_cards;

    public TeamCalculatedData() {
        auto_baseline = new LowLevelStats();
        auto_gears = new GearResults();
        auto_shooting = new ShootingResults();
        auto_hoppers = new LowLevelStats();
        auto_points = new LowLevelStats();

        teleop_gears = new GearResults();
        teleop_shooting = new ShootingResults();
        teleop_hoppers = new LowLevelStats();
        teleop_picked_up_gears = new LowLevelStats();
        teleop_points = new LowLevelStats();

        climb = new ClimbResults();
        endgame_points = new LowLevelStats();

        no_show = new LowLevelStats();
        stopped_moving = new LowLevelStats();
        dq = new LowLevelStats();

        fouls = new LowLevelStats();
        tech_fouls = new LowLevelStats();
        yellow_cards = new LowLevelStats();
        red_cards = new LowLevelStats();
    }
}
