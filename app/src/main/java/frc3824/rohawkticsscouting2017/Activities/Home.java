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
import frc3824.rohawkticsscouting2017.Statistics.Aggregate;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created:
 *
 * Home page for the app that allows the user to reach all other areas of the map. Display specific
 * button based on the usertype.
 */
public class Home extends Activity implements View.OnClickListener{

    private Database mDatabase;
    private SharedPreferences mSharedPreferences;

    private TextView mEventTextView;
    private TextView mUserTypeTextView;
    private TextView mUserSubTypeTextView;

    private String mEventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((TextView)findViewById(R.id.version)).setText(String.format("Version: %s", Constants.VERSION));

        setupButton(R.id.settings_button);

        mSharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String user_type = mSharedPreferences.getString(Constants.Settings.USER_TYPE, "");
        mEventKey = mSharedPreferences.getString(Constants.Settings.EVENT_KEY, "");

        mEventTextView = (TextView)findViewById(R.id.event);
        mUserTypeTextView = (TextView)findViewById(R.id.user_type);
        mUserSubTypeTextView = (TextView)findViewById(R.id.user_subtype);

        switch (user_type)
        {
            case Constants.User_Types.MATCH_SCOUT:
                userTypeMatchScoutSetup();
                break;
            case Constants.User_Types.PIT_SCOUT:
                userTypePitScoutSetup();
                break;
            case Constants.User_Types.SUPER_SCOUT:
                userTypeSuperScoutSetup();
                break;
            case Constants.User_Types.DRIVE_TEAM:
                userTypeDriveTeamSetup();
                break;
            case Constants.User_Types.STRATEGY:
                userTypeStrategySetup();
                break;
            case Constants.User_Types.SERVER:
                userTypeServerSetup();
                break;
            case Constants.User_Types.ADMIN:
                userTypeAdminSetup();
                break;
        }

        if(mEventKey != "") {
            mDatabase = Database.getInstance(mEventKey);
            Storage.getInstance(mEventKey);
        }
        else
        {
            Database.getInstance();
            Storage.getInstance();
        }
    }

    //region User Type Setups
    private void userTypeMatchScoutSetup()
    {
        setupButton(R.id.schedule_button);
        setupButton(R.id.scout_match_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Match Scout");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        String allianceColor = mSharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
        String userSubtype;
        if(allianceColor == Constants.Alliance_Colors.BLUE)
        {
            mUserSubTypeTextView.setTextColor(Color.BLUE);
            userSubtype = "Blue ";
        }
        else
        {
            mUserSubTypeTextView.setTextColor(Color.RED);
            userSubtype = "Red ";
        }
        int allianceNumber = mSharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
        userSubtype += String.valueOf(allianceNumber);

        mUserSubTypeTextView.setText(userSubtype);
        mUserSubTypeTextView.setVisibility(View.VISIBLE);
    }

    private void userTypePitScoutSetup()
    {
        setupButton(R.id.schedule_button);
        setupButton(R.id.scout_pit_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Pit Scout");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        int pitGroup = mSharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, -1);
        mUserTypeTextView.setText(String.format("Group Number: %d", pitGroup));
        mUserTypeTextView.setVisibility(View.VISIBLE);
    }

    private void userTypeSuperScoutSetup()
    {
        setupButton(R.id.schedule_button);
        setupButton(R.id.scout_super_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Super Scout");
        mUserTypeTextView.setVisibility(View.VISIBLE);
    }

    private void userTypeDriveTeamSetup()
    {
        setupButton(R.id.schedule_button);
        setupButton(R.id.match_planning_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Drive Team");
        mUserTypeTextView.setVisibility(View.VISIBLE);
    }

    private void userTypeStrategySetup()
    {
        setupButton(R.id.schedule_button);

        setupButton(R.id.view_team_button);
        setupButton(R.id.view_match_button);
        setupButton(R.id.view_rankings_button);
        setupButton(R.id.view_event_button);
        setupButton(R.id.view_pick_list_button);
        setupButton(R.id.view_notes_button);

        setupButton(R.id.match_planning_button);

        setupButton(R.id.aggregate_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Strategy");
        mUserTypeTextView.setVisibility(View.VISIBLE);
    }

    private void userTypeServerSetup()
    {
        setupButton(R.id.schedule_button);
        setupButton(R.id.server_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Server");
        mUserTypeTextView.setVisibility(View.VISIBLE);
    }

    private void userTypeAdminSetup()
    {
        setupButton(R.id.schedule_button);
        setupButton(R.id.scout_match_button);
        setupButton(R.id.scout_pit_button);
        setupButton(R.id.scout_super_button);

        setupButton(R.id.view_team_button);
        setupButton(R.id.view_match_button);
        setupButton(R.id.view_rankings_button);
        setupButton(R.id.view_event_button);
        setupButton(R.id.view_pick_list_button);
        setupButton(R.id.view_notes_button);

        setupButton(R.id.match_planning_button);

        setupButton(R.id.server_button);

        setupButton(R.id.cloud_storage_button);

        setupButton(R.id.aggregate_button);


        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Admin");
        mUserTypeTextView.setVisibility(View.VISIBLE);
    }
    //endregion

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
            case R.id.view_notes_button:
                intent = new Intent(this, NotesViewActivity.class);
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
            case R.id.aggregate_button:
                for(int team_number: mDatabase.getTeamNumbers())
                {
                    Aggregate.aggregateForTeam(team_number);
                }
                Aggregate.aggregateForSuper();
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
