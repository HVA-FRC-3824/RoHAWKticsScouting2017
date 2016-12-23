package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toolbar;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_PickList;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 8/23/16
 *
 * Activity to display pick lists
 */
public class PickList extends Activity implements ViewPager.OnPageChangeListener {

    private final static String TAG = "PickList";

    private FPA_PickList mFPA;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        setTitle("Pick List");

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        mFPA = new FPA_PickList(getFragmentManager());

        viewPager.setAdapter(mFPA);
        viewPager.setOffscreenPageLimit(mFPA.getCount());
        viewPager.setOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mFPA.update(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
