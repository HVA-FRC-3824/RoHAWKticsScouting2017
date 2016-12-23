package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamDTFeedback;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_notes, container, false);

        TextView matchNotes = (TextView)view.findViewById(R.id.match_notes);
        TextView superNotes = (TextView)view.findViewById(R.id.super_notes);
        TextView driveTeamFeedback = (TextView)view.findViewById(R.id.drive_team_feedback);

        Database database = Database.getInstance();

        Team team = database.getTeam(mTeamNumber);
        String matchNotesText = "";
        String superNotesText = "";
        for(int matchNumber : team.info.match_numbers) {
            TeamMatchData tm = database.getTeamMatchData(matchNumber, mTeamNumber);
            if(tm != null && tm.notes != null && tm.notes.equals("")) {
                matchNotesText += String.format("Match %d:\n\t%s\n", matchNumber, tm.notes);
            }
            SuperMatchData sm = database.getSMD(matchNumber);
            if(sm != null && sm.notes != null && sm.notes.equals("")) {
                superNotesText += String.format("Match %d:\n\t%s\n", matchNumber, sm.notes);
            }
        }

        TeamDTFeedback teamDTFeedback = database.getTDTF(mTeamNumber);
        String feedbackText = "";
        if(teamDTFeedback != null && teamDTFeedback.feedback != null) {
            for (Map.Entry<Integer, String> entry : teamDTFeedback.feedback.entrySet()) {
                if(entry.getValue() != null && !entry.getValue().equals("")) {
                    feedbackText += String.format("Match %d:\n\t%s\n", entry.getKey(), entry.getValue());
                }
            }
        }

        matchNotes.setText(matchNotesText);
        superNotes.setText(superNotesText);
        driveTeamFeedback.setText(feedbackText);

        return view;
    }

}
