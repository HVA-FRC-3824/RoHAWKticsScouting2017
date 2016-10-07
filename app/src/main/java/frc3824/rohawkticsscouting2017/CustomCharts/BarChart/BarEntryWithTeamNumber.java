package frc3824.rohawkticsscouting2017.CustomCharts.BarChart;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.data.BarEntry;

/**
 * @author frc3824
 * Created: 9/3/16
 *
 *
 */
@SuppressLint("ParcelCreator")
public class BarEntryWithTeamNumber extends BarEntry {

    private final static String TAG = "BarEntryWithTeamNumber";

    private int mTeamNumber;

    public BarEntryWithTeamNumber(int x, int team_number, float y) {
        super(y, x);
        mTeamNumber = team_number;
    }

    public int getTeamNumber()
    {
        return mTeamNumber;
    }
}
