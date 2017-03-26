package frc3824.rohawkticsscouting2017.Fragments.RankingsView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
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

        rankings.sort(new Comparator<TeamRankingData>() {
            @Override
            public int compare(TeamRankingData o1, TeamRankingData o2) {
                int rp_compare = Float.compare(o1.RPs, o2.RPs);
                int ftb_compare = Integer.compare(o1.first_tie_breaker, o2.first_tie_breaker);
                int stb_compare = Integer.compare(o1.second_tie_breaker, o2.second_tie_breaker);

                if(rp_compare != 0){
                    return -rp_compare;
                } else if(ftb_compare != 0) {
                    return -ftb_compare;
                } else if(stb_compare != 0) {
                    return -stb_compare;
                }

                return 0;
            }
        });
        for(int i = 0; i < rankings.size(); i++){
            rankings.get(i).rank = i + 1;
        }

        LVA_Ranking lva = new LVA_Ranking(getContext(), rankings);

        ListView lv = (ListView)view.findViewById(R.id.list);
        lv.setAdapter(lva);

        return view;
    }

}
