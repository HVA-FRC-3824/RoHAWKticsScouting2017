package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class ScoutedMatchAccuracy {

    private final static String TAG = "ScoutedMatchAccuracy";

    public long last_modified;
    public int match_number;
    public String alliance_color;
    public int alliance_number;

    public int total_error;
    public int auto_mobility_error;
    public int auto_rotor_error;
    public int teleop_rotor_error;
    public int endgame_error;

    public ScoutedMatchAccuracy(){

    }

}
