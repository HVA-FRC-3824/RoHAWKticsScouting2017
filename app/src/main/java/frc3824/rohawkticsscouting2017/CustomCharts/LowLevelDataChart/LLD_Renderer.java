package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.LineScatterCandleRadarRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class LLD_Renderer extends LineScatterCandleRadarRenderer {

    private final static String TAG = "LLD_Renderer";

    private LLD_DataProvider mChart;

    private float[] mShadowBuffers = new float[8];
    private float[] mBodyBuffers = new float[4];
    private float[] mRangeBuffers = new float[4];
    private float[] mTopBuffers = new float[4];
    private float[] mBottomBuffers = new float[4];

    public LLD_Renderer(LLD_DataProvider chart, ChartAnimator animator,
                        ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;
    }

    @Override
    public void initBuffers() {

    }

    @Override
    public void drawData(Canvas c) {

        LLD_Data candleData = mChart.getLLD_Data();

        for (ILLD_DataSet set : candleData.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    @SuppressWarnings("ResourceAsColor")
    protected void drawDataSet(Canvas c, ILLD_DataSet dataSet) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();
        float barSpace = dataSet.getBarSpace();
        boolean showCandleBar = dataSet.getShowCandleBar();

        mXBounds.set(mChart, dataSet);

        mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());

        // draw the body
        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

            // get the entry
            LLD_Entry e = dataSet.getEntryForIndex(j);

            if (e == null)
                continue;

            final float xPos = e.getX();

            final float avg = e.getAvg();
            float std = e.getStd();
            final float avg_plus = avg + std;
            final float avg_minus = avg - std;
            final float max = e.getMax();
            final float min = e.getMin();

            if (showCandleBar) {
                // calculate the shadow

                mShadowBuffers[0] = xPos;
                mShadowBuffers[2] = xPos;
                mShadowBuffers[4] = xPos;
                mShadowBuffers[6] = xPos;

                mShadowBuffers[1] = max * phaseY;
                mShadowBuffers[3] = avg_plus * phaseY;
                mShadowBuffers[5] = min * phaseY;
                mShadowBuffers[7] = avg_minus * phaseY;


                trans.pointValuesToPixel(mShadowBuffers);

                // draw the shadows

                if (dataSet.getShadowColorSameAsCandle()) {

                    //if (open > close)
                        mRenderPaint.setColor(
                                dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE ?
                                        dataSet.getColor(j) :
                                        dataSet.getDecreasingColor()
                        );
/*
                    else if (open < close)
                        mRenderPaint.setColor(
                                dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE ?
                                        dataSet.getColor(j) :
                                        dataSet.getIncreasingColor()
                        );

                    else
                        mRenderPaint.setColor(
                                dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE ?
                                        dataSet.getColor(j) :
                                        dataSet.getNeutralColor()
                        );
*/
                } else {
                    mRenderPaint.setColor(
                            dataSet.getShadowColor() == ColorTemplate.COLOR_NONE ?
                                    dataSet.getColor(j) :
                                    dataSet.getShadowColor()
                    );
                }

                mRenderPaint.setStyle(Paint.Style.STROKE);

                c.drawLines(mShadowBuffers, mRenderPaint);

                // calculate the body

                mBodyBuffers[0] = xPos - 0.5f + barSpace;
                mBodyBuffers[1] = avg_minus * phaseY;
                mBodyBuffers[2] = (xPos + 0.5f - barSpace);
                mBodyBuffers[3] = avg_plus * phaseY;

                trans.pointValuesToPixel(mBodyBuffers);

                // draw body differently for increasing and decreasing entry
      //          if (open > close) { // decreasing

                    if (dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) {
                        mRenderPaint.setColor(dataSet.getColor(j));
                    } else {
                        mRenderPaint.setColor(dataSet.getDecreasingColor());
                    }

                    mRenderPaint.setStyle(dataSet.getDecreasingPaintStyle());

                    c.drawRect(
                            mBodyBuffers[0], mBodyBuffers[3],
                            mBodyBuffers[2], mBodyBuffers[1],
                            mRenderPaint);
/*
                } else if (open < close) {

                    if (dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) {
                        mRenderPaint.setColor(dataSet.getColor(j));
                    } else {
                        mRenderPaint.setColor(dataSet.getIncreasingColor());
                    }

                    mRenderPaint.setStyle(dataSet.getIncreasingPaintStyle());

                    c.drawRect(
                            mBodyBuffers[0], mBodyBuffers[1],
                            mBodyBuffers[2], mBodyBuffers[3],
                            mRenderPaint);
                } else { // equal values

                    if (dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) {
                        mRenderPaint.setColor(dataSet.getColor(j));
                    } else {
                        mRenderPaint.setColor(dataSet.getNeutralColor());
                    }

                    c.drawLine(
                            mBodyBuffers[0], mBodyBuffers[1],
                            mBodyBuffers[2], mBodyBuffers[3],
                            mRenderPaint);
                }
*/
            } else {

                mRangeBuffers[0] = xPos;
                mRangeBuffers[1] = avg_plus * phaseY;
                mRangeBuffers[2] = xPos;
                mRangeBuffers[3] = avg_minus * phaseY;

                mTopBuffers[0] = xPos - 0.5f + barSpace;
                mTopBuffers[1] = avg_plus * phaseY;
                mTopBuffers[2] = xPos;
                mTopBuffers[3] = avg_plus * phaseY;

                mBottomBuffers[0] = xPos + 0.5f - barSpace;
                mBottomBuffers[1] = avg_minus * phaseY;
                mBottomBuffers[2] = xPos;
                mBottomBuffers[3] = avg_minus * phaseY;

                trans.pointValuesToPixel(mRangeBuffers);
                trans.pointValuesToPixel(mTopBuffers);
                trans.pointValuesToPixel(mBottomBuffers);

                // draw the ranges
                int barColor;

                //if (open > close)
                    barColor = dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE
                            ? dataSet.getColor(j)
                            : dataSet.getDecreasingColor();
/*
                else if (open < close)
                    barColor = dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE
                            ? dataSet.getColor(j)
                            : dataSet.getIncreasingColor();
                else
                    barColor = dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE
                            ? dataSet.getColor(j)
                            : dataSet.getNeutralColor();
*/
                mRenderPaint.setColor(barColor);
                c.drawLine(
                        mRangeBuffers[0], mRangeBuffers[1],
                        mRangeBuffers[2], mRangeBuffers[3],
                        mRenderPaint);
                c.drawLine(
                        mTopBuffers[0], mTopBuffers[1],
                        mTopBuffers[2], mTopBuffers[3],
                        mRenderPaint);
                c.drawLine(
                        mBottomBuffers[0], mBottomBuffers[1],
                        mBottomBuffers[2], mBottomBuffers[3],
                        mRenderPaint);
            }
        }
    }

    @Override
    public void drawValues(Canvas c) {
// if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<ILLD_DataSet> dataSets = mChart.getLLD_Data().getDataSets();

            for (int i = 0; i < dataSets.size(); i++) {

                ILLD_DataSet dataSet = dataSets.get(i);

                if (!dataSet.isDrawValuesEnabled() || dataSet.getEntryCount() == 0)
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                LLD_Transformer trans = (LLD_Transformer)mChart.getTransformer(dataSet.getAxisDependency());

                mXBounds.set(mChart, dataSet);

                float[] positions = trans.generateTransformedValuesLLD(
                        dataSet, mAnimator.getPhaseX(), mAnimator.getPhaseY(), mXBounds.min, mXBounds.max);

                float yOffset = Utils.convertDpToPixel(5f);

                for (int j = 0; j < positions.length; j += 2) {

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))
                        continue;

                    LLD_Entry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    drawValue(c, dataSet.getValueFormatter(), entry.getMax(), entry, i, x, y - yOffset, dataSet
                            .getValueTextColor(j / 2));
                }
            }
        }
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        LLD_Data lld_data = mChart.getLLD_Data();

        for (Highlight high : indices) {

            ILLD_DataSet set = lld_data.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            LLD_Entry e = set.getEntryForXPos(high.getX());

            if (!isInBoundsX(e, set))
                continue;

            float lowValue = e.getMin() * mAnimator.getPhaseY();
            float highValue = e.getMax() * mAnimator.getPhaseY();
            float y = (lowValue + highValue) / 2f;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelsForValues(e.getX(), y);

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);
        }
    }

}
