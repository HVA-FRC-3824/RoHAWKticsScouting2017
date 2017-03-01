package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.Toolbar;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 2/28/17
 */
public class ScoutAccuracy extends Activity {

    private final static String TAG = "ScoutAccuracy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_accuracy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        setTitle("Scout Accuracy");
        ArrayList<String> scout_names = Database.getInstance().getScoutNames();
        scout_names.add(0, "All");
        FPA_ScoutAccuracy fpa = new FPA_ScoutAccuracy(getFragmentManager(), scout_names);
        viewPager.setAdapter(fpa);
        viewPager.setOffscreenPageLimit(fpa.getCount());
    }

}
