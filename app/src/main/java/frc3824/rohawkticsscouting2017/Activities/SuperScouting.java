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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_SuperScouting;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_MatchScoutDrawer;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.MatchNumberCheck;
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
 * Created: 8/16/16
 *
 * The activity that handles all the super scout fragments
 */
public class SuperScouting extends Activity{

    private final static String TAG = "SuperScouting";

    private int mMatchNumber = -1;

    private boolean mPractice = false;

    private Database mDatabase;

    private FPA_SuperScouting mFPA;
    private String mServerName;
    private String mLastScoutName;
    private String mScoutName;

    private ListView mDrawerList;
    private LVA_MatchScoutDrawer mLVA;

    private AlertDialog mLogisticsDialog;
    private View mLogisticsView;
    private AutoCompleteTextView mScoutNameTextView;
    private View mLogisticsScoutNameBackground;
    private TextView mLogisticsIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.super_scouting_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mServerName = shared_preferences.getString(Constants.Settings.SERVER, "");
        mLastScoutName = shared_preferences.getString(Constants.Settings.LAST_SUPER_SCOUT, "");


        mDatabase = Database.getInstance();

        if(mMatchNumber > 0)
        {
            setTitle(String.format("Match: %d", mMatchNumber));

            mDrawerList = (ListView)findViewById(R.id.drawer_list);
            ArrayList<MatchNumberCheck> mncs = new ArrayList<>();
            for(int i = 1; i <= mDatabase.getNumberOfMatches(); i++){
                if(mDatabase.getSMD(i) != null){
                    mncs.add(new MatchNumberCheck(i, true));
                } else {
                    mncs.add(new MatchNumberCheck(i));
                }
            }

            mLVA = new LVA_MatchScoutDrawer(this, mncs);
            mDrawerList.setAdapter(mLVA);
        }
        else
        {
            setTitle("Practice Match");
            mPractice = true;
        }

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.super_scouting_view_pager);
        mFPA = new FPA_SuperScouting(getFragmentManager(), mMatchNumber);
        SMD sm = mDatabase.getSMD(mMatchNumber);
        if (sm != null) {
            mFPA.setValueMap(sm.toMap());
            mScoutName = sm.scout_name;
        }
        viewPager.setAdapter(mFPA);
        // Set the off screen page limit to more than the number of fragments
        viewPager.setOffscreenPageLimit(mFPA.getCount());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.super_scouting_tab_layout);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mLogisticsView = LayoutInflater.from(this).inflate(R.layout.dialog_super_logistics, null);
        ((TextView)mLogisticsView.findViewById(R.id.match_number)).setText(String.format("Match Number: %d", mMatchNumber));

        mScoutNameTextView = (AutoCompleteTextView)mLogisticsView.findViewById(R.id.scout_name);
        if(!mLastScoutName.equals("")){
            String[] arr = {mLastScoutName};
            ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
            mScoutNameTextView.setAdapter(aa);
        }
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
                        if(mScoutName.equals("")){
                            mLogisticsScoutNameBackground.setBackgroundColor(Color.RED);
                            mLogisticsIncorrect.setVisibility(View.VISIBLE);
                        } else {
                            SharedPreferences.Editor edit = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                            edit.putString(Constants.Settings.LAST_SUPER_SCOUT, mScoutName);
                            edit.commit();
                            mLogisticsDialog.dismiss();
                        }
                    }
                });
            }
        });
        if(mScoutName == null || mScoutName.equals("")) {
            mLogisticsDialog.show();
        }
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
        menu.removeItem(R.id.switch_team);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SuperScouting.this);
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
                    Intent intent = new Intent(SuperScouting.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SuperScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();

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
                Intent intent = new Intent(SuperScouting.this, Home.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SuperScouting.this);
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

                    Intent intent = new Intent(SuperScouting.this, MatchList.class);
                    intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.SUPER_SCOUTING);
                    startActivity(intent);
                } else {
                    Toast.makeText(SuperScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(SuperScouting.this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.SUPER_SCOUTING);
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
                new SaveTask().execute(data);
            }
        } else {
            Toast.makeText(SuperScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The action that happens when the previous match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the previous match.
     */
    private void previous_press() {
        Log.d(TAG, "previous match pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(SuperScouting.this);
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
                    Intent intent = new Intent(SuperScouting.this, SuperScouting.class);
                    if (mPractice) {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                    } else {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber - 1);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(SuperScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(SuperScouting.this, SuperScouting.class);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(SuperScouting.this);
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
                    Intent intent = new Intent(SuperScouting.this, SuperScouting.class);
                    if (mPractice) {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, -1);
                    } else {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber + 1);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(SuperScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(SuperScouting.this, SuperScouting.class);
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
            map.put(Constants.Super_Scouting.SCOUT_NAME, mScoutName);
            SMD smd = new SMD(map);
            mDatabase.setSMD(smd);

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothQueue queue = BluetoothQueue.getInstance();

            if(bluetoothAdapter == null)
            {
                queue.add(smd);
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
                queue.add(smd);
                return null;
            }

            ConnectThread connectThread = new ConnectThread(server,true);
            connectThread.start();
            while (!connectThread.isConnected());
            Gson gson = new GsonBuilder().create();
            if(connectThread.write(String.format("%c%s",Constants.Bluetooth.Message_Headers.SUPER_HEADER, gson.toJson(smd))))
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
                queue.add(smd);
            }
            connectThread.cancel();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SuperScouting.this);
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
