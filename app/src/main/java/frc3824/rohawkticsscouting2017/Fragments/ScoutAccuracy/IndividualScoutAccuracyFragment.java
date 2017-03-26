package frc3824.rohawkticsscouting2017.Fragments.ScoutAccuracy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_IndividualScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutedMatchAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 2/28/17
 */
public class IndividualScoutAccuracyFragment extends Fragment{

    private final static String TAG = "IndividualScoutAccuracyFragment";

    private String mScoutName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_individual_scout_accuracy, container, false);

        ScoutAccuracy sa = Database.getInstance().getScoutAccuracy(mScoutName);
        ArrayList<ScoutedMatchAccuracy> scoutedMatchAccuracies = new ArrayList<>(sa.scouted_matches.values());
        Collections.sort(scoutedMatchAccuracies, new Comparator<ScoutedMatchAccuracy>() {
            @Override
            public int compare(ScoutedMatchAccuracy o1, ScoutedMatchAccuracy o2) {
                return Integer.compare(o1.match_number, o2.match_number);
            }
        });
        scoutedMatchAccuracies.add(0, new ScoutedMatchAccuracy());

        ((TextView)view.findViewById(R.id.name)).setText(sa.name);

        LVA_IndividualScoutAccuracy lva = new LVA_IndividualScoutAccuracy(getContext(), scoutedMatchAccuracies);

        ((ListView)view.findViewById(R.id.listview)).setAdapter(lva);

        return view;
    }

    public void setScout(String scout_name){
        mScoutName = scout_name;
    }

}
