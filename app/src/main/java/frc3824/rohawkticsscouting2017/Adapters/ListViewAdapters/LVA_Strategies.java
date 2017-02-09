package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 *         Created: 8/20/16
 */
public class LVA_Strategies extends ArrayAdapter<Strategy> {

    private final static String TAG = "LVA_Strategies";
    private Context mContext;
    private ArrayList<Strategy> mStrategies;

    public LVA_Strategies(Context context, ArrayList<Strategy> objects) {
        super(context, R.layout.list_item_strategies, objects);
        mStrategies = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_strategies, null);
        }

        final Strategy strategy = mStrategies.get(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView strategyNameTextView = (TextView) convertView.findViewById(R.id.strategy_name);
        String strategyName = strategy.filepath.substring(strategy.filepath.lastIndexOf('/') + 1);
        strategyName = strategyName.substring(0, strategyName.lastIndexOf('.'));

        // Create checks if file exists and if it is older than the last_modified variable
        strategy.create(mContext);

        strategyNameTextView.setText(strategyName);
        displayPicture(image, strategy.filepath);

        return convertView;
    }

    /**
     * Sets the Image view to display the image
     *
     * @return
     */
    private void displayPicture(ImageView imageView, String filepath) {
        // Get the dimensions of the View
        int targetW = 100;
        int targetH = 100;

        File f = new File(filepath);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}
