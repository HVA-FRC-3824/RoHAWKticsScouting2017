package frc3824.rohawkticsscouting2017.Utilities;

/**
 * @author Andrew Messing
 *         Created: 8/10/16
 */
public interface Constants {

    String APP_DATA = "appData";

    /**
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

    //Match Indices
    interface Match_Indices {
        int BLUE1 = 0;
        int BLUE2 = 1;
        int BLUE3 = 2;
        int RED1 = 3;
        int RED2 = 4;
        int RED3 = 5;

        int BLUE_ALLIANCE = 0;
        int RED_ALLIANCE = 1;
    }

    // Intent Extras
    interface Intent_Extras {
        String TEAM_NUMBER = "team_number";
        String MATCH_NUMBER = "match_number";
        String NEXT_PAGE = "next_page";
        String MATCH_SCOUTING = "match_scouting";
        String SUPER_SCOUTING = "super_scouting";
        String PIT_SCOUTING = "pit_scouting";
        String MATCH_VIEWING = "match_viewing";
        String TEAM_VIEWING = "team_viewing";

        String BLUE1 = "blue1";
        String BLUE2 = "blue2";
        String BLUE3 = "blue3";
        String RED1 = "red1";
        String RED2 = "red2";
        String RED3 = "red3";
    }

    interface The_Blue_Alliance {
        String TBA_Header_NAME = "X-TBA-App-Id";
        String TBA_Header_VALUE = "frc3824:scouting-system:" + VERSION;

    }

    interface Match_Scouting
    {
        interface Autonomous
        {

        }

        interface Teleop
        {

        }

        interface Endgame
        {

        }

        interface Fouls
        {
            String FOUL = "foul_standard";
            String TECH_FOUL = "foul_tech";
            String YELLOW_CARD = "foul_yellow_card";
            String RED_CARD = "foul_red_card";
        }

        interface PostMatch
        {
            String DQ = "post_dq";
            String STOPPED_MOVING = "post_stopped";
            String NO_SHOW = "post_no_show";
            String NOTES = "post_notes";
        }
    }

    interface Pit_Scouting
    {
        String PIT_SCOUTED = "pit_scouted";

        String ROBOT_PICTURE_FILEPATH = "robot_picture_filepath";

        interface Dimensions
        {
            String WIDTH = "width";
            String LENGTH = "length";
            String HEIGHT = "height";
            String WEIGHT = "weight";
        }

        interface Miscellaneous
        {
            String PROGRAMMING_LANGUAGE = "programming_language";
        }

        String NOTES = "pit_notes";
    }

    interface Super_Scouting
    {
        interface Qualitative
        {
            interface Speed
            {
                String BLUE1 = "blue1_speed";
                String BLUE2 = "blue2_speed";
                String BLUE3 = "blue3_speed";
                String RED1 = "red1_speed";
                String RED2 = "red2_speed";
                String RED3 = "red3_speed";
            }

            interface Pushing_Power
            {
                String BLUE1 = "blue1_pushing_power";
                String BLUE2 = "blue2_pushing_power";
                String BLUE3 = "blue3_pushing_power";
                String RED1 = "red1_pushing_power";
                String RED2 = "red2_pushing_power";
                String RED3 = "red3_pushing_power";
            }

            interface Control
            {
                String BLUE1 = "blue1_control";
                String BLUE2 = "blue2_control";
                String BLUE3 = "blue3_control";
                String RED1 = "red1_control";
                String RED2 = "red2_control";
                String RED3 = "red3_control";
            }
        }

        String NOTES = "super_notes";

    }

    interface Team
    {
        String NICKNAME = "nickname";
    }

    interface Cloud
    {
        int ROBOT_PICTURE = 0;
        int STRATEGY = 1;
    }

}
