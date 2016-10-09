package frc3824.rohawkticsscouting2017.Views.NoteCriteria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 *         Created: 9/4/16
 */
public class NoteCriteriaNumber extends RelativeLayout implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private final static String TAG = "NoteCriteriaNumber";

    private Spinner mCriteriaType;
    private EditText mBefore;
    private EditText mAfter;
    private TextView mHyphen;
    private String[] mTypes = {"Between", "Before", "After"};
    private Button mRemove;

    public NoteCriteriaNumber(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.note_criteria_number, this);

        mCriteriaType = (Spinner)findViewById(R.id.criteria_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mTypes);
        mCriteriaType.setAdapter(adapter);
        mCriteriaType.setOnItemSelectedListener(this);

        mBefore = (EditText)findViewById(R.id.before);
        mAfter = (EditText)findViewById(R.id.after);
        mHyphen = (TextView)findViewById(R.id.hyphen);

        mRemove = (Button)findViewById(R.id.remove);
        mRemove.setOnClickListener(this);
    }

    public int getType()
    {
        return mCriteriaType.getSelectedItemPosition();
    }

    public int getBefore()
    {
        return Integer.parseInt(mBefore.getText().toString());
    }

    public int getAfter()
    {
        return Integer.parseInt(mAfter.getText().toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (pos) {
            // Between
            case 0:
                mBefore.setVisibility(VISIBLE);
                mBefore.setText("");
                mAfter.setVisibility(VISIBLE);
                mAfter.setText("");
                mHyphen.setVisibility(VISIBLE);
                break;
            // Before
            case 1:
                mBefore.setVisibility(GONE);
                mBefore.setText("");
                mAfter.setVisibility(VISIBLE);
                mAfter.setText("");
                mHyphen.setVisibility(GONE);
                break;
            // After
            case 2:
                mBefore.setVisibility(VISIBLE);
                mBefore.setText("");
                mAfter.setVisibility(GONE);
                mAfter.setText("");
                mHyphen.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        this.setVisibility(GONE);
    }
}
