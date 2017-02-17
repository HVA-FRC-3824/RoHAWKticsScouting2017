package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Activities.TeamView;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/18/16
 *
 *
 */
public class MatchViewTeamFragment extends Fragment {

    private final static String TAG = "MatchViewTeamFragment";

    private View mView;
    private Team mTeam;

    public MatchViewTeamFragment() {
        mTeam = null;
    }

    public void setTeam(int team_number) {
        mTeam = Database.getInstance().getTeam(team_number);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_team, container, false);
        if(mTeam != null) {

            ((TextView) mView.findViewById(R.id.team_number)).setText(String.valueOf(mTeam.team_number));

            ((TextView) mView.findViewById(R.id.number_of_matches)).setText(String.valueOf(mTeam.completed_matches.size()));

            ((TextView) mView.findViewById(R.id.gears)).setText(String.format("A: %0.2f T: %0.2f", mTeam.calc.auto_total_gears_placed.average, mTeam.calc.teleop_total_gears_placed.average));

            ((TextView) mView.findViewById(R.id.climbing)).setText(String.format("%d (%0.2f s)", mTeam.calc.endgame_climb_successful, mTeam.calc.endgame_climb_time.average));

            ((TextView) mView.findViewById(R.id.fuel)).setText(String.format("A: H - %0.2f L - %0.2f\nT: H - %0.2f L - %0.2f",
                    mTeam.calc.auto_high_goal_made.average,
                    mTeam.calc.auto_low_goal_made.average,
                    mTeam.calc.teleop_high_goal_made.average,
                    mTeam.calc.teleop_low_goal_made.average));

            ((TextView) mView.findViewById(R.id.pilot)).setText(String.format("%0.2f", mTeam.calc.pilot_ratings.average));

            ((TextView) mView.findViewById(R.id.defense)).setText(String.valueOf(mTeam.calc.rank_defense));

            ((TextView) mView.findViewById(R.id.driver_control)).setText(String.valueOf(mTeam.calc.rank_control));

            ((TextView) mView.findViewById(R.id.speed)).setText(String.valueOf(mTeam.calc.rank_speed));

            mView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TeamView.class);
                    intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeam.team_number);
                    startActivity(intent);
                }
            });
        } else {
            mView.findViewById(R.id.details).setVisibility(View.INVISIBLE);
        }

        return mView;
    }

}
