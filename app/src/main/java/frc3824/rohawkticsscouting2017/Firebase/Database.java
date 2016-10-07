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
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TCD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TDTF;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TID;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPA;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TRD;
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
    private DatabaseReference mPitRef;
    private DatabaseReference mSuperRef;
    private DatabaseReference mFeedbackRef;
    private DatabaseReference mPartialMatchRef;
    private DatabaseReference mCalulatedRef;
    private DatabaseReference mInfoRef;
    private DatabaseReference mStrategyRef;
    private DatabaseReference mCurrentRankingRef;
    private DatabaseReference mPredictedRankingRef;
    private DatabaseReference mFirstPickRef;
    private DatabaseReference mSecondPickRef;
    private DatabaseReference mThirdPickRef;
    //endregion

    private String mEventKey;

    //region Maps
    private static Set<String> mEvents;
    private Map<Integer, Match> mSchedule;
    private Map<Integer, SMD> mSMDs;
    private Map<Integer, TPD> mTPDs;
    private Map<String, TMD> mTMDs;
    private Map<Integer, TDTF> mTDTFs;
    private Map<Integer, TID> mTIDs;
    private Map<Integer, TCD> mTCDs;
    private Map<Integer, TRD> mCurrent_TRDs;
    private Map<Integer, TRD> mPredicted_TRDs;
    private Map<Integer, TPA> mFirst_TPA;
    private Map<Integer, TPA> mSecond_TPA;
    private Map<Integer, TPA> mThird_TPA;
    private Map<String, Strategy> mStrategies;
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
        mTMDs = new HashMap<>();
        mPartialMatchRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildAdded: " + dataSnapshot.getKey());
                mTMDs.put(dataSnapshot.getKey(),dataSnapshot.getValue(TMD.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildChanged: " + dataSnapshot.getKey());
                mTMDs.put(dataSnapshot.getKey(),dataSnapshot.getValue(TMD.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "partial_match.onChildRemoved: " + dataSnapshot.getKey());
                mTMDs.remove(dataSnapshot.getKey());
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
        mPitRef = mEventRef.child("pit");
        mTPDs = new HashMap<>();
        mPitRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pit.onChildAdded: " + dataSnapshot.getKey());
                mTPDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPD.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pit.onChildChanged: " + dataSnapshot.getKey());
                mTPDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPD.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "pit.onChildRemoved: " + dataSnapshot.getKey());
                mTPDs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mSuperRef = mEventRef.child("super_match");
        mSMDs = new HashMap<>();
        mSuperRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildAdded: " + dataSnapshot.getKey());
                mSMDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(SMD.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildChanged: " + dataSnapshot.getKey());
                mSMDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(SMD.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "super_match.onChildRemoved: " + dataSnapshot.getKey());
                mSMDs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mFeedbackRef = mEventRef.child("feedback");
        mTDTFs = new HashMap<>();
        mFeedbackRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "feedback.onChildAdded: " + dataSnapshot.getKey());
                mTDTFs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TDTF.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "feedback.onChildChanged: " + dataSnapshot.getKey());
                mTDTFs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TDTF.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "feedback.onChildRemoved: " + dataSnapshot.getKey());
                mTDTFs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mInfoRef = mEventRef.child("info");
        mTIDs = new HashMap<>();
        mInfoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "info.onChildAdded: " + dataSnapshot.getKey());
                mTIDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TID.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "info.onChildChanged: " + dataSnapshot.getKey());
                mTIDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TID.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "info.onChildRemoved: " + dataSnapshot.getKey());
                mTIDs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mCalulatedRef = mEventRef.child("calculated");
        mTCDs = new HashMap<>();
        mCalulatedRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "calc.onChildAdded: " + dataSnapshot.getKey());
                mTCDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TCD.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "calc.onChildChanged: " + dataSnapshot.getKey());
                mTCDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TCD.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "calc.onChildRemoved: " + dataSnapshot.getKey());
                mTCDs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mStrategyRef = mEventRef.child("strategies");
        mStrategies = new HashMap<>();
        mStrategyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.onChildAdded: " + dataSnapshot.getKey());
                mStrategies.put(dataSnapshot.getKey(), dataSnapshot.getValue(Strategy.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.onChildChanged: " + dataSnapshot.getKey());
                mStrategies.put(dataSnapshot.getKey(), dataSnapshot.getValue(Strategy.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "strategies.onChildRemoved: " + dataSnapshot.getKey());
                mStrategies.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "strategies.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "strategies.onCancelled");
            }
        });
        //endregion

        //region Current Ranking
        mCurrentRankingRef = mEventRef.child("rankings").child("current");
        mCurrent_TRDs = new HashMap<>();
        mCurrentRankingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "current_ranking.onChildAdded: " + dataSnapshot.getKey());
                mCurrent_TRDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TRD.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "current_ranking.onChildChanged: " + dataSnapshot.getKey());
                mCurrent_TRDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TRD.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "current_ranking.onChildRemoved: " + dataSnapshot.getKey());
                mCurrent_TRDs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mPredictedRankingRef = mEventRef.child("rankings").child("predicted");
        mPredicted_TRDs = new HashMap<>();
        mPredictedRankingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "predicted_ranking.onChildAdded: " + dataSnapshot.getKey());
                mPredicted_TRDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TRD.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "predicted_ranking.onChildChanged: " + dataSnapshot.getKey());
                mPredicted_TRDs.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TRD.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "predicted_ranking.onChildRemoved: " + dataSnapshot.getKey());
                mPredicted_TRDs.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mFirstPickRef = mEventRef.child("first_pick");
        mFirst_TPA = new HashMap<>();
        mFirstPickRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "first_pick.onChildAdded: " + dataSnapshot.getKey());
                mFirst_TPA.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPA.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "first_pick.onChildChanged: " + dataSnapshot.getKey());
                mFirst_TPA.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPA.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "first_pick.onChildRemoved: " + dataSnapshot.getKey());
                mFirst_TPA.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mSecondPickRef = mEventRef.child("second_pick");
        mSecond_TPA = new HashMap<>();
        mSecondPickRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "second_pick.onChildAdded: " + dataSnapshot.getKey());
                mSecond_TPA.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPA.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "second_pick.onChildChanged: " + dataSnapshot.getKey());
                mSecond_TPA.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPA.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "second_pick.onChildRemoved: " + dataSnapshot.getKey());
                mSecond_TPA.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        mThirdPickRef = mEventRef.child("third_pick");
        mThird_TPA = new HashMap<>();
        mThirdPickRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "third_pick.onChildAdded: " + dataSnapshot.getKey());
                mThird_TPA.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPA.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "third_pick.onChildChanged: " + dataSnapshot.getKey());
                mThird_TPA.put(Integer.parseInt(dataSnapshot.getKey()), dataSnapshot.getValue(TPA.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "third_pick.onChildRemoved: " + dataSnapshot.getKey());
                mThird_TPA.remove(Integer.parseInt(dataSnapshot.getKey()));
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
        //endregion

        mEventKey = eventKey;
    }

    //region Schedule Data
    public void setMatch(Match match) {
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
    public void setTMD(TMD tmd)
    {
        mPartialMatchRef.child(String.format("%d_%d", tmd.match_number,tmd.team_number)).setValue(tmd);
    }

    public TMD getTMD(int match_number, int team_number)
    {
        return mTMDs.get(String.format("%d_%d", match_number, team_number));
    }
    //endregion

    //region Pit Scouting Data
    public void setTPD(TPD tpd)
    {
        mPitRef.child(String.format("%d", tpd.team_number)).setValue(tpd);
    }

    public TPD getTPD(int team_number)
    {
        return mTPDs.get(team_number);
    }
    //endregion

    //region Super Scouting Data
    public void setSMD(SMD smd) {
        mSuperRef.child(String.format("%d", smd.match_number)).setValue(smd);
    }

    public SMD getSMD(int match_number) {
        return mSMDs.get(String.format("%d",match_number));
    }

    public Map<Integer, SMD> getSMDs() { return  mSMDs; }
    //endregion

    //region Drive Team Feedback Data
    public void setTDTF(TDTF tdtf) {
        mFeedbackRef.child(String.format("%d", tdtf.team_number)).setValue(tdtf);
    }

    public TDTF getTDTF(int team_number)
    {
        return mTDTFs.get(String.format("%d", team_number));
    }

    public ArrayList<TDTF> getTDTFs() {
        return new ArrayList<>(mTDTFs.values());
    }
    //endregion

    //region Team Info
    public void setTID(TID tid)
    {
        mInfoRef.child(String.format("%d", tid.team_number)).setValue(tid);
    }

    public TID getTID(int team_number)
    {
        return mTIDs.get(team_number);
    }

    public void removeTID(int team_number){
        mInfoRef.child(String.format("%d", team_number)).removeValue();
    }
    //endregion

    //region Calculated data
    public void setTCD(TCD tcd)
    {
        mCalulatedRef.child(String.format("%d",tcd.team_number)).setValue(tcd);
    }

    public TCD getTCD(int team_number){ return mTCDs.get(team_number); }
    //endregion

    //region Pick List Data
    //region First Pick
    public void setFirstTPA(TPA first)
    {
        mFirstPickRef.child(String.valueOf(first.team_number)).setValue(first);
    }

    public TPA getFirstTPA(int team_number)
    {
        return mFirst_TPA.get(team_number);
    }
    //endregion

    //region Second Pick
    public void setSecondTPA(TPA second)
    {
        mSecondPickRef.child(String.valueOf(second.team_number)).setValue(second);
    }

    public TPA getSecondTPA(int team_number)
    {
        return mSecond_TPA.get(team_number);
    }
    //endregion

    //region Third Pick
    public void setThirdTPA(TPA third)
    {
        mThirdPickRef.child(String.valueOf(third.team_number)).setValue(third);
    }

    public TPA getThirdTPA(int team_number)
    {
        return mThird_TPA.get(team_number);
    }
    //endregion

    //region DNP
    public void setDNP(int team_number, boolean dnp)
    {
        mFirstPickRef.child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mSecondPickRef.child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mThirdPickRef.child(String.valueOf(team_number)).child("dnp").setValue(dnp);
    }

    public ArrayList<Integer> getDnpList()
    {
        ArrayList<Integer> teams = getTeamNumbers();
        // Remove all teams that are not set to do not pick
        for(int i = 0; i < teams.size(); i++)
        {
            if(!mFirst_TPA.get(teams.get(i)).dnp)
            {
                teams.remove(i);
                i--;
            }
        }
        return teams;
    }
    //endregion

    public void setPicked(int team_number, boolean picked)
    {
        mFirstPickRef.child(String.valueOf(team_number)).child("picked").setValue(picked);
        mSecondPickRef.child(String.valueOf(team_number)).child("picked").setValue(picked);
        mThirdPickRef.child(String.valueOf(team_number)).child("picked").setValue(picked);
    }
    //endregion

    //region Rankings
    //region Current Rankings
    public void setCurrentTRD(TRD trd)
    {
        mCurrentRankingRef.child(String.format("%d", trd.team_number)).setValue(trd);
    }

    public TRD getCurrentTRD(int team_number)
    {
        return mCurrent_TRDs.get(team_number);
    }

    public Map<Integer, TRD> getCurrentRankings()
    {
        return mCurrent_TRDs;
    }
    //endregion

    //region Predicted Rankings
    public void setPredictedTRD(TRD trd)
    {
        mPredictedRankingRef.child(String.format("%d", trd.team_number)).setValue(trd);
    }

    public TRD getPredictedTRD(int team_number)
    {
        return mPredicted_TRDs.get(team_number);
    }

    public Map<Integer, TRD> getPredictedRankings()
    {
        return mPredicted_TRDs;
    }
    //endregion
    //endregion

    //region All Team Data
    public Team getTeam(int team_number)
    {
        Team team = new Team();
        team.team_number = team_number;

        team.info = getTID(team_number);
        if(team.info == null)
        {
            team.info = new TID();
            team.info.team_number = team_number;
        }

        team.pit = getTPD(team_number);
        if(team.pit == null)
        {
            team.pit = new TPD();
            team.pit.team_number = team_number;
        }

        team.drive_team_feedback = getTDTF(team_number);
        if(team.drive_team_feedback == null)
        {
            team.drive_team_feedback = new TDTF();
            team.drive_team_feedback.team_number = team_number;
        }

        team.calc = getTCD(team_number);
        if(team.calc == null)
        {
            team.calc = new TCD();
            team.calc.team_number = team_number;
        }

        team.completed_matches = new HashMap<>();
        for(int match_number : team.info.match_numbers)
        {
            TMD tmd = getTMD(match_number, team_number);
            if(tmd != null) {
                team.completed_matches.put(match_number, tmd);
            }
        }

        team.current_ranking = getCurrentTRD(team_number);
        if(team.current_ranking == null)
        {
            team.current_ranking = new TRD();
            team.current_ranking.team_number = team_number;
        }

        team.predicted_ranking = getPredictedTRD(team_number);
        if(team.predicted_ranking == null)
        {
            team.predicted_ranking = new TRD();
            team.predicted_ranking.team_number = team_number;
        }

        team.first_pick = getFirstTPA(team_number);
        if(team.first_pick == null)
        {
            team.first_pick = new TPA();
            team.first_pick.team_number = team_number;
        }

        team.second_pick = getSecondTPA(team_number);
        if(team.second_pick == null)
        {
            team.second_pick = new TPA();
            team.second_pick.team_number = team_number;
        }

        team.third_pick = getThirdTPA(team_number);
        if(team.third_pick == null)
        {
            team.third_pick = new TPA();
            team.third_pick.team_number = team_number;
        }

        return team;
    }

    public void setTeam(Team team)
    {
        setTCD(team.calc);
        setTID(team.info);
        setTPD(team.pit);
        setTDTF(team.drive_team_feedback);
        for(TMD entry: team.completed_matches.values())
        {
            setTMD(entry);
        }
        setCurrentTRD(team.current_ranking);
        setPredictedTRD(team.predicted_ranking);
        setFirstTPA(team.first_pick);
        setSecondTPA(team.second_pick);
        setThirdTPA(team.third_pick);
    }

    public List<TMD> getCompletedMatches(int team_number)
    {
        Team team = getTeam(team_number);
        List<TMD> completedMatches = new ArrayList<>();
        for(int match_number: team.info.match_numbers)
        {
            TMD tim = getTMD(match_number, team_number);
            if(tim != null)
            {
                completedMatches.add(tim);
            }
        }

        return completedMatches;
    }
    //endregion

    //region Strategy
    public void setStrategy(Strategy strategy)
    {
        mStrategyRef.child(strategy.name).setValue(strategy);
    }

    public Strategy getStrategy(String strategy_name)
    {
        return mStrategies.get(strategy_name);
    }

    public ArrayList<Strategy> getStrategies()
    {
        return new ArrayList<>(mStrategies.values());
    }
    //endregion

    //region Notes
    public ArrayList<NoteView> getAllNotes()
    {
        ArrayList<NoteView> notes = new ArrayList<>();
        for(TMD tmd: mTMDs.values())
        {
            NoteView note = new NoteView();
            note.note_type = NoteView.NoteType.MATCH;
            note.match_number = tmd.match_number;
            note.team_number = tmd.team_number;
            note.note = tmd.notes;
            notes.add(note);
        }
        for(SMD smd: mSMDs.values())
        {
            NoteView note = new NoteView();
            note.note_type = NoteView.NoteType.SUPER;
            note.match_number = smd.match_number;
            note.team_number = -1;
            note.note = smd.notes;
            notes.add(note);
        }
        return notes;
    }
    //endregion

    //region Team Numbers
    public ArrayList<Integer> getTeamNumbers()
    {
        ArrayList<Integer> team_numbers = new ArrayList<>(mTPDs.keySet());
        Collections.sort(team_numbers);
        return team_numbers;
    }

    public int getTeamNumberBefore(int team_number)
    {
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

    public int getTeamNumberAfter(int team_number)
    {
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
