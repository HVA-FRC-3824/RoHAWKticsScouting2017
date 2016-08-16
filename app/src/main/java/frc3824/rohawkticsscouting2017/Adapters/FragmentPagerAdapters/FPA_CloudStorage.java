package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Fragments.CloudStorage.CloudDebugFragment;
import frc3824.rohawkticsscouting2017.Fragments.CloudStorage.CloudPickListFragment;
import frc3824.rohawkticsscouting2017.Fragments.CloudStorage.CloudRobotPictureFragment;
import frc3824.rohawkticsscouting2017.Fragments.CloudStorage.CloudStrategyFragment;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 *
 */
public class FPA_CloudStorage extends FragmentPagerAdapter {

    private final static String TAG = "FPA_CloudStorage";

    private String mTabTitles[] = new String[]{"Robot Picture", "Strategy", "Pick List", "Debug"};
    private Map<Integer, Fragment> mFragments = new HashMap<>();


    public FPA_CloudStorage(FragmentManager fm) {
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
                    f = new CloudRobotPictureFragment();
                    break;
                case 1:
                    f = new CloudStrategyFragment();
                    break;
                case 2:
                    f = new CloudPickListFragment();
                    break;
                case 3:
                    f = new CloudDebugFragment();
                    break;
            }


            mFragments.put(position, f);
        }

        return f;
    }

    /**
     *
     * @param position - the position of the tab
     * @return the title of the tab
     */
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
