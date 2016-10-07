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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_MatchScouting;
import frc3824.rohawkticsscouting2017.Bluetooth.BluetoothQueue;
import frc3824.rohawkticsscouting2017.Bluetooth.ConnectThread;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TDTF;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;

/**
 * @author frc3824
 * Created: 8/11/16
 *
 * Activity that handles match scouting. Also takes care of saving, and switching between matches.
 */
public class MatchScouting extends Activity {


    private final static String TAG = "MatchScouting";

    private FPA_MatchScouting mFPA;

    private int mTeamNumber = -1;
    private int mMatchNumber = -1;

    private boolean mPractice = false;

    private Database mDatabase;

    private String mServerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.match_scouting_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String allianceColor = shared_preferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
        int allianceNumber = shared_preferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
        mServerName = shared_preferences.getString(Constants.Settings.SERVER, "");

        mDatabase = Database.getInstance();

        if (mMatchNumber > 0) {
            if (allianceColor.equals(Constants.Alliance_Colors.BLUE)) {
                mTeamNumber = mDatabase.getMatch(mMatchNumber).teams.get(allianceNumber - 1);
            } else {
                mTeamNumber = mDatabase.getMatch(mMatchNumber).teams.get(allianceNumber + 2);
            }

            setTitle(String.format("Match Number: %d Team Number: %d", mMatchNumber, mTeamNumber));
        }
        // mMatchNumber is -1 for practice matches
        else {
            mPractice = true;
            setTitle("Practice Match");
        }

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.match_scouting_view_pager);
        mFPA = new FPA_MatchScouting(getFragmentManager());
        TMD tim = mDatabase.getTMD(mMatchNumber, mTeamNumber);
        if (tim != null) {
            mFPA.setValueMap(tim.toMap());
        }
        viewPager.setAdapter(mFPA);
        // Set the off screen page limit to more than the number of fragments
        viewPager.setOffscreenPageLimit(mFPA.getCount());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.match_scouting_tab_layout);
        if (allianceColor.equals(Constants.Alliance_Colors.BLUE)) {
            tabLayout.setBackgroundColor(Color.BLUE);
        } else {
            tabLayout.setBackgroundColor(Color.RED);
        }
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
    }

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
                        new SaveTask().execute(data);
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
                // Go to the next match
                Intent intent = new Intent(MatchScouting.this, Home.class);
                startActivity(intent);
            }
        });
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
                        new SaveTask().execute(data);
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
                Intent intent = new Intent(MatchScouting.this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.MATCH_SCOUTING);
                startActivity(intent);
            }
        });
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
                new SaveTask().execute(data);
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
                        new SaveTask().execute(data);
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
                // Go to the next match
                Intent intent = new Intent(MatchScouting.this, MatchScouting.class);
                if (mPractice) {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                } else {
                    intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber - 1);
                }
                startActivity(intent);
            }
        });
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
                        new SaveTask().execute(data);
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
            TMD tmd = new TMD(map);
            mDatabase.setTMD(tmd);

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothQueue queue = BluetoothQueue.getInstance();

            if(bluetoothAdapter == null)
            {
                queue.add(tmd);
                publishProgress(Constants.Bluetooth.Data_Transfer_Status.NO_BLUETOOTH);
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
                if(deviceName.equals(mServerName))
                {
                    server = device;
                    break;
                }
            }

            if(server == null)
            {
                publishProgress(Constants.Bluetooth.Data_Transfer_Status.SERVER_NOT_FOUND);
                queue.add(tmd);
                return null;
            }

            ConnectThread connectThread = new ConnectThread(server,true);
            connectThread.start();
            while (!connectThread.isConnected());
            Gson gson = new GsonBuilder().create();
            if(connectThread.write(String.format("%c%s",Constants.Bluetooth.Message_Headers.MATCH_HEADER, gson.toJson(tmd))))
            {
                publishProgress(Constants.Bluetooth.Data_Transfer_Status.SUCCESS);
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
                            case Constants.Bluetooth.Message_Headers.MATCH_HEADER:
                                queue.add(gson.fromJson(s.substring(1), TMD.class));
                                break;
                            case Constants.Bluetooth.Message_Headers.SUPER_HEADER:
                                queue.add(gson.fromJson(s.substring(1), SMD.class));
                                break;
                            case Constants.Bluetooth.Message_Headers.FEEDBACK_HEADER:
                                queue.add(gson.fromJson(s.substring(1), TDTF.class));
                                break;
                        }
                    }
                }
                if(queueEmpty && queuedString.size() > 0)
                {
                    publishProgress(Constants.Bluetooth.Data_Transfer_Status.QUEUE_EMPTIED);
                }
            }
            else
            {
                publishProgress(Constants.Bluetooth.Data_Transfer_Status.FAILURE);
                queue.add(tmd);
            }
            connectThread.cancel();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MatchScouting.this);
            switch (values[0])
            {
                case Constants.Bluetooth.Data_Transfer_Status.NO_BLUETOOTH:
                    builder.setTitle("No bluetooth on this device!!!");
                    builder.setIcon(getDrawable(R.drawable.bluetooth_2_color));
                    break;
                case Constants.Bluetooth.Data_Transfer_Status.QUEUE_EMPTIED:
                    break;
                case Constants.Bluetooth.Data_Transfer_Status.SERVER_NOT_FOUND:
                    builder.setTitle("Server not found");
                    builder.setIcon(getDrawable(R.drawable.error_color));
                    break;
                case Constants.Bluetooth.Data_Transfer_Status.SUCCESS:
                    builder.setTitle("Data transfer successful");
                    builder.setIcon(getDrawable(R.drawable.ok_color));
                    break;
                case Constants.Bluetooth.Data_Transfer_Status.FAILURE:
                    builder.setTitle("Data transfer failure (Added to Queue)");
                    builder.setIcon(getDrawable(R.drawable.error_color));
                    break;
            }
            builder.show();
        }
    }
}
