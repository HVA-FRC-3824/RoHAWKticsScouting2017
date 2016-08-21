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

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/17/16
 *
 *
 */
public class PitDataFragment extends Fragment {

    private final static String TAG = "PitDataFragment";

    private int mTeamNumber;

    public PitDataFragment(){}

    public void setTeamNumber(int teamNumber)
    {
        mTeamNumber = teamNumber;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pit_data, container, false);

        TPD team = Database.getInstance().getTPD(mTeamNumber);

        if(team.robot_image_filepath != null && team.robot_image_filepath != "")
        {
            ImageView imageView = (ImageView)view.findViewById(R.id.robot_picture);
            displayPicture(team.robot_image_filepath, imageView);
        }

        ((TextView)view.findViewById(R.id.width)).setText(String.valueOf(team.width));
        ((TextView)view.findViewById(R.id.length)).setText(String.valueOf(team.length));
        ((TextView)view.findViewById(R.id.height)).setText(String.valueOf(team.height));
        ((TextView)view.findViewById(R.id.weight)).setText(String.valueOf(team.weight));

        ((TextView)view.findViewById(R.id.programming_language)).setText(team.programming_language);

        ((TextView)view.findViewById(R.id.notes)).setText(team.notes);

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
}
