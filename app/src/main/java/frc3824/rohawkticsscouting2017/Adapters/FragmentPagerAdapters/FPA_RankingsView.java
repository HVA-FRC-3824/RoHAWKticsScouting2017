package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Fragments.RankingsView.ActualRankingsFragment;
import frc3824.rohawkticsscouting2017.Fragments.RankingsView.PredictedRankingsFragment;

/**
 * @author Andrew Messing
 *         Created: 8/19/16
 */
public class FPA_RankingsView extends FragmentPagerAdapter {

    private final static String TAG = "FPA_RankingsView";

    private String mTabTitles[] = new String[]{"Actual", "Predicted"};

    private Map<Integer, Fragment> mFragments = new HashMap<>();

    public FPA_RankingsView(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if(mFragments.containsKey(position))
        {
            f = mFragments.get(position);
        }
        else {
            switch (position)
            {
                case 0:
                    f = new ActualRankingsFragment();
                    break;
                case 1:
                    f = new PredictedRankingsFragment();
                    break;
            }

            mFragments.put(position, f);
        }

        return f;
    }

    @Override
    public String getPageTitle(int position)
    {
        return mTabTitles[position];
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }
}
