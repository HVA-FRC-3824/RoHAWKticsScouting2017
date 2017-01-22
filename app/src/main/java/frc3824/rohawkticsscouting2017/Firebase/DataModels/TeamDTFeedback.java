package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.Map;

/**
 * @author frc3824
 * Created: 9/26/16
 *
 * Class that stores feedback the drive team gives on a specific team
 */
public class TeamDTFeedback {

    private final static String TAG = "TeamDTFeedback";

    public long last_modified;
    public int team_number;
    public Map<Integer, String> feedback;

}
