package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.DriveTeamFeedback;
import frc3824.rohawkticsscouting2017.Activities.MatchScouting;
import frc3824.rohawkticsscouting2017.Activities.MatchView;
import frc3824.rohawkticsscouting2017.Activities.SuperScouting;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Match;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Views.ImageTextButton;

/**
 * @author frc3824
 *         Created: 10/10/16
 */
public class LVA_MatchList extends ArrayAdapter<Integer> {
    private final static String TAG = "LVA_MatchList";

    private ArrayList<Integer> mMatchNumbers;
    private Context mContext;
    private Database mDatabase;
    private MatchListType mMatchListType;

    private boolean mAdmin;
    private String mAllianceColor;
    private int mAllianceNumber;

    public enum MatchListType {
        MATCH_SCOUT,
        SUPER_SCOUT,
        MATCH_VIEW,
        DRIVE_TEAM_FEEDBACK
    }

    public LVA_MatchList(Context context, ArrayList<Integer> objects, MatchListType mlt) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mMatchNumbers = objects;
        mContext = context;
        mMatchListType = mlt;
        mDatabase = Database.getInstance();
        mAdmin = false;
    }


    public void setMatchScoutExtras(boolean admin, String color, int number) {
        mAdmin = admin;
        mAllianceColor = color;
        mAllianceNumber = number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (mAdmin) {
                convertView = inflater.inflate(R.layout.list_item_match_list_admin, null);
            } else {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            }
        }

        final int match_number = mMatchNumbers.get(position);

        if (mAdmin) {
            // Practice Match
            if (match_number == -1) {

                Button p = (Button)convertView.findViewById(R.id.practice_match);
                p.setVisibility(View.VISIBLE);
                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

                ImageTextButton m = (ImageTextButton) convertView.findViewById(R.id.match_number);
                m.setVisibility(View.GONE);

                LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.teams);
                ll.setVisibility(View.GONE);
            } else {
                Match match = mDatabase.getMatch(match_number);

                convertView.findViewById(R.id.practice_match).setVisibility(View.GONE);

                final ImageTextButton m = (ImageTextButton) convertView.findViewById(R.id.match_number);
                m.setText(String.format("Match %d", match_number));

                final LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.teams);
                ll.setVisibility(View.GONE);

                m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(m.getImageResourceId() == R.drawable.collapse_color) {
                            m.setImage(R.drawable.expand_color);
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            m.setImage(R.drawable.collapse_color);
                            ll.setVisibility(View.GONE);
                        }
                    }
                });


                Button b1 = (Button) convertView.findViewById(R.id.blue1);
                b1.setText(String.format("Blue 1: %d", match.teams.get(0)));
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor edit = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                        edit.putString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.BLUE);
                        edit.putInt(Constants.Settings.ALLIANCE_NUMBER, 1);
                        edit.commit();
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

                Button b2 = (Button) convertView.findViewById(R.id.blue2);
                b2.setText(String.format("Blue 2: %d", match.teams.get(1)));
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor edit = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                        edit.putString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.BLUE);
                        edit.putInt(Constants.Settings.ALLIANCE_NUMBER, 2);
                        edit.commit();
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

                Button b3 = (Button) convertView.findViewById(R.id.blue3);
                b3.setText(String.format("Blue 3: %d", match.teams.get(2)));
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor edit = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                        edit.putString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.BLUE);
                        edit.putInt(Constants.Settings.ALLIANCE_NUMBER, 3);
                        edit.commit();
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

                Button r1 = (Button) convertView.findViewById(R.id.red1);
                r1.setText(String.format("Red 1: %d", match.teams.get(3)));
                r1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor edit = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                        edit.putString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.RED);
                        edit.putInt(Constants.Settings.ALLIANCE_NUMBER, 1);
                        edit.commit();
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

                Button r2 = (Button) convertView.findViewById(R.id.red2);
                r2.setText(String.format("Red 2: %d", match.teams.get(4)));
                r2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor edit = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                        edit.putString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.RED);
                        edit.putInt(Constants.Settings.ALLIANCE_NUMBER, 2);
                        edit.commit();
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

                Button r3 = (Button) convertView.findViewById(R.id.red3);
                r3.setText(String.format("Red 3: %d", match.teams.get(5)));
                r3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor edit = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE).edit();
                        edit.putString(Constants.Settings.ALLIANCE_COLOR, Constants.Alliance_Colors.RED);
                        edit.putInt(Constants.Settings.ALLIANCE_NUMBER, 3);
                        edit.commit();
                        Intent intent = new Intent(mContext, MatchScouting.class);
                        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                        mContext.startActivity(intent);
                    }
                });

            }
        } else {
            TextView txt1 = (TextView) convertView.findViewById(android.R.id.text1);
            txt1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            switch (mMatchListType) {
                case MATCH_SCOUT:


                    // Practice Match
                    if (match_number == -1) {
                        convertView.setBackgroundColor(Color.GRAY);
                        txt1.setText("Practice Match");
                    } else {
                        Match match = mDatabase.getMatch(match_number);
                        int team_number = -1;
                        if (mAllianceColor.equals(Constants.Alliance_Colors.BLUE)) {
                            convertView.setBackgroundColor(Color.BLUE);
                            txt1.setTextColor(Color.WHITE);
                            team_number = match.teams.get(mAllianceNumber - 1);
                        } else {
                            convertView.setBackgroundColor(Color.RED);
                            team_number = match.teams.get(mAllianceNumber + 2);
                        }
                        txt1.setText(String.format("Match: %d - Team: %d", match_number, team_number));
                    }

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, MatchScouting.class);
                            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                            mContext.startActivity(intent);
                        }
                    });

                    break;
                case SUPER_SCOUT:
                    // Practice Match
                    if (match_number == -1) {
                        convertView.setBackgroundColor(Color.GRAY);
                        txt1.setText("Practice Match");
                    } else {
                        convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.navy_blue));
                        txt1.setTextColor(Color.WHITE);
                        txt1.setText(String.format("Match: %d", match_number));
                    }
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, SuperScouting.class);
                            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case MATCH_VIEW:
                    convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.navy_blue));
                    txt1.setTextColor(Color.WHITE);
                    txt1.setText(String.format("Match: %d", match_number));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, MatchView.class);
                            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case DRIVE_TEAM_FEEDBACK:
                    Match match = mDatabase.getMatch(match_number);
                    try {
                        if (match.isBlue(Constants.OUR_TEAM_NUMBER)) {
                            convertView.setBackgroundColor(Color.BLUE);
                        } else {
                            convertView.setBackgroundColor(Color.RED);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        convertView.setBackgroundColor(Color.GRAY);
                    }
                    txt1.setTextColor(Color.WHITE);
                    txt1.setText(String.format("Match: %d", match_number));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, DriveTeamFeedback.class);
                            intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
            }
        }

        return convertView;
    }
}
