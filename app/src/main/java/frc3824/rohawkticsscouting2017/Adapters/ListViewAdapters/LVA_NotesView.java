package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

import static frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView.NoteType.MATCH;

/**
 * @author frc3824
 *         Created: 9/6/16
 *         <p>
 *         List View Adapter
 */
public class LVA_NotesView extends ArrayAdapter<NoteView> {

    private final static String TAG = "LVA_NotesView";

    private ArrayList<NoteView> mNotes;
    private Map<String, String> mDisplayTags;

    public LVA_NotesView(Context context, ArrayList<NoteView> objects) {
        super(context, R.layout.list_item_note, objects);
        mNotes = objects;
        mDisplayTags = new HashMap<>();
        mDisplayTags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BLOCK_SHOTS, "Blocked Shots");
        mDisplayTags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.PINNED_ROBOT, "Pinned Robot" );
        mDisplayTags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_LOADING_STATION, "Defended Loading Station");
        mDisplayTags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_AIRSHIP, "Defended Airship");
        mDisplayTags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BROKE, "Broke");
        mDisplayTags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DUMPED_ALL_HOPPERS, "Dumped all hoppers");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_note, null);
        }

        NoteView nv = mNotes.get(position);

        TextView matchType = (TextView) convertView.findViewById(R.id.note_type);
        matchType.setText(NoteView.toString(nv.note_type));

        TextView matchNumber = (TextView) convertView.findViewById(R.id.match_number);
        matchNumber.setText(String.valueOf(nv.match_number));

        if(nv.note_type == MATCH) {
            TextView teamNumber = (TextView) convertView.findViewById(R.id.team_number);
            teamNumber.setText(String.valueOf(nv.team_number));
        }

        TextView note = (TextView) convertView.findViewById(R.id.note);
        note.setText(nv.note);

        String keywords = "Tags: ";

        if(nv.note_type == MATCH) {
            for (Map.Entry<String, Boolean> entry : nv.tags.entrySet()) {
                if (entry.getValue()) {
                    keywords += mDisplayTags.get(entry.getKey()) + ", ";
                }
            }
        }
        keywords = keywords.substring(0, keywords.length() - 2);

        if(keywords.length() == 4)
        {
            keywords = "";
        }

        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        tags.setText(keywords);

        return convertView;
    }
}
