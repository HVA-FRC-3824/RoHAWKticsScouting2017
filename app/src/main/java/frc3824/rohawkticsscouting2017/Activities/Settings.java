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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Match;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Team;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TheBlueAlliance;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created:
 *
 * Page that lets the user set all their preferences. Also can pull the event info from
 * The Blue Alliance
 */
public class Settings extends Activity {

    private final static String TAG = "Settings";

    private boolean pullAllowed = false;
    private boolean backAllowed = false;
    private SharedPreferences mSharedPreferences;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        final Spinner colorSelector = (Spinner) findViewById(R.id.allianceColorSelector);
        String[] colors = new String[]{Constants.Alliance_Colors.BLUE, Constants.Alliance_Colors.RED};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorSelector.setAdapter(adapter1);
        colorSelector.setSelection(Arrays.asList(colors).indexOf(mSharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR,
                Constants.Alliance_Colors.BLUE)));

        final Spinner numSelector = (Spinner) findViewById(R.id.allianceNumberSelector);
        String[] numbers = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        numSelector.setAdapter(adapter2);
        numSelector.setSelection(Arrays.asList(numbers).indexOf(Integer.toString(mSharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, 1))));

        final Spinner pitGroupSelector = (Spinner) findViewById(R.id.pitGroupSelector);
        String[] numbers2 = new String[]{"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers2);
        pitGroupSelector.setAdapter(adapter3);
        pitGroupSelector.setSelection(Arrays.asList(numbers2).indexOf(Integer.toString(mSharedPreferences.getInt(Constants.Settings.PIT_GROUP_NUMBER, 1))));

        final Spinner serverSelector = (Spinner) findViewById(R.id.serverSelector);
        String[] devices = new String[]{};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, devices);
        serverSelector.setAdapter(adapter4);
        serverSelector.setSelection(Arrays.asList(devices).indexOf(mSharedPreferences.getString(Constants.Settings.SERVER, "")));

        Spinner typeSelector = (Spinner) findViewById(R.id.typeSelector);
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
                        findViewById(R.id.textView3).setVisibility(View.VISIBLE);
                        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                        findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                        colorSelector.setVisibility(View.VISIBLE);
                        numSelector.setVisibility(View.VISIBLE);
                        pitGroupSelector.setVisibility(View.VISIBLE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.MATCH_SCOUT:
                        findViewById(R.id.textView3).setVisibility(View.VISIBLE);
                        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                        findViewById(R.id.textView4).setVisibility(View.GONE);
                        colorSelector.setVisibility(View.VISIBLE);
                        numSelector.setVisibility(View.VISIBLE);
                        pitGroupSelector.setVisibility(View.GONE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.PIT_SCOUT:
                        findViewById(R.id.textView3).setVisibility(View.GONE);
                        findViewById(R.id.textView2).setVisibility(View.GONE);
                        findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                        colorSelector.setVisibility(View.GONE);
                        numSelector.setVisibility(View.GONE);
                        pitGroupSelector.setVisibility(View.VISIBLE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    case Constants.User_Types.SERVER:
                        findViewById(R.id.textView3).setVisibility(View.GONE);
                        findViewById(R.id.textView2).setVisibility(View.GONE);
                        findViewById(R.id.textView4).setVisibility(View.GONE);
                        colorSelector.setVisibility(View.GONE);
                        numSelector.setVisibility(View.GONE);
                        pitGroupSelector.setVisibility(View.GONE);
                        serverSelector.setVisibility(View.VISIBLE);
                        break;
                    default:
                        findViewById(R.id.textView3).setVisibility(View.GONE);
                        findViewById(R.id.textView2).setVisibility(View.GONE);
                        findViewById(R.id.textView4).setVisibility(View.GONE);
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
        Spinner typeSelector = (Spinner) findViewById(R.id.typeSelector);
        Spinner colorSelector = (Spinner) findViewById(R.id.allianceColorSelector);
        Spinner numSelector = (Spinner) findViewById(R.id.allianceNumberSelector);
        Spinner pitGroupSelector = (Spinner) findViewById(R.id.pitGroupSelector);
        Spinner serverSelector = (Spinner) findViewById(R.id.serverSelector);
        AutoCompleteTextView eventKey = (AutoCompleteTextView) findViewById(R.id.event_key);

        SharedPreferences.Editor prefEditor = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();

        String eventKeyText = String.valueOf(eventKey.getText());
        if (!eventKeyText.equals("")) {
            prefEditor.putString(Constants.Settings.EVENT_KEY, eventKeyText);
            String type = String.valueOf(typeSelector.getSelectedItem());
            prefEditor.putString(Constants.Settings.USER_TYPE, type);

            if (String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.MATCH_SCOUT) || String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.ADMIN)) {
                prefEditor.putString(Constants.Settings.ALLIANCE_COLOR, String.valueOf(colorSelector.getSelectedItem()));
                prefEditor.putInt(Constants.Settings.ALLIANCE_NUMBER, Integer.parseInt(String.valueOf(numSelector.getSelectedItem())));
            }

            if (String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.PIT_SCOUT) || String.valueOf(typeSelector.getSelectedItem()).equals(Constants.User_Types.ADMIN)) {
                prefEditor.putInt(Constants.Settings.PIT_GROUP_NUMBER, Integer.parseInt(String.valueOf(pitGroupSelector.getSelectedItem())));
            }

            //TODO: add saving of the selected server (requires there to be server options)

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
            Toast.makeText(this, "Event ID must be entered", Toast.LENGTH_LONG);
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
                Database database = Database.getInstance(eventKey);

                ArrayList<TBA_Team> teams = theBlueAlliance.getEventTeams(eventKey);
                int numberOfTeams = teams.size();
                ArrayList<TBA_Match> matches = theBlueAlliance.getEventMatches(eventKey);
                int numberOfMatches = matches.size();
                int currentIndex = 0;
                Map<Integer, List<Integer>> teamMatchNumbers = new HashMap<>();
                publishProgress(currentIndex, numberOfTeams + numberOfMatches);
                for(TBA_Match tbaMatch: matches)
                {
                    if(!tbaMatch.comp_level.equals("qm")) {
                        currentIndex++;
                        publishProgress(currentIndex, numberOfTeams + numberOfMatches);
                        continue;
                    }

                    Match match = new Match();
                    match.match_number = tbaMatch.match_number;

                    match.blue1 = Integer.parseInt(tbaMatch.alliances.blue.teams[0].substring(3));
                    match.blue2 = Integer.parseInt(tbaMatch.alliances.blue.teams[1].substring(3));
                    match.blue3 = Integer.parseInt(tbaMatch.alliances.blue.teams[2].substring(3));

                    match.blue_score = tbaMatch.alliances.blue.score;

                    match.red1 = Integer.parseInt(tbaMatch.alliances.red.teams[0].substring(3));
                    match.red2 = Integer.parseInt(tbaMatch.alliances.red.teams[1].substring(3));
                    match.red3 = Integer.parseInt(tbaMatch.alliances.red.teams[2].substring(3));

                    match.red_score = tbaMatch.alliances.red.score;

                    if(!teamMatchNumbers.containsKey(match.blue1))
                    {
                        teamMatchNumbers.put(match.blue1,new ArrayList<Integer>());
                    }
                    teamMatchNumbers.get(match.blue1).add(match.match_number);

                    if(!teamMatchNumbers.containsKey(match.blue2)) {
                        teamMatchNumbers.put(match.blue2, new ArrayList<Integer>());
                    }
                    teamMatchNumbers.get(match.blue2).add(match.match_number);

                    if(!teamMatchNumbers.containsKey(match.blue3))
                    {
                        teamMatchNumbers.put(match.blue3,new ArrayList<Integer>());
                    }
                    teamMatchNumbers.get(match.blue3).add(match.match_number);

                    if(!teamMatchNumbers.containsKey(match.red1))
                    {
                        teamMatchNumbers.put(match.red1,new ArrayList<Integer>());

                    }
                    teamMatchNumbers.get(match.red1).add(match.match_number);

                    if(!teamMatchNumbers.containsKey(match.red2))
                    {
                        teamMatchNumbers.put(match.red2,new ArrayList<Integer>());
                    }
                    teamMatchNumbers.get(match.red2).add(match.match_number);

                    if(!teamMatchNumbers.containsKey(match.red3))
                    {
                        teamMatchNumbers.put(match.red3,new ArrayList<Integer>());
                    }
                    teamMatchNumbers.get(match.red3).add(match.match_number);

                    database.setMatch(match);
                    currentIndex++;
                    publishProgress(currentIndex, numberOfTeams + numberOfMatches);
                }

                publishProgress(currentIndex, numberOfTeams + numberOfMatches);
                for(TBA_Team tbaTeam: teams)
                {
                    Team team = new Team();
                    team.team_number = tbaTeam.team_number;
                    team.nickname = tbaTeam.nickname;
                    team.pit_scouted = false;
                    team.match_numbers = teamMatchNumbers.get(team.team_number);
                    Collections.sort(team.match_numbers);
                    database.setTeam(team);
                    currentIndex++;
                    publishProgress(currentIndex, numberOfTeams + numberOfMatches);
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
