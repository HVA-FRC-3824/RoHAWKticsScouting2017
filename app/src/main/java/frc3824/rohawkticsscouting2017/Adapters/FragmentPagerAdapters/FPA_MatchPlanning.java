package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchStrategy;
import frc3824.rohawkticsscouting2017.Fragments.MatchPlanning.SinglePlanFragment;
import frc3824.rohawkticsscouting2017.Fragments.MatchPlanning.MultiPlanFragment;

/**
 * @author frc3824
 * Created: 12/21/16
 */
public class FPA_MatchPlanning extends FragmentPagerAdapter {

    private final static String TAG = "FPA_MatchPlanning";

    private String mTabTitles[] = new String[]{"Autonomous", "Teleop", "Endgame"};

    private Map<Integer, Fragment> mFragments = new HashMap<>();
    private int mMatchNumber;
    private ArrayList<Integer> mTeams;
    private MatchStrategy mMatchStrategy;

    public FPA_MatchPlanning(FragmentManager fm) {
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
        Fragment f = null;

        if(mFragments.containsKey(position))
        {
            f = mFragments.get(position);
        }
        else {
            switch (position) {
                case 0:
                    f = new SinglePlanFragment();
                    ((SinglePlanFragment)f).setMatch(mTeams);
                    if(mMatchStrategy != null) {
                        ((SinglePlanFragment) f).load(mMatchStrategy.auto);
                    }
                    break;
                case 1:
                    f = new MultiPlanFragment();
                    ((MultiPlanFragment)f).setMatch(mTeams);
                    if(mMatchStrategy != null) {
                        ((MultiPlanFragment) f).load(mMatchStrategy.teleop);
                    }
                    break;
                case 2:
                    f = new SinglePlanFragment();
                    ((SinglePlanFragment)f).setMatch(mTeams);
                    if(mMatchStrategy != null) {
                        ((SinglePlanFragment) f).load(mMatchStrategy.endgame);
                    }
                    break;
                default:
                    assert false;
            }

            mFragments.put(position, f);
        }


        return f;
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

    public void setMatch(int match_number, ArrayList<Integer> teams){
        mMatchNumber = match_number;
        mTeams = teams;
    }

    public MatchStrategy save(){
        MatchStrategy strategy = new MatchStrategy();
        strategy.match_number = mMatchNumber;
        strategy.auto = ((SinglePlanFragment)mFragments.get(0)).save();
        strategy.teleop = ((MultiPlanFragment)mFragments.get(1)).save();
        strategy.endgame = ((SinglePlanFragment)mFragments.get(2)).save();
        return strategy;
    }

    public void load(MatchStrategy matchStrategy){
        mMatchStrategy = matchStrategy;
    }
}
