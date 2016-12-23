package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_IndividualStrategy;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_MatchStrategy;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 11/5/16
 */
public class StrategyList extends Activity implements View.OnClickListener {

    private final static String TAG = "StrategyList";
    Button mIndividualOpen;
    Button mEntireMatchOpen;
    ListView mIndividualList;
    ListView mEntireMatchList;
    private boolean mIndividualIsOpen;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_list);

        mIndividualList = (ListView)findViewById(R.id.individual_list);

        Database database = Database.getInstance();

        ArrayList<Strategy> strategies = database.getStrategies();
        strategies.add(0, new Strategy()); // To be replaced with button
        mIndividualList.setAdapter(new LVA_IndividualStrategy(this, strategies));

        mEntireMatchList = (ListView)findViewById(R.id.entire_match_list);

        ArrayList<Pair<Integer, Boolean>> match_numbers = new ArrayList<>();
        TeamLogistics tl = database.getTID(Constants.OUR_TEAM_NUMBER);
        if(tl != null) {
            for (Integer match_number : tl.match_numbers) {
                match_numbers.add(Pair.create(match_number, database.getMatchStrategy(match_number) != null));
            }

            mEntireMatchList.setAdapter(new LVA_MatchStrategy(this, match_numbers));
        }
        mIndividualOpen = (Button)findViewById(R.id.individual_open);
        mIndividualOpen.setOnClickListener(this);

        mEntireMatchOpen = (Button)findViewById(R.id.entire_match_open);
        mEntireMatchOpen.setOnClickListener(this);

        mIndividualIsOpen = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.individual_open:
            case R.id.entire_match_open:
                if(mIndividualIsOpen){
                    mIndividualList.setVisibility(View.GONE);
                    mEntireMatchList.setVisibility(View.VISIBLE);

                    mIndividualOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.expand_color, 0, 0, 0);
                    mEntireMatchOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapse_color, 0, 0, 0);

                    mIndividualIsOpen = false;
                } else {
                    mIndividualList.setVisibility(View.VISIBLE);
                    mEntireMatchList.setVisibility(View.GONE);

                    mIndividualOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapse_color, 0, 0, 0);
                    mEntireMatchOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.expand_color, 0, 0, 0);

                    mIndividualIsOpen = true;
                }
                break;
        }
    }
}
