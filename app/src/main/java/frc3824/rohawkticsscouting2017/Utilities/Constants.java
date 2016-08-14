package frc3824.rohawkticsscouting2017.Utilities;

/**
 * @author Andrew Messing
 *         Created: 8/10/16
 */
public interface Constants {

    String APP_DATA = "appData";

    /*
        Version number changing rules
        - right most number get changed for major changes (after alpha version is finished)
        - middle number is changed after events
        - left most number is changed after the season
     */

    String VERSION = "2.0.1";
    int OUR_TEAM_NUMBER = 3824;

    // Settings
    interface Settings {
        String EVENT_KEY = "event_key";
        String USER_TYPE = "user_type";
        String ALLIANCE_COLOR = "alliance_color";
        String ALLIANCE_NUMBER = "alliance_number";
        String PIT_GROUP_NUMBER = "pit_group_number";
        String SERVER = "server";
    }

    //User Types
    interface User_Types {
        String MATCH_SCOUT = "Match Scout";
        String PIT_SCOUT = "Pit Scout";
        String SUPER_SCOUT = "Super Scout";
        String DRIVE_TEAM = "Drive Team";
        String STRATEGY = "Strategy";
        String SERVER = "Server";
        String ADMIN = "Admin";
    }

    //Alliance Colors
    interface Alliance_Colors {
        String BLUE = "Blue";
        String RED = "Red";
    }

    // Intent Extras
    interface Intent_Extras {
        String TEAM_NUMBER = "team_number";
        String MATCH_NUMBER = "match_number";
        String NEXT_PAGE = "next_page";
        String MATCH_SCOUTING = "match_scouting";
    }

    interface The_Blue_Alliance {
        String TBA_Header_NAME = "X-TBA-App-Id";
        String TBA_Header_VALUE ="frc3824:scouting-system:" + VERSION;

    }

}
