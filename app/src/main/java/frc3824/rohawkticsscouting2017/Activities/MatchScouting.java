package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_MatchScouting;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_MatchScoutDrawer;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.MatchNumberCheck;
import frc3824.rohawkticsscouting2017.Comms.ConnectThread;
import frc3824.rohawkticsscouting2017.Comms.MessageQueue;
import frc3824.rohawkticsscouting2017.Comms.SocketThread;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamDTFeedback;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;

/**
 * @author frc3824
 * Created: 8/11/16
 *
 * Activity that handles match scouting. Also takes care of saving, and switching between matches.
 */
public class MatchScouting extends Activity{


    private final static String TAG = "MatchScouting";

    private FPA_MatchScouting mFPA;

    private int mTeamNumber = -1;
    private int mMatchNumber = -1;

    private String mAllianceColor;
    private int mAllianceNumber;
    private String mScoutName;
    private boolean mAdmin;

    private boolean mPractice = false;

    private Database mDatabase;
    private String mServerType;

    private ListView mDrawerList;
    private LVA_MatchScoutDrawer mLVA;

    private AlertDialog mLogisticsDialog;
    private View mLogisticsView;
    private TextView mLogisticsAlliance;
    private AutoCompleteTextView mScoutNameTextView;
    private View mLogisticsScoutNameBackground;
    private TextView mLogisticsIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mAllianceColor = shared_preferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
        mAllianceNumber = shared_preferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
        mServerType = shared_preferences.getString(Constants.Settings.SERVER_TYPE, "");
        mAdmin = shared_preferences.getString(Constants.Settings.USER_TYPE, "").equals(Constants.User_Types.ADMIN);

        mDatabase = Database.getInstance();

