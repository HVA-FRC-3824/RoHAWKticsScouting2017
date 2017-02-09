package frc3824.rohawkticsscouting2017.Fragments.SuperScouting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;
import frc3824.rohawkticsscouting2017.Views.SavableSpinner;

/**
 * @author frc3824
 * Created: 1/24/17
 */
public class SuperMiscellaneousFragment extends ScoutFragment {

    private final static String TAG = "SuperMiscellaneousFragment";
    private int mMatchNumber;

    public SuperMiscellaneousFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_super_miscellaneous, container, false);

        Match match = Database.getInstance().getMatch(mMatchNumber);

        ((SavableSpinner)view.findViewById(R.id.blue1_pilot_rating)).setLabel(String.format("%d's pilot:", match.teams.get(Constants.Match_Indices.BLUE1)));
        ((SavableSpinner)view.findViewById(R.id.blue2_pilot_rating)).setLabel(String.format("%d's pilot:", match.teams.get(Constants.Match_Indices.BLUE2)));
        ((SavableSpinner)view.findViewById(R.id.blue3_pilot_rating)).setLabel(String.format("%d's pilot:", match.teams.get(Constants.Match_Indices.BLUE3)));
        ((SavableSpinner)view.findViewById(R.id.red1_pilot_rating)).setLabel(String.format("%d's pilot:", match.teams.get(Constants.Match_Indices.RED1)));
        ((SavableSpinner)view.findViewById(R.id.red2_pilot_rating)).setLabel(String.format("%d's pilot:", match.teams.get(Constants.Match_Indices.RED2)));
        ((SavableSpinner)view.findViewById(R.id.red3_pilot_rating)).setLabel(String.format("%d's pilot:", match.teams.get(Constants.Match_Indices.RED3)));


        if(mValueMap != null)
        {
            restoreContentsFromMap(mValueMap, (ViewGroup)view);
        }

        Utilities.setupUi(getActivity(), view);

        return view;
    }

    public void setMatchNumber(int match_number){
        mMatchNumber = match_number;
    }

}
