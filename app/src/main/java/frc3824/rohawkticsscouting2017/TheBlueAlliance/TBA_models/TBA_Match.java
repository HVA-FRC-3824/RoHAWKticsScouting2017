package frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models;

/**
 * @author Andrew Messing
 *         Created: 8/14/16
 */
public class TBA_Match {

    private final static String TAG = "TBA_Match";

    public String comp_level;
    public int match_number;
    public Video[] videos;
    public Object time_string;
    public int set_number;
    public String key;
    public int time;
    public Alliances alliances;
    public String event_key;

    public static class Alliances {
        public Alliance blue;
        public Alliance red;
    }

    public static class Alliance {
        public int score;
        public String[] teams;
    }

    public static class Video
    {
        public String type;
        public String key;
    }
}
