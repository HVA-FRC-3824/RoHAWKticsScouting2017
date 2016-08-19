package frc3824.rohawkticsscouting2017.Fragments.RankingsView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 *         Created: 8/19/16
 */
public class ActualRankingsFragment extends Fragment {

    private final static String TAG = "ActualRankingsFragment";

    public ActualRankingsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_actual_rankings, container, false);

        return view;
    }

}