        if (mMatchNumber > 0) {
            if (mAllianceColor.equals(Constants.Alliance_Colors.BLUE)) {
                mTeamNumber = mDatabase.getMatch(mMatchNumber).teams.get(mAllianceNumber - 1);
            } else {
                mTeamNumber = mDatabase.getMatch(mMatchNumber).teams.get(mAllianceNumber + 2);
            }

            setTitle(String.format("Match Number: %d Team Number: %d", mMatchNumber, mTeamNumber));

            mDrawerList = (ListView)findViewById(R.id.drawer_list);
            ArrayList<MatchNumberCheck> mncs = new ArrayList<>();
            for(int i = 1; i <= mDatabase.getNumberOfMatches(); i++){
                if(mDatabase.getTeamMatchData(i,  mDatabase.getMatch(i).teams.get(mAllianceNumber - 1)) != null){
                    mncs.add(new MatchNumberCheck(i, true));
                } else {
                    mncs.add(new MatchNumberCheck(i));
                }
            }

            mLVA = new LVA_MatchScoutDrawer(this, mncs);
            mDrawerList.setAdapter(mLVA);

        } else {
            // mMatchNumber is -1 for practice matches
            mPractice = true;
            setTitle("Practice Match");
        }

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mFPA = new FPA_MatchScouting(getFragmentManager());
        TeamMatchData tim = mDatabase.getTeamMatchData(mMatchNumber, mTeamNumber);
        if (tim != null) {
            mFPA.setValueMap(tim.toMap());
            mScoutName = tim.scout_name;
        }
        viewPager.setAdapter(mFPA);
        viewPager.setOffscreenPageLimit(mFPA.getCount());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mAllianceColor.equals(Constants.Alliance_Colors.BLUE)) {
            tabLayout.setBackgroundColor(Color.BLUE);
        } else {
            tabLayout.setBackgroundColor(Color.RED);
        }
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mLogisticsView = LayoutInflater.from(this).inflate(R.layout.dialog_match_logistics, null);
        ((TextView)mLogisticsView.findViewById(R.id.match_number)).setText(String.format("Match Number: %d", mMatchNumber));
        ((TextView)mLogisticsView.findViewById(R.id.team_number)).setText(String.format("Team Number: %d", mTeamNumber));
        mLogisticsAlliance = (TextView)mLogisticsView.findViewById(R.id.alliance);
        mLogisticsAlliance.setText(String.format("%s %d", mAllianceColor.equals(Constants.Alliance_Colors.BLUE) ? "Blue" : "Red", mAllianceNumber));
        if(mAllianceColor.equals(Constants.Alliance_Colors.BLUE)){
            mLogisticsAlliance.setTextColor(Color.BLUE);
        } else {
            mLogisticsAlliance.setTextColor(Color.RED);
        }
        mScoutNameTextView = (AutoCompleteTextView)mLogisticsView.findViewById(R.id.scout_name);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Constants.Settings.MATCH_SCOUTS_LIST);
        mScoutNameTextView.setAdapter(aa);
        builder.setView(mLogisticsView);

        mLogisticsIncorrect = (TextView)mLogisticsView.findViewById(R.id.incorrect);
        mLogisticsScoutNameBackground = mLogisticsView.findViewById(R.id.scout_name_background);

        builder.setPositiveButton("Ok", null);
        builder.setCancelable(false);
        mLogisticsDialog = builder.create();
        mLogisticsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mScoutName = mScoutNameTextView.getText().toString();
                        mScoutName = mScoutName.trim();
                        mScoutName = mScoutName.replace("\n", "");
                        Pattern p = Pattern.compile("[^a-zA-Z -]");
                        boolean hasSpecialChar = p.matcher(mScoutName).find();
                        if(mScoutName.isEmpty() || hasSpecialChar){
                            mLogisticsScoutNameBackground.setBackgroundColor(Color.RED);
                            mLogisticsIncorrect.setVisibility(View.VISIBLE);
                        } else {
                            mLogisticsDialog.dismiss();
                        }
                    }
                });
            }
        });
        if(mScoutName == null || mScoutName.isEmpty()) {
            mLogisticsDialog.show();
        }

    }

    // TODO: Create option for admin to switch which alliance color and/or number
    /**
     * Creates the overflow menu for the toolbar. Removes previous match or next match options if
     * they do not exist.
     *
     * @param menu The menu that is filled with the overflow menu.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_overflow, menu);
        if (mPractice || mMatchNumber == 1) {
            menu.removeItem(R.id.previous_match);
        }
        if (mPractice || mMatchNumber == mDatabase.getNumberOfMatches()) {
            menu.removeItem(R.id.next_match);
        }
        if(!mAdmin){
            menu.removeItem(R.id.switch_team);
        }
        return true;
    }

    /**
     * Override to show icons on the overflow menu
     * http://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * Implements the actions for the overflow menu
     *
     * @param item Menu item that is selected from the overflow menu
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                home_press();
                break;
            case R.id.match_list:
                back_press();
                break;
            case R.id.save:
                save_press();
                break;
            case R.id.previous_match:
                previous_press();
                break;
            case R.id.next_match:
                next_press();
                break;
            case R.id.switch_team:
                //TODO: add switching team
                break;
            case R.id.scout_name:
                mLogisticsDialog.show();
                break;
            default:
                assert false;
        }
        return true;
    }

    /**
     * The action that happens when the home button is pressed. Brings up dialog with options to save
     * and takes user to the home screen.
     */
    private void home_press() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScouting.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "save pressed");
                // Collect values from all the custom elements
                List<ScoutFragment> fragmentList = mFPA.getAllFragments();
                ScoutMap data = new ScoutMap();
                String error = "";
                for (ScoutFragment fragment : fragmentList) {
                    error += fragment.writeContentsToMap(data);
                }

                if (error.equals("")) {
                    Log.d(TAG, "Saving values");
                    if (!mPractice) {
                        new SaveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
                    }

                    // Go to the next match
                    Intent intent = new Intent(MatchScouting.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MatchScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();

                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                // Go to home
                Intent intent = new Intent(MatchScouting.this, Home.class);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * The action that happens when the back button is pressed. Brings up dialog with options to save
     * and takes user to the match list.
     */
    private void back_press() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScouting.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "save pressed");
                // Collect values from all the custom elements
                List<ScoutFragment> fragmentList = mFPA.getAllFragments();
                ScoutMap data = new ScoutMap();
                String error = "";
                for (ScoutFragment fragment : fragmentList) {
                    error += fragment.writeContentsToMap(data);
                }

                if (error.equals("")) {
                    Log.d(TAG, "Saving values");
                    if (!mPractice) {
                        new SaveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
                    }

                    Intent intent = new Intent(MatchScouting.this, MatchList.class);
                    intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.MATCH_SCOUTING);
                    startActivity(intent);
                } else {
                    Toast.makeText(MatchScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                Intent intent = new Intent(MatchScouting.this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.MATCH_SCOUTING);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Saves the current data
     */
    private void save_press() {
        // Collect values from all the custom elements
        List<ScoutFragment> fragmentList = mFPA.getAllFragments();
        ScoutMap data = new ScoutMap();
        String error = "";
        for (ScoutFragment fragment : fragmentList) {
            error += fragment.writeContentsToMap(data);
        }

        if (error.equals("")) {
            Log.d(TAG, "Saving values");
            if (!mPractice) {
                new SaveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
            }
        } else {
            Toast.makeText(MatchScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The action that happens when the previous match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the previous match.
     */
    private void previous_press() {
        Log.d(TAG, "previous match pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScouting.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Collect values from all the custom elements
                List<ScoutFragment> fragmentList = mFPA.getAllFragments();
                ScoutMap data = new ScoutMap();
                String error = "";
                for (ScoutFragment fragment : fragmentList) {
                    error += fragment.writeContentsToMap(data);
                }

                if (error.equals("")) {
                    Log.d(TAG, "Saving values");
                    if (!mPractice) {
                        new SaveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
                    }

                    // Go to the next match
                    Intent intent = new Intent(MatchScouting.this, MatchScouting.class);
                    if (mPractice) {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                    } else {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber - 1);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(MatchScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                // Go to the previous match
                Intent intent = new Intent(MatchScouting.this, MatchScouting.class);
                if (mPractice) {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                } else {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber - 1);
                }
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * The action that happens when the next match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the next match.
     */
    private void next_press() {
        Log.d(TAG, "next match pressed");

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchScouting.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d(TAG, "Save pressed");
                // Collect values from all the custom elements
                List<ScoutFragment> fragmentList = mFPA.getAllFragments();
                ScoutMap data = new ScoutMap();
                String error = "";
                for (ScoutFragment fragment : fragmentList) {
                    error += fragment.writeContentsToMap(data);
                }

                if (error.equals("")) {
                    if (!mPractice) {
                        Log.d(TAG, "Saving values");
                        new SaveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
                    }

                    // Go to the next match
                    Intent intent = new Intent(MatchScouting.this, MatchScouting.class);
                    if (mPractice) {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                    } else {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber + 1);
                    }
                    startActivity(intent);
                } else {
                    Log.e(TAG, error);
                    Toast.makeText(MatchScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Cancel Option
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dialogbox goes away
            }
        });

        // Continue w/o Saving Option
        builder.setNegativeButton("Continue w/o Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cont W/O Saving");
                // Go to the next match
                Intent intent = new Intent(MatchScouting.this, MatchScouting.class);
                if (mPractice) {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                } else {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber + 1);
                }
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        back_press();
    }

    private class SaveTask extends AsyncTask<ScoutMap, Integer, Void> {

        @Override
        protected Void doInBackground(ScoutMap... scoutMaps) {
            ScoutMap map = scoutMaps[0];
            map.put(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber);
            map.put(Constants.Intent_Extras.TEAM_NUMBER, mTeamNumber);
            map.put(Constants.Settings.ALLIANCE_COLOR, mAllianceColor);
            map.put(Constants.Settings.ALLIANCE_NUMBER, mAllianceNumber);
            map.put(Constants.Match_Scouting.SCOUT_NAME, mScoutName);
            TeamMatchData teamMatchData = new TeamMatchData(map);
            mDatabase.setTeamMatchData(teamMatchData);

            if(mServerType.equals(Constants.Server_Type.SOCKET)){
                return socketVersion(teamMatchData);
            } else {
                return bluetoothVersion(teamMatchData);
            }
        }

        private Void socketVersion(TeamMatchData teamMatchData){
            try {
                Socket socket = new Socket("localhost", Constants.Socket.PORT);

                MessageQueue queue = MessageQueue.getInstance();
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
                Gson gson = new GsonBuilder().create();
                if(socketThread.write(String.format("%c%s", Constants.Comms.Message_Headers.MATCH_HEADER, gson.toJson(teamMatchData))))
                {
                    publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                    List<String> queuedString = queue.getQueueList();
                    queue.clear();
                    boolean queueEmpty = true;
                    for (String s: queuedString)
                    {
                        if(!socketThread.write(s))
                        {
                            queueEmpty = false;
                            switch (s.charAt(0))
                            {
                                case Constants.Comms.Message_Headers.MATCH_HEADER:
                                    queue.add(gson.fromJson(s.substring(1), TeamMatchData.class));
                                    break;
                                case Constants.Comms.Message_Headers.SUPER_HEADER:
                                    queue.add(gson.fromJson(s.substring(1), SuperMatchData.class));
                                    break;
                                case Constants.Comms.Message_Headers.FEEDBACK_HEADER:
                                    queue.add(gson.fromJson(s.substring(1), TeamDTFeedback.class));
                                    break;
                            }
                        }
                    }
                    if(queueEmpty && queuedString.size() > 0)
                    {
                        publishProgress(Constants.Comms.Data_Transfer_Status.QUEUE_EMPTIED);
                    }
                }
                else
                {
                    publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                    queue.add(teamMatchData);
                }
                socketThread.cancel();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private Void bluetoothVersion(TeamMatchData teamMatchData){
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            MessageQueue queue = MessageQueue.getInstance();

            if(bluetoothAdapter == null)
            {
                queue.add(teamMatchData);
                publishProgress(Constants.Comms.Data_Transfer_Status.NO_BLUETOOTH);
                return null;
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
                Log.d(TAG, deviceName);
                if(deviceName.equals(mServerType))
                {
                    server = device;
                    break;
                }
            }

            if(server == null)
            {
                publishProgress(Constants.Comms.Data_Transfer_Status.SERVER_NOT_FOUND);
                queue.add(teamMatchData);
                return null;
            }

            ConnectThread connectThread = new ConnectThread(server,true);
            connectThread.start();
            while (!connectThread.isConnected());
            Gson gson = new GsonBuilder().create();
            if(connectThread.write(String.format("%c%s", Constants.Comms.Message_Headers.MATCH_HEADER, gson.toJson(teamMatchData))))
            {
                publishProgress(Constants.Comms.Data_Transfer_Status.SUCCESS);
                List<String> queuedString = queue.getQueueList();
                queue.clear();
                boolean queueEmpty = true;
                for (String s: queuedString)
                {
                    if(!connectThread.write(s))
                    {
                        queueEmpty = false;
                        switch (s.charAt(0))
                        {
                            case Constants.Comms.Message_Headers.MATCH_HEADER:
                                queue.add(gson.fromJson(s.substring(1), TeamMatchData.class));
                                break;
                            case Constants.Comms.Message_Headers.SUPER_HEADER:
                                queue.add(gson.fromJson(s.substring(1), SuperMatchData.class));
                                break;
                            case Constants.Comms.Message_Headers.FEEDBACK_HEADER:
                                queue.add(gson.fromJson(s.substring(1), TeamDTFeedback.class));
                                break;
                        }
                    }
                }
                if(queueEmpty && queuedString.size() > 0)
                {
                    publishProgress(Constants.Comms.Data_Transfer_Status.QUEUE_EMPTIED);
                }
            }
            else
            {
                publishProgress(Constants.Comms.Data_Transfer_Status.FAILURE);
                queue.add(teamMatchData);
            }
            connectThread.cancel();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MatchScouting.this);
            switch (values[0])
            {
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
                    builder.setTitle("Data transfer successful");
                    builder.setIcon(getDrawable(R.drawable.ok_color));
                    break;
                case Constants.Comms.Data_Transfer_Status.FAILURE:
                    builder.setTitle("Data transfer failure (Added to Queue)");
                    builder.setIcon(getDrawable(R.drawable.error_color));
                    break;
            }
            builder.show();
        }
    }
}
