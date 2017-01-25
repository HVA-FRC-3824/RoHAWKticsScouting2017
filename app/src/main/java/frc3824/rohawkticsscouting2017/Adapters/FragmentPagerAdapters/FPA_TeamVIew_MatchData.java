package frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.AllMatchDataFragment;
import frc3824.rohawkticsscouting2017.Fragments.TeamView.IndividualMatchDataFragment;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 *
 */
public class FPA_TeamView_MatchData extends FragmentPagerAdapter {

    private final static String TAG = "FPA_TeamView_MatchData";
    private Database mDatabase;
    private Team mTeam;
    private int mTeamNumber;


    public FPA_TeamView_MatchData(FragmentManager fm, int team_number) {
        super(fm);
        mDatabase = Database.getInstance();
        mTeamNumber = team_number;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if(position == 0){
            f = new AllMatchDataFragment();
            ((AllMatchDataFragment)f).setTeam(mTeamNumber);
        } else {
            f = new IndividualMatchDataFragment();
            ((IndividualMatchDataFragment)f).setTeamMatch(mTeamNumber, position);
        }
        return f;
    }

    @Override
    public int getCount() {
        return mTeam.completed_matches.size() + 1;
    }

    /**
     *
     * @param position - the position of the tab
     * @return the title of the tab
     */
    @Override
    public String getPageTitle(int position) {

        if(position == 0){
            return "All";
        } else {
            return String.valueOf(mTeam.info.match_numbers.get(position));
        }
    }
}
