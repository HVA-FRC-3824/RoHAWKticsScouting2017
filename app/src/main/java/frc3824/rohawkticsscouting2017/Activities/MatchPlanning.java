package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toolbar;
import android.support.v4.view.ViewPager;


import java.lang.reflect.Method;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_MatchPlanning;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchStrategy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Views.NonScrollableViewPager;

/**
 * @author frc3824
 * Created: 12/21/16
 *
 *
 */
public class MatchPlanning extends Activity {

    private final static String TAG = "MatchPlanning";

    private FPA_MatchPlanning mFPA;
    private  int mMatchNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_plan);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        NonScrollableViewPager viewPager = (NonScrollableViewPager) findViewById(R.id.view_pager);
        viewPager.setPagingEnabled(false);
        mFPA = new FPA_MatchPlanning(getFragmentManager());

        Database database = Database.getInstance();

        Match match = database.getMatch(mMatchNumber);
        mFPA.setMatch(mMatchNumber, match.teams);

        viewPager.setAdapter(mFPA);
        viewPager.setOffscreenPageLimit(mFPA.getCount());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);

        MatchStrategy matchStrategy = database.getMatchStrategy(mMatchNumber);
        if(matchStrategy != null){
            mFPA.load(matchStrategy);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_overflow, menu);
        menu.removeItem(R.id.previous_match);
        menu.removeItem(R.id.next_match);
        menu.removeItem(R.id.switch_team);
        menu.findItem(R.id.match_list).setTitle("Strategy List");
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
        switch (item.getItemId()) {
            case R.id.home:
                home_press();
                break;
            case R.id.match_list:
                back_press();
                break;
            case R.id.save:
                save_press();
                break;
            default:
                assert false;
        }
        return true;
    }

    public void home_press(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void back_press(){
        this.finish();
    }

    public void save_press(){
        Database.getInstance().setMatchStrategy(mFPA.save());
    }
}
