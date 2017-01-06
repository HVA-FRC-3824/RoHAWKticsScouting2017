package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamRankingData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.DataModel.TBA_Match;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.DataModel.TBA_Ranking;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.DataModel.TBA_Team;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TheBlueAlliance;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author frc3824
 * Created:
 *
 * Page that lets the user set all their preferences. Also can pull the event info from
 * The Blue Alliance
 */
public class Settings extends Activity implements OnClickListener{

    private final static String TAG = "Settings";

    private boolean mBackAllowed = false;
    private SharedPreferences mSharedPreferences;
    private ProgressBar mProgressBar;

    private Spinner mAllianceColorSelector;
    private Spinner mAllianceNumberSelector;
    private Spinner mPitGroupSelector;
    private Spinner mServerSelector;
    private Spinner mUserTypeSelector;
    private AutoCompleteTextView mEventKeyTextView;

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Utilities.setupUi(this, findViewById(android.R.id.content));

        mSharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        mAllianceColorSelector = (Spinner) findViewById(R.id.alliance_color_selector);
        final Spinner alliance_color_selector = mAllianceColorSelector;
        String[] alliance_colors_options = new String[]{Constants.Alliance_Colors.BLUE, Constants.Alliance_Colors.RED};
        ArrayAdapter<String> alliance_color_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, alliance_colors_options);
        alliance_color_selector.setAdapter(alliance_color_adapter);
        alliance_color_selector.setSelection(Arrays.asList(alliance_colors_options).indexOf(
                mSharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.BLUE)));

        mAllianceNumberSelector = (Spinner) findViewById(R.id.alliance_number_selector);
        final Spinner alliance_number_selector = mAllianceNumberSelector;
        String[] alliance_number_options = new String[]{"1", "2", "3"};
        ArrayAdapter<String> alliance_number_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, alliance_number_options);
        alliance_number_selector.setAdapter(alliance_number_adapter);
        alliance_number_selector.setSelection(Arrays.asList(alliance_number_options).indexOf(
                Integer.toString(mSharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, 1))));

        mPitGroupSelector = (Spinner) findViewById(R.id.pit_group_selector);
        final Spinner pit_group_selector = mPitGroupSelector;
        String[] pit_group_options = new String[]{"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> pit_group_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pit_group_options);
        pit_group_selector.setAdapter(pit_group_adapter);
        pit_group_selector.setSelection(Arrays.asList(pit_group_options).indexOf(
                Integer.toString(mSharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, 1))));

        mServerSelector = (Spinner) findViewById(R.id.server_selector);
        final Spinner server_selector = mServerSelector;
        String[] server_options = new String[]{Constants.Bluetooth.Device_Names.BLUE1, Constants.Bluetooth.Device_Names.BLUE2, Constants.Bluetooth.Device_Names.BLUE3,
        Constants.Bluetooth.Device_Names.RED1, Constants.Bluetooth.Device_Names.RED2, Constants.Bluetooth.Device_Names.RED3,
        Constants.Bluetooth.Device_Names.SUPER, Constants.Bluetooth.Device_Names.SERVER, Constants.Bluetooth.Device_Names.STRATEGY,
        Constants.Bluetooth.Device_Names.DRIVETEAM, Constants.Bluetooth.Device_Names.RED_PI, Constants.Socket.SERVER};
        ArrayAdapter<String> server_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, server_options);
        server_selector.setAdapter(server_adapter);
        server_selector.setSelection(Arrays.asList(server_options).indexOf(
                mSharedPreferences.getString(Constants.Settings.SERVER, "")));

        mUserTypeSelector = (Spinner) findViewById(R.id.user_type_selector);
        Spinner user_type_selector = mUserTypeSelector;
        String[] user_type_options = new String[]{Constants.User_Types.MATCH_SCOUT, Constants.User_Types.PIT_SCOUT,
                Constants.User_Types.SUPER_SCOUT, Constants.User_Types.DRIVE_TEAM, Constants.User_Types.STRATEGY,
                Constants.User_Types.SERVER, Constants.User_Types.ADMIN};
        ArrayAdapter<String> user_type_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, user_type_options);
        user_type_selector.setAdapter(user_type_adapter);
        user_type_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case Constants.User_Types.ADMIN:
                        findViewById(R.id.alliance_number).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color).setVisibility(View.GONE);
                        findViewById(R.id.pit_group).setVisibility(View.GONE);
                        findViewById(R.id.server).setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.MATCH_SCOUT:
                        findViewById(R.id.alliance_number).setVisibility(View.VISIBLE);
                        findViewById(R.id.alliance_color).setVisibility(View.VISIBLE);
                        findViewById(R.id.pit_group).setVisibility(View.GONE);
                        findViewById(R.id.server).setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.PIT_SCOUT:
                        findViewById(R.id.alliance_number).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color).setVisibility(View.GONE);
                        findViewById(R.id.pit_group).setVisibility(View.VISIBLE);
                        findViewById(R.id.server).setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.SERVER:
                        findViewById(R.id.alliance_number).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color).setVisibility(View.GONE);
                        findViewById(R.id.pit_group).setVisibility(View.GONE);
                        findViewById(R.id.server).setVisibility(View.GONE);
                        break;
                    default:
                        findViewById(R.id.alliance_number).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color).setVisibility(View.GONE);
                        findViewById(R.id.pit_group).setVisibility(View.GONE);
                        findViewById(R.id.server).setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        user_type_selector.setSelection(Arrays.asList(user_type_options).indexOf(
                mSharedPreferences.getString(Constants.Settings.USER_TYPE, "")));

        String event_key = mSharedPreferences.getString(Constants.Settings.EVENT_KEY, "");

        mEventKeyTextView = (AutoCompleteTextView) findViewById(R.id.event_key);
        AutoCompleteTextView event_key_textview = mEventKeyTextView;
        ArrayList<String> events = new ArrayList<>();
        Database.getInstance(event_key);
        Set<String> event_list = Database.getEvents();
        events.addAll(event_list);
        ArrayAdapter<String> events_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, events);
        event_key_textview.setAdapter(events_adapter);
        event_key_textview.setText(event_key);

        if(!event_key.equals(""))
        {
            findViewById(R.id.home_button).setVisibility(View.VISIBLE);
            findViewById(R.id.pull_event_button).setVisibility(View.VISIBLE);
            mBackAllowed = true;
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        findViewById(R.id.home_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
        findViewById(R.id.pull_event_button).setOnClickListener(this);
    }

    /**
     * Only allow back to be used after settings have been saved
     */
    @Override
    public void onBackPressed() {
        if(mBackAllowed)
        {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }

    /**
     * Function for when a button is clicked. In this activity there are three buttons: home, save and
     * pull event data
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_button:
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                break;
            case R.id.pull_event_button:
                mProgressBar.setVisibility(View.VISIBLE);
                new PullEvent().execute(mSharedPreferences.getString(Constants.Settings.EVENT_KEY, ""));
                break;
            case R.id.save_button:
                SharedPreferences.Editor prefEditor = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();

                String event_key = String.valueOf(mEventKeyTextView.getText());
                if (!event_key.equals("")) {
                    prefEditor.putString(Constants.Settings.EVENT_KEY, event_key);
                    Database.getInstance(event_key);
                    Storage.getInstance(event_key);

                    String user_type = String.valueOf(mUserTypeSelector.getSelectedItem());
                    prefEditor.putString(Constants.Settings.USER_TYPE, user_type);

                    if (user_type.equals(Constants.User_Types.MATCH_SCOUT) || user_type.equals(Constants.User_Types.ADMIN)) {
                        String alliance_color = String.valueOf(mAllianceColorSelector.getSelectedItem());
                        prefEditor.putString(Constants.Settings.ALLIANCE_COLOR, alliance_color);
                        int alliance_number = Integer.parseInt(String.valueOf(mAllianceNumberSelector.getSelectedItem()));
                        prefEditor.putInt(Constants.Settings.ALLIANCE_NUMBER, alliance_number);
                    }

                    if (user_type.equals(Constants.User_Types.PIT_SCOUT) || user_type.equals(Constants.User_Types.ADMIN)) {
                        int pit_group = Integer.parseInt(String.valueOf(mPitGroupSelector.getSelectedItem()));
                        prefEditor.putInt(Constants.Settings.PIT_GROUP_NUMBER, pit_group);
                    }

                    if(!user_type.equals(Constants.User_Types.SERVER)) {
                        String server = String.valueOf(mServerSelector.getSelectedItem());
                        prefEditor.putString(Constants.Settings.SERVER, server);
                    }

                    prefEditor.commit();
                    findViewById(R.id.home_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.pull_event_button).setVisibility(View.VISIBLE);
                    mBackAllowed = true;

                    Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    findViewById(R.id.pull_event_button).setVisibility(View.GONE);
                    mBackAllowed = false;
                    findViewById(R.id.home_button).setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Event Key must be entered", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Asynchronous task to pull data from the blue alliance
     * (May not be needed if using the python server)
     */
    private class PullEvent extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String eventKey = strings[0];

            TheBlueAlliance theBlueAlliance = TheBlueAlliance.getInstance();
            try {
                Database database = Database.getInstance();

                ArrayList<TBA_Team> teams = theBlueAlliance.getEventTeams(eventKey);
                int numberOfTeams = teams.size();
                ArrayList<TBA_Match> matches = theBlueAlliance.getEventMatches(eventKey);
                int numberOfMatches = matches.size();
                ArrayList<TBA_Ranking> rankings = theBlueAlliance.getEventRankings(eventKey);

                int total = 2 * numberOfTeams + numberOfMatches;
                int currentIndex = 0;
                Map<Integer, ArrayList<Integer>> teamMatchNumbers = new HashMap<>();
                publishProgress(currentIndex, total);
                for(TBA_Match tbaMatch: matches)
                {
                    if(!tbaMatch.comp_level.equals("qm")) {
                        currentIndex++;
                        publishProgress(currentIndex, total);
                        continue;
                    }

                    Match match = new Match();
                    match.match_number = tbaMatch.match_number;
                    match.teams = new ArrayList<>();
                    match.scores = new ArrayList<>();

                    match.teams.add(Integer.parseInt(tbaMatch.alliances.blue.teams[0].substring(3)));
                    match.teams.add(Integer.parseInt(tbaMatch.alliances.blue.teams[1].substring(3)));
                    match.teams.add(Integer.parseInt(tbaMatch.alliances.blue.teams[2].substring(3)));

                    match.scores.add(tbaMatch.alliances.blue.score);

                    match.teams.add(Integer.parseInt(tbaMatch.alliances.red.teams[0].substring(3)));
                    match.teams.add(Integer.parseInt(tbaMatch.alliances.red.teams[1].substring(3)));
                    match.teams.add(Integer.parseInt(tbaMatch.alliances.red.teams[2].substring(3)));

                    match.scores.add(tbaMatch.alliances.red.score);

                    for(int i = 0; i < match.teams.size(); i++)
                    {
                        if(!teamMatchNumbers.containsKey(match.teams.get(i)))
                        {
                            teamMatchNumbers.put(match.teams.get(i), new ArrayList<Integer>());
                        }
                        teamMatchNumbers.get(match.teams.get(i)).add(match.match_number);
                    }

                    database.setMatch(match);
                    currentIndex++;
                    publishProgress(currentIndex, total);
                }

                publishProgress(currentIndex, total);
                for(TBA_Team tbaTeam: teams)
                {
                    TeamLogistics info = new TeamLogistics();
                    info.team_number = tbaTeam.team_number;
                    info.nickname = tbaTeam.nickname;
                    info.match_numbers = teamMatchNumbers.get(info.team_number);
                    Collections.sort(info.match_numbers);
                    database.setTID(info);

//                    TeamPitData pit = new TeamPitData();
//                    pit.team_number = tbaTeam.team_number;
//                    pit.pit_scouted = false;
//                    database.setTeamPitData(pit);

                    TeamPickAbility pick = new TeamPickAbility();
                    pick.team_number = tbaTeam.team_number;
                    pick.nickname = tbaTeam.nickname;
                    pick.manual_ranking = -1;
                    database.setFirstTPA(pick);
                    database.setSecondTPA(pick);
                    database.setThirdTPA(pick);

                    currentIndex++;
                    publishProgress(currentIndex, total);
                }
                publishProgress(currentIndex, total);
                for(TBA_Ranking tbaRanking: rankings)
                {
                    TeamRankingData ranking = new TeamRankingData();
                    ranking.team_number = tbaRanking.team_number;
                    ranking.rank = tbaRanking.rank;
                    ranking.RPs = tbaRanking.RPs;
                    ranking.wins = tbaRanking.wins;
                    ranking.ties = tbaRanking.ties;
                    ranking.loses = tbaRanking.loses;
                    ranking.played = tbaRanking.played;
                    ranking.auto = tbaRanking.auto;
                    ranking.scale_challenge = tbaRanking.scale_challenge;
                    ranking.goals = tbaRanking.goals;
                    ranking.defenses = tbaRanking.defenses;
                    database.setCurrentTRD(ranking);
                    currentIndex++;
                    publishProgress(currentIndex, total);
                }

            } catch (IOException e) {
                Log.e(TAG, "Error: pulling event from The Blue Alliance");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int numCompleted = values[0];
            int numTotal = values[0];
            mProgressBar.setMax(numTotal);
            mProgressBar.setProgress(numCompleted);
        }

        @Override
        protected void onPostExecute(Void v)
        {
            mProgressBar.setVisibility(View.GONE);
        }

    }
}
