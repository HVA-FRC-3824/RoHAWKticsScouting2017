package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_NotesView;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.NoteView;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Views.NoteCriteria.NoteCriteriaContent;
import frc3824.rohawkticsscouting2017.Views.NoteCriteria.NoteCriteriaNumber;

/**
 * @author frc3824
 * Created: 9/3/16
 *
 * Activity for filtering through notes
 */

//TODO: create add/or for content criteria
public class NotesViewActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "NotesViewActivity";

    //region variables
    private RelativeLayout mBasicSearch;
    private Button mBasicSearchOpen;
    private RadioGroup mBasicSearchType;
    private EditText mBasicSearchEditText;
    private Button mBasicSearchButton;

    private LinearLayout mAdvancedSearch;
    private Button mAdvancedSearchOpen;
    private LinearLayout mAdvancedSearchMatchNumberCriteria;
    private Button mAdvancedSearchMatchNumberCriteriaAdd;
    private LinearLayout mAdvancedSearchTeamNumberCriteria;
    private Button mAdvancedSearchTeamNumberCriteriaAdd;
    private LinearLayout mAdvancedSearchContentCriteria;
    private Button mAdvancedSearchContentCriteriaAdd;
    private CheckBox mMatchNotesCheckbox;
    private CheckBox mSuperNotesCheckbox;
    private CheckBox mDriveTeamNotesCheckbox;

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
        mBasicSearchOpen = (Button)findViewById(R.id.basic_search_open);
        mBasicSearchOpen.setOnClickListener(this);
        mBasicSearchType = (RadioGroup)findViewById(R.id.basic_search_type);
        mBasicSearchType.check(R.id.basic_match_number_option);
        mBasicSearchEditText = (EditText)findViewById(R.id.basic_search_edittext);
        mBasicSearchButton = (Button)findViewById(R.id.basic_search_button);
        //endregion

        //region Advanced Search Find Views
        mAdvancedSearch = (LinearLayout)findViewById(R.id.advanced_search);
        mAdvancedSearchOpen = (Button)findViewById(R.id.advanced_search_open);
        mAdvancedSearchOpen.setOnClickListener(this);
        mAdvancedSearchMatchNumberCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_match_number_criteria);
        mAdvancedSearchMatchNumberCriteriaAdd = (Button)mAdvancedSearchMatchNumberCriteria.findViewById(R.id.advanced_match_number_criteria_add);
        mAdvancedSearchMatchNumberCriteriaAdd.setOnClickListener(this);
        mAdvancedSearchTeamNumberCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_team_number_criteria);
        mAdvancedSearchTeamNumberCriteriaAdd = (Button)mAdvancedSearchTeamNumberCriteria.findViewById(R.id.advanced_team_number_criteria_add);
        mAdvancedSearchTeamNumberCriteriaAdd.setOnClickListener(this);
        mAdvancedSearchContentCriteria = (LinearLayout)mAdvancedSearch.findViewById(R.id.advanced_content_criteria);
        mAdvancedSearchContentCriteriaAdd = (Button)mAdvancedSearchContentCriteria.findViewById(R.id.advanced_content_criteria_add);
        mAdvancedSearchContentCriteriaAdd.setOnClickListener(this);
        mMatchNotesCheckbox = (CheckBox)mAdvancedSearch.findViewById(R.id.match_notes_checkbox);
        mSuperNotesCheckbox = (CheckBox)mAdvancedSearch.findViewById(R.id.super_notes_checkbox);
        mDriveTeamNotesCheckbox = (CheckBox)mAdvancedSearch.findViewById(R.id.drive_team_notes_checkbox);
        //endregion

        mLayoutInflator = getLayoutInflater();

        mListView = (ListView)findViewById(R.id.list_view);

        Database database = Database.getInstance();
        mAllNotes = database.getAllNotes();
        mFilteredNotes = new ArrayList<>(mAllNotes);
        mAdapter = new LVA_NotesView(this, mFilteredNotes);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.basic_search_open:
                //region Basic Search Open
                mBasicSearch.setVisibility(View.VISIBLE);
                mAdvancedSearch.setVisibility(View.GONE);
                mBasicSearchOpen.setVisibility(View.GONE);
                mAdvancedSearchOpen.setVisibility(View.VISIBLE);
                // Remove all the advanced search views that are no longer needed
                for(int i = 0; i < mAdvancedSearchMatchNumberCriteria.getChildCount() - 1; i++)
                {
                    mAdvancedSearchMatchNumberCriteria.removeViewAt(0);
                }
                for(int i = 0; i < mAdvancedSearchTeamNumberCriteria.getChildCount() - 1; i++)
                {
                    mAdvancedSearchTeamNumberCriteria.removeViewAt(0);
                }
                for(int i = 0; i < mAdvancedSearchContentCriteria.getChildCount() - 1; i++)
                {
                    mAdvancedSearchContentCriteria.removeViewAt(0);
                }
                //endregion
                break;
            case R.id.advanced_search_open:
                //region Advanced Search Open
                mBasicSearch.setVisibility(View.GONE);
                mAdvancedSearch.setVisibility(View.VISIBLE);
                mBasicSearchOpen.setVisibility(View.VISIBLE);
                mAdvancedSearchOpen.setVisibility(View.GONE);
                //endregion
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
            case R.id.advanced_search_button:
                advancedSearch();
                break;
        }
    }

    private void basicSearch(){
        int search_type = -1;
        int number = -1;
        String content = "";

        switch (mBasicSearchType.getCheckedRadioButtonId())
        {
            case R.id.basic_match_number_option:
                search_type = 0;
                number = Integer.parseInt(mBasicSearchEditText.getText().toString());
                break;
            case R.id.basic_team_number_option:
                search_type = 1;
                number = Integer.parseInt(mBasicSearchEditText.getText().toString());
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
        for(int i = 0; i < mAdvancedSearchContentCriteria.getChildCount() - 1; i++)
        {
            NoteCriteriaContent ncc = ((NoteCriteriaContent)mAdvancedSearchContentCriteria.getChildAt(i));
            if(ncc.getType() == 0) {
                contains.add(ncc.getContent());
            } else {
                dnContains.add(ncc.getContent());
            }
        }

        mFilteredNotes = new ArrayList<>(mAllNotes);
        for(int i = 0; i < mFilteredNotes.size(); i++)
        {
            NoteView nv = mFilteredNotes.get(i);

            if(filterMatches(nv, matchRanges))
            {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }


            if(filterTeams(nv, teamRanges))
            {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }

            if(filterContents(nv, contains, dnContains))
            {
                mFilteredNotes.remove(i);
                i--;
                continue;
            }

            switch (nv.note_type)
            {
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
    }

    //region get ranges
    private ArrayList<Integer[]> getMatchRanges()
    {
        ArrayList<Integer[]> rv = new ArrayList<>();
        for(int i = 0; i < mAdvancedSearchMatchNumberCriteria.getChildCount() - 1; i++)
        {
            NoteCriteriaNumber ncn = (NoteCriteriaNumber)mAdvancedSearchMatchNumberCriteria.getChildAt(i);
            switch (ncn.getType())
            {
                //Between
                case 0:
                    rv.add(new Integer[]{ncn.getBefore(), ncn.getAfter()});
                    break;
                //Before
                case 1:
                    rv.add(new Integer[]{ncn.getBefore(), -1});
                    break;
                //After
                case 2:
                    rv.add(new Integer[]{-1, ncn.getAfter()});
                    break;
            }
        }
        return rv;
    }

    private ArrayList<Integer[]> getTeamRanges()
    {
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
                    rv.add(new Integer[]{ncn.getBefore(), -1});
                    break;
                //After
                case 2:
                    rv.add(new Integer[]{-1, ncn.getAfter()});
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
    //endregion
}
