package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.Map;

/**
 * @author frc3824
 * Created: 9/26/16
 *
 * Class that stores feedback the drive team_number gives on a specific team_number
 */
public class TeamDTFeedback {

    private final static String TAG = "TeamDTFeedback";

    public int team_number;
    public long last_modified;
    public Map<Integer, String> feedback;

}
