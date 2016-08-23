package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author Andrew Messing
 * Created: 8/19/16
 *
 *
 */
public class TRD {

    private final static String TAG = "TRD";

    public int team_number;

    /*
        Current Ranking Data
    */
    public int rank;
    public int RPs;
    public int wins;
    public int ties;
    public int loses;
    public int played;

    // Game Specific
    public int auto;
    public int scale_challenge;
    public int goals;
    public int defenses;



    public TRD(){}

}
