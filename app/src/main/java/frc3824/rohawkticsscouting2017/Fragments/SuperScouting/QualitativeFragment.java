package frc3824.rohawkticsscouting2017.Fragments.SuperScouting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
import frc3824.rohawkticsscouting2017.Views.SavableQualitative;

/**
 * @author frc3824
 * Created: 8/16/16
 *
 *
 */
public class QualitativeFragment extends ScoutFragment {

    private final static String TAG = "QualitativeFragment";
    
    private int mMatchNumber;

    public QualitativeFragment(){}
    
    public void setMatchNumber(int match_number)
    {
        mMatchNumber = match_number;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_super_qualitative, container, false);

        Database database = Database.getInstance();
        ArrayList<Integer> blue = new ArrayList<>();
        ArrayList<Integer> red = new ArrayList<>();

        if(mMatchNumber > 0) {
            Match match = database.getMatch(mMatchNumber);
            for (int i = 0; i < 3; i++) {
                blue.add(match.team_numbers.get(i));
            }
            for (int i = 3; i < 6; i++) {
                red.add(match.team_numbers.get(i));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                blue.add(i + 1);
            }
            for (int i = 3; i < 6; i++) {
                red.add(i + 1);
            }
        }
        SavableQualitative blue_speed = (SavableQualitative)view.findViewById(R.id.blue_speed);
        blue_speed.setTeams(blue);

        SavableQualitative blue_intake_ability = (SavableQualitative)view.findViewById(R.id.blue_torque);
        blue_intake_ability.setTeams(blue);
        SavableQualitative blue_control = (SavableQualitative)view.findViewById(R.id.blue_control);
        blue_control.setTeams(blue);
        SavableQualitative blue_defense = (SavableQualitative)view.findViewById(R.id.blue_defense);
        blue_defense.setTeams(blue);

        SavableQualitative red_speed = (SavableQualitative)view.findViewById(R.id.red_speed);
        red_speed.setTeams(red);
        SavableQualitative red_intake_ability = (SavableQualitative)view.findViewById(R.id.red_torque);
        red_intake_ability.setTeams(red);
        SavableQualitative red_control = (SavableQualitative)view.findViewById(R.id.red_control);
        red_control.setTeams(red);
        SavableQualitative red_defense = (SavableQualitative)view.findViewById(R.id.red_defense);
        red_defense.setTeams(red);

        
        if(mValueMap != null)
        {
            restoreContentsFromMap(mValueMap, (ViewGroup)view);
        }

        //Utilities.setupUi(getActivity(), view);

        return view;
    }

}
