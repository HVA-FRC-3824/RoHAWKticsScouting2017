package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.CustomCharts.BarChart.BarEntryWithTeamNumber;
import frc3824.rohawkticsscouting2017.CustomCharts.BarChart.Bar_MarkerView;
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
 *
 * An activity for comparing all the teams at an event
 */
public class EventView extends Activity implements AdapterView.OnItemSelectedListener{

    private final static String TAG = "EventView";

    private Spinner mMainDropdown;
    private ArrayAdapter<String> mMainAdapter;

    private Spinner mSecondaryDropdown;
    private ArrayAdapter<String> mSecondaryAdapter;

    private LLD_Chart mLLDChart;
    private BarChart mBarChart;

    private Database mDatabase;
    private XAxis mXAxis;
    private YAxis mYAxis;
    private ArrayList<Integer> mTeamNumbers;
    private ArrayList<String> mCurrentTeamNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        mMainDropdown = (Spinner)findViewById(R.id.main_dropdown);
        mMainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                Constants.Event_View.Main_Dropdown_Options.OPTIONS);
        mMainDropdown.setAdapter(mMainAdapter);
        mMainDropdown.setOnItemSelectedListener(this);

        mSecondaryDropdown = (Spinner)findViewById(R.id.secondary_dropdown);
        mSecondaryDropdown.setOnItemSelectedListener(this);

        //region Low Level Data Chart Setup
        mLLDChart = (LLD_Chart) findViewById(R.id.lld_chart);
        mLLDChart.setMarkerView(new LLD_MarkerView(this, R.layout.marker_lld));
        mLLDChart.setDoubleTapToZoomEnabled(false);
        mLLDChart.setPinchZoom(false);
        mLLDChart.setDescription("");
        //endregion

        //region Bar Chart Setup
        mBarChart = (BarChart) findViewById(R.id.bar_chart);
        mBarChart.setMarkerView(new Bar_MarkerView(this, R.layout.marker_bar));
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDescription("");
        //endregion

        //region Y-Axis Setup
        YAxis yAxis = mLLDChart.getAxisRight();
        yAxis.setEnabled(false);
        yAxis = mBarChart.getAxisRight();
        yAxis.setEnabled(false);
        //endregion

        //region X-Axis Setup
        mXAxis = mLLDChart.getXAxis();
        mXAxis.setLabelRotationAngle(90);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setDrawGridLines(false);

        mXAxis = mBarChart.getXAxis();
        mXAxis.setLabelRotationAngle(90);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setDrawGridLines(false);
        //endregion

        //region Legend
        Legend legend = mLLDChart.getLegend();
        legend.setEnabled(false);
        legend = mBarChart.getLegend();
        legend.setEnabled(false);
        //endregion

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
                        mSecondaryAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                Constants.Event_View.Foul_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.POST_MATCH:
                        mSecondaryAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                Constants.Event_View.Post_Match_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);

                    // Add GAME SPECIFIC Main Dropdown Options
                }
                break;
            case R.id.secondary_dropdown:

                switch (mMainDropdown.getSelectedItem().toString()) {
                    case Constants.Event_View.Main_Dropdown_Options.FOULS:
                        foulSecondary(position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.POST_MATCH:
                        postMatchSecondary(position);
                        break;

                    // Add GAME SPECIFIC Secondary Dropdown Options
                }
                break;
        }
    }

    private void foulSecondary(int position) {
        mLLDChart.setVisibility(View.VISIBLE);
        mBarChart.setVisibility(View.GONE);


        List<LLD_Entry> entries;
        LLD_DataSet dataset;
        LLD_Data data;
        switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position]) {
            case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                //region Fouls
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.fouls.max, (float) tcd.fouls.min, (float) tcd.fouls.average, (float) tcd.fouls.std));
                    }
                }
                dataset = new LLD_DataSet(entries, "");
                data = new LLD_Data(mCurrentTeamNumbers, dataset);
                mLLDChart.setData(data);

                mXAxis = mLLDChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                mYAxis = mLLDChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mLLDChart.notifyDataSetChanged();
                mLLDChart.invalidate();
                break;
                //endregion
            case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                //region Tech Fouls
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.tech_fouls.max, (float) tcd.tech_fouls.min, (float) tcd.tech_fouls.average, (float) tcd.tech_fouls.std));
                    }
                }
                dataset = new LLD_DataSet(entries, "");
                data = new LLD_Data(mCurrentTeamNumbers, dataset);
                mLLDChart.setData(data);

                mXAxis = mLLDChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                mYAxis = mLLDChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mLLDChart.notifyDataSetChanged();
                mLLDChart.invalidate();
                break;
                //endregion
            case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                //region Yellow Cards
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.yellow_cards.max, (float) tcd.yellow_cards.min, (float) tcd.yellow_cards.average, (float) tcd.yellow_cards.std));
                    }
                }
                dataset = new LLD_DataSet(entries, "");
                data = new LLD_Data(mCurrentTeamNumbers, dataset);
                mLLDChart.setData(data);

                mXAxis = mLLDChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                mYAxis = mLLDChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mLLDChart.notifyDataSetChanged();
                mLLDChart.invalidate();
                break;
                //endregion
            case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                //region Red Cards
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.red_cards.max, (float) tcd.red_cards.min, (float) tcd.red_cards.average, (float) tcd.red_cards.std));
                    }
                }
                dataset = new LLD_DataSet(entries, "");
                data = new LLD_Data(mCurrentTeamNumbers, dataset);
                mLLDChart.setData(data);

                mXAxis = mLLDChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                //mXAxis.setLabelCount(mCurrentTeamNumbers.size(), true);
                mYAxis = mLLDChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mLLDChart.notifyDataSetChanged();
                mLLDChart.invalidate();
                break;
                //endregion
        }
    }

    private void postMatchSecondary(int position) {
        mLLDChart.setVisibility(View.GONE);
        mBarChart.setVisibility(View.VISIBLE);

        List<BarEntry> entries;
        BarDataSet dataset;
        BarData data;

        switch (Constants.Event_View.Post_Match_Secondary_Options.OPTIONS[position])
        {
            case Constants.Event_View.Post_Match_Secondary_Options.DQ:
                //region DQ
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new BarEntryWithTeamNumber(i, tcd.team_number, (float)tcd.dq.total));
                    }
                }
                dataset = new BarDataSet(entries, "");
                dataset.setDrawValues(false);
                data = new BarData(mCurrentTeamNumbers, dataset);
                mBarChart.setData(data);

                mXAxis = mBarChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                mYAxis = mBarChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mBarChart.notifyDataSetChanged();
                mBarChart.invalidate();
                //endregion
                break;
            case Constants.Event_View.Post_Match_Secondary_Options.NO_SHOW:
                //region No Show
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new BarEntryWithTeamNumber(i, tcd.team_number, (float)tcd.no_show.total));
                    }
                }
                dataset = new BarDataSet(entries, "");
                dataset.setDrawValues(false);
                data = new BarData(mCurrentTeamNumbers, dataset);
                mBarChart.setData(data);

                mXAxis = mBarChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                mYAxis = mBarChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mBarChart.notifyDataSetChanged();
                mBarChart.invalidate();
                //endregion
                break;
            case Constants.Event_View.Post_Match_Secondary_Options.STOPPED_MOVING:
                //region Stopped Moving
                entries = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                mCurrentTeamNumbers = new ArrayList<>();
                for (int i = 0; i < mTeamNumbers.size(); i++) {
                    TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
                    if (tcd != null) {
                        mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                        entries.add(new BarEntryWithTeamNumber(i, tcd.team_number, (float)tcd.stopped_moving.total));
                    }
                }
                dataset = new BarDataSet(entries, "");
                dataset.setDrawValues(false);
                data = new BarData(mCurrentTeamNumbers, dataset);
                mBarChart.setData(data);

                mXAxis = mBarChart.getXAxis();
                mXAxis.setLabelsToSkip(0);
                mYAxis = mBarChart.getAxisLeft();
                mYAxis.setAxisMinValue(0.0f);

                mBarChart.notifyDataSetChanged();
                mBarChart.invalidate();
                break;
                //endregion
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
