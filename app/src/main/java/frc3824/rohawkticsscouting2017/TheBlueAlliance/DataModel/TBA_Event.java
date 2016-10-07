package frc3824.rohawkticsscouting2017.TheBlueAlliance.DataModel;

/**
 * @author frc3824
 * Created: 8/14/16
 *
 * Data Model to hold event data from The Blue Alliance
 */
public class TBA_Event {

    private final static String TAG = "TBA_Event";

    public String key;
    public String website;
    public boolean official;
    public String end_date;
    public String name;
    public String short_name;
    public Object facebook_eid;
    public Object event_district_string;
    public String venue_address;
    public int event_district;
    public String location;
    public String event_code;
    public int year;
    public Webcast[] webcast;
    public Alliance[] alliances;
    public String event_type_string;
    public String start_date;
    public int event_type;

    public static class Alliance {
        public Object[] declines;
        public String[] picks;
    }

    public static class Webcast {
        public String type;
        public String channel;
        public String file;
    }
}
