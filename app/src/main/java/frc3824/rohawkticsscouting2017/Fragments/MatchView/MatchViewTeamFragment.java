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

        ((TextView) mView.findViewById(R.id.team_number)).setText(String.valueOf(mTeam.team_number));

        if(mTeam.completed_matches.size() == 0) {

            ((TextView) mView.findViewById(R.id.number_of_matches)).setText("0");

            ((TextView) mView.findViewById(R.id.gears)).setText("N/A");

            ((TextView) mView.findViewById(R.id.climbing)).setText("N/A");

            ((TextView) mView.findViewById(R.id.fuel)).setText("N/A\nN/A");

            ((TextView) mView.findViewById(R.id.pilot)).setText("N/A");

            ((TextView) mView.findViewById(R.id.defense)).setText("N/A");

            ((TextView) mView.findViewById(R.id.driver_control)).setText("N/A");

            ((TextView) mView.findViewById(R.id.torque)).setText("N/A");

            ((TextView) mView.findViewById(R.id.speed)).setText("N/A");
        } else {
            ((TextView) mView.findViewById(R.id.number_of_matches)).setText(String.valueOf(mTeam.completed_matches.size()));

            ((TextView) mView.findViewById(R.id.gears)).setText(String.format("A: %.2f T: %.2f", mTeam.calc.auto_gears.total.placed.average, mTeam.calc.teleop_gears.total.placed.average));

            ((TextView) mView.findViewById(R.id.climbing)).setText(String.format("%02.2f%% (%.2f s)", mTeam.calc.climb.success_percentage * 100.0, mTeam.calc.climb.time.average));

            ((TextView) mView.findViewById(R.id.fuel)).setText(String.format("A: H - %.2f L - %.2f\nT: H - %.2f L - %.2f",
                    mTeam.calc.auto_shooting.high.made.average,
                    mTeam.calc.auto_shooting.low.made.average,
                    mTeam.calc.teleop_shooting.high.made.average,
                    mTeam.calc.teleop_shooting.low.made.average));

            ((TextView) mView.findViewById(R.id.pilot)).setText(String.format("%.2f / %.2f / %.2f", mTeam.pilot.rating.average, mTeam.pilot.lifts.average, mTeam.pilot.drops.average));

            ((TextView) mView.findViewById(R.id.defense)).setText(String.valueOf(mTeam.qualitative.defense.rank));

            ((TextView) mView.findViewById(R.id.driver_control)).setText(String.valueOf(mTeam.qualitative.control.rank));

            ((TextView) mView.findViewById(R.id.torque)).setText(String.valueOf(mTeam.qualitative.torque.rank));

            ((TextView) mView.findViewById(R.id.speed)).setText(String.valueOf(mTeam.qualitative.speed.rank));
        }

        mView.findViewById(R.id.details).setVisibility(View.VISIBLE);

        mView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TeamView.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeam.team_number);
                startActivity(intent);
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_team, container, false);

        return mView;
    }

}
