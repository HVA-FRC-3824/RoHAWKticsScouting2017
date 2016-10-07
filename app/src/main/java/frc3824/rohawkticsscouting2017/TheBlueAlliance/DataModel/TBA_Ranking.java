package frc3824.rohawkticsscouting2017.TheBlueAlliance.DataModel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/22/16
 *
 * Data Model to hold ranking data from The Blue Alliance
 */
public class TBA_Ranking {

    private final static String TAG = "TBA_Ranking";

    public int team_number;
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

    public TBA_Ranking(){}

    public TBA_Ranking(JSONArray jsonArray)
    {
        try {
            rank = Integer.parseInt(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.RANK));
            team_number = Integer.parseInt(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.TEAM_NUMBER));
            RPs = (int)Double.parseDouble(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.RPs));
            played = (int)Double.parseDouble(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.PLAYED));

            auto = (int)Double.parseDouble(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.AUTO));
            scale_challenge = (int)Double.parseDouble(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.SCALE_CHALLENGE));
            goals = (int)Double.parseDouble(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.GOALS));
            defenses = (int)Double.parseDouble(jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.DEFENSE));

            String win_tie_lose = jsonArray.getString(Constants.The_Blue_Alliance.Ranking_Indices.RECORD);

            int firstDash = win_tie_lose.indexOf('-');
            int secondDash = win_tie_lose.lastIndexOf('-');

            String wins_string = win_tie_lose.substring(0, firstDash);
            String ties_string = win_tie_lose.substring(firstDash + 1, secondDash);
            String loses_string = win_tie_lose.substring(secondDash + 1);

            wins = Integer.parseInt(wins_string);
            ties = Integer.parseInt(ties_string);
            loses = Integer.parseInt(loses_string);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
