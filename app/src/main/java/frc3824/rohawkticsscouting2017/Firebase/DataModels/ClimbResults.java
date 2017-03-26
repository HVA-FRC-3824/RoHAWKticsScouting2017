package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class ClimbResults {

    public int success;
    public int failed;
    public int fell;
    public int no_attempt;
    public int foul_credit;
    public int total;

    public double success_percentage;

    public LowLevelStats time;

    public ClimbResults(){
        time = new LowLevelStats();
    }
}
