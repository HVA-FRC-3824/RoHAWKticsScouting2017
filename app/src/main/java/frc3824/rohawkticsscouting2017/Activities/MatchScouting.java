package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toolbar;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_MatchScouting;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 *  @author frc3824
 *  Created: 8/11/16
 */
public class MatchScouting extends Activity {


    private final static String TAG = "MatchScouting";

    private FPA_MatchScouting fpa;

    private int team_number = -1;
    private int match_number = -1;

    private int previous_team_number = -1;
    private int next_team_number = -1;

    private boolean practice = false;

    //private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.match_scouting_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        match_number = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        if (match_number > 0)
        {
            team_number = extras.getInt(Constants.Intent_Extras.TEAM_NUMBER);
            setTitle(String.format("Match Number: %d Team Number: %d", match_number, team_number));
        }
        // match_number is -1 for practice matches
        else
        {
            practice = true;
            setTitle("Practice Match");
        }

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String alliance_color = shared_preferences.getString(Constants.Settings.ALLIANCE_COLOR, "");

        // Set up tabs and pages for different fragments of a match
        findViewById(android.R.id.content).setKeepScreenOn(true);

        ViewPager view_pager = (ViewPager)findViewById(R.id.match_scouting_view_pager);
        fpa = new FPA_MatchScouting(getFragmentManager());
        view_pager.setAdapter(fpa);
        // Set the off screen page limit to more than the number of fragments
        view_pager.setOffscreenPageLimit(5);

        /*
        TabLayout tab_layout = (TabLayout) findViewById(R.id.match_scouting_tab_layout);
        if(alliance_color.equals(Constants.Alliance_Colors.BLUE))
        {
            tab_layout.setBackground(Color.BLUE);
        }
        else
        {
            tab_layout.setBackground(Color.RED);
        }
        tab_layout.setTabTextColors(Color.WHITE, Color.GREEN);
*/
    }


}
