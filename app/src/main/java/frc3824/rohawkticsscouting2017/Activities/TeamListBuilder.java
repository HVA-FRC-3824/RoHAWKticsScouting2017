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

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_TeamListBuilder;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TID;
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

    private ArrayList<TID> mTIDs;
    private LayoutInflater mLayoutInflator;
    private LVA_TeamListBuilder mLVA;
    private Database mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
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
        final EditText teamNumberEdit = (EditText)findViewById(R.id.team_number);
        final EditText nicknameEdit = (EditText)findViewById(R.id.nickname);
        builder.setView(dialogView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int team_number = Integer.parseInt(teamNumberEdit.getText().toString());
                String nickname = nicknameEdit.getText().toString();

                TID tid = new TID();
                tid.team_number = team_number;
                tid.nickname = nickname;
                mDatabase.setTID(tid);
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
