package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_EventViewDrawer;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.TeamNumberCheck;
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
 * @author frc3824
 * Created: 8/23/16
 *
 * An activity for comparing all the teams at an event
 */
public class EventView extends Activity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private final static String TAG = "EventView";

    private ListView mDrawerList;
    private LVA_EventViewDrawer mLVA;

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
    private ArrayList<TeamNumberCheck> mTeamNumbersSelect;
    private ArrayList<String> mCurrentTeamNumbers;
    private ArrayList<Integer> mSortedTeamNumbers;

    private final int ALL = 0;
    private final int TOP_5 = 1;
    private final int TOP_10 = 2;
    private final int TOP_24 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        //region Dropdown Setup
        mMainDropdown = (Spinner) findViewById(R.id.main_dropdown);
        mMainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                Constants.Event_View.Main_Dropdown_Options.OPTIONS);
        mMainDropdown.setAdapter(mMainAdapter);
        mMainDropdown.setOnItemSelectedListener(this);

        mSecondaryDropdown = (Spinner) findViewById(R.id.secondary_dropdown);
        mSecondaryDropdown.setOnItemSelectedListener(this);
        //endregion

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

        //region Drawer Setup
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mTeamNumbersSelect = new ArrayList<>();
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // All
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // Top 5
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // Top 10
        mTeamNumbersSelect.add(new TeamNumberCheck(-1)); // Top 24
        for (Integer team_number : mTeamNumbers) {
            mTeamNumbersSelect.add(new TeamNumberCheck(team_number, true));
        }
        mLVA = new LVA_EventViewDrawer(this, mTeamNumbersSelect, this);
        mDrawerList.setAdapter(mLVA);
        mDrawerList.setOnItemClickListener(this);
        //endregion
    }

    /**
     * For the dropdowns (spinners)
     *
     * @param parent
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch (parent.getId()) {
            case R.id.main_dropdown:
                switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[position]) {
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
                updateChart();
                break;
        }
    }

    public void updateChart() {
        int main_dropdown_position = mMainDropdown.getSelectedItemPosition();
        int secondary_dropdown_position = mSecondaryDropdown.getSelectedItemPosition();

        switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_dropdown_position]) {
            case Constants.Event_View.Main_Dropdown_Options.FOULS:
                foulSecondary(secondary_dropdown_position);
                break;
            case Constants.Event_View.Main_Dropdown_Options.POST_MATCH:
                postMatchSecondary(secondary_dropdown_position);

                // Add GAME SPECIFIC Main Dropdown Options
        }
    }

    private void foulSecondary(int position) {
        mLLDChart.setVisibility(View.VISIBLE);
        mBarChart.setVisibility(View.GONE);

        List<LLD_Entry> entries;
        LLD_DataSet dataset;
        LLD_Data data;
        entries = new ArrayList<>();
        mCurrentTeamNumbers = new ArrayList<>();
        for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
            if(!mTeamNumbersSelect.get(i).check){
                continue;
            }
            TCD tcd = mDatabase.getTCD(mTeamNumbersSelect.get(i).team_number);
            if (tcd != null) {
                mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position]) {
                    case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                        //region Fouls
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.fouls.max, (float) tcd.fouls.min, (float) tcd.fouls.average, (float) tcd.fouls.std));
                        break;
                        //endregion
                    case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                        //region Tech Fouls
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.tech_fouls.max, (float) tcd.tech_fouls.min, (float) tcd.tech_fouls.average, (float) tcd.tech_fouls.std));
                        break;
                        //endregion
                    case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                        //region Yellow Cards
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.yellow_cards.max, (float) tcd.yellow_cards.min, (float) tcd.yellow_cards.average, (float) tcd.yellow_cards.std));
                        break;
                        //endregion
                    case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                        //region Red Cards
                        entries.add(new LLD_Entry(i, tcd.team_number, (float) tcd.red_cards.max, (float) tcd.red_cards.min, (float) tcd.red_cards.average, (float) tcd.red_cards.std));
                        break;
                        //endregion
                }
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

        mSortedTeamNumbers = new ArrayList<>();
        final Map<Integer, Double> sort_values = new HashMap<>();
        for (int i = 0; i < mTeamNumbers.size(); i++) {
            TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
            if (tcd != null) {
                switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position]) {
                    case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                        //region Fouls
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.fouls.total);
                        break;
                    //endregion
                    case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                        //region Tech Fouls
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.tech_fouls.total);
                        break;
                    //endregion
                    case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                        //region Yellow Cards
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.yellow_cards.total);
                        break;
                    //endregion
                    case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                        //region Red Cards
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.red_cards.total);
                        break;
                    //endregion
                }
            }
        }
        Collections.sort(mSortedTeamNumbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return Double.compare(sort_values.get(i1), sort_values.get(i2));
            }
        });
    }

    private void postMatchSecondary(int position) {
        mLLDChart.setVisibility(View.GONE);
        mBarChart.setVisibility(View.VISIBLE);

        List<BarEntry> entries;
        BarDataSet dataset;
        BarData data;
        entries = new ArrayList<>();
        mCurrentTeamNumbers = new ArrayList<>();
        for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
            if(!mTeamNumbersSelect.get(i).check){
                continue;
            }
            TCD tcd = mDatabase.getTCD(mTeamNumbersSelect.get(i).team_number);
            if (tcd != null) {
                mCurrentTeamNumbers.add(String.valueOf(tcd.team_number));
                switch (Constants.Event_View.Post_Match_Secondary_Options.OPTIONS[position]) {
                    case Constants.Event_View.Post_Match_Secondary_Options.DQ:
                        //region DQ
                        entries.add(new BarEntryWithTeamNumber(i, tcd.team_number, (float) tcd.dq.total));
                        break;
                        //endregion
                    case Constants.Event_View.Post_Match_Secondary_Options.NO_SHOW:
                        //region No Show
                        entries.add(new BarEntryWithTeamNumber(i, tcd.team_number, (float) tcd.no_show.total));
                        break;
                        //endregion
                    case Constants.Event_View.Post_Match_Secondary_Options.STOPPED_MOVING:
                        //region Stopped Moving
                        entries.add(new BarEntryWithTeamNumber(i, tcd.team_number, (float) tcd.stopped_moving.total));
                        break;
                        //endregion
                }
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

        mSortedTeamNumbers = new ArrayList<>();
        final Map<Integer, Double> sort_values = new HashMap<>();
        for(int i = 4; i < mTeamNumbers.size(); i++){
            TCD tcd = mDatabase.getTCD(mTeamNumbers.get(i));
            if (tcd != null) {
                switch (Constants.Event_View.Post_Match_Secondary_Options.OPTIONS[position]) {
                    case Constants.Event_View.Post_Match_Secondary_Options.DQ:
                        //region DQ
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.dq.total);
                        break;
                    //endregion
                    case Constants.Event_View.Post_Match_Secondary_Options.NO_SHOW:
                        //region No Show
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.no_show.total);
                        break;
                    //endregion
                    case Constants.Event_View.Post_Match_Secondary_Options.STOPPED_MOVING:
                        //region Stopped Moving
                        mSortedTeamNumbers.add(tcd.team_number);
                        sort_values.put(tcd.team_number, tcd.stopped_moving.total);
                        break;
                    //endregion
                }
            }
        }

        Collections.sort(mSortedTeamNumbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return Double.compare(sort_values.get(i1), sort_values.get(i2));
            }
        });
    }

    public ArrayList<Integer> getTop(int top){
        return new ArrayList<>(mSortedTeamNumbers.subList(0, top));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * For the nav drawer
     *
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        boolean new_check = !mTeamNumbersSelect.get(position).check;
        ArrayList<Integer> top;
        switch (position){
            case ALL:
                for(int i = 0; i < 4; i++){
                    mTeamNumbersSelect.get(i).check = false;
                }
                mTeamNumbersSelect.get(position).check = new_check;
                if(new_check) {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
                        mTeamNumbersSelect.get(i).check = true;
                    }
                }
                mLVA.setEnabled(!new_check);
                mLVA.notifyDataSetChanged();
                updateChart();
                break;
            case TOP_5:
                top = getTop(5);
                for(int i = 0; i < 4; i++){
                    mTeamNumbersSelect.get(i).check = false;
                }
                mTeamNumbersSelect.get(position).check = new_check;
                if(new_check) {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
                        mTeamNumbersSelect.get(i).check = false;
                        for (int j = 0; j < top.size(); j++) {
                            if (mTeamNumbersSelect.get(i).team_number == top.get(j)) {
                                mTeamNumbersSelect.get(i).check = true;
                                break;
                            }
                        }
                    }
                }
                mLVA.setEnabled(!new_check);
                mLVA.notifyDataSetChanged();
                updateChart();
                break;
            case TOP_10:
                top = getTop(10);
                for(int i = 0; i < 4; i++){
                    mTeamNumbersSelect.get(i).check = false;
                }
                mTeamNumbersSelect.get(position).check = new_check;
                if(new_check) {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
                        mTeamNumbersSelect.get(i).check = false;
                        for (int j = 0; j < top.size(); j++) {
                            if (mTeamNumbersSelect.get(i).team_number == top.get(j)) {
                                mTeamNumbersSelect.get(i).check = true;
                                break;
                            }
                        }
                    }
                }
                mLVA.setEnabled(!new_check);
                mLVA.notifyDataSetChanged();
                updateChart();
                break;
            case TOP_24:
                top = getTop(24);
                for(int i = 0; i < 4; i++){
                    mTeamNumbersSelect.get(i).check = false;
                }
                mTeamNumbersSelect.get(position).check = new_check;
                if(new_check) {
                    for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
                        mTeamNumbersSelect.get(i).check = false;
                        for (int j = 0; j < top.size(); j++) {
                            if (mTeamNumbersSelect.get(i).team_number == top.get(j)) {
                                mTeamNumbersSelect.get(i).check = true;
                                break;
                            }
                        }
                    }
                }
                mLVA.setEnabled(!new_check);
                mLVA.notifyDataSetChanged();
                updateChart();
                break;
            default:
                if(mLVA.getEnabled()) {
                    mTeamNumbersSelect.get(position).check = new_check;
                    mLVA.notifyDataSetChanged();
                    updateChart();
                }
        }
    }
}
