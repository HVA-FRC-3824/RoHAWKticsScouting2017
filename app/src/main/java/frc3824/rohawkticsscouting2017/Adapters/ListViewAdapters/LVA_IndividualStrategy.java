package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.IndividualStrategyPlanning;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 11/5/16
 *
 */
public class LVA_IndividualStrategy extends ArrayAdapter<Strategy> {

    private final static String TAG = "LVA_IndividualStrategy";

    private ArrayList<Strategy> mStrategies;
    private Context mContext;
    private Storage mStorage;
    private Database mDatabase;


    public LVA_IndividualStrategy(Context context, ArrayList<Strategy> objects) {
        super(context, R.layout.list_item_cloud_image, objects);
        mStrategies = objects;
        mContext = context;
        mStorage = Storage.getInstance();
        mDatabase = Database.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_cloud_image, null);
        }

        if(position == 0){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = new Button(mContext);
            ((Button)convertView).setText("New Individual Strategy");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, IndividualStrategyPlanning.class);
                    mContext.startActivity(intent);
                }
            });
        } else {

            final Strategy strategy = mStrategies.get(position);
            convertView.findViewById(R.id.upload).setVisibility(View.GONE);
            convertView.findViewById(R.id.download).setVisibility(View.GONE);
            final ImageView image = (ImageView) convertView.findViewById(R.id.image);
            strategy.create(mContext);
            displayPicture(image, strategy.filepath);


            TextView filepath = (TextView) convertView.findViewById(R.id.filename);
            final String filename = strategy.filepath.substring(strategy.filepath.lastIndexOf('/') + 1).substring(0, strategy.filepath.lastIndexOf('.') - strategy.filepath.lastIndexOf('/') - 1);
            filepath.setText(filename);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, IndividualStrategyPlanning.class);
                    intent.putExtra(Constants.Intent_Extras.MATCH_PLAN_NAME, filename);
                    mContext.startActivity(intent);
                }
            });
        }

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
