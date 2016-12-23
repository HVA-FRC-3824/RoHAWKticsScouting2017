package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toolbar;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_RankingsView;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Activity for displaying current and predicted rankings
 */
public class RankingsView extends Activity {

    private final static String TAG = "RankingsView";

    private FPA_RankingsView mFPA;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setActionBar(toolbar);

        setTitle("Rankings");

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mFPA = new FPA_RankingsView(getFragmentManager());
        viewPager.setAdapter(mFPA);
        // Set the off screen page limit to more than the number of fragments
        viewPager.setOffscreenPageLimit(mFPA.getCount());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
    }

}
