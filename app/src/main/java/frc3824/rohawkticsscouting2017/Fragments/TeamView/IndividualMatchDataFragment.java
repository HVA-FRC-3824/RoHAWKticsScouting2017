package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.R;

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

        ((TextView)points.findViewById(R.id.total)).setText(String.format("%d (%0.2f)", tmd.total_points, ((float)tmd.total_points) - mTeam.calc.total_points.average));
        ((TextView)points.findViewById(R.id.auto)).setText(String.format("%d (%0.2f)", tmd.auto_points, ((float)tmd.auto_points) - mTeam.calc.auto_points.average));
        ((TextView)points.findViewById(R.id.teleop)).setText(String.format("%d (%0.2f)", tmd.teleop_points, ((float)tmd.teleop_points) - mTeam.calc.teleop_points.average));
        ((TextView)points.findViewById(R.id.endgame)).setText(String.format("%d (%0.2f)", tmd.endgame_points, ((float)tmd.endgame_points) - mTeam.calc.endgame_points.average));

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
