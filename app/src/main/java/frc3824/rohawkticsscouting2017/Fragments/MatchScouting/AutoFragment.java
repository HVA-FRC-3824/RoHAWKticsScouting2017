package frc3824.rohawkticsscouting2017.Fragments.MatchScouting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author frc3824
 * Created: 8/11/16
 *
 * Fragment that contains all the widgets for scouting during the autonomous portion of a match
 */
public class AutoFragment extends ScoutFragment{

    private final static String TAG = "AutoFragment";

    public AutoFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_auto, container, false);
        if(mValueMap != null) {
            restoreContentsFromMap(mValueMap, (ViewGroup)view);
        }

        Utilities.setupUi(getActivity(), view);

        return view;
    }

}
