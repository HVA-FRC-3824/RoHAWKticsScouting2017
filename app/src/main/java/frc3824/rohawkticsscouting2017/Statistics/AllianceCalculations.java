package frc3824.rohawkticsscouting2017.Statistics;

import org.apache.commons.math3.distribution.TDistribution;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;

/**
 * @author frc3824
 * Created: 8/19/16
 *
 *
 */
public class AllianceCalculations {

    private final static String TAG = "AllianceCalculations";
    private Database mDatabase;
    private ArrayList<Team> mAlliance;
    private ArrayList<TeamCalculations> mTeams;

    public AllianceCalculations(ArrayList<Team> a) {
        mAlliance = a;
        mDatabase = Database.getInstance();
        mTeams = new ArrayList<>();
        for(int i = 0; i < mAlliance.size(); i++)
        {
            mTeams.add(new TeamCalculations(mAlliance.get(i)));
        }
    }

    /**
        Predicted Score

        pScore = ∑_(T in A) autoAbility(T)
    */
    public double predictedScore()
    {
        return predictedScore(false);
    }

    public double predictedScore(boolean elimination) {
        double pScore = 0.0;

        double auto_gears = 0.0;
        double teleop_gears = 0.0;
        double fuel_points = 0.0;

        for(Team t: mAlliance)
        {
            pScore += t.calc.auto_baseline.average * 5;

            double num_low_balls = (t.calc.auto_high_goal_made.average * 9 + t.calc.auto_low_goal_made.average * 3 +
                    t.calc.teleop_high_goal_made.average * 3 + t.calc.teleop_low_goal_made.average) / 9;
            fuel_points += num_low_balls;
            pScore += (int)num_low_balls;

            pScore += t.calc.endgame_climb_successful.average * 50;

            auto_gears += t.calc.auto_total_gears_placed.average;
            teleop_gears += t.calc.teleop_total_gears_placed.average;
        }

        if(elimination && fuel_points >= 40){
            pScore += 20;
        }

        int rotors = 0;
        if(auto_gears >= 3){
            pScore += 120;
            rotors += 2;
        } else if(auto_gears >= 1) {
            pScore += 60;
            rotors ++;
        }

        if(auto_gears + teleop_gears >= 12) {
            pScore += (4 - rotors) * 40;
            if(elimination){
                pScore += 100;
            }
        } else if(auto_gears + teleop_gears >= 6) {
            pScore += (3-rotors) * 40;
        } else if(auto_gears + teleop_gears >= 2) {
            pScore += (2 - rotors) * 40;
        } else {
            pScore += (1 - rotors) * 40;
        }

        return pScore;
    }

    /**
        Standard Deviation of Predicted Score
     */
    public double std_predictedScore() {
        return std_predictedScore(false);
    }

    public double std_predictedScore(boolean elimination) {
        double std_pScore = 0.0;

        double auto_points = 0;
        double teleop_points = 0;

        double auto_fuel_points = 0;
        double teleop_fuel_points = 0;

        double auto_gear_points = 0;
        double teleop_gear_points = 0;

        double auto_gear = 0;
        double teleop_gear = 0;

        double climb_points = 0;

        for(Team t: mAlliance) {
            auto_fuel_points += (int)(t.calc.auto_high_goal_made.average + t.calc.auto_low_goal_made.average / 3);
            teleop_fuel_points += (int)(t.calc.teleop_high_goal_made.average / 3 + t.calc.teleop_low_goal_made.average / 9);

            auto_gear += t.calc.auto_total_gears_placed.average;
            teleop_gear += t.calc.teleop_total_gears_placed.average;

            auto_points += t.calc.auto_baseline.average;

            climb_points += t.calc.endgame_climb_successful.average * 50;
        }

        int rotor = 0;
        if(auto_gear >= 3){
            auto_gear_points = 120;
            rotor = 2;
        } else if(auto_gear >= 1){
            auto_gear_points = 60;
            rotor = 1;
        }

        if(auto_gear + teleop_gear >= 12){
            teleop_gear_points = (4 - rotor) * 40;
        } else if(auto_gear + teleop_gear >= 6){
            teleop_gear_points = (3 - rotor) * 40;
        } else if(auto_gear + teleop_gear >= 2){
            teleop_gear_points = (2 - rotor) * 40;
        } else {
            teleop_gear_points = (1 - rotor) * 40;
        }

        std_pScore += Math.pow(auto_fuel_points, 2) + Math.pow(auto_gear_points, 2) + Math.pow(auto_points, 2) +
                Math.pow(teleop_fuel_points, 2) + Math.pow(teleop_gear_points, 2) + Math.pow(climb_points, 2);

        std_pScore = Math.sqrt(std_pScore);

        return std_pScore;
    }

