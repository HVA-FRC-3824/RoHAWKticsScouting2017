package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.MatchDataFragment;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.PitDataFragment;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.ScheduleFragment;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.ViewNotesFragment;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.VisualsFragment;

/**
 * @author Andrew Messing
 * Created: 8/16/16
 *
 * The fragment pager adapter that handles the Team View Activity.
 */
public class FPA_TeamView extends FragmentPagerAdapter {

    private final static String TAG = "FPA_TeamView";
    private Database mDatabase;
    private Storage mStorage;
    private int mTeamNumber;

    private String mTabTitles[] = new String[]{"Visuals", "Pit Data", "Match Data", "Notes", "Schedule"};

    private Map<Integer, Fragment> mFragments = new HashMap<>();

    public FPA_TeamView(FragmentManager fm, int teamNumber, Database database, Storage storage) {
        super(fm);
        mTeamNumber = teamNumber;
        mDatabase = database;
        mStorage = storage;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if(mFragments.containsKey(position))
        {
            f = mFragments.get(position);
        }
        else
        {
            switch (position)
            {
                case 0:
                    f = new VisualsFragment();
                    break;
                case 1:
                    f = new PitDataFragment();
                    ((PitDataFragment)f).setTeamNumber(mTeamNumber);
                    break;
                case 2:
                    f = new MatchDataFragment();
                    break;
                case 3:
                    f = new ViewNotesFragment();
                    ((ViewNotesFragment)f).setTeamNumber(mTeamNumber);
                    break;
                case 4:
                    f = new ScheduleFragment();
                    ((ScheduleFragment)f).setTeamNumber(mTeamNumber);
                    break;
            }

            mFragments.put(position, f);
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
