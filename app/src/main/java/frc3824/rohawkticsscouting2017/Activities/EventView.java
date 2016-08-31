package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart.LLD_Chart;
import frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart.LLD_Data;
import frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart.LLD_DataSet;
import frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart.LLD_Entry;
import frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart.LLD_MarkerView;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TCD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 */
public class EventView extends Activity implements AdapterView.OnItemSelectedListener, AxisValueFormatter{

    private final static String TAG = "EventView";

    private Spinner mMainDropdown;
    private ArrayAdapter<String> mMainAdapter;

    private Spinner mSecondaryDropdown;
    private ArrayAdapter<String> mSecondaryAdapter;

    private LLD_Chart mLLDChart;

    private Database mDatabase;
    private XAxis mXAxis;
    private ArrayList<Integer> mTeamNumbers;
    private ArrayList<Integer> mCurrentTeamNumbers;

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

        mLLDChart = (LLD_Chart) findViewById(R.id.lld_chart);
        mLLDChart.setMarkerView(new LLD_MarkerView(this, R.layout.marker_lld));
        mLLDChart.setDoubleTapToZoomEnabled(false);
        mLLDChart.setDescription("");

        YAxis yAxis = mLLDChart.getAxisRight();
        yAxis.setEnabled(false);

        mXAxis = mLLDChart.getXAxis();
        mXAxis.setLabelRotationAngle(90);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setDrawGridLines(false);
        mXAxis.setValueFormatter(this);


        Legend legend = mLLDChart.getLegend();
        legend.setEnabled(false);

        mDatabase = Database.getInstance();
        mTeamNumbers = mDatabase.getTeamNumbers();
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
                List<LLD_Entry> entries;
                LLD_DataSet dataset;
                LLD_Data data;
                switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position])
                {
                    case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                        entries = new ArrayList<>();
                        mCurrentTeamNumbers = new ArrayList<>();
                        for(int i = 0; i < mTeamNumbers.size(); i++)
                        {
                            TCD tcd  = mDatabase.getTCD(mTeamNumbers.get(i));
                            if(tcd != null) {
                                mCurrentTeamNumbers.add(tcd.team_number);
                                entries.add(new LLD_Entry((float) i, tcd.team_number, (float) tcd.fouls.max, (float) tcd.fouls.min, (float) tcd.fouls.average, (float) tcd.fouls.std));
                            }
                        }
                        dataset = new LLD_DataSet(entries, "");
                        data = new LLD_Data(dataset);
                        mLLDChart.setData(data);

                        mXAxis.setLabelCount(mCurrentTeamNumbers.size(), true);

                        mLLDChart.notifyDataSetChanged();
                        mLLDChart.invalidate();
                        break;
                    case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                        entries = new ArrayList<>();
                        mCurrentTeamNumbers = new ArrayList<>();
                        for(int i = 0; i < mTeamNumbers.size(); i++)
                        {
                            TCD tcd  = mDatabase.getTCD(mTeamNumbers.get(i));
                            if(tcd != null) {
                                mCurrentTeamNumbers.add(tcd.team_number);
                                entries.add(new LLD_Entry((float) i, tcd.team_number, (float) tcd.tech_fouls.max, (float) tcd.tech_fouls.min, (float) tcd.tech_fouls.average, (float) tcd.tech_fouls.std));
                            }
                        }
                        dataset = new LLD_DataSet(entries, "");
                        data = new LLD_Data(dataset);
                        mLLDChart.setData(data);

                        mXAxis.setLabelCount(mCurrentTeamNumbers.size(), true);

                        mLLDChart.notifyDataSetChanged();
                        mLLDChart.invalidate();
                        break;
                    case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                        entries = new ArrayList<>();
                        mCurrentTeamNumbers = new ArrayList<>();
                        for(int i = 0; i < mTeamNumbers.size(); i++)
                        {
                            TCD tcd  = mDatabase.getTCD(mTeamNumbers.get(i));
                            if(tcd != null) {
                                mCurrentTeamNumbers.add(tcd.team_number);
                                entries.add(new LLD_Entry((float) i, tcd.team_number, (float) tcd.yellow_cards.max, (float) tcd.yellow_cards.min, (float) tcd.yellow_cards.average, (float) tcd.yellow_cards.std));
                            }
                        }
                        dataset = new LLD_DataSet(entries, "");
                        data = new LLD_Data(dataset);
                        mLLDChart.setData(data);

                        mXAxis.setLabelCount(mCurrentTeamNumbers.size(), true);

                        mLLDChart.notifyDataSetChanged();
                        mLLDChart.invalidate();
                        break;
                    case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                        entries = new ArrayList<>();
                        mCurrentTeamNumbers = new ArrayList<>();
                        for(int i = 0; i < mTeamNumbers.size(); i++)
                        {
                            TCD tcd  = mDatabase.getTCD(mTeamNumbers.get(i));
                            if(tcd != null) {
                                mCurrentTeamNumbers.add(tcd.team_number);
                                entries.add(new LLD_Entry((float) i, tcd.team_number, (float) tcd.red_cards.max, (float) tcd.red_cards.min, (float) tcd.red_cards.average, (float) tcd.red_cards.std));
                            }
                        }
                        dataset = new LLD_DataSet(entries, "");
                        data = new LLD_Data(dataset);
                        mLLDChart.setData(data);

                        mXAxis.setLabelCount(mCurrentTeamNumbers.size(), true);

                        mLLDChart.notifyDataSetChanged();
                        mLLDChart.invalidate();
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return String.valueOf(mCurrentTeamNumbers.get((int)value));
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
