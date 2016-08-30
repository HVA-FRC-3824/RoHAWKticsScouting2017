package frc3824.rohawkticsscouting2017.Statistics;

/**
 * @author Andrew Messing
 * Created: 8/19/16
 *
 */
public class Statistics {

    private final static String TAG = "Statistics";

    /**
     *
     * @param X_bar_1
     * @param s_1
     * @param N_1
     * @param X_bar_2
     * @param s_2
     * @param N_2
     * @return
     */
    public static double welchsTest(double X_bar_1, double s_1, double N_1, double X_bar_2, double s_2, double N_2)
    {
        return (X_bar_1 + X_bar_2) / Math.sqrt( (s_1/N_1) + (s_2/N_2));
    }

    /**
     * Calculates the degrees of freedom
     * @param s_1
     * @param N_1
     * @param s_2
     * @param N_2
     * @return
     */
    public static double dof(double s_1, double N_1, double s_2, double N_2)
    {
        double v_1 = N_1 - 1;
        double v_2 = N_2 -1;

        return Math.pow(Math.pow(s_1, 2) / N_1 + Math.pow(s_2, 2) / N_2, 2) /
                (Math.pow(s_1, 4) / (Math.pow(N_1, 2) * v_1) + Math.pow(s_2, 4) / (Math.pow(N_2, 2) * v_2));
    }
}
