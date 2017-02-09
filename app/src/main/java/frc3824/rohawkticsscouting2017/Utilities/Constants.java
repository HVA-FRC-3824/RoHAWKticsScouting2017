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
        String SERVER_TYPE = "server";

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
        String ADMIN = "Admin";

        String[] LIST = {MATCH_SCOUT, PIT_SCOUT, SUPER_SCOUT, DRIVE_TEAM, STRATEGY, ADMIN};
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
        String LAST_MODIFIED = "last_modified";
        String NEXT_PAGE = "next_page";
        String MATCH_SCOUTING = "match_scouting";
        String SUPER_SCOUTING = "super_scouting";
        String DRIVE_TEAM_FEEDBACK = "drive_team_feedback";
        String PIT_SCOUTING = "pit_scouting";
        String MATCH_VIEWING = "match_viewing";
        String TEAM_VIEWING = "team_viewing";
        String MATCH_PLAN_NAME = "match_plan_name";
        String SCOUTER = "scouter";

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
            String AUTO_START_POSITION = "auto_start_position";
            String AUTO_BASELINE = "auto_baseline";
            String AUTO_GEARS = "auto_gears";
            String AUTO_HIGH_GOAL_MADE = "auto_high_goal_made";
            String AUTO_HIGH_GOAL_MISSED = "auto_high_goal_missed";
            String AUTO_HIGH_GOAL_CORRECTION = "auto_high_goal_correction";
            String AUTO_LOW_GOAL_MADE = "auto_low_goal_made";
            String AUTO_LOW_GOAL_MISSED = "auto_low_goal_missed";
            String AUTO_LOW_GOAL_CORRECTION = "auto_low_goal_correction";
            String AUTO_HOPPERS = "auto_hoppers";
        }

        interface Teleop {
            // GAME SPECIFIC

            String TELEOP_GEARS = "teleop_gears";
            String TELEOP_HIGH_GOAL_MADE = "teleop_high_goal_made";
            String TELEOP_HIGH_GOAL_MISSED = "teleop_high_goal_missed";
            String TELEOP_HIGH_GOAL_CORRECTION = "teleop_high_goal_correction";
            String TELEOP_LOW_GOAL_MADE = "teleop_low_goal_made";
            String TELEOP_LOW_GOAL_MISSED = "teleop_low_goal_missed";
            String TELEOP_LOW_GOAL_CORRECTION = "teleop_low_goal_correction";
            String TELEOP_HOPPERS = "teleop_hoppers";
            String TELEOP_PICKED_UP_GEARS = "teleop_picked_up_gears";
        }

        interface Endgame {
            // GAME SPECIFIC
            String ENDGAME_CLIMB = "endgame_climb";
            interface CLIMB_OPTIONS {
                String NO_ATTEMPT = "No attempt";
                String DID_NOT_FINISH_IN_TIME = "Did not finish in time";
                String ROBOT_FELL = "Robot fell";
                String SUCCESSFUL = "Successful";
                String[] LIST = {SUCCESSFUL, ROBOT_FELL, DID_NOT_FINISH_IN_TIME, NO_ATTEMPT};
            }

            String ENDGAME_CLIMB_TIME = "endgame_climb_time";
            interface CLIMB_TIME_OPTIONS {
                String N_A = "N/A";
                String LESS_THAN_5S = "< 5s";
                String LESS_THAN_10S = "< 10s";
                String LESS_THAN_15S = "< 15s";
                String LESS_THAN_20S = "< 20s";
                String LESS_THAN_25S = "< 25s";
                String LESS_THAN_30S = "< 30s";
                String[] LIST = {N_A, LESS_THAN_5S, LESS_THAN_10S, LESS_THAN_15S, LESS_THAN_20S,
                    LESS_THAN_25S, LESS_THAN_30S};
            }
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
            // TAGS
            String TAGS = "post_tags";
            interface Tags{
                String BLOCK_SHOTS = "_blocked_shots";
                String PINNED_ROBOT = "_pinned_robot";
                String DEFENDED_LOADING_STATION = "_defended_loading_station";
                String DEFENDED_AIRSHIP = "_defended_airship";
                String BROKE = "_broke";
                String DUMPED_ALL_HOPPERS = "_dumped_all_hoppers";
            }
        }

        interface Custom {
            interface Gears{
                String FAR = "far";
                String CENTER = "center";
                String NEAR = "near";
                String[] LOCATIONS = {FAR, CENTER, NEAR};
            }
        }
    }

    interface Pit_Scouting {
        String SCOUT_NAME = "scout_name";

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
            interface Programming_Languages {
                String CPP = "C++";
                String JAVA = "Java";
                String LABVIEW = "Labview";
                String PYTHON = "Python";
                String CSHARP = "C#";

                String[] PROGRAMMING_LANGUAGES = {CPP, JAVA, LABVIEW, PYTHON, CSHARP};
            }
            String DRIVE_TRAIN = "drive_train";
            interface Drive_Trains {
                String TANK_4_WHEEL = "Tank (4 wheel)";
                String TANK_6_WHEEL = "Tank (6 wheel)";
                String TANK_8_WHEEL = "Tank (8 wheel)";
                String TANK_TREAD = "Tank (tread)";
                String MECANUM = "Mecanum";
                String SWERVE = "Swerve";

                String[] DRIVE_TRAINS = {TANK_4_WHEEL, TANK_6_WHEEL, TANK_8_WHEEL, TANK_TREAD, MECANUM, SWERVE};
            }
            String CIMS = "cims";


            String MAX_HOPPER_LOAD = "max_hopper_load";
            String CHOSEN_VOLUME = "chosen_volume";
            interface Volumes {
                String SHORT = "Short (40in x 36in x 24in)";
                String TALL = "Tall (30in x 32in x 36in)";
                String[] VOLUMES = {SHORT, TALL};
            }
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

    interface Socket {
        String SERVER = "Socket";
        int PORT = 38240;
    }

    interface Comms {
        String NAME_SECURE = "SyncSecure";
        String UUID_SECURE = "fa87c0d0-afac-11de-8a39-0800200c9a66";

        String NAME_INSECURE = "SyncInsecure";
        String UUID_INSECURE = "8ce255c0-200a-11e0-ac64-0800200c9a66";

        int SENDING = 0;
        int RECEIVING = 1;

        int CHUNK_SIZE = 4192;
        int HEADER_MSB = 0x10;
        int HEADER_LSB = 0x55;

        interface Message_Type {
            int DATA_SENT_OK = 0x00;
            int DATA_RECEIVED = 0x02;
            int SENDING_DATA = 0x04;

            int DIGEST_DID_NOT_MATCH = 0x50;
            int COULD_NOT_CONNECT = 0x51;
            int INVALID_HEADER = 0x52;
            int CONNECTION_LOST = 0x53;
            int NEW_CONNECTION = 0x54;
        }

        interface Message_Headers {
            char MATCH_HEADER = 'M';
            char PIT_HEADER = 'P';
            char SUPER_HEADER = 'S';
            char CALC_HEADER = 'C';
            char FEEDBACK_HEADER = 'F';
            char SYNC_HEADER = 'R';
            char STRATEGY_HEADER = 'T';
            char STRATEGY_SUGGESTION_HEADER = 'U';
        }

        interface Device_Names {
            String BLUE1 = "3824_Blue1";
            String BLUE2 = "3824_Blue2";
            String BLUE3 = "3824_Blue3";
            String RED1 = "3824_Red1";
            String RED2 = "3824_Red2";
            String RED3 = "3824_Red3";
            String SUPER = "3824_Super_Scout";
            String SPARE = "3824_Spare";
            String STRATEGY = "3824_Strategy";
            String DRIVETEAM = "3824_Drive_Team";
            String RED_PI = "raspberrypi";
        }

        interface Data_Transfer_Status {
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

    interface Server_Type {
        String BLUETOOTH = "Bluetooth";
        String SOCKET = "Socket";
    }

    interface Server_Log_Colors {
        String RED = "red";
        String BLUE = "blue";
        String BLACK = "black";
        String GREEN = "green";
        String YELLOW = "yellow";
        String WHITE = "white";
    }

    interface Team_View {
        interface Gear_Options {
            String NEAR_PLACED = "Near - Placed";
            String NEAR_DROPPED = "Near - Dropped";
            String CENTER_PLACED = "Center - Placed";
            String CENTER_DROPPED = "Center - Dropped";
            String FAR_PLACED = "Far - Placed";
            String FAR_DROPPED = "Far - Dropped";
            String[] LIST = {NEAR_PLACED, NEAR_DROPPED, CENTER_PLACED, CENTER_DROPPED, FAR_PLACED, FAR_DROPPED};
        }
    }

    interface Event_View {
        interface Main_Dropdown_Options {
            String POINTS = "Points";
            String FOULS = "Fouls";
            String POST_MATCH = "Post Match";

            // GAME SPECIFIC
            String GEARS = "Gears";
            String SHOOTING = "Shooting";
            String CLIMB = "Climb";

            String[] OPTIONS = {FOULS, POST_MATCH};
        }

        interface Points_Secondary_Options {
            String TOTAL = "Total";
            String AUTO = "Auto";
            String TELEOP = "Teleop";
            String ENDGAME = "Endgame";

            String[] OPTIONS = {TOTAL, AUTO, TELEOP, ENDGAME};
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
        interface Gears_Secondary_Options {
            
            String BOTH = "Both ";
            String AUTO = "Auto ";
            String TELEOP = "Teleop ";
            
            String NEAR = "Near ";
            String CENTER = "Center ";
            String FAR = "Far ";
            
            String PLACED = "Placed";
            String DROPPED = "Dropped";
            
            String BOTH_PLACED = BOTH + PLACED;
            String AUTO_PLACED = AUTO + PLACED;
            String TELEOP_PLACED = TELEOP + PLACED;
            
            String BOTH_DROPPED = BOTH + DROPPED;
            String AUTO_DROPPED = AUTO + DROPPED;
            String TELEOP_DROPPED = TELEOP + DROPPED;
            
            String BOTH_NEAR_PLACED = BOTH + NEAR + PLACED;
            String AUTO_NEAR_PLACED = AUTO + NEAR + PLACED;
            String TELEOP_NEAR_PLACED = TELEOP + NEAR + PLACED;
            
            String BOTH_CENTER_PLACED = BOTH + CENTER + PLACED;
            String AUTO_CENTER_PLACED = AUTO + CENTER + PLACED;
            String TELEOP_CENTER_PLACED = TELEOP + CENTER + PLACED;
            
            String BOTH_FAR_PLACED = BOTH + FAR + PLACED;
            String AUTO_FAR_PLACED = AUTO + FAR + PLACED;
            String TELEOP_FAR_PLACED = TELEOP + FAR + PLACED;
            
            String BOTH_NEAR_DROPPED = BOTH + NEAR + DROPPED;
            String AUTO_NEAR_DROPPED = AUTO + NEAR + DROPPED;
            String TELEOP_NEAR_DROPPED = TELEOP + NEAR + DROPPED;
            
            String BOTH_CENTER_DROPPED = BOTH + CENTER + DROPPED;
            String AUTO_CENTER_DROPPED = AUTO + CENTER + DROPPED;
            String TELEOP_CENTER_DROPPED = TELEOP + CENTER + DROPPED;
            
            String BOTH_FAR_DROPPED = BOTH + FAR + DROPPED;
            String AUTO_FAR_DROPPED = AUTO + FAR + DROPPED;
            String TELEOP_FAR_DROPPED = TELEOP + FAR + DROPPED;

            String[] OPTIONS = {
                                BOTH_PLACED, AUTO_PLACED, TELEOP_PLACED,
                                BOTH_DROPPED, AUTO_DROPPED, TELEOP_DROPPED,
                                BOTH_NEAR_PLACED, AUTO_NEAR_PLACED, TELEOP_NEAR_PLACED,
                                BOTH_CENTER_PLACED, AUTO_CENTER_PLACED, TELEOP_CENTER_PLACED,
                                BOTH_FAR_PLACED, AUTO_FAR_PLACED, TELEOP_FAR_PLACED,
                                BOTH_NEAR_DROPPED, AUTO_NEAR_DROPPED, TELEOP_NEAR_DROPPED,
                                BOTH_CENTER_DROPPED, AUTO_CENTER_DROPPED, TELEOP_CENTER_DROPPED,
                                BOTH_FAR_DROPPED, AUTO_FAR_DROPPED, TELEOP_FAR_DROPPED
                                };
        }

        interface Shooting_Secondary_Options {
            String AUTO_HIGH_MADE = "Auto High Goal Made";
            String AUTO_LOW_MADE = "Auto Low Goal Made";
            String AUTO_HIGH_PERCENT = "Auto High Goal Percent";
            String AUTO_LOW_PERCENT = "Auto Low Goal Percent";

            String TELEOP_HIGH_MADE = "Teleop High Goal Made";
            String TELEOP_LOW_MADE = "Teleop Low Goal Made";
            String TELEOP_HIGH_PERCENT = "Teleop High Goal Percent";
            String TELEOP_LOW_PERCENT = "Teleop Low Goal Percent";

            String[] OPTIONS = {AUTO_HIGH_MADE, AUTO_HIGH_PERCENT,
                                AUTO_LOW_MADE, AUTO_LOW_PERCENT,
                                TELEOP_HIGH_MADE, TELEOP_HIGH_PERCENT,
                                TELEOP_LOW_MADE, TELEOP_LOW_PERCENT};
        }

        interface Climb_Secondary_Options {
            String SUCCESSFUL_ATTEMPTS = "Successful Attempts";
            String TIME = "Time";

            String[] OPTIONS = {SUCCESSFUL_ATTEMPTS, TIME};
        }
    }
}
