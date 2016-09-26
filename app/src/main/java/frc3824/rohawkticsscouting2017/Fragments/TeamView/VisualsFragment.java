package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/17/16
 *
 * Fragment that holds all the charts and graphics for an individual team's stats
 */
public class VisualsFragment extends Fragment {

    private final static String TAG = "VisualsFragment";

    public VisualsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visuals, container, false);
        return view;
    }
}
