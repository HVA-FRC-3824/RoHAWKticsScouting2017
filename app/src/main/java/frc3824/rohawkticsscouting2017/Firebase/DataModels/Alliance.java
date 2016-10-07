package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Collection of Team objects that represent the teams on an alliance
 */
public class Alliance {

    private final static String TAG = "Alliance";

    public ArrayList<Team> teams;

    public Alliance() {
        teams = new ArrayList<>();
    }
}
