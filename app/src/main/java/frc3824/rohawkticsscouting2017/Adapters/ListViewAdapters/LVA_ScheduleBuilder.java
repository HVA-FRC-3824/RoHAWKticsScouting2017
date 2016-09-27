package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 *         Created: 9/25/16
 */
public class LVA_ScheduleBuilder extends ArrayAdapter<Match> {

    private final static String TAG = "LVA_ScheduleBuilder";

    private ArrayList<Match> mMatches;
    private Context mContext;
    private LayoutInflater mLayoutInflator;
    private Database mDatabase;

    public LVA_ScheduleBuilder(Context context, ArrayList<Match> objects) {
        super(context, R.layout.list_item_schedule_builder, objects);
        mMatches = objects;
        mContext = context;
        mLayoutInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatabase = Database.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflator.inflate(R.layout.list_item_schedule_builder, null);
        }

        final Match match = mMatches.get(position);

        final TextView match_number = (TextView)convertView.findViewById(R.id.match_number);
        match_number.setText(String.valueOf(position + 1));
        match.match_number = position + 1;

        final TextView blue1 = (TextView)convertView.findViewById(R.id.blue1);
        blue1.setText(String.valueOf(match.teams.get(Constants.Match_Indices.BLUE1)));

        final TextView blue2 = (TextView)convertView.findViewById(R.id.blue2);
        blue2.setText(String.valueOf(match.teams.get(Constants.Match_Indices.BLUE2)));

        final TextView blue3 = (TextView)convertView.findViewById(R.id.blue3);
        blue3.setText(String.valueOf(match.teams.get(Constants.Match_Indices.BLUE3)));


        final TextView red1 = (TextView)convertView.findViewById(R.id.red1);
        red1.setText(String.valueOf(match.teams.get(Constants.Match_Indices.RED1)));

        final TextView red2 = (TextView)convertView.findViewById(R.id.red2);
        red2.setText(String.valueOf(match.teams.get(Constants.Match_Indices.RED2)));

        final TextView red3 = (TextView)convertView.findViewById(R.id.red3);
        red3.setText(String.valueOf(match.teams.get(Constants.Match_Indices.RED3)));

        final ImageButton edit = (ImageButton)convertView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(String.format("Edit Match Number %d", match.match_number));
                View dialogView = mLayoutInflator.inflate(R.layout.dialog_schedule_builder, null);
                final ArrayList<EditText> edits = new ArrayList<>();
                edits.add((EditText) dialogView.findViewById(R.id.blue1));
                edits.add((EditText) dialogView.findViewById(R.id.blue2));
                edits.add((EditText) dialogView.findViewById(R.id.blue3));
                edits.add((EditText) dialogView.findViewById(R.id.red1));
                edits.add((EditText) dialogView.findViewById(R.id.red2));
                edits.add((EditText) dialogView.findViewById(R.id.red3));

                for(int i = 0; i < 6; i++)
                {
                    edits.get(i).setText(String.valueOf(match.teams.get(i)));
                }


                builder.setView(dialogView);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Integer> teams = new ArrayList<>();
                        for(int j = 0; j < 6; j++)
                        {
                            teams.add(Integer.parseInt(edits.get(j).getText().toString()));
                        }
                        match.teams = teams;
                        mDatabase.setMatch(match);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dialogbox goes away
                    }
                });

                builder.show();
            }
        });

        final ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.removeMatch(match.match_number);
                mMatches.remove(position);
                notifyDataSetChanged();
            }
        });

        // Set textboxes to correct width
        final View cv = convertView;
        convertView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int button_widths = edit.getMeasuredWidth() + delete.getMeasuredWidth();
                int parent_width = ((View)cv.getParent()).getWidth();
                int textview_width = (parent_width - button_widths) / 7;

                match_number.setWidth(textview_width);
                blue1.setWidth(textview_width);
                blue2.setWidth(textview_width);
                blue3.setWidth(textview_width);
                red1.setWidth(textview_width);
                red2.setWidth(textview_width);
                red3.setWidth(textview_width);
            }
        });

        return convertView;
    }



}
