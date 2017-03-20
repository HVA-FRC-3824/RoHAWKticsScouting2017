package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class ShootingResults {

    public GoalResults high;
    public GoalResults low;

    public ShootingResults(){
        high = new GoalResults();
        low = new GoalResults();
    }
}