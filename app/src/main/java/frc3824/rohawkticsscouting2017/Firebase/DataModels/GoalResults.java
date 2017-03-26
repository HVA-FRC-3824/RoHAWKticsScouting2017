package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class GoalResults {
    public LowLevelStats made;
    public LowLevelStats missed;

    public GoalResults(){
        made = new LowLevelStats();
        missed = new LowLevelStats();
    }
}
