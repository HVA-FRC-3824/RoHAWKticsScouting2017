package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Messing
 *         Created: 1/2/17
 */
public class ScoutAccuracy {

    private final static String TAG = "ScoutAccuracy";

    public String name;
    public Map<Integer, ScoutedMatchAccuracy> scouted_matches;
    public int total_error;
    public int auto_error;
    public int teleop_error;
    public int endgame_error;

    public ScoutAccuracy(){
        scouted_matches = new HashMap<>();
    }

}
