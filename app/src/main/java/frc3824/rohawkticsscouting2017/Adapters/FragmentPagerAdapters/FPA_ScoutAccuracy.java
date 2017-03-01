package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Fragments.ScoutAccuracy.IndividualScoutAccuracyFragment;
import frc3824.rohawkticsscouting2017.Fragments.ScoutAccuracy.ScoutAccuracyFragment;

/**
 * @author frc3824
 * Created: 2/28/17
 */
public class FPA_ScoutAccuracy extends FragmentPagerAdapter {

    private final static String TAG = "FPA_ScoutAccuracy";

    private ArrayList<String> mTabTitles;

    public FPA_ScoutAccuracy(FragmentManager fm, ArrayList<String> tabTitles) {
        super(fm);
        mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        if(position == 0)
        {
            f = new ScoutAccuracyFragment();
        }
        else
        {
            f = new IndividualScoutAccuracyFragment();
            ((IndividualScoutAccuracyFragment)f).setScout(mTabTitles.get(position));
        }

        return f;
    }

    @Override
    public String getPageTitle(int position)
    {
        return mTabTitles.get(position).replace(' ', '\n').replace('_', '\n');
    }

    @Override
    public int getCount() {
        return mTabTitles.size();
    }

}
