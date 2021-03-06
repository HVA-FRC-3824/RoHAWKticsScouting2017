package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toolbar;

import java.lang.reflect.Method;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_TeamView;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/16/16
 *
 * Activity that displays data about a specific team
 */
public class TeamView extends Activity {

    private final static String TAG = "TeamView";

    private int mTeamNumber;
    private int mTeamBefore;
    private int mTeamAfter;
    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        mTeamNumber = extras.getInt(Constants.Intent_Extras.TEAM_NUMBER);

        setTitle(String.format("Team: %d", mTeamNumber));

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        FPA_TeamView fpa = new FPA_TeamView(getFragmentManager(), mTeamNumber);

        viewPager.setAdapter(fpa);
        viewPager.setOffscreenPageLimit(fpa.getCount());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Creates the overflow menu for the toolbar. Removes previous team or next team options if
     * they do not exist.
     *
     * @param menu The menu that is filled with the overflow menu.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.team_overflow, menu);
        mDatabase = Database.getInstance();
        mTeamBefore = mDatabase.getTeamNumberBefore(mTeamNumber);
        if (mTeamBefore == 0) {
            menu.removeItem(R.id.previous_team);
        }
        mTeamAfter = mDatabase.getTeamNumberAfter(mTeamNumber);
        if (mTeamAfter == 0) {
            menu.removeItem(R.id.next_team);
        }
        menu.removeItem(R.id.save);
        menu.removeItem(R.id.scout_name);
        return true;
    }

    /**
     * Override to show icons on the overflow menu
     * http://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * Implements the actions for the overflow menu
     *
     * @param item Menu item that is selected from the overflow menu
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(this, Home.class);
                startActivity(intent);
                break;
            case R.id.team_list:
                intent = new Intent(this, TeamList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.TEAM_VIEWING);
                startActivity(intent);
                break;
            case R.id.previous_team:
                intent = new Intent(this, TeamView.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeamBefore);
                startActivity(intent);
                break;
            case R.id.next_team:
                intent = new Intent(this, TeamView.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeamAfter);
                startActivity(intent);
                break;
            default:
                assert false;
        }
        return true;
    }
}
