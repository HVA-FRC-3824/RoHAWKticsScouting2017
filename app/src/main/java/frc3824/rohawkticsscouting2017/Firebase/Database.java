package frc3824.rohawkticsscouting2017.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.MatchPilotData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.StrategySuggestion;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamCalculatedData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamDTFeedback;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamLogistics;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPilotData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamQualitativeData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamRankingData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * The Database class handles all data and connecting with the Firebase system
 */
public class Database {

    private final static String TAG = "Database";

    private FirebaseDatabase mFirebaseDatabase;

    private ArrayList< DatabaseReference> mReferences;

    private String mEventKey;

    //region Maps
    private static Set<String> mEvents;
    private ArrayList< Map<String, DataSnapshot> > mMaps;
    //endregion

    public enum PickType {
        FIRST,
        SECOND,
        THIRD
    }

    public enum RankingType {
        CURRENT,
        PREDICTED
    }

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
        mReferences = new ArrayList<>();
        mReferences.add(mFirebaseDatabase.getReference());


        mEvents = new HashSet<>();

        //Root reference's children are the events
        mReferences.get(Constants.Database_Lists.indices.ROOT).addChildEventListener(new ChildEventListener() {
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

        DatabaseReference root = mReferences.get(0);
        // Remove all references except root
        mReferences.clear();
        mReferences = new ArrayList<>();
        mReferences.add(root);
        mReferences.add(root.child(eventKey));
        if(mMaps != null) {
            mMaps.clear();
        }
        mMaps = new ArrayList<>();
        // Placeholders for indices
        mMaps.add(new HashMap<String, DataSnapshot>());
        mMaps.add(new HashMap<String, DataSnapshot>());

        for(int i = 2; i < Constants.Database_Lists.indices.TOTAL_REFERENCES; i++){
            Log.v(TAG, String.format("%d %s", i, Constants.Database_Lists.children.LIST[i]));
            mMaps.add(new HashMap<String, DataSnapshot>());
            mReferences.add(mReferences.get(Constants.Database_Lists.indices.EVENT).child(Constants.Database_Lists.children.LIST[i]));
            final int j = i;
            mReferences.get(i).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.v(TAG, String.format("%s.onChildAdded: %s", Constants.Database_Lists.children.LIST[j], dataSnapshot.getKey()));
                    mMaps.get(j).put(dataSnapshot.getKey(), dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.v(TAG, String.format("%s.onChildChanged: %s", Constants.Database_Lists.children.LIST[j], dataSnapshot.getKey()));
                    mMaps.get(j).put(dataSnapshot.getKey(), dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.v(TAG, String.format("%s.onChildRemoved: %s", Constants.Database_Lists.children.LIST[j], dataSnapshot.getKey()));
                    mMaps.get(j).remove(dataSnapshot.getKey());

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.v(TAG, String.format("%s.onChildMoved: %s", Constants.Database_Lists.children.LIST[j], dataSnapshot.getKey()));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v(TAG, String.format("%s.onCancelled", Constants.Database_Lists.children.LIST[j]));

                }
            });
        }

        mEventKey = eventKey;
    }

    //region Schedule Data
    public void setMatch(Match match) {
        match.last_modified = System.currentTimeMillis();
        mReferences.get(Constants.Database_Lists.indices.SCHEDULE).child(String.valueOf(match.match_number)).setValue(match);
    }

    public Match getMatch(int match_number) {
        return mMaps.get(Constants.Database_Lists.indices.SCHEDULE).get(String.valueOf(match_number)).getValue(Match.class);
    }

    public void removeMatch(int match_number){
        mReferences.get(Constants.Database_Lists.indices.SCHEDULE).child(String.valueOf(match_number)).removeValue();
    }

