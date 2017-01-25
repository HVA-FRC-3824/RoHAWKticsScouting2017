package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 9/26/16
 *
 *
 */
public class LVA_TeamListBuilder extends ArrayAdapter<TeamLogistics> {

    private final static String TAG = "LVA_TeamListBuilder";

    private ArrayList<TeamLogistics> mTeams;
    private Context mContext;
    private LayoutInflater mLayoutInflator;
    private Database mDatabase;

    public LVA_TeamListBuilder(Context context, ArrayList<TeamLogistics> objects) {
        super(context, R.layout.list_item_team_list_builder, objects);
        mTeams = objects;
        mContext = context;
        mLayoutInflator = LayoutInflater.from(mContext);
        mDatabase = Database.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null)
        {
            convertView = mLayoutInflator.inflate(R.layout.list_item_team_list_builder, null);
        }

        final TeamLogistics teamLogistics = mTeams.get(position);

        TextView team_number = (TextView)convertView.findViewById(R.id.team_number);
        team_number.setText(String.valueOf(teamLogistics.team_number));

        TextView nickname = (TextView)convertView.findViewById(R.id.nickname);
        nickname.setText(teamLogistics.nickname);

        ImageButton edit = (ImageButton)convertView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle(String.format("Edit Team Number: %d", teamLogistics.team_number));
                View dialogView = mLayoutInflator.inflate(R.layout.dialog_team_list_builder, null);
                final EditText teamNumberEdit = (EditText)dialogView.findViewById(R.id.team_number);
                teamNumberEdit.setText(String.valueOf(teamLogistics.team_number));
                final EditText nicknameEdit = (EditText)dialogView.findViewById(R.id.nickname);
                nicknameEdit.setText(teamLogistics.nickname);

                builder.setView(dialogView);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        teamLogistics.team_number = Integer.parseInt(teamNumberEdit.getText().toString());
                        teamLogistics.nickname = nicknameEdit.getText().toString();
                        mDatabase.setTeamLogistics(teamLogistics);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.removeTeamLogistics(teamLogistics.team_number);
                mTeams.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
