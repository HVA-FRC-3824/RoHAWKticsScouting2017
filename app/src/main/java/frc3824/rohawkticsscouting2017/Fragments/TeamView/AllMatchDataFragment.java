package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamCalculatedData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPilotData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 * Fragment that displays averages, totals and aggregated stats of all metrics
 */
public class AllMatchDataFragment extends Fragment {

    private final static String TAG = "AllMatchDataFragment";

    private View mView;
    private TeamCalculatedData mTcd;

    public AllMatchDataFragment(){
        mView = null;
        mTcd = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_view_all_match_data, container, false);

        if(mTcd == null) {
            // Points
            View points = mView.findViewById(R.id.points_body);


            ((TextView)points.findViewById(R.id.total)).setText("N/A");
            ((TextView)points.findViewById(R.id.auto)).setText("N/A");
            ((TextView)points.findViewById(R.id.teleop)).setText("N/A");

            ((TextView)points.findViewById(R.id.endgame)).setText("N/A");

            // Auto (auto is a keyword so the underscore is used)
            View auto_ = mView.findViewById(R.id.auto_body);

            ((TextView)auto_.findViewById(R.id.start_position)).setText("N/A");
            ((TextView)auto_.findViewById(R.id.baseline)).setText("N/A");
            ((TextView)auto_.findViewById(R.id.gears)).setText("N/A");

            ((TextView)auto_.findViewById(R.id.high_goal)).setText("N/A");

            ((TextView)auto_.findViewById(R.id.low_goal )).setText("N/A");

            ((TextView)auto_.findViewById(R.id.hoppers)).setText("N/A");

            // Teleop
            View teleop = mView.findViewById(R.id.teleop_body);


            ((TextView)teleop.findViewById(R.id.gears)).setText("N/A");

            ((TextView)teleop.findViewById(R.id.high_goal)).setText("N/A");

            ((TextView)teleop.findViewById(R.id.low_goal)).setText("N/A");

            ((TextView)teleop.findViewById(R.id.hoppers)).setText("N/A");
            ((TextView)teleop.findViewById(R.id.gears_picked_up)).setText("N/A");

            // Endgame
            View endgame = mView.findViewById(R.id.endgame_body);

            ((TextView)endgame.findViewById(R.id.climb)).setText("N/A");
            ((TextView)endgame.findViewById(R.id.climb_time)).setText("N/A");

            // Pilot
            View pilot = mView.findViewById(R.id.pilot_body);
            ((TextView)pilot.findViewById(R.id.rating)).setText("N/A");
            ((TextView)pilot.findViewById(R.id.lifts)).setText("N/A");
            ((TextView)pilot.findViewById(R.id.drops)).setText("N/A");
            ((TextView)pilot.findViewById(R.id.lift_percentage)).setText("N/A");
        } else {
            // Points
            View points = mView.findViewById(R.id.points_body);


            double auto_points = mTcd.auto_shooting.high.made.average + mTcd.auto_shooting.low.made.average / 3;
            double auto_gears = mTcd.auto_gears.total.placed.average;

            int rotors = 0;
            if(auto_gears == 3){
                auto_points += 120;
                rotors += 2;
            } else if(auto_gears > 1){
                auto_points += 60;
                rotors += 1;
            }

            double teleop_points = mTcd.teleop_shooting.high.made.average / 3 + mTcd.teleop_shooting.low.made.average / 9;

            double teleop_gears = mTcd.teleop_gears.total.placed.average;

            if(auto_gears + teleop_gears >= 12){
                teleop_points += 40 * (4 - rotors);
            } else if(auto_gears + teleop_gears >= 6) {
                teleop_points += 40 * (3 - rotors);
            } else if(auto_gears + teleop_gears >= 2) {
                teleop_points += 40 * (2 - rotors);
            } else {
                teleop_points += 40 * (1 - rotors);
            }

            double endgame_points = mTcd.climb.success_percentage * 50;

            double total_points = auto_points + teleop_points + endgame_points;

            ((TextView)points.findViewById(R.id.total)).setText(String.format("%02.2f", total_points));
            ((TextView)points.findViewById(R.id.auto)).setText(String.format("%02.2f", auto_points));
            ((TextView)points.findViewById(R.id.teleop)).setText(String.format("%02.2f", teleop_points));

            ((TextView)points.findViewById(R.id.endgame)).setText(String.format("%02.2f", endgame_points));

            // Auto (auto is a keyword so the underscore is used)
            View auto_ = mView.findViewById(R.id.auto_body);

            ((TextView)auto_.findViewById(R.id.start_position)).setText(String.format("%d / %d / %d",
                    mTcd.auto_start_position_near,
                    mTcd.auto_start_position_center,
                    mTcd.auto_start_position_far));
            ((TextView)auto_.findViewById(R.id.baseline)).setText(String.format("%02.2f%%", mTcd.auto_baseline.average * 100.0));
            ((TextView)auto_.findViewById(R.id.gears)).setText(String.format("%02.1f (%02.1f) / %02.1f (%02.1f) / %02.1f (%02.1f) / %02.1f (%02.1f)",
                    mTcd.auto_gears.near.placed.average, mTcd.auto_gears.near.dropped.average,
                    mTcd.auto_gears.center.placed.average, mTcd.auto_gears.center.dropped.average,
                    mTcd.auto_gears.far.placed.average, mTcd.auto_gears.far.dropped.average,
                    mTcd.auto_gears.total.placed.average, mTcd.auto_gears.total.dropped.average));

            double percent = mTcd.auto_shooting.high.made.average /
                    (mTcd.auto_shooting.high.made.average + mTcd.auto_shooting.high.missed.average);
            if(Double.isNaN(percent)){
                percent = 0;
            }
            ((TextView)auto_.findViewById(R.id.high_goal)).setText(String.format("%02.2f / %02.2f (%02.2f%%)",
                    mTcd.auto_shooting.high.made.average, mTcd.auto_shooting.high.missed.average, percent * 100.0));

            percent = (mTcd.auto_shooting.low.made.average) /
                    (mTcd.auto_shooting.low.made.average + mTcd.auto_shooting.low.missed.average);
            if(Double.isNaN(percent)){
                percent = 0;
            }
            ((TextView)auto_.findViewById(R.id.low_goal)).setText(String.format("%02.2f / %02.2f (%02.2f%%)",
                    mTcd.auto_shooting.low.made.average, mTcd.auto_shooting.low.missed.average, percent * 100.0));

            ((TextView)auto_.findViewById(R.id.hoppers)).setText(String.valueOf(mTcd.auto_hoppers.average));

            // Teleop
            View teleop = mView.findViewById(R.id.teleop_body);


            ((TextView)teleop.findViewById(R.id.gears)).setText(String.format("%02.1f (%02.1f) / %02.1f (%02.1f) / %02.1f (%02.1f) / %02.1f (%02.1f)",
                    mTcd.teleop_gears.near.placed.average, mTcd.teleop_gears.near.dropped.average,
                    mTcd.teleop_gears.center.placed.average, mTcd.teleop_gears.center.dropped.average,
                    mTcd.teleop_gears.far.placed.average, mTcd.teleop_gears.far.dropped.average,
                    mTcd.teleop_gears.total.placed.average, mTcd.teleop_gears.total.dropped.average));

            percent = mTcd.teleop_shooting.high.made.average /
                    (mTcd.teleop_shooting.high.made.average + mTcd.teleop_shooting.high.missed.average);
            if(Double.isNaN(percent)){
                percent = 0;
            }
            ((TextView)teleop.findViewById(R.id.high_goal)).setText(String.format("%02.2f / %02.2f (%02.2f%%)",
                    mTcd.teleop_shooting.high.made.average, mTcd.teleop_shooting.high.missed.average, percent* 100.0));

            percent = mTcd.teleop_shooting.low.made.average /
                    (mTcd.teleop_shooting.low.made.average + mTcd.teleop_shooting.low.missed.average);
            if(Double.isNaN(percent)){
                percent = 0;
            }
            ((TextView)teleop.findViewById(R.id.low_goal)).setText(String.format("%02.2f / %02.2f (%02.2f%%)",
                    mTcd.teleop_shooting.low.made.average, mTcd.teleop_shooting.low.missed.average, percent * 100.0));

            ((TextView)teleop.findViewById(R.id.hoppers)).setText(String.valueOf(mTcd.teleop_hoppers.average));
            ((TextView)teleop.findViewById(R.id.gears_picked_up)).setText(String.valueOf(mTcd.teleop_picked_up_gears.average));

            // Endgame
            View endgame = mView.findViewById(R.id.endgame_body);

            ((TextView)endgame.findViewById(R.id.climb)).setText(String.format("%02.2f%%", mTcd.climb.success_percentage * 100.0));
            ((TextView)endgame.findViewById(R.id.climb_time)).setText(String.format("%02.1fs", mTcd.climb.time.average));

            // Pilot
            TeamPilotData tpd = Database.getInstance().getTeamPilotData(mTcd.team_number);

            View pilot = mView.findViewById(R.id.pilot_body);
            ((TextView)pilot.findViewById(R.id.rating)).setText(String.format("%02.1f",tpd.rating.average));
            ((TextView)pilot.findViewById(R.id.lifts)).setText(String.format("%02.1f",tpd.lifts.average));
            ((TextView)pilot.findViewById(R.id.drops)).setText(String.format("%02.1f",tpd.drops.average));

            percent = tpd.lifts.average / (tpd.lifts.average + tpd.drops.average);
            if(Double.isNaN(percent)){
                percent = 0;
            }
            ((TextView)pilot.findViewById(R.id.lift_percentage)).setText(String.format("%02.2f%%", percent * 100));
        }

        return mView;
    }

    public void setup(){


    }

    public void setTeam(int team_number){
        mTcd = Database.getInstance().getTeamCalculatedData(team_number);
    }
}
