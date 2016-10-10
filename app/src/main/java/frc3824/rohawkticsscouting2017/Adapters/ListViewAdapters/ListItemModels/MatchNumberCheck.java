package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels;

/**
 * @author frc3824
 * Created: 10/9/16
 *
 *
 */
public class MatchNumberCheck {

    private final static String TAG = "MatchNumberCheck";

    public int match_number;
    public boolean check;

    public MatchNumberCheck(int match_number){
        this.match_number = match_number;
        this.check = false;
    }

    public MatchNumberCheck(int match_number, boolean check){
        this.match_number = match_number;
        this.check = check;
    }

}
