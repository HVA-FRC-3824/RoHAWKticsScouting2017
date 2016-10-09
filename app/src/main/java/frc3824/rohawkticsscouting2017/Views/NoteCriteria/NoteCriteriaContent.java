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
 * @author frc3824
 * Created: 9/6/16
 *
 *
 */
//TODO: create add/or not contain for content criteria
public class NoteCriteriaContent extends RelativeLayout implements View.OnClickListener {

    private final static String TAG = "NoteCriteriaContent";

    private Spinner mCriteriaType;
    private EditText mContent;
    private Button mRemove;

    private String[] mTypes = {"Contains", "Does not contain"};

    public NoteCriteriaContent(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.note_criteria_content, this);

        mCriteriaType = (Spinner)findViewById(R.id.criteria_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mTypes);
        mCriteriaType.setAdapter(adapter);

        mContent = (EditText)findViewById(R.id.content);

        mRemove = (Button)findViewById(R.id.remove);
        mRemove.setOnClickListener(this);
    }

    public int getType()
    {
        return mCriteriaType.getSelectedItemPosition();
    }

    public String getContent()
    {
        return mContent.getText().toString();
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
