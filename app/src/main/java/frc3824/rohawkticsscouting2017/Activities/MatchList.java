package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_MatchList;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * List of matches which forward to match scouting, super scouting, match view, or drive team feedback
 */
public class MatchList extends Activity{

    private final static String TAG = "MatchList";

    private String mNextPage;
    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        Bundle extras = getIntent().getExtras();
        mNextPage = extras.getString(Constants.Intent_Extras.NEXT_PAGE);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        mDatabase = Database.getInstance();

        int numberOfMatches = mDatabase.getNumberOfMatches();
        ArrayList<Integer> match_numbers = new ArrayList<>();

        ListView listView = (ListView) findViewById(R.id.match_list);
        LVA_MatchList.MatchListType mlt = LVA_MatchList.MatchListType.MATCH_SCOUT;
        switch (mNextPage) {
            case Constants.Intent_Extras.MATCH_SCOUTING:
                mlt = LVA_MatchList.MatchListType.MATCH_SCOUT;
                match_numbers.add(-1);
                for(int i = 1; i <= numberOfMatches; i++) {
                    match_numbers.add(i);
                }

                break;
            case Constants.Intent_Extras.SUPER_SCOUTING:
                mlt = LVA_MatchList.MatchListType.SUPER_SCOUT;
                match_numbers.add(-1);
                for(int i = 1; i <= numberOfMatches; i++) {
                    match_numbers.add(i);
                }
                break;
            case Constants.Intent_Extras.MATCH_VIEWING:
                mlt = LVA_MatchList.MatchListType.MATCH_VIEW;
                for(int i = 1; i <= numberOfMatches; i++) {
                    match_numbers.add(i);
                }
                break;
            case Constants.Intent_Extras.DRIVE_TEAM_FEEDBACK:
                mlt = LVA_MatchList.MatchListType.DRIVE_TEAM_FEEDBACK;
                for(int i = 1; i <= numberOfMatches; i++) {
                    Match match = mDatabase.getMatch(i);
                    try {
                        if (match.isBlue(Constants.OUR_TEAM_NUMBER) || match.isRed(Constants.OUR_TEAM_NUMBER)) {
                            match_numbers.add(i);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                break;
            default:
                assert false;
        }
        LVA_MatchList lva = new LVA_MatchList(this, match_numbers, mlt);

        if(mNextPage.equals(Constants.Intent_Extras.MATCH_SCOUTING)){
            String alliance_color = sharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
            int alliance_number = sharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
            lva.setMatchScoutExtras(sharedPreferences.getString(Constants.Settings.USER_TYPE, "").equals(Constants.User_Types.ADMIN),
                    alliance_color, alliance_number);
        }
        listView.setAdapter(lva);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
    }
}
