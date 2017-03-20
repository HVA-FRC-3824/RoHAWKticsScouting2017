package frc3824.rohawkticsscouting2017.Fragments.RankingsView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_Ranking;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamRankingData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Fragment that displays the current rankings
 */
public class CurrentRankingsFragment extends Fragment {

    private final static String TAG = "CurrentRankingsFragment";

    public CurrentRankingsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rankings, container, false);

        Database database = Database.getInstance();

        ArrayList<TeamRankingData> rankings = database.getAllTeamRankingData(Database.RankingType.CURRENT);

        LVA_Ranking lva = new LVA_Ranking(getContext(), rankings);

        ListView lv = (ListView)view.findViewById(R.id.list);
        lv.setAdapter(lva);

        return view;
    }

}
