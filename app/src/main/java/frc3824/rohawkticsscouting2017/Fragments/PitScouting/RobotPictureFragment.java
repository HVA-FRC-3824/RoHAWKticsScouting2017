package frc3824.rohawkticsscouting2017.Fragments.PitScouting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Fragments.ScoutFragment;
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

    private Button mTakePictureButton;
    private Button mSetDefaultButton;
    private Button mDeleteButton;
    private ImageButton mLeftButton;
    private ImageButton mRightButton;

    private int mDefaultPhoto;
    private int mCurrentPhoto;
    private ArrayList<String> mPhotoPaths;
    private ArrayList<String> mPhotoUrls;
    private Context mContext;
    private ImageView mImageView;

    private final static int REQUEST_CAMERA_PERMISSION = 3;
    private final static int REQUEST_TAKE_PHOTO = 1;

    public RobotPictureFragment() {
        mPhotoPaths = new ArrayList<>();
        mPhotoUrls = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pit_robot_picture, container, false);
        Utilities.setupUi(getActivity(), view);

        mContext = getContext();

        mImageView = (ImageView) view.findViewById(R.id.robot_picture);
        mTakePictureButton = (Button) view.findViewById(R.id.take_picture);
        mTakePictureButton.setOnClickListener(this);

        mSetDefaultButton = (Button) view.findViewById(R.id.set_default);
        mSetDefaultButton.setOnClickListener(this);

        mDeleteButton = (Button) view.findViewById(R.id.delete_picture);
        mDeleteButton.setOnClickListener(this);

        mLeftButton = (ImageButton) view.findViewById(R.id.left);
        mLeftButton.setOnClickListener(this);
        mLeftButton.setEnabled(false);

        mRightButton = (ImageButton) view.findViewById(R.id.right);
        mRightButton.setOnClickListener(this);
        mRightButton.setEnabled(false);


        if(ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            mTakePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        else
        {
            mTakePictureButton.setEnabled(true);
        }

        // get photo filename from the database and display image
        if (mValueMap != null) {
            // Set up the image if one already exists
            if (mValueMap.contains(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS)) {
                mLeftButton.setEnabled(true);
                mRightButton.setEnabled(true);
                try {
                    mPhotoPaths = (ArrayList<String>)mValueMap.getObject(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS);
                    mPhotoUrls = (ArrayList<String>)mValueMap.getObject(Constants.Pit_Scouting.ROBOT_PICTURE_URLS);
                    if(mValueMap.contains(Constants.Pit_Scouting.ROBOT_PICTURE_DEFAULT)) {
                        mDefaultPhoto = mValueMap.getInt(Constants.Pit_Scouting.ROBOT_PICTURE_DEFAULT);
                    } else {
                        mDefaultPhoto = -1;
                    }
                    mCurrentPhoto = mDefaultPhoto;
                    if (mCurrentPhoto > -1 && mPhotoPaths.size() > 0) {
                        if(new File(mPhotoPaths.get(mDefaultPhoto)).exists()) {
                            displayPicture();
                            mSetDefaultButton.setVisibility(View.VISIBLE);
                            mDeleteButton.setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.need_to_download).setVisibility(View.VISIBLE);
                        }
                    }
                } catch (ScoutValue.TypeException e) {
                    Log.e(TAG, e.getMessage());
                }

            }
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.take_picture:
                dispatchTakePictureIntent();
                break;
            case R.id.delete_picture:
                File file = new File(mPhotoPaths.get(mCurrentPhoto));
                file.delete();
                mPhotoPaths.remove(mCurrentPhoto);
                mPhotoUrls.remove(mCurrentPhoto);
                if(mPhotoPaths.size() == 0){
                    mDefaultPhoto = -1;
                    mCurrentPhoto = -1;
                    mImageView.setImageDrawable(null);
                    mSetDefaultButton.setVisibility(View.GONE);
                    mDeleteButton.setVisibility(View.GONE);
                    mLeftButton.setEnabled(false);
                    mRightButton.setEnabled(false);
                } else {
                    if(mDefaultPhoto == mCurrentPhoto){
                        mDefaultPhoto = 0;
                    }
                    if(mCurrentPhoto == mPhotoPaths.size()){
                        mCurrentPhoto--;
                    }
                    displayPicture();
                }
                break;
            case R.id.set_default:
                mDefaultPhoto = mCurrentPhoto;
                break;
            case R.id.left:
                mCurrentPhoto --;
                if(mCurrentPhoto < 0)
                    mCurrentPhoto += mPhotoPaths.size();
                displayPicture();
                break;
            case R.id.right:
                mCurrentPhoto ++;
                if(mCurrentPhoto >= mPhotoPaths.size())
                    mCurrentPhoto -= mPhotoPaths.size();
                displayPicture();
                break;
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
        mPhotoPaths.add(image.getAbsolutePath());
        mPhotoUrls.add("");

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
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if(resultCode == Activity.RESULT_OK) {
                mCurrentPhoto = mPhotoPaths.size() - 1;
                if(mDefaultPhoto == -1){
                    mDefaultPhoto = mCurrentPhoto;
                }
                mLeftButton.setEnabled(true);
                mRightButton.setEnabled(true);
                displayPicture();
                mDeleteButton.setVisibility(View.VISIBLE);
                mSetDefaultButton.setVisibility(View.VISIBLE);

            } else {
                mPhotoPaths.remove(mPhotoPaths.size() - 1);
                mPhotoUrls.remove(mPhotoUrls.size() - 1);
            }
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

        File f = new File(mPhotoPaths.get(mCurrentPhoto));

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPaths.get(mCurrentPhoto), bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    public void cameraHasPermission() {
        mTakePictureButton.setEnabled(true);
    }

    /**
     * Special write that records the path of the picture
     *
     * @param map
     * @return
     */
    @Override
    public String writeContentsToMap(ScoutMap map) {
        //if(mPhotoPaths.size() != 0) {
            map.put(Constants.Pit_Scouting.ROBOT_PICTURE_FILEPATHS, mPhotoPaths);
            map.put(Constants.Pit_Scouting.ROBOT_PICTURE_URLS, mPhotoUrls);
            map.put(Constants.Pit_Scouting.ROBOT_PICTURE_DEFAULT, mDefaultPhoto);
        //}
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Caught permission");
                    mTakePictureButton.setEnabled(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

        }
    }
}
