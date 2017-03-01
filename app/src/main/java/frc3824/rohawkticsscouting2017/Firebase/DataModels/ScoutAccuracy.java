package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class ScoutAccuracy {

    private final static String TAG = "ScoutAccuracy";

    public String name;
    public long last_modified;
    public Map<String, ScoutedMatchAccuracy> scouted_matches;
    public int total_error;
    public int auto_mobility_error;
    public int auto_rotor_error;
    public int teleop_rotor_error;
    public int endgame_error;

    public ScoutAccuracy(){
        scouted_matches = new HashMap<>();
    }

}
