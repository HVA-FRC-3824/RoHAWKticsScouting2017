package frc3824.rohawkticsscouting2017.Fragments.RankingsView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_CurrentRanking;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TRD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/19/16
 *
 *
 */
public class CurrentRankingsFragment extends Fragment {

    private final static String TAG = "CurrentRankingsFragment";

    public CurrentRankingsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_current_rankings, container, false);

        Database database = Database.getInstance();

        Map<Integer, TRD> rankings = database.getCurrentRankings();

        ArrayList<TRD> rankingsList = new ArrayList<>(rankings.values());
        LVA_CurrentRanking lva = new LVA_CurrentRanking(getContext(), rankingsList);

        ListView lv = (ListView)view.findViewById(R.id.list);
        lv.setAdapter(lva);

        return view;
    }

}
