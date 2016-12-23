package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.Activities.MatchPlanning;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 12/22/16
 */
public class MatchViewMatchPlanFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MatchViewMatchPlanFragment";

    private int mMatchNumber;

    public MatchViewMatchPlanFragment(){

    }

    public void setMatch(int match_number){
        mMatchNumber = match_number;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_view_match_plan, container, false);

        view.findViewById(R.id.go).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MatchPlanning.class);
        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber);
        getContext().startActivity(intent);
    }
}
