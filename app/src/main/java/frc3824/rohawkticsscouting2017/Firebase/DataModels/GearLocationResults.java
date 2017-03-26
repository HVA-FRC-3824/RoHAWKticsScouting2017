package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class GearLocationResults {

    public LowLevelStats placed;
    public LowLevelStats dropped;

    public GearLocationResults(){
        placed = new LowLevelStats();
        dropped = new LowLevelStats();
    }
}
