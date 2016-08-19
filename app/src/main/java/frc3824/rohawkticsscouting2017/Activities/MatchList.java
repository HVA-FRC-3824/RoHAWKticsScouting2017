package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 * List of matches which forward to match scouting, super scouting, or match view
 */
public class MatchList extends Activity implements View.OnClickListener{

    private final static String TAG = "MatchList";

    private String mNextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        Bundle extras = getIntent().getExtras();
        mNextPage = extras.getString(Constants.Intent_Extras.NEXT_PAGE);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventKey = sharedPreferences.getString(Constants.Settings.EVENT_KEY, "");

        Database database = Database.getInstance(eventKey);

        int numberOfMatches = database.getNumberOfMatches();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.match_list);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 4, 4, 4);

        // Set up practice match button for match and super scouting
        if (mNextPage.equals(Constants.Intent_Extras.MATCH_SCOUTING) || mNextPage.equals(Constants.Intent_Extras.SUPER_SCOUTING)) {
            Button button = new Button(this);
            button.setLayoutParams(lp);
            button.setText("Practice Match");
            button.setOnClickListener(this);
            button.setId(0);
            linearLayout.addView(button);
        } else if(mNextPage.equals(Constants.Intent_Extras.MATCH_VIEWING))
        {
            Button button = new Button(this);
            button.setLayoutParams(lp);
            button.setText("Custom Match");
            button.setOnClickListener(this);
            button.setId(0);
            linearLayout.addView(button);
        }

        for (int match_number = 1; match_number <= numberOfMatches; match_number++) {
            Match match = database.getMatch(match_number);
            Button button = new Button(this);
            button.setLayoutParams(lp);

            switch (mNextPage) {
                // If Match Scouting put the match and team number on the button
                // Also it color is set based on the alliance color
                case Constants.Intent_Extras.MATCH_SCOUTING:
                    int team_number = -1;
                    String allianceColor = sharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
                    int allianceNumber = sharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
                    if (allianceColor == Constants.Alliance_Colors.BLUE) {
                        team_number = match.teams.get(allianceNumber - 1);
                    } else {
                        team_number = match.teams.get(allianceNumber + 2);
                    }

                    button.setText(String.format("Match: %d - Team: %d", match_number, team_number));
                    if (allianceColor == Constants.Alliance_Colors.BLUE) {
                        button.setBackgroundColor(Color.BLUE);
                    } else {
                        button.setBackgroundColor(Color.RED);
                    }
                    break;
                case Constants.Intent_Extras.SUPER_SCOUTING:
                case Constants.Intent_Extras.MATCH_VIEWING:
                    button.setText(String.format("Match: %d", match_number));
                    break;
            }
            button.setOnClickListener(this);
            button.setId(match_number);
            linearLayout.addView(button);
        }
    }

    @Override
    public void onClick(View view) {
        int match_number = view.getId();
        Intent intent;
        switch (mNextPage)
        {
            case Constants.Intent_Extras.MATCH_SCOUTING:
                intent = new Intent(this, MatchScouting.class);
                if(match_number == 0)
                {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                }
                else
                {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                }
                startActivity(intent);
                break;
            case Constants.Intent_Extras.SUPER_SCOUTING:
                intent = new Intent(this, SuperScouting.class);
                if(match_number == 0)
                {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                }
                else
                {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                }
                startActivity(intent);
                break;
            case Constants.Intent_Extras.MATCH_VIEWING:
                intent = new Intent(this, MatchView.class);
                if(match_number == 0)
                {
                    //TODO: setup dialogbox to collect 6 team numbers and pass those to match view
                }
                else {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                }
                startActivity(intent);
                break;
            default:
                assert false;
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, Home.class));
    }
}
