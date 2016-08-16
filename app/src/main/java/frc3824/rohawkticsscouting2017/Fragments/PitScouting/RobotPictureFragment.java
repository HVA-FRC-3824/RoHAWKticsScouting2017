package frc3824.rohawkticsscouting2017.Fragments.PitScouting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
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
 *
 * https://developer.android.com/training/camera/photobasics.html
 */
//TODO: setup taking a picture and firebase storage
public class RobotPictureFragment extends ScoutFragment implements View.OnClickListener
{

    private final static String TAG = "RobotPictureFragment";

    private Button mButton;
    private String mCurrentPhotoPath;
    private Context mContext;
    private ImageView mImageView;

    private final static int REQUEST_TAKE_PHOTO = 1;

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
            if (mValueMap.contains(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH)) {
                try {
                    mCurrentPhotoPath = mValueMap.getString(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);
                    //mValueMap.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);
                    if (!mCurrentPhotoPath.equals("")) {
                        if(new File(mCurrentPhotoPath).exists()) {
                            displayPicture();
                            mButton.setText("Remove Picture");
                        }
                        else
                        {
                            view.findViewById(R.id.need_to_download).setVisibility(View.VISIBLE);
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
            dispatchTakePictureIntent();
        }
        // Removes the image from the file system
        else if (text.equals("Remove Picture")) {
            File file = new File(mCurrentPhotoPath);
            boolean deleted = file.delete();
            Log.d(TAG, "deleted: " + deleted);
            mButton.setText("Take Picture");
            mImageView.setImageDrawable(null);
            mValueMap.remove(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH);
            view.findViewById(R.id.need_to_download).setVisibility(View.GONE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(!storageDir.exists())
        {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "frc3824.rohawkticsscouting2017.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
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
        super.onActivityResult(requestCode,resultCode,data);
        Log.d(TAG, "Request: " + requestCode + " Result: " + resultCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            displayPicture();
            mButton.setText("Remove Picture");
        }
    }

    /**
     * Sets the Image view to display the image
     *
     * @return
     */
    private void displayPicture() {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 600;

        File f = new File(mCurrentPhotoPath);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    /**
     * Special write that records the path of the picture
     *
     * @param map
     * @return
     */
    @Override
    public String writeContentsToMap(ScoutMap map) {
        if(!mCurrentPhotoPath.equals("")) {
            Log.d(TAG, mCurrentPhotoPath);
            map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATH, mCurrentPhotoPath);
        }
        return "";
    }



}
