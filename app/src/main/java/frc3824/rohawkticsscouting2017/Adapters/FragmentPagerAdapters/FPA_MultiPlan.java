package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Fragments.MatchPlanning.SinglePlanFragment;

/**
 * @author frc3824
 * Created: 12/22/16
 */
public class FPA_MultiPlan extends FragmentPagerAdapter {

    private final static String TAG = "FPA_MultiPlan";

    private ArrayList<SinglePlanFragment> mFragments = new ArrayList<>();
    private ArrayList<Integer> mTeams;

    public FPA_MultiPlan(FragmentManager fm) {
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
        return mFragments.get(position);
    }

    public void add(int index){
        SinglePlanFragment f = new SinglePlanFragment();
        f.setMatch(mTeams);
        mFragments.add(index, f);
        notifyDataSetChanged();
    }

    public void remove(int index){
        mFragments.remove(index);
        notifyDataSetChanged();
    }

    /**
     * Gets the number of fragments
     * @return the number of fragments
     */
    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     *
     * @param position - the position of the tab
     * @return the title of the tab
     */
    @Override
    public String getPageTitle(int position)
    {
        return String.valueOf(position + 1);
    }

    public void setMatch(ArrayList<Integer> teams){
        mTeams = teams;
        add(0);
    }

    public ArrayList<Strategy> save(){
        ArrayList<Strategy> strategies = new ArrayList<>();
        for(SinglePlanFragment f : mFragments){
            strategies.add(f.save());
        }
        return strategies;
    }
}
