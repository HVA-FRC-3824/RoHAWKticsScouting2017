package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_TeamList;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/14/16
 *
 * Displays a list of buttons with all the team at the given event. Can lead to Pit Scouting or Team
 * View.
 */
public class TeamList extends Activity{

    private final static String TAG = "TeamList";

    private String mNextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        Bundle extras = getIntent().getExtras();
        mNextPage = extras.getString(Constants.Intent_Extras.NEXT_PAGE);

        Database database = Database.getInstance();

        ArrayList<Integer> teams = database.getTeamNumbers();

        // For pit scouting show only the team in group
        int start = 0;
        int end = teams.size();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String user_type = sharedPreferences.getString(Constants.Settings.USER_TYPE, "");
        if(user_type.equals(Constants.User_Types.PIT_SCOUT)){
            int pit_group = sharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, -1);
            int group_size = (int)((float)(teams.size()) / 6.0 + 0.5f);
            start = group_size * (pit_group - 1);
            end = group_size * (pit_group);
            if(end > teams.size()){
                end = teams.size();
            }
        }

        ArrayList<Integer> team_numbers = new ArrayList<>();
        for(int i = start; i < end; i++) {
            team_numbers.add(teams.get(i));
        }


        ListView listView = (ListView)findViewById(R.id.team_list);
        LVA_TeamList.TeamListType tlt = LVA_TeamList.TeamListType.PIT_SCOUT;
        if(mNextPage.equals(Constants.Intent_Extras.TEAM_VIEWING)){
            tlt = LVA_TeamList.TeamListType.TEAM_VIEW;
        }
        LVA_TeamList lva= new LVA_TeamList(this, team_numbers, tlt);
        listView.setAdapter(lva);
    }



    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, Home.class));
    }
}
