package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 * Logistical information about a specific team
 */
@IgnoreExtraProperties
public class TID {

    private final static String TAG = "TID";

    public int team_number;
    public String nickname;

    public List<Integer> match_numbers;

    public TID() {}
}
