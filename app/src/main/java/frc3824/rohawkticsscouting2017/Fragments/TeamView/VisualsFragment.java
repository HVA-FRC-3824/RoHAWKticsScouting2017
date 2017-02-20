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
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/17/16
 *
 * Fragment that holds all the charts and graphics for an individual team's stats
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
    private BarDataSet mAutoHoppers;
    private BarDataSet mTeleopHoppers;
    private BarDataSet mBothHoppers;

    private PieChart mClimb;
    private PieDataSet mClimbData;

    private LineChart mClimbTime;
    private YAxis mClimbTimeY;
    private LineDataSet mClimbTimeData;

    private PieChart mPilot;
    private PieDataSet mPilotData;

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
                return String.format("%0.2f", value);
            }
        };

        percentVF = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.1f%%",value);
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
                return String.format("%0.2f", value);
            }
        };

        percentYVF = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf((int)value)+"%";
            }
        };

        mMatches = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_visuals, container, false);

        Team team = Database.getInstance().getTeam(mTeamNumber);

        //region gears
        mGears = (RadarChart) view.findViewById(R.id.gears);
        mGears.getLegend().setEnabled(false);
        mGears.setDescription("");
        mGearsY = mGears.getYAxis();
        mGearsY.setShowOnlyMinMax(true);
        mGearsY.setValueFormatter(intYVF);
        setupGearsData(team);
        mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsTotal));
        mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
        mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
        mGears.notifyDataSetChanged();
        mGears.invalidate();
        //endregion

        //region Shooting
        mShooting = (LineChart)view.findViewById(R.id.shooting);
        mShooting.getLegend().setEnabled(false);
        mShooting.setDescription("");
        XAxis xAxis = mShooting.getXAxis();
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
        mHoppers.getAxisLeft().setAxisMaxValue(5);
        mHoppers.getAxisLeft().setLabelCount(5, true);
        mHoppers.getAxisLeft().setValueFormatter(intYVF);
        mHoppers.setPinchZoom(false);
        mHoppers.setDoubleTapToZoomEnabled(false);
        setupHopperData(team);
        mHoppers.setData(new BarData(mMatches, mBothHoppers));
        //endregion

        //region Climb
        mClimb = (PieChart)view.findViewById(R.id.climb);
        mClimb.setUsePercentValues(true);
        mClimb.setDescription("");
        setupClimbData(team);
        mClimb.setData(new PieData(Constants.Match_Scouting.Endgame.CLIMB_OPTIONS.LIST, mClimbData));
        //endregion

        //region Climb Time
        mClimbTime = (LineChart)view.findViewById(R.id.climb_time);
        mClimbTime.getLegend().setEnabled(false);
        mClimbTime.setDescription("");
        xAxis = mClimbTime.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        mClimbTimeY = mClimbTime.getAxisLeft();
        mClimbTimeY.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mClimbTime.getAxisRight().setEnabled(false);
        mClimbTime.setDoubleTapToZoomEnabled(false);
        mClimbTime.setPinchZoom(false);
        setupClimbTimeData(team);
        mClimbTime.setData(new LineData(mMatches, mClimbTimeData));
        //endregion

        //region Pilot Rating
        mPilot = (PieChart)view.findViewById(R.id.pilot);
        mPilot.setUsePercentValues(true);
        mPilot.setDescription("");
        setupPilotRatingData(team);
        mPilot.setData(new PieData(Constants.Team_View.Pilot_Rating_Options.LIST, mPilotData));
        //endregion

        ((RadioGroup)view.findViewById(R.id.gears_radio)).setOnCheckedChangeListener(this);
        ((RadioGroup)view.findViewById(R.id.shooting_radio)).setOnCheckedChangeListener(this);
        ((RadioGroup)view.findViewById(R.id.hoppers_radio)).setOnCheckedChangeListener(this);
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
        entries.add(new Entry((float)team.calc.auto_near_gears_placed.total, 0));
        entries.add(new Entry((float)team.calc.auto_near_gears_dropped.total, 1));
        entries.add(new Entry((float)team.calc.auto_center_gears_placed.total, 2));
        entries.add(new Entry((float)team.calc.auto_center_gears_dropped.total, 3));
        entries.add(new Entry((float)team.calc.auto_far_gears_placed.total, 4));
        entries.add(new Entry((float)team.calc.auto_far_gears_dropped.total, 5));
        mAutoGearsTotal = new RadarDataSet(entries, "Auto Total");
        mAutoGearsTotal.setColor(Color.RED);
        mAutoGearsTotal.setValueFormatter(intVF);

        entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.auto_near_gears_placed.average, 0));
        entries.add(new Entry((float)team.calc.auto_near_gears_dropped.average, 1));
        entries.add(new Entry((float)team.calc.auto_center_gears_placed.average, 2));
        entries.add(new Entry((float)team.calc.auto_center_gears_dropped.average, 3));
        entries.add(new Entry((float)team.calc.auto_far_gears_placed.average, 4));
        entries.add(new Entry((float)team.calc.auto_far_gears_dropped.average, 5));
        mAutoGearsAverage = new RadarDataSet(entries, "Auto Avg");
        mAutoGearsAverage.setColor(Color.RED);
        mAutoGearsAverage.setValueFormatter(intVF);

        entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.teleop_near_gears_placed.total, 0));
        entries.add(new Entry((float)team.calc.teleop_near_gears_dropped.total, 1));
        entries.add(new Entry((float)team.calc.teleop_center_gears_placed.total, 2));
        entries.add(new Entry((float)team.calc.teleop_center_gears_dropped.total, 3));
        entries.add(new Entry((float)team.calc.teleop_far_gears_placed.total, 4));
        entries.add(new Entry((float)team.calc.teleop_far_gears_dropped.total, 5));
        mTeleopGearsTotal = new RadarDataSet(entries, "Teleop Total");
        mTeleopGearsTotal.setColor(Color.RED);
        mTeleopGearsTotal.setValueFormatter(intVF);

        entries = new ArrayList<>();
        entries.add(new Entry((float)team.calc.teleop_near_gears_placed.average, 0));
        entries.add(new Entry((float)team.calc.teleop_near_gears_dropped.average, 1));
        entries.add(new Entry((float)team.calc.teleop_center_gears_placed.average, 2));
        entries.add(new Entry((float)team.calc.teleop_center_gears_dropped.average, 3));
        entries.add(new Entry((float)team.calc.teleop_far_gears_placed.average, 4));
        entries.add(new Entry((float)team.calc.teleop_far_gears_dropped.average, 5));
        mTeleopGearsAverage = new RadarDataSet(entries, "Teleop Avg");
        mTeleopGearsAverage.setColor(Color.RED);
        mTeleopGearsAverage.setValueFormatter(intVF);
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
        for(TeamMatchData tmd: team.completed_matches.values()){
            auto_high_made.add(new Entry(tmd.auto_high_goal_made + tmd.auto_high_goal_correction, i));
            float percent = ((float)(tmd.auto_high_goal_made + tmd.auto_high_goal_correction)) / 
                    ((float)(tmd.auto_high_goal_made + tmd.auto_high_goal_correction + tmd.auto_high_goal_missed));
            auto_high_percent.add(new Entry(percent, i));
            auto_low_made.add(new Entry(tmd.auto_low_goal_made + tmd.auto_low_goal_correction, i));
            percent = ((float)(tmd.auto_low_goal_made + tmd.auto_low_goal_correction)) /
                    ((float)(tmd.auto_low_goal_made + tmd.auto_low_goal_correction + tmd.auto_low_goal_missed));
            auto_low_percent.add(new Entry(percent, i));
            teleop_high_made.add(new Entry(tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction, i));
            percent = ((float)(tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction)) /
                    ((float)(tmd.teleop_high_goal_made + tmd.teleop_high_goal_correction + tmd.teleop_high_goal_missed));
            teleop_high_percent.add(new Entry(percent, i));
            teleop_low_made.add(new Entry(tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction, i));
            percent = ((float)(tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction)) /
                    ((float)(tmd.teleop_low_goal_made + tmd.teleop_low_goal_correction + tmd.teleop_low_goal_missed));
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
        for(TeamMatchData tmd: team.completed_matches.values()){
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
        int i = 0;
        List climb_options = Arrays.asList(Constants.Match_Scouting.Endgame.CLIMB_OPTIONS.LIST);
        for(TeamMatchData tmd: team.completed_matches.values()){
            entries.add(new Entry(climb_options.indexOf(tmd.endgame_climb), i));
            i++;
        }
        mClimbData = new PieDataSet(entries, "Climb");

    }

    /**
     * Setup all the data for climb time line chart
     * @param team
     */
    private void setupClimbTimeData(Team team){
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        List climb_time_options = Arrays.asList(Constants.Match_Scouting.Endgame.CLIMB_TIME_OPTIONS.LIST);
        for(TeamMatchData tmd: team.completed_matches.values()){
            entries.add(new Entry(climb_time_options.indexOf(tmd.endgame_climb_time) * 5, i));
            i++;
        }
        mClimbTimeData = new LineDataSet(entries, "Climb Time");
    }

    private void setupPilotRatingData(Team team){
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        List pilot_rating_options = Arrays.asList(Constants.Team_View.Pilot_Rating_Options.LIST);
        for(TeamMatchData tmd: team.completed_matches.values()){
            SuperMatchData smd = Database.getInstance().getSuperMatchData(tmd.match_number);
            if(smd == null){
                continue;
            }
            if(tmd.alliance_color == Constants.Alliance_Colors.BLUE){
                switch (tmd.alliance_number) {
                    case 1:
                        entries.add(new Entry(pilot_rating_options.indexOf(smd.blue1_pilot_rating), i));
                        break;
                    case 2:
                        entries.add(new Entry(pilot_rating_options.indexOf(smd.blue2_pilot_rating), i));
                        break;
                    case 3:
                        entries.add(new Entry(pilot_rating_options.indexOf(smd.blue3_pilot_rating), i));
                        break;
                    default:
                        assert(false);
                }
            } else {
                switch (tmd.alliance_number) {
                    case 1:
                        entries.add(new Entry(pilot_rating_options.indexOf(smd.red1_pilot_rating), i));
                        break;
                    case 2:
                        entries.add(new Entry(pilot_rating_options.indexOf(smd.red2_pilot_rating), i));
                        break;
                    case 3:
                        entries.add(new Entry(pilot_rating_options.indexOf(smd.red3_pilot_rating), i));
                        break;
                    default:
                        assert(false);
                }
            }



            i++;
        }
        mPilotData = new PieDataSet(entries, "Pilot Rating");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.auto_gears_total:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsTotal));
                mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
                mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
                mGearsY.setValueFormatter(intYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.auto_gears_average:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsAverage));
                mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
                mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
                mGearsY.setValueFormatter(floatYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.teleop_gears_total:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsTotal));
                mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
                mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
                mGearsY.setValueFormatter(intYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.teleop_gears_average:
                mGears.setData(new RadarData(Constants.Team_View.Gear_Options.LIST, mAutoGearsTotal));
                mGearsY.setAxisMaxValue((int) mAutoGearsTotal.getYMax() + 1);
                mGearsY.setLabelCount((int) mAutoGearsTotal.getYMax() + 2, true);
                mGearsY.setValueFormatter(floatYVF);
                mGears.notifyDataSetChanged();
                mGears.invalidate();
                break;
            case R.id.auto_high_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoHighMade));
                mShootingY.setAxisMaxValue((int) mAutoHighMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mAutoHighMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_low_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoLowMade));
                mShootingY.setAxisMaxValue((int) mAutoLowMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mAutoLowMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_high_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mTeleopHighMade));
                mShootingY.setAxisMaxValue((int) mTeleopHighMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mTeleopHighMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_low_made:
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mTeleopLowMade));
                mShootingY.setAxisMaxValue((int) mTeleopLowMade.getYMax() + 1);
                mShootingY.setLabelCount((int) mTeleopLowMade.getYMax() + 2, true);
                mShootingY.setValueFormatter(intYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_high_percent:
                mShooting.getAxisLeft().setAxisMaxValue(100.0f);
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoHighPercent));
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_low_percent:
                mShooting.getAxisLeft().setAxisMaxValue(100.0f);
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mAutoLowPercent));
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_high_percent:
                mShooting.getAxisLeft().setAxisMaxValue(100.0f);
                mShooting.clear();
                mShooting.setData(new LineData(mMatches, mTeleopHighPercent));
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.teleop_low_percent:
                mShooting.getAxisLeft().setAxisMaxValue(100.0f);
                mShooting.clear();
                mShooting.setData(new LineData(mMatches,mTeleopLowPercent));
                mShootingY.setValueFormatter(percentYVF);
                mShooting.notifyDataSetChanged();
                mShooting.invalidate();
                break;
            case R.id.auto_hoppers:
                mHoppers.setData(new BarData(mMatches, mAutoHoppers));
                break;
            case R.id.teleop_hoppers:
                mHoppers.setData(new BarData(mMatches, mTeleopHoppers));
                break;
            case R.id.both_hoppers:
                mHoppers.setData(new BarData(mMatches, mBothHoppers));
                break;
        }
    }
}
