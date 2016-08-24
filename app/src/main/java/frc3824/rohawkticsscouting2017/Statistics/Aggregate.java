package frc3824.rohawkticsscouting2017.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.LowLevelStats;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class Aggregate {

    private final static String TAG = "Aggregate";

    public static void aggregateForTeam(int team_number)
    {
        Database database = Database.getInstance();

        Team team = database.getTeam(team_number);

        // Low level calculations (avg, std, max, min, total)

        /////// GAME SPECIFIC ///////


        /////////////////////////////

        List<Integer> fouls = new ArrayList<>();
        List<Integer> tech_fouls = new ArrayList<>();
        List<Boolean> yellow_cards = new ArrayList<>();
        List<Boolean> red_cards = new ArrayList<>();

        List<Boolean> stopped_moving = new ArrayList<>();
        List<Boolean> no_show = new ArrayList<>();
        List<Boolean> dq = new ArrayList<>();

        for(TMD tmd: team.completed_matches.values())
        {
            /////// GAME SPECIFIC ///////


            /////////////////////////////


            // fouls
            fouls.add(tmd.fouls);
            tech_fouls.add(tmd.tech_fouls);
            yellow_cards.add(tmd.yellow_card);
            red_cards.add(tmd.red_card);

            // misc
            stopped_moving.add(tmd.stopped_moving);
            no_show.add(tmd.no_show);
            dq.add(tmd.dq);
        }

        /////// GAME SPECIFIC ///////


        /////////////////////////////

        team.calc.fouls = LowLevelStats.fromInt(fouls);
        team.calc.tech_fouls = LowLevelStats.fromInt(tech_fouls);
        team.calc.yellow_cards = LowLevelStats.fromBoolean(yellow_cards);
        team.calc.red_cards = LowLevelStats.fromBoolean(red_cards);

        team.calc.stopped_moving = LowLevelStats.fromBoolean(stopped_moving);
        team.calc.no_show = LowLevelStats.fromBoolean(no_show);
        team.calc.dq = LowLevelStats.fromBoolean(dq);

        database.setTCD(team.calc);
    }

    public static void aggregateForSuper(int match_number)
    {
        Database database = Database.getInstance();

        Match match = database.getMatch(match_number);
        SMD smd = database.getSMD(match_number);
        
    }

}
