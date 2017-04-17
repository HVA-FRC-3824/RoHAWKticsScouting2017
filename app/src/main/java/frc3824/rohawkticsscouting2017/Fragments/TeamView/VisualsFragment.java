package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Gear;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchPilotData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchTeamPilotData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/17/16
 *
 * Fragment that holds all the charts and graphics for an individual team_number's stats
 */
public class VisualsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "VisualsFragment";

    private int mTeamNumber;

    private RadarChart mGears;
    private YAxis mGearsY;
    private RadarDataSet mAutoGearsTotal;
    private RadarDataSet mAutoGearsAverage;
    private RadarDataSet mTeleopGearsTotal;
    private RadarDataSet mTeleopGearsAverage;

    private LineChart mGearsLine;
    private YAxis mGearsLineY;
    private LineDataSet mAutoGearsTotalPlaced;
    private LineDataSet mAutoGearsNearPlaced;
    private LineDataSet mAutoGearsCenterPlaced;
    private LineDataSet mAutoGearsFarPlaced;
    private LineDataSet mAutoGearsTotalDropped;
    private LineDataSet mAutoGearsNearDropped;
    private LineDataSet mAutoGearsCenterDropped;
    private LineDataSet mAutoGearsFarDropped;
    private LineDataSet mTeleopGearsTotalPlaced;
    private LineDataSet mTeleopGearsNearPlaced;
    private LineDataSet mTeleopGearsCenterPlaced;
    private LineDataSet mTeleopGearsFarPlaced;
    private LineDataSet mTeleopGearsTotalDropped;
    private LineDataSet mTeleopGearsNearDropped;
    private LineDataSet mTeleopGearsCenterDropped;
    private LineDataSet mTeleopGearsFarDropped;
    private LineDataSet mTeleopGearsLoadingStationDropped;

    private ArrayList<String> mMatches;

    private LineChart mShooting;
    private YAxis mShootingY;
    private LineDataSet mAutoHighMade;
    private LineDataSet mAutoHighPercent;
    private LineDataSet mAutoLowMade;
    private LineDataSet mAutoLowPercent;
    private LineDataSet mTeleopHighMade;
    private LineDataSet mTeleopHighPercent;
    private LineDataSet mTeleopLowMade;
    private LineDataSet mTeleopLowPercent;


    private BarChart mHoppers;
    private YAxis mHoppersY;
    private BarDataSet mAutoHoppers;
    private BarDataSet mTeleopHoppers;
    private BarDataSet mBothHoppers;

    private PieChart mClimb;
    private PieDataSet mClimbData;
    private ArrayList<String> mClimbLabels;

    private LineChart mClimbTime;
    private YAxis mClimbTimeY;
    private LineDataSet mClimbTimeData;

    ArrayList<String> mPilotMatches;

    private PieChart mPilot;
    private PieDataSet mPilotData;
    private ArrayList<String> mPilotLabels;
    private LineChart mPilotLine;
    private LineDataSet mPilotLifts;
    private LineDataSet mPilotDrops;
    private LineDataSet mPilotLiftPercentage;
    private YAxis mPilotLineY;

    private ValueFormatter intVF;
    private ValueFormatter floatVF;
    private ValueFormatter percentVF;
    private YAxisValueFormatter intYVF;
    private YAxisValueFormatter floatYVF;
    private YAxisValueFormatter percentYVF;

    public VisualsFragment() {
        intVF = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int)value);
            }
        };

        floatVF = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%02.2f", value);
            }
        };

        percentVF = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%01.1f%%",value);
            }
        };

        intYVF = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf((int)value);
            }
        };

        floatYVF = new YAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.format("%01.1f", value);
            }
        };

        percentYVF = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.format("%01.1f%%",value);
            }
        };

        mMatches = new ArrayList<>();
        mPilotLabels = new ArrayList<>();
        mClimbLabels = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_visuals, container, false);

        Team team = Database.getInstance().getTeam(mTeamNumber);

        //region Gears
        mGears = (RadarChart) view.findViewById(R.id.gears);
        mGears.getLegend().setEnabled(false);
        mGears.setDescription("");
        mGearsY = mGears.getYAxis();
        mGearsY.setShowOnlyMinMax(true);
        mGearsY.setValueFormatter(intYVF);
        setupGearsData(team);
        mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsTotal));
        mGearsY.setAxisMinValue(0);
        mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
        mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
        mGears.notifyDataSetChanged();
        mGears.invalidate();

        mGearsLine = (LineChart) view.findViewById(R.id.gears_line);
        mGearsLine.getLegend().setEnabled(false);
        mGearsLine.setDescription("");
        XAxis xAxis = mGearsLine.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        mGearsLineY = mGearsLine.getAxisLeft();
        mGearsLineY.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mGearsLine.getAxisRight().setEnabled(false);
        mGearsLine.setDoubleTapToZoomEnabled(false);
        mGearsLine.setPinchZoom(false);
        setupGearsLineData(team);
        mGearsLine.clear();
        LineData lineData = new LineData(mMatches);
        lineData.addDataSet(mAutoGearsTotalPlaced);
        lineData.addDataSet(mTeleopGearsTotalPlaced);
        mGearsLine.setData(lineData);
        mGearsLineY.setAxisMinValue(0);
        mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsTotalPlaced.getYMax(), mTeleopGearsTotalPlaced.getYMax())  + 1);
        mGearsLineY.setLabelCount((int) Math.max(mAutoGearsTotalPlaced.getYMax(), mTeleopGearsTotalPlaced.getYMax()) + 2, true);
        mGearsLineY.setValueFormatter(intYVF);
        mGearsLine.notifyDataSetChanged();
        mGearsLine.invalidate();

        //endregion

        //region Shooting
        mShooting = (LineChart)view.findViewById(R.id.shooting);
        mShooting.getLegend().setEnabled(false);
        mShooting.setDescription("");
        xAxis = mShooting.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        mShootingY = mShooting.getAxisLeft();
        mShootingY.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mShooting.getAxisRight().setEnabled(false);
        mShooting.setDoubleTapToZoomEnabled(false);
        mShooting.setPinchZoom(false);
        setupShootingData(team);
        mShooting.clear();
        mShooting.setData(new LineData(mMatches, mAutoHighMade));
        mShootingY.setAxisMinValue(0);
        mShootingY.setAxisMaxValue((int) mAutoHighMade.getYMax() + 1);
        mShootingY.setLabelCount((int) mAutoHighMade.getYMax() + 2, true);
        mShootingY.setValueFormatter(intYVF);
        mShooting.notifyDataSetChanged();
        mShooting.invalidate();
        //endregion

        //region Hoppers
        mHoppers = (BarChart)view.findViewById(R.id.hoppers);
        mHoppers.setDescription("");
        mHoppers.getAxisRight().setEnabled(false);
        mHoppers.getLegend().setEnabled(false);
        mHoppers.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mHoppersY = mHoppers.getAxisLeft();
        mHoppersY.setAxisMinValue(0);
        mHoppersY.setAxisMaxValue(5);
        mHoppersY.setLabelCount(6, true);
        mHoppersY.setValueFormatter(intYVF);
        mHoppers.setPinchZoom(false);
        mHoppers.setDoubleTapToZoomEnabled(false);
        setupHopperData(team);
        mHoppers.setData(new BarData(mMatches, mBothHoppers));
        //endregion

        //region Climb
        mClimb = (PieChart)view.findViewById(R.id.climb);
        mClimb.setUsePercentValues(true);
        mClimb.setDescription("");
        mClimb.setDrawHoleEnabled(false);
        mClimb.setRotationEnabled(false);
        mClimb.setHighlightPerTapEnabled(true);
        setupClimbData(team);
        mClimb.setData(new PieData(mClimbLabels, mClimbData));
        //endregion

        //region Climb Time
        mClimbTime = (LineChart)view.findViewById(R.id.climb_time);
        mClimbTime.getLegend().setEnabled(false);
        mClimbTime.setDescription("");
        setupClimbTimeData(team);
        xAxis = mClimbTime.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        mClimbTimeY = mClimbTime.getAxisLeft();
        mClimbTimeY.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mClimbTimeY.setAxisMinValue(0);
        mClimbTimeY.setAxisMaxValue(mClimbTimeData.getYMax() + 1);
        mClimbTime.getAxisRight().setEnabled(false);
        mClimbTime.setDoubleTapToZoomEnabled(false);
        mClimbTime.setPinchZoom(false);
        mClimbTime.setData(new LineData(mMatches, mClimbTimeData));
        //endregion

        //region Pilot Rating
        mPilot = (PieChart)view.findViewById(R.id.pilot);
        mPilot.setUsePercentValues(true);
        mPilot.setDescription("");
        setupPilotRatingData(team);
        mPilot.setDrawHoleEnabled(false);
        mPilot.setRotationEnabled(false);
        mPilot.setHighlightPerTapEnabled(true);
        mPilot.setData(new PieData(mPilotLabels, mPilotData));
        //endregion

        //region Pilot Line
        mPilotLine = (LineChart)view.findViewById(R.id.pilot_line);
        mPilotLine.getLegend().setEnabled(false);
        mPilotLine.setDescription("");
        setupPilotLineData(team);
        xAxis = mPilotLine.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        mPilotLineY = mPilotLine.getAxisLeft();
        mPilotLineY.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mPilotLineY.setAxisMinValue(0);
        mPilotLineY.setAxisMaxValue(mPilotLifts.getYMax() + 1);
        mPilotLine.getAxisRight().setEnabled(false);
        mPilotLine.setDoubleTapToZoomEnabled(false);
        mPilotLine.setPinchZoom(false);
        mPilotLine.setData(new LineData(mPilotMatches, mPilotLifts));
        //endregion

        ((RadioGroup)view.findViewById(R.id.gears_radio)).check(R.id.auto_gears_total);
        ((RadioGroup)view.findViewById(R.id.gears_radio)).setOnCheckedChangeListener(this);
        ((RadioGroup)view.findViewById(R.id.gears_line_radio)).check(R.id.total_placed);
        ((RadioGroup)view.findViewById(R.id.gears_line_radio)).setOnCheckedChangeListener(this);
        ((RadioGroup)view.findViewById(R.id.shooting_radio)).check(R.id.auto_high_made);
        ((RadioGroup)view.findViewById(R.id.shooting_radio)).setOnCheckedChangeListener(this);
        ((RadioGroup)view.findViewById(R.id.hoppers_radio)).check(R.id.both_hoppers);
        ((RadioGroup)view.findViewById(R.id.hoppers_radio)).setOnCheckedChangeListener(this);
        ((RadioGroup)view.findViewById(R.id.pilot_radio)).check(R.id.pilot_lifts);
        ((RadioGroup)view.findViewById(R.id.pilot_radio)).setOnCheckedChangeListener(this);

        return view;
    }

    public void setTeamNumber(int team_number){
        mTeamNumber = team_number;
    }

    /**
     * Setup all the data for the gears radar chart
     * @param team
     */
    private void setupGearsData(Team team){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.auto_gears.near.placed.total, 0));
        entries.add(new Entry((float)team.calc.auto_gears.near.dropped.total, 1));
        entries.add(new Entry((float)team.calc.auto_gears.center.placed.total, 2));
        entries.add(new Entry((float)team.calc.auto_gears.center.dropped.total, 3));
        entries.add(new Entry((float)team.calc.auto_gears.far.placed.total, 4));
        entries.add(new Entry((float)team.calc.auto_gears.far.dropped.total, 5));
        entries.add(new Entry((float)team.calc.auto_gears.loading_station.dropped.total, 6));
        mAutoGearsTotal = new RadarDataSet(entries, "Auto Total");
        mAutoGearsTotal.setColor(Color.RED);
        mAutoGearsTotal.setValueFormatter(intVF);

        entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.auto_gears.near.placed.average, 0));
        entries.add(new Entry((float)team.calc.auto_gears.near.dropped.average, 1));
        entries.add(new Entry((float)team.calc.auto_gears.center.placed.average, 2));
        entries.add(new Entry((float)team.calc.auto_gears.center.dropped.average, 3));
        entries.add(new Entry((float)team.calc.auto_gears.far.placed.average, 4));
        entries.add(new Entry((float)team.calc.auto_gears.far.dropped.average, 5));
        entries.add(new Entry((float)team.calc.auto_gears.loading_station.dropped.average, 6));
        mAutoGearsAverage = new RadarDataSet(entries, "Auto Avg");
        mAutoGearsAverage.setColor(Color.RED);
        mAutoGearsAverage.setValueFormatter(intVF);

        entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.teleop_gears.near.placed.total, 0));
        entries.add(new Entry((float)team.calc.teleop_gears.near.dropped.total, 1));
        entries.add(new Entry((float)team.calc.teleop_gears.center.placed.total, 2));
        entries.add(new Entry((float)team.calc.teleop_gears.center.dropped.total, 3));
        entries.add(new Entry((float)team.calc.teleop_gears.far.placed.total, 4));
        entries.add(new Entry((float)team.calc.teleop_gears.far.dropped.total, 5));
        entries.add(new Entry((float)team.calc.teleop_gears.loading_station.dropped.total, 6));
        mTeleopGearsTotal = new RadarDataSet(entries, "Teleop Total");
        mTeleopGearsTotal.setColor(Color.RED);
        mTeleopGearsTotal.setValueFormatter(intVF);

        entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.teleop_gears.near.placed.average, 0));
        entries.add(new Entry((float)team.calc.teleop_gears.near.dropped.average, 1));
        entries.add(new Entry((float)team.calc.teleop_gears.center.placed.average, 2));
        entries.add(new Entry((float)team.calc.teleop_gears.center.dropped.average, 3));
        entries.add(new Entry((float)team.calc.teleop_gears.far.placed.average, 4));
        entries.add(new Entry((float)team.calc.teleop_gears.far.dropped.average, 5));
        entries.add(new Entry((float)team.calc.teleop_gears.loading_station.dropped.average, 6));
        mTeleopGearsAverage = new RadarDataSet(entries, "Teleop Avg");
        mTeleopGearsAverage.setColor(Color.RED);
        mTeleopGearsAverage.setValueFormatter(intVF);
    }

    /**
     * Setup all the data for the gears line chart
     */
    private void setupGearsLineData(Team team){
        ArrayList<Entry> auto_total_placed = new ArrayList<>();
        ArrayList<Entry> auto_near_placed = new ArrayList<>();
        ArrayList<Entry> auto_center_placed = new ArrayList<>();
        ArrayList<Entry> auto_far_placed = new ArrayList<>();
        ArrayList<Entry> auto_total_dropped = new ArrayList<>();
        ArrayList<Entry> auto_near_dropped = new ArrayList<>();
        ArrayList<Entry> auto_center_dropped = new ArrayList<>();
        ArrayList<Entry> auto_far_dropped = new ArrayList<>();
        ArrayList<Entry> auto_loading_station_dropped = new ArrayList<>();
        ArrayList<Entry> teleop_total_placed = new ArrayList<>();
        ArrayList<Entry> teleop_near_placed = new ArrayList<>();
        ArrayList<Entry> teleop_center_placed = new ArrayList<>();
        ArrayList<Entry> teleop_far_placed = new ArrayList<>();
        ArrayList<Entry> teleop_total_dropped = new ArrayList<>();
        ArrayList<Entry> teleop_near_dropped = new ArrayList<>();
        ArrayList<Entry> teleop_center_dropped = new ArrayList<>();
        ArrayList<Entry> teleop_far_dropped = new ArrayList<>();
        ArrayList<Entry> teleop_loading_station_dropped = new ArrayList<>();
        int i = 0;
        List<TeamMatchData> completed_matches = new ArrayList(team.completed_matches.values());
        Collections.sort(completed_matches, new Comparator<TeamMatchData>() {
            @Override
            public int compare(TeamMatchData o1, TeamMatchData o2) {
                return Integer.compare(o1.match_number, o2.match_number);
            }
        });

        for(TeamMatchData tmd: completed_matches){
            int auto_total_placed_tmd = 0;
            int auto_near_placed_tmd = 0;
            int auto_center_placed_tmd = 0;
            int auto_far_placed_tmd = 0;
            int auto_total_dropped_tmd = 0;
            int auto_near_dropped_tmd = 0;
            int auto_center_dropped_tmd = 0;
            int auto_far_dropped_tmd = 0;
            for(Gear gear: tmd.auto_gears) {
                if(gear.placed){
                    switch (gear.location){
                        case Constants.Match_Scouting.Custom.Gears.NEAR:
                            auto_near_placed_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.CENTER:
                            auto_center_placed_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.FAR:
                            auto_far_placed_tmd ++;
                            break;
                    }
                    auto_total_placed_tmd ++;
                } else {
                    switch (gear.location){
                        case Constants.Match_Scouting.Custom.Gears.NEAR:
                            auto_near_dropped_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.CENTER:
                            auto_center_dropped_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.FAR:
                            auto_far_dropped_tmd ++;
                            break;
                    }
                    auto_total_dropped_tmd ++;
                }
            }
            auto_total_placed.add(new Entry(auto_total_placed_tmd, i));
            auto_near_placed.add(new Entry(auto_near_placed_tmd, i));
            auto_center_placed.add(new Entry(auto_center_placed_tmd, i));
            auto_far_placed.add(new Entry(auto_far_placed_tmd, i));
            auto_total_dropped.add(new Entry(auto_total_dropped_tmd, i));
            auto_near_dropped.add(new Entry(auto_near_dropped_tmd, i));
            auto_center_dropped.add(new Entry(auto_center_dropped_tmd, i));
            auto_far_dropped.add(new Entry(auto_far_dropped_tmd, i));

            int teleop_total_placed_tmd = 0;
            int teleop_near_placed_tmd = 0;
            int teleop_center_placed_tmd = 0;
            int teleop_far_placed_tmd = 0;
            int teleop_total_dropped_tmd = 0;
            int teleop_near_dropped_tmd = 0;
            int teleop_center_dropped_tmd = 0;
            int teleop_far_dropped_tmd = 0;
            int teleop_loading_station_dropped_tmd = 0;
            
            for(Gear gear: tmd.teleop_gears) {
                if(gear.placed){
                    switch (gear.location){
                        case Constants.Match_Scouting.Custom.Gears.NEAR:
                            teleop_near_placed_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.CENTER:
                            teleop_center_placed_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.FAR:
                            teleop_far_placed_tmd ++;
                            break;
                    }
                    teleop_total_placed_tmd ++;
                } else {
                    switch (gear.location){
                        case Constants.Match_Scouting.Custom.Gears.NEAR:
                            teleop_near_dropped_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.CENTER:
                            teleop_center_dropped_tmd ++;
                            break;
                        case Constants.Match_Scouting.Custom.Gears.FAR:
                            teleop_far_dropped_tmd ++;
                            break;
                    }
                    teleop_total_dropped_tmd ++;
                }
            }
            teleop_total_placed.add(new Entry(teleop_total_placed_tmd, i));
            teleop_near_placed.add(new Entry(teleop_near_placed_tmd, i));
            teleop_center_placed.add(new Entry(teleop_center_placed_tmd, i));
            teleop_far_placed.add(new Entry(teleop_far_placed_tmd, i));
            teleop_total_dropped.add(new Entry(teleop_total_dropped_tmd, i));
            teleop_near_dropped.add(new Entry(teleop_near_dropped_tmd, i));
            teleop_center_dropped.add(new Entry(teleop_center_dropped_tmd, i));
            teleop_far_dropped.add(new Entry(teleop_far_dropped_tmd, i));
            teleop_loading_station_dropped.add(new Entry(teleop_loading_station_dropped_tmd, i));
            i++;
        }

        mAutoGearsTotalPlaced = new LineDataSet(auto_total_placed, "Auto Total Placed");
        mAutoGearsTotalPlaced.setColor(Color.RED);
        mAutoGearsTotalPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsTotalPlaced.setValueFormatter(intVF);

        mTeleopGearsTotalPlaced = new LineDataSet(teleop_total_placed, "Teleop Total Placed");
        mTeleopGearsTotalPlaced.setColor(Color.GREEN);
        mTeleopGearsTotalPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsTotalPlaced.setValueFormatter(intVF);

        mAutoGearsNearPlaced = new LineDataSet(auto_near_placed, "Auto Near Placed");
        mAutoGearsNearPlaced.setColor(Color.RED);
        mAutoGearsNearPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsNearPlaced.setValueFormatter(intVF);

        mTeleopGearsNearPlaced = new LineDataSet(teleop_near_placed, "Teleop Near Placed");
        mTeleopGearsNearPlaced.setColor(Color.GREEN);
        mTeleopGearsNearPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsNearPlaced.setValueFormatter(intVF);

        mAutoGearsCenterPlaced = new LineDataSet(auto_center_placed, "Auto Center Placed");
        mAutoGearsCenterPlaced.setColor(Color.RED);
        mAutoGearsCenterPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsCenterPlaced.setValueFormatter(intVF);

        mTeleopGearsCenterPlaced = new LineDataSet(teleop_center_placed, "Teleop Center Placed");
        mTeleopGearsCenterPlaced.setColor(Color.GREEN);
        mTeleopGearsCenterPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsCenterPlaced.setValueFormatter(intVF);

        mAutoGearsFarPlaced = new LineDataSet(auto_far_placed, "Auto Far Placed");
        mAutoGearsFarPlaced.setColor(Color.RED);
        mAutoGearsFarPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsFarPlaced.setValueFormatter(intVF);

        mTeleopGearsFarPlaced = new LineDataSet(teleop_far_placed, "Teleop Far Placed");
        mTeleopGearsFarPlaced.setColor(Color.GREEN);
        mTeleopGearsFarPlaced.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsFarPlaced.setValueFormatter(intVF);

        mAutoGearsTotalDropped = new LineDataSet(auto_total_dropped, "Auto Total Dropped");
        mAutoGearsTotalDropped.setColor(Color.RED);
        mAutoGearsTotalDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsTotalDropped.setValueFormatter(intVF);

        mTeleopGearsTotalDropped = new LineDataSet(teleop_total_dropped, "Teleop Total Dropped");
        mTeleopGearsTotalDropped.setColor(Color.GREEN);
        mTeleopGearsTotalDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsTotalDropped.setValueFormatter(intVF);

        mAutoGearsNearDropped = new LineDataSet(auto_near_dropped, "Auto Near Dropped");
        mAutoGearsNearDropped.setColor(Color.RED);
        mAutoGearsNearDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsNearDropped.setValueFormatter(intVF);

        mTeleopGearsNearDropped = new LineDataSet(teleop_near_dropped, "Teleop Near Dropped");
        mTeleopGearsNearDropped.setColor(Color.GREEN);
        mTeleopGearsNearDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsNearDropped.setValueFormatter(intVF);

        mAutoGearsCenterDropped = new LineDataSet(auto_center_dropped, "Auto Center Dropped");
        mAutoGearsCenterDropped.setColor(Color.RED);
        mAutoGearsCenterDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsCenterDropped.setValueFormatter(intVF);

        mTeleopGearsCenterDropped = new LineDataSet(teleop_center_dropped, "Teleop Center Dropped");
        mTeleopGearsCenterDropped.setColor(Color.GREEN);
        mTeleopGearsCenterDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsCenterDropped.setValueFormatter(intVF);

        mAutoGearsFarDropped = new LineDataSet(auto_far_dropped, "Auto Far Dropped");
        mAutoGearsFarDropped.setColor(Color.RED);
        mAutoGearsFarDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoGearsFarDropped.setValueFormatter(intVF);

        mTeleopGearsFarDropped = new LineDataSet(teleop_far_dropped, "Teleop Far Dropped");
        mTeleopGearsFarDropped.setColor(Color.GREEN);
        mTeleopGearsFarDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsFarDropped.setValueFormatter(intVF);

        mTeleopGearsLoadingStationDropped = new LineDataSet(teleop_loading_station_dropped, "Teleop Loading Station Dropped");
        mTeleopGearsLoadingStationDropped.setColor(Color.GREEN);
        mTeleopGearsLoadingStationDropped.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopGearsLoadingStationDropped.setValueFormatter(intVF);
    }

    /**
     * Setup all the data for the shooting line chart
     * @param team
     */
    private void setupShootingData(Team team){
        ArrayList<Entry> auto_high_made = new ArrayList<>();
        ArrayList<Entry> auto_high_percent = new ArrayList<>();
        ArrayList<Entry> auto_low_made = new ArrayList<>();
        ArrayList<Entry> auto_low_percent = new ArrayList<>();
        ArrayList<Entry> teleop_high_made = new ArrayList<>();
        ArrayList<Entry> teleop_high_percent = new ArrayList<>();
        ArrayList<Entry> teleop_low_made = new ArrayList<>();
        ArrayList<Entry> teleop_low_percent = new ArrayList<>();
        
        int i = 0;
        List<TeamMatchData> completed_matches = new ArrayList(team.completed_matches.values());
        Collections.sort(completed_matches, new Comparator<TeamMatchData>() {
            @Override
            public int compare(TeamMatchData o1, TeamMatchData o2) {
                return Integer.compare(o1.match_number, o2.match_number);
            }
        });
        for(TeamMatchData tmd: completed_matches){
            auto_high_made.add(new Entry(tmd.auto_high_goal_made + tmd.auto_high_goal_correction, i));
            float percent = ((float)(tmd.auto_high_goal_made + tmd.auto_high_goal_correction)) / 
                    ((float)(tmd.auto_high_goal_made + tmd.auto_high_goal_correction + tmd.auto_high_goal_missed));
            if(Float.isNaN(percent)) {
                percent = 0.0f;
            }
            auto_high_percent.add(new Entry(percent, i));
            auto_low_made.add(new Entry(tmd.auto_low_goal_made + tmd.auto_low_goal_correction, i));
            percent = ((float)(tmd.auto_low_goal_made + tmd.auto_low_goal_correction)) /
                    ((float)(tmd.auto_low_goal_made + tmd.auto_low_goal_correction + tmd.auto_low_goal_missed));
            if(Float.isNaN(percent)) {
                percent = 0.0f;
            }
            auto_low_percent.add(new Entry(percent, i));
            teleop_high_made.add(new Entry(tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction, i));
            percent = ((float)(tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction)) /
                    ((float)(tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction + tmd.teleop_high_goal_missed));
            if(Float.isNaN(percent)) {
                percent = 0.0f;
            }
            teleop_high_percent.add(new Entry(percent, i));
            teleop_low_made.add(new Entry(tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction, i));
            percent = ((float)(tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction)) /
                    ((float)(tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction + tmd.teleop_low_goal_missed));
            if(Float.isNaN(percent)) {
                percent = 0.0f;
            }
            teleop_low_percent.add(new Entry(percent, i));

            mMatches.add(String.format("M%d", tmd.match_number));
            
            i++;
        }
        mAutoHighMade = new LineDataSet(auto_high_made, "Auto High Made");
        mAutoHighMade.setColor(Color.RED);
        mAutoHighMade.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoHighMade.setValueFormatter(intVF);

        mAutoHighPercent = new LineDataSet(auto_high_percent, "Auto High Percent");
        mAutoHighPercent.setColor(Color.RED);
        mAutoHighPercent.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoHighPercent.setValueFormatter(percentVF);

        mAutoLowMade = new LineDataSet(auto_low_made, "Auto Low Made");
        mAutoLowMade.setColor(Color.RED);
        mAutoLowMade.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoLowMade.setValueFormatter(intVF);

        mAutoLowPercent = new LineDataSet(auto_low_percent, "Auto Low Percent");
        mAutoLowPercent.setColor(Color.RED);
        mAutoLowPercent.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoLowPercent.setValueFormatter(percentVF);

        mTeleopHighMade = new LineDataSet(teleop_high_made, "Teleop High Made");
        mTeleopHighMade.setColor(Color.RED);
        mTeleopHighMade.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopHighMade.setValueFormatter(intVF);

        mTeleopHighPercent = new LineDataSet(teleop_high_percent, "Teleop High Percent");
        mTeleopHighPercent.setColor(Color.RED);
        mTeleopHighPercent.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopHighPercent.setValueFormatter(percentVF);

        mTeleopLowMade = new LineDataSet(teleop_low_made, "Teleop Low Made");
        mTeleopLowMade.setColor(Color.RED);
        mTeleopLowMade.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopLowMade.setValueFormatter(intVF);

        mTeleopLowPercent = new LineDataSet(teleop_low_percent, "Teleop Low Percent");
        mTeleopLowPercent.setColor(Color.RED);
        mTeleopLowPercent.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopLowPercent.setValueFormatter(percentVF);
    }

    /**
     * Setup all the data for hoppers bar chart
     * @param team
     */
    private void setupHopperData(Team team){
        ArrayList<BarEntry> auto_ = new ArrayList<>();
        ArrayList<BarEntry> teleop = new ArrayList<>();
        ArrayList<BarEntry> both = new ArrayList<>();
        int i = 0;
        List<TeamMatchData> completed_matches = new ArrayList(team.completed_matches.values());
        Collections.sort(completed_matches, new Comparator<TeamMatchData>() {
            @Override
            public int compare(TeamMatchData o1, TeamMatchData o2) {
                return Integer.compare(o1.match_number, o2.match_number);
            }
        });
        for(TeamMatchData tmd: completed_matches){
            auto_.add(new BarEntry(tmd.auto_hoppers, i));
            teleop.add(new BarEntry(tmd.teleop_hoppers, i));
            both.add(new BarEntry(tmd.auto_hoppers + tmd.teleop_hoppers, i));
            i++;
        }
        mAutoHoppers = new BarDataSet(auto_, "Auto");
        mAutoHoppers.setColor(Color.RED);
        mAutoHoppers.setAxisDependency(YAxis.AxisDependency.LEFT);
        mAutoHoppers.setValueFormatter(intVF);

        mTeleopHoppers = new BarDataSet(teleop, "Teleop");
        mTeleopHoppers.setColor(Color.RED);
        mTeleopHoppers.setAxisDependency(YAxis.AxisDependency.LEFT);
        mTeleopHoppers.setValueFormatter(intVF);

        mBothHoppers = new BarDataSet(both, "Both");
        mBothHoppers.setColor(Color.RED);
        mBothHoppers.setAxisDependency(YAxis.AxisDependency.LEFT);
        mBothHoppers.setValueFormatter(intVF);
    }

    /**
     * Setup all the data for climb pie chart
     * @param team
     */
    private void setupClimbData(Team team){
        ArrayList<Entry> entries = new ArrayList<>();
        List climb_options = Arrays.asList(Constants.Team_View.Climb_Options.LIST);
        List<TeamMatchData> completed_matches = new ArrayList(team.completed_matches.values());
        Collections.sort(completed_matches, new Comparator<TeamMatchData>() {
            @Override
            public int compare(TeamMatchData o1, TeamMatchData o2) {
                return Integer.compare(o1.match_number, o2.match_number);
            }
        });

        int[] climb_options_frequency = new int[climb_options.size()];

        for(TeamMatchData tmd: completed_matches){
            climb_options_frequency[climb_options.indexOf(tmd.endgame_climb)] ++;
        }

        ArrayList<Integer> colors = new ArrayList<>();

        for(int i = 0; i < climb_options.size(); i++)
        {
            if(climb_options_frequency[i] > 0) {
                entries.add(new Entry(climb_options_frequency[i], i));
                mClimbLabels.add(Constants.Team_View.Climb_Options.LIST[i]);
                colors.add(Constants.Team_View.Climb_Options.COLOR_LIST[i]);
            }
        }

        mClimbData = new PieDataSet(entries, "Climb");
        mClimbData.setColors(colors);
    }

    /**
     * Setup all the data for climb time line chart
     * @param team
     */
    private void setupClimbTimeData(Team team){
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        List climb_time_options = Arrays.asList(Constants.Match_Scouting.Endgame.CLIMB_TIME_OPTIONS.LIST);
        List<TeamMatchData> completed_matches = new ArrayList(team.completed_matches.values());
        Collections.sort(completed_matches, new Comparator<TeamMatchData>() {
            @Override
            public int compare(TeamMatchData o1, TeamMatchData o2) {
                return Integer.compare(o1.match_number, o2.match_number);
            }
        });
        for(TeamMatchData tmd: completed_matches){
            if(tmd.endgame_climb.equals(Constants.Match_Scouting.Endgame.CLIMB_OPTIONS.SUCCESSFUL)) {
                entries.add(new Entry((climb_time_options.indexOf(tmd.endgame_climb_time) + 1) * 5, i));
                i++;
            } else {
                entries.add(new Entry(0, i));
                i++;
            }
        }
        mClimbTimeData = new LineDataSet(entries, "Climb Time");
        mClimbTimeData.setColor(Color.RED);
    }

    private void setupPilotRatingData(Team team){
        ArrayList<Entry> entries = new ArrayList<>();
        List pilot_rating_options = Arrays.asList(Constants.Team_View.Pilot_Rating_Options.LIST);

        int[] pilot_rating_options_frequency = new int[pilot_rating_options.size()];

        Database database = Database.getInstance();

        for(int match_number: team.info.match_numbers){
            MatchPilotData mpd = database.getMatchPilotData(match_number);
            if(mpd == null){
                continue;
            }
            for(MatchTeamPilotData mtpd: mpd.teams){
                if(mtpd.team_number == team.team_number) {
                    pilot_rating_options_frequency[pilot_rating_options.indexOf(mtpd.rating)] ++;
                    break;
                }
            }
        }
        ArrayList<Integer> colors = new ArrayList<>();

        for(int i = 0; i < pilot_rating_options.size(); i++){
            if(pilot_rating_options_frequency[i] > 0) {
                entries.add(new Entry(pilot_rating_options_frequency[i], i));
                mPilotLabels.add(Constants.Team_View.Pilot_Rating_Options.LIST[i]);
                colors.add(Constants.Team_View.Pilot_Rating_Options.COLOR_LIST[i]);
            }
        }

        mPilotData = new PieDataSet(entries, "Pilot Rating");
        mPilotData.setColors(colors);
    }

    private void setupPilotLineData(Team team){
        ArrayList<Entry> lift_entries = new ArrayList<>();
        ArrayList<Entry> drop_entries = new ArrayList<>();
        ArrayList<Entry> lift_percentage_entries = new ArrayList<>();

        Database database = Database.getInstance();

        mPilotMatches = new ArrayList<>();

        for(int i = 0; i < team.info.match_numbers.size(); i++){
            MatchPilotData mpd = database.getMatchPilotData(team.info.match_numbers.get(i));
            if(mpd == null){
                continue;
            }
            for(MatchTeamPilotData mtpd: mpd.teams){
                if(mtpd.team_number == team.team_number) {
                    lift_entries.add(new Entry(mtpd.lifts, i));
                    drop_entries.add(new Entry(mtpd.drops, i));

                    float percentage = (float)mtpd.lifts / (float)(mtpd.lifts + mtpd.drops) * 100;
                    if(Float.isNaN(percentage)){
                        percentage = 0.0f;
                    }

                    lift_percentage_entries.add(new Entry(percentage, i));
                    break;
                }
            }
            mPilotMatches.add(String.format("M%d", mpd.match_number));
        }

        mPilotLifts = new LineDataSet(lift_entries, "Lifts");
        mPilotLifts.setColor(Color.RED);
        mPilotLifts.setAxisDependency(YAxis.AxisDependency.LEFT);
        mPilotLifts.setValueFormatter(intVF);

        mPilotDrops = new LineDataSet(drop_entries, "Drops");
        mPilotDrops.setColor(Color.RED);
        mPilotDrops.setAxisDependency(YAxis.AxisDependency.LEFT);
        mPilotDrops.setValueFormatter(intVF);

        mPilotLiftPercentage = new LineDataSet(lift_percentage_entries, "Lift Percentage");
        mPilotLiftPercentage.setColor(Color.RED);
        mPilotLiftPercentage.setAxisDependency(YAxis.AxisDependency.LEFT);
        mPilotLiftPercentage.setValueFormatter(percentVF);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        LineData lineData;
        switch (checkedId){
            case R.id.auto_gears_total:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsTotal));
                mGearsY.setAxisMinValue(0);
                mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
                mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
                mGearsY.setValueFormatter(intYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.auto_gears_average:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsAverage));
                mGearsY.setAxisMinValue(0);
                mGearsY.setAxisMaxValue((int) mAutoGearsAverage.getYMax() + 1);
                mGearsY.setLabelCount((int) mAutoGearsAverage.getYMax() + 2, true);
                mGearsY.setValueFormatter(floatYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.teleop_gears_total:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mTeleopGearsTotal));
                mGearsY.setAxisMinValue(0);
                mGearsY.setAxisMaxValue((int) mTeleopGearsTotal.getYMax() + 1);
                mGearsY.setLabelCount((int) mTeleopGearsTotal.getYMax() + 2, true);
                mGearsY.setValueFormatter(intYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.teleop_gears_average:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mTeleopGearsAverage));
                mGearsY.setAxisMinValue(0);
                mGearsY.setAxisMaxValue((int) mTeleopGearsAverage.getYMax() + 1);
                mGearsY.setLabelCount((int) mTeleopGearsAverage.getYMax() + 2, true);
                mGearsY.setValueFormatter(floatYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.auto_high_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoHighMade));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue((int) mAutoHighMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mAutoHighMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_low_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoLowMade));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue((int) mAutoLowMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mAutoLowMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_high_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mTeleopHighMade));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue((int) mTeleopHighMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mTeleopHighMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_low_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mTeleopLowMade));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue((int) mTeleopLowMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mTeleopLowMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_high_percent:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoHighPercent));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue(100);
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_low_percent:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoLowPercent));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue(100);
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_high_percent:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mTeleopHighPercent));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue(100);
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_low_percent:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches,mTeleopLowPercent));
                mShootingY.setAxisMinValue(0);
                mShootingY.setAxisMaxValue(100);
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_hoppers:
                mHoppers.clear();
                mHoppers.setData(new BarData(mMatches, mAutoHoppers));
                mHoppersY.setAxisMinValue(0);
                mHoppersY.setAxisMaxValue(5);
                mHoppersY.setLabelCount(6, true);
                mHoppersY.setValueFormatter(intYVF);
                mHoppers.notifyDataSetChanged();
                mHoppers.invalidate();
                break;
            case R.id.teleop_hoppers:
                mHoppers.clear();
                mHoppers.setData(new BarData(mMatches, mTeleopHoppers));
                mHoppersY.setAxisMinValue(0);
                mHoppersY.setAxisMaxValue(5);
                mHoppersY.setLabelCount(6, true);
                mHoppersY.setValueFormatter(intYVF);
                mHoppers.notifyDataSetChanged();
                mHoppers.invalidate();
                break;
            case R.id.both_hoppers:
                mHoppers.clear();
                mHoppers.setData(new BarData(mMatches, mBothHoppers));
                mHoppersY.setAxisMinValue(0);
                mHoppersY.setAxisMaxValue(5);
                mHoppersY.setLabelCount(6, true);
                mHoppersY.setValueFormatter(intYVF);
                mHoppers.notifyDataSetChanged();
                mHoppers.invalidate();
                break;
            case R.id.pilot_lifts:
                mPilotLine.clear();
                mPilotLine.setData(new LineData(mMatches, mPilotLifts));
                mPilotLineY.setAxisMinValue(0);
                mPilotLineY.setAxisMaxValue((int) mPilotLifts.getYMax() + 1);
                mPilotLineY.setLabelCount((int) mPilotLifts.getYMax() + 2, true);
                mPilotLineY.setValueFormatter(intYVF);
                mPilotLine.notifyDataSetChanged();
                mPilotLine.invalidate();
                break;
            case R.id.pilot_drops:
                mPilotLine.clear();
                mPilotLine.setData(new LineData(mMatches, mPilotDrops));
                mPilotLineY.setAxisMinValue(0);
                mPilotLineY.setAxisMaxValue((int) mPilotDrops.getYMax() + 1);
                mPilotLineY.setLabelCount((int) mPilotDrops.getYMax() + 2, true);
                mPilotLineY.setValueFormatter(intYVF);
                mPilotLine.notifyDataSetChanged();
                mPilotLine.invalidate();
                break;
            case R.id.pilot_lift_percentage:
                mPilotLine.clear();
                mPilotLine.setData(new LineData(mMatches, mPilotLiftPercentage));
                mPilotLineY.setAxisMinValue(0);
                mPilotLineY.setAxisMaxValue(100);
                mPilotLineY.setValueFormatter(percentYVF);
                mPilotLine.notifyDataSetChanged();
                mPilotLine.invalidate();
                break;
            case R.id.total_placed:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsTotalPlaced);
                lineData.addDataSet(mTeleopGearsTotalPlaced);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsTotalPlaced.getYMax(), mTeleopGearsTotalPlaced.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsTotalPlaced.getYMax(), mTeleopGearsTotalPlaced.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.near_placed:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsNearPlaced);
                lineData.addDataSet(mTeleopGearsNearPlaced);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsNearPlaced.getYMax(), mTeleopGearsNearPlaced.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsNearPlaced.getYMax(), mTeleopGearsNearPlaced.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.center_placed:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsCenterPlaced);
                lineData.addDataSet(mTeleopGearsCenterPlaced);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsCenterPlaced.getYMax(), mTeleopGearsCenterPlaced.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsCenterPlaced.getYMax(), mTeleopGearsCenterPlaced.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.far_placed:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsFarPlaced);
                lineData.addDataSet(mTeleopGearsFarPlaced);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsFarPlaced.getYMax(), mTeleopGearsFarPlaced.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsFarPlaced.getYMax(), mTeleopGearsFarPlaced.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.total_dropped:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsTotalDropped);
                lineData.addDataSet(mTeleopGearsTotalDropped);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsTotalDropped.getYMax(), mTeleopGearsTotalDropped.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsTotalDropped.getYMax(), mTeleopGearsTotalDropped.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.near_dropped:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsNearDropped);
                lineData.addDataSet(mTeleopGearsNearDropped);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsNearDropped.getYMax(), mTeleopGearsNearDropped.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsNearDropped.getYMax(), mTeleopGearsNearDropped.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.center_dropped:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsCenterDropped);
                lineData.addDataSet(mTeleopGearsCenterDropped);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsCenterDropped.getYMax(), mTeleopGearsCenterDropped.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsCenterDropped.getYMax(), mTeleopGearsCenterDropped.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.far_dropped:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mAutoGearsFarDropped);
                lineData.addDataSet(mTeleopGearsFarDropped);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) Math.max(mAutoGearsFarDropped.getYMax(), mTeleopGearsFarDropped.getYMax())  + 1);
                mGearsLineY.setLabelCount((int) Math.max(mAutoGearsFarDropped.getYMax(), mTeleopGearsFarDropped.getYMax()) + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
            case R.id.loading_station_dropped:
                mGearsLine.clear();
                lineData = new LineData(mMatches);
                lineData.addDataSet(mTeleopGearsLoadingStationDropped);
                mGearsLine.setData(lineData);
                mGearsLineY.setAxisMinValue(0);
                mGearsLineY.setAxisMaxValue((int) mTeleopGearsLoadingStationDropped.getYMax()  + 1);
                mGearsLineY.setLabelCount((int) mTeleopGearsLoadingStationDropped.getYMax() + 2, true);
                mGearsLineY.setValueFormatter(intYVF);
                mGearsLine.notifyDataSetChanged();
                mGearsLine.invalidate();
                break;
        }
    }
}
