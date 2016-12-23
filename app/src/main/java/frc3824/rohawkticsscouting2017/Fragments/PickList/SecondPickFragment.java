package frc3824.rohawkticsscouting2017.Fragments.PickList;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;

/**
 * @author frc3824
 * Created: 8/23/16
 *
 *
 */
public class SecondPickFragment extends ScoutPickFragment {

    private final static String TAG = "SecondPickFragment";

    public SecondPickFragment(){}

    @Override
    public ArrayList<TeamPickAbility> setupTeamList() {
        ArrayList<TeamPickAbility> teams = new ArrayList<>();
        for(int team_number: mDatabase.getTeamNumbers())
        {
            teams.add(mDatabase.getSecondTPA(team_number));
        }

        return teams;
    }

    @Override
    public void save() {
        for(TeamPickAbility teamPickAbility : mTeams)
        {
            mDatabase.setSecondTPA(teamPickAbility);
        }
    }

}
