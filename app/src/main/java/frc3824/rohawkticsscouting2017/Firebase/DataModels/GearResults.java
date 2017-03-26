package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class GearResults {

    public GearLocationResults total;
    public GearLocationResults near;
    public GearLocationResults center;
    public GearLocationResults far;

    public GearResults(){
        total = new GearLocationResults();
        near = new GearLocationResults();
        center = new GearLocationResults();
        far = new GearLocationResults();
    }

}
