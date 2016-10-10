package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.PitScouting;
import frc3824.rohawkticsscouting2017.Activities.TeamView;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 10/10/16
 *
 *
 */
public class LVA_TeamList extends ArrayAdapter<Integer>{

    private final static String TAG = "LVA_TeamList";

    private ArrayList<Integer> mTeamNumbers;
    private Context mContext;
    private Database mDatabase;
    private TeamListType mTeamListType;

    public enum TeamListType{
        PIT_SCOUT,
        TEAM_VIEW
    }

    public LVA_TeamList(Context context, ArrayList<Integer> objects, TeamListType tlt) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mTeamNumbers = objects;
        mContext = context;
        mTeamListType = tlt;
        mDatabase = Database.getInstance();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView txt1 = (TextView) convertView.findViewById(android.R.id.text1);
        txt1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        final int team_number = mTeamNumbers.get(position);
        txt1.setText(String.format("Team: %d", team_number));

        switch (mTeamListType){
            case PIT_SCOUT:
                if(mDatabase.getTPD(team_number).pit_scouted) {
                    convertView.setBackgroundColor(Color.GREEN);
                } else {
                    convertView.setBackgroundColor(Color.RED);
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, PitScouting.class);
                        intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, team_number);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case TEAM_VIEW:
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.navy_blue));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, TeamView.class);
                        intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, team_number);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

        return convertView;
    }
}
