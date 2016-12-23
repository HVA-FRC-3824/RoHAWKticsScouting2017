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
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_MatchView;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/18/16
 *
 * Activity for showing all the teams in a match
 */
public class MatchView extends Activity {

    private final static String TAG = "MatchView";
    private int mMatchNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER, -1);

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
        if(mMatchNumber == -1)
        {
            setTitle("Custom Match");
            ArrayList<Integer> teams = new ArrayList<>();
            teams.add(extras.getInt(Constants.Intent_Extras.BLUE1,-1));
            teams.add(extras.getInt(Constants.Intent_Extras.BLUE2,-1));
            teams.add(extras.getInt(Constants.Intent_Extras.BLUE3,-1));
            teams.add(extras.getInt(Constants.Intent_Extras.RED1,-1));
            teams.add(extras.getInt(Constants.Intent_Extras.RED2,-1));
            teams.add(extras.getInt(Constants.Intent_Extras.RED3,-1));

            FPA_MatchView fpa = new FPA_MatchView(getFragmentManager(), teams);
            viewPager.setAdapter(fpa);
            viewPager.setOffscreenPageLimit(fpa.getCount());
        }
        else
        {
            setTitle("Match Number: " + mMatchNumber);
            FPA_MatchView fpa = new FPA_MatchView(getFragmentManager(), mMatchNumber);
            viewPager.setAdapter(fpa);
            viewPager.setOffscreenPageLimit(fpa.getCount());
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
        if (mMatchNumber == -1 || mMatchNumber == Database.getInstance().getNumberOfMatches()) {
            menu.removeItem(R.id.next_match);
        }
        menu.removeItem(R.id.switch_team);
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
