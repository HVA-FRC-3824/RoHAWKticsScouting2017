package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Fragments.PitScouting.NotesFragment;
import frc3824.rohawkticsscouting2017.Fragments.PitScouting.DimensionsFragment;
import frc3824.rohawkticsscouting2017.Fragments.PitScouting.MiscellaneousFragment;
import frc3824.rohawkticsscouting2017.Fragments.PitScouting.RobotPictureFragment;
import frc3824.rohawkticsscouting2017.Utilities.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;

/**
 * @author frc3824
 * Created: 8/11/16
 *
 * Adapter for the fragments to be used in the Match Scouting Activity
 */
public class FPA_PitScouting extends FragmentPagerAdapter {

    private final static String TAG = "FPA_PitScouting";

    private String mTabTitles[] = new String[]{"Robot Picture", "Dimensions", "Misc", "Notes"};

    private Map<Integer, ScoutFragment> mFragments = new HashMap<>();

    private ScoutMap mValueMap = null;

    public FPA_PitScouting(FragmentManager fm) {
        super(fm);
    }

    /**
     * Gets the fragment at the specified position for display
     *
     * @param position position of the fragment wanted
     * @return fragment to be displayed
     */
    @Override
    public Fragment getItem(int position) {
        ScoutFragment sf = null;
        if(mFragments.containsKey(position))
        {
            sf = mFragments.get(position);
        }
        else {
            switch (position)
            {
                case 0:
                    sf = new RobotPictureFragment();
                    break;
                case 1:
                    sf = new DimensionsFragment();
                    break;
                case 2:
                    sf = new MiscellaneousFragment();
                    break;
                case 3:
                    sf = new NotesFragment();
                    break;
            }

            if(mValueMap != null) {
                sf.setValueMap(mValueMap);
            }

            mFragments.put(position, sf);
        }

        return sf;
    }

    /**
     * Gets the number of fragments
     * @return the number of fragments
     */
    @Override
    public int getCount() {
        return mTabTitles.length;
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

    /**
     * Sets the value map that will be passed to each of the fragments for restoring purposes
     * @param valueMap a map of the values to be displayed
     */
    public void setValueMap(ScoutMap valueMap)
    {
        mValueMap = valueMap;
    }

    /**
     *  Gets a list of all the scout fragments
     * @return list of all the scout fragments
     */
    public List<ScoutFragment> getAllFragments()
    {
        List<ScoutFragment> list = new ArrayList<>();
        for(Map.Entry<Integer, ScoutFragment> entry: mFragments.entrySet())
        {
            list.add(entry.getValue());
        }

        return list;
    }
}
