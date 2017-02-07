package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Comms.MessageQueue;
import frc3824.rohawkticsscouting2017.Comms.ConnectThread;
import frc3824.rohawkticsscouting2017.Comms.SocketThread;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Views.ImageTextButton;

/**
 * @author frc3824
 * Created:
 *
 * Home page for the app that allows the user to reach all other areas of the app. Display specific
 * buttons based on the user's type (Match Scout, Pit Scout, etc).
 */
public class Home extends Activity implements View.OnClickListener{

    private static final String TAG = "Home";

    private Database mDatabase;
    private SharedPreferences mSharedPreferences;

    private TextView mEventTextView;
    private TextView mUserTypeTextView;
    private TextView mUserSubTypeTextView;

    private String mUserType;
    private String mEventKey;
    private String mServerName;

    private boolean mScoutingOpenButton;
    private boolean mStrategyOpenButton;
    private boolean mPickListOpenButton;
    private boolean mAdminOpenButton;

    /**
     *  Sets up what buttons to display on the home screen
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((TextView)findViewById(R.id.version)).setText(String.format("Version: %s", Constants.VERSION));

        setupImageButton(R.id.settings_button);

        mSharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mUserType = mSharedPreferences.getString(Constants.Settings.USER_TYPE, "");
        mEventKey = mSharedPreferences.getString(Constants.Settings.EVENT_KEY, "");
        mServerName = mSharedPreferences.getString(Constants.Settings.SERVER_TYPE, "");

        mEventTextView = (TextView)findViewById(R.id.event);
        mUserTypeTextView = (TextView)findViewById(R.id.user_type);
        mUserSubTypeTextView = (TextView)findViewById(R.id.user_subtype);

        // Run the setup function for the usr type
        switch (mUserType)
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
            case Constants.User_Types.ADMIN:
                userTypeAdminSetup();
                break;
        }

        // Setup the database or reload it (to make the schedule and button list work)
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

    /**
     * Setup function if the user is a match scout
     */
    private void userTypeMatchScoutSetup() {
        setupImageButton(R.id.schedule_button);
        setupImageButton(R.id.scout_match_button);
        setupButton(R.id.scouting_open_button);

        setupImageButton(R.id.sync_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Match Scout");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        String alliance_color = mSharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
        Log.d(TAG, alliance_color);
        String userSubtype;
        if(alliance_color.equals(Constants.Alliance_Colors.BLUE))
        {
            mUserSubTypeTextView.setTextColor(Color.BLUE);
            userSubtype = "Blue ";
        } else {
            mUserSubTypeTextView.setTextColor(Color.RED);
            userSubtype = "Red ";
        }
        int allianceNumber = mSharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
        userSubtype += String.valueOf(allianceNumber);

        mUserSubTypeTextView.setText(userSubtype);
        mUserSubTypeTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.scouting_open_button).setOnClickListener(this);
        mScoutingOpenButton = false;
    }

    /**
     * Setup function if the user is a pit scout
     */
    private void userTypePitScoutSetup() {
        setupImageButton(R.id.schedule_button);
        setupImageButton(R.id.scout_pit_button);
        setupButton(R.id.scouting_open_button);

        setupImageButton(R.id.sync_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Pit Scout");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        int pitGroup = mSharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, -1);
        mUserTypeTextView.setText(String.format("Group Number: %d", pitGroup));
        mUserTypeTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.scouting_open_button).setOnClickListener(this);
        mScoutingOpenButton = false;
    }

