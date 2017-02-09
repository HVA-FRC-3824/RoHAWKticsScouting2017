package frc3824.rohawkticsscouting2017.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.StrategySuggestion;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamCalculatedData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamDTFeedback;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamRankingData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * The Database class handles all data and connecting with the Firebase system
 */
public class Database {

    private final static String TAG = "Database";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRootRef;

    //region Database References
    private DatabaseReference mEventRef;
    private DatabaseReference mScheduleRef;
    private DatabaseReference mTeamPitDataRef;
    private DatabaseReference mSuperMatchDataRef;
    private DatabaseReference mTeamDTFeedbackRef;
    private DatabaseReference mPartialMatchRef;
    private DatabaseReference mTeamCalulatedDataRef;
    private DatabaseReference mTeamLogisticsRef;
    private DatabaseReference mStrategyRef;
    private DatabaseReference mStrategySuggestionRef;
    private DatabaseReference mCurrentTeamRankingDataRef;
    private DatabaseReference mPredictedTeamRankingDataRef;
    private DatabaseReference mFirstTeamPickAbilityRef;
    private DatabaseReference mSecondTeamPickAbilityRef;
    private DatabaseReference mThirdTeamPickAbilityRef;
    private DatabaseReference mScoutAccuracyRef;
    //endregion

    private String mEventKey;

    //region Maps
    private static Set<String> mEvents;
    private Map<Integer, Match> mSchedule;
    private Map<Integer, SuperMatchData> mSuperMatchDataMap;
    private Map<Integer, TeamPitData> mTeamPitDataMap;
    private Map<String, TeamMatchData> mTeamMatchDataMap;
    private Map<Integer, TeamDTFeedback> mTeamDTFeedbackMap;
    private Map<Integer, TeamLogistics> mTeamLogisticsMap;
    private Map<Integer, TeamCalculatedData> mTeamCalculatedDataMap;
    private Map<Integer, TeamRankingData> mCurrentTeamRankingDataMap;
    private Map<Integer, TeamRankingData> mPredictedTeamRankngDataMap;
    private Map<Integer, TeamPickAbility> mFirstTeamPickAbilityMap;
    private Map<Integer, TeamPickAbility> mSecondTeamPickAbilityMap;
    private Map<Integer, TeamPickAbility> mThirdTeamPickAbilityMap;
    private Map<String, Strategy> mStrategyMap;
    private Map<String, StrategySuggestion> mStrategySuggestionMap;
    private Map<String, ScoutAccuracy> mScoutAccuracyMap;
    //endregion

    private static Database mSingleton;

    public static Database getInstance(String eventKey) {
        if(mSingleton == null)
        {
            mSingleton = new Database();
        }

        mSingleton.setEventKey(eventKey);
        return  mSingleton;
    }

    public static Database getInstance() {
        if(mSingleton == null)
        {
            mSingleton = new Database();
        }

        return  mSingleton;
    }

    private Database() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mRootRef = mFirebaseDatabase.getReference();

        mEvents = new HashSet<>();

