package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 */
public interface ILLD_DataSet extends ILineScatterCandleRadarDataSet<LLD_Entry> {

    /**
     * Returns the space that is left out on the left and right side of each
     * candle.
     *
     * @return
     */
    float getBarSpace();

    /**
     * Returns whether the candle bars should show?
     * When false, only "ticks" will show
     *
     * - default: true
     *
     * @return
     */
    boolean getShowCandleBar();

    /**
     * Returns the width of the candle-shadow-line in pixels.
     *
     * @return
     */
    float getShadowWidth();

    /**
     * Returns shadow color for all entries
     *
     * @return
     */
    int getShadowColor();

    /**
     * Returns the neutral color (for open == close)
     *
     * @return
     */
    int getNeutralColor();

    /**
     * Returns the increasing color (for open < close).
     *
     * @return
     */
    int getIncreasingColor();

    /**
     * Returns the decreasing color (for open > close).
     *
     * @return
     */
    int getDecreasingColor();

    /**
     * Returns paint style when open < close
     *
     * @return
     */
    Paint.Style getIncreasingPaintStyle();

    /**
     * Returns paint style when open > close
     *
     * @return
     */
    Paint.Style getDecreasingPaintStyle();

    /**
     * Is the shadow color same as the candle color?
     *
     * @return
     */
    boolean getShadowColorSameAsCandle();

}