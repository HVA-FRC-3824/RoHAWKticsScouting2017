package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_SuperScouting;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_scouting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.super_scouting_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        SharedPreferences shared_preferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventKey = shared_preferences.getString(Constants.Settings.EVENT_KEY, "");

        mDatabase = Database.getInstance(eventKey);

        if(mMatchNumber > 0)
        {
            setTitle(String.format("Match: %d", mMatchNumber));
        }
        else
        {
            setTitle("Practice Match");
            mPractice = true;
        }

        findViewById(android.R.id.content).setKeepScreenOn(true);

        // Set up tabs and pages for different fragments of a match
        ViewPager viewPager = (ViewPager) findViewById(R.id.super_scouting_view_pager);
        mFPA = new FPA_SuperScouting(getFragmentManager());
        SMD sm = mDatabase.getSMD(mMatchNumber);
        if (sm != null) {
            mFPA.setValueMap(sm.toMap());
        }
        viewPager.setAdapter(mFPA);
        // Set the off screen page limit to more than the number of fragments
        viewPager.setOffscreenPageLimit(mFPA.getCount());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.super_scouting_tab_layout);
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
            map.put(Constants.Intent_Extras.MATCH_NUMBER, mMatchNumber);
            SMD smd = new SMD(map);
            mDatabase.setSMD(smd);

            //TODO: add Bluetooth and Syncing

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String text = values[0];
            Log.d(TAG, text);
            Toast.makeText(SuperScouting.this, text, Toast.LENGTH_SHORT).show();
        }
    }

}