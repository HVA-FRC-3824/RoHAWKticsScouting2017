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
 * @author Andrew Messing
 *         Created: 9/6/16
 */
//TODO: create add/or and contains/does not contain for content criteria
public class NoteCriteriaContent extends RelativeLayout implements View.OnClickListener {

    private final static String TAG = "NoteCriteriaContent";

    private EditText mContent;
    private Button mRemove;

    public NoteCriteriaContent(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.note_criteria_content, this);

        mContent = (EditText)findViewById(R.id.content);

        mRemove = (Button)findViewById(R.id.remove);
        mRemove.setOnClickListener(this);
    }

    public String getContains()
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
