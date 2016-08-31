package frc3824.rohawkticsscouting2017.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.LowLevelStats;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TCD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 *
 *
 */
public class Aggregate {

    private final static String TAG = "Aggregate";

    public static void aggregateForTeam(int team_number)
    {
        Database database = Database.getInstance();

        Team team = database.getTeam(team_number);

        // Low level calculations (avg, std, max, min, total)

        /////////////////////////////////// GAME SPECIFIC ///////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////

        List<Integer> fouls = new ArrayList<>();
        List<Integer> tech_fouls = new ArrayList<>();
        List<Boolean> yellow_cards = new ArrayList<>();
        List<Boolean> red_cards = new ArrayList<>();

        List<Boolean> stopped_moving = new ArrayList<>();
        List<Boolean> no_show = new ArrayList<>();
        List<Boolean> dq = new ArrayList<>();

        for(TMD tmd: team.completed_matches.values())
        {
            /////////////////////////////////// GAME SPECIFIC ///////////////////////////////////


            /////////////////////////////////////////////////////////////////////////////////////


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

        /////////////////////////////////// GAME SPECIFIC ///////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////

        team.calc.fouls = LowLevelStats.fromInt(fouls);
        team.calc.tech_fouls = LowLevelStats.fromInt(tech_fouls);
        team.calc.yellow_cards = LowLevelStats.fromBoolean(yellow_cards);
        team.calc.red_cards = LowLevelStats.fromBoolean(red_cards);

        team.calc.stopped_moving = LowLevelStats.fromBoolean(stopped_moving);
        team.calc.no_show = LowLevelStats.fromBoolean(no_show);
        team.calc.dq = LowLevelStats.fromBoolean(dq);


        TeamCalculations tc = new TeamCalculations(team);

        team.first_pick.pick_ability = tc.firstPickAbility();
        team.second_pick.pick_ability = tc.secondPickAbility();
        team.third_pick.pick_ability = tc.thirdPickAbility();

        if(team.pit.robot_image_filepath != null && team.pit.robot_image_filepath != "")
        {
            team.first_pick.robot_picture_filepath = team.pit.robot_image_filepath;
            team.second_pick.robot_picture_filepath = team.pit.robot_image_filepath;
            team.third_pick.robot_picture_filepath = team.pit.robot_image_filepath;
        }

        /////////////////////////////////// GAME SPECIFIC ///////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////

        team.first_pick.stopped_moving = team.calc.stopped_moving.total > 0;
        team.second_pick.stopped_moving = team.calc.stopped_moving.total > 0;
        team.third_pick.stopped_moving = team.calc.stopped_moving.total > 0;

        team.first_pick.yellow_card = team.calc.yellow_cards.total > 0;
        team.second_pick.yellow_card = team.calc.yellow_cards.total > 0;
        team.third_pick.yellow_card = team.calc.yellow_cards.total > 0;

        team.first_pick.red_card = team.calc.red_cards.total > 0;
        team.second_pick.red_card = team.calc.red_cards.total > 0;
        team.third_pick.red_card = team.calc.red_cards.total > 0;

        database.setTeam(team);
    }

    public static void aggregateForSuper()
    {
        Database database = Database.getInstance();
        Map<Integer, ZscoreTeam> teams = new HashMap<>();

        for(SMD smd : database.getSMDs().values())
        {
            for(int i = 0; i < 3; i++) {
                if (!teams.containsKey(smd.blue_speed.get(i))) {
                    teams.put(smd.blue_speed.get(i), new ZscoreTeam(smd.blue_speed.get(i)));
                }
                if (!teams.containsKey(smd.red_speed.get(i))) {
                    teams.put(smd.red_speed.get(i), new ZscoreTeam(smd.red_speed.get(i)));
                }
            }

            teams.get(smd.blue_speed.get(0)).speed_values.add(4);
            teams.get(smd.red_speed.get(0)).speed_values.add(4);

            teams.get(smd.blue_torque.get(0)).speed_values.add(4);
            teams.get(smd.red_torque.get(0)).speed_values.add(4);

            teams.get(smd.blue_control.get(0)).speed_values.add(4);
            teams.get(smd.red_control.get(0)).speed_values.add(4);

            teams.get(smd.blue_defense.get(0)).speed_values.add(4);
            teams.get(smd.red_defense.get(0)).speed_values.add(4);

            for(int i = 1; i < 3; i++)
            {
                teams.get(smd.blue_speed.get(i)).speed_values.add(3 - i);
                teams.get(smd.red_speed.get(i)).speed_values.add(3 - i);

                teams.get(smd.blue_torque.get(i)).speed_values.add(3 - i);
                teams.get(smd.red_torque.get(i)).speed_values.add(3 - i);

                teams.get(smd.blue_control.get(i)).speed_values.add(3 - i);
                teams.get(smd.red_control.get(i)).speed_values.add(3 - i);

                teams.get(smd.blue_defense.get(i)).speed_values.add(3 - i);
                teams.get(smd.red_defense.get(i)).speed_values.add(3 - i);
            }
        }

        double competition_speed_avg = 0.0;
        double competition_torque_avg = 0.0;
        double competition_control_avg = 0.0;
        double competition_defense_avg = 0.0;

        for(ZscoreTeam z: teams.values())
        {
            z.average();
            competition_speed_avg += z.speed_avg;
            competition_torque_avg += z.torque_avg;
            competition_control_avg += z.control_avg;
            competition_defense_avg += z.defense_avg;
        }

        competition_speed_avg /= teams.size();
        competition_torque_avg /= teams.size();
        competition_control_avg /= teams.size();
        competition_defense_avg /= teams.size();
        
        double competition_speed_std = 0.0;
        double competition_torque_std = 0.0;
        double competition_control_std = 0.0;
        double competition_defense_std = 0.0;
        

        for(ZscoreTeam z: teams.values())
        {
            competition_speed_std += Math.pow(z.speed_avg - competition_speed_avg, 2);
            competition_torque_std += Math.pow(z.torque_avg - competition_torque_avg, 2);
            competition_control_std += Math.pow(z.control_avg - competition_control_avg, 2);
            competition_defense_std += Math.pow(z.defense_avg - competition_defense_avg, 2);
        }

        competition_speed_std /= teams.size();
        competition_torque_std /= teams.size();
        competition_control_std /= teams.size();
        competition_defense_std /= teams.size();

        competition_speed_std = Math.sqrt(competition_speed_std);
        competition_torque_std = Math.sqrt(competition_torque_std);
        competition_control_std = Math.sqrt(competition_control_std);
        competition_defense_std = Math.sqrt(competition_defense_std);

        for(ZscoreTeam z: teams.values())
        {
            z.speed_zscore = (z.speed_avg - competition_speed_avg) / competition_speed_std;
            z.torque_zscore = (z.torque_avg - competition_torque_avg) / competition_torque_std;
            z.control_zscore = (z.control_avg - competition_control_avg) / competition_control_std;
            z.defense_zscore = (z.defense_avg - competition_defense_avg) / competition_defense_std;
        }

        ArrayList<ZscoreTeam> teams_list = new ArrayList<>(teams.values());
        Collections.sort(teams_list, new Comparator<ZscoreTeam>() {
            @Override
            public int compare(ZscoreTeam z1, ZscoreTeam z2) {
                return Double.compare(z1.speed_zscore, z2.speed_zscore);
            }
        });
        for(int i = 0; i < teams_list.size(); i++)
        {
            teams_list.get(i).speed_rank = i + 1;
        }

        Collections.sort(teams_list, new Comparator<ZscoreTeam>() {
            @Override
            public int compare(ZscoreTeam z1, ZscoreTeam z2) {
                return Double.compare(z1.torque_zscore, z2.torque_zscore);
            }
        });
        for(int i = 0; i < teams_list.size(); i++)
        {
            teams_list.get(i).torque_rank = i + 1;
        }

        Collections.sort(teams_list, new Comparator<ZscoreTeam>() {
            @Override
            public int compare(ZscoreTeam z1, ZscoreTeam z2) {
                return Double.compare(z1.control_zscore, z2.control_zscore);
            }
        });
        for(int i = 0; i < teams_list.size(); i++)
        {
            teams_list.get(i).control_rank = i + 1;
        }

        Collections.sort(teams_list, new Comparator<ZscoreTeam>() {
            @Override
            public int compare(ZscoreTeam z1, ZscoreTeam z2) {
                return Double.compare(z1.defense_zscore, z2.defense_zscore);
            }
        });
        for(int i = 0; i < teams_list.size(); i++)
        {
            ZscoreTeam z = teams_list.get(i);
            z.defense_rank = i + 1;
            TCD tcd = database.getTCD(z.team_number);

            tcd.rank_speed = z.speed_rank;
            tcd.zscore_speed = z.speed_zscore;

            tcd.rank_torque = z.torque_rank;
            tcd.zscore_torque = z.torque_zscore;

            tcd.rank_control = z.control_rank;
            tcd.zscore_control = z.control_zscore;

            tcd.rank_defense = z.defense_rank;
            tcd.zscore_defense = z.defense_zscore;

            database.setTCD(tcd);
        }
    }
}