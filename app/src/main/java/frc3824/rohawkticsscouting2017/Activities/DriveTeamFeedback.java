package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.HashMap;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TDTF;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TID;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 9/26/16
 *
 *
 */
public class DriveTeamFeedback extends Activity {

    private final static String TAG = "DriveTeamFeedback";

    private int mMatchNumber;
    private int mPartner1;
    private int mPartner2;
    private EditText mPartner1Note;
    private EditText mPartner2Note;
    private Database mDatabase;
    private TID mTid;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_team_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.drive_team_feedback_toolbar);
        setActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMatchNumber = extras.getInt(Constants.Intent_Extras.MATCH_NUMBER);

        setTitle(String.format("Match Number: %d", mMatchNumber));

        mDatabase = Database.getInstance();
        Match match = mDatabase.getMatch(mMatchNumber);
        try {
            if (match.isBlue(mMatchNumber)) {
                for(int i = 0; i < 3; i++)
                {
                    if(match.teams.get(i) == Constants.OUR_TEAM_NUMBER)
                    {
                        mPartner1 = match.teams.get( (i + 1) % 3 );
                        mPartner2 = match.teams.get( (i + 2) % 3 );
                    }
                }
            } else {
                for(int i = 0; i < 3; i++)
                {
                    if(match.teams.get(i + 3) == Constants.OUR_TEAM_NUMBER)
                    {
                        mPartner1 = match.teams.get( ((i + 1) % 3) + 3);
                        mPartner2 = match.teams.get( ((i + 2) % 3) + 3);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

        TextView partner1_textview = (TextView)findViewById(R.id.partner_1);
        partner1_textview.setText(String.valueOf(mPartner1));
        TextView partner2_textview = (TextView)findViewById(R.id.partner_2);
        partner2_textview.setText(String.valueOf(mPartner2));

        mPartner1Note = (EditText)findViewById(R.id.partner_1_note);
        mPartner2Note = (EditText)findViewById(R.id.partner_2_note);

        mTid = mDatabase.getTID(Constants.OUR_TEAM_NUMBER);
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

        if (mMatchNumber == mTid.match_numbers.get(0)) {
            menu.removeItem(R.id.previous_match);
        }
        if (mMatchNumber == mTid.match_numbers.get(mTid.match_numbers.size() - 1)) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DriveTeamFeedback.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                save_press();

                // Go to the next match
                Intent intent = new Intent(DriveTeamFeedback.this, Home.class);
                startActivity(intent);
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
                Intent intent = new Intent(DriveTeamFeedback.this, Home.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DriveTeamFeedback.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                save_press();

                Intent intent = new Intent(DriveTeamFeedback.this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.DRIVE_TEAM_FEEDBACK);
                startActivity(intent);
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
                Intent intent = new Intent(DriveTeamFeedback.this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE, Constants.Intent_Extras.DRIVE_TEAM_FEEDBACK);
                startActivity(intent);
            }
        });
        builder.show();
    }

    /**
     * Saves the current data
     */
    private void save_press() {
        TDTF tdtf1 = mDatabase.getTDTF(mPartner1);
        if(tdtf1 == null)
        {
            tdtf1 = new TDTF();
            tdtf1.team_number = mPartner1;
            tdtf1.feedback = new HashMap<>();
        }
        tdtf1.feedback.put(mMatchNumber, mPartner1Note.getText().toString());

        TDTF tdtf2 = mDatabase.getTDTF(mPartner2);
        if(tdtf2 == null)
        {
            tdtf2 = new TDTF();
            tdtf2.team_number = mPartner2;
            tdtf2.feedback = new HashMap<>();
        }
        tdtf2.feedback.put(mMatchNumber, mPartner2Note.getText().toString());

        Log.d(TAG, "Saving values");

    }

    /**
     * The action that happens when the previous match button is pressed. Brings up dialog with options to save
     * and takes user to match scout the previous match.
     */
    private void previous_press() {
        Log.d(TAG, "previous match pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(DriveTeamFeedback.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                save_press();

                // Go to the previous match
                Intent intent = new Intent(DriveTeamFeedback.this, DriveTeamFeedback.class);

                for(int i = 0; i < mTid.match_numbers.size(); i++)
                {
                    if(mTid.match_numbers.get(i) == mMatchNumber)
                    {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mTid.match_numbers.get(i - 1));
                        break;
                    }
                }

                startActivity(intent);
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
                // Go to the previous match
                Intent intent = new Intent(DriveTeamFeedback.this, DriveTeamFeedback.class);
                for(int i = 0; i < mTid.match_numbers.size(); i++)
                {
                    if(mTid.match_numbers.get(i) == mMatchNumber)
                    {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mTid.match_numbers.get(i - 1));
                        break;
                    }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(DriveTeamFeedback.this);
        builder.setTitle("Save match data?");

        // Save option
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                save_press();

                // Go to the next match
                Intent intent = new Intent(DriveTeamFeedback.this, DriveTeamFeedback.class);
                for(int i = 0; i < mTid.match_numbers.size(); i++)
                {
                    if(mTid.match_numbers.get(i) == mMatchNumber)
                    {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mTid.match_numbers.get(i + 1));
                        break;
                    }
                }
                startActivity(intent);

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
                Intent intent = new Intent(DriveTeamFeedback.this, DriveTeamFeedback.class);
                for(int i = 0; i < mTid.match_numbers.size(); i++)
                {
                    if(mTid.match_numbers.get(i) == mMatchNumber)
                    {
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, mTid.match_numbers.get(i + 1));
                        break;
                    }
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
}
