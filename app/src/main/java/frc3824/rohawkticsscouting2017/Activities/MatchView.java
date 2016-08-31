package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Fragments.MatchView.MatchViewPredictionFragment;
import frc3824.rohawkticsscouting2017.Fragments.MatchView.MatchViewTeamFragment;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/18/16
 *
 *
 */
public class MatchView extends Activity {

    private final static String TAG = "MatchView";

    private int mMatchNumber;
    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.match_view_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        mDatabase = Database.getInstance();

        FragmentManager fm = getFragmentManager();

        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER, -1);
        MatchViewTeamFragment[] teamFragments = new MatchViewTeamFragment[6];
        teamFragments[Constants.Match_Indices.BLUE1] = (MatchViewTeamFragment) fm.findFragmentById(R.id.blue1);
        teamFragments[Constants.Match_Indices.BLUE2] = (MatchViewTeamFragment) fm.findFragmentById(R.id.blue2);
        teamFragments[Constants.Match_Indices.BLUE3] = (MatchViewTeamFragment) fm.findFragmentById(R.id.blue3);
        teamFragments[Constants.Match_Indices.RED1] = (MatchViewTeamFragment) fm.findFragmentById(R.id.red1);
        teamFragments[Constants.Match_Indices.RED2] = (MatchViewTeamFragment) fm.findFragmentById(R.id.red2);
        teamFragments[Constants.Match_Indices.RED3] = (MatchViewTeamFragment) fm.findFragmentById(R.id.red3);

        MatchViewPredictionFragment bluePrediction = (MatchViewPredictionFragment) fm.findFragmentById(R.id.blue_prediction);
        MatchViewPredictionFragment redPrediction = (MatchViewPredictionFragment) fm.findFragmentById(R.id.red_prediction);

        if(mMatchNumber == -1)
        {
            int number = extras.getInt(Constants.Intent_Extras.BLUE1,-1);
            Team blueTeam1 = mDatabase.getTeam(number);
            teamFragments[Constants.Match_Indices.BLUE1].setTeam(blueTeam1);

            number = extras.getInt(Constants.Intent_Extras.BLUE2,-1);
            Team blueTeam2 = mDatabase.getTeam(number);
            teamFragments[Constants.Match_Indices.BLUE2].setTeam(blueTeam2);

            number = extras.getInt(Constants.Intent_Extras.BLUE3,-1);
            Team blueTeam3 = mDatabase.getTeam(number);
            teamFragments[Constants.Match_Indices.BLUE3].setTeam(blueTeam3);

            bluePrediction.setTeams(blueTeam1, blueTeam2, blueTeam3);

            number = extras.getInt(Constants.Intent_Extras.RED1,-1);
            Team redTeam1 = mDatabase.getTeam(number);
            teamFragments[Constants.Match_Indices.RED1].setTeam(redTeam1);

            number = extras.getInt(Constants.Intent_Extras.RED2,-1);
            Team redTeam2 = mDatabase.getTeam(number);
            teamFragments[Constants.Match_Indices.RED2].setTeam(redTeam2);

            number = extras.getInt(Constants.Intent_Extras.RED3,-1);
            Team redTeam3 = mDatabase.getTeam(number);
            teamFragments[Constants.Match_Indices.RED3].setTeam(redTeam3);

            redPrediction.setTeams(redTeam1, redTeam2, redTeam3);
        }
        else
        {
            setTitle("Match Number: " + mMatchNumber);
            Match match = mDatabase.getMatch(mMatchNumber);
            Team[] blueAlliance = new Team[3];
            Team[] redAlliance = new Team[3];
            for(int i = 0; i < match.teams.size(); i++)
            {
                Team team = mDatabase.getTeam(match.teams.get(i));
                teamFragments[i].setTeam(team);
                if(i < 3)
                {
                    blueAlliance[i] = team;
                }
                else
                {
                    redAlliance[i - 3] = team;
                }
            }

            bluePrediction.setTeams(blueAlliance[0], blueAlliance[1], blueAlliance[2]);
            redPrediction.setTeams(redAlliance[0], redAlliance[1], redAlliance[2]);

        }
    }

    /**
     *  Creates the overflow menu for the toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_overflow, menu);
        if (mMatchNumber == -1 || mMatchNumber == 1) {
            menu.removeItem(R.id.previous_match);
        }
        if (mMatchNumber == -1 || mMatchNumber == mDatabase.getNumberOfMatches()) {
            menu.removeItem(R.id.next_match);
        }
        menu.removeItem(R.id.save);
        return true;
    }

    /**
     *  Implements the overflow menu actions
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(this, Home.class);
                startActivity(intent);
                break;
            case R.id.match_list:
                intent = new Intent(this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.MATCH_VIEWING);
                startActivity(intent);
                break;
            case R.id.previous_match:
                intent = new Intent(this, MatchView.class);
                intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber - 1);
                startActivity(intent);
                break;
            case R.id.next_match:
                intent = new Intent(this, MatchView.class);
                intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber + 1);
                startActivity(intent);
                break;
            default:
                assert false;
        }
        return true;
    }

}
