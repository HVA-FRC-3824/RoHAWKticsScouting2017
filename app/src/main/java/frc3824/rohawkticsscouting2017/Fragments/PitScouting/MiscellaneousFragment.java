package frc3824.rohawkticsscouting2017.Fragments.PitScouting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Fragment that handles collecting the miscellaneous information for pit scouting
 */
public class MiscellaneousFragment extends ScoutFragment {

    private final static String TAG = "MiscellaneousFragment";

    public MiscellaneousFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_miscellaneous, container, false);
        if(mValueMap != null)
        {
            restoreContentsFromMap(mValueMap, (ViewGroup)view);
        }

        Utilities.setupUi(getActivity(), view);

        return view;
    }
}