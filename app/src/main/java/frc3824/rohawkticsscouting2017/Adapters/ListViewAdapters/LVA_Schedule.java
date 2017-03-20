package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 *         Created: 8/20/16
 */
public class LVA_Schedule extends ArrayAdapter<Integer> {

    private final static String TAG = "LVA_Schedule";

    private ArrayList<Integer> mMatchNumbers;
    private Database mDatabase;

    public LVA_Schedule(Context context, ArrayList<Integer> objects) {
        super(context, R.layout.list_item_schedule, objects);
        mMatchNumbers = objects;
        mDatabase = Database.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_schedule, null);
        }

        final int matchNumber = mMatchNumbers.get(position);

        if(position == 0) {
            ((TextView) convertView.findViewById(R.id.match_number)).setText(String.format("Match Number", matchNumber));
            TextView textView = (TextView) convertView.findViewById(R.id.blue1);
            textView.setText("Blue 1");
            textView.setBackgroundColor(Color.BLUE);
            textView.setTextColor(Color.WHITE);

            textView = (TextView) convertView.findViewById(R.id.blue2);
            textView.setText("Blue 2");
            textView.setBackgroundColor(Color.BLUE);
            textView.setTextColor(Color.WHITE);


            textView = (TextView) convertView.findViewById(R.id.blue3);
            textView.setText("Blue 3");
            textView.setBackgroundColor(Color.BLUE);
            textView.setTextColor(Color.WHITE);


            textView = (TextView) convertView.findViewById(R.id.red1);
            textView.setText("Red 1");
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);


            textView = (TextView) convertView.findViewById(R.id.red2);
            textView.setText("Red 2");
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);


            textView = (TextView) convertView.findViewById(R.id.red3);
            textView.setText("Red 3");
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);

        } else {

            ((TextView) convertView.findViewById(R.id.match_number)).setText(String.format("%d", matchNumber));

            Match match = mDatabase.getMatch(matchNumber);

            TextView[] teamTextViews = new TextView[6];
            teamTextViews[Constants.Match_Indices.BLUE1] = (TextView) convertView.findViewById(R.id.blue1);
            teamTextViews[Constants.Match_Indices.BLUE2] = (TextView) convertView.findViewById(R.id.blue2);
            teamTextViews[Constants.Match_Indices.BLUE3] = (TextView) convertView.findViewById(R.id.blue3);
            teamTextViews[Constants.Match_Indices.RED1] = (TextView) convertView.findViewById(R.id.red1);
            teamTextViews[Constants.Match_Indices.RED2] = (TextView) convertView.findViewById(R.id.red2);
            teamTextViews[Constants.Match_Indices.RED3] = (TextView) convertView.findViewById(R.id.red3);

            for (int i = 0; i < match.team_numbers.size(); i++) {
                int team_number = match.team_numbers.get(i);
                teamTextViews[i].setText(String.valueOf(team_number));
                if (team_number == Constants.OUR_TEAM_NUMBER) {
                    teamTextViews[i].setBackgroundColor(Color.YELLOW);
                    teamTextViews[i].setTextColor(Color.BLACK);
                } else if(i < 3) {
                    teamTextViews[i].setBackgroundColor(Color.BLUE);
                    teamTextViews[i].setTextColor(Color.WHITE);
                } else {
                    teamTextViews[i].setBackgroundColor(Color.RED);
                    teamTextViews[i].setTextColor(Color.WHITE);
                }
            }
        }
        return convertView;
    }
}
