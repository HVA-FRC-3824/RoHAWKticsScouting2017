package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * Class to hold all information about a specific team
 */
@IgnoreExtraProperties
public class Team {

    private final static String TAG = "Team";

    public int team_number;

    // Collected data
    public Map<Integer, TeamMatchData> completed_matches;
    public TeamLogistics info;
    public TeamPitData pit;
    public TeamDTFeedback drive_team_feedback;

    public TeamCalculatedData calc;

    // Rankings
    public TeamRankingData current_ranking;
    public TeamRankingData predicted_ranking;

    // Picks
    public TeamPickAbility first_pick;
    public TeamPickAbility second_pick;
    public TeamPickAbility third_pick;

    public Team() {}
}