        //Root reference's children are the events
        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildAdded: " + dataSnapshot.getKey());
                mEvents.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildChanged: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "onChildRemoved: " + dataSnapshot.getKey());
                mEvents.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "onCancelled");
            }
        });
    }

    public static Set<String> getEvents()
    {
        return mEvents;
    }

    private void setEventKey(String eventKey) {
        if(eventKey == "" || mEventKey == eventKey)
            return;

        mEventRef = mRootRef.child(eventKey);

        //region Setup references and maps
        //region Schedule
        mScheduleRef = mEventRef.child("schedule");
        mSchedule = new HashMap<>();
        mScheduleRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "schedule.onChildAdded: " + dataSnapshot.getKey());
                mSchedule.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "schedule.onChildChanged: " + dataSnapshot.getKey());
                mSchedule.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "schedule.onChildRemoved: " + dataSnapshot.getKey());
                mSchedule.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "schedule.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "schedule.onCancelled");
            }
        });
        //endregion

        //region Partial Matches
        mPartialMatchRef = mEventRef.child("partial_match");
        mTeamMatchDataMap = new HashMap<>();
        mPartialMatchRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildAdded: " + dataSnapshot.getKey());
                mTeamMatchDataMap.put(dataSnapshot.getKey(),dataSnapshot.getValue(TeamMatchData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildChanged: " + dataSnapshot.getKey());
                mTeamMatchDataMap.put(dataSnapshot.getKey(),dataSnapshot.getValue(TeamMatchData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "partial_match.onChildRemoved: " + dataSnapshot.getKey());
                mTeamMatchDataMap.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "partial_match.onCancelled");
            }
        });
        //endregion

        //region Pit
        mTeamPitDataRef = mEventRef.child("pit");
        mTeamPitDataMap = new HashMap<>();
        mTeamPitDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pit.onChildAdded: " + dataSnapshot.getKey());
                mTeamPitDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPitData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pit.onChildChanged: " + dataSnapshot.getKey());
                mTeamPitDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPitData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "pit.onChildRemoved: " + dataSnapshot.getKey());
                mTeamPitDataMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pit.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "pit.onCancelled");
            }
        });
        //endregion

        //region Super
        mSuperMatchDataRef = mEventRef.child("super_match");
        mSuperMatchDataMap = new HashMap<>();
        mSuperMatchDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildAdded: " + dataSnapshot.getKey());
                mSuperMatchDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(SuperMatchData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildChanged: " + dataSnapshot.getKey());
                mSuperMatchDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(SuperMatchData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "super_match.onChildRemoved: " + dataSnapshot.getKey());
                mSuperMatchDataMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "super_match.onCancelled: ");
            }
        });
        //endregion

        //region Drive Team Feedback
        mTeamDTFeedbackRef = mEventRef.child("feedback");
        mTeamDTFeedbackMap = new HashMap<>();
        mTeamDTFeedbackRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "feedback.onChildAdded: " + dataSnapshot.getKey());
                mTeamDTFeedbackMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamDTFeedback.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "feedback.onChildChanged: " + dataSnapshot.getKey());
                mTeamDTFeedbackMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamDTFeedback.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "feedback.onChildRemoved: " + dataSnapshot.getKey());
                mTeamDTFeedbackMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "feedback.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "feedback.onChildRemoved");
            }
        });
        //endregion

        //region Team Info
        mTeamLogisticsRef = mEventRef.child("info");
        mTeamLogisticsMap = new HashMap<>();
        mTeamLogisticsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "info.onChildAdded: " + dataSnapshot.getKey());
                mTeamLogisticsMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamLogistics.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "info.onChildChanged: " + dataSnapshot.getKey());
                mTeamLogisticsMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamLogistics.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "info.onChildRemoved: " + dataSnapshot.getKey());
                mTeamLogisticsMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "info.onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "info.onCancelled");
            }
        });
        //endregion

        //region Calculated
        mTeamCalulatedDataRef = mEventRef.child("calculated");
        mTeamCalculatedDataMap = new HashMap<>();
        mTeamCalulatedDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "calc.onChildAdded: " + dataSnapshot.getKey());
                mTeamCalculatedDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamCalculatedData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "calc.onChildChanged: " + dataSnapshot.getKey());
                mTeamCalculatedDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamCalculatedData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "calc.onChildRemoved: " + dataSnapshot.getKey());
                mTeamCalculatedDataMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "calc.onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "calc.onCancelled");
            }
        });
        //endregion

        //region Strategy
        mStrategyRef = mEventRef.child("strategy").child("drawings");
        mStrategyMap = new HashMap<>();
        mStrategyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.individual.onChildAdded: " + dataSnapshot.getKey());
                mStrategyMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Strategy.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.individual.onChildChanged: " + dataSnapshot.getKey());
                mStrategyMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Strategy.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "strategies.individual.onChildRemoved: " + dataSnapshot.getKey());
                mStrategyMap.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.individual.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "strategies.individual.onCancelled");
            }
        });
        //endregion

        //region Strategy Suggestion
        mStrategySuggestionRef = mEventRef.child("strategy").child("suggestions");
        mStrategySuggestionMap = new HashMap<>();
        mStrategySuggestionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.suggestion.onChildAdded:" + dataSnapshot.getKey());
                mStrategySuggestionMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(StrategySuggestion.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.suggestion.onChildChanged: " + dataSnapshot.getKey());
                mStrategySuggestionMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(StrategySuggestion.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "strategies.suggestion.onChildRemoved: " + dataSnapshot.getKey());
                mStrategySuggestionMap.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.suggestion.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "strategies.suggestion.onCancelled");
            }
        });
        //endregion

        //region Current Ranking
        mCurrentTeamRankingDataRef = mEventRef.child("rankings").child("current");
        mCurrentTeamRankingDataMap = new HashMap<>();
        mCurrentTeamRankingDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "current_ranking.onChildAdded: " + dataSnapshot.getKey());
                mCurrentTeamRankingDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamRankingData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "current_ranking.onChildChanged: " + dataSnapshot.getKey());
                mCurrentTeamRankingDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamRankingData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "current_ranking.onChildRemoved: " + dataSnapshot.getKey());
                mCurrentTeamRankingDataMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "current_ranking.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "current_ranking.onCancelled");
            }
        });
        //endregion

        //region Predicted Ranking
        mPredictedTeamRankingDataRef = mEventRef.child("rankings").child("predicted");
        mPredictedTeamRankngDataMap = new HashMap<>();
        mPredictedTeamRankingDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "predicted_ranking.onChildAdded: " + dataSnapshot.getKey());
                mPredictedTeamRankngDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamRankingData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "predicted_ranking.onChildChanged: " + dataSnapshot.getKey());
                mPredictedTeamRankngDataMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamRankingData.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "predicted_ranking.onChildRemoved: " + dataSnapshot.getKey());
                mPredictedTeamRankngDataMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "predicted_ranking.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "predicted_ranking.onCancelled");
            }
        });
        //endregion

        //region First Pick
        mFirstTeamPickAbilityRef = mEventRef.child("first_pick");
        mFirstTeamPickAbilityMap = new HashMap<>();
        mFirstTeamPickAbilityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "first_pick.onChildAdded: " + dataSnapshot.getKey());
                mFirstTeamPickAbilityMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPickAbility.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "first_pick.onChildChanged: " + dataSnapshot.getKey());
                mFirstTeamPickAbilityMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPickAbility.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "first_pick.onChildRemoved: " + dataSnapshot.getKey());
                mFirstTeamPickAbilityMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "first_pick.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "first_pick.onCancelled");
            }
        });
        //endregion

        //region Second Pick
        mSecondTeamPickAbilityRef = mEventRef.child("second_pick");
        mSecondTeamPickAbilityMap = new HashMap<>();
        mSecondTeamPickAbilityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "second_pick.onChildAdded: " + dataSnapshot.getKey());
                mSecondTeamPickAbilityMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPickAbility.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "second_pick.onChildChanged: " + dataSnapshot.getKey());
                mSecondTeamPickAbilityMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPickAbility.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "second_pick.onChildRemoved: " + dataSnapshot.getKey());
                mSecondTeamPickAbilityMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "second_pick.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "second_pick.onCancelled");
            }
        });
        //endregion

        //region Third Pick
        mThirdTeamPickAbilityRef = mEventRef.child("third_pick");
        mThirdTeamPickAbilityMap = new HashMap<>();
        mThirdTeamPickAbilityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "third_pick.onChildAdded: " + dataSnapshot.getKey());
                mThirdTeamPickAbilityMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPickAbility.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "third_pick.onChildChanged: " + dataSnapshot.getKey());
                mThirdTeamPickAbilityMap.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TeamPickAbility.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "third_pick.onChildRemoved: " + dataSnapshot.getKey());
                mThirdTeamPickAbilityMap.remove(Integer.parseInt(dataSnapshot.getKey()));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "third_pick.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "third_pick.onCancelled");
            }
        });
        //endregion

        //region Scout Accuracy
        mScoutAccuracyRef = mEventRef.child("scout_accuracy");
        mScoutAccuracyMap = new HashMap<>();
        mScoutAccuracyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "scout_accuracy.onChildAdded" + dataSnapshot.getKey());
                mScoutAccuracyMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(ScoutAccuracy.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "scout_accuracy.onChildChanged" + dataSnapshot.getKey());
                mScoutAccuracyMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(ScoutAccuracy.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "scout_accuracy.onChildRemoved" + dataSnapshot.getKey());
                mScoutAccuracyMap.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "scout_accuracy.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "scout_accuracy.onCancelled");
            }
        });
        //endregion
        //endregion

        mEventKey = eventKey;
    }

    //region Schedule Data
    public void setMatch(Match match) {
        match.last_modified = System.currentTimeMillis();
        mScheduleRef.child(String.format("%d", match.match_number)).setValue(match);
    }

    public Match getMatch(int match_number)
    {
        return mSchedule.get(match_number);
    }

    public void removeMatch(int match_number){
        mScheduleRef.child(String.format("%d", match_number)).removeValue();
    }

    public Map<Integer, Match> getSchedule()
    {
        return mSchedule;
    }

    public int getNumberOfMatches()
    {
        return mSchedule.size();
    }
    //endregion

    //region Match Scouting Data
    public void setTeamMatchData(TeamMatchData teamMatchData) {
        teamMatchData.last_modified = System.currentTimeMillis();
        mPartialMatchRef.child(String.format("%d_%d", teamMatchData.match_number, teamMatchData.team_number)).setValue(teamMatchData);
    }

    public TeamMatchData getTeamMatchData(int match_number, int team_number) {
        return mTeamMatchDataMap.get(String.format("%d_%d", match_number, team_number));
    }

    public ArrayList<TeamMatchData> getAllTeamMatchData(){
        return new ArrayList<>(mTeamMatchDataMap.values());
    }
    //endregion

    //region Pit Scouting Data
    public void setTeamPitData(TeamPitData teamPitData) {
        teamPitData.last_modified = System.currentTimeMillis();
        mTeamPitDataRef.child(String.format("%d", teamPitData.team_number)).setValue(teamPitData);
    }

    public TeamPitData getTeamPitData(int team_number)
    {
        return mTeamPitDataMap.get(team_number);
    }

    public ArrayList<TeamPitData> getAllTeamPitData(){
        return new ArrayList<>(mTeamPitDataMap.values());
    }
    //endregion

    //region Super Scouting Data
    public void setSuperMatchData(SuperMatchData superMatchData) {
        superMatchData.last_modified = System.currentTimeMillis();
        mSuperMatchDataRef.child(String.format("%d", superMatchData.match_number)).setValue(superMatchData);
    }

    public SuperMatchData getSuperMatchData(int match_number) {
        return mSuperMatchDataMap.get(String.format("%d",match_number));
    }

    public ArrayList<SuperMatchData> getAllSuperMatchData() {
        return new ArrayList<>(mSuperMatchDataMap.values());
    }
    //endregion

    //region Drive Team Feedback Data
    public void setTeamDTFeedback(TeamDTFeedback teamDTFeedback) {
        teamDTFeedback.last_modified = System.currentTimeMillis();
        mTeamDTFeedbackRef.child(String.format("%d", teamDTFeedback.team_number)).setValue(teamDTFeedback);
    }

    public TeamDTFeedback getTeamDTFeedback(int team_number) {
        return mTeamDTFeedbackMap.get(String.format("%d", team_number));
    }

    public ArrayList<TeamDTFeedback> getAllTeamDTFeedbacks() {
        return new ArrayList<>(mTeamDTFeedbackMap.values());
    }
    //endregion

    //region Team Info
    public void setTeamLogistics(TeamLogistics teamLogistics) {
        teamLogistics.last_modified = System.currentTimeMillis();
        mTeamLogisticsRef.child(String.format("%d", teamLogistics.team_number)).setValue(teamLogistics);
    }

    public TeamLogistics getTeamLogistics(int team_number) {
        return mTeamLogisticsMap.get(team_number);
    }

    public void removeTeamLogistics(int team_number){
        mTeamLogisticsRef.child(String.format("%d", team_number)).removeValue();
    }
    //endregion

    //region Calculated data
    public void setTeamCalculatedData(TeamCalculatedData teamCalculatedData) {
        teamCalculatedData.last_modified = System.currentTimeMillis();
        mTeamCalulatedDataRef.child(String.format("%d", teamCalculatedData.team_number)).setValue(teamCalculatedData);
    }

    public TeamCalculatedData getTeamCalculatedData(int team_number){ return mTeamCalculatedDataMap.get(team_number); }
    //endregion

    //region Pick List Data
    //region First Pick
    public void setFirstTPA(TeamPickAbility first) {
        first.last_modified = System.currentTimeMillis();
        mFirstTeamPickAbilityRef.child(String.valueOf(first.team_number)).setValue(first);
    }

    public TeamPickAbility getFirstTPA(int team_number)
    {
        return mFirstTeamPickAbilityMap.get(team_number);
    }
    //endregion

    //region Second Pick
    public void setSecondTPA(TeamPickAbility second) {
        second.last_modified = System.currentTimeMillis();
        mSecondTeamPickAbilityRef.child(String.valueOf(second.team_number)).setValue(second);
    }

    public TeamPickAbility getSecondTPA(int team_number) {
        return mSecondTeamPickAbilityMap.get(team_number);
    }
    //endregion

    //region Third Pick
    public void setThirdTPA(TeamPickAbility third) {
        third.last_modified = System.currentTimeMillis();
        mThirdTeamPickAbilityRef.child(String.valueOf(third.team_number)).setValue(third);
    }

    public TeamPickAbility getThirdTPA(int team_number) {
        return mThirdTeamPickAbilityMap.get(team_number);
    }
    //endregion

    //region DNP
    public void setDNP(int team_number, boolean dnp) {
        long last_modified = System.currentTimeMillis();
        mFirstTeamPickAbilityRef.child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mFirstTeamPickAbilityRef.child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mSecondTeamPickAbilityRef.child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mSecondTeamPickAbilityRef.child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mThirdTeamPickAbilityRef.child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mThirdTeamPickAbilityRef.child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
    }

    public ArrayList<Integer> getDnpList() {
        ArrayList<Integer> teams = getTeamNumbers();
        // Remove all teams that are not set to do not pick
        for(int i = 0; i < teams.size(); i++)
        {
            if(!mFirstTeamPickAbilityMap.get(teams.get(i)).dnp)
            {
                teams.remove(i);
                i--;
            }
        }
        return teams;
    }
    //endregion

    public void setPicked(int team_number, boolean picked) {
        long last_modified = System.currentTimeMillis();
        mFirstTeamPickAbilityRef.child(String.valueOf(team_number)).child("picked").setValue(picked);
        mFirstTeamPickAbilityRef.child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mSecondTeamPickAbilityRef.child(String.valueOf(team_number)).child("picked").setValue(picked);
        mSecondTeamPickAbilityRef.child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mThirdTeamPickAbilityRef.child(String.valueOf(team_number)).child("picked").setValue(picked);
        mThirdTeamPickAbilityRef.child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
    }
    //endregion

    //region Rankings
    //region Current Rankings
    public void setCurrentTRD(TeamRankingData teamRankingData) {
        teamRankingData.last_modified = System.currentTimeMillis();
        mCurrentTeamRankingDataRef.child(String.format("%d", teamRankingData.team_number)).setValue(teamRankingData);
    }

    public TeamRankingData getCurrentTRD(int team_number) {
        return mCurrentTeamRankingDataMap.get(team_number);
    }

    public Map<Integer, TeamRankingData> getCurrentRankings()
    {
        return mCurrentTeamRankingDataMap;
    }
    //endregion

    //region Predicted Rankings
    public void setPredictedTRD(TeamRankingData teamRankingData) {
        teamRankingData.last_modified = System.currentTimeMillis();
        mPredictedTeamRankingDataRef.child(String.format("%d", teamRankingData.team_number)).setValue(teamRankingData);
    }

    public TeamRankingData getPredictedTRD(int team_number) {
        return mPredictedTeamRankngDataMap.get(team_number);
    }

    public Map<Integer, TeamRankingData> getPredictedRankings() {
        return mPredictedTeamRankngDataMap;
    }
    //endregion
    //endregion

    //region All Team Data
    public Team getTeam(int team_number) {
        Team team = new Team();
        team.team_number = team_number;

        team.info = getTeamLogistics(team_number);
        if(team.info == null)
        {
            team.info = new TeamLogistics();
            team.info.team_number = team_number;
            team.info.match_numbers = new ArrayList<>();
        }
        Collections.sort(team.info.match_numbers);

        team.pit = getTeamPitData(team_number);
        if(team.pit == null)
        {
            team.pit = new TeamPitData();
            team.pit.team_number = team_number;
        }

        team.drive_team_feedback = getTeamDTFeedback(team_number);
        if(team.drive_team_feedback == null)
        {
            team.drive_team_feedback = new TeamDTFeedback();
            team.drive_team_feedback.team_number = team_number;
        }

        team.calc = getTeamCalculatedData(team_number);
        if(team.calc == null)
        {
            team.calc = new TeamCalculatedData();
            team.calc.team_number = team_number;
        }

        team.completed_matches = new HashMap<>();
        for(int match_number : team.info.match_numbers)
        {
            TeamMatchData teamMatchData = getTeamMatchData(match_number, team_number);
            if(teamMatchData != null) {
                team.completed_matches.put(match_number, teamMatchData);
            }
        }

        team.current_ranking = getCurrentTRD(team_number);
        if(team.current_ranking == null)
        {
            team.current_ranking = new TeamRankingData();
            team.current_ranking.team_number = team_number;
        }

        team.predicted_ranking = getPredictedTRD(team_number);
        if(team.predicted_ranking == null)
        {
            team.predicted_ranking = new TeamRankingData();
            team.predicted_ranking.team_number = team_number;
        }

        team.first_pick = getFirstTPA(team_number);
        if(team.first_pick == null)
        {
            team.first_pick = new TeamPickAbility();
            team.first_pick.team_number = team_number;
        }

        team.second_pick = getSecondTPA(team_number);
        if(team.second_pick == null)
        {
            team.second_pick = new TeamPickAbility();
            team.second_pick.team_number = team_number;
        }

        team.third_pick = getThirdTPA(team_number);
        if(team.third_pick == null)
        {
            team.third_pick = new TeamPickAbility();
            team.third_pick.team_number = team_number;
        }

        return team;
    }

    public void setTeam(Team team) {
        setTeamCalculatedData(team.calc);
        setTeamLogistics(team.info);
        setTeamPitData(team.pit);
        setTeamDTFeedback(team.drive_team_feedback);
        for(TeamMatchData entry: team.completed_matches.values())
        {
            setTeamMatchData(entry);
        }
        setCurrentTRD(team.current_ranking);
        setPredictedTRD(team.predicted_ranking);
        setFirstTPA(team.first_pick);
        setSecondTPA(team.second_pick);
        setThirdTPA(team.third_pick);
    }

    public List<TeamMatchData> getCompletedMatches(int team_number) {
        Team team = getTeam(team_number);
        List<TeamMatchData> completedMatches = new ArrayList<>();
        for(int match_number: team.info.match_numbers)
        {
            TeamMatchData tim = getTeamMatchData(match_number, team_number);
            if(tim != null)
            {
                completedMatches.add(tim);
            }
        }

        return completedMatches;
    }
    //endregion

    //region Strategy
    public void setStrategy(Strategy strategy) {
        strategy.last_modified = System.currentTimeMillis();
        mStrategyRef.child(strategy.name).setValue(strategy);
    }

    public Strategy getStrategy(String strategy_name) {
        return mStrategyMap.get(strategy_name);
    }

    public ArrayList<Strategy> getAllStrategies() {
        return new ArrayList<>(mStrategyMap.values());
    }
    //endregion

    //region StrategySuggestion
    public void setStrategySuggestion(StrategySuggestion strategySuggestion){
        strategySuggestion.last_modified = System.currentTimeMillis();
        mStrategySuggestionRef.child(strategySuggestion.key).setValue(strategySuggestion);
    }

    public StrategySuggestion getStrategySuggestion(String key){
        return mStrategySuggestionMap.get(key);
    }

    public ArrayList<StrategySuggestion> getAllStrategySuggestions(){
        return new ArrayList<>(mStrategySuggestionMap.values());
    }
    //endregion

    //region Notes
    public ArrayList<NoteView> getAllNotes() {
        ArrayList<NoteView> notes = new ArrayList<>();
        for(TeamMatchData teamMatchData : mTeamMatchDataMap.values())
        {
            NoteView note = new NoteView();
            note.note_type = NoteView.NoteType.MATCH;
            note.match_number = teamMatchData.match_number;
            note.team_number = teamMatchData.team_number;
            note.note = teamMatchData.notes;
            notes.add(note);
        }
        for(SuperMatchData superMatchData : mSuperMatchDataMap.values())
        {
            NoteView note = new NoteView();
            note.note_type = NoteView.NoteType.SUPER;
            note.match_number = superMatchData.match_number;
            note.team_number = -1;
            note.note = superMatchData.notes;
            notes.add(note);
        }
        return notes;
    }
    //endregion

    //region Scout Accuracy
    public void setScoutAccuracy(ScoutAccuracy scout){
        scout.last_modified = System.currentTimeMillis();
        mScoutAccuracyRef.child(scout.name).setValue(scout);
    }

    public ScoutAccuracy getScoutAccuracy(String name){
        return mScoutAccuracyMap.get(name);
    }

    public ArrayList<ScoutAccuracy> getScoutAccuracies(){
        return new ArrayList<>(mScoutAccuracyMap.values());
    }
    //endregion

    //region Team Numbers
    public ArrayList<Integer> getTeamNumbers() {
        ArrayList<Integer> team_numbers = new ArrayList<>(mTeamLogisticsMap.keySet());
        Collections.sort(team_numbers);
        return team_numbers;
    }

    public int getTeamNumberBefore(int team_number) {
        ArrayList<Integer> teams = getTeamNumbers();

        for(int i = 0; i < teams.size(); i++)
        {
            if(teams.get(i) == team_number)
            {
                if(i == 0)
                {
                    return 0;
                }
                else
                {
                    return teams.get(i - 1);
                }
            }
        }

        return 0;
    }

    public int getTeamNumberAfter(int team_number) {
        ArrayList<Integer> teams = getTeamNumbers();

        for(int i = 0; i < teams.size(); i++)
        {
            if(teams.get(i) == team_number)
            {
                if(i == teams.size() - 1)
                {
                    return 0;
                }
                else
                {
                    return teams.get(i + 1);
                }
            }
        }

        return 0;
    }
    //endregion
}
