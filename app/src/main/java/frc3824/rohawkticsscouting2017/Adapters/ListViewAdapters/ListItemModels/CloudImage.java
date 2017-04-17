package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Data Structure for holding information for uploading/downloading images
 */
public class CloudImage {

    private final static String TAG = "CloudImage";

    public String extra;

    public String filepath;

    public boolean local;
    public boolean remote;
    public boolean internet;

    public CloudImage(){}

}
