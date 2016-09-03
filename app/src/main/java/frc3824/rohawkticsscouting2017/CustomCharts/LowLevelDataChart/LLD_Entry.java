package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.data.Entry;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
@SuppressLint("ParcelCreator")
public class LLD_Entry extends Entry {

    private final static String TAG = "LLD_Entry";

    private int mTeamNumber;
    private float mMax;
    private float mMin;
    private float mStd;
    private float mAvg;

    /**
     * Constructor.
     *
     * @param x The value on the x-axis.
     * @param max The highest value.
     * @param min The lowest value.
     * @param average The average.
     * @param std The standard deviation.
     */
    public LLD_Entry(int x, int team_number, float max, float min, float average, float std) {
        super((max + min) / 2f, x);

        this.mTeamNumber = team_number;
        this.mMax = max;
        this.mMin = min;
        this.mAvg = average;
        this.mStd = std;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis.
     * @param max The highest value.
     * @param min The lowest value.
     * @param average The average.
     * @param std The standard deviation.
     * @param data Spot for additional data this Entry represents.
     */
    public LLD_Entry(int x, float max, float min, float average, float std,
                     Object data) {
        super((max + min) / 2f, x, data);

        this.mMax = max;
        this.mMin = min;
        this.mAvg = average;
        this.mStd = std;
    }

    /**
     * Returns the overall range (difference) between shadow-high and
     * shadow-low.
     *
     * @return
     */
    public float getShadowRange() {
        return Math.abs(mMax - mMin);
    }

    /**
     * Returns the body size (difference between open and close).
     *
     * @return
     */
    public float getBodyRange() {
        return Math.abs(2 * mStd);
    }

    /**
     * Returns the center value of the candle. (Middle value between high and
     * low)
     */
    @Override
    public float getVal() {
        return super.getVal();
    }

    public LLD_Entry copy() {

        return new LLD_Entry(getXIndex(), mMax, mMin, mAvg, mStd, getData());
    }

    public int getTeamNumber() { return mTeamNumber; }

    /**
     * Returns the max value.
     *
     * @return
     */
    public float getMax() {
        return mMax;
    }

    public void setMax(float max) {
        this.mMax = max;
    }

    /**
     * Returns the lower shadows lowest value.
     *
     * @return
     */
    public float getMin() {
        return mMin;
    }

    public void setMin(float min) {
        this.mMin = min;
    }

    /**
     * Returns the bodys close value.
     *
     * @return
     */
    public float getStd() {
        return mStd;
    }

    public void setStd(float std) {
        this.mStd = std;
    }

    /**
     * Returns the bodys open value.
     *
     * @return
     */
    public float getAvg() {
        return mAvg;
    }

    public void setAvg(float average) {
        this.mAvg = average;
    }

}
