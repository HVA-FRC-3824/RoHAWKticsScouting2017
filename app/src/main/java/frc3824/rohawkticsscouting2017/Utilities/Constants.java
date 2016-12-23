package frc3824.rohawkticsscouting2017.Utilities;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Pair;

/**
 * @author frc3824
 * Created: 8/10/16
 *
 * Interface holding constants
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

    interface Settings {
        String EVENT_KEY = "event_key";
        String USER_TYPE = "user_type";
        String ALLIANCE_COLOR = "alliance_color";
        String ALLIANCE_NUMBER = "alliance_number";
        String PIT_GROUP_NUMBER = "pit_group_number";
        String SERVER = "server";

        // Not really settings, but are in shared preferences
        String LAST_MATCH_SCOUT = "last_match_scout";
        String LAST_PIT_SCOUT = "last_pit_scout";
        String LAST_SUPER_SCOUT = "last_super_scout";
    }

    interface User_Types {
        String MATCH_SCOUT = "Match Scout";
        String PIT_SCOUT = "Pit Scout";
        String SUPER_SCOUT = "Super Scout";
        String DRIVE_TEAM = "Drive Team";
        String STRATEGY = "Strategy";
        String SERVER = "Server";
        String ADMIN = "Admin";
    }

    interface Alliance_Colors {
        String BLUE = "Blue";
        String RED = "Red";
    }

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

    interface Intent_Extras {
        String TEAM_NUMBER = "team_number";
        String MATCH_NUMBER = "match_number";
        String NEXT_PAGE = "next_page";
        String MATCH_SCOUTING = "match_scouting";
        String SUPER_SCOUTING = "super_scouting";
        String DRIVE_TEAM_FEEDBACK = "drive_team_feedback";
        String PIT_SCOUTING = "pit_scouting";
        String MATCH_VIEWING = "match_viewing";
        String TEAM_VIEWING = "team_viewing";
        String MATCH_PLAN_NAME = "match_plan_name";

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

        interface Ranking_Indices {
            int RANK = 0;
            int TEAM_NUMBER = 1;
            int RPs = 2;
            int AUTO = 3;
            int SCALE_CHALLENGE = 4;
            int GOALS = 5;
            int DEFENSE = 6;
            int RECORD = 7;
            int PLAYED = 8;
        }
    }

    interface Match_Scouting {

        String SCOUT_NAME = "scout_name";

        interface Autonomous {
            // GAME SPECIFIC
        }

        interface Teleop {
            // GAME SPECIFIC
        }

        interface Endgame {
            // GAME SPECIFIC
        }

        interface Fouls {
            String FOUL = "foul_standard";
            String TECH_FOUL = "foul_tech";
            String YELLOW_CARD = "foul_yellow_card";
            String RED_CARD = "foul_red_card";
        }

        interface PostMatch {
            String DQ = "post_dq";
            String STOPPED_MOVING = "post_stopped";
            String NO_SHOW = "post_no_show";
            String NOTES = "post_notes";
        }
    }

    interface Pit_Scouting {
        String SCOUT_NAME = "scout_name";
        String PIT_SCOUTED = "pit_scouted";

        String ROBOT_PICTURE_DEFAULT = "robot_picture_default";
        String ROBOT_PICTURE_FILEPATHS = "robot_picture_filepaths";
        String ROBOT_PICTURE_URLS = "robot_picture_urls";

        interface Dimensions {
            String WIDTH = "width";
            String LENGTH = "length";
            String HEIGHT = "height";
            String WEIGHT = "weight";
        }

        interface Miscellaneous {
            String PROGRAMMING_LANGUAGE = "programming_language";

            // GAME SPECIFIC
        }

        String NOTES = "pit_notes";
    }

    interface Super_Scouting {

        String SCOUT_NAME = "scout_name";

        // GAME SPECIFIC


        interface Qualitative {
            String BLUE_SPEED = "blue_speed";
            String RED_SPEED = "red_speed";

            String BLUE_TORQUE = "blue_torque";
            String RED_TORQUE = "red_torque";

            String BLUE_CONTROL = "blue_control";
            String RED_CONTROL = "red_control";

            String BLUE_DEFENSE = "blue_defense";
            String RED_DEFENSE = "red_defense";
        }

        String NOTES = "super_notes";
    }

    interface Team {
        String NICKNAME = "nickname";
    }

    interface Cloud {
        int ROBOT_PICTURE = 0;
        int STRATEGY = 1;
    }

    interface Bluetooth {
        String NAME_SECURE = "SyncSecure";
        String UUID_SECURE = "fa87c0d0-afac-11de-8a39-0800200c9a66";

        String NAME_INSECURE = "SyncInsecure";
        String UUID_INSECURE = "8ce255c0-200a-11e0-ac64-0800200c9a66";

        int SENDING = 0;
        int RECEIVING = 1;

        int CHUNK_SIZE = 4192;
        int HEADER_MSB = 0x10;
        int HEADER_LSB = 0x55;

        interface Message_Type
        {
            int DATA_SENT_OK = 0x00;
            int DATA_RECEIVED = 0x02;
            int SENDING_DATA = 0x04;

            int DIGEST_DID_NOT_MATCH = 0x50;
            int COULD_NOT_CONNECT = 0x51;
            int INVALID_HEADER = 0x52;
            int CONNECTION_LOST = 0x53;
            int NEW_CONNECTION = 0x54;
        }

        interface Message_Headers
        {
            char MATCH_HEADER = 'M';
            char PIT_HEADER = 'P';
            char SUPER_HEADER = 'S';
            char CALC_HEADER = 'C';
            char FEEDBACK_HEADER = 'F';
        }

        interface Device_Names
        {
            String BLUE1 = "3824_Blue1";
            String BLUE2 = "3824_Blue2";
            String BLUE3 = "3824_Blue3";
            String RED1 = "3824_Red1";
            String RED2 = "3824_Red2";
            String RED3 = "3824_Red3";
            String SUPER = "3824_Super_Scout";
            String SERVER = "3824_Server";
            String STRATEGY = "3824_Strategy";
            String DRIVETEAM = "3824_Drive_Team";
        }

        interface Data_Transfer_Status
        {
            int NO_BLUETOOTH = 0;
            int SERVER_NOT_FOUND = 1;
            int SUCCESS = 2;
            int QUEUE_EMPTIED = 3;
            int FAILURE = 4;
        }
    }

    interface Notifications {
        int MATCH_RECIEVED = 1;
        int SUPER_RECIEVED = 2;
        int UPLOAD_STRATEGIES = 3;
        int DOWNLOAD_STRATEGIES = 4;
        int UPLOAD_ROBOT_PICTURES = 5;
        int DOWNLOAD_ROBOT_PICTURES = 6;
    }

    interface Server_Log_Colors {
        String RED = "red";
        String BLUE = "blue";
        String BLACK = "black";
        String GREEN = "green";
        String YELLOW = "yellow";
        String WHITE = "white";
    }

    interface Event_View {
        interface Main_Dropdown_Options {
            String FOULS = "Fouls";
            String POST_MATCH = "Post Match";

            // GAME SPECIFIC

            String[] OPTIONS = {FOULS, POST_MATCH};
        }

        interface Foul_Secondary_Options {
            String STANDARD_FOULS = "Standard Fouls";
            String TECH_FOULS = "Tech Fouls";
            String YELLOW_CARDS = "Yellow Cards";
            String RED_CARDS = "Red Cards";

            String[] OPTIONS = {STANDARD_FOULS, TECH_FOULS, YELLOW_CARDS, RED_CARDS};
        }

        interface Post_Match_Secondary_Options {
            String DQ = "DQ";
            String NO_SHOW = "No Show";
            String STOPPED_MOVING = "Stopped Moving";

            String[] OPTIONS = {DQ, NO_SHOW, STOPPED_MOVING};
        }

        // GAME SPECIFIC
    }

    interface StrategyPlanning {
        interface Auto_Endgame {
            int BLUE_X = 125;
            int RED_X = 1400;
            int BLUE1Y = 200;
            int BLUE2Y = 500;
            int BLUE3Y = 800;
            int RED1Y = 800;
            int RED2Y = 500;
            int RED3Y = 200;
            Point BLUE1_POSITION = new Point(BLUE_X, BLUE1Y);
            Point BLUE2_POSITION = new Point(BLUE_X, BLUE2Y);
            Point BLUE3_POSITION = new Point(BLUE_X, BLUE3Y);
            Point RED1_POSITION = new Point(RED_X, RED1Y);
            Point RED2_POSITION = new Point(RED_X, RED2Y);
            Point RED3_POSITION = new Point(RED_X, RED3Y);
            Point[] POSITIONS = {BLUE1_POSITION, BLUE2_POSITION, BLUE3_POSITION, RED1_POSITION, RED2_POSITION, RED3_POSITION};

            float RADIUS = 60.0f;
        }

        interface Teleop {
            int BLUE_X = 125;
            int RED_X = 1400;
            int BLUE1Y = 200;
            int BLUE2Y = 500;
            int BLUE3Y = 800;
            int RED1Y = 800;
            int RED2Y = 500;
            int RED3Y = 200;
            Point BLUE1_POSITION = new Point(BLUE_X, BLUE1Y);
            Point BLUE2_POSITION = new Point(BLUE_X, BLUE2Y);
            Point BLUE3_POSITION = new Point(BLUE_X, BLUE3Y);
            Point RED1_POSITION = new Point(RED_X, RED1Y);
            Point RED2_POSITION = new Point(RED_X, RED2Y);
            Point RED3_POSITION = new Point(RED_X, RED3Y);
            Point[] POSITIONS = {BLUE1_POSITION, BLUE2_POSITION, BLUE3_POSITION, RED1_POSITION, RED2_POSITION, RED3_POSITION};

            float RADIUS = 60.0f;
        }

        int BLUE1_COLOR = Color.rgb(0,191,255);
        int BLUE2_COLOR = Color.rgb(30,144,255);
        int BLUE3_COLOR = Color.rgb(0,0,255);
        int RED1_COLOR = Color.rgb(220,20,60);
        int RED2_COLOR = Color.rgb(255,0,0);
        int RED3_COLOR = Color.rgb(139,0,0);
        int[] COLORS = {BLUE1_COLOR, BLUE2_COLOR, BLUE3_COLOR, RED1_COLOR, RED2_COLOR, RED3_COLOR};
    }
}
