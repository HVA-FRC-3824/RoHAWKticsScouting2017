package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        Bundle extras = getIntent().getExtras();
        mNextPage = extras.getString(Constants.Intent_Extras.NEXT_PAGE);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        mDatabase = Database.getInstance();

        int numberOfMatches = mDatabase.getNumberOfMatches();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.match_list);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 4, 4, 4);

        // Set up practice match button for match and super scouting
        Button button = null;
        switch (mNextPage)
        {
            case Constants.Intent_Extras.MATCH_SCOUTING:
            case Constants.Intent_Extras.SUPER_SCOUTING:
                button = new Button(this);
                button.setLayoutParams(lp);
                button.setText("Practice Match");
                button.setOnClickListener(this);
                button.setId(0);
                linearLayout.addView(button);
                break;
            case Constants.Intent_Extras.MATCH_VIEWING:
                button = new Button(this);
                button.setLayoutParams(lp);
                button.setText("Custom Match");
                button.setOnClickListener(this);
                button.setId(0);
                linearLayout.addView(button);
                break;
        }


        for (int match_number = 1; match_number <= numberOfMatches; match_number++) {
            Match match = mDatabase.getMatch(match_number);
            button = new Button(this);
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
        switch (mNextPage)
        {
            case Constants.Intent_Extras.MATCH_SCOUTING:
                goToMatchScouting(match_number);
                break;
            case Constants.Intent_Extras.SUPER_SCOUTING:
                goToSuperScouting(match_number);
                break;
            case Constants.Intent_Extras.MATCH_VIEWING:
                goToMatchViewing(match_number);
                break;
            default:
                assert false;
        }
    }

    private void goToMatchScouting(int match_number)
    {
        Intent intent = new Intent(this, MatchScouting.class);
        if(match_number == 0)
        {
            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
        }
        else
        {
            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
        }
        startActivity(intent);
    }

    private void goToSuperScouting(int match_number)
    {
        Intent intent = new Intent(this, SuperScouting.class);
        if(match_number == 0)
        {
            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
        }
        else
        {
            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
        }
        startActivity(intent);
    }

    private void goToMatchViewing(int match_number)
    {
        Intent intent = new Intent(this, MatchView.class);
        if(match_number == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Custom Match");

            ArrayList<Integer> teamNumbers = mDatabase.getTeamNumbers();

            View matchTeamsView = this.getLayoutInflater().inflate(R.layout.dialog_custom_match, null);

            //TODO: figure out scrolling so keyboard doesn't block

            final AutoCompleteTextView blue1 = (AutoCompleteTextView)matchTeamsView.findViewById(R.id.blue1);
            blue1.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamNumbers));

            final AutoCompleteTextView blue2 = (AutoCompleteTextView)matchTeamsView.findViewById(R.id.blue2);
            blue2.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamNumbers));

            final AutoCompleteTextView blue3 = (AutoCompleteTextView)matchTeamsView.findViewById(R.id.blue3);
            blue3.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamNumbers));

            final AutoCompleteTextView red1 = (AutoCompleteTextView)matchTeamsView.findViewById(R.id.red1);
            red1.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamNumbers));

            final AutoCompleteTextView red2 = (AutoCompleteTextView)matchTeamsView.findViewById(R.id.red2);
            red2.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamNumbers));

            final AutoCompleteTextView red3 = (AutoCompleteTextView)matchTeamsView.findViewById(R.id.red3);
            red3.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamNumbers));

            final TextView error = (TextView)matchTeamsView.findViewById(R.id.error);

            builder.setView(matchTeamsView);

            builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent(MatchList.this, MatchView.class);

                    String blue1_text = blue1.getText().toString();
                    String blue2_text = blue2.getText().toString();
                    String blue3_text = blue3.getText().toString();
                    String red1_text = red1.getText().toString();
                    String red2_text = red2.getText().toString();
                    String red3_text = red3.getText().toString();
                    try {

                        int blue1_number = Integer.parseInt(blue1_text);
                        int blue2_number = Integer.parseInt(blue2_text);
                        int blue3_number = Integer.parseInt(blue3_text);
                        int red1_number = Integer.parseInt(red1_text);
                        int red2_number = Integer.parseInt(red2_text);
                        int red3_number = Integer.parseInt(red3_text);

                        intent1.putExtra(Constants.Intent_Extras.BLUE1, blue1_number);
                        intent1.putExtra(Constants.Intent_Extras.BLUE2, blue2_number);
                        intent1.putExtra(Constants.Intent_Extras.BLUE3, blue3_number);
                        intent1.putExtra(Constants.Intent_Extras.RED1, red1_number);
                        intent1.putExtra(Constants.Intent_Extras.RED2, red2_number);
                        intent1.putExtra(Constants.Intent_Extras.RED3, red3_number);
                        error.setVisibility(View.GONE);
                        startActivity(intent1);
                    }
                    catch (NumberFormatException e)
                    {
                        Log.e(TAG, "One of the lines in not parsable");
                        error.setVisibility(View.VISIBLE);
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.show();

        } else {
            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, Home.class));
    }
}