    /**
        Win Probability

        In order to determine the win probability of alliance A facing alliance O, Welch's t-test
        (https://en.wikipedia.org/wiki/Welch%27s_t-test). This test is expressed using the formula

            t = (X_bar_1 + X_bar_2) / sqrt(s_1^2 / N_1 + s_2^2 / N_2)

            - X_bar_1 is the mean of the first sample
            - s_1 is the standard deviation of the first sample
            - N_1 is the size of the first sample

            - X_bar_2 is the mean of the second sample
            - s_2 is the standard deviation of the second sample
            - N_2 is the size of the second sample

       This t is then converted to a win probability using the cumulative distribution function
       (https://en.wikipedia.org/wiki/Cumulative_distribution_function) for a t-distribution T(t|v)

       In this case X_bar_1 is the predicted score for alliance A, s_1 is the standard deviation of
       the predicted score for alliance A, and N_1 is the average number of completed matches for
       each of the teams on alliance A.

            wC(A,O) = T(t|v)

       t is the t-value generated by the Welch's test and v is the degrees of freedom approximated
       by the Welch-Satterthwaite equation(https://en.wikipedia.org/wiki/WelchSatterthwaite_equation)

            v ≈ (s_1^2 / N_1 + s_2^2 / N_2)^2 / (s_1^4 / (N_1^2 * v_1) + s_2^4 / (N_2^2 * v_2))

       where v_1 = N_1 - 1 (the degrees of freedom for the first variance) and v_2 = N_2 -1
    */
    public double winProbabilityOver(ArrayList<Team> O) {
        AllianceCalculations oCalc = new AllianceCalculations(O);
        return winProbabilityOver(oCalc);
    }

    public double winProbabilityOver(AllianceCalculations oCalc){
        double m_1 = predictedScore();
        double m_2 = oCalc.predictedScore();
        double s_1 = std_predictedScore();
        double s_2 = oCalc.std_predictedScore();
        double N_1 = sampleSize();
        double N_2 = oCalc.sampleSize();

        double t = Statistics.welchsTest(m_1, s_1, N_1, m_2, s_2, N_2);

        double v = Statistics.dof(s_1, N_1, s_2, N_2);

        TDistribution tDistribution = new TDistribution(v);
        return tDistribution.cumulativeProbability(t);
    }

    public double sampleSize() {
        double average = 0.0;
        for(int i = 0; i < mAlliance.size(); i++)
        {
            average += mTeams.get(i).numberOfCompletedMatches();
        }

        average /= mAlliance.size();

        return average;
    }

    public double pressureChance(){
        double x = 40 * 9; // kPa in terms of low goal teleop
        double auto_high = 0;
        double auto_low = 0;
        double teleop_high = 0;
        double teleop_low = 0;
        double auto_high_squared = 0;
        double auto_low_squared = 0;
        double teleop_high_squared = 0;
        double teleop_low_squared = 0;
        for(Team t: mAlliance){
            auto_high += t.calc.auto_high_goal_made.average * 9;
            auto_low += t.calc.auto_low_goal_made.average * 3;
            teleop_high += t.calc.teleop_high_goal_made.average * 3;
            teleop_low += t.calc.teleop_low_goal_made.average;

            auto_high_squared += Math.pow(t.calc.auto_high_goal_made.average * 9, 2);
            auto_low_squared += Math.pow(t.calc.auto_low_goal_made.average * 3, 2);
            teleop_high_squared += Math.pow(t.calc.teleop_high_goal_made.average * 3, 2);
            teleop_low_squared += Math.pow(t.calc.teleop_low_goal_made.average, 2);
        }
        double mu = auto_high + auto_low + teleop_high + teleop_low;
        double sigma = Math.sqrt(auto_high_squared + auto_low_squared + teleop_high_squared + teleop_low_squared);

        return Statistics.probabilityDensity(x, mu, sigma);
    }

    public double rotorChance(){
        double x = 12; // gears
        double mu = 0;
        double sigma = 0;
        for(Team t: mAlliance){
            mu += t.calc.auto_total_gears_placed.average + t.calc.teleop_total_gears_placed.average;
            sigma += Math.pow(t.calc.auto_total_gears_placed.average, 2) + Math.pow(t.calc.teleop_total_gears_placed.average, 2);
        }
        sigma = Math.sqrt(sigma);
        return Statistics.probabilityDensity(x, mu, sigma);
    }
}
