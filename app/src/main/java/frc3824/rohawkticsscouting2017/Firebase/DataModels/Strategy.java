package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Class to hold the info for a strategy image
 */
@IgnoreExtraProperties
public class Strategy {

    private final static String TAG = "Strategy";

    public String name;
    public String filepath;
    public String url;

    public Strategy() {}

}
