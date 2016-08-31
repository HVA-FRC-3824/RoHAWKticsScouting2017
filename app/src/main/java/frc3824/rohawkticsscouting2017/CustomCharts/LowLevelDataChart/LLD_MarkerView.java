package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author Andrew Messing
 *         Created: 8/30/16
 */
public class LLD_MarkerView extends MarkerView {

    private final static String TAG = "LLD_MarkerView";

    private TextView mTeamNumber;
    private TextView mMax;
    private TextView mMin;
    private TextView mAvg;
    private TextView mStd;

    private int screenWidth;
    private Context mContext;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public LLD_MarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        mTeamNumber = (TextView)findViewById(R.id.team_number);
        mMax = (TextView)findViewById(R.id.max);
        mMin = (TextView)findViewById(R.id.min);
        mAvg = (TextView)findViewById(R.id.avg);
        mStd = (TextView)findViewById(R.id.std);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        mContext = context;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        LLD_Entry le = (LLD_Entry)e;

        mTeamNumber.setText(String.format("%d", le.getTeamNumber()));
        mMax.setText(String.format(java.util.Locale.US, "Max: %.2f", le.getMax()));
        mMin.setText(String.format(java.util.Locale.US, "Min: %.2f", le.getMin()));
        mAvg.setText(String.format(java.util.Locale.US, "Average: %.2f", le.getAvg()));
        mStd.setText(String.format(java.util.Locale.US, "Std: %.2f", le.getStd()));
    }

    @Override
    public int getXOffset(float xpos) {

        Log.d(TAG, String.format("X: %f, W: %d, SW: %d",xpos, getWidth(), screenWidth));
        if(xpos + getWidth() > screenWidth)
        {
            return (int)(screenWidth - xpos - getWidth() - Utilities.dpToPixels(mContext, 16));
        }

        return 0;
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
