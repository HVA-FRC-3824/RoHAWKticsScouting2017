package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TID;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPA;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TRD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Match;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Ranking;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Team;
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
public class Settings extends Activity {

    private final static String TAG = "Settings";

    private boolean backAllowed = false;
    private SharedPreferences mSharedPreferences;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Utilities.setupUi(this, findViewById(android.R.id.content));


        mSharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        final Spinner colorSelector = (Spinner) findViewById(R.id.alliance_color_selector);
        String[] colors = new String[]{Constants.Alliance_Colors.BLUE, Constants.Alliance_Colors.RED};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorSelector.setAdapter(adapter1);
        colorSelector.setSelection(Arrays.asList(colors).indexOf(mSharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR,
                Constants.Alliance_Colors.BLUE)));

        final Spinner numSelector = (Spinner) findViewById(R.id.alliance_number_selector);
        String[] numbers = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        numSelector.setAdapter(adapter2);
        numSelector.setSelection(Arrays.asList(numbers).indexOf(Integer.toString(mSharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, 1))));

        final Spinner pitGroupSelector = (Spinner) findViewById(R.id.pit_group_selector);
        String[] numbers2 = new String[]{"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers2);
        pitGroupSelector.setAdapter(adapter3);
        pitGroupSelector.setSelection(Arrays.asList(numbers2).indexOf(Integer.toString(mSharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, 1))));

        final Spinner serverSelector = (Spinner) findViewById(R.id.server_selector);
        String[] devices = new String[]{Constants.Bluetooth.Device_Names.BLUE1, Constants.Bluetooth.Device_Names.BLUE2, Constants.Bluetooth.Device_Names.BLUE3,
        Constants.Bluetooth.Device_Names.RED1, Constants.Bluetooth.Device_Names.RED2, Constants.Bluetooth.Device_Names.RED3,
        Constants.Bluetooth.Device_Names.SUPER, Constants.Bluetooth.Device_Names.SERVER, Constants.Bluetooth.Device_Names.STRATEGY,
        Constants.Bluetooth.Device_Names.DRIVETEAM};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, devices);
        serverSelector.setAdapter(adapter4);
        serverSelector.setSelection(Arrays.asList(devices).indexOf(mSharedPreferences.getString(Constants.Settings.SERVER, "")));

        Spinner typeSelector = (Spinner) findViewById(R.id.type_selector);
        String[] types = new String[]{Constants.User_Types.MATCH_SCOUT, Constants.User_Types.PIT_SCOUT,
                Constants.User_Types.SUPER_SCOUT, Constants.User_Types.DRIVE_TEAM, Constants.User_Types.STRATEGY,
                Constants.User_Types.SERVER, Constants.User_Types.ADMIN};
        ArrayAdapter<String> adapter0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        typeSelector.setAdapter(adapter0);
        typeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case Constants.User_Types.ADMIN:
                        findViewById(R.id.alliance_number_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.alliance_color_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.pit_group_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.server_label).setVisibility(View.VISIBLE);
                        colorSelector.setVisibility(View.VISIBLE);
                        numSelector.setVisibility(View.VISIBLE);
                        pitGroupSelector.setVisibility(View.VISIBLE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.MATCH_SCOUT:
                        findViewById(R.id.alliance_number_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.alliance_color_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.pit_group_label).setVisibility(View.GONE);
                        findViewById(R.id.server_label).setVisibility(View.VISIBLE);
                        colorSelector.setVisibility(View.VISIBLE);
                        numSelector.setVisibility(View.VISIBLE);
                        pitGroupSelector.setVisibility(View.GONE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.PIT_SCOUT:
                        findViewById(R.id.alliance_number_label).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color_label).setVisibility(View.GONE);
                        findViewById(R.id.pit_group_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.server_label).setVisibility(View.VISIBLE);
                        colorSelector.setVisibility(View.GONE);
                        numSelector.setVisibility(View.GONE);
                        pitGroupSelector.setVisibility(View.VISIBLE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.SERVER:
                        findViewById(R.id.alliance_number_label).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color_label).setVisibility(View.GONE);
                        findViewById(R.id.pit_group_label).setVisibility(View.GONE);
                        findViewById(R.id.server_label).setVisibility(View.GONE);
                        colorSelector.setVisibility(View.GONE);
                        numSelector.setVisibility(View.GONE);
                        pitGroupSelector.setVisibility(View.GONE);
                        serverSelector.setVisibility(View.GONE);
                        break;
                    default:
                        findViewById(R.id.alliance_number_label).setVisibility(View.GONE);
                        findViewById(R.id.alliance_color_label).setVisibility(View.GONE);
                        findViewById(R.id.pit_group_label).setVisibility(View.GONE);
                        findViewById(R.id.server_label).setVisibility(View.VISIBLE);
                        colorSelector.setVisibility(View.GONE);
                        numSelector.setVisibility(View.GONE);
                        pitGroupSelector.setVisibility(View.GONE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String eventKeyText = mSharedPreferences.getString(Constants.Settings.EVENT_KEY, "");

        AutoCompleteTextView eventKey = (AutoCompleteTextView) findViewById(R.id.event_key);
        ArrayList<String> events = new ArrayList<>();
        Database.getInstance(eventKeyText);
        Set<String> eventList = Database.getEvents();
        events.addAll(eventList);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, events);
        eventKey.setAdapter(adapter5);
        eventKey.setText(eventKeyText);

        if(!eventKeyText.equals(""))
        {
            findViewById(R.id.homeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.pull_event_button).setVisibility(View.VISIBLE);
        }

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
    }

    /**
     * Saves the current settings to shared preferences if an event id is set
     *
     * @param view
     */
    public void saveSettings(View view) {
        Spinner typeSelector = (Spinner) findViewById(R.id.type_selector);
        Spinner colorSelector = (Spinner) findViewById(R.id.alliance_color_selector);
        Spinner numSelector = (Spinner) findViewById(R.id.alliance_number_selector);
        Spinner pitGroupSelector = (Spinner) findViewById(R.id.pit_group_selector);
        Spinner serverSelector = (Spinner) findViewById(R.id.server_selector);
        AutoCompleteTextView eventKey = (AutoCompleteTextView) findViewById(R.id.event_key);

        SharedPreferences.Editor prefEditor = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();

        String eventKeyText = String.valueOf(eventKey.getText());
        if (!eventKeyText.equals("")) {
            prefEditor.putString(Constants.Settings.EVENT_KEY, eventKeyText);
            Database.getInstance(eventKeyText);
            Storage.getInstance(eventKeyText);

            String type = String.valueOf(typeSelector.getSelectedItem());
            prefEditor.putString(Constants.Settings.USER_TYPE, type);

            if (String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.MATCH_SCOUT) || String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.ADMIN)) {
                prefEditor.putString(Constants.Settings.ALLIANCE_COLOR, String.valueOf(colorSelector.getSelectedItem()));
                prefEditor.putInt(Constants.Settings.ALLIANCE_NUMBER, Integer.parseInt(String.valueOf(numSelector.getSelectedItem())));
            }

            if (String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.PIT_SCOUT) || String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.ADMIN)) {
                prefEditor.putInt(Constants.Settings.PIT_GROUP_NUMBER, Integer.parseInt(String.valueOf(pitGroupSelector.getSelectedItem())));
            }

            if(!String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.SERVER))
            {
                prefEditor.putString(Constants.Settings.SERVER, String.valueOf(serverSelector.getSelectedItem()));
            }

            prefEditor.commit();
            Button homeButton = (Button) findViewById(R.id.homeButton);
            homeButton.setVisibility(View.VISIBLE);

            findViewById(R.id.pull_event_button).setVisibility(View.VISIBLE);
            backAllowed = true;

            Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
            toast.show();

        } else {
            findViewById(R.id.pull_event_button).setVisibility(View.GONE);
            backAllowed = false;
            Button homeButton = (Button) findViewById(R.id.homeButton);
            homeButton.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Event ID must be entered", Toast.LENGTH_LONG).show();
        }
    }

    public void pullEvent(View view)
    {
        mProgressBar.setVisibility(View.VISIBLE);
        new PullEvent().execute(mSharedPreferences.getString(Constants.Settings.EVENT_KEY, ""));
    }

    /**
     * Back button brings the user to the home screen
     *
     * @param view
     */
    public void home(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        if(backAllowed)
        {
            super.onBackPressed();
        }
    }

    private class PullEvent extends AsyncTask<String, Integer, Void>
    {

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
                Map<Integer, List<Integer>> teamMatchNumbers = new HashMap<>();
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
                    TID info = new TID();
                    info.team_number = tbaTeam.team_number;
                    info.nickname = tbaTeam.nickname;
                    info.match_numbers = teamMatchNumbers.get(info.team_number);
                    Collections.sort(info.match_numbers);
                    database.setTID(info);

                    TPD pit = new TPD();
                    pit.team_number = tbaTeam.team_number;
                    pit.pit_scouted = false;
                    database.setTPD(pit);

                    TPA pick = new TPA();
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
                    TRD ranking = new TRD();
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
        protected void onProgressUpdate(Integer... values)
        {
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
