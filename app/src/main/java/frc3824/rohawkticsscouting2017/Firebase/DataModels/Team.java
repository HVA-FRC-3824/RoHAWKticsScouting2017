package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.Map;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
public class Team {

    private final static String TAG = "Team";

    public int team_number;

    public Map<Integer, TMD> completed_matches;
    public TPD pit;
    public TCD calc;
    public TID info;
    public TRD current_ranking;
    public TRD predicted_ranking;

    public Team() {}
}
