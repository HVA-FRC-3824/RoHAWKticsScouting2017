package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_Schedule;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 * Activity for displaying the schedule
 */
public class Schedule extends Activity implements View.OnClickListener{

    private final static String TAG = "Schedule";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString(Constants.Settings.USER_TYPE, "");
        if(userType.equals(Constants.User_Types.ADMIN))
        {
            Button builderButton = (Button)findViewById(R.id.schedule_builder_button);
            builderButton.setVisibility(View.VISIBLE);
            builderButton.setOnClickListener(this);
        }

        ListView schedule = (ListView)findViewById(R.id.schedule);

        int numMatches = Database.getInstance().getNumberOfMatches();
        ArrayList<Integer> matches = new ArrayList<>();
        for(int i = 0; i <= numMatches; i++)
        {
            matches.add(i);
        }
        LVA_Schedule lva = new LVA_Schedule(this, matches);
        schedule.setAdapter(lva);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.schedule_builder_button:
                Intent intent = new Intent(this, ScheduleBuilder.class);
                startActivity(intent);
                break;
        }
    }
}
