package frc3824.rohawkticsscouting2017.Fragments.MatchPlanning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Adapters.FragmentPagerAdapters.FPA_MultiPlan;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;
import frc3824.rohawkticsscouting2017.Views.NonScrollableViewPager;
import frc3824.rohawkticsscouting2017.Views.StrategyDrawingView;

/**
 * @author frc3824
 * Created: 12/22/16
 */
public class MultiPlanFragment extends Fragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private final static String TAG = "MultiPlanFragment";

    private NonScrollableViewPager mViewPager;
    private TabLayout mTabLayout;
    private int numTabs;
    private ArrayList<Strategy> mStrategies;
    private int currentTab;
    private int previousTab;

    private StrategyDrawingView mDrawingView;
    private EditText mNotes;
    private float mExtraSmallBrush, mSmallBrush, mMediumBrush, mLargeBrush;
    private ArrayList<Integer> mTeams;

    Button removeButton;

    public MultiPlanFragment(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_multi_plan, container, false);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundColor(Color.BLUE);
        mTabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        mTabLayout.setSelectedTabIndicatorColor(Color.GREEN);
        mTabLayout.addOnTabSelectedListener(this);

        view.findViewById(R.id.add).setOnClickListener(this);
        removeButton = (Button)view.findViewById(R.id.remove);
        removeButton.setOnClickListener(this);

        mDrawingView = (StrategyDrawingView) view.findViewById(R.id.drawing);
        mNotes = (EditText)view.findViewById(R.id.notes);

        mExtraSmallBrush = 5;
        mSmallBrush = 10;
        mMediumBrush = 20;
        mLargeBrush = 30;

        view.findViewById(R.id.draw_btn).setOnClickListener(this);
        view.findViewById(R.id.erase_btn).setOnClickListener(this);
        view.findViewById(R.id.new_btn).setOnClickListener(this);

        mDrawingView.setBrushSize(mExtraSmallBrush);

        Integer[] teamArray = mTeams.toArray(new Integer[mTeams.size()]);
        mDrawingView.markTeams(teamArray, Constants.StrategyPlanning.Teleop.POSITIONS, Constants.StrategyPlanning.Teleop.RADIUS);

        mStrategies = new ArrayList<>();
        mStrategies.add(new Strategy());
        currentTab = 0;
        previousTab = 0;

        numTabs = 1;
        TabLayout.Tab tab = mTabLayout.newTab().setText(String.valueOf(numTabs));
        mTabLayout.addTab(tab);

        Utilities.setupUi(getActivity(), view);

        return view;
    }


    public void setMatch(ArrayList<Integer> teams){
        mTeams = teams;
    }

    public ArrayList<Strategy> save(){
        return mStrategies;
    }

    public void load(ArrayList<Strategy> strategies){
        mStrategies.clear();
        mStrategies = strategies;
        mTabLayout.removeAllTabs();
        for(numTabs = 1; numTabs <= mStrategies.size(); numTabs++){
            TabLayout.Tab tab = mTabLayout.newTab().setText(String.valueOf(numTabs));
            mTabLayout.addTab(tab);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                numTabs++;
                TabLayout.Tab tab = mTabLayout.newTab().setText(String.valueOf(numTabs));
                mTabLayout.addTab(tab);
                mStrategies.add(currentTab + 1, new Strategy());
                removeButton.setEnabled(true);
                break;
            case R.id.remove:
                numTabs--;
                mTabLayout.removeTabAt(numTabs);
                mStrategies.remove(currentTab);
                if(mStrategies.size() == 1){
                    removeButton.setEnabled(false);
                }
                break;
            case R.id.draw_btn:
                drawButtonClicked();
                break;
            case R.id.erase_btn:
                eraseButtonClicked();
                break;
            case R.id.new_btn:
                newButtonClicked();
                break;
        }
    }

    private void drawButtonClicked() {
        mDrawingView.setErase(false);
        final Dialog brushDialog = new Dialog(getContext());
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.dialog_brush_chooser);

        // extra small button sets the size of the brush to extra small
        ImageButton extraSmallBtn = (ImageButton) brushDialog.findViewById(R.id.extra_small_brush);
        extraSmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mExtraSmallBrush);
                mDrawingView.setLastBrushSize(mExtraSmallBrush);
                brushDialog.dismiss();
            }
        });

        // small button sets the size of the brush to small
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mSmallBrush);
                mDrawingView.setLastBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });

        // medium button sets the size of the brush to medium
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mMediumBrush);
                mDrawingView.setLastBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });

        // large button sets the size of the brush to large
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mLargeBrush);
                mDrawingView.setLastBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    private void newButtonClicked() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(getContext());
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new strategy (you will lose the current strategy)?");

        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDrawingView.startNew();
                dialog.dismiss();
            }
        });

        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        newDialog.show();
    }

    private void eraseButtonClicked() {
        final Dialog eraser_brushDialog = new Dialog(getContext());
        eraser_brushDialog.setTitle("Eraser size:");
        eraser_brushDialog.setContentView(R.layout.dialog_brush_chooser);

        // extra small button sets the size of the eraser brush to extra small
        ImageButton eraser_extraSmallBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.extra_small_brush);
        eraser_extraSmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mExtraSmallBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // small button sets the size of the eraser brush to small
        ImageButton eraser_smallBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.small_brush);
        eraser_smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mSmallBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // medium button sets the size of the eraser brush to medium
        ImageButton eraser_mediumBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.medium_brush);
        eraser_mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mMediumBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // large button sets the size of the eraser brush to large
        ImageButton eraser_largeBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.large_brush);
        eraser_largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mLargeBrush);
                eraser_brushDialog.dismiss();
            }
        });
        eraser_brushDialog.show();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab){

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        previousTab = currentTab;
        currentTab = mTabLayout.getSelectedTabPosition();

        Strategy previousStrategy = mStrategies.get(previousTab);
        previousStrategy.path_json = mDrawingView.toJson();
        previousStrategy.notes = mNotes.getText().toString();

        Strategy nextStrategy = mStrategies.get(currentTab);
        mDrawingView.load(nextStrategy.path_json);
        mNotes.setText(nextStrategy.notes);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab){

    }
}
