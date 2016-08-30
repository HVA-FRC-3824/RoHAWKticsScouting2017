package frc3824.rohawkticsscouting2017.Fragments.PickList;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPA;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class ThirdPickFragment extends ScoutPickFragment {

    private final static String TAG = "ThirdPickFragment";

    public ThirdPickFragment(){}

    @Override
    public ArrayList<TPA> setupTeamList()
    {

        ArrayList<TPA> team = new ArrayList<>();
        for(int team_number: mDatabase.getTeamNumbers())
        {
            team.add(mDatabase.getThirdTPA(team_number));
        }

        return team;
    }

    @Override
    public void save() {
        for(TPA tpa: mTeams)
        {
            mDatabase.setThirdTPA(tpa);
        }
    }

}
