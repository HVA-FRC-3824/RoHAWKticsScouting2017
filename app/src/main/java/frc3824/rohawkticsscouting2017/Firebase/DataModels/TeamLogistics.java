package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 * Logistical information about a specific team
 */
@IgnoreExtraProperties
public class TeamLogistics {

    private final static String TAG = "TeamLogistics";

    public int team_number;
    public String nickname;

    public ArrayList<Integer> match_numbers;

    public int surrogate_match_number;

    public TeamLogistics() {
        surrogate_match_number = -1;
    }
}
