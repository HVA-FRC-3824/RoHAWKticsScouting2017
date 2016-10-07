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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.List;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_PitScouting;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
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
    private FPA_PitScouting mFPA;
    private String mEventKey;

    private int mTeamBefore;
    private int mTeamAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.pit_scouting_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        mTeamNumber = extras.getInt(Constants.Intent_Extras.TEAM_NUMBER);

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mEventKey = shared_preferences.getString(Constants.Settings.EVENT_KEY, "");
        mDatabase = Database.getInstance();

        setTitle(String.format("Team: %d", mTeamNumber));

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.pit_scouting_view_pager);
        mFPA = new FPA_PitScouting(getFragmentManager());
        TPD team = mDatabase.getTPD(mTeamNumber);
        if (team != null && team.pit_scouted) {
            mFPA.setValueMap(team.toMap());
        }

        viewPager.setAdapter(mFPA);
        viewPager.setOffscreenPageLimit(mFPA.getCount());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.pit_scouting_tab_layout);
        tabLayout.setBackgroundColor(Color.BLUE);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        tabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        tabLayout.setupWithViewPager(viewPager);
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
        //TODO: check if next or previous is in the group
        getMenuInflater().inflate(R.menu.team_overflow, menu);
        mTeamBefore = mDatabase.getTeamNumberBefore(mTeamNumber);
        if (mTeamBefore == 0) {
            menu.removeItem(R.id.previous_team);
        }
        mTeamAfter = mDatabase.getTeamNumberAfter(mTeamNumber);
        if (mTeamAfter == 0) {
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
            map.put(Constants.Pit_Scouting.PIT_SCOUTED, true);

            // Change picture filename to use event id and team number
            if (map.contains(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH)) {
                String picture_filename = null;
                try {
                    picture_filename = map.getString(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);
                    if (!picture_filename.equals("")) {
                        File picture = new File(picture_filename);
                        if (picture.exists() && picture.length() > 0) {
                            String newPathName = String.format("%s/robot_pictures/", mEventKey);
                            File newPath = new File(getFilesDir(), newPathName);
                            if (!newPath.exists()) {
                                newPath.mkdirs();
                            }
                            File newPicture = new File(newPath, String.format("%d.jpg", mTeamNumber));
                            newPicture.delete();
                            copy(picture, newPicture);
                            picture.delete();
                            map.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);
                            map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH, newPicture.getAbsolutePath());
                        } else {
                            map.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);
                        }
                    }
                } catch (ScoutValue.TypeException | IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            TPD team = new TPD(map);
            mDatabase.setTPD(team);

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
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
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
