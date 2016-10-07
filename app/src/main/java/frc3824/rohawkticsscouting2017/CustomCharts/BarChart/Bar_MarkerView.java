package frc3824.rohawkticsscouting2017.CustomCharts.BarChart;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author frc3824
 * Created: 9/3/16
 *
 *
 */
public class Bar_MarkerView extends MarkerView{

    private final static String TAG = "Bar_MarkerView";

    private TextView mTeamNumber;
    private TextView mTotal;

    private int screenWidth;
    private Context mContext;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public Bar_MarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        mTeamNumber = (TextView)findViewById(R.id.team_number);
        mTotal = (TextView)findViewById(R.id.total);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        mContext = context;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        BarEntryWithTeamNumber b = (BarEntryWithTeamNumber)e;
        mTeamNumber.setText(String.valueOf(b.getTeamNumber()));
        mTotal.setText(String.valueOf(b.getVal()));
    }

    @Override
    public int getXOffset(float xpos) {
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
