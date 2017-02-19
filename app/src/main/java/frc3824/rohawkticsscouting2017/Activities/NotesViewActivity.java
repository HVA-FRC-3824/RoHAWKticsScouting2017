package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_NotesView;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;
import frc3824.rohawkticsscouting2017.Views.ImageTextButton;
import frc3824.rohawkticsscouting2017.Views.NoteCriteria.NoteCriteriaContent;
import frc3824.rohawkticsscouting2017.Views.NoteCriteria.NoteCriteriaNumber;

/**
 * @author frc3824
 * Created: 9/3/16
 *
 * Activity for filtering through notes
 */

//TODO: create add/or for content criteria
public class NotesViewActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "NotesViewActivity";

    //region variables
    private boolean mSearchBasic;

    private RelativeLayout mBasicSearch;
    private ImageTextButton mBasicSearchOpen;
    private RadioGroup mBasicSearchType;
    private EditText mBasicSearchEditText;
    private Button mBasicSearchButton;

    private LinearLayout mAdvancedSearch;
    private ImageTextButton mAdvancedSearchOpen;
    private LinearLayout mAdvancedSearchMatchNumberCriteria;
    private ImageTextButton mAdvancedSearchMatchNumberCriteriaAdd;
    private LinearLayout mAdvancedSearchTeamNumberCriteria;
    private ImageTextButton mAdvancedSearchTeamNumberCriteriaAdd;
    private LinearLayout mAdvancedSearchContentCriteria;
    private ImageTextButton mAdvancedSearchContentCriteriaAdd;
    private LinearLayout mAdvancedSearchTagsCriteria;
    private ImageTextButton mAdvancedSearchTagsCriteriaAdd;
    private CheckBox mMatchNotesCheckbox;
    private CheckBox mSuperNotesCheckbox;
    private CheckBox mDriveTeamNotesCheckbox;
    private ImageTextButton mAdvancedSearchButton;

    private ListView mListView;
    private LVA_NotesView mAdapter;
    private ArrayList<NoteView> mAllNotes;
    private ArrayList<NoteView> mFilteredNotes;

    private LayoutInflater mLayoutInflator;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        //region Basic Search Find Views
        mBasicSearch = (RelativeLayout)findViewById(R.id.basic_search);
        mBasicSearchOpen = (ImageTextButton)findViewById(R.id.basic_search_open);
        mBasicSearchOpen.setOnClickListener(this);
        mBasicSearchType = (RadioGroup)findViewById(R.id.basic_search_type);
        mBasicSearchType.check(R.id.basic_match_number_option);
        mBasicSearchType.setOnCheckedChangeListener(this);
        mBasicSearchEditText = (EditText)findViewById(R.id.basic_search_edittext);
        mBasicSearchButton = (Button)findViewById(R.id.basic_search_button);
        mBasicSearchButton.setOnClickListener(this);
        //endregion

        //region Advanced Search Find Views
        mAdvancedSearch = (LinearLayout)findViewById(R.id.advanced_search);
        mAdvancedSearchOpen = (ImageTextButton)findViewById(R.id.advanced_search_open);
        mAdvancedSearchOpen.setOnClickListener(this);
        mAdvancedSearchMatchNumberCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_match_number_criteria);
        mAdvancedSearchMatchNumberCriteriaAdd = (ImageTextButton)mAdvancedSearchMatchNumberCriteria.findViewById(R.id.advanced_match_number_criteria_add);
        mAdvancedSearchMatchNumberCriteriaAdd.setOnClickListener(this);
        mAdvancedSearchTeamNumberCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_team_number_criteria);
        mAdvancedSearchTeamNumberCriteriaAdd = (ImageTextButton)mAdvancedSearchTeamNumberCriteria.findViewById(R.id.advanced_team_number_criteria_add);
        mAdvancedSearchTeamNumberCriteriaAdd.setOnClickListener(this);
        mAdvancedSearchContentCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_content_criteria);
        mAdvancedSearchContentCriteriaAdd = (ImageTextButton)mAdvancedSearchContentCriteria.findViewById(R.id.advanced_content_criteria_add);
        mAdvancedSearchContentCriteriaAdd.setOnClickListener(this);
        mAdvancedSearchTagsCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_tags_criteria);
        mAdvancedSearchTagsCriteriaAdd = (ImageTextButton)mAdvancedSearchTagsCriteria.findViewById(R.id.advanced_tags_criteria_add);
        mAdvancedSearchTagsCriteriaAdd.setOnClickListener(this);
        mMatchNotesCheckbox = (CheckBox)mAdvancedSearch.findViewById(R.id.match_notes_checkbox);
        mSuperNotesCheckbox = (CheckBox)mAdvancedSearch.findViewById(R.id.super_notes_checkbox);
        mDriveTeamNotesCheckbox = (CheckBox)mAdvancedSearch.findViewById(R.id.drive_team_notes_checkbox);
        mAdvancedSearchButton = (ImageTextButton)mAdvancedSearch.findViewById(R.id.advanced_search_button);
        mAdvancedSearchButton.setOnClickListener(this);
        //endregion

        //region list header setup
        ((TextView)(findViewById(R.id.header).findViewById(R.id.note_type))).setText("Note Type");
        ((TextView)(findViewById(R.id.header).findViewById(R.id.match_number))).setText("Match Number");
        ((TextView)(findViewById(R.id.header).findViewById(R.id.team_number))).setText("Team Number");
        ((TextView)(findViewById(R.id.header).findViewById(R.id.note))).setText("Note");
        //endregion

        mLayoutInflator = getLayoutInflater();

        mListView = (ListView)findViewById(R.id.list_view);

        Database database = Database.getInstance();
        mAllNotes = database.getAllNotes();
        mFilteredNotes = new ArrayList<>(mAllNotes);
        mAdapter = new LVA_NotesView(this, mFilteredNotes);
        mListView.setAdapter(mAdapter);

        Utilities.setupUi(this, findViewById(android.R.id.content));
        mSearchBasic = true;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.basic_search_open:
                if(!mSearchBasic) {
                    clearFilters();
                    //region Basic Search Open
                    mBasicSearch.setVisibility(View.VISIBLE);
                    mAdvancedSearch.setVisibility(View.GONE);
                    mBasicSearchOpen.setImage(R.drawable.expand_color);
                    mAdvancedSearchOpen.setImage(R.drawable.collapse_color);
                    // Remove all the advanced search views that are no longer needed
                    for (int i = 0; i < mAdvancedSearchMatchNumberCriteria.getChildCount() - 1; i++) {
                        mAdvancedSearchMatchNumberCriteria.removeViewAt(0);
                    }
                    for (int i = 0; i < mAdvancedSearchTeamNumberCriteria.getChildCount() - 1; i++) {
                        mAdvancedSearchTeamNumberCriteria.removeViewAt(0);
                    }
                    for (int i = 0; i < mAdvancedSearchContentCriteria.getChildCount() - 1; i++) {
                        mAdvancedSearchContentCriteria.removeViewAt(0);
                    }
                    //endregion
                    mSearchBasic = true;
                }
                break;
            case R.id.advanced_search_open:
                if(mSearchBasic) {
                    clearFilters();
                    //region Advanced Search Open
                    mBasicSearch.setVisibility(View.GONE);
                    mAdvancedSearch.setVisibility(View.VISIBLE);
                    mBasicSearchOpen.setImage(R.drawable.collapse_color);
                    mAdvancedSearchOpen.setImage(R.drawable.expand_color);
                    //endregion
                    mSearchBasic = false;
                }
                break;
            case R.id.basic_search_button:
                basicSearch();
                break;
            case R.id.advanced_match_number_criteria_add:
                mAdvancedSearchMatchNumberCriteria.addView(new NoteCriteriaNumber(this), 0);
                break;
            case R.id.advanced_team_number_criteria_add:
                mAdvancedSearchTeamNumberCriteria.addView(new NoteCriteriaNumber(this), 0);
                break;
            case R.id.advanced_content_criteria_add:
                mAdvancedSearchContentCriteria.addView(new NoteCriteriaContent(this), 0);
                break;
            case R.id.advanced_tags_criteria_add:
                mAdvancedSearchTagsCriteria.addView(new NoteCriteriaContent(this), 0);
                break;
            case R.id.advanced_search_button:
                advancedSearch();
                break;
        }
    }

    private void clearFilters(){
        mFilteredNotes = new ArrayList<>(mAllNotes);
        mAdapter = new LVA_NotesView(this, mFilteredNotes);
        mListView.setAdapter(mAdapter);
    }

    private void basicSearch(){
        Log.d(TAG, mBasicSearchEditText.getText().toString());
        if(mBasicSearchEditText.getText().toString().equals("")){
            clearFilters();
            return;
        }

        int search_type = -1;
        int number = -1;
        String content = "";

        switch (mBasicSearchType.getCheckedRadioButtonId()) {
            case R.id.basic_match_number_option:
                search_type = 0;
                try {
                    number = Integer.parseInt(mBasicSearchEditText.getText().toString());
                } catch (NumberFormatException e){
                    return;
                }
                break;
            case R.id.basic_team_number_option:
                search_type = 1;
                try {
                    number = Integer.parseInt(mBasicSearchEditText.getText().toString());
                } catch (NumberFormatException e){
                    return;
                }
                break;
            case R.id.basic_content_option:
                search_type = 2;
                content = mBasicSearchEditText.getText().toString();
                break;
            default:
                assert(false);
        }

        mFilteredNotes = new ArrayList<>(mAllNotes);
        for(int i = 0; i < mFilteredNotes.size(); i++) {
            NoteView nv = mFilteredNotes.get(i);
            switch (search_type){
                case 0:
                    if(nv.match_number != number){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
                case 1:
                    if(nv.team_number != number){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
                case 2:
                    if(!nv.note.contains(content)){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
                case 3:
                    if(!nv.tags.contains(content)){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
            }
        }
        mAdapter = new LVA_NotesView(this, mFilteredNotes);
        mListView.setAdapter(mAdapter);
    }

    private void advancedSearch() {
        cleanGoneViews();

        ArrayList<Integer[]> matchRanges = getMatchRanges();
        ArrayList<Integer[]> teamRanges = getTeamRanges();

        ArrayList<String> contains = new ArrayList<>();
        ArrayList<String> dnContains = new ArrayList<>();
        for(int i = 0; i < mAdvancedSearchContentCriteria.getChildCount() - 1; i++) {
            NoteCriteriaContent ncc = ((NoteCriteriaContent)mAdvancedSearchContentCriteria.getChildAt(i));
            if(ncc.getType() == 0) {
                contains.add(ncc.getContent());
            } else {
                dnContains.add(ncc.getContent());
            }
        }

        ArrayList<String> hasTag = new ArrayList<>();
        ArrayList<String> dnHaveTag = new ArrayList<>();

        for(int i = 0; i < mAdvancedSearchTagsCriteria.getChildCount() - 1; i++) {
            NoteCriteriaTags ncc = ((NoteCriteriaTags)mAdvancedSearchTagsCriteria.getChildAt(i));
            if(ncc.getType() == 0) {
                contains.add(ncc.getContent());
            } else {
                dnContains.add(ncc.getContent());
            }
        }

        mFilteredNotes = new ArrayList<>(mAllNotes);
        for(int i = 0; i < mFilteredNotes.size(); i++) {
            NoteView nv = mFilteredNotes.get(i);

            if(matchRanges.size() > 0 && filterMatches(nv, matchRanges)) {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }


            if(teamRanges.size() > 0 && filterTeams(nv, teamRanges)) {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }

            if((contains.size()  > 0 || dnContains.size() > 0) && filterContents(nv, contains, dnContains)) {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }

            if((contains.size()  > 0 || dnContains.size() > 0) && filterTags(nv, hasTag, dnHaveTag)) {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }

            switch (nv.note_type) {
                case MATCH:
                    if(!mMatchNotesCheckbox.isChecked()){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
                case SUPER:
                    if(!mSuperNotesCheckbox.isChecked()){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
                case DRIVE_TEAM:
                    if(!mDriveTeamNotesCheckbox.isChecked()){
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    break;
            }
        }
        mAdapter = new LVA_NotesView(this, mFilteredNotes);
        mListView.setAdapter(mAdapter);
    }

    private void cleanGoneViews() {
        for(int i = 0; i < mAdvancedSearchMatchNumberCriteria.getChildCount(); i++)
        {
            if(mAdvancedSearchMatchNumberCriteria.getChildAt(i).getVisibility() == View.GONE)
            {
                mAdvancedSearchMatchNumberCriteria.removeViewAt(i);
                i--;
            }
        }
        for(int i = 0; i < mAdvancedSearchTeamNumberCriteria.getChildCount(); i++)
        {
            if(mAdvancedSearchTeamNumberCriteria.getChildAt(i).getVisibility() == View.GONE)
            {
                mAdvancedSearchTeamNumberCriteria.removeViewAt(i);
                i--;
            }
        }
        for(int i = 0; i < mAdvancedSearchContentCriteria.getChildCount(); i++)
        {
            if(mAdvancedSearchContentCriteria.getChildAt(i).getVisibility() == View.GONE)
            {
                mAdvancedSearchContentCriteria.removeViewAt(i);
                i--;
            }
        }
        for(int i = 0; i < mAdvancedSearchTagsCriteria.getChildCount(); i++)
        {
            if(mAdvancedSearchTagsCriteria.getChildAt(i).getVisibility() == View.GONE)
            {
                mAdvancedSearchTagsCriteria.removeViewAt(i);
                i--;
            }
        }
    }

    //region get ranges
    private ArrayList<Integer[]> getMatchRanges() {
        ArrayList<Integer[]> rv = new ArrayList<>();
        for(int i = 0; i < mAdvancedSearchMatchNumberCriteria.getChildCount() - 1; i++) {
            NoteCriteriaNumber ncn = (NoteCriteriaNumber)mAdvancedSearchMatchNumberCriteria.getChildAt(i);
            switch (ncn.getType()) {
                //Between
                case 0:
                    rv.add(new Integer[]{ncn.getBefore(), ncn.getAfter()});
                    break;
                //Before
                case 1:
                    rv.add(new Integer[]{Integer.MIN_VALUE, ncn.getAfter()});
                    break;
                //After
                case 2:
                    rv.add(new Integer[]{ncn.getBefore(), Integer.MAX_VALUE});
                    break;
            }
        }
        return rv;
    }

    private ArrayList<Integer[]> getTeamRanges() {
        ArrayList<Integer[]> rv = new ArrayList<>();
        for(int i = 0; i < mAdvancedSearchTeamNumberCriteria.getChildCount() - 1; i++)
        {
            NoteCriteriaNumber ncn = (NoteCriteriaNumber)mAdvancedSearchTeamNumberCriteria.getChildAt(i);
            switch (ncn.getType())
            {
                //Between
                case 0:
                    rv.add(new Integer[]{ncn.getBefore(), ncn.getAfter()});
                    break;
                //Before
                case 1:
                    rv.add(new Integer[]{ncn.getBefore(), Integer.MAX_VALUE});
                    break;
                //After
                case 2:
                    rv.add(new Integer[]{Integer.MIN_VALUE, ncn.getAfter()});
                    break;
            }
        }
        return rv;
    }
    //endregion

    //region filters
    /**
     * Function that determines if note should be filtered based on match number
     *
     * @param nv
     * @param matchRanges
     * @return
     */
    private boolean filterMatches(NoteView nv, ArrayList<Integer[]> matchRanges) {
        for(int j = 0; j < matchRanges.size(); j++) {
            int before = matchRanges.get(j)[0];
            int after = matchRanges.get(j)[1];
            if(nv.match_number >= before && nv.match_number <= after) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function that determines if note should be filtered based on team number
     *
     * @param nv
     * @param teamRanges
     * @return
     */
    private boolean filterTeams(NoteView nv, ArrayList<Integer[]> teamRanges) {
        for(int j = 0; j < teamRanges.size(); j++) {
            int before = teamRanges.get(j)[0];
            int after = teamRanges.get(j)[1];
            if(nv.team_number >= before && nv.team_number <= after) {
                return false;
            }
        }
        return true;
    }

    /**
     * Function that determines if note should be filtered based on content
     *
     * @param nv
     * @param contains
     * @param dnContains
     * @return
     */
    private boolean filterContents(NoteView nv, ArrayList<String> contains, ArrayList<String> dnContains) {
        for(int j = 0; j < contains.size(); j++) {
            if(!nv.note.contains(contains.get(j))) {
                return true;
            }
        }
        for(int j = 0; j < dnContains.size(); j++) {
            if(nv.note.contains(dnContains.get(j))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Function that determines if note should be filtered based on tags
     *
     * @param nv
     * @param hasTag
     * @param dnHaveTag
     * @return
     */
    private boolean filterTags(NoteView nv, ArrayList<String> hasTag, ArrayList<String> dnHaveTag) {
        for(int j = 0; j < hasTag.size(); j++) {
            if(!nv.tags.contains(hasTag.get(j))) {
                return true;
            }
        }
        for(int j = 0; j < dnHaveTag.size(); j++) {
            if(nv.tags.contains(dnHaveTag.get(j))) {
                return true;
            }
        }
        return false;
    }
    //endregion

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.basic_match_number_option:
                mBasicSearchEditText.setInputType(2);
                break;
            case R.id.basic_team_number_option:
                mBasicSearchEditText.setInputType(2);
                break;
            case R.id.basic_content_option:
                mBasicSearchEditText.setInputType(1);
                break;
        }
    }
}
