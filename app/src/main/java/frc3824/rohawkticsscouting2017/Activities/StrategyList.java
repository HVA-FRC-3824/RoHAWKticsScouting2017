package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_StrategyUploadDownload;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 11/5/16
 */
public class StrategyList extends Activity{

    private final static String TAG = "StrategyList";
    ListView mStrategyList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_list);

        mStrategyList = (ListView)findViewById(R.id.strategy_list);

        Database database = Database.getInstance();

        ArrayList<Strategy> strategies = database.getAllStrategies();
        strategies.add(0, new Strategy()); // To be replaced with button
        mStrategyList.setAdapter(new LVA_StrategyUploadDownload(this, strategies));
    }

}
