package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 *
 * Team Pick Ability
 */
@IgnoreExtraProperties
public class TPA {

    private final static String TAG = "TPA";

    public int team_number;
    public String nickname;
    public double pick_ability;
    public int manual_ranking;
    public String top_line;
    public String second_line;
    public String third_line;
    public String robot_picture_filepath;
    public boolean yellow_card;
    public boolean red_card;
    public boolean stopped_moving;

    public boolean picked;
    public boolean dnp;

    public TPA(){}

}
