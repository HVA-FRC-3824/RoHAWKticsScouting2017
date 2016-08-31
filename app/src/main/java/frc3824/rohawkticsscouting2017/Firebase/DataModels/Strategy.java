package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author Andrew Messing
 *         Created: 8/20/16
 */
@IgnoreExtraProperties
public class Strategy {

    private final static String TAG = "Strategy";

    public String name;
    public String filepath;
    public String url;

    public Strategy() {}

}
