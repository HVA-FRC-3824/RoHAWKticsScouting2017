package frc3824.rohawkticsscouting2017.Statistics;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Alliance;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 *         Created: 8/19/16
 */
public class TeamCalculations {

    private final static String TAG = "TeamCalculations";

    private Team mTeam;
    private Database mDatabase;

    public TeamCalculations(Team team)
    {
        mTeam = team;
        mDatabase = Database.getInstance();
    }

    public double autoAbility()
    {
        return 0.0;
    }

    public double std_autoAbility()
    {
        return 0.0;
    }

    public int numberOfCompletedMatches()
    {
        return mTeam.completed_matches.size();
    }

    /**
     * Predicts the number of ranking points at the end of qualifications using the actual ranking
     * points from the completed matches and predicting ranking points acquired from the remaining
     * ones.
     *
     * Note:
     * Currently set up based on 2 for wins, 1 for ties, and 0 for loses. Additional RP will
     * need to be added.
     *
     * @return predicted number of ranking points at the end of qualifications
     */
    public int predictedRankingPoints()
    {
        int actualRPs = 0;

        // Make sure the matches are in order
        Collections.sort(mTeam.info.match_numbers);

        for(int i = 0; i < mTeam.completed_matches.size(); i++)
        {
            Match match = mDatabase.getMatch(mTeam.info.match_numbers.get(i));
            try {
                if (match.isBlue(mTeam.team_number)) {
                    if (match.scores.get(Constants.Match_Indices.BLUE_ALLIANCE) > match.scores.get(Constants.Match_Indices.RED_ALLIANCE)) {
                        actualRPs += 2;
                    } else if (match.scores.get(Constants.Match_Indices.BLUE_ALLIANCE) == match.scores.get(Constants.Match_Indices.RED_ALLIANCE)) {
                        actualRPs++;
                    }
                }
                else {
                    if (match.scores.get(Constants.Match_Indices.RED_ALLIANCE) > match.scores.get(Constants.Match_Indices.BLUE_ALLIANCE)) {
                        actualRPs += 2;
                    } else if (match.scores.get(Constants.Match_Indices.BLUE_ALLIANCE) == match.scores.get(Constants.Match_Indices.RED_ALLIANCE)) {
                        actualRPs++;
                    }
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }

        int predictedRPs = 0;
        for (int i = mTeam.completed_matches.size(); i < mTeam.info.match_numbers.size(); i++)
        {
            Match match = mDatabase.getMatch(mTeam.info.match_numbers.get(i));
            Alliance blueAlliance = new Alliance();
            blueAlliance.teams = new Team[3];
            for(int j = 0; j < 3; j++)
            {
                blueAlliance.teams[j] = mDatabase.getTeam(match.teams.get(j));
            }

            Alliance redAlliance = new Alliance();
            redAlliance.teams = new Team[3];
            for(int j = 3; j < 6; j++)
            {
                redAlliance.teams[j] = mDatabase.getTeam(match.teams.get(j));
            }

            try {
                if (match.isBlue(mTeam.team_number)) {
                    AllianceCalculations ac = new AllianceCalculations(blueAlliance);
                    // Only predict wins not ties
                    if(ac.winProbabilityOver(redAlliance) > .5)
                    {
                        predictedRPs += 2;
                    }
                } else {
                    AllianceCalculations ac = new AllianceCalculations(redAlliance);
                    // Only predict wins not ties
                    if(ac.winProbabilityOver(blueAlliance) > .5)
                    {
                        predictedRPs += 2;
                    }
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }

        return actualRPs + predictedRPs;
    }

    /**
     * Predict the first tie breaker for rankings. Use the actual value for matches that are complete
     * and predict the value for ones that are not.
     *
     * @return
     */
    public int predictFirstTieBreaker()
    {
        return 0;
    }

    /**
     * Predict the second tie breaker for rankings. Use the actual value for matches that are complete
     * and predict the values for ones that are not.
     *
     * @return
     */
    public int predictSecondTieBreaker()
    {
        return 0;
    }

    /**
     * Calculate the first pick ability which is the predicted offensive score that the team can
     * contribute combined with our team.
     *
     * @return
     */
    public double firstPickAbility()
    {
        return 0.0;
    }

    /**
     * Calculate the second pick ability
     *
     * @return
     */
    public double secondPickAbility()
    {
        return 0.0;
    }

    /**
     * Calculate the third pick ability
     *
     * @return
     */
    public double thirdPickAbility()
    {
        return 0.0;
    }
}