    public Map<Integer, Match> getSchedule() {
        Map<Integer, Match> schedule = new HashMap<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.SCHEDULE).entrySet()){
            schedule.put(Integer.parseInt(entry.getKey()), entry.getValue().getValue(Match.class));
        }
        return schedule;
    }

    public int getNumberOfMatches() {
        return mMaps.get(Constants.Database_Lists.indices.SCHEDULE).size();
    }
    //endregion

    //region Match Scouting Data
    public void setTeamMatchData(TeamMatchData teamMatchData) {
        mReferences.get(Constants.Database_Lists.indices.MATCH).child(String.format("%d_%d", teamMatchData.match_number, teamMatchData.team_number)).setValue(teamMatchData);
    }

    public TeamMatchData getTeamMatchData(int match_number, int team_number) {
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.MATCH).get(String.format("%d_%d", match_number, team_number));
        if(d == null){
            return null;
        }
        return d.getValue(TeamMatchData.class);
    }

    public ArrayList<TeamMatchData> getAllTeamMatchData(){
        ArrayList<TeamMatchData> all = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.MATCH).entrySet()){
            all.add(entry.getValue().getValue(TeamMatchData.class));
        }
        return all;
    }
    //endregion

    //region Pit Scouting Data
    public void setTeamPitData(TeamPitData teamPitData) {
        teamPitData.last_modified = System.currentTimeMillis();
        mReferences.get(Constants.Database_Lists.indices.PIT).child(String.valueOf(teamPitData.team_number)).setValue(teamPitData);
    }

    public TeamPitData getTeamPitData(int team_number) {
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.PIT).get(String.valueOf(team_number));
        if(d == null){
            return null;
        }
        return d.getValue(TeamPitData.class);
    }

    public ArrayList<TeamPitData> getAllTeamPitData(){
        ArrayList<TeamPitData> pits = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.PIT).entrySet()){
            pits.add(entry.getValue().getValue(TeamPitData.class));
        }
        return pits;
    }
    //endregion

    //region Super Scouting Data
    public void setSuperMatchData(SuperMatchData superMatchData) {
        superMatchData.last_modified = System.currentTimeMillis();
        mReferences.get(Constants.Database_Lists.indices.SUPER).child(String.valueOf(superMatchData.match_number)).setValue(superMatchData);
    }

    public SuperMatchData getSuperMatchData(int match_number) {
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.SUPER).get(String.valueOf(match_number));
        if(d == null){
            return null;
        }
        return d.getValue(SuperMatchData.class);
    }

    public ArrayList<SuperMatchData> getAllSuperMatchData() {
        ArrayList<SuperMatchData> supers = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.SUPER).entrySet()){
            supers.add(entry.getValue().getValue(SuperMatchData.class));
        }
        return supers;
    }
    //endregion

    //region Drive Team Feedback Data
    public void setTeamDTFeedback(TeamDTFeedback teamDTFeedback) {
        teamDTFeedback.last_modified = System.currentTimeMillis();
        mReferences.get(Constants.Database_Lists.indices.DT).child(String.valueOf(teamDTFeedback.team_number)).setValue(teamDTFeedback);
    }

    public TeamDTFeedback getTeamDTFeedback(int team_number) {
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.DT).get(String.valueOf(team_number));
        if(d == null){
            return null;
        }
        return d.getValue(TeamDTFeedback.class);
    }

    public ArrayList<TeamDTFeedback> getAllTeamDTFeedbacks() {
        ArrayList<TeamDTFeedback> dts = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.DT).entrySet()){
            dts.add(entry.getValue().getValue(TeamDTFeedback.class));
        }
        return dts;
    }
    //endregion

    //region Team Logistics
    public void setTeamLogistics(TeamLogistics teamLogistics) {
        teamLogistics.last_modified = System.currentTimeMillis();
        mReferences.get(Constants.Database_Lists.indices.LOGISTICS).child(String.valueOf(teamLogistics.team_number)).setValue(teamLogistics);
    }

    public TeamLogistics getTeamLogistics(int team_number) {
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.LOGISTICS).get(String.valueOf(team_number));
        if(d == null){
            return null;
        }
        TeamLogistics tl = d.getValue(TeamLogistics.class);

        Collections.sort(tl.match_numbers);
        return tl;
    }

    public void removeTeamLogistics(int team_number){
        mReferences.get(Constants.Database_Lists.indices.LOGISTICS).child(String.valueOf(team_number)).removeValue();
    }
    //endregion

    //region Calculated data
    public TeamCalculatedData getTeamCalculatedData(int team_number){
        Log.v(TAG, Constants.Database_Lists.children.LIST[Constants.Database_Lists.indices.CALC]);

        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.CALC).get(String.valueOf(team_number));
        if(d == null){
            return null;
        }
        return d.getValue(TeamCalculatedData.class);
    }
    //endregion

    //region Qualitative
    public TeamQualitativeData getTeamQualitativeData(int team_number){
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.QUALITATIVE).get(String.valueOf(team_number));
        if(d == null){
            return null;
        }
        return d.getValue(TeamQualitativeData.class);
    }
    //endregion

    //region Pilot Data
    public MatchPilotData getMatchPilotData(int match_number){
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.MATCH_PILOT).get(String.valueOf(match_number));
        if(d == null){
            return null;
        }
        return d.getValue(MatchPilotData.class);
    }

    public TeamPilotData getTeamPilotData(int team_number){
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.TEAM_PILOT).get(String.valueOf(team_number));
        if(d == null){
            return null;
        }
        return d.getValue(TeamPilotData.class);
    }
    //endregion

    //region Pick List Data

    public TeamPickAbility getTeamPickAbility(int team_number, PickType pt){
        DataSnapshot d = null;

        switch (pt){
            case FIRST:
                 d = mMaps.get(Constants.Database_Lists.indices.FIRST_PICK).get(String.valueOf(team_number));
                break;
            case SECOND:
                d = mMaps.get(Constants.Database_Lists.indices.SECOND_PICK).get(String.valueOf(team_number));
                break;
            case THIRD:
                d = mMaps.get(Constants.Database_Lists.indices.THIRD_PICK).get(String.valueOf(team_number));
                break;
        }

        if(d == null){
            return null;
        }

        return d.getValue(TeamPickAbility.class);
    }

    public void setTeamPickAbility(TeamPickAbility teamPickAbility, PickType pt){
        switch (pt){
            case FIRST:
                mReferences.get(Constants.Database_Lists.indices.FIRST_PICK).child(String.valueOf(teamPickAbility.team_number)).setValue(teamPickAbility);
                break;
            case SECOND:
                mReferences.get(Constants.Database_Lists.indices.SECOND_PICK).child(String.valueOf(teamPickAbility.team_number)).setValue(teamPickAbility);
                break;
            case THIRD:
                mReferences.get(Constants.Database_Lists.indices.THIRD_PICK).child(String.valueOf(teamPickAbility.team_number)).setValue(teamPickAbility);
                break;
        }
    }

    //region DNP
    public void setDNP(int team_number, boolean dnp) {
        long last_modified = System.currentTimeMillis();
        mReferences.get(Constants.Database_Lists.indices.FIRST_PICK).child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mReferences.get(Constants.Database_Lists.indices.FIRST_PICK).child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mReferences.get(Constants.Database_Lists.indices.SECOND_PICK).child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mReferences.get(Constants.Database_Lists.indices.SECOND_PICK).child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mReferences.get(Constants.Database_Lists.indices.THIRD_PICK).child(String.valueOf(team_number)).child("dnp").setValue(dnp);
        mReferences.get(Constants.Database_Lists.indices.THIRD_PICK).child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
    }

    public ArrayList<Integer> getDnpList() {
        ArrayList<Integer> teams = getTeamNumbers();
        // Remove all teams that are not set to do not pick
        for(int i = 0; i < teams.size(); i++)
        {
            DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.FIRST_PICK).get(String.valueOf(teams.get(i)));
            if(!d.getValue(TeamPickAbility.class).dnp)
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
        mReferences.get(Constants.Database_Lists.indices.FIRST_PICK).child(String.valueOf(team_number)).child("picked").setValue(picked);
        mReferences.get(Constants.Database_Lists.indices.FIRST_PICK).child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mReferences.get(Constants.Database_Lists.indices.SECOND_PICK).child(String.valueOf(team_number)).child("picked").setValue(picked);
        mReferences.get(Constants.Database_Lists.indices.SECOND_PICK).child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
        mReferences.get(Constants.Database_Lists.indices.THIRD_PICK).child(String.valueOf(team_number)).child("picked").setValue(picked);
        mReferences.get(Constants.Database_Lists.indices.THIRD_PICK).child(String.valueOf(team_number)).child("last_modified").setValue(last_modified);
    }
    //endregion

    //region Rankings
    public TeamRankingData getTeamRankingData(int team_number, RankingType rt){
        DataSnapshot d = null;

        switch (rt){
            case CURRENT:
                d = mMaps.get(Constants.Database_Lists.indices.CURRENT_RANKING).get(String.valueOf(team_number));
                break;
            case PREDICTED:
                d = mMaps.get(Constants.Database_Lists.indices.PREDICTED_RANKING).get(String.valueOf(team_number));
                break;
        }

        if(d == null){
            return null;
        }

        return d.getValue(TeamRankingData.class);
    }

    public ArrayList<TeamRankingData> getAllTeamRankingData(RankingType rt){
        ArrayList<TeamRankingData> trds = new ArrayList<>();
        switch (rt){
            case CURRENT:
                for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.CURRENT_RANKING).entrySet()){
                    trds.add(entry.getValue().getValue(TeamRankingData.class));
                }
                break;
            case PREDICTED:
                for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.PREDICTED_RANKING).entrySet()){
                    trds.add(entry.getValue().getValue(TeamRankingData.class));
                }
                break;
        }

        return trds;
    }
    //endregion

    //region All Team Data
    public Team getTeam(int team_number) {
        Team team = new Team();
        team.team_number = team_number;

        team.info = getTeamLogistics(team_number);
        if(team.info == null) {
            team.info = new TeamLogistics();
            team.info.team_number = team_number;
            team.info.match_numbers = new ArrayList<>();
        } else if(team.info.match_numbers != null){
            Collections.sort(team.info.match_numbers);
        }

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

        team.current_ranking = getTeamRankingData(team_number, RankingType.CURRENT);
        if(team.current_ranking == null)
        {
            team.current_ranking = new TeamRankingData();
            team.current_ranking.team_number = team_number;
        }

        team.predicted_ranking = getTeamRankingData(team_number, RankingType.PREDICTED);
        if(team.predicted_ranking == null)
        {
            team.predicted_ranking = new TeamRankingData();
            team.predicted_ranking.team_number = team_number;
        }

        team.first_pick = getTeamPickAbility(team_number, PickType.FIRST);
        if(team.first_pick == null)
        {
            team.first_pick = new TeamPickAbility();
            team.first_pick.team_number = team_number;
        }

        team.second_pick = getTeamPickAbility(team_number, PickType.SECOND);
        if(team.second_pick == null)
        {
            team.second_pick = new TeamPickAbility();
            team.second_pick.team_number = team_number;
        }

        team.third_pick = getTeamPickAbility(team_number, PickType.THIRD);
        if(team.third_pick == null)
        {
            team.third_pick = new TeamPickAbility();
            team.third_pick.team_number = team_number;
        }

        team.pilot = getTeamPilotData(team_number);
        if(team.pilot == null){
            team.pilot = new TeamPilotData();
            team.pilot.team_number = team_number;
        }

        team.qualitative = getTeamQualitativeData(team_number);
        if(team.qualitative == null) {
            team.qualitative = new TeamQualitativeData();
            team.qualitative.team_number = team_number;
        }

        return team;
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
        mReferences.get(Constants.Database_Lists.indices.STRATEGY).child(strategy.name).setValue(strategy);
    }

    public Strategy getStrategy(String strategy_name) {
        return mMaps.get(Constants.Database_Lists.indices.STRATEGY).get(strategy_name).getValue(Strategy.class);
    }

    public ArrayList<Strategy> getAllStrategies() {
        ArrayList<Strategy> strategies = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.STRATEGY).entrySet()){
            strategies.add(entry.getValue().getValue(Strategy.class));
        }
        return strategies;
    }
    //endregion

    //region StrategySuggestion
    public void setStrategySuggestion(StrategySuggestion strategySuggestion){
        mReferences.get(Constants.Database_Lists.indices.SUGGESTION).child(strategySuggestion.key).setValue(strategySuggestion);
    }

    public StrategySuggestion getStrategySuggestion(String key){
        DataSnapshot d = mMaps.get(Constants.Database_Lists.indices.SUGGESTION).get(key);
        if(d == null){
            return null;
        }
        return d.getValue(StrategySuggestion.class);
    }

    public ArrayList<StrategySuggestion> getAllStrategySuggestions(){
        ArrayList<StrategySuggestion> suggestions = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.SUGGESTION).entrySet()){
            suggestions.add(entry.getValue().getValue(StrategySuggestion.class));
        }
        return suggestions;
    }
    //endregion

    //region Notes
    public ArrayList<NoteView> getAllNotes() {
        ArrayList<NoteView> notes = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.MATCH).entrySet())
        {
            TeamMatchData teamMatchData = entry.getValue().getValue(TeamMatchData.class);

            NoteView note = new NoteView();
            note.note_type = NoteView.NoteType.MATCH;
            note.match_number = teamMatchData.match_number;
            note.team_number = teamMatchData.team_number;
            note.note = teamMatchData.notes;
            note.tags = new HashMap();
            note.tags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BLOCK_SHOTS, teamMatchData.tags_blocked_shots);
            note.tags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.PINNED_ROBOT, teamMatchData.tags_pinned_robot);
            note.tags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_LOADING_STATION, teamMatchData.tags_defended_loading_station);
            note.tags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DEFENDED_AIRSHIP, teamMatchData.tags_defended_airship);
            note.tags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.BROKE, teamMatchData.tags_broke);
            note.tags.put(Constants.Match_Scouting.PostMatch.TAGS + Constants.Match_Scouting.PostMatch.Tags.DUMPED_ALL_HOPPERS, teamMatchData.tags_dumped_all_hoppers);

            notes.add(note);
        }
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.SUPER).entrySet())
        {
            SuperMatchData superMatchData = entry.getValue().getValue(SuperMatchData.class);
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
    public ScoutAccuracy getScoutAccuracy(String name){
        return mMaps.get(Constants.Database_Lists.indices.SCOUT_ACCURACY).get(name).getValue(ScoutAccuracy.class);
    }

    public ArrayList<ScoutAccuracy> getScoutAccuracies(){
        ArrayList<ScoutAccuracy> accuracies = new ArrayList<>();
        for(Map.Entry<String, DataSnapshot> entry: mMaps.get(Constants.Database_Lists.indices.SCOUT_ACCURACY).entrySet()){
            accuracies.add(entry.getValue().getValue(ScoutAccuracy.class));
        }
        return accuracies;
    }

    public ArrayList<String> getScoutNames(){
        return new ArrayList<>(mMaps.get(Constants.Database_Lists.indices.SCOUT_ACCURACY).keySet());
    }
    //endregion

    //region Team Numbers
    public ArrayList<Integer> getTeamNumbers() {
        ArrayList<Integer> team_numbers = new ArrayList<>();
        for(String key: mMaps.get(Constants.Database_Lists.indices.LOGISTICS).keySet()) {
            team_numbers.add(Integer.parseInt(key));
        }
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
