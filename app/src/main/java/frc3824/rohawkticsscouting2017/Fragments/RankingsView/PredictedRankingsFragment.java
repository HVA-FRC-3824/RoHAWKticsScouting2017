package frc3824.rohawkticsscouting2017.Fragments.RankingsView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/19/16
 *
 */
public class PredictedRankingsFragment extends Fragment{

    private final static String TAG = "PredictedRankingsFragment";

    public PredictedRankingsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_predicted_rankings, container, false);

        return view;
    }
}
