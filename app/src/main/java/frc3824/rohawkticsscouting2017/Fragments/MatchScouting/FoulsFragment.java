package frc3824.rohawkticsscouting2017.Fragments.MatchScouting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Fragment that contains all the widgets for recording fouls
 */
public class FoulsFragment extends ScoutFragment{

    private final static String TAG = "FoulsFragment";

    public FoulsFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fouls, container, false);
        if(mValueMap != null)
        {
            restoreContentsFromMap(mValueMap, (ViewGroup)view);
        }

        Utilities.setupUi(getActivity(), view);

        return view;
    }

}
