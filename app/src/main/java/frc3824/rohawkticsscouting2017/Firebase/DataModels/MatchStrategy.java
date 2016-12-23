package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.content.Context;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 12/20/16
 */
public class MatchStrategy {

    private final static String TAG = "MatchStrategy";

    public int match_number;

    public Strategy auto;

    public ArrayList<Strategy> teleop;

    public Strategy endgame;

    public MatchStrategy(){

    }

    /**
     * If the image files are not on this device create them from the json
     * @param context to be used for file system
     */
    public void create(Context context){
        auto.create(context);
        for(Strategy strategy : teleop){
            strategy.create(context);
        }
        endgame.create(context);
    }
}
