package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
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
public class TeamList extends Activity implements View.OnClickListener{

    private final static String TAG = "TeamList";

    private String mNextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        Bundle extras = getIntent().getExtras();
        mNextPage = extras.getString(Constants.Intent_Extras.NEXT_PAGE);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventKey = sharedPreferences.getString(Constants.Settings.EVENT_KEY, "");

        Database database = Database.getInstance(eventKey);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.team_list);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 4, 4, 4);

        ArrayList<Team> teams = database.getTeams();
        for(int i = 0; i < teams.size(); i++)
        {
            Team team = teams.get(i);
            Button button = new Button(this);
            button.setLayoutParams(lp);
            button.setText(String.format("Team: %d", team.team_number));
            if(mNextPage.equals(Constants.Intent_Extras.PIT_SCOUTING))
            {
                if(team.pit_scouted)
                {
                    button.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    button.setBackgroundColor(Color.RED);
                }
            }
            button.setOnClickListener(this);
            button.setId(team.team_number);
            linearLayout.addView(button);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (mNextPage)
        {
            case Constants.Intent_Extras.PIT_SCOUTING:
                intent = new Intent(this, PitScouting.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, view.getId());
                startActivity(intent);
                break;
            default:
                assert false;
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, Home.class));
    }
}
