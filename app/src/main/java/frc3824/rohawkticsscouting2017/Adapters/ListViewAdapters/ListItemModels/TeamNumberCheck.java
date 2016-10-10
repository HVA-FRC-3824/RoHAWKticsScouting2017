package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels;

/**
 * @author frc3824
 * Created: 10/9/16
 *
 *
 */
public class TeamNumberCheck {

    private final static String TAG = "TeamNumberCheck";

    public int team_number;
    public boolean check;

    public TeamNumberCheck(int team_number){
        this.team_number = team_number;
        this.check = false;
    }

    public TeamNumberCheck(int team_number, boolean check){
        this.team_number = team_number;
        this.check = check;
    }
}
