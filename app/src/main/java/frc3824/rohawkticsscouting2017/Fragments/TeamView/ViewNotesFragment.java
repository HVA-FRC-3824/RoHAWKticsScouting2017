package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatch;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamInMatch;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/17/16
 *
 * The fragment that displays all the notes (except pit notes) for a specific team
 */
public class ViewNotesFragment extends Fragment{

    private final static String TAG = "ViewNotesFragment";

    private int mTeamNumber;

    public ViewNotesFragment(){}

    public void setTeamNumber(int teamNumber)
    {
        mTeamNumber = teamNumber;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_team_view_notes, container, false);

        TextView matchNotes = (TextView)view.findViewById(R.id.match_notes);
        TextView superNotes = (TextView)view.findViewById(R.id.super_notes);
        TextView driveTeamFeedback = (TextView)view.findViewById(R.id.drive_team_feedback);

        Database database = Database.getInstance();

        Team team = database.getTeam(mTeamNumber);
        String matchNotesText = "";
        String superNotesText = "";
        for(int matchNumber : team.match_numbers)
        {
            TeamInMatch tim = database.getTeamInMatch(matchNumber, mTeamNumber);
            if(tim != null && tim.notes != null && tim.notes != "") {
                matchNotesText += String.format("Match %d:\n\t%s\n", matchNumber, tim.notes);
            }
            SuperMatch sm = database.getSuperMatch(matchNumber);
            if(sm != null && sm.notes != null && sm.notes != "") {
                superNotesText += String.format("Match %d:\n\t%s\n", matchNumber, sm.notes);
            }
        }
        matchNotes.setText(matchNotesText);
        superNotes.setText(superNotesText);

        //TODO: fill in drive team feedback

        return view;
    }

}
