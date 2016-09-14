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
 * @author Andrew Messing
 *         Created: 9/3/16
 */

//TODO: create add/or and contains/does not contain for content criteria
public class NotesViewActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "NotesViewActivity";

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
    private  Button mAdvancedSearchTeamNumberCriteriaAdd;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        mBasicSearch = (RelativeLayout)findViewById(R.id.basic_search);
        mBasicSearchOpen = (Button)findViewById(R.id.basic_search_open);
        mBasicSearchOpen.setOnClickListener(this);
        mBasicSearchType = (RadioGroup)findViewById(R.id.basic_search_type);
        mBasicSearchType.check(R.id.basic_match_number_option);
        mBasicSearchEditText = (EditText)findViewById(R.id.basic_search_edittext);
        mBasicSearchButton = (Button)findViewById(R.id.basic_search_button);

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
                break;
            case R.id.advanced_search_open:
                mBasicSearch.setVisibility(View.GONE);
                mAdvancedSearch.setVisibility(View.VISIBLE);
                mBasicSearchOpen.setVisibility(View.VISIBLE);
                mAdvancedSearchOpen.setVisibility(View.GONE);
                break;
            case R.id.basic_search_button:
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

                // Remove all the "Gone" views
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

                ArrayList<Integer[]> matchRanges = new ArrayList<>();
                for(int i = 0; i < mAdvancedSearchMatchNumberCriteria.getChildCount() - 1; i++)
                {
                    NoteCriteriaNumber ncn = (NoteCriteriaNumber)mAdvancedSearchMatchNumberCriteria.getChildAt(i);
                    switch (ncn.getType())
                    {
                        //Between
                        case 0:
                            matchRanges.add(new Integer[]{ncn.getBefore(), ncn.getAfter()});
                            break;
                        //Before
                        case 1:
                            matchRanges.add(new Integer[]{ncn.getBefore(), -1});
                            break;
                        //After
                        case 2:
                            matchRanges.add(new Integer[]{-1, ncn.getAfter()});
                            break;
                    }
                }
                ArrayList<Integer[]> teamRanges = new ArrayList<>();
                for(int i = 0; i < mAdvancedSearchTeamNumberCriteria.getChildCount() - 1; i++)
                {
                    NoteCriteriaNumber ncn = (NoteCriteriaNumber)mAdvancedSearchTeamNumberCriteria.getChildAt(i);
                    switch (ncn.getType())
                    {
                        //Between
                        case 0:
                            teamRanges.add(new Integer[]{ncn.getBefore(), ncn.getAfter()});
                            break;
                        //Before
                        case 1:
                            teamRanges.add(new Integer[]{ncn.getBefore(), Integer.MAX_VALUE});
                            break;
                        //After
                        case 2:
                            teamRanges.add(new Integer[]{Integer.MIN_VALUE, ncn.getAfter()});
                            break;
                    }
                }

                ArrayList<String> contains = new ArrayList<>();
                for(int i = 0; i < mAdvancedSearchContentCriteria.getChildCount() - 1; i++)
                {
                    contains.add(((NoteCriteriaContent)mAdvancedSearchContentCriteria.getChildAt(i)).getContains());
                }
                mFilteredNotes = new ArrayList<>(mAllNotes);
                for(int i = 0; i < mFilteredNotes.size(); i++)
                {
                    NoteView nv = mFilteredNotes.get(i);
                    boolean remove = true;
                    for(int j = 0; j < matchRanges.size(); j++)
                    {
                        int before = matchRanges.get(j)[0];
                        int after = matchRanges.get(j)[1];
                        if(nv.match_number >= before && nv.match_number <= after)
                        {
                            remove = false;
                            break;
                        }
                    }
                    if(remove)
                    {
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    remove = true;
                    for(int j = 0; j < teamRanges.size(); j++)
                    {
                        int before = teamRanges.get(j)[0];
                        int after = teamRanges.get(j)[1];
                        if(nv.team_number >= before && nv.team_number <= after)
                        {
                            remove = false;
                            break;
                        }
                    }
                    if(remove)
                    {
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                    remove = true;
                    for(int j = 0; j < contains.size(); j++)
                    {
                        if(nv.note.contains(contains.get(j)))
                        {
                            remove = false;
                            break;
                        }
                    }
                    if(remove)
                    {
                        mFilteredNotes.remove(i);
                        i--;
                        continue;
                    }
                }
                mAdapter = new LVA_NotesView(this, mFilteredNotes);
                mListView.setAdapter(mAdapter);
                break;
        }
    }
}
