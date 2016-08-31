package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class LLD_Transformer {

    private final static String TAG = "LLD_Transformer";

    private Transformer mTransformer;

    public LLD_Transformer(Transformer transformer) {
        mTransformer = transformer;
    }

    protected float[] valuePointsForGenerateTransformedValuesLLD = new float[1];
    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the LLD_Chart.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesLLD(ILLD_DataSet data,
                                                   float phaseX, float phaseY, int from, int to) {

        final int count = (int) ((to - from) * phaseX + 1) * 2;

        if(valuePointsForGenerateTransformedValuesLLD.length != count){
            valuePointsForGenerateTransformedValuesLLD = new float[count];
        }
        float[] valuePoints = valuePointsForGenerateTransformedValuesLLD;

        for (int j = 0; j < count; j += 2) {

            LLD_Entry e = data.getEntryForIndex(j / 2 + from);

            if (e != null) {
                valuePoints[j] = e.getX();
                valuePoints[j + 1] = e.getMax() * phaseY;
            }else{
                valuePoints[j] = 0;
                valuePoints[j + 1] = 0;
            }
        }

        mTransformer.getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }
}
