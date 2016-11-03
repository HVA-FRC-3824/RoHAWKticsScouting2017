package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_TeamView_MatchData;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/17/16
 *
 *
 */
public class MatchDataFragment extends Fragment {

    private final static String TAG = "MatchDataFragment";

    private ViewPager mViewPager;
    private int mTeamNumber;

    public MatchDataFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_match_data, container, false);

        mViewPager = (ViewPager)view.findViewById(R.id.match_data_view_pager);
        FPA_TeamView_MatchData fpa = new FPA_TeamView_MatchData(getChildFragmentManager(), mTeamNumber);
        mViewPager.setAdapter(fpa);
        mViewPager.setOffscreenPageLimit(fpa.getCount());
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.match_data_tab_layout);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public void setTeamNumber(int team_number){
        mTeamNumber = team_number;
    }

}
