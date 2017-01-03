package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_DisplayScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 1/2/17
 */
public class DisplayScoutAccuracy extends Activity {

    private final static String TAG = "DisplayScoutAccuracy";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scout_accuracy);

        ArrayList<ScoutAccuracy> scoutAccuracies = Database.getInstance().getScoutAccuracies();

        View view = findViewById(R.id.total);

        ScoutAccuracy total = new ScoutAccuracy();
        total.name = "Total";

        for(ScoutAccuracy sa: scoutAccuracies){
            total.total_error += sa.total_error;
            total.auto_error += sa.auto_error;
            total.teleop_error += sa.teleop_error;
            total.endgame_error += sa.endgame_error;

        }
        ((TextView)view.findViewById(R.id.name)).setText(total.name);
        ((TextView)view.findViewById(R.id.total_error)).setText(total.name);
        ((TextView)view.findViewById(R.id.name)).setText(total.name);
        ((TextView)view.findViewById(R.id.name)).setText(total.name);
        ((TextView)view.findViewById(R.id.name)).setText(total.name);

        scoutAccuracies.add(0, new ScoutAccuracy());

        LVA_DisplayScoutAccuracy lva = new LVA_DisplayScoutAccuracy(this, scoutAccuracies);
        ((ListView)findViewById(R.id.listview)).setAdapter(lva);
    }

}
