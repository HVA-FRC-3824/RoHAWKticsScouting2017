package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPref = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);

        final Spinner colorSelector = (Spinner) findViewById(R.id.allianceColorSelector);
        String[] colors = new String[]{Constants.Alliance_Colors.BLUE, Constants.Alliance_Colors.RED};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorSelector.setAdapter(adapter1);
        colorSelector.setSelection(Arrays.asList(colors).indexOf(sharedPref.getString(Constants.Settings.ALLIANCE_COLOR,
                Constants.Alliance_Colors.BLUE)));

        final Spinner numSelector = (Spinner) findViewById(R.id.allianceNumberSelector);
        String[] numbers = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        numSelector.setAdapter(adapter2);
        numSelector.setSelection(Arrays.asList(numbers).indexOf(Integer.toString(sharedPref.getInt(Constants.Settings.ALLIANCE_NUMBER, 1))));

        final Spinner pitGroupSelector = (Spinner) findViewById(R.id.pitGroupSelector);
        String[] numbers2 = new String[]{"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numbers2);
        pitGroupSelector.setAdapter(adapter3);
        pitGroupSelector.setSelection(Arrays.asList(numbers2).indexOf(Integer.toString(sharedPref.getInt(Constants.Settings.PIT_GROUP_NUMBER, 1))));

        final Spinner serverSelector = (Spinner) findViewById(R.id.serverSelector);
        String[] devices = new String[]{};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, devices);
        serverSelector.setAdapter(adapter4);
        serverSelector.setSelection(Arrays.asList(devices).indexOf(sharedPref.getString(Constants.Settings.SERVER, "")));

        Spinner typeSelector = (Spinner) findViewById(R.id.typeSelector);
        String[] types = new String[]{Constants.User_Types.MATCH_SCOUT, Constants.User_Types.PIT_SCOUT,
                Constants.User_Types.SUPER_SCOUT, Constants.User_Types.DRIVE_TEAM, Constants.User_Types.STRATEGY,
                Constants.User_Types.SERVER, Constants.User_Types.ADMIN};
        ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
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
    }
}
