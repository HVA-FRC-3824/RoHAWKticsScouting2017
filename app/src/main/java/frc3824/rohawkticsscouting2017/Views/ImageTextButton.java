package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 9/26/16
 *
 * Button that contains both an image and text in the center
 */
public class ImageTextButton extends LinearLayout{

    private final static String TAG = "ImageTextButton";

    private TextView mTextView;
    private ImageView mImageView;
    private int mImageResourceId;

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.image_text_button, this);

        mTextView= (TextView) findViewById(R.id.text);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
        mTextView.setText(typedArray.getString(R.styleable.ImageTextButton_text));
        mTextView.setTextColor(typedArray.getColor(R.styleable.ImageTextButton_textColor, Color.WHITE));
        mTextView.setTextSize(typedArray.getDimensionPixelSize(R.styleable.ImageTextButton_textSize, 12));

        mImageView = (ImageView) findViewById(R.id.icon);
        int resource_id = typedArray.getResourceId(R.styleable.ImageTextButton_src, -1);
        mImageResourceId = resource_id;

        if(resource_id > -1)
        {
            mImageView.setImageResource(resource_id);
        }
    }

    public void setText(String text){
        mTextView.setText(text);
    }

    public int getImageResourceId(){
        return mImageResourceId;
    }

    public void setImage(int resource_id)
    {
        mImageView.setImageResource(resource_id);
        mImageResourceId = resource_id;
    }
}
