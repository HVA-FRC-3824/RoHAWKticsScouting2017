package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Gear;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchPilotData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchTeamPilotData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
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

    private TeamMatchData mTmd;
    private boolean mSurrogate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_individual_match_data, container, false);

        //Remove Asterisk from start position header
        ((TextView)view.findViewById(R.id.auto_header).findViewById(R.id.start_position)).setText("Start Position");

        if(mSurrogate){
            view.findViewById(R.id.surrogate_text).setVisibility(View.VISIBLE);
        }

        // Points
        View points = view.findViewById(R.id.points_body);

        if(mTmd == null)
        {
            return view;
        }

        int auto_points = (mTmd.auto_high_goal_made + mTmd.auto_high_goal_correction) +
                          (mTmd.auto_low_goal_made + mTmd.auto_low_goal_correction) / 3;
        int auto_gears = 0;
        for(Gear gear: mTmd.auto_gears)
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

        int teleop_points = (mTmd.teleop_high_goal_made + mTmd.teleop_high_goal_correction) / 3 +
                            (mTmd.teleop_low_goal_made + mTmd.teleop_low_goal_correction) / 9;


        int teleop_gears = 0;
        for(Gear gear: mTmd.teleop_gears)
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

        int endgame_points = mTmd.endgame_climb == Constants.Match_Scouting.Endgame.CLIMB_OPTIONS.SUCCESSFUL? 50 : 0;

        int total_points = auto_points + teleop_points + endgame_points;

        ((TextView)points.findViewById(R.id.total)).setText(String.format("%d", total_points));
        ((TextView)points.findViewById(R.id.auto)).setText(String.format("%d", auto_points));
        ((TextView)points.findViewById(R.id.teleop)).setText(String.format("%d", teleop_points));

        ((TextView)points.findViewById(R.id.endgame)).setText(String.format("%d", endgame_points));

        // Auto (auto is a keyword so the underscore is used)
        View auto_ = view.findViewById(R.id.auto_body);

        ((TextView)auto_.findViewById(R.id.start_position)).setText(mTmd.auto_start_position);
        ((TextView)auto_.findViewById(R.id.baseline)).setText(mTmd.auto_baseline ? "Yes" : "No");
        String gears = "";
        for(Gear gear : mTmd.auto_gears){
            if(gear.placed){
                switch (gear.location){
                    case Constants.Match_Scouting.Custom.Gears.FAR:
                        gears += "F";
                        break;
                    case Constants.Match_Scouting.Custom.Gears.CENTER:
                        gears += "C";
                        break;
                    case Constants.Match_Scouting.Custom.Gears.NEAR:
                        gears += "N";
                        break;
                }
            } else {
                gears += "D";
            }
        }
        ((TextView)auto_.findViewById(R.id.gears)).setText(gears);

        float percent = ((float)(mTmd.auto_high_goal_made + mTmd.auto_high_goal_correction)) /
                ((float)(mTmd.auto_high_goal_made + mTmd.auto_high_goal_correction + mTmd.auto_high_goal_missed));

        if(Float.isNaN(percent)){
            percent = 0;
        }

        ((TextView)auto_.findViewById(R.id.high_goal)).setText(String.format("%d / %d (%02.2f%%)",
                mTmd.auto_high_goal_made + mTmd.auto_high_goal_correction, mTmd.auto_high_goal_missed, percent));

        percent = ((float)(mTmd.auto_low_goal_made + mTmd.auto_low_goal_correction)) /
                ((float)(mTmd.auto_low_goal_made + mTmd.auto_low_goal_correction + mTmd.auto_low_goal_missed));

        if(Float.isNaN(percent)){
            percent = 0;
        }

        ((TextView)auto_.findViewById(R.id.low_goal)).setText(String.format("%d / %d (%02.2f%%)",
                mTmd.auto_low_goal_made + mTmd.auto_low_goal_correction, mTmd.auto_low_goal_missed, percent));
        
        ((TextView)auto_.findViewById(R.id.hoppers)).setText(String.valueOf(mTmd.auto_hoppers));

        // Teleop
        View teleop = view.findViewById(R.id.teleop_body);

        gears = "";
        for(Gear gear : mTmd.teleop_gears){
            if(gear.placed){
                switch (gear.location){
                    case Constants.Match_Scouting.Custom.Gears.FAR:
                        gears += "F";
                        break;
                    case Constants.Match_Scouting.Custom.Gears.CENTER:
                        gears += "C";
                        break;
                    case Constants.Match_Scouting.Custom.Gears.NEAR:
                        gears += "N";
                        break;
                }
            } else {
                gears += "D";
            }
        }
        ((TextView)teleop.findViewById(R.id.gears)).setText(gears);

        percent = ((float)(mTmd.teleop_high_goal_made + mTmd.teleop_high_goal_correction)) /
                ((float)(mTmd.teleop_high_goal_made + mTmd.teleop_high_goal_correction + mTmd.teleop_high_goal_missed));
        if(Float.isNaN(percent)){
            percent = 0;
        }
        ((TextView)teleop.findViewById(R.id.high_goal)).setText(String.format("%d / %d (%02.2f%%)",
                mTmd.teleop_high_goal_made + mTmd.teleop_high_goal_correction, mTmd.teleop_high_goal_missed, percent));

        percent = ((float)(mTmd.teleop_low_goal_made + mTmd.teleop_low_goal_correction)) /
                ((float)(mTmd.teleop_low_goal_made + mTmd.teleop_low_goal_correction + mTmd.teleop_low_goal_missed));
        if(Float.isNaN(percent)){
            percent = 0;
        }
        ((TextView)teleop.findViewById(R.id.low_goal)).setText(String.format("%d / %d (%02.2f%%)",
                mTmd.teleop_low_goal_made + mTmd.teleop_low_goal_correction, mTmd.teleop_low_goal_missed, percent));

        ((TextView)teleop.findViewById(R.id.hoppers)).setText(String.valueOf(mTmd.teleop_hoppers));
        ((TextView)teleop.findViewById(R.id.gears_picked_up)).setText(String.valueOf(mTmd.teleop_picked_up_gears));

        // Endgame
        View endgame = view.findViewById(R.id.endgame_body);

        ((TextView)endgame.findViewById(R.id.climb)).setText(mTmd.endgame_climb);
        ((TextView)endgame.findViewById(R.id.climb_time)).setText(mTmd.endgame_climb_time);

        //Pilot
        View pilot = view.findViewById(R.id.pilot_body);

        MatchPilotData mpd = Database.getInstance().getMatchPilotData(mTmd.match_number);
        if(mpd != null) {
            for (MatchTeamPilotData mtpd : mpd.teams) {
                if (mtpd.team_number == mTmd.team_number) {
                    ((TextView) pilot.findViewById(R.id.rating)).setText(String.valueOf(mtpd.rating.charAt(0)));
                    ((TextView) pilot.findViewById(R.id.lifts)).setText(String.valueOf(mtpd.lifts));
                    ((TextView) pilot.findViewById(R.id.drops)).setText(String.valueOf(mtpd.drops));

                    float percentage = (float) mtpd.lifts / (float) (mtpd.lifts + mtpd.drops);
                    if (Float.isNaN(percentage)) {
                        percentage = 0.0f;
                    }

                    ((TextView) pilot.findViewById(R.id.lift_percentage)).setText(String.format("%02.2f%%", percentage * 100));
                    break;
                }
            }
        } else {
            ((TextView) pilot.findViewById(R.id.rating)).setText("N/A");
            ((TextView) pilot.findViewById(R.id.lifts)).setText("N/A");
            ((TextView) pilot.findViewById(R.id.drops)).setText("N/A");
            ((TextView) pilot.findViewById(R.id.lift_percentage)).setText("N/A");
        }

        return view;
    }

    public void setTeamMatch(int team_number, int position){

        TeamLogistics tl = Database.getInstance().getTeamLogistics(team_number);
        int match_number = tl.match_numbers.get(position);
        mTmd = Database.getInstance().getTeamMatchData(match_number, team_number);
        mSurrogate = match_number == tl.surrogate_match_number;
    }
}
