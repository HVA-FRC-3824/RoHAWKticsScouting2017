package frc3824.rohawkticsscouting2017.Fragments.PitScouting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.ScoutFragment;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Fragment for Pit Scouting that handles taking a picture of the robot
 */
//TODO: setup taking a picture and firebase storage
public class RobotPictureFragment extends ScoutFragment implements View.OnClickListener{

    private final static String TAG = "RobotPictureFragment";

    private Button mButton;
    private String mCurrentPhotoFilename;
    private Context mContext;
    private ImageView mImageView;

    private final static int REQUEST_IMAGE_CAPTURE = 1;

    public RobotPictureFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_robot_picture, container, false);
        Utilities.setupUi(getActivity(), view);

        mContext = getContext();
        mImageView = (ImageView) view.findViewById(R.id.robot_picture);
        mButton = (Button) view.findViewById(R.id.take_picture);

        // get photo filename from the database and display image
        if (mValueMap != null) {
            // Set up the image if one already exists
            if (mValueMap.contains(Constants.Pit_Scouting.ROBOT_PICTURE_FILENAME)) {
                try {
                    mCurrentPhotoFilename = mValueMap.getString(Constants.Pit_Scouting.ROBOT_PICTURE_FILENAME);
                    //mValueMap.remove(Constants.Pit_Inputs.PIT_ROBOT_PICTURE);
                    if (!mCurrentPhotoFilename.equals("")) {
                        if (displayPicture()) {
                            mButton.setText("Remove Picture");
                        }
                    }
                } catch (ScoutValue.TypeException e) {
                    Log.e(TAG, e.getMessage());
                }

            }
        }

        mButton.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        String text = mButton.getText().toString();
        if(text.equals("Take Picture"))
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                mCurrentPhotoFilename = "robot_picture_" + timeStamp + ".jpg";

                File f = new File(mContext.getFilesDir(), mCurrentPhotoFilename);
                f.delete();
                FileOutputStream fos = null;
                try {
                    fos = mContext.openFileOutput(mCurrentPhotoFilename, Context.MODE_WORLD_WRITEABLE);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                //Get reference to the file
                f = new File(mContext.getFilesDir(), mCurrentPhotoFilename);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        // Removes the image from the file system
        else if (text.equals("Remove Picture")) {
            File file = new File(getContext().getFilesDir(), mCurrentPhotoFilename);
            boolean deleted = file.delete();
            Log.d(TAG, "deleted: " + deleted);
            mButton.setText("Take Picture");
            mImageView.setImageDrawable(null);
            mValueMap.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILENAME);
        }
    }

    /**
     * Handles when the camera app returns with the image
     *
     * @param requestCode The code for what type of activity was requested
     * @param resultCode  Whether or not the result was ok
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Request: " + requestCode + " Result: " + resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            displayPicture();
            mButton.setText("Remove Picture");
        }
    }

    /**
     * Sets the Image view to display the image
     *
     * @return
     */
    private boolean displayPicture() {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 600;

        String fullPath = mContext.getFilesDir().getAbsolutePath() + "/" + mCurrentPhotoFilename;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fullPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fullPath, bmOptions);
        try {
            FileOutputStream fos = mContext.openFileOutput(mCurrentPhotoFilename, Context.MODE_WORLD_WRITEABLE);
            if (fos != null && bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                mImageView.setImageBitmap(bitmap);
                return true;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return false;
    }

    /**
     * Special write that records the path of the picture
     *
     * @param map
     * @return
     */
    @Override
    public String writeContentsToMap(ScoutMap map) {
        if(!mCurrentPhotoFilename.equals("")) {
            map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILENAME, mCurrentPhotoFilename);
        }
        return "";
    }
}
