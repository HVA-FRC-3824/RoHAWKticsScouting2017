package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;

/**
 * @author frc3824
 * Created: 1/13/17
 */
public class SavableTags extends SavableView {

    private final static String TAG = "SavableTags";
    private String mKey;
    private SavableCheckbox[] mCheckboxes;

    public SavableTags(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_tags, this);

        LinearLayout ll = (LinearLayout)findViewById(R.id.layout);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableTags);
        int labelsId = typedArray.getResourceId(R.styleable.SavableTags_labels, 0);
        String[] labels = context.getResources().getStringArray(labelsId);
        int keyExtensionsId = typedArray.getResourceId(R.styleable.SavableTags_keys, 0);
        String[] keys = context.getResources().getStringArray(keyExtensionsId);
        typedArray.recycle();

        mCheckboxes = new SavableCheckbox[labels.length];
        for(int i = 0; i < labels.length; i++){
            mCheckboxes[i] = new SavableCheckbox(context, labels[i], mKey + keys[i]);
            ll.addView(mCheckboxes[i]);
        }
    }

    @Override
    public String writeToMap(ScoutMap map){
        String error = "";
        for(int i = 0; i < mCheckboxes.length; i++){
            error += mCheckboxes[i].writeToMap(map);
        }
        return error;
    }

    @Override
    public String restoreFromMap(ScoutMap map){
        String error = "";
        for(int i = 0; i < mCheckboxes.length; i++){
            error += mCheckboxes[i].restoreFromMap(map);
        }
        return error;
    }
}
