package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import java.util.List;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
public class LowLevelStats {

    private final static String TAG = "LowLevelStats";

    public double max;
    public double min;
    public double average;
    public double std;
    public double total;

    public LowLevelStats() {}

    public static LowLevelStats fromInt(List<Integer> list)
    {
        LowLevelStats lls = new LowLevelStats();
        lls.total = 0.0;
        lls.max = Double.MIN_VALUE;
        lls.min = Double.MAX_VALUE;
        for(int i: list)
        {
            if(i > lls.max)
            {
                lls.max = i;
            }
            if(i < lls.min)
            {
                lls.min = i;
            }
            lls.total += i;
        }

        lls.average /= list.size();
        lls.std = 0.0;
        for(int i: list)
        {
            lls.std += Math.pow((double)i - lls.average, 2);
        }

        lls.std /= list.size();
        lls.std = Math.sqrt(lls.std);

        return lls;
    }

    public static LowLevelStats fromDouble(List<Double> list)
    {
        LowLevelStats lls = new LowLevelStats();
        lls.total = 0.0;
        lls.max = Double.MIN_VALUE;
        lls.min = Double.MAX_VALUE;
        for(double i: list)
        {
            if(i > lls.max)
            {
                lls.max = i;
            }
            if(i < lls.min)
            {
                lls.min = i;
            }
            lls.total += i;
        }

        lls.average /= list.size();
        lls.std = 0.0;
        for(double i: list)
        {
            lls.std += Math.pow(i - lls.average, 2);
        }

        lls.std /= list.size();
        lls.std = Math.sqrt(lls.std);

        return lls;
    }

    public static LowLevelStats fromBoolean(List<Boolean> list)
    {
        LowLevelStats lls = new LowLevelStats();
        lls.total = 0;
        lls.max = Double.MIN_VALUE;
        lls.min = Double.MAX_VALUE;
        for(boolean i: list)
        {
            if(i)
            {
                lls.max = 1.0;
            }
            if(!i)
            {
                lls.min = 0.0;
            }
            lls.total += i ? 1.0 : 0.0;
        }

        lls.average /= list.size();
        lls.std = 0.0;
        for(boolean i: list)
        {
            lls.std += Math.pow((i ? 1.0 : 0.0) - lls.average, 2);
        }

        lls.std /= list.size();
        lls.std = Math.sqrt(lls.std);

        return lls;
    }
}
