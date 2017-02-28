package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
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
        String[] server_options = new String[]{Constants.Server_Type.BLUETOOTH, Constants.Server_Type.SOCKET};
        ArrayAdapter<String> server_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, server_options);
        server_selector.setAdapter(server_adapter);
        server_selector.setSelection(Arrays.asList(server_options).indexOf(
                mSharedPreferences.getString(Constants.Settings.SERVER_TYPE, "")));

        mUserTypeSelector = (Spinner) findViewById(R.id.user_type_selector);
        Spinner user_type_selector = mUserTypeSelector;
        ArrayAdapter<String> user_type_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Constants.User_Types.LIST);
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
        user_type_selector.setSelection(Arrays.asList(Constants.User_Types.LIST).indexOf(
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
            mBackAllowed = true;
        }

        findViewById(R.id.home_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
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

                    String server = String.valueOf(mServerSelector.getSelectedItem());
                    prefEditor.putString(Constants.Settings.SERVER_TYPE, server);

                    prefEditor.commit();
                    findViewById(R.id.home_button).setVisibility(View.VISIBLE);
                    mBackAllowed = true;

                    Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    mBackAllowed = false;
                    findViewById(R.id.home_button).setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Event Key must be entered", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
