package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Gear;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Gears;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 *
 */
public class IndividualMatchDataFragment extends Fragment{

    private final static String TAG = "IndividualMatchDataFragment";

    private Team mTeam;
    private int mMatchNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_individual_match_data, container, false);

        TeamMatchData tmd = mTeam.completed_matches.get(mMatchNumber);

        if(mMatchNumber == mTeam.info.surrogate_match_number){
            view.findViewById(R.id.surrogate_text).setVisibility(View.VISIBLE);
        }

        // Points
        LinearLayout points = (LinearLayout)view.findViewById(R.id.points_body);

        int auto_points = (tmd.auto_high_goal_made + tmd.auto_high_goal_correction) +
                          (tmd.auto_low_goal_made + tmd.auto_low_goal_correction) / 3;
        int auto_gears = 0;
        for(Gear gear: tmd.auto_gears)
        {
            if(gear.placed){
                auto_gears += 1;
            }
        }

        int rotors = 0;
        if(auto_gears == 3){
            auto_points += 120;
            rotors += 2;
        } else if(auto_gears > 1){
            auto_points += 60;
            rotors += 1;
        }

        int teleop_points = (tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction) / 3 +
                            (tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction) / 9;


        int teleop_gears = 0;
        for(Gear gear: tmd.teleop_gears)
        {
            if(gear.placed){
                teleop_gears += 1;
            }
        }

        if(auto_gears + teleop_gears >= 12){
            teleop_points += 40 * (4 - rotors);
        } else if(auto_gears + teleop_gears >= 6) {
            teleop_points += 40 * (3 - rotors);
        } else if(auto_gears + teleop_gears >= 2) {
            teleop_points += 40 * (2 - rotors);
        } else {
            teleop_points += 40 * (1 - rotors);
        }

        int endgame_points = tmd.endgame_climb == Constants.Match_Scouting.Endgame.CLIMB_OPTIONS.SUCCESSFUL? 50 : 0;

        int total_points = auto_points + teleop_points + endgame_points;

        ((TextView)points.findViewById(R.id.total)).setText(String.format("%d", total_points));
        ((TextView)points.findViewById(R.id.auto)).setText(String.format("%d", auto_points));
        ((TextView)points.findViewById(R.id.teleop)).setText(String.format("%d", teleop_points));

        ((TextView)points.findViewById(R.id.endgame)).setText(String.format("%d", endgame_points));

        // Auto (auto is a keyword so the underscore is used)
        LinearLayout auto_ = (LinearLayout)view.findViewById(R.id.auto_body);


        // Teleop
        LinearLayout teleop = (LinearLayout)view.findViewById(R.id.teleop_body);


        // Endgame
        LinearLayout endgame = (LinearLayout)view.findViewById(R.id.endgame_body);


        return view;
    }

    public void setTeamMatch(Team team, int position){
        mTeam = team;
        mMatchNumber = mTeam.info.match_numbers.get(position);
    }
}
