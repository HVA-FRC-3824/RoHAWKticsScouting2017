package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Fragments.MatchView.MatchViewAllianceFragment;
import frc3824.rohawkticsscouting2017.Fragments.MatchView.MatchViewPredictionFragment;
import frc3824.rohawkticsscouting2017.Fragments.MatchView.MatchViewStrategyFragment;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 *
 */
public class FPA_MatchView extends FragmentPagerAdapter {

    private final static String TAG = "FPA_MatchView";

    private String[] mTabTitles;
    private int mMatchNumber;
    private ArrayList<Integer> mTeams;
    private boolean custom;

    public FPA_MatchView(FragmentManager fm, int match_number) {
        super(fm);
        mMatchNumber = match_number;
        mTeams = Database.getInstance().getMatch(mMatchNumber).team_numbers;
        mTabTitles = new String[]{"Blue", "Red", "Strategy", "Prediction"};
        custom = false;
    }

    public FPA_MatchView(FragmentManager fm, ArrayList<Integer> teams) {
        super(fm);
        mTeams = teams;
        mTabTitles = new String[]{"Blue", "Red", "Strategy", "Prediction"};
        custom = true;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch (position){
            case 0:
                f = new MatchViewAllianceFragment();
                if(custom){
                    ((MatchViewAllianceFragment) f).setMatchAlliance(mTeams, true);
                } else {
                    ((MatchViewAllianceFragment) f).setMatchAlliance(mMatchNumber, true);
                }
                break;
            case 1:
                f = new MatchViewAllianceFragment();
                if(custom) {
                    ((MatchViewAllianceFragment) f).setMatchAlliance(mTeams, false);
                } else {
                    ((MatchViewAllianceFragment) f).setMatchAlliance(mMatchNumber, false);
                }
                break;
            case 2:
                f = new MatchViewStrategyFragment();
                if(custom) {
                    ((MatchViewStrategyFragment) f).setMatch(mTeams);
                } else {
                    ((MatchViewStrategyFragment) f).setMatch(mMatchNumber);
                }
                break;
            case 3:
                f = new MatchViewPredictionFragment();
                if (custom) {
                    ((MatchViewPredictionFragment) f).setMatch(mTeams);
                } else {
                    ((MatchViewPredictionFragment) f).setMatch(mMatchNumber);
                }
                break;
            case 4:
                f = new MatchViewPredictionFragment();
                if (custom) {
                    ((MatchViewPredictionFragment) f).setMatch(mTeams);
                } else {
                    ((MatchViewPredictionFragment) f).setMatch(mMatchNumber);
                }
                break;
            default:
                assert false;
        }
        return f;
    }

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
}
