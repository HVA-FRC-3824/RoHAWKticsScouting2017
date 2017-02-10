package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
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

    public Team() {
        completed_matches = new HashMap<>();
        info = new TeamLogistics();
        pit = new TeamPitData();
        drive_team_feedback = new TeamDTFeedback();
        calc = new TeamCalculatedData();
        current_ranking = new TeamRankingData();
        predicted_ranking = new TeamRankingData();
        first_pick = new TeamPickAbility();
        second_pick = new TeamPickAbility();
        third_pick = new TeamPickAbility();
    }
}
