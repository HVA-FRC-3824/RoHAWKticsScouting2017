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
import frc3824.rohawkticsscouting2017.Firebase.DataModels.LowLevelStats;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamCalculatedData;
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
                    case Constants.Event_View.Main_Dropdown_Options.POINTS:
                        mSecondaryAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                Constants.Event_View.Points_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);
                        break;
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
                        break;
                    // GAME SPECIFIC Main Dropdown Options
                    case Constants.Event_View.Main_Dropdown_Options.GEARS:
                        mSecondaryAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                Constants.Event_View.Gears_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.SHOOTING:
                        mSecondaryAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                Constants.Event_View.Shooting_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.CLIMB:
                        mSecondaryAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                Constants.Event_View.Climb_Secondary_Options.OPTIONS);
                        mSecondaryDropdown.setAdapter(mSecondaryAdapter);
                        break;
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

        if(Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_dropdown_position] == Constants.Event_View.Main_Dropdown_Options.POST_MATCH){
            barChartSetup(main_dropdown_position, secondary_dropdown_position);

        } else if(Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_dropdown_position] == Constants.Event_View.Main_Dropdown_Options.SHOOTING){
            if(secondary_dropdown_position % 2 == 1){
                barChartSetup(main_dropdown_position, secondary_dropdown_position);
            } else {
                lldChartSetup(main_dropdown_position, secondary_dropdown_position);
            }
        } else if(Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_dropdown_position] == Constants.Event_View.Main_Dropdown_Options.CLIMB) {
            if(Constants.Event_View.Climb_Secondary_Options.OPTIONS[secondary_dropdown_position] == Constants.Event_View.Climb_Secondary_Options.SUCCESSFUL_ATTEMPTS){
                barChartSetup(main_dropdown_position, secondary_dropdown_position);
            } else {
                lldChartSetup(main_dropdown_position, secondary_dropdown_position);
            }
        }else {
            lldChartSetup(main_dropdown_position, secondary_dropdown_position);
        }
    }

    private void barChartSetup(int main_position, int secondary_position){
        mLLDChart.setVisibility(View.GONE);
        mBarChart.setVisibility(View.VISIBLE);

        ArrayList<BarEntry> entries;
        BarDataSet dataset;
        BarData data;
        entries = new ArrayList<>();
        mCurrentTeamNumbers = new ArrayList<>();
        int x = 0;
        for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
            if(!mTeamNumbersSelect.get(i).check){
                continue;
            }
            TeamCalculatedData teamCalculatedData = mDatabase.getTeamCalculatedData(mTeamNumbersSelect.get(i).team_number);
            if (teamCalculatedData != null) {
                mCurrentTeamNumbers.add(String.valueOf(teamCalculatedData.team_number));
                switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_position]){
                    case Constants.Event_View.Main_Dropdown_Options.SHOOTING:
                        shootingSecondary1_bar(entries, x, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.POST_MATCH:
                        postMatchSecondary1(entries, x, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.CLIMB:
                        climbSecondary1_bar(entries, x, teamCalculatedData, secondary_position);
                        break;
                }
            }
            x++;
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
            TeamCalculatedData teamCalculatedData = mDatabase.getTeamCalculatedData(mTeamNumbers.get(i));
            if (teamCalculatedData != null) {
                switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_position]){
                    case Constants.Event_View.Main_Dropdown_Options.SHOOTING:
                        shootingSecondary2_bar(sort_values, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.POST_MATCH:
                        postMatchSecondary2(sort_values, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.CLIMB:
                        climbSecondary2(sort_values, teamCalculatedData, secondary_position);
                        break;
                }
            }
        }

        Collections.sort(mSortedTeamNumbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return -Double.compare(sort_values.get(i1), sort_values.get(i2));
            }
        });
    }

    private void lldChartSetup(int main_position, int secondary_position){
        mLLDChart.setVisibility(View.VISIBLE);
        mBarChart.setVisibility(View.GONE);

        ArrayList<LLD_Entry> entries;
        LLD_DataSet dataset;
        LLD_Data data;
        entries = new ArrayList<>();
        mCurrentTeamNumbers = new ArrayList<>();
        int x = 0;
        for (int i = 4; i < mTeamNumbersSelect.size(); i++) {
            if(!mTeamNumbersSelect.get(i).check){
                continue;
            }
            TeamCalculatedData teamCalculatedData = mDatabase.getTeamCalculatedData(mTeamNumbersSelect.get(i).team_number);
            if (teamCalculatedData != null) {
                mCurrentTeamNumbers.add(String.valueOf(teamCalculatedData.team_number));
                switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_position]){
                    case Constants.Event_View.Main_Dropdown_Options.POINTS:
                        pointsSecondary1(entries, x, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.GEARS:
                        gearsSecondary1(entries, x, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.SHOOTING:
                        shootingSecondary1_lld(entries, x, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.CLIMB:
                        climbSecondary1_lld(entries, x, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.FOULS:
                        foulSecondary1(entries, x, teamCalculatedData, secondary_position);
                        break;
                }
            }
            x++;
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
            TeamCalculatedData teamCalculatedData = mDatabase.getTeamCalculatedData(mTeamNumbers.get(i));
            if (teamCalculatedData != null) {
                switch (Constants.Event_View.Main_Dropdown_Options.OPTIONS[main_position]){
                    case Constants.Event_View.Main_Dropdown_Options.POINTS:
                        pointsSecondary2(sort_values, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.GEARS:
                        gearsSecondary2(sort_values, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.SHOOTING:
                        shootingSecondary2_lld(sort_values, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.CLIMB:
                        climbSecondary2(sort_values, teamCalculatedData, secondary_position);
                        break;
                    case Constants.Event_View.Main_Dropdown_Options.FOULS:
                        foulSecondary2(sort_values, teamCalculatedData, secondary_position);
                        break;
                }
            }
        }
        Collections.sort(mSortedTeamNumbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return -Double.compare(sort_values.get(i1), sort_values.get(i2));
            }
        });

        int breakpoint = 0;
    }

    //region Points
    private void pointsSecondary1(ArrayList<LLD_Entry> entries, int i, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Points_Secondary_Options.OPTIONS[position]){
            //region Total
            case Constants.Event_View.Points_Secondary_Options.TOTAL:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.total_points.max, (float) teamCalculatedData.total_points.min, (float) teamCalculatedData.total_points.average, (float) teamCalculatedData.total_points.std));
                break;
            //endregion
            //region Auto
            case Constants.Event_View.Points_Secondary_Options.AUTO:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.auto_points.max, (float) teamCalculatedData.auto_points.min, (float) teamCalculatedData.auto_points.average, (float) teamCalculatedData.auto_points.std));
                break;
            //endregion
            //region Teleop
            case Constants.Event_View.Points_Secondary_Options.TELEOP:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.teleop_points.max, (float) teamCalculatedData.teleop_points.min, (float) teamCalculatedData.teleop_points.average, (float) teamCalculatedData.teleop_points.std));
                break;
            //endregion
            //region Endgame
            case Constants.Event_View.Points_Secondary_Options.ENDGAME:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.endgame_points.max, (float) teamCalculatedData.endgame_points.min, (float) teamCalculatedData.endgame_points.average, (float) teamCalculatedData.endgame_points.std));
                break;
            //endregion
        }
    }

    private void pointsSecondary2(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Points_Secondary_Options.OPTIONS[position]){
            //region Total
            case Constants.Event_View.Points_Secondary_Options.TOTAL:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.total_points.total);
                break;

            //endregion
            //region Auto
            case Constants.Event_View.Points_Secondary_Options.AUTO:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.auto_points.total);
                break;
            //endregion
            //region Teleop
            case Constants.Event_View.Points_Secondary_Options.TELEOP:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.teleop_points.total);
                break;
            //endregion
            //region Endgame
            case Constants.Event_View.Points_Secondary_Options.ENDGAME:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.endgame_points.total);
                break;
            //endregion
        }
    }
    //endregion

    //region Foul
    private void foulSecondary1(ArrayList<LLD_Entry> entries, int i, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position]) {
            //region Fouls
            case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.fouls.max, (float) teamCalculatedData.fouls.min, (float) teamCalculatedData.fouls.average, (float) teamCalculatedData.fouls.std));
                break;
            //endregion
            //region Tech Fouls
            case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.tech_fouls.max, (float) teamCalculatedData.tech_fouls.min, (float) teamCalculatedData.tech_fouls.average, (float) teamCalculatedData.tech_fouls.std));
                break;
            //endregion
            //region Yellow Cards
            case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.yellow_cards.max, (float) teamCalculatedData.yellow_cards.min, (float) teamCalculatedData.yellow_cards.average, (float) teamCalculatedData.yellow_cards.std));
                break;
            //endregion
            //region Red Cards
            case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.red_cards.max, (float) teamCalculatedData.red_cards.min, (float) teamCalculatedData.red_cards.average, (float) teamCalculatedData.red_cards.std));
                break;
            //endregion
        }
    }

    private void foulSecondary2(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Foul_Secondary_Options.OPTIONS[position]) {
            //region Fouls
            case Constants.Event_View.Foul_Secondary_Options.STANDARD_FOULS:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.fouls.total);
                break;
            //endregion
            //region Tech Fouls
            case Constants.Event_View.Foul_Secondary_Options.TECH_FOULS:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.tech_fouls.total);
                break;
            //endregion
            //region Yellow Cards
            case Constants.Event_View.Foul_Secondary_Options.YELLOW_CARDS:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.yellow_cards.total);
                break;
            //endregion
            //region Red Cards
            case Constants.Event_View.Foul_Secondary_Options.RED_CARDS:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.red_cards.total);
                break;
            //endregion
        }
    }
    //endregion

    //region Post Match
    private void postMatchSecondary1(ArrayList<BarEntry> entries, int i, TeamCalculatedData teamCalculatedData, int position) {
        switch (Constants.Event_View.Post_Match_Secondary_Options.OPTIONS[position]) {
            //region DQ
            case Constants.Event_View.Post_Match_Secondary_Options.DQ:
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, (float) teamCalculatedData.dq.total));
                break;
            //endregion
            //region No Show
            case Constants.Event_View.Post_Match_Secondary_Options.NO_SHOW:
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, (float) teamCalculatedData.no_show.total));
                break;
            //endregion
            //region Stopped Moving
            case Constants.Event_View.Post_Match_Secondary_Options.STOPPED_MOVING:
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, (float) teamCalculatedData.stopped_moving.total));
                break;
            //endregion
        }
    }

    private void postMatchSecondary2(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position) {
        switch (Constants.Event_View.Post_Match_Secondary_Options.OPTIONS[position]) {
            //region DQ
            case Constants.Event_View.Post_Match_Secondary_Options.DQ:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, -teamCalculatedData.dq.total);
                break;
            //endregion
            //region No Show
            case Constants.Event_View.Post_Match_Secondary_Options.NO_SHOW:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, -teamCalculatedData.no_show.total);
                break;
            //endregion
            //region Stopped Moving
            case Constants.Event_View.Post_Match_Secondary_Options.STOPPED_MOVING:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, -teamCalculatedData.stopped_moving.total);
                break;
            //endregion
        }
    }
    //endregion

    //region Gears
    private void gearsSecondary1(ArrayList<LLD_Entry> entries, int i, TeamCalculatedData teamCalculatedData, int position){
        LowLevelStats lls = new LowLevelStats();
        switch (Constants.Event_View.Gears_Secondary_Options.OPTIONS[position]) {
            //region Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_PLACED:
                //region Both Placed
                lls.max = teamCalculatedData.auto_total_gears_placed.max + teamCalculatedData.teleop_total_gears_placed.max;
                lls.min = teamCalculatedData.auto_total_gears_placed.min + teamCalculatedData.teleop_total_gears_placed.min;
                lls.total = teamCalculatedData.auto_total_gears_placed.total + teamCalculatedData.teleop_total_gears_placed.total;
                lls.average = teamCalculatedData.auto_total_gears_placed.average + teamCalculatedData.teleop_total_gears_placed.average;
                lls.std = teamCalculatedData.auto_total_gears_placed.std + teamCalculatedData.teleop_total_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_PLACED:
                //region Auto Placed
                lls.max = teamCalculatedData.auto_total_gears_placed.max;
                lls.min = teamCalculatedData.auto_total_gears_placed.min;
                lls.total = teamCalculatedData.auto_total_gears_placed.total;
                lls.average = teamCalculatedData.auto_total_gears_placed.average;
                lls.std = teamCalculatedData.auto_total_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_PLACED:
                //region Teleop Placed
                lls.max = teamCalculatedData.teleop_total_gears_placed.max;
                lls.min = teamCalculatedData.teleop_total_gears_placed.min;
                lls.total = teamCalculatedData.teleop_total_gears_placed.total;
                lls.average = teamCalculatedData.teleop_total_gears_placed.average;
                lls.std = teamCalculatedData.teleop_total_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_DROPPED:
                //region Both Dropped
                lls.max = teamCalculatedData.auto_total_gears_dropped.max + teamCalculatedData.teleop_total_gears_dropped.max;
                lls.min = teamCalculatedData.auto_total_gears_dropped.min + teamCalculatedData.teleop_total_gears_dropped.min;
                lls.total = teamCalculatedData.auto_total_gears_dropped.total + teamCalculatedData.teleop_total_gears_dropped.total;
                lls.average = teamCalculatedData.auto_total_gears_dropped.average + teamCalculatedData.teleop_total_gears_dropped.average;
                lls.std = teamCalculatedData.auto_total_gears_dropped.std + teamCalculatedData.teleop_total_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_DROPPED:
                //region Auto Dropped
                lls.max = teamCalculatedData.auto_total_gears_dropped.max;
                lls.min = teamCalculatedData.auto_total_gears_dropped.min;
                lls.total = teamCalculatedData.auto_total_gears_dropped.total;
                lls.average = teamCalculatedData.auto_total_gears_dropped.average;
                lls.std = teamCalculatedData.auto_total_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_DROPPED:
                //region Teleop Dropped
                lls.max = teamCalculatedData.teleop_total_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_total_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_total_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_total_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_total_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Near Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_NEAR_PLACED:
                //region Both Near Placed
                lls.max = teamCalculatedData.auto_near_gears_placed.max + teamCalculatedData.teleop_near_gears_placed.max;
                lls.min = teamCalculatedData.auto_near_gears_placed.min + teamCalculatedData.teleop_near_gears_placed.min;
                lls.total = teamCalculatedData.auto_near_gears_placed.total + teamCalculatedData.teleop_near_gears_placed.total;
                lls.average = teamCalculatedData.auto_near_gears_placed.average + teamCalculatedData.teleop_near_gears_placed.average;
                lls.std = teamCalculatedData.auto_near_gears_placed.std + teamCalculatedData.teleop_near_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_NEAR_PLACED:
                //region Auto Near Placed
                lls.max = teamCalculatedData.auto_near_gears_placed.max;
                lls.min = teamCalculatedData.auto_near_gears_placed.min;
                lls.total = teamCalculatedData.auto_near_gears_placed.total;
                lls.average = teamCalculatedData.auto_near_gears_placed.average;
                lls.std = teamCalculatedData.auto_near_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_NEAR_PLACED:
                //region Teleop Near Placed
                lls.max = teamCalculatedData.teleop_near_gears_placed.max;
                lls.min = teamCalculatedData.teleop_near_gears_placed.min;
                lls.total = teamCalculatedData.teleop_near_gears_placed.total;
                lls.average = teamCalculatedData.teleop_near_gears_placed.average;
                lls.std = teamCalculatedData.teleop_near_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Near Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_NEAR_DROPPED:
                //region Both Near Dropped
                lls.max = teamCalculatedData.auto_near_gears_dropped.max + teamCalculatedData.teleop_near_gears_dropped.max;
                lls.min = teamCalculatedData.auto_near_gears_dropped.min + teamCalculatedData.teleop_near_gears_dropped.min;
                lls.total = teamCalculatedData.auto_near_gears_dropped.total + teamCalculatedData.teleop_near_gears_dropped.total;
                lls.average = teamCalculatedData.auto_near_gears_dropped.average + teamCalculatedData.teleop_near_gears_dropped.average;
                lls.std = teamCalculatedData.auto_near_gears_dropped.std + teamCalculatedData.teleop_near_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_NEAR_DROPPED:
                //region Auto Near Dropped
                lls.max = teamCalculatedData.auto_near_gears_dropped.max;
                lls.min = teamCalculatedData.auto_near_gears_dropped.min;
                lls.total = teamCalculatedData.auto_near_gears_dropped.total;
                lls.average = teamCalculatedData.auto_near_gears_dropped.average;
                lls.std = teamCalculatedData.auto_near_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_NEAR_DROPPED:
                //region Teleop Near Dropped
                lls.max = teamCalculatedData.teleop_near_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_near_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_near_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_near_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_near_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Center Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_CENTER_PLACED:
                //region Both Center Placed
                lls.max = teamCalculatedData.auto_center_gears_placed.max + teamCalculatedData.teleop_center_gears_placed.max;
                lls.min = teamCalculatedData.auto_center_gears_placed.min + teamCalculatedData.teleop_center_gears_placed.min;
                lls.total = teamCalculatedData.auto_center_gears_placed.total + teamCalculatedData.teleop_center_gears_placed.total;
                lls.average = teamCalculatedData.auto_center_gears_placed.average + teamCalculatedData.teleop_center_gears_placed.average;
                lls.std = teamCalculatedData.auto_center_gears_placed.std + teamCalculatedData.teleop_center_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_CENTER_PLACED:
                //region Auto Center Placed
                lls.max = teamCalculatedData.auto_center_gears_placed.max;
                lls.min = teamCalculatedData.auto_center_gears_placed.min;
                lls.total = teamCalculatedData.auto_center_gears_placed.total;
                lls.average = teamCalculatedData.auto_center_gears_placed.average;
                lls.std = teamCalculatedData.auto_center_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_CENTER_PLACED:
                //region Teleop Center Placed
                lls.max = teamCalculatedData.teleop_center_gears_placed.max;
                lls.min = teamCalculatedData.teleop_center_gears_placed.min;
                lls.total = teamCalculatedData.teleop_center_gears_placed.total;
                lls.average = teamCalculatedData.teleop_center_gears_placed.average;
                lls.std = teamCalculatedData.teleop_center_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Center Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_CENTER_DROPPED:
                //region Both Center Dropped
                lls.max = teamCalculatedData.auto_center_gears_dropped.max + teamCalculatedData.teleop_center_gears_dropped.max;
                lls.min = teamCalculatedData.auto_center_gears_dropped.min + teamCalculatedData.teleop_center_gears_dropped.min;
                lls.total = teamCalculatedData.auto_center_gears_dropped.total + teamCalculatedData.teleop_center_gears_dropped.total;
                lls.average = teamCalculatedData.auto_center_gears_dropped.average + teamCalculatedData.teleop_center_gears_dropped.average;
                lls.std = teamCalculatedData.auto_center_gears_dropped.std + teamCalculatedData.teleop_center_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_CENTER_DROPPED:
                //region Auto Center Dropped
                lls.max = teamCalculatedData.auto_center_gears_dropped.max;
                lls.min = teamCalculatedData.auto_center_gears_dropped.min;
                lls.total = teamCalculatedData.auto_center_gears_dropped.total;
                lls.average = teamCalculatedData.auto_center_gears_dropped.average;
                lls.std = teamCalculatedData.auto_center_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_CENTER_DROPPED:
                //region Teleop Center Dropped
                lls.max = teamCalculatedData.teleop_center_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_center_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_center_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_center_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_center_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Far Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_FAR_PLACED:
                //region Both Far Placed
                lls.max = teamCalculatedData.auto_far_gears_placed.max + teamCalculatedData.teleop_far_gears_placed.max;
                lls.min = teamCalculatedData.auto_far_gears_placed.min + teamCalculatedData.teleop_far_gears_placed.min;
                lls.total = teamCalculatedData.auto_far_gears_placed.total + teamCalculatedData.teleop_far_gears_placed.total;
                lls.average = teamCalculatedData.auto_far_gears_placed.average + teamCalculatedData.teleop_far_gears_placed.average;
                lls.std = teamCalculatedData.auto_far_gears_placed.std + teamCalculatedData.teleop_far_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_FAR_PLACED:
                //region Auto Far Placed
                lls.max = teamCalculatedData.auto_far_gears_placed.max;
                lls.min = teamCalculatedData.auto_far_gears_placed.min;
                lls.total = teamCalculatedData.auto_far_gears_placed.total;
                lls.average = teamCalculatedData.auto_far_gears_placed.average;
                lls.std = teamCalculatedData.auto_far_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_FAR_PLACED:
                //region Teleop Far Placed
                lls.max = teamCalculatedData.teleop_far_gears_placed.max;
                lls.min = teamCalculatedData.teleop_far_gears_placed.min;
                lls.total = teamCalculatedData.teleop_far_gears_placed.total;
                lls.average = teamCalculatedData.teleop_far_gears_placed.average;
                lls.std = teamCalculatedData.teleop_far_gears_placed.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
            //region Far Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_FAR_DROPPED:
                //region Both Far Dropped
                lls.max = teamCalculatedData.auto_far_gears_dropped.max + teamCalculatedData.teleop_far_gears_dropped.max;
                lls.min = teamCalculatedData.auto_far_gears_dropped.min + teamCalculatedData.teleop_far_gears_dropped.min;
                lls.total = teamCalculatedData.auto_far_gears_dropped.total + teamCalculatedData.teleop_far_gears_dropped.total;
                lls.average = teamCalculatedData.auto_far_gears_dropped.average + teamCalculatedData.teleop_far_gears_dropped.average;
                lls.std = teamCalculatedData.auto_far_gears_dropped.std + teamCalculatedData.teleop_far_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_FAR_DROPPED:
                //region Auto Far Dropped
                lls.max = teamCalculatedData.auto_far_gears_dropped.max;
                lls.min = teamCalculatedData.auto_far_gears_dropped.min;
                lls.total = teamCalculatedData.auto_far_gears_dropped.total;
                lls.average = teamCalculatedData.auto_far_gears_dropped.average;
                lls.std = teamCalculatedData.auto_far_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_FAR_DROPPED:
                //region Teleop Far Dropped
                lls.max = teamCalculatedData.teleop_far_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_far_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_far_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_far_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_far_gears_dropped.std;
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) lls.max, (float) lls.min, (float) lls.average, (float) lls.std));
                break;
            //endregion
            //endregion
        }
    }

    private void gearsSecondary2(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position){
        LowLevelStats lls = new LowLevelStats();
        switch (Constants.Event_View.Gears_Secondary_Options.OPTIONS[position]){
            //region Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_PLACED:
                //region Both Placed
                lls.max = teamCalculatedData.auto_total_gears_placed.max + teamCalculatedData.teleop_total_gears_placed.max;
                lls.min = teamCalculatedData.auto_total_gears_placed.min + teamCalculatedData.teleop_total_gears_placed.min;
                lls.total = teamCalculatedData.auto_total_gears_placed.total + teamCalculatedData.teleop_total_gears_placed.total;
                lls.average = teamCalculatedData.auto_total_gears_placed.average + teamCalculatedData.teleop_total_gears_placed.average;
                lls.std = teamCalculatedData.auto_total_gears_placed.std + teamCalculatedData.teleop_total_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_PLACED:
                //region Auto Placed
                lls.max = teamCalculatedData.auto_total_gears_placed.max;
                lls.min = teamCalculatedData.auto_total_gears_placed.min;
                lls.total = teamCalculatedData.auto_total_gears_placed.total;
                lls.average = teamCalculatedData.auto_total_gears_placed.average;
                lls.std = teamCalculatedData.auto_total_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_PLACED:
                //region Teleop Placed
                lls.max = teamCalculatedData.teleop_total_gears_placed.max;
                lls.min = teamCalculatedData.teleop_total_gears_placed.min;
                lls.total = teamCalculatedData.teleop_total_gears_placed.total;
                lls.average = teamCalculatedData.teleop_total_gears_placed.average;
                lls.std = teamCalculatedData.teleop_total_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_DROPPED:
                //region Both Dropped
                lls.max = teamCalculatedData.auto_total_gears_dropped.max + teamCalculatedData.teleop_total_gears_dropped.max;
                lls.min = teamCalculatedData.auto_total_gears_dropped.min + teamCalculatedData.teleop_total_gears_dropped.min;
                lls.total = teamCalculatedData.auto_total_gears_dropped.total + teamCalculatedData.teleop_total_gears_dropped.total;
                lls.average = teamCalculatedData.auto_total_gears_dropped.average + teamCalculatedData.teleop_total_gears_dropped.average;
                lls.std = teamCalculatedData.auto_total_gears_dropped.std + teamCalculatedData.teleop_total_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_DROPPED:
                //region Auto Dropped
                lls.max = teamCalculatedData.auto_total_gears_dropped.max;
                lls.min = teamCalculatedData.auto_total_gears_dropped.min;
                lls.total = teamCalculatedData.auto_total_gears_dropped.total;
                lls.average = teamCalculatedData.auto_total_gears_dropped.average;
                lls.std = teamCalculatedData.auto_total_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_DROPPED:
                //region Teleop Dropped
                lls.max = teamCalculatedData.teleop_total_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_total_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_total_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_total_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_total_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Near Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_NEAR_PLACED:
                //region Both Near Placed
                lls.max = teamCalculatedData.auto_near_gears_placed.max + teamCalculatedData.teleop_near_gears_placed.max;
                lls.min = teamCalculatedData.auto_near_gears_placed.min + teamCalculatedData.teleop_near_gears_placed.min;
                lls.total = teamCalculatedData.auto_near_gears_placed.total + teamCalculatedData.teleop_near_gears_placed.total;
                lls.average = teamCalculatedData.auto_near_gears_placed.average + teamCalculatedData.teleop_near_gears_placed.average;
                lls.std = teamCalculatedData.auto_near_gears_placed.std + teamCalculatedData.teleop_near_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_NEAR_PLACED:
                //region Auto Near Placed
                lls.max = teamCalculatedData.auto_near_gears_placed.max;
                lls.min = teamCalculatedData.auto_near_gears_placed.min;
                lls.total = teamCalculatedData.auto_near_gears_placed.total;
                lls.average = teamCalculatedData.auto_near_gears_placed.average;
                lls.std = teamCalculatedData.auto_near_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_NEAR_PLACED:
                //region Teleop Near Placed
                lls.max = teamCalculatedData.teleop_near_gears_placed.max;
                lls.min = teamCalculatedData.teleop_near_gears_placed.min;
                lls.total = teamCalculatedData.teleop_near_gears_placed.total;
                lls.average = teamCalculatedData.teleop_near_gears_placed.average;
                lls.std = teamCalculatedData.teleop_near_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Near Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_NEAR_DROPPED:
                //region Both Near Dropped
                lls.max = teamCalculatedData.auto_near_gears_dropped.max + teamCalculatedData.teleop_near_gears_dropped.max;
                lls.min = teamCalculatedData.auto_near_gears_dropped.min + teamCalculatedData.teleop_near_gears_dropped.min;
                lls.total = teamCalculatedData.auto_near_gears_dropped.total + teamCalculatedData.teleop_near_gears_dropped.total;
                lls.average = teamCalculatedData.auto_near_gears_dropped.average + teamCalculatedData.teleop_near_gears_dropped.average;
                lls.std = teamCalculatedData.auto_near_gears_dropped.std + teamCalculatedData.teleop_near_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_NEAR_DROPPED:
                //region Auto Near Dropped
                lls.max = teamCalculatedData.auto_near_gears_dropped.max;
                lls.min = teamCalculatedData.auto_near_gears_dropped.min;
                lls.total = teamCalculatedData.auto_near_gears_dropped.total;
                lls.average = teamCalculatedData.auto_near_gears_dropped.average;
                lls.std = teamCalculatedData.auto_near_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_NEAR_DROPPED:
                //region Teleop Near Dropped
                lls.max = teamCalculatedData.teleop_near_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_near_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_near_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_near_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_near_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Center Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_CENTER_PLACED:
                //region Both Center Placed
                lls.max = teamCalculatedData.auto_center_gears_placed.max + teamCalculatedData.teleop_center_gears_placed.max;
                lls.min = teamCalculatedData.auto_center_gears_placed.min + teamCalculatedData.teleop_center_gears_placed.min;
                lls.total = teamCalculatedData.auto_center_gears_placed.total + teamCalculatedData.teleop_center_gears_placed.total;
                lls.average = teamCalculatedData.auto_center_gears_placed.average + teamCalculatedData.teleop_center_gears_placed.average;
                lls.std = teamCalculatedData.auto_center_gears_placed.std + teamCalculatedData.teleop_center_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_CENTER_PLACED:
                //region Auto Center Placed
                lls.max = teamCalculatedData.auto_center_gears_placed.max;
                lls.min = teamCalculatedData.auto_center_gears_placed.min;
                lls.total = teamCalculatedData.auto_center_gears_placed.total;
                lls.average = teamCalculatedData.auto_center_gears_placed.average;
                lls.std = teamCalculatedData.auto_center_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_CENTER_PLACED:
                //region Teleop Center Placed
                lls.max = teamCalculatedData.teleop_center_gears_placed.max;
                lls.min = teamCalculatedData.teleop_center_gears_placed.min;
                lls.total = teamCalculatedData.teleop_center_gears_placed.total;
                lls.average = teamCalculatedData.teleop_center_gears_placed.average;
                lls.std = teamCalculatedData.teleop_center_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Center Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_CENTER_DROPPED:
                //region Both Center Dropped
                lls.max = teamCalculatedData.auto_center_gears_dropped.max + teamCalculatedData.teleop_center_gears_dropped.max;
                lls.min = teamCalculatedData.auto_center_gears_dropped.min + teamCalculatedData.teleop_center_gears_dropped.min;
                lls.total = teamCalculatedData.auto_center_gears_dropped.total + teamCalculatedData.teleop_center_gears_dropped.total;
                lls.average = teamCalculatedData.auto_center_gears_dropped.average + teamCalculatedData.teleop_center_gears_dropped.average;
                lls.std = teamCalculatedData.auto_center_gears_dropped.std + teamCalculatedData.teleop_center_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_CENTER_DROPPED:
                //region Auto Center Dropped
                lls.max = teamCalculatedData.auto_center_gears_dropped.max;
                lls.min = teamCalculatedData.auto_center_gears_dropped.min;
                lls.total = teamCalculatedData.auto_center_gears_dropped.total;
                lls.average = teamCalculatedData.auto_center_gears_dropped.average;
                lls.std = teamCalculatedData.auto_center_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_CENTER_DROPPED:
                //region Teleop Center Dropped
                lls.max = teamCalculatedData.teleop_center_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_center_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_center_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_center_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_center_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Far Placed
            case Constants.Event_View.Gears_Secondary_Options.BOTH_FAR_PLACED:
                //region Both Far Placed
                lls.max = teamCalculatedData.auto_far_gears_placed.max + teamCalculatedData.teleop_far_gears_placed.max;
                lls.min = teamCalculatedData.auto_far_gears_placed.min + teamCalculatedData.teleop_far_gears_placed.min;
                lls.total = teamCalculatedData.auto_far_gears_placed.total + teamCalculatedData.teleop_far_gears_placed.total;
                lls.average = teamCalculatedData.auto_far_gears_placed.average + teamCalculatedData.teleop_far_gears_placed.average;
                lls.std = teamCalculatedData.auto_far_gears_placed.std + teamCalculatedData.teleop_far_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_FAR_PLACED:
                //region Auto Far Placed
                lls.max = teamCalculatedData.auto_far_gears_placed.max;
                lls.min = teamCalculatedData.auto_far_gears_placed.min;
                lls.total = teamCalculatedData.auto_far_gears_placed.total;
                lls.average = teamCalculatedData.auto_far_gears_placed.average;
                lls.std = teamCalculatedData.auto_far_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_FAR_PLACED:
                //region Teleop Far Placed
                lls.max = teamCalculatedData.teleop_far_gears_placed.max;
                lls.min = teamCalculatedData.teleop_far_gears_placed.min;
                lls.total = teamCalculatedData.teleop_far_gears_placed.total;
                lls.average = teamCalculatedData.teleop_far_gears_placed.average;
                lls.std = teamCalculatedData.teleop_far_gears_placed.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
            //region Far Dropped
            case Constants.Event_View.Gears_Secondary_Options.BOTH_FAR_DROPPED:
                //region Both Far Dropped
                lls.max = teamCalculatedData.auto_far_gears_dropped.max + teamCalculatedData.teleop_far_gears_dropped.max;
                lls.min = teamCalculatedData.auto_far_gears_dropped.min + teamCalculatedData.teleop_far_gears_dropped.min;
                lls.total = teamCalculatedData.auto_far_gears_dropped.total + teamCalculatedData.teleop_far_gears_dropped.total;
                lls.average = teamCalculatedData.auto_far_gears_dropped.average + teamCalculatedData.teleop_far_gears_dropped.average;
                lls.std = teamCalculatedData.auto_far_gears_dropped.std + teamCalculatedData.teleop_far_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.AUTO_FAR_DROPPED:
                //region Auto Far Dropped
                lls.max = teamCalculatedData.auto_far_gears_dropped.max;
                lls.min = teamCalculatedData.auto_far_gears_dropped.min;
                lls.total = teamCalculatedData.auto_far_gears_dropped.total;
                lls.average = teamCalculatedData.auto_far_gears_dropped.average;
                lls.std = teamCalculatedData.auto_far_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            case Constants.Event_View.Gears_Secondary_Options.TELEOP_FAR_DROPPED:
                //region Teleop Far Dropped
                lls.max = teamCalculatedData.teleop_far_gears_dropped.max;
                lls.min = teamCalculatedData.teleop_far_gears_dropped.min;
                lls.total = teamCalculatedData.teleop_far_gears_dropped.total;
                lls.average = teamCalculatedData.teleop_far_gears_dropped.average;
                lls.std = teamCalculatedData.teleop_far_gears_dropped.std;
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, lls.total);
                break;
            //endregion
            //endregion
        }
    }
    //endregion

    //region Shooting
    private void shootingSecondary1_lld(ArrayList<LLD_Entry> entries, int i, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Shooting_Secondary_Options.OPTIONS[position]){
            //region Auto High Made
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_HIGH_MADE:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.auto_high_goal_made.max, (float) teamCalculatedData.auto_high_goal_made.min, (float) teamCalculatedData.auto_high_goal_made.average, (float) teamCalculatedData.auto_high_goal_made.std));
                break;
            //endregion
            //region Auto Low Made
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_LOW_MADE:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.auto_low_goal_made.max, (float) teamCalculatedData.auto_low_goal_made.min, (float) teamCalculatedData.auto_low_goal_made.average, (float) teamCalculatedData.auto_low_goal_made.std));
                break;
            //endregion
            //region Teleop High Made
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_HIGH_MADE:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.teleop_high_goal_made.max, (float) teamCalculatedData.teleop_high_goal_made.min, (float) teamCalculatedData.teleop_high_goal_made.average, (float) teamCalculatedData.teleop_high_goal_made.std));
                break;
            //endregion
            //region Teleop Low Made
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_LOW_MADE:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float) teamCalculatedData.teleop_low_goal_made.max, (float) teamCalculatedData.teleop_low_goal_made.min, (float) teamCalculatedData.teleop_low_goal_made.average, (float) teamCalculatedData.teleop_low_goal_made.std));
                break;
            //endregion
        }
    }

    private void shootingSecondary2_lld(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Shooting_Secondary_Options.OPTIONS[position]){
            //region Auto High Made
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_HIGH_MADE:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.auto_high_goal_made.total);
                break;
            //endregion
            //region Auto Low Made
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_LOW_MADE:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.auto_low_goal_made.total);
                break;
            //endregion
            //region Teleop High Made
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_HIGH_MADE:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.teleop_high_goal_made.total);
                break;
            //endregion
            //region Teleop Low Made
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_LOW_MADE:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.teleop_low_goal_made.total);
                break;
            //endregion
        }
    }

    private void shootingSecondary1_bar(ArrayList<BarEntry> entries, int i, TeamCalculatedData teamCalculatedData, int position){
        float percent;
        switch (Constants.Event_View.Shooting_Secondary_Options.OPTIONS[position]){
            //region Auto High Percent
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_HIGH_PERCENT:
                percent = (float)(teamCalculatedData.auto_high_goal_made.total / (teamCalculatedData.auto_high_goal_made.total + teamCalculatedData.auto_high_goal_missed.total));
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, percent));
                break;
            //endregion
            //region Auto Low Percent
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_LOW_PERCENT:
                percent = (float)(teamCalculatedData.auto_low_goal_made.total / (teamCalculatedData.auto_low_goal_made.total + teamCalculatedData.auto_low_goal_missed.total));
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, percent));
                break;
            //endregion
            //region Teleop High Percent
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_HIGH_PERCENT:
                percent = (float)(teamCalculatedData.teleop_high_goal_made.total / (teamCalculatedData.teleop_high_goal_made.total + teamCalculatedData.teleop_high_goal_missed.total));
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, percent));
                break;
            //endregion
            //region Teleop Low Percent
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_LOW_PERCENT:
                percent = (float)(teamCalculatedData.teleop_low_goal_made.total / (teamCalculatedData.teleop_low_goal_made.total + teamCalculatedData.teleop_low_goal_missed.total));
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, percent));
                break;
            //endregion
        }
    }

    private void shootingSecondary2_bar(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position){
        double percent;
        switch (Constants.Event_View.Shooting_Secondary_Options.OPTIONS[position]){
            //region Auto High Percent
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_HIGH_PERCENT:
                percent = (float)(teamCalculatedData.auto_high_goal_made.total / (teamCalculatedData.auto_high_goal_made.total + teamCalculatedData.auto_high_goal_missed.total));
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, percent);
                break;
            //endregion
            //region Auto Low Percent
            case Constants.Event_View.Shooting_Secondary_Options.AUTO_LOW_PERCENT:
                percent = (float)(teamCalculatedData.auto_low_goal_made.total / (teamCalculatedData.auto_low_goal_made.total + teamCalculatedData.auto_low_goal_missed.total));
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, percent);
                break;
            //endregion
            //region Teleop High Percent
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_HIGH_PERCENT:
                percent = (float)(teamCalculatedData.teleop_high_goal_made.total / (teamCalculatedData.teleop_high_goal_made.total + teamCalculatedData.teleop_high_goal_missed.total));
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, percent);
                break;
            //endregion
            //region Teleop Low Percent
            case Constants.Event_View.Shooting_Secondary_Options.TELEOP_LOW_PERCENT:
                percent = (float)(teamCalculatedData.teleop_low_goal_made.total / (teamCalculatedData.teleop_low_goal_made.total + teamCalculatedData.teleop_low_goal_missed.total));
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, percent);
                break;
            //endregion
        }
    }
    //endregion

    //region Climb
    private void climbSecondary1_bar(ArrayList<BarEntry> entries, int i, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Climb_Secondary_Options.OPTIONS[position]){
            case Constants.Event_View.Climb_Secondary_Options.SUCCESSFUL_ATTEMPTS:
                entries.add(new BarEntryWithTeamNumber(i, teamCalculatedData.team_number, (float) teamCalculatedData.endgame_climb_successful.total));
                break;
        }
    }

    private void climbSecondary1_lld(ArrayList<LLD_Entry> entries, int i, TeamCalculatedData teamCalculatedData, int position) {
        switch (Constants.Event_View.Climb_Secondary_Options.OPTIONS[position]){
            case Constants.Event_View.Climb_Secondary_Options.TIME:
                entries.add(new LLD_Entry(i, teamCalculatedData.team_number, (float)teamCalculatedData.endgame_climb_time.max, (float)teamCalculatedData.endgame_climb_time.min, (float)teamCalculatedData.endgame_climb_time.average, (float)teamCalculatedData.endgame_climb_time.std));
                break;
        }
    }

    private void climbSecondary2(Map<Integer, Double> sort_values, TeamCalculatedData teamCalculatedData, int position){
        switch (Constants.Event_View.Climb_Secondary_Options.OPTIONS[position]){
            case Constants.Event_View.Climb_Secondary_Options.SUCCESSFUL_ATTEMPTS:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, teamCalculatedData.endgame_climb_successful.total);
                break;
            case Constants.Event_View.Climb_Secondary_Options.TIME:
                mSortedTeamNumbers.add(teamCalculatedData.team_number);
                sort_values.put(teamCalculatedData.team_number, -teamCalculatedData.endgame_climb_time.average);
                break;
        }
    }
    //endregion

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
