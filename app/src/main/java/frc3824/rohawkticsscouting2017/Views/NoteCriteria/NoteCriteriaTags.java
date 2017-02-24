package frc3824.rohawkticsscouting2017.Views.NoteCriteria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author bacony42
 * Created: 2/15/17
 *
 *
 */
public class NoteCriteriaTags extends RelativeLayout implements View.OnClickListener {

    private final static String TAG = "NoteCriteriaTags";

    private Spinner mCriteriaType;
    private EditText mTag;
    private Button mRemove;

    private String[] mTypes = {"Has Tag", "Does not have Tag"};

    public NoteCriteriaTags(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.note_criteria_tags, this);

        mCriteriaType = (Spinner)findViewById(R.id.criteria_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mTypes);
        mCriteriaType.setAdapter(adapter);

        mTag = (EditText)findViewById(R.id.tags);

        mRemove = (Button)findViewById(R.id.remove);
        mRemove.setOnClickListener(this);
    }

    public int getType()
    {
        return mCriteriaType.getSelectedItemPosition();
    }

    public String getContent()
    {
        return mTag.getText().toString();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.remove:
                this.setVisibility(GONE);
                break;
        }
    }
}