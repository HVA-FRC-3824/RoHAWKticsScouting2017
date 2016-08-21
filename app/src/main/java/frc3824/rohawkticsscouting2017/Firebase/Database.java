package frc3824.rohawkticsscouting2017.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TCD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TID;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;

/**
 * @author Andrew Messing
 * Created: 8/13/16
 *
 *
 */
public class Database {

    private final static String TAG = "Database";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRootRef;

    private DatabaseReference mEventRef;
    private DatabaseReference mScheduleRef;
    private DatabaseReference mPitRef;
    private DatabaseReference mSuperRef;
    private DatabaseReference mPartialMatchRef;
    private DatabaseReference mCalulatedRef;
    private DatabaseReference mInfoRef;
    private DatabaseReference mStrategyRef;

    private String mEventKey;

    private static Set<String> mEvents;
    private Map<Integer, Match> mSchedule;
    private Map<Integer, SMD> mSMDs;
    private Map<Integer, TPD> mTPDs;
    private Map<String, TMD> mTMDs;
    private Map<Integer, TID> mTIDs;
    private Map<Integer, TCD> mTCDs;
    private Map<String, Strategy> mStrategies;

    private static Database mSingleton;

    public static Database getInstance(String eventKey)
    {
        if(mSingleton == null)
        {
            mSingleton = new Database();
        }

        mSingleton.setEventKey(eventKey);
        return  mSingleton;
    }

    public static Database getInstance()
    {
        if(mSingleton == null)
        {
            mSingleton = new Database();
        }

        return  mSingleton;
    }

    private Database()
    {
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

    private void setEventKey(String eventKey)
    {
        if(eventKey == "" || mEventKey == eventKey)
            return;

        mEventRef = mRootRef.child(eventKey);

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

        mEventKey = eventKey;
    }

    /*
        Schedule Data
    */

    public void setMatch(Match match)
    {
        mScheduleRef.child(String.format("%d", match.match_number)).setValue(match);
    }

    public Match getMatch(int match_number)
    {
        return mSchedule.get(match_number);
    }

    public Map<Integer, Match> getSchedule()
    {
        return mSchedule;
    }

    public int getNumberOfMatches()
    {
        return mSchedule.size();
    }

    /*
        Match Scouting Data
    */

    public void setTMD(TMD tmd)
    {
        mPartialMatchRef.child(String.format("%d_%d", tmd.match_number,tmd.team_number)).setValue(tmd);
    }

    public TMD getTMD(int match_number, int team_number)
    {
        return mTMDs.get(String.format("%d_%d", match_number, team_number));
    }

    /*
        Pit Scouting Data
    */
    public void setTPD(TPD tpd)
    {
        mPitRef.child(String.format("%d", tpd.team_number)).setValue(tpd);
    }

    public TPD getTPD(int team_number)
    {
        return mTPDs.get(team_number);
    }

    /*
        Super Scouting Data
    */

    public void setSMD(SMD smd)
    {
        mSuperRef.child(String.format("%d", smd.match_number)).setValue(smd);
    }

    public SMD getSMD(int match_number)
    {
        return mSMDs.get(String.format("M%d",match_number));
    }

    /*
        Team Info
    */

    public void setTID(TID tid)
    {
        mInfoRef.child(String.format("%d", tid.team_number)).setValue(tid);
    }

    public TID getTID(int team_number)
    {
        return mTIDs.get(team_number);
    }

    /*
        Calculated data
    */
    public void setTCD(TCD tcd)
    {
        mCalulatedRef.child(String.format("%d",tcd.team_number)).setValue(tcd);
    }

    public TCD getTCD(int team_number){ return mTCDs.get(team_number); }

    public void setTeam(Team team)
    {
        setTCD(team.calc);
        setTID(team.info);
        setTPD(team.pit);
        for(Map.Entry entry: team.completed_matches.entrySet())
        {
            setTMD((TMD)entry.getValue());
        }
    }

    public Team getTeam(int team_number)
    {
        Team team = new Team();
        team.team_number = team_number;
        team.info = getTID(team_number);
        team.pit = getTPD(team_number);
        team.calc = getTCD(team_number);
        for(int match_number : team.info.match_numbers)
        {
            TMD tmd = getTMD(match_number, team_number);
            if(tmd != null) {
                team.completed_matches.put(match_number, tmd);
            }
        }

        return team;
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

    public ArrayList<Integer> getTeamNumbers()
    {
        return new ArrayList<>(mTPDs.keySet());
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
}
