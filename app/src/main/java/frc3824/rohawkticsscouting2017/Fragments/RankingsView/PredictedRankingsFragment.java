package frc3824.rohawkticsscouting2017.Fragments.RankingsView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_PredictedRanking;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TRD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Fragment that displays a list of the predicted rankings based on the current rankings
 */
public class PredictedRankingsFragment extends Fragment{

    private final static String TAG = "PredictedRankingsFragment";

    public PredictedRankingsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predicted_rankings, container, false);

        Database database = Database.getInstance();

        Map<Integer, TRD> rankings = database.getPredictedRankings();

        ArrayList<TRD> rankingsList = new ArrayList<>(rankings.values());
        LVA_PredictedRanking lva = new LVA_PredictedRanking(getContext(), rankingsList);

        ListView lv = (ListView)view.findViewById(R.id.list);
        lv.setAdapter(lva);

        return view;
    }
}
