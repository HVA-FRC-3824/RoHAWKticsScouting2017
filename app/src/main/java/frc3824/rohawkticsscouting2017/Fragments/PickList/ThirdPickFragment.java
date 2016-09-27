package frc3824.rohawkticsscouting2017.Fragments.PickList;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPA;

/**
 * @author frc3824
 * Created: 8/23/16
 *
 *
 */
public class ThirdPickFragment extends ScoutPickFragment {

    private final static String TAG = "ThirdPickFragment";

    public ThirdPickFragment(){}

    @Override
    public ArrayList<TPA> setupTeamList() {

        ArrayList<TPA> teams = new ArrayList<>();
        for(int team_number: mDatabase.getTeamNumbers())
        {
            teams.add(mDatabase.getThirdTPA(team_number));
        }

        return teams;
    }

    @Override
    public void save() {
        for(TPA tpa: mTeams)
        {
            mDatabase.setThirdTPA(tpa);
        }
    }

}
