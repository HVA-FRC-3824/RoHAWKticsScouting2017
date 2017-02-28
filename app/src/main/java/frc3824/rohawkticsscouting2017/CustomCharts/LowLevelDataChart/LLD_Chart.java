package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarLineChartBase;


/**
 * @author frc3824
 * Created: 8/23/16
 */
public class LLD_Chart extends BarLineChartBase<LLD_Data> implements LLD_DataProvider {

    private final static String TAG = "CustomCandleChart";

    public LLD_Chart(Context context){
        super(context);
    }

    public LLD_Chart(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LLD_Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new LLD_Renderer(this, mAnimator, mViewPortHandler);
        mXAxis.mAxisMinimum = -0.5f;
    }

    @Override
    public LLD_Data getLLD_Data() {
        return mData;
    }
}
