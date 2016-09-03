package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;


import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

import java.util.ArrayList;
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

    public LLD_Data(List<String> xVals) {
        super(xVals);
    }

    public LLD_Data(String[] xVals) {
        super(xVals);
    }

    public LLD_Data(List<String> xVals, List<ILLD_DataSet> dataSets) {
        super(xVals, dataSets);
    }

    public LLD_Data(String[] xVals, List<ILLD_DataSet> dataSets) {
        super(xVals, dataSets);
    }

    public LLD_Data(List<String> xVals, ILLD_DataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    public LLD_Data(String[] xVals, ILLD_DataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    private static List<ILLD_DataSet> toList(ILLD_DataSet dataSet) {
        List<ILLD_DataSet> sets = new ArrayList<ILLD_DataSet>();
        sets.add(dataSet);
        return sets;
    }

}
