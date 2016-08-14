package frc3824.rohawkticsscouting2017.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatch;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamInMatch;

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
    private DatabaseReference mPartialMatchRef;
    private DatabaseReference mSuperMatchRef;
    private DatabaseReference mTeamRef;

    private String mEventId;

    private List<String> mEvents;
    private Map<String, Match> mSchedule;
    private Map<String, TeamInMatch> mPartialMatches;
    private Map<String, SuperMatch> mSuperMatches;
    private Map<String, Team> mTeams;

    private static Database mSingleton;

    public static Database getInstance(String eventId)
    {
        if(mSingleton == null)
        {
            mSingleton = new Database();
        }

        mSingleton.setEventId(eventId);
        return  mSingleton;
    }

    private Database()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mRootRef = mFirebaseDatabase.getReference();

        //Root reference's children are the events
        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey());
                mEvents.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: " + dataSnapshot.getKey());
                mEvents.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });
    }

    private void setEventId(String eventId)
    {
        if(mEventId == eventId)
            return;

        mEventRef = mRootRef.child(eventId);

        mScheduleRef = mEventRef.child("schedule");
        mSchedule = new HashMap<>();
        mScheduleRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "schedule.onChildAdded: " + dataSnapshot.getKey());
                mSchedule.put(dataSnapshot.getKey(), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "schedule.onChildChanged: " + dataSnapshot.getKey());
                mSchedule.put(dataSnapshot.getKey(), dataSnapshot.getValue(Match.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "schedule.onChildRemoved: " + dataSnapshot.getKey());
                mSchedule.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "schedule.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "schedule.onCancelled");
            }
        });

        mPartialMatchRef = mEventRef.child("partial_match");
        mPartialMatches = new HashMap<>();
        mPartialMatchRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "partial_match.onChildAdded: " + dataSnapshot.getKey());
                mPartialMatches.put(dataSnapshot.getKey(),dataSnapshot.getValue(TeamInMatch.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "partial_match.onChildChanged: " + dataSnapshot.getKey());
                mPartialMatches.put(dataSnapshot.getKey(),dataSnapshot.getValue(TeamInMatch.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "partial_match.onChildRemoved: " + dataSnapshot.getKey());
                mPartialMatches.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "partial_match.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "partial_match.onCancelled");
            }
        });

        mSuperMatchRef = mEventRef.child("super_match");
        mSuperMatches = new HashMap<>();
        mSuperMatchRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "super_match.onChildAdded: " + dataSnapshot.getKey());
                mSuperMatches.put(dataSnapshot.getKey(), dataSnapshot.getValue(SuperMatch.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "super_match.onChildChanged: " + dataSnapshot.getKey());
                mSuperMatches.put(dataSnapshot.getKey(), dataSnapshot.getValue(SuperMatch.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "super_match.onChildRemoved: " + dataSnapshot.getKey());
                mSuperMatches.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "super_match.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "super_match.onCancelled: ");
            }
        });

        mTeamRef = mEventRef.child("teams");
        mTeams = new HashMap<>();
        mTeamRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "teams.onChildAdded: " + dataSnapshot.getKey());
                mTeams.put(dataSnapshot.getKey(), dataSnapshot.getValue(Team.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "teams.onChildAdded: " + dataSnapshot.getKey());
                mTeams.put(dataSnapshot.getKey(), dataSnapshot.getValue(Team.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "teams.onChildRemoved: " + dataSnapshot.getKey());
                mTeams.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "teams.onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "teams.onCancelled");
            }
        });
    }

    /*
        Schedule Data
    */

    public void setMatch(Match match)
    {
        mScheduleRef.child(String.format("M%d", match.match_number)).setValue(match);
    }

    public Match getMatch(int match_number)
    {
        return mSchedule.get(String.format("M%d", match_number));
    }

    public Map<String, Match> getSchedule()
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

    public void setTeamInMatch(TeamInMatch tim)
    {
        mPartialMatchRef.child(String.format("M%d_T%d", tim.match_number, tim.team_number)).setValue(tim);
    }

    public TeamInMatch getTeamInMatch(int match_number, int team_number)
    {
        return mPartialMatches.get(String.format("M%d_T%d", match_number, team_number));
    }

    /*
        Super Scouting Data
    */

    public void setSuperMatch(SuperMatch sm)
    {
        mSuperMatchRef.child(String.format("M%d", sm.match_number)).setValue(sm);
    }

    public SuperMatch getSuperMatch(int match_number)
    {
        return mSuperMatches.get(String.format("M%d",match_number));
    }

    /*
        Team, Pit, and Calculated Data
    */

    public void setTeam(Team team)
    {
        mTeamRef.child(String.format("T%d", team.team_number)).setValue(team);
    }

    public Team getTeam(int team_number)
    {
        return mTeams.get(String.format("T%d", team_number));
    }
}
