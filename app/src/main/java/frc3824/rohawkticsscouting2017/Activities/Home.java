package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created:
 *
 * Home page for the app that allows the user to reach all other areas of the map. Display specific
 * button based on the usertype.
 */
public class Home extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((TextView)findViewById(R.id.version)).setText(String.format("Version: %s", Constants.VERSION));

        setupButton(R.id.settings_button);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String user_type = sharedPreferences.getString(Constants.Settings.USER_TYPE, "");
        String eventKey = sharedPreferences.getString(Constants.Settings.EVENT_KEY, "");

        TextView eventTextview = (TextView)findViewById(R.id.event);
        TextView usertypeTextview = (TextView)findViewById(R.id.user_type);
        TextView userSubtypeTextView = (TextView)findViewById(R.id.user_subtype);
        switch (user_type)
        {
            case Constants.User_Types.MATCH_SCOUT:
                setupButton(R.id.schedule_button);
                setupButton(R.id.scout_match_button);

                eventTextview.setText("Event: " + eventKey);
                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Match Scout");
                usertypeTextview.setVisibility(View.VISIBLE);

                String allianceColor = sharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
                String userSubtype;
                if(allianceColor == Constants.Alliance_Colors.BLUE)
                {
                    userSubtypeTextView.setTextColor(Color.BLUE);
                    userSubtype = "Blue ";
                }
                else
                {
                    userSubtypeTextView.setTextColor(Color.RED);
                    userSubtype = "Red ";
                }
                int allianceNumber = sharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
                userSubtype += String.valueOf(allianceNumber);

                userSubtypeTextView.setText(userSubtype);
                userSubtypeTextView.setVisibility(View.VISIBLE);
                break;
            case Constants.User_Types.PIT_SCOUT:
                setupButton(R.id.schedule_button);
                setupButton(R.id.scout_pit_button);

                eventTextview.setText("Event: " + eventKey);
                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Pit Scout");
                usertypeTextview.setVisibility(View.VISIBLE);

                int pitGroup = sharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, -1);
                userSubtypeTextView.setText(String.format("Group Number: %d", pitGroup));
                userSubtypeTextView.setVisibility(View.VISIBLE);
                break;
            case Constants.User_Types.SUPER_SCOUT:
                setupButton(R.id.schedule_button);
                setupButton(R.id.scout_super_button);

                eventTextview.setText("Event: " + eventKey);
                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Super Scout");
                usertypeTextview.setVisibility(View.VISIBLE);

                break;
            case Constants.User_Types.DRIVE_TEAM:
                setupButton(R.id.schedule_button);
                setupButton(R.id.match_planning_button);

                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Drive Team");
                usertypeTextview.setVisibility(View.VISIBLE);
                break;

            case Constants.User_Types.STRATEGY:
                setupButton(R.id.schedule_button);
                setupButton(R.id.view_team_button);
                setupButton(R.id.view_match_button);
                setupButton(R.id.view_rankings_button);
                setupButton(R.id.view_event_button);
                setupButton(R.id.view_pick_list_button);

                setupButton(R.id.match_planning_button);

                eventTextview.setText("Event: " + eventKey);
                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Strategy");
                usertypeTextview.setVisibility(View.VISIBLE);

                break;
            case Constants.User_Types.SERVER:
                setupButton(R.id.schedule_button);
                setupButton(R.id.server_button);

                eventTextview.setText("Event: " + eventKey);
                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Server");
                usertypeTextview.setVisibility(View.VISIBLE);
                break;
            case Constants.User_Types.ADMIN:
                setupButton(R.id.schedule_button);
                setupButton(R.id.scout_match_button);
                setupButton(R.id.scout_pit_button);
                setupButton(R.id.scout_super_button);

                setupButton(R.id.view_team_button);
                setupButton(R.id.view_match_button);
                setupButton(R.id.view_rankings_button);
                setupButton(R.id.view_event_button);
                setupButton(R.id.view_pick_list_button);

                setupButton(R.id.match_planning_button);

                setupButton(R.id.server_button);

                setupButton(R.id.cloud_storage_button);


                eventTextview.setText("Event: " + eventKey);
                eventTextview.setVisibility(View.VISIBLE);

                usertypeTextview.setText("User: Admin");
                usertypeTextview.setVisibility(View.VISIBLE);

                break;
        }

        //Authentication.getInstance();
        if(eventKey != "") {
            Database.getInstance(eventKey);
            Storage.getInstance(eventKey);
        }
        else
        {
            Database.getInstance();
            Storage.getInstance();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.settings_button:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            case R.id.schedule_button:
                intent = new Intent(this, Schedule.class);
                startActivity(intent);
                break;
            case R.id.scout_match_button:
                intent = new Intent(this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.MATCH_SCOUTING);
                startActivity(intent);
                break;
            case R.id.scout_pit_button:
                intent = new Intent(this, TeamList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.PIT_SCOUTING);
                startActivity(intent);
                break;
            case R.id.scout_super_button:
                intent = new Intent(this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.SUPER_SCOUTING);
                startActivity(intent);
                break;
            case R.id.view_team_button:
                intent = new Intent(this, TeamList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.TEAM_VIEWING);
                startActivity(intent);
                break;
            case R.id.view_match_button:
                intent = new Intent(this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.MATCH_VIEWING);
                startActivity(intent);
                break;
            case R.id.view_rankings_button:
                intent = new Intent(this, RankingsView.class);
                startActivity(intent);
                break;
            case R.id.view_event_button:
                intent = new Intent(this, EventView.class);
                startActivity(intent);
                break;
            case R.id.view_pick_list_button:
                intent = new Intent(this, PickList.class);
                startActivity(intent);
                break;
            case R.id.match_planning_button:
                intent = new Intent(this, MatchPlanning.class);
                startActivity(intent);
                break;
            case R.id.server_button:
                intent = new Intent(this, Server.class);
                startActivity(intent);
                break;
            case R.id.cloud_storage_button:
                intent = new Intent(this, CloudStorage.class);
                startActivity(intent);
                break;
            default:
                assert false;
        }
    }

    /**
     * Makes the button visible and attaches the on click listener
     *
     * @param btn id for the button to be set up
     */
    private void setupButton(int btn) {
        Button button = (Button) findViewById(btn);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
    }
}
