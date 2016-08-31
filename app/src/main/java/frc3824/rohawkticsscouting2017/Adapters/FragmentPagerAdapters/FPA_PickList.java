package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Fragments.PickList.DoNotPickFragment;
import frc3824.rohawkticsscouting2017.Fragments.PickList.FirstPickFragment;
import frc3824.rohawkticsscouting2017.Fragments.PickList.ScoutPickFragment;
import frc3824.rohawkticsscouting2017.Fragments.PickList.SecondPickFragment;
import frc3824.rohawkticsscouting2017.Fragments.PickList.ThirdPickFragment;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 *
 *
 */
public class FPA_PickList extends FragmentPagerAdapter {

    private final static String TAG = "FPA_PickList";

    private String mTabTitles[] = new String[]{"First Pick", "Second Pick", "Third Pick", "DNP"};
    private Map<Integer, ScoutPickFragment> mFragments = new HashMap<>();

    public FPA_PickList(FragmentManager fm) {
        super(fm);
        mFragments.put(0, new FirstPickFragment());
        mFragments.put(1, new SecondPickFragment());
        mFragments.put(2, new ThirdPickFragment());
        mFragments.put(3, new DoNotPickFragment());
    }

    @Override
    public Fragment getItem(int position) {
        ScoutPickFragment f = null;
        if(mFragments.containsKey(position))
        {
            f = mFragments.get(position);
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

    public void update(int position){
        if(position < mTabTitles.length - 1)
        {
            mFragments.get(position).update();
        }
    }
}
