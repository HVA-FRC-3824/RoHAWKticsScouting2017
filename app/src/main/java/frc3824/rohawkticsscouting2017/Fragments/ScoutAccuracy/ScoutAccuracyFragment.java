package frc3824.rohawkticsscouting2017.Fragments.ScoutAccuracy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 2/28/17
 */
public class ScoutAccuracyFragment extends Fragment{

    private final static String TAG = "ScoutAccuracyFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scout_accuracy, container, false);


        ArrayList<ScoutAccuracy> scoutAccuracies = Database.getInstance().getScoutAccuracies();

        scoutAccuracies.add(0, new ScoutAccuracy());

        LVA_ScoutAccuracy lva = new LVA_ScoutAccuracy(getContext(), scoutAccuracies);
        ((ListView)view.findViewById(R.id.listview)).setAdapter(lva);

        return view;
    }
}
