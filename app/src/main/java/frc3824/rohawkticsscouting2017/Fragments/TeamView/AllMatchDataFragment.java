package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Gear;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamCalculatedData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 * Fragment that displays averages, totals and aggregated stats of all metrics
 */
public class AllMatchDataFragment extends Fragment {

    private final static String TAG = "AllMatchDataFragment";

    private TeamCalculatedData mTcd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_all_match_data, container, false);

        // Points
        View points = view.findViewById(R.id.points_body);


        double auto_points = mTcd.auto_high_goal_made.average + mTcd.auto_low_goal_made.average / 3;
        double auto_gears = mTcd.auto_total_gears_placed.average;

        int rotors = 0;
        if(auto_gears == 3){
            auto_points += 120;
            rotors += 2;
        } else if(auto_gears > 1){
            auto_points += 60;
            rotors += 1;
        }

        double teleop_points = mTcd.teleop_high_goal_made.average / 3 + mTcd.teleop_low_goal_made.average / 9;

        double teleop_gears = mTcd.teleop_total_gears_placed.average;

        if(auto_gears + teleop_gears >= 12){
            teleop_points += 40 * (4 - rotors);
        } else if(auto_gears + teleop_gears >= 6) {
            teleop_points += 40 * (3 - rotors);
        } else if(auto_gears + teleop_gears >= 2) {
            teleop_points += 40 * (2 - rotors);
        } else {
            teleop_points += 40 * (1 - rotors);
        }

        int endgame_points = mTcd.endgame_climb_success;

        double total_points = auto_points + teleop_points + endgame_points;

        ((TextView)points.findViewById(R.id.total)).setText(String.format("%0.2f", total_points));
        ((TextView)points.findViewById(R.id.auto)).setText(String.format("%0.2f", auto_points));
        ((TextView)points.findViewById(R.id.teleop)).setText(String.format("%0.2f", teleop_points));

        ((TextView)points.findViewById(R.id.endgame)).setText(String.format("%0.2f", endgame_points));

        // Auto (auto is a keyword so the underscore is used)
        View auto_ = view.findViewById(R.id.auto_body);

        ((TextView)auto_.findViewById(R.id.start_position)).setText(String.format("%d / %d / %d",
                mTcd.auto_start_position_near,
                mTcd.auto_start_position_center,
                mTcd.auto_start_position_far));
        ((TextView)auto_.findViewById(R.id.baseline)).setText(String.format("%0.2%%", mTcd.auto_baseline.average * 100.0));
        ((TextView)auto_.findViewById(R.id.gears)).setText(String.format("%0.1f (%0.1f) / %0.1f (%0.1f) / %0.1f (%0.1f) / %0.1f (0.1f)",
                mTcd.auto_near_gears_placed.average, mTcd.auto_near_gears_dropped.average,
                mTcd.auto_center_gears_placed.average, mTcd.auto_center_gears_dropped.average,
                mTcd.auto_far_gears_placed.average, mTcd.auto_far_gears_dropped.average,
                mTcd.auto_total_gears_placed.average, mTcd.auto_total_gears_dropped.average));

        double percent = mTcd.auto_high_goal_made.average /
                (mTcd.auto_high_goal_made.average + mTcd.auto_high_goal_missed.average);
        ((TextView)auto_.findViewById(R.id.high_goal)).setText(String.format("%d / %d (%0.2f%%)",
                mTcd.auto_high_goal_made.average, mTcd.auto_high_goal_missed.average, percent));

        percent = (mTcd.auto_low_goal_made.average) /
                (mTcd.auto_low_goal_made.average + mTcd.auto_low_goal_missed.average);
        ((TextView)auto_.findViewById(R.id.high_goal)).setText(String.format("%d / %d (%0.2f%%)",
                mTcd.auto_low_goal_made.average, mTcd.auto_low_goal_missed.average, percent));

        ((TextView)auto_.findViewById(R.id.hoppers)).setText(String.valueOf(mTcd.auto_hoppers.average));

        // Teleop
        View teleop = view.findViewById(R.id.teleop_body);

        
        ((TextView)teleop.findViewById(R.id.gears)).setText(String.format("%0.1f (%0.1f) / %0.1f (%0.1f) / %0.1f (%0.1f) / %0.1f (0.1f)",
                mTcd.teleop_near_gears_placed.average, mTcd.teleop_near_gears_dropped.average,
                mTcd.teleop_center_gears_placed.average, mTcd.teleop_center_gears_dropped.average,
                mTcd.teleop_far_gears_placed.average, mTcd.teleop_far_gears_dropped.average,
                mTcd.teleop_total_gears_placed.average, mTcd.teleop_total_gears_dropped.average));

        percent = mTcd.teleop_high_goal_made.average /
                (mTcd.teleop_high_goal_made.average + mTcd.teleop_high_goal_missed.average);
        ((TextView)teleop.findViewById(R.id.high_goal)).setText(String.format("%d / %d (%0.2f%%)",
                mTcd.teleop_high_goal_made.average, mTcd.teleop_high_goal_missed.average, percent));

        percent = mTcd.teleop_low_goal_made.average /
                (mTcd.teleop_low_goal_made.average + mTcd.teleop_low_goal_missed.average);
        ((TextView)teleop.findViewById(R.id.high_goal)).setText(String.format("%d / %d (%0.2f%%)",
                mTcd.teleop_low_goal_made, mTcd.teleop_low_goal_missed, percent));

        ((TextView)teleop.findViewById(R.id.hoppers)).setText(String.valueOf(mTcd.teleop_hoppers.average));
        ((TextView)teleop.findViewById(R.id.gears_picked_up)).setText(String.valueOf(mTcd.teleop_picked_up_gears.average));

        // Endgame
        View endgame = view.findViewById(R.id.endgame_body);

        ((TextView)endgame.findViewById(R.id.climb)).setText(String.format("%0.2f%%", mTcd.endgame_climb_success));
        ((TextView)endgame.findViewById(R.id.climb_time)).setText(String.format("%0.1fs", mTcd.endgame_climb_time.average));

        return view;
    }

    public void setTeam(int team_number){
        mTcd = Database.getInstance().getTeamCalculatedData(team_number);
    }
}