    /**
     * Setup if the user is a super scout
     */
    private void userTypeSuperScoutSetup() {
        setupImageButton(R.id.schedule_button);
        setupImageButton(R.id.scout_super_button);
        setupButton(R.id.scouting_open_button);

        setupImageButton(R.id.sync_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Super Scout");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.scouting_open_button).setOnClickListener(this);
        mScoutingOpenButton = false;
    }

    /**
     * Setup if the user is a drive team member
     */
    private void userTypeDriveTeamSetup() {
        setupImageButton(R.id.schedule_button);
        setupImageButton(R.id.match_planning_button);
        setupImageButton(R.id.drive_team_feedback_button);
        setupButton(R.id.scouting_open_button);

        setupImageButton(R.id.view_team_button);
        setupImageButton(R.id.view_match_button);
        setupImageButton(R.id.view_notes_button);
        setupImageButton(R.id.view_rankings_button);
        setupButton(R.id.strategy_open_button);

        setupImageButton(R.id.sync_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Drive Team");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.scouting_open_button).setOnClickListener(this);
        mScoutingOpenButton = false;
        findViewById(R.id.strategy_open_button).setOnClickListener(this);
        mStrategyOpenButton = false;

        findViewById(R.id.pick_list_open_button).setOnClickListener(this);
        mPickListOpenButton = false;
    }

    /**
     * Setup if the user is strategy team member
     */
    private void userTypeStrategySetup() {
        setupImageButton(R.id.schedule_button);

        setupImageButton(R.id.view_team_button);
        setupImageButton(R.id.view_match_button);
        setupImageButton(R.id.view_rankings_button);
        setupImageButton(R.id.view_event_button);
        setupImageButton(R.id.view_pick_list_button);
        setupImageButton(R.id.view_notes_button);
        setupButton(R.id.strategy_open_button);
        setupButton(R.id.pick_list_open_button);

        setupImageButton(R.id.match_planning_button);

        setupImageButton(R.id.sync_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Strategy");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.strategy_open_button).setOnClickListener(this);
        mStrategyOpenButton = false;

        findViewById(R.id.pick_list_open_button).setOnClickListener(this);
        mPickListOpenButton = false;
    }

    /**
     * Setup if the user is an admin
     */
    private void userTypeAdminSetup() {
        setupImageButton(R.id.schedule_button);
        setupImageButton(R.id.scout_match_button);
        setupImageButton(R.id.scout_pit_button);
        setupImageButton(R.id.scout_super_button);
        setupImageButton(R.id.drive_team_feedback_button);
        setupButton(R.id.scouting_open_button);

        setupImageButton(R.id.view_team_button);
        setupImageButton(R.id.view_match_button);
        setupImageButton(R.id.view_rankings_button);
        setupImageButton(R.id.view_event_button);
        setupImageButton(R.id.view_pick_list_button);
        setupImageButton(R.id.view_notes_button);
        setupButton(R.id.strategy_open_button);
        setupButton(R.id.pick_list_open_button);

        setupImageButton(R.id.match_planning_button);

        setupImageButton(R.id.cloud_storage_button);

        setupImageButton(R.id.team_list_builder_button);
        setupImageButton(R.id.scouting_accuracy_button);
        setupButton(R.id.admin_open_button);

        setupImageButton(R.id.sync_button);

        mEventTextView.setText("Event: " + mEventKey);
        mEventTextView.setVisibility(View.VISIBLE);

        mUserTypeTextView.setText("User: Admin");
        mUserTypeTextView.setVisibility(View.VISIBLE);

        findViewById(R.id.scouting_open_button).setOnClickListener(this);
        mScoutingOpenButton = false;

        findViewById(R.id.strategy_open_button).setOnClickListener(this);
        mStrategyOpenButton = false;

        findViewById(R.id.pick_list_open_button).setOnClickListener(this);
        mPickListOpenButton = false;

        findViewById(R.id.admin_open_button).setOnClickListener(this);
        mAdminOpenButton = false;
    }
    //endregion

    /**
     * Function that handles button clicks
     * @param view
     */
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
            case R.id.drive_team_feedback_button:
                intent = new Intent(this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.DRIVE_TEAM_FEEDBACK);
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
                intent = new Intent(this, StrategyPlanning.class);
                startActivity(intent);
                break;
            case R.id.cloud_storage_button:
                intent = new Intent(this, CloudStorage.class);
                startActivity(intent);
                break;
            case R.id.team_list_builder_button:
                intent = new Intent(this, TeamListBuilder.class);
                startActivity(intent);
                break;
            case R.id.scouting_accuracy_button:
                intent = new Intent(this, DisplayScoutAccuracy.class);
                startActivity(intent);
                break;
            case R.id.scouting_open_button:
                if(mScoutingOpenButton){
                    findViewById(R.id.scouting_buttons).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.scouting_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.expand_color, 0, 0, 0);
                    mScoutingOpenButton = false;
                } else {
                    findViewById(R.id.scouting_buttons).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.scouting_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapse_color, 0, 0, 0);
                    mScoutingOpenButton = true;
                }
                break;
            case R.id.strategy_open_button:
                if(mStrategyOpenButton){
                    findViewById(R.id.strategy_buttons).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.strategy_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.expand_color, 0, 0, 0);
                    mStrategyOpenButton = false;
                } else {
                    findViewById(R.id.strategy_buttons).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.strategy_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapse_color, 0, 0, 0);
                    mStrategyOpenButton = true;
                }
                break;
            case R.id.pick_list_open_button:
                if(mPickListOpenButton){
                    findViewById(R.id.pick_list_buttons).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.pick_list_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.expand_color, 0, 0, 0);
                    mPickListOpenButton = false;
                } else {
                    findViewById(R.id.pick_list_buttons).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.pick_list_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapse_color, 0, 0, 0);
                    mPickListOpenButton = true;
                }
                break;
            case R.id.admin_open_button:
                if(mAdminOpenButton){
                    findViewById(R.id.admin_buttons).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.admin_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.expand_color, 0, 0, 0);
                    mAdminOpenButton = false;
                } else {
                    findViewById(R.id.admin_buttons).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.admin_open_button)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.collapse_color, 0, 0, 0);
                    mAdminOpenButton = true;
                }
                break;
            default:
                assert false;
        }
    }

    /**
     * Makes the image button visible and attaches the on click listener
     *
     * @param btn
     */
    private void setupImageButton(int btn) {
        ImageTextButton button = (ImageTextButton) findViewById(btn);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
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

    /**
     * Asynchronous task the handles asking for data from the server using bluetooth or usb socket
     */
    private class SyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (mServerName.equals(Constants.Socket.SERVER)) {
                socketVersion();
            } else {
                bluetoothVersion();
            }
            return null;
        }

        /**
         * Handles syncing if using usb sockets
         */
        private void socketVersion(){
            try {
                Socket socket = new Socket("localhost", Constants.Socket.PORT);
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
                Gson gson = new GsonBuilder().create();
                switch (mUserType){
                    case Constants.User_Types.ADMIN:
                        if(socketThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }
                        break;
                    case Constants.User_Types.STRATEGY:
                        if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.STRATEGY_HEADER, gson.toJson(mDatabase.getAllStrategies()))))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }

                        if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.STRATEGY_SUGGESTION_HEADER, gson.toJson(mDatabase.getAllStrategySuggestions()))))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }

                        if(socketThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }
                        break;
                    case Constants.User_Types.DRIVE_TEAM:
                        if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.FEEDBACK_HEADER, gson.toJson(mDatabase.getAllTeamDTFeedbacks()))))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }

                        if(socketThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }
                        break;
                    case Constants.User_Types.MATCH_SCOUT:
                        if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.MATCH_HEADER, gson.toJson(mDatabase.getAllTeamMatchData()))))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }

                        if(socketThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }
                        break;
                    case Constants.User_Types.PIT_SCOUT:
                        if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.PIT_HEADER, gson.toJson(mDatabase.getAllTeamPitData()))))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }

                        if(socketThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }
                        break;
                    case Constants.User_Types.SUPER_SCOUT:
                        if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.SUPER_HEADER, gson.toJson(mDatabase.getAllSuperMatchData()))))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }

                        if(socketThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                            while (!socketThread.messageReceived());
                        }
                        else
                        {
                            publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                        }
                        break;
                }

                socketThread.cancel();

            } catch (IOException e) {
                publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
            }
        }

        /**
         * Handles syncing if using bluetooth
         */
        private void bluetoothVersion(){
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            MessageQueue queue = MessageQueue.getInstance();

            if(bluetoothAdapter == null)
            {
                publishProgress(Constants.Comms.Data_Transfer_Status.NO_BLUETOOTH);
                return;
            }

            if (!bluetoothAdapter.isEnabled())
            {
                bluetoothAdapter.enable();
                while (!bluetoothAdapter.isEnabled());
            }

            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

            BluetoothDevice server = null;
            for(BluetoothDevice device: devices)
            {
                String deviceName = device.getName();
                if(deviceName.equals(mServerName))
                {
                    server = device;
                    break;
                }
            }

            if(server == null)
            {
                publishProgress(Constants.Comms.Data_Transfer_Status.SERVER_NOT_FOUND);
                return;
            }

            ConnectThread connectThread = new ConnectThread(server,true);
            connectThread.start();
            while (!connectThread.isConnected());
            Gson gson = new GsonBuilder().create();
            switch (mUserType){
                case Constants.User_Types.ADMIN:
                    if(connectThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }
                    break;
                case Constants.User_Types.STRATEGY:
                    if(connectThread.write(String.format("%c%s", Constants.Comms.Message_Headers.STRATEGY_HEADER, gson.toJson(mDatabase.getAllStrategies()))))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }

                    if(connectThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }
                    break;
                case Constants.User_Types.DRIVE_TEAM:
                    if(connectThread.write(String.format("%c%s", Constants.Comms.Message_Headers.FEEDBACK_HEADER, gson.toJson(mDatabase.getAllTeamDTFeedbacks()))))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }

                    if(connectThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }
                    break;
                case Constants.User_Types.MATCH_SCOUT:
                    if(connectThread.write(String.format("%c%s", Constants.Comms.Message_Headers.MATCH_HEADER, gson.toJson(mDatabase.getAllTeamMatchData()))))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }

                    if(connectThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }
                    break;
                case Constants.User_Types.PIT_SCOUT:
                    if(connectThread.write(String.format("%c%s", Constants.Comms.Message_Headers.PIT_HEADER, gson.toJson(mDatabase.getAllTeamPitData()))))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }

                    if(connectThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }
                    break;
                case Constants.User_Types.SUPER_SCOUT:
                    if(connectThread.write(String.format("%c%s", Constants.Comms.Message_Headers.SUPER_HEADER, gson.toJson(mDatabase.getAllSuperMatchData()))))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }

                    if(connectThread.write(String.format("%c", Constants.Comms.Message_Headers.SYNC_HEADER)))
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                        while (!connectThread.messageReceived());
                    }
                    else
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    }
                    break;
            }
            connectThread.cancel();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            switch (values[0]) {
                case Constants.Comms.Data_Transfer_Status.NO_BLUETOOTH:
                    builder.setTitle("No bluetooth on this device!!!");
                    builder.setIcon(getDrawable(R.drawable.bluetooth_2_color));
                    break;
                case Constants.Comms.Data_Transfer_Status.QUEUE_EMPTIED:
                    break;
                case Constants.Comms.Data_Transfer_Status.SERVER_NOT_FOUND:
                    builder.setTitle("Server not found");
                    builder.setIcon(getDrawable(R.drawable.error_color));
                    break;
                case Constants.Comms.Data_Transfer_Status.SUCCESS:
                    builder.setTitle("Sync Request Successful");
                    builder.setIcon(getDrawable(R.drawable.ok_color));
                    break;
                case Constants.Comms.Data_Transfer_Status.FAILURE:
                    builder.setTitle("Sync Request Failure");
                    builder.setIcon(getDrawable(R.drawable.error_color));
                    break;
            }
            builder.show();
        }
    }
}
