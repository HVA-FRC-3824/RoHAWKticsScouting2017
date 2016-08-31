package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.LineScatterCandleRadarRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class LLD_Renderer extends LineScatterCandleRadarRenderer {

    private final static String TAG = "LLD_Renderer";

    private LLD_DataProvider mChart;

    private float[] mShadowBuffers = new float[8];
    private float[] mBodyBuffers = new float[4];
    private float[] mAvgBuffers = new float[6];

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

        LLD_Data lld_data = mChart.getLLD_Data();

        for (ILLD_DataSet set : lld_data.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    @SuppressWarnings("ResourceAsColor")
    protected void drawDataSet(Canvas c, ILLD_DataSet dataSet) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();
        float barSpace = dataSet.getBarSpace();

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
            final float std = e.getStd();
            final float avg_plus = avg + std;
            final float avg_minus = avg - std;
            final float max = e.getMax();
            final float min = e.getMin();

            // calculate the shadow
            mShadowBuffers[0] = xPos;
            mShadowBuffers[2] = xPos;
            mShadowBuffers[4] = xPos;
            mShadowBuffers[6] = xPos;

            mShadowBuffers[1] = max * phaseY;
            mShadowBuffers[3] = avg_plus * phaseY;
            mShadowBuffers[5] = avg_minus * phaseY;
            mShadowBuffers[7] = min * phaseY;

            trans.pointValuesToPixel(mShadowBuffers);

            // draw the shadows
            mRenderPaint.setColor(Color.BLACK);
            mRenderPaint.setStyle(Paint.Style.STROKE);

            c.drawLines(mShadowBuffers, mRenderPaint);

            // calculate the body
            mBodyBuffers[0] = xPos - 0.5f + barSpace;
            mBodyBuffers[1] = avg_minus * phaseY;
            mBodyBuffers[2] = xPos + 0.5f - barSpace;
            mBodyBuffers[3] = avg_plus * phaseY;

            trans.pointValuesToPixel(mBodyBuffers);

            // draw the body
            mRenderPaint.setColor(dataSet.getColor(j));
            mRenderPaint.setStyle(Paint.Style.FILL);

            // left, top, right, bottom
            c.drawRect(
                    mBodyBuffers[0], mBodyBuffers[3],
                    mBodyBuffers[2], mBodyBuffers[1],
                    mRenderPaint);

            // outline the body
            mRenderPaint.setColor(Color.BLACK);
            mRenderPaint.setStyle(Paint.Style.STROKE);

            // left, top, right, bottom
            c.drawRect(
                    mBodyBuffers[0], mBodyBuffers[3],
                    mBodyBuffers[2], mBodyBuffers[1],
                    mRenderPaint);

            // calculate average line
            mAvgBuffers[0] = xPos - 0.5f + barSpace;
            mAvgBuffers[1] = avg;
            mAvgBuffers[2] = xPos + 0.5f - barSpace;
            mAvgBuffers[3] = avg;
            mAvgBuffers[4] = xPos;
            mAvgBuffers[5] = avg;

            trans.pointValuesToPixel(mAvgBuffers);

            // draw the average line
            c.drawLine(mAvgBuffers[0], mAvgBuffers[1], mAvgBuffers[2], mAvgBuffers[3], mRenderPaint);
        }
    }

    @Override
    public void drawValues(Canvas c) {
        /*
        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<ILLD_DataSet> dataSets = mChart.getLLD_Data().getDataSets();

            for (int i = 0; i < dataSets.size(); i++) {

                ILLD_DataSet dataSet = dataSets.get(i);

                if (!dataSet.isDrawValuesEnabled() || dataSet.getEntryCount() == 0)
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                LLD_Transformer trans = new LLD_Transformer(mChart.getTransformer(dataSet.getAxisDependency()));

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
        */
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
