package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class TeamPilotData {

    public int team_number;
    public LowLevelStats rating;
    public LowLevelStats lifts;
    public LowLevelStats drops;

    public TeamPilotData(){
        rating = new LowLevelStats();
        lifts = new LowLevelStats();
        drops = new LowLevelStats();
    }
}
