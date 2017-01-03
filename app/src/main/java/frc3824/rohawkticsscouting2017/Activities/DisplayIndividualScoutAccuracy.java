package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_DisplayIndividualScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutedMatchAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class DisplayIndividualScoutAccuracy extends Activity {

    private final static String TAG = "DisplayIndividualScoutAccuracy";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_individual_scout_activity);

        Bundle extras = getIntent().getExtras();
        String scouter = extras.getString(Constants.Intent_Extras.SCOUTER);

        ScoutAccuracy sa = Database.getInstance().getScoutAccuracy(scouter);
        ArrayList<ScoutedMatchAccuracy> scoutedMatchAccuracies = new ArrayList<>(sa.scouted_matches.values());
        scoutedMatchAccuracies.add(0, new ScoutedMatchAccuracy());

        ((TextView)findViewById(R.id.name)).setText(sa.name);

        LVA_DisplayIndividualScoutAccuracy lva = new LVA_DisplayIndividualScoutAccuracy(this, scoutedMatchAccuracies);

        ((ListView)findViewById(R.id.listview)).setAdapter(lva);
    }

}
