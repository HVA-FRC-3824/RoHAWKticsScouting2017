package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Data collected by pit scouts
 */
@IgnoreExtraProperties
public class TeamPitData {

    private final static String TAG = "TeamPitData";

    /*
        Pit Scouting
    */
    public int team_number;
    public String scout_name;
    public long last_modified;

    // Robot Image
    public int robot_image_default;
    public ArrayList<String> robot_image_filepaths;
    public ArrayList<String> robot_image_urls;

    // Dimensions
    public double weight;
    public double width;
    public double length;
    public double height;

    // Miscellaneous

    public String programming_language;
    public String drive_train;
    public int cims;
    public int max_hopper_load;
    public String chosen_volume;


    // Notes
    public String notes;

    public TeamPitData(){
        robot_image_filepaths = new ArrayList<>();
        robot_image_urls = new ArrayList<>();
        robot_image_default = -1;
    }

    public TeamPitData(ScoutMap map) {
        try {
            team_number = map.getInt(Constants.Intent_Extras.TEAM_NUMBER);
            scout_name = map.getString(Constants.Pit_Scouting.SCOUT_NAME);

            if(map.contains(Constants.Pit_Scouting.ROBOT_PICTURE_DEFAULT)){
                robot_image_default = map.getInt(Constants.Pit_Scouting.ROBOT_PICTURE_DEFAULT);
            } else {
                robot_image_default = -1;
            }


            robot_image_filepaths = (ArrayList)map.getObject(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS);
            if(map.contains(Constants.Pit_Scouting.ROBOT_PICTURE_URLS)) {
                robot_image_urls = (ArrayList) map.getObject(Constants.Pit_Scouting.ROBOT_PICTURE_URLS);
            }


            width = map.getDouble(Constants.Pit_Scouting.Dimensions.WIDTH);
            length = map.getDouble(Constants.Pit_Scouting.Dimensions.LENGTH);
            height = map.getDouble(Constants.Pit_Scouting.Dimensions.HEIGHT);
            weight = map.getDouble(Constants.Pit_Scouting.Dimensions.WEIGHT);

            programming_language = map.getString(Constants.Pit_Scouting.Miscellaneous.PROGRAMMING_LANGUAGE);
            cims = map.getInt(Constants.Pit_Scouting.Miscellaneous.CIMS);
            drive_train = map.getString(Constants.Pit_Scouting.Miscellaneous.DRIVE_TRAIN);
            max_hopper_load = (int)map.getDouble(Constants.Pit_Scouting.Miscellaneous.MAX_HOPPER_LOAD);
            chosen_volume = map.getString(Constants.Pit_Scouting.Miscellaneous.CHOSEN_VOLUME);

            notes = map.getString(Constants.Pit_Scouting.NOTES);

        } catch (ScoutValue.TypeException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Exclude
    public ScoutMap toMap() {
        ScoutMap map = new ScoutMap();

        map.put(Constants.Intent_Extras.TEAM_NUMBER, team_number);
        map.put(Constants.Pit_Scouting.SCOUT_NAME, scout_name);

        map.put(Constants.Pit_Scouting.ROBOT_PICTURE_DEFAULT, robot_image_default);
        map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS, robot_image_filepaths);
        map.put(Constants.Pit_Scouting.ROBOT_PICTURE_URLS, robot_image_urls);

        map.put(Constants.Pit_Scouting.Dimensions.WIDTH, width);
        map.put(Constants.Pit_Scouting.Dimensions.LENGTH, length);
        map.put(Constants.Pit_Scouting.Dimensions.HEIGHT, height);
        map.put(Constants.Pit_Scouting.Dimensions.WEIGHT, weight);

        map.put(Constants.Pit_Scouting.Miscellaneous.PROGRAMMING_LANGUAGE, programming_language);
        map.put(Constants.Pit_Scouting.Miscellaneous.DRIVE_TRAIN, drive_train);
        map.put(Constants.Pit_Scouting.Miscellaneous.CIMS, cims);
        map.put(Constants.Pit_Scouting.Miscellaneous.MAX_HOPPER_LOAD, (double)max_hopper_load);
        map.put(Constants.Pit_Scouting.Miscellaneous.CHOSEN_VOLUME, chosen_volume);

        map.put(Constants.Pit_Scouting.NOTES, notes);

        return map;
    }
}
