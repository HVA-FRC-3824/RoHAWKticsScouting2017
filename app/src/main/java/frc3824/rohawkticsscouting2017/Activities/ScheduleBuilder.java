package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_ScheduleBuilder;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Views.DragSortListView.DragSortListView;



/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Activity for modifying the schedule
 */
public class ScheduleBuilder extends Activity implements View.OnClickListener, DragSortListView.DropListener {

    private final static String TAG = "ScheduleBuilder";

    private ArrayList<Match> mMatches;
    private LayoutInflater mLayoutInflator;
    private LVA_ScheduleBuilder mLVA;
    private Database mDatabase;

    public void onCreate(Bundle savedBundleInstance)
    {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_schedule_builder);

        mDatabase = Database.getInstance();

        mMatches = new ArrayList<>(mDatabase.getSchedule().values());

        mLVA = new LVA_ScheduleBuilder(this, mMatches);

        DragSortListView dslv = (DragSortListView)findViewById(R.id.listview);
        dslv.setAdapter(mLVA);
        dslv.setDropListener(this);

        View add = findViewById(R.id.add);
        add.setOnClickListener(this);

        mLayoutInflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onClick(View view) {
        final Match match = new Match();
        match.match_number = mMatches.size() + 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format("Edit Match Number %d", match.match_number));
        View dialogView = mLayoutInflator.inflate(R.layout.dialog_schedule_builder, null);
        final ArrayList<EditText> edits = new ArrayList<>();
        edits.add((EditText) dialogView.findViewById(R.id.blue1));
        edits.add((EditText) dialogView.findViewById(R.id.blue2));
        edits.add((EditText) dialogView.findViewById(R.id.blue3));
        edits.add((EditText) dialogView.findViewById(R.id.red1));
        edits.add((EditText) dialogView.findViewById(R.id.red2));
        edits.add((EditText) dialogView.findViewById(R.id.red3));

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
                mMatches.add(match);
                mDatabase.setMatch(match);
                mLVA.notifyDataSetChanged();
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

    @Override
    public void drop(int from, int to) {
        Match match = mMatches.get(from);
        mMatches.remove(from);
        mMatches.add(to, match);
        mLVA.notifyDataSetChanged();
    }
}
