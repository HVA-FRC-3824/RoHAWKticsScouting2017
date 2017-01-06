package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_PitScouting;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_PitScoutDrawer;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Activity for recording pit scouting data
 */
public class PitScouting extends Activity {

    private final static String TAG = "PitScouting";

    private final static int REQUEST_CAMERA_PERMISSION = 3;

    private int mTeamNumber;
    private Database mDatabase;
    private String mEventKey;
    private String mLastScoutName;
    private String mScoutName;
    private FPA_PitScouting mFPA;

    private int mTeamBefore;
    private int mTeamAfter;
    private int mFirstTeamInPitGroup;
    private int mLastTeamInPitGroup;

    private ListView mDrawerList;
    private LVA_PitScoutDrawer mLVA;

    private AlertDialog mLogisticsDialog;
    private View mLogisticsView;
    private AutoCompleteTextView mScoutNameTextView;
    private View mLogisticsScoutNameBackground;
    private TextView mLogisticsIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        mTeamNumber = extras.getInt(Constants.Intent_Extras.TEAM_NUMBER);
        mDatabase = Database.getInstance();

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mEventKey = shared_preferences.getString(Constants.Settings.EVENT_KEY, "");
        mLastScoutName = shared_preferences.getString(Constants.Settings.LAST_PIT_SCOUT, "");

