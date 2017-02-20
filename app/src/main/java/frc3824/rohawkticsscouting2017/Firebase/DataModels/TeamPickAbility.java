package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/23/16
 *
 * Class to hold information for picks for a specific team
 */
@IgnoreExtraProperties
public class TeamPickAbility {

    private final static String TAG = "TeamPickAbility";

    public long last_modified;

    public int team_number;
    public String nickname;
    public double pick_ability;
    public int manual_ranking;
    public String top_line;
    public String second_line;
    public String third_line;
    public String fourth_line;
    public String robot_picture_filepath;
    public boolean yellow_card;
    public boolean red_card;
    public boolean stopped_moving;

    public boolean picked;
    public boolean dnp;

    public TeamPickAbility(){}

}
