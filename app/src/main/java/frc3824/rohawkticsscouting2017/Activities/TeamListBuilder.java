package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_TeamListBuilder;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_TeamListBuilderDialog;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 9/25/16
 *
 * Activity to modify the team list
 */
public class TeamListBuilder extends Activity implements View.OnClickListener{

    private final static String TAG = "TeamListBuilder";

    private ArrayList<TeamLogistics> mTeamLogisticses;
    private LayoutInflater mLayoutInflator;
    private LVA_TeamListBuilder mLVA;
    private Database mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list_builder);

        mDatabase = Database.getInstance();

        View add = findViewById(R.id.add);
        add.setOnClickListener(this);

        mLayoutInflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Team");
        View dialogView = mLayoutInflator.inflate(R.layout.dialog_team_list_builder, null);
        final EditText teamNumberEdit = (EditText) dialogView.findViewById(R.id.team_number);
        final EditText nicknameEdit = (EditText) dialogView.findViewById(R.id.nickname);
        final ListView lv = (ListView) dialogView.findViewById(R.id.match_list);
        final ArrayList<Integer> match_numbers = new ArrayList<>();
        final LVA_TeamListBuilderDialog lva = new LVA_TeamListBuilderDialog(this, match_numbers);
        lv.setAdapter(lva);
        final EditText addMatchNumberEdit = (EditText)dialogView.findViewById(R.id.match_number);
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                match_numbers.add(Integer.parseInt(addMatchNumberEdit.getText().toString()));
                lva.notifyDataSetChanged();
            }
        });
        builder.setView(dialogView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int team_number = Integer.parseInt(teamNumberEdit.getText().toString());
                String nickname = nicknameEdit.getText().toString();

                TeamLogistics teamLogistics = new TeamLogistics();
                teamLogistics.team_number = team_number;
                teamLogistics.nickname = nickname;
                teamLogistics.match_numbers = match_numbers;
                mDatabase.setTID(teamLogistics);
                mLVA.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
}