        ArrayList<Integer> teams = mDatabase.getTeamNumbers();
        if (shared_preferences.getString(Constants.Settings.USER_TYPE, "").equals(Constants.User_Types.PIT_SCOUT)) {
            int pit_group = shared_preferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, -1);
            int group_size = (int) ((float) (teams.size()) / 6.0 + 0.5f);
            int start = group_size * (pit_group - 1);
            mFirstTeamInPitGroup = teams.get(start);
            int end = group_size * (pit_group);
            if (end > teams.size()) {
                end = teams.size();
            }
            mLastTeamInPitGroup = teams.get(end - 1);
            teams = new ArrayList(teams.subList(start, end));
        } else {
            mFirstTeamInPitGroup = teams.get(0);
            mLastTeamInPitGroup = teams.get(teams.size() - 1);
        }

        setTitle(String.format("Team: %d", mTeamNumber));

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mFPA = new FPA_PitScouting(getFragmentManager());
        TeamPitData team = mDatabase.getTeamPitData(mTeamNumber);
        if (team != null) {
            mFPA.setValueMap(team.toMap());
            mScoutName = team.scout_name;
        }

        viewPager.setAdapter(mFPA);
        viewPager.setOffscreenPageLimit(mFPA.getCount());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        mLVA = new LVA_PitScoutDrawer(this, mDatabase.getTeamNumbers());
        mDrawerList.setAdapter(mLVA);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mLogisticsView = LayoutInflater.from(this).inflate(R.layout.dialog_super_logistics, null);
        ((TextView) mLogisticsView.findViewById(R.id.match_number)).setText(String.format("Team Number: %d", mTeamNumber));

        mScoutNameTextView = (AutoCompleteTextView) mLogisticsView.findViewById(R.id.scout_name);
        if (!mLastScoutName.equals("")) {
            String[] arr = {mLastScoutName};
            ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
            mScoutNameTextView.setAdapter(aa);
        }
        builder.setView(mLogisticsView);

        mLogisticsIncorrect = (TextView) mLogisticsView.findViewById(R.id.incorrect);
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
                        if (mScoutName.equals("")) {
                            mLogisticsScoutNameBackground.setBackgroundColor(Color.RED);
                            mLogisticsIncorrect.setVisibility(View.VISIBLE);
                        } else {
                            SharedPreferences.Editor edit = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                            edit.putString(Constants.Settings.LAST_PIT_SCOUT, mScoutName);
                            edit.commit();
                            mLogisticsDialog.dismiss();
                        }
                    }
                });
            }
        });
        if (mScoutName == null || mScoutName.equals("")) {
            mScoutNameTextView.setText(mScoutName);
            mLogisticsDialog.show();
        }
    }

    /**
     * Creates the overflow menu for the toolbar. Removes previous team or next team options if
     * they do not exist.
     *
     * @param menu The menu that is filled with the overflow menu.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.team_overflow, menu);
        mTeamBefore = mDatabase.getTeamNumberBefore(mTeamNumber);
        if (mTeamBefore == 0 || mTeamBefore < mFirstTeamInPitGroup) {
            menu.removeItem(R.id.previous_team);
        }
        mTeamAfter = mDatabase.getTeamNumberAfter(mTeamNumber);
        if (mTeamAfter == 0 || mTeamAfter > mLastTeamInPitGroup) {
            menu.removeItem(R.id.next_team);
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
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "onMenuOpened", e);
                } catch (Exception e) {
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
            case R.id.team_list:
                back_press();
                break;
            case R.id.save:
                save_press();
                break;
            case R.id.previous_team:
                previous_press();
                break;
            case R.id.next_team:
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PitScouting.this);
        builder.setTitle("Save pit data?");

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

                    new SaveTask().execute(data);

                    // Go to the next match
                    Intent intent = new Intent(PitScouting.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PitScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();

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
                Intent intent = new Intent(PitScouting.this, Home.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PitScouting.this);
        builder.setTitle("Save pit data?");

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

                    new SaveTask().execute(data);

                    Intent intent = new Intent(PitScouting.this, TeamList.class);
                    intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.PIT_SCOUTING);
                    startActivity(intent);
                } else {
                    Toast.makeText(PitScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(PitScouting.this, TeamList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.PIT_SCOUTING);
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

            new SaveTask().execute(data);

        } else {
            Toast.makeText(PitScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The action that happens when the previous match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the previous match.
     */
    private void previous_press() {
        Log.d(TAG, "previous team pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(PitScouting.this);
        builder.setTitle("Save pit data?");

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
                    new SaveTask().execute(data);

                    Intent intent = new Intent(PitScouting.this, PitScouting.class);
                    intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeamBefore);
                    startActivity(intent);
                } else {
                    Toast.makeText(PitScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
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
                // Go to the next team
                Intent intent = new Intent(PitScouting.this, PitScouting.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeamBefore);
                startActivity(intent);
            }
        });
        builder.show();
    }

    /**
     * The action that happens when the previous match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the previous match.
     */
    private void next_press() {
        Log.d(TAG, "next team pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(PitScouting.this);
        builder.setTitle("Save pit data?");

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
                    new SaveTask().execute(data);

                    Intent intent = new Intent(PitScouting.this, PitScouting.class);
                    intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeamAfter);
                    startActivity(intent);
                } else {
                    Toast.makeText(PitScouting.this, String.format("Error: %s", error), Toast.LENGTH_LONG).show();
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
                // Go to the next team
                Intent intent = new Intent(PitScouting.this, PitScouting.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mTeamAfter);
                startActivity(intent);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        back_press();
    }

    private class SaveTask extends AsyncTask<ScoutMap, String, Void> {

        @Override
        protected Void doInBackground(ScoutMap... scoutMaps) {
            ScoutMap map = scoutMaps[0];
            map.put(Constants.Intent_Extras.TEAM_NUMBER, mTeamNumber);
            map.put(Constants.Pit_Scouting.SCOUT_NAME, mScoutName);
            // Change picture filename to use event id and team number
            if (map.contains(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS)) {
                try {
                    ArrayList<String> picture_filepaths = (ArrayList) map.getObject(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS);
                    ArrayList<String> new_picture_filepaths = new ArrayList<>();
                    for (int i = 0; i < picture_filepaths.size(); i++) {
                        String picture_filename = picture_filepaths.get(i);
                        if (!picture_filename.equals("")) {
                            File picture = new File(picture_filename);
                            if (picture.exists() && picture.length() > 0) {
                                String newPathName = String.format("%s/robot_pictures/", mEventKey);
                                File newPath = new File(getFilesDir(), newPathName);
                                if (!newPath.exists()) {
                                    newPath.mkdirs();
                                }
                                File newPicture = new File(newPath, String.format("%d_%d.jpg", mTeamNumber, i));
                                newPicture.delete();
                                copy(picture, newPicture);
                                picture.delete();
                                new_picture_filepaths.add(newPicture.getAbsolutePath());
                            } else {
                                map.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS);
                            }
                        }
                    }
                    map.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS);
                    map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS, new_picture_filepaths);
                } catch (ScoutValue.TypeException | IOException e) {
                    Log.e(TAG, e.getMessage());
                }

            }

            TeamPitData team = new TeamPitData(map);
            mDatabase.setTeamPitData(team);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String text = values[0];
            Log.d(TAG, text);
            Toast.makeText(PitScouting.this, text, Toast.LENGTH_SHORT).show();
        }

        private void copy(final File f1, final File f2) throws IOException {
            f2.createNewFile();

            final RandomAccessFile file1 = new RandomAccessFile(f1, "r");
            final RandomAccessFile file2 = new RandomAccessFile(f2, "rw");

            file2.getChannel().write(file1.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, f1.length()));

            file1.close();
            file2.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFPA.cameraHasPermission();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

        }
    }
}
