package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TCD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 */
public class EventView extends Activity implements AdapterView.OnItemSelectedListener{

    private final static String TAG = "EventView";

    private Spinner mMainDropdown;
    private ArrayAdapter<String> mMainAdapter;

    private Spinner mSecondaryDropdown;
    private ArrayAdapter<String> mSecondaryAdapter;

    private CandleStickChart mCandleStickChart;

    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        mMainDropdown = (Spinner)findViewById(R.id.main_dropdown);
        mMainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Constants.Event_View.Main_Dropdown_Options.OPTIONS);
        mMainDropdown.setAdapter(mMainAdapter);
        mMainDropdown.setOnItemSelectedListener(this);

        mSecondaryDropdown = (Spinner)findViewById(R.id.secondary_dropdown);
        mSecondaryDropdown.setOnItemSelectedListener(this);

        mCandleStickChart = (CandleStickChart) findViewById(R.id.candle_stick_chart);

        mDatabase = Database.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch (parent.getId())
        {
            case R.id.main_dropdown:
                switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[position])
                {
                    case Constants.Event_View.Main_Dropdown_Options.FOULS:
                        mSecondaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Constants.Event_View.Foul_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);
                        break;
                }
                break;
            case R.id.secondary_dropdown:
                switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position])
                {
                    case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                        final ArrayList<Integer> team_numbers = mDatabase.getTeamNumbers();
                        List<CandleEntry> entries = new ArrayList<>();
                        for(int i = 0; i < team_numbers.size(); i++)
                        {
                            TCD tcd  = mDatabase.getTCD(team_numbers.get(i));
                            if(tcd != null) {
                                //high, low, open, close
                                entries.add(new CandleEntry((float) i, (float) tcd.fouls.max, (float) tcd.fouls.min, (float) (tcd.fouls.average + tcd.fouls.std), (float) (tcd.fouls.average - tcd.fouls.std)));
                            }
                        }
                        CandleDataSet candleDataSet = new CandleDataSet(entries, "");
                        CandleData candleData = new CandleData(candleDataSet);
                        mCandleStickChart.setData(candleData);

                        XAxis xAxis = mCandleStickChart.getXAxis();

                        xAxis.setValueFormatter(new AxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return String.valueOf(team_numbers.get((int)value));
                            }

                            @Override
                            public int getDecimalDigits() {
                                return 0;
                            }
                        });

                        mCandleStickChart.notifyDataSetChanged();
                        mCandleStickChart.invalidate();
                        break;
                    case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                        break;
                    case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                        break;
                    case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
