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
 *
 */
public class MatchList extends Activity {

    private final static String TAG = "MatchList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        Bundle extras = getIntent().getExtras();
        final String nextPage = extras.getString(Constants.Intent_Extras.NEXT_PAGE);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventId = sharedPreferences.getString(Constants.Settings.EVENT_ID, "");
        String allianceColor = sharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
        int allianceNumber = sharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);

        Database database = Database.getInstance(eventId);

        int numberOfMatches = database.getNumberOfMatches();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.match_list);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 4, 4, 4);

        // Set up practice match button for match and super scouting
        if (nextPage.equals(Constants.Intent_Extras.MATCH_SCOUTING)) {
            Button button = new Button(this);
            button.setLayoutParams(lp);
            button.setText("Practice Match");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (nextPage.equals(Constants.Intent_Extras.MATCH_SCOUTING)) {
                        intent = new Intent(MatchList.this, MatchScouting.class);
                    }
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                    startActivity(intent);
                }
            });
            linearLayout.addView(button);
        }

        if (nextPage == Constants.Intent_Extras.MATCH_SCOUTING) {

            for (int match_number = 1; match_number <= numberOfMatches; match_number++) {
                int team_number = -1;
                Match match = database.getMatch(match_number);
                if (allianceColor == Constants.Alliance_Colors.BLUE) {
                    switch (allianceNumber) {
                        case 1:
                            team_number = match.blue1;
                            break;
                        case 2:
                            team_number = match.blue2;
                            break;
                        case 3:
                            team_number = match.blue3;
                            break;
                        default:
                            assert false;
                    }
                } else {
                    switch (allianceNumber) {
                        case 1:
                            team_number = match.red1;
                            break;
                        case 2:
                            team_number = match.red2;
                            break;
                        case 3:
                            team_number = match.red3;
                            break;
                        default:
                            assert false;
                    }
                }

                final int match_number_final = match_number;
                final int team_number_final = team_number;
                Button button = new Button(this);
                button.setLayoutParams(lp);
                button.setText(String.format("Match %d Team %d", match_number, team_number));
                if(allianceColor == Constants.Alliance_Colors.BLUE) {
                    button.setBackgroundColor(Color.BLUE);
                } else {
                    button.setBackgroundColor(Color.RED);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MatchList.this, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number_final);
                        intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, team_number_final);
                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);
            }
        }
    }
}
