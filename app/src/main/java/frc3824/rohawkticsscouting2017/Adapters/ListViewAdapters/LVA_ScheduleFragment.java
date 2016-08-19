package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/17/16
 *
 *
 */
public class LVA_ScheduleFragment extends ArrayAdapter<Integer> {

    private final static String TAG = "LVA_ScheduleFragment";

    private Context mContext;
    private List<Integer> mMatchNumbers;
    private Database mDatabase;
    private int mTeamNumber;


    public LVA_ScheduleFragment(Context context, List<Integer> objects, int teamNumber) {
        super(context, R.layout.list_item_fragment_schedule, objects);
        mMatchNumbers = objects;
        mContext = context;
        mDatabase = Database.getInstance();
        mTeamNumber = teamNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_fragment_schedule, null);
        }

        final int matchNumber = mMatchNumbers.get(position);

        TextView matchNumberTextView = (TextView)convertView.findViewById(R.id.match_number);
        matchNumberTextView.setText(String.format("Match: %d", matchNumber));

        Match match = mDatabase.getMatch(matchNumber);

        TextView[] teamTextViews = new TextView[6];
        teamTextViews[Constants.Match_Indices.BLUE1] = (TextView)convertView.findViewById(R.id.blue_1);
        teamTextViews[Constants.Match_Indices.BLUE2] = (TextView)convertView.findViewById(R.id.blue_2);
        teamTextViews[Constants.Match_Indices.BLUE3] = (TextView)convertView.findViewById(R.id.blue_3);
        teamTextViews[Constants.Match_Indices.RED1] = (TextView)convertView.findViewById(R.id.red_1);
        teamTextViews[Constants.Match_Indices.RED2] = (TextView)convertView.findViewById(R.id.red_2);
        teamTextViews[Constants.Match_Indices.RED3] = (TextView)convertView.findViewById(R.id.red_3);

        for(int i = 0; i < match.teams.size(); i++)
        {
            teamTextViews[i].setText(String.valueOf(match.teams.get(i)));
            if(mTeamNumber == match.teams.get(i))
            {
                teamTextViews[i].setBackgroundColor(Color.YELLOW);
            }
            else
            {
                teamTextViews[i].setBackgroundColor(Color.TRANSPARENT);
            }
        }

        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MatchView.class);
                intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, matchNumber);
                mContext.startActivity(intent);
            }
        });
        */
        return convertView;
    }
}
