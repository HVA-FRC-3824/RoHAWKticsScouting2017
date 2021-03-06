package frc3824.rohawkticsscouting2017.Fragments.TeamView;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.UploadableImage;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/17/16
 *
 * Fragment for the team_number view that displays all the pit information and most importantly the robot's
 * pictures
 */
public class PitDataFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = "PitDataFragment";

    private int mTeamNumber;
    private ArrayList<UploadableImage> mPictures;
    private int mCurrentPicture;
    private ImageView mImageView;

    public PitDataFragment(){}

    public void setTeamNumber(int teamNumber)
    {
        mTeamNumber = teamNumber;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_view_pit_data, container, false);

        TeamPitData team = Database.getInstance().getTeamPitData(mTeamNumber);

        if(team != null) {
            view.findViewById(R.id.left).setOnClickListener(this);
            view.findViewById(R.id.right).setOnClickListener(this);

            if (team.robot_pictures != null && team.robot_pictures.size() != 0) {
                mImageView = (ImageView) view.findViewById(R.id.robot_picture);
                displayPicture(team.robot_pictures.get(team.robot_picture_default).filepath, mImageView);
                mPictures = team.robot_pictures;
                mCurrentPicture = team.robot_picture_default;
                if(mCurrentPicture < 0 || mCurrentPicture >= team.robot_pictures.size())
                {
                    mCurrentPicture = 0;
                }
            }

            ((TextView) view.findViewById(R.id.width)).setText(String.format("%03.2f (in)", team.width));
            ((TextView) view.findViewById(R.id.length)).setText(String.format("%03.2f (in)", team.length));
            ((TextView) view.findViewById(R.id.height)).setText(String.format("%03.2f (in)", team.height));
            ((TextView) view.findViewById(R.id.weight)).setText(String.format("%03.2f (lbs)", team.weight));

            ((TextView) view.findViewById(R.id.programming_language)).setText(team.programming_language);
            ((TextView) view.findViewById(R.id.drive_train)).setText(team.drive_train);
            ((TextView) view.findViewById(R.id.cims)).setText(String.valueOf(team.cims));
            ((TextView) view.findViewById(R.id.max_hopper_load)).setText(String.valueOf(team.max_hopper_load));

            ((TextView) view.findViewById(R.id.notes)).setText(team.notes);
        }
        return view;
    }

    /**
     * Sets the Image view to display the image
     *
     * @return
     */
    private void displayPicture(String photoPath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 600;

        File f = new File(photoPath);

        if(!f.exists()){
            return;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left:
                mCurrentPicture --;
                if(mCurrentPicture < 0) {
                    mCurrentPicture += mPictures.size();
                }
                displayPicture(mPictures.get(mCurrentPicture).filepath, mImageView);
                break;
            case R.id.right:
                mCurrentPicture ++;
                if(mCurrentPicture >= mPictures.size()) {
                    mCurrentPicture -= mPictures.size();
                }
                displayPicture(mPictures.get(mCurrentPicture).filepath, mImageView);
                break;
        }
    }
}
