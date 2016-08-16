package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toolbar;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_CloudStorage;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Activity for uploading/downloading images and files from the Google Cloud Storage through
 * Firebase
 */
public class CloudStorage extends Activity {

    private final static String TAG = "CloudStorage";

    private FPA_CloudStorage mFPA;

    private Storage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_storage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cloud_storage_toolbar);
        setActionBar(toolbar);

        setTitle("Cloud Storage");

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventKey = shared_preferences.getString(Constants.Settings.EVENT_KEY, "");
        mStorage = Storage.getInstance(eventKey);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager)findViewById(R.id.cloud_storage_view_pager);
        mFPA = new FPA_CloudStorage(getFragmentManager());

        viewPager.setAdapter(mFPA);
        viewPager.setOffscreenPageLimit(4);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.cloud_storage_tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
    }

}
