package frc3824.rohawkticsscouting2017.CustomCharts.LowLevelDataChart;

import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public interface LLD_DataProvider extends BarLineScatterCandleBubbleDataProvider {

    LLD_Data getLLD_Data();
}
