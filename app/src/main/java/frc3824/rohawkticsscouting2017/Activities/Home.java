package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

public class Home extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupButton(R.id.settings_button);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String user_type = sharedPreferences.getString(Constants.Settings.USER_TYPE, "");
        switch (user_type)
        {
            case Constants.User_Types.MATCH_SCOUT:
                setupButton(R.id.scoutMatch_button);

                TextView usertypeTextview = (TextView)findViewById(R.id.user_type);
                usertypeTextview.setText("Match Scout");
                usertypeTextview.setVisibility(View.VISIBLE);

                String allianceColor = sharedPreferences.getString(Constants.Settings.ALLIANCE_COLOR, "");
                String userSubtype;
                if(allianceColor == Constants.Alliance_Colors.BLUE)
                {
                    userSubtype = "Blue ";
                }
                else
                {
                    userSubtype = "Red ";
                }
                int allianceNumber = sharedPreferences.getInt(Constants.Settings.ALLIANCE_NUMBER, -1);
                userSubtype += String.valueOf(allianceNumber);
                TextView userSubtypeTextView = (TextView)findViewById(R.id.user_subtype);
                userSubtypeTextView.setText(userSubtype);
                userSubtypeTextView.setVisibility(View.VISIBLE);
                break;
            default:
                assert false;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.settings_button:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            case R.id.scoutMatch_button:
                intent = new Intent(this, MatchList.class);
                intent.putExtra(Constants.Intent_Extras.NEXT_PAGE,Constants.Intent_Extras.MATCH_SCOUTING);
                startActivity(intent);
                break;
        }
    }

    /**
     * Makes the button visible and attaches the on click listener
     *
     * @param btn id for the button to be set up
     */
    private void setupButton(int btn) {
        Button button = (Button) findViewById(btn);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
    }
}
