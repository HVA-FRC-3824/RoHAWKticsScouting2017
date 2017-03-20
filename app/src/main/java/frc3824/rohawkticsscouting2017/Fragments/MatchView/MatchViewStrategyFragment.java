package frc3824.rohawkticsscouting2017.Fragments.MatchView;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.StrategySuggestion;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author frc3824
 * Created: 11/3/16
 *
 *
 */
public class MatchViewStrategyFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MatchViewStrategyFragment";
    private View mView;
    private StrategySuggestion mStrategySuggestion;

    private TextView mOffenseTextview;
    private EditText mOffenseEdittext;

    private TextView mDefenseTextview;
    private EditText mDefenseEdittext;

    private Button mEditButton;
    private Button mCancelButton;
    private Button mSaveButton;

    public MatchViewStrategyFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_match_view_strategy, container, false);

        mEditButton = (Button)mView.findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(this);

        mCancelButton = (Button)mView.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(this);

        mSaveButton = (Button)mView.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this);


        mOffenseTextview = (TextView)mView.findViewById(R.id.offense_textview);
        mOffenseEdittext = (EditText)mView.findViewById(R.id.offense_edittext);
        mDefenseTextview = (TextView)mView.findViewById(R.id.defense_textview);
        mDefenseEdittext = (EditText)mView.findViewById(R.id.defense_edittext);

        mOffenseTextview.setText(mStrategySuggestion.offense_text);
        mDefenseTextview.setText(mStrategySuggestion.defense_text);

        Utilities.setupUi(getActivity(), mView);

        return mView;
    }

    public void setMatch(int match_number){
        mStrategySuggestion = Database.getInstance().getStrategySuggestion(String.format("M%d", match_number));
        if(mStrategySuggestion == null){
            mStrategySuggestion = new StrategySuggestion();
            mStrategySuggestion.key = String.format("M%d", match_number);
        }
    }

    public void setMatch(ArrayList<Integer> teams){
        mStrategySuggestion = Database.getInstance().getStrategySuggestion(String.format("%d_%d_%d_%d_%d_%d", teams.get(0), teams.get(1), teams.get(2),
                teams.get(3), teams.get(4),teams.get(5)));
        if(mStrategySuggestion == null){
            mStrategySuggestion = new StrategySuggestion();
            mStrategySuggestion.key = String.format("%d_%d_%d_%d_%d_%d", teams.get(0), teams.get(1), teams.get(2),
                    teams.get(3), teams.get(4),teams.get(5));
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                mOffenseTextview.setVisibility(View.GONE);
                mOffenseEdittext.setText(mOffenseTextview.getText());
                mOffenseEdittext.setVisibility(View.VISIBLE);

                mDefenseTextview.setVisibility(View.GONE);
                mDefenseEdittext.setText(mDefenseTextview.getText());
                mDefenseEdittext.setVisibility(View.VISIBLE);

                mEditButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_button:
                mOffenseEdittext.setVisibility(View.GONE);
                mOffenseTextview.setVisibility(View.VISIBLE);

                mDefenseEdittext.setVisibility(View.GONE);
                mDefenseTextview.setVisibility(View.VISIBLE);

                mEditButton.setVisibility(View.VISIBLE);
                mCancelButton.setVisibility(View.GONE);
                mSaveButton.setVisibility(View.GONE);

                hideKeyboard();
                break;
            case R.id.save_button:
                mOffenseEdittext.setVisibility(View.GONE);
                mOffenseTextview.setText(mOffenseEdittext.getText());
                mOffenseTextview.setVisibility(View.VISIBLE);

                mStrategySuggestion.offense_text = mOffenseTextview.getText().toString();

                mDefenseEdittext.setVisibility(View.GONE);
                mDefenseTextview.setText(mDefenseEdittext.getText());
                mDefenseTextview.setVisibility(View.VISIBLE);

                mStrategySuggestion.defense_text = mDefenseTextview.getText().toString();

                Database.getInstance().setStrategySuggestion(mStrategySuggestion);

                mEditButton.setVisibility(View.VISIBLE);
                mCancelButton.setVisibility(View.GONE);
                mSaveButton.setVisibility(View.GONE);

                hideKeyboard();
                break;
        }
    }
}
