package frc3824.rohawkticsscouting2017.Fragments.PickList;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPA;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class SecondPickFragment extends ScoutPickFragment {

    private final static String TAG = "SecondPickFragment";

    public SecondPickFragment(){}

    @Override
    public ArrayList<TPA> setupTeamList()
    {
        ArrayList<TPA> teams = new ArrayList<>();
        for(int team_number: mDatabase.getTeamNumbers())
        {
            teams.add(mDatabase.getSecondTPA(team_number));
        }

        return teams;
    }

    @Override
    public void save() {
        for(TPA tpa: mTeams)
        {
            mDatabase.setSecondTPA(tpa);
        }
    }

}
