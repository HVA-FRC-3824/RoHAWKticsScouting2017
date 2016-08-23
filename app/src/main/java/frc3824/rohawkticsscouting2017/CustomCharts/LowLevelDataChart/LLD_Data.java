package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;


import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

import java.util.List;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 */
public class LLD_Data extends BarLineScatterCandleBubbleData<ILLD_DataSet> {

    private final static String TAG = "LLD_Data";

    public LLD_Data() {
        super();
    }

    public LLD_Data(List<ILLD_DataSet> dataSets) {
        super(dataSets);
    }

    public LLD_Data(ILLD_DataSet... dataSets) {
        super(dataSets);
    }

}
