package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Utilities.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Adapter for the fragments to be used in the Match Scouting Activity
 */
public class FPA_MatchScouting extends FragmentPagerAdapter {

    private final static String TAG = "FPA_MatchScouting";

    private String mTabTitles[] = new String[]{"Autonomous", "Teleop", "Endgame", "Post-Match", "Fouls"};

    //TODO: Check if this works or if individual constructors are needed (weird bug with it last year)
    private Map<Integer, ScoutFragment> mFragments = new HashMap<>();

    private ScoutMap mValueMap = null;

    public FPA_MatchScouting(FragmentManager fm) {
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

        switch (position){
            default:
                assert false;
        }

        // Set the value map that restores values
        if(mValueMap != null)
        {
            sf.setValueMap(mValueMap);
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
