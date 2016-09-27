package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
@IgnoreExtraProperties
public class Team {

    private final static String TAG = "Team";

    public int team_number;

    public Map<Integer, TMD> completed_matches;
    public TID info;
    public TPD pit;
    public TDTF drive_team_feedback;
    public TCD calc;

    // Rankings
    public TRD current_ranking;
    public TRD predicted_ranking;

    // Picks
    public TPA first_pick;
    public TPA second_pick;
    public TPA third_pick;

    public Team() {}
}
