package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 *         Created: 9/6/16
 */
public class LVA_NotesView extends ArrayAdapter<NoteView> {

    private final static String TAG = "LVA_NotesView";

    private ArrayList<NoteView> mNotes;

    public LVA_NotesView(Context context, ArrayList<NoteView> objects) {
        super(context, R.layout.list_item_note, objects);
        mNotes = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_note, null);
        }

        NoteView nv = mNotes.get(position);

        TextView matchType = (TextView)convertView.findViewById(R.id.note_type);
        matchType.setText(NoteView.toString(nv.note_type));

        TextView matchNumber = (TextView)convertView.findViewById(R.id.match_number);
        matchNumber.setText(String.valueOf(nv.match_number));

        TextView teamNumber = (TextView)convertView.findViewById(R.id.team_number);
        teamNumber.setText(String.valueOf(nv.team_number));

        TextView note = (TextView)convertView.findViewById(R.id.note);
        note.setText(nv.note);

        return convertView;
    }
}
