package frc3824.rohawkticsscouting2017.Firebase.DataModels;

/**
 * @author frc3824
 * Created: 3/16/17
 */

public class TeamQualitativeData {

    public int team_number;

    public QualitativeResult speed;
    public QualitativeResult torque;
    public QualitativeResult control;
    public QualitativeResult defense;

    public TeamQualitativeData(){
        speed = new QualitativeResult();
        torque = new QualitativeResult();
        control = new QualitativeResult();
        defense = new QualitativeResult();
    }
}
