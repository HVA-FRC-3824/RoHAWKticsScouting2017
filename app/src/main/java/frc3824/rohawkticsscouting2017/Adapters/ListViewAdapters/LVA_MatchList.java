package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.DriveTeamFeedback;
import frc3824.rohawkticsscouting2017.Activities.MatchScouting;
import frc3824.rohawkticsscouting2017.Activities.MatchView;
import frc3824.rohawkticsscouting2017.Activities.SuperScouting;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 10/10/16
 *
 *
 */
public class LVA_MatchList extends ArrayAdapter<Integer>{
    private final static String TAG = "LVA_MatchList";

    private ArrayList<Integer> mMatchNumbers;
    private Context mContext;
    private Database mDatabase;
    private MatchListType mMatchListType;
    private String mAllianceColor;
    private int mAllianceNumber;

    public enum MatchListType{
        MATCH_SCOUT,
        SUPER_SCOUT,
        MATCH_VIEW,
        DRIVE_TEAM_FEEDBACK
    }

    public LVA_MatchList(Context context, ArrayList<Integer> objects, MatchListType mlt) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mMatchNumbers = objects;
        mContext = context;
        mMatchListType = mlt;
        mDatabase = Database.getInstance();
    }



    public void setAlliance(String color, int number){
        mAllianceColor = color;
        mAllianceNumber = number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView txt1 = (TextView) convertView.findViewById(android.R.id.text1);
        txt1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        final int match_number = mMatchNumbers.get(position);

        switch (mMatchListType){
            case MATCH_SCOUT:
                // Practice Match
                if(match_number == -1){
                    convertView.setBackgroundColor(Color.GRAY);
                    txt1.setText("Practice Match");
                } else {
                    Match match = mDatabase.getMatch(match_number);
                    int team_number = -1;
                    if(mAllianceColor.equals(Constants.Alliance_Colors.BLUE)) {
                        convertView.setBackgroundColor(Color.BLUE);
                        txt1.setTextColor(Color.WHITE);
                        team_number = match.teams.get(mAllianceNumber - 1);
                    } else {
                        convertView.setBackgroundColor(Color.RED);
                        team_number = match.teams.get(mAllianceNumber + 2);
                    }
                    txt1.setText(String.format("Match: %d - Team: %d", match_number, team_number));
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case SUPER_SCOUT:
                // Practice Match
                if(match_number == -1){
                    convertView.setBackgroundColor(Color.GRAY);
                    txt1.setText("Practice Match");
                } else {
                    convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.navy_blue));
                    txt1.setTextColor(Color.WHITE);
                    txt1.setText(String.format("Match: %d", match_number));
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, SuperScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case MATCH_VIEW:
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.navy_blue));
                txt1.setTextColor(Color.WHITE);
                txt1.setText(String.format("Match: %d", match_number));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MatchView.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case DRIVE_TEAM_FEEDBACK:
                Match match = mDatabase.getMatch(match_number);
                try {
                    if (match.isBlue(Constants.OUR_TEAM_NUMBER)) {
                        convertView.setBackgroundColor(Color.BLUE);
                    } else {
                        convertView.setBackgroundColor(Color.RED);
                    }
                } catch(Exception e){
                    Log.e(TAG, e.getMessage());
                    convertView.setBackgroundColor(Color.GRAY);
                }
                txt1.setTextColor(Color.WHITE);
                txt1.setText(String.format("Match: %d", match_number));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DriveTeamFeedback.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

        return convertView;
    }
}
