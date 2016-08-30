package frc3824.rohawkticsscouting2017.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Messing
 *         Created: 8/30/16
 */
public class ZscoreTeam {

    private final static String TAG = "ZscoreTeam";

    public int team_number;

    public List<Integer> speed_values;
    public double speed_avg;
    public double speed_zscore;
    public int speed_rank;

    public List<Integer> torque_values;
    public double torque_avg;
    public double torque_zscore;
    public int torque_rank;

    public List<Integer> control_values;
    public double control_avg;
    public double control_zscore;
    public int control_rank;

    public List<Integer> defense_values;
    public double defense_avg;
    public double defense_zscore;
    public int defense_rank;

    public ZscoreTeam(int team_number)
    {
        this.team_number = team_number;

        speed_avg = 0.0;
        torque_avg = 0.0;
        control_avg = 0.0;
        defense_avg = 0.0;

        speed_values = new ArrayList<>();
        torque_values = new ArrayList<>();
        control_values = new ArrayList<>();
        defense_values = new ArrayList<>();
    }

    public void average()
    {
        for(int i = 0; i < speed_values.size(); i++)
        {
            speed_avg += speed_values.get(i);
            torque_avg += torque_values.get(i);
            control_avg += control_values.get(i);
            defense_avg += defense_values.get(i);
        }

        speed_avg /= speed_values.size();
        torque_avg /= torque_values.size();
        control_avg /= control_values.size();
        defense_avg /= defense_values.size();
    }
}
